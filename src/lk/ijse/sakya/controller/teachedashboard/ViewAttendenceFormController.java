package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.DateAttendenceTM;
import lk.ijse.sakya.dto.Student;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.interfaces.DashBoard;
import lk.ijse.sakya.model.AttendenceController;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.StudentController;
import lk.ijse.sakya.util.DashBoardNavigation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewAttendenceFormController implements DashBoard {

    public AnchorPane viewAttendenceContext;
    public JFXComboBox cbClasses;
    public TableView tblStudent;
    public TableColumn colStudentId;
    public TableColumn colStudentName;
    public TableView tblDateStatus;
    public TableColumn colDate;
    public TableColumn colStatus;
    private User user;
    private CourseTM course;

    public void initialize(){

    }

    public void btnViewByDateOnAction(ActionEvent actionEvent) throws IOException {
        /*Parent load = FXMLLoader.load(getClass().getResource("../../view/teacherdashboard/ViewAttendenceForm1.fxml"));
        viewAttendenceContext.getChildren().clear();
        viewAttendenceContext.getChildren().add(load);*/
        DashBoardNavigation.setUi("teacherdashboard",viewAttendenceContext,"ViewAttendenceForm1",user);
        load();
    }

    public void load(){
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a, b, c)->{
            viewAttendenceContext.getChildren().add(c);
        });
        instance.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,nw).show();
        });
        Thread t1 = new Thread(instance);
        t1.start();
    }

    public void setClassesComboBox(String userId){
        cbClasses.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                CourseTM co = (CourseTM) object;
                return co.getCourseName();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            ObservableList<CourseTM> courses = CourseController.getCoursesByTeacherId(userId);
            for(CourseTM co : courses){
                co.setCourseName(co.getYear()+" - "+co.getGrade()+" - "+co.getName());
            }
            cbClasses.setItems(courses);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setStudentTable(CourseTM course){
        colStudentId.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        try {
            ArrayList<Student> list= StudentController.getStudentsByCourseId(course);
            tblStudent.setItems(FXCollections.observableArrayList(list));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }



    public void cbClassesOnAction(ActionEvent actionEvent) {
        course =(CourseTM) cbClasses.getSelectionModel().getSelectedItem();
        if(course==null){
            return;
        }
        setStudentTable(course);

    }


    public void tblStudentsOnAction(MouseEvent mouseEvent) {
        Student student = (Student) tblStudent.getSelectionModel().getSelectedItem();
        if(student==null){
            return;
        }
        setAttendenceTable(student.getId(),course.getId());
    }

    public void setAttendenceTable(String studentId,String courseId){
        colDate.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,String>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,String>("status"));
        try {
            tblDateStatus.setItems(AttendenceController.getAllAttendenceByStudentAndCourse(studentId,courseId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLoggedUser(User user) {
        this.user = user;
        setClassesComboBox(user.getId());
    }
}
