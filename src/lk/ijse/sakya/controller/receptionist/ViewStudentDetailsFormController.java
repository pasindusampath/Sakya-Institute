package lk.ijse.sakya.controller.receptionist;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.DateAttendenceTM;
import lk.ijse.sakya.dto.ExamResultTM;

import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.service.interfaces.QrPerformance;
import lk.ijse.sakya.service.custom.AttendenceService;
import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.service.custom.ExamStudentService;
import lk.ijse.sakya.service.custom.StudentService;
import lk.ijse.sakya.service.custom.impl.AttendenceServiceImpl;
import lk.ijse.sakya.service.custom.impl.CourseServiceImpl;
import lk.ijse.sakya.service.custom.impl.ExamStudentServiceImpl;
import lk.ijse.sakya.service.custom.impl.StudentServiceImpl;
import lk.ijse.sakya.thread.LoadQrUiTask;

import java.sql.SQLException;

public class ViewStudentDetailsFormController implements QrPerformance {
    public TableView tblClasses;
    public TableColumn colClassName;
    public TableColumn colTeacher;
    public TableColumn colDate;
    public TableColumn colStatus;
    public JFXTextField txtStudentId;
    public Label lblStudentName;
    public TableView tblAttendence;
    public TableView tblExam;
    public TableColumn collExamId;
    public TableColumn colModuleName;
    public TableColumn colResult;
    public JFXButton btnQr;
    private Student student;
    private StudentService studentService;
    private CourseService courseService;
    private AttendenceService attendenceService;
    private ExamStudentService examStudentService;

    public void initialize(){
        studentService = new StudentServiceImpl();
        courseService=new CourseServiceImpl();
        attendenceService=new AttendenceServiceImpl();
        examStudentService = new ExamStudentServiceImpl();
    }


    public void tblClassOnMouseClickAction(MouseEvent mouseEvent) {
        CourseTM selectedItem =(CourseTM) tblClasses.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            return;
        }
        setAttendenceTable(student.getId(),selectedItem.getId());
        setExamTable(student.getId(),selectedItem.getId());
    }

    public void btnQrOnAction(ActionEvent actionEvent) {
        btnQr.setDisable(true);
        Stage stage= new Stage();
        LoadQrUiTask tsk = new LoadQrUiTask(this,btnQr.getScene().getWindow(),stage);
        tsk.valueProperty().addListener((a,b,c)->{
            stage.setScene(c);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnQr.getScene().getWindow());
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

    @Override
    public void qrIdRequestAction(String id) {
        if(student!=null){
            setClassDetailsTable(id);
            txtStudentId.setText(id);
            lblStudentName.setText(student.getName());
        }

    }

    @Override
    public String getStudentDetail(String id) {
        try {
            lk.ijse.sakya.entity.custom.Student student = studentService.searchStudent(id);
            this.student = new Student(student.getId(),student.getName(),student.getDob(),student.getAddress(),
                    student.getContact(),student.getGmail(),student.getP_gmail(),student.getP_contact());
            if(this.student !=null){
                return this.student.getName();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setClassDetailsTable(String stId){
        colClassName.setCellValueFactory(new PropertyValueFactory<CourseTM,String >("courseName"));
        colTeacher.setCellValueFactory(new PropertyValueFactory<CourseTM,String >("teacherId"));

            try {
                ObservableList<CourseTM> courseList = courseService.getCourseDetailsByStudentId(stId);
                for(CourseTM ob : courseList){
                    ob.setCourseName(ob.getYear()+"-"+ob.getGrade()+"-"+ob.getName());
                }
                tblClasses.setItems(courseList);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

    }

    public void setAttendenceTable(String stId,String cId){
        colDate.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,String>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,String>("status"));
        try {
            tblAttendence.setItems(attendenceService.getAllAttendenceByStudentAndCourse(stId,cId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setExamTable(String stId,String cId){
        collExamId.setCellValueFactory(new PropertyValueFactory<ExamResultTM,String>("id"));
        colModuleName.setCellValueFactory(new PropertyValueFactory<ExamResultTM,String >("name"));
        colResult.setCellValueFactory(new PropertyValueFactory<ExamResultTM,JFXTextField>("txtMark"));

        try {
            tblExam.setItems(examStudentService.getExamResultsByStudentIdAndCourseId(cId,stId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        getStudentDetail(txtStudentId.getText());
        if(student==null){
            new Alert(Alert.AlertType.ERROR,"Student Not Found").show();
            return;
        }
        if(txtStudentId.getText().equals(student.getId())) {
            qrIdRequestAction(student.getId());
        }else {
            new Alert(Alert.AlertType.ERROR,"Student Not Found").show();
        }
    }
}
