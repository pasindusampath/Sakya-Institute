package lk.ijse.sakya.controller.receptionist;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.*;
import lk.ijse.sakya.interfaces.DashBoard;
import lk.ijse.sakya.interfaces.QrPerformance;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.PaymentController;
import lk.ijse.sakya.model.StudentController;
import lk.ijse.sakya.thread.LoadQrUiTask;
import lk.ijse.sakya.thread.PrintBillTask;
import lk.ijse.sakya.thread.SendMail;


import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;

import java.util.HashMap;


public class MakePaymentFormController implements QrPerformance, DashBoard {
    public JFXComboBox cbClasses;
    public JFXTextField txtAmount;
    public Label lblTotal;
    public AnchorPane billContext;
    public JFXTextField txtReciptionistName;
    public Label invoiceNo;
    public JFXProgressBar progress;
    public JFXButton btnQr;
    private Student student = null;
    public Label lblDate;
    public Label lblTime;
    public TableColumn colAmount;
    public TableColumn colClassName;
    public TableView tblCoursePayment;
    public Label lblStudentName;
    public Label lblStudentId;
    public JFXTextField txtStudentId;
    private CourseTM selectedItem;
    private User user;

    public void initialize() {
        setBillTable();
        setNewInvoiceId();
        setDate();
        progress.setVisible(false);
    }

    public void setNewInvoiceId(){
        try {
            invoiceNo.setText(PaymentController.getNewInvoiceId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setDate(){
        lblDate.setText(String.valueOf(LocalDate.now()));
    }

    @Override
    public void qrIdRequestAction(String id) {
        if (student != null) {
            lblStudentId.setText(id);
            lblStudentName.setText(student.getName());
            txtStudentId.setText(id);
            setClassesComboBox(id);
        }


    }

    @Override
    public String getStudentDetail(String id) {

        try {
            student = StudentController.searchStudent(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return student.getName();
    }

    public void btnQrOnAction(ActionEvent actionEvent) {
        btnQr.setDisable(true);
        Stage stage= new Stage();
        LoadQrUiTask tsk = new LoadQrUiTask(this,lblTotal.getScene().getWindow(),stage);
        tsk.valueProperty().addListener((a,b,c)->{
            stage.setScene(c);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lblTotal.getScene().getWindow());
            stage.showAndWait();
            btnQr.setDisable(false);
        });
        tsk.messageProperty().addListener((a,b,c)->{
            System.out.println(c);
            btnQr.setDisable(false);
        });
        Thread t = new Thread(tsk);
        t.setDaemon(false);
        t.start();

    }

    public void setClassesComboBox(String stId) {
        cbClasses.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                CourseTM course = (CourseTM) object;
                return course.getYear() + "-" + course.getGrade() + "-" + course.getName()
                        + "-" + course.getTeacherId();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            cbClasses.setItems(CourseController.getCourseDetailsByStudentId(stId));

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setBillTable() {
        colAmount.setCellValueFactory(new PropertyValueFactory<PaymentTM,
                Double>("amount"));
        colClassName.setCellValueFactory(new PropertyValueFactory<PaymentTM, String
                >("courseName"));
    }

    public void cbSelectClassOnAction(ActionEvent actionEvent) {
        selectedItem = (CourseTM) cbClasses.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            return;
        }
        txtAmount.setText(String.valueOf(selectedItem.getFee()));
    }

    public void btnAddToBillOnAction(ActionEvent actionEvent) {
        if (selectedItem == null) {
            new Alert(Alert.AlertType.ERROR, "Select Course From ComboBox").show();
            return;
        }
        Payment payment = new Payment(student.getId(),selectedItem.getId(),LocalDate.now().getMonthValue());
        try {
            if(PaymentController.isAlreadyPaid(payment)){
                new Alert(Alert.AlertType.ERROR, "Student Already Paid For This Course For "+LocalDate.now().getMonthValue()
                        +"Month").show();
                return;
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Search Course Error - Database Error").show();
            return;
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Search Course Error - Driver Error").show();
            return;
        }

        String s = selectedItem.getYear() + "-" + selectedItem.getGrade() + "-" + selectedItem.getName()
                + "-" + selectedItem.getTeacherId();
        ObservableList<PaymentTM> items = tblCoursePayment.getItems();
        for (PaymentTM ob : items) {
            if (ob.getCourseName().equals(s)) {
                return;
            }
        }
        items.add(new PaymentTM(selectedItem.getId(),s, Double.parseDouble(txtAmount.getText())));

        if (lblTotal.getText().equals("")) {
            lblTotal.setText(txtAmount.getText());
        } else {
            double v = Double.parseDouble(txtAmount.getText()) + Double.parseDouble(lblTotal.getText());
            String total = String.valueOf(v);
            lblTotal.setText(total);
        }

    }

    public void btnPayOnAction(ActionEvent actionEvent) {
        ObservableList<PaymentTM> items = tblCoursePayment.getItems();
        ArrayList<Payment> list = new ArrayList<>();
        for (PaymentTM ob : items) {
            list.add(new Payment(student.getId(), ob.getCourseId(), LocalDate.now().getMonthValue()
                    , String.valueOf(LocalDate.now()), ob.getAmount(), invoiceNo.getText()));
        }
        try {
            if (PaymentController.addPaymentRecords(list)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Complete").show();
                printBill();
                WritableImage image = billContext.snapshot(new SnapshotParameters(), null);
                File file = new File("G:\\StudentPayments\\" + list.get(0).getStudentId() + list.get(0).getDate() + ".png");
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } else {
                new Alert(Alert.AlertType.ERROR, "Payment Failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Payment Failed - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Payment Failed - Driver Error").show();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Bill Generating Failed - Driver Error").show();
        }
    }

    @Override
    public void setLoggedUser(User user) {
        this.user = user;
        txtReciptionistName.setText(user.getName());
    }

    public void printBill(){
        String billPath = "G:\\IJSE\\GDSE 63\\DBP\\FinalProject\\Sakya Institute\\src\\lk\\ijse\\sakya\\report\\Sakya.jrxml";
        String sql="SELECT  c.year as year ,s.name as sub_name,s.grade,u.name as teacherName, sp.month , sp.amount as fee FROM student_payment sp  inner join course c on sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on c.teacherId = u.id WHERE invoice_nu= '"+invoiceNo.getText()+"'";
        String path = "G:\\StudentPayments\\"+txtStudentId.getText()+invoiceNo.getText()+".pdf";
        HashMap<String, Object> para=new HashMap<>();
        para.put("cashierName",user.getName());
        para.put("studentId",txtStudentId.getText());
        para.put("invoiceNo",invoiceNo.getText());
        para.put("total",lblTotal.getText());
        PrintBillTask ob = new PrintBillTask(billPath,sql,para,path);
        ob.valueProperty().addListener((a,b,c)->{
            if(c!=null){
                progress.progressProperty().unbind();
                progress.setVisible(false);
                SendMail mail = new SendMail(student.getGmail(),"Payment Success","Payment Done",c);
                Thread t1 = new Thread(mail);
                t1.start();
            }
        });
        progress.progressProperty().bind(ob.progressProperty());
        progress.setVisible(true);
        Thread t2 = new Thread(ob);
        t2.start();

    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtStudentId.clear();
        cbClasses.getItems().clear();
        txtAmount.clear();
        setNewInvoiceId();
        lblStudentId.setText("");
        lblStudentId.setText("");
        lblTotal.setText("");
        lblStudentName.setText("");
        tblCoursePayment.getItems().clear();
        tblCoursePayment.refresh();
    }

    public void btnOkOnAction(ActionEvent actionEvent) {
        getStudentDetail(txtStudentId.getText());
        qrIdRequestAction(txtStudentId.getText());
    }
}
