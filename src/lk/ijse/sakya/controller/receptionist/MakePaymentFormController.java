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
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.interfaces.DashBoard;
import lk.ijse.sakya.service.interfaces.QrPerformance;



import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.service.custom.PaymentService;
import lk.ijse.sakya.service.custom.PrintBillService;
import lk.ijse.sakya.service.custom.StudentService;
import lk.ijse.sakya.service.custom.impl.CourseServiceImpl;
import lk.ijse.sakya.service.custom.impl.PaymentServiceImpl;
import lk.ijse.sakya.service.custom.impl.PrintBillServiceImpl;
import lk.ijse.sakya.service.custom.impl.StudentServiceImpl;
import lk.ijse.sakya.thread.LoadQrUiTask;


import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.time.LocalDate;

import java.util.ArrayList;

import java.util.HashMap;

//Done
public class MakePaymentFormController implements QrPerformance, DashBoard {
    public JFXComboBox cbClasses;
    public JFXTextField txtAmount;
    public Label lblTotal;
    public AnchorPane billContext;
    public JFXTextField txtReceptionistName;
    public Label invoiceNo;
    public JFXProgressBar progress;
    public JFXButton btnQr;
    private Student student = null;
    public Label lblDate;
    public TableColumn colAmount;
    public TableColumn colClassName;
    public TableView tblCoursePayment;
    public Label lblStudentName;
    public Label lblStudentId;
    public JFXTextField txtStudentId;
    private CourseTM selectedItem;
    private User user;
    private PaymentService paymentService;
    private StudentService studentService;
    private CourseService courseService;
    private PrintBillService printBillService;

    public void initialize() {
        courseService= new CourseServiceImpl();
        paymentService=new PaymentServiceImpl();
        studentService=new StudentServiceImpl();
        printBillService = new PrintBillServiceImpl();
        setBillTable();
        setNewInvoiceId();
        setDate();
        progress.setVisible(false);
    }

    public void setNewInvoiceId(){
        try {
            invoiceNo.setText(paymentService.getNewInvoiceId());
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
            lk.ijse.sakya.entity.custom.Student student = studentService.searchStudent(id);
            this.student=new Student(student.getId(),student.getName(),student.getDob(),student.getAddress(),student.getContact()
            ,student.getGmail(),student.getP_gmail(),student.getP_contact());
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
            cbClasses.setItems(courseService.getCourseDetailsByStudentId(stId));

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
        lk.ijse.sakya.entity.custom.Payment payment = new lk.ijse.sakya.entity.custom.Payment(student.getId(),selectedItem.getId(),LocalDate.now().getMonthValue());
        try {
            if(paymentService.isAlreadyPaid(payment)){
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
        ArrayList<lk.ijse.sakya.entity.custom.Payment> list = new ArrayList<>();
        for (PaymentTM ob : items) {
            list.add(new lk.ijse.sakya.entity.custom.Payment(student.getId(), ob.getCourseId(), LocalDate.now().getMonthValue()
                    , String.valueOf(LocalDate.now()), ob.getAmount(), invoiceNo.getText()));
        }
        try {
            if (paymentService.addPaymentRecords(list)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment Complete").show();
                //-----------------------------------------------------------------------------------------------------------
                lk.ijse.sakya.entity.custom.Student student=new lk.ijse.sakya.entity.custom.Student(this.student.getId(),
                        this.student.getName(),this.student.getDob(),this.student.getAddress(),this.student.getContact(),
                        this.student.getGmail(),this.student.getP_gmail(),this.student.getP_contact());
                lk.ijse.sakya.entity.custom.User user1 = new lk.ijse.sakya.entity.custom.User(user.getId(), user.getName(),
                        user.getType(), user.getGmail(), user.getContact(), user.getPassword(), user.getDob(), user.getAddress());

                //-----------------------------------------------------------------------------------------------------------


                paymentService.printBill(user1,txtStudentId,invoiceNo,lblTotal,progress,student);



                WritableImage image = billContext.snapshot(new SnapshotParameters(), null);
                String path = FileSystems.getDefault().getPath("StudentPayments\\" + list.get(0).getStudentId() +
                        list.get(0).getDate() + ".png").toAbsolutePath().toString();
                File file = new File(path);
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
        txtReceptionistName.setText(user.getName());
    }

    public void printBill(){
        String billPath = FileSystems.getDefault().getPath("src/lk/ijse/sakya/report/Sakya.jrxml").toAbsolutePath().toString();
        String sql="SELECT  c.year as year ,s.name as sub_name,s.grade,u.name as teacherName, sp.month , sp.amount as" +
                " fee FROM student_payment sp  inner join course c on sp.c_id = c.id inner join subject s on c.sub_id" +
                " = s.id inner join user u on c.teacherId = u.id WHERE invoice_nu= '"+invoiceNo.getText()+"'";
        String path = FileSystems.getDefault().getPath("StudentPayments\\"+txtStudentId.getText()+invoiceNo.getText
                ()+".pdf").toAbsolutePath().toString();
        HashMap<String, Object> para=new HashMap<>();
        para.put("cashierName",user.getName());
        para.put("studentId",txtStudentId.getText());
        para.put("invoiceNo",invoiceNo.getText());
        para.put("total",lblTotal.getText());


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
