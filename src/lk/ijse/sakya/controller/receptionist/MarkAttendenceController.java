package lk.ijse.sakya.controller.receptionist;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lk.ijse.sakya.controller.ServerController;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Attendence;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.LectureTM;
import lk.ijse.sakya.interfaces.MobileQrPerformance;
import lk.ijse.sakya.interfaces.QrPerformance;
import lk.ijse.sakya.model.AttendenceController;
import lk.ijse.sakya.model.LectureController;
import lk.ijse.sakya.thread.LoadQrUiTask;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class MarkAttendenceController implements MobileQrPerformance {
    public JFXButton btnOK;
    public TableView tblLectureId;
    public TableColumn colLectureId;
    public TableColumn colClassName;
    public TableColumn colTeacherName;
    public TableView tblAttendence;
    public TableColumn colStudentID;
    public TableColumn colStudentName;
    public TableColumn colStatus;
    public TableColumn colPayment;
    public JFXRadioButton rdButton1;
    public JFXRadioButton rdButton2;
    public JFXButton btnSaveRecord;
    public Label lblStudentId;
    public JFXButton btnQr;
    private AttendenceTM selectedItem;
    private LectureTM selectedLecture;

    public void initialize(){
        setLectureTable();
    }

    public void btnCreateLectureOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/reciptionist" +
                    "/CreateLectureForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnOK.getScene().getWindow());
        stage.showAndWait();
        setLectureTable();
    }

    public void setLectureTable(){
        colLectureId.setCellValueFactory(new PropertyValueFactory<LectureTM,String>("id"));
        colClassName.setCellValueFactory(new PropertyValueFactory<LectureTM,String>("name"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<LectureTM,String>("teacherName"));

        try {
            tblLectureId.setItems(LectureController.getLecturesByDate(String.valueOf(LocalDate.now())));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

     public void setAttendencTable(LectureTM selectedLecture){
        colStudentID.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("status"));
        colPayment.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("payment"));


        try {
            tblAttendence.setItems(AttendenceController.getAllAttendenceByLectureId(selectedLecture.getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void tblLectureIdOnMouseClickAction(MouseEvent mouseEvent) {
        selectedLecture = (LectureTM) tblLectureId.getSelectionModel().getSelectedItem();
        if(selectedLecture==null){
            return;
        }
        setAttendencTable(selectedLecture);
        selectedItem=null;
    }

    public void tblAttendenceMouseClickOnAction(MouseEvent mouseEvent) {
        selectedItem =(AttendenceTM) tblAttendence.getSelectionModel().getSelectedItem();
        //System.out.println(selectedItem.getStatus());
        if(selectedItem == null){
            return;
        }
        lblStudentId.setText(selectedItem.getStudentId());

        if(selectedItem.getStatus().equals("AB")){
            rdButton2.setSelected(true);
        }else{
            rdButton1.setSelected(true);
        }
    }

    public void btnOkONAction(ActionEvent actionEvent) {
        if(selectedItem==null){
            new Alert(Alert.AlertType.ERROR,"Select Student To Mark Attendence").show();
            return;
        }
        if(rdButton1.isSelected()){
            selectedItem.setStatus("P");
        }else{
            selectedItem.setStatus("AB");
        }
        tblAttendence.refresh();
        lblStudentId.setText("");
        tblAttendence.getSelectionModel().select(null);
        selectedItem=null;
    }

    public void qrIdRequestAction(String id){
        //System.out.println(id+"From Mark");
        ObservableList<AttendenceTM> items = tblAttendence.getItems();
        for(AttendenceTM ob : items){
            if(ob.getStudentId().equals(id)){
                ob.setStatus("P");
                tblAttendence.refresh();
            }
        }
    }

    @Override
    public String getStudentDetail(String id) {
        ObservableList<AttendenceTM> items = tblAttendence.getItems();
        for(AttendenceTM ob : items){
            if(ob.getStudentId().equals(id)){
                return ob.getName().trim()+" - Payment "+ob.getPayment();
            }
        }
        return null;
    }

    public void btnSaveRecordOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ObservableList<AttendenceTM> ob = tblAttendence.getItems();
        ArrayList<Attendence> list = new ArrayList<>();
        for(AttendenceTM obj : ob){
            list.add(new Attendence(selectedLecture.getId(),obj.getStudentId(), obj.getStatus()));
        }
       try {
            boolean flag = AttendenceController.updateAttendence(list);
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"Attendence Mark Compleate").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Attendence Mark Failed").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
           DBConnection.getInstance().getConnection().setAutoCommit(true);
       }
    }

    public void btnQrOnAction(ActionEvent actionEvent) {
        btnQr.setDisable(true);
        Stage stage= new Stage();
        LoadQrUiTask tsk = new LoadQrUiTask(this,lblStudentId.getScene().getWindow(),stage);
        tsk.valueProperty().addListener((a,b,c)->{
            stage.setScene(c);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(lblStudentId.getScene().getWindow());
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
        /*Stage stage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../view/reciptionist/QrScannerForm.fxml"));

            Parent load = loader.load();
            QrScannerFormController controller = loader.getController();
            controller.setController(this);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    controller.btnStopOnAction(null);
                }
            });

            stage.setScene(new Scene(load));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setX(200);
        stage.setY(230);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(btnOK.getScene().getWindow());
        stage.showAndWait();
        setLectureTable();*/

    }

    public void btnMobileOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Server.fxml"));
        try {
            Parent load = loader.load();
            ServerController controller = loader.getController();
            stage.setScene(new Scene(load));
            controller.setController(this);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    controller.closeConnections();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getStudentDetails(String id) {
        return getStudentDetail(id);
    }

    @Override
    public void onClientDataReceived(String id) {
        qrIdRequestAction(id);
    }
}
