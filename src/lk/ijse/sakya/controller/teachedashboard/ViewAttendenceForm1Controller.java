package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;



import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.DateAttendenceTM;

import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.interfaces.DashBoard;
import lk.ijse.sakya.service.custom.AttendenceService;
import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.service.custom.impl.AttendenceServiceImpl;
import lk.ijse.sakya.service.custom.impl.CourseServiceImpl;
import lk.ijse.sakya.util.DashBoardNavigation;

import java.io.IOException;
import java.sql.SQLException;

public class ViewAttendenceForm1Controller implements DashBoard {
    public AnchorPane viewAttendenceContext;
    public JFXComboBox cbClasses;
    public TableView tblStudentDetail;
    public TableColumn colStudentId;
    public TableColumn colStudentName;
    public TableColumn colStatus;
    public TableView tblDateCount;
    public TableColumn colDate;
    public TableColumn colCount;
    private User user;
    private CourseTM course;

    private CourseService courseService;
    private AttendenceService attendenceService;

    public void initialize(){
        courseService = new CourseServiceImpl();
        attendenceService = new AttendenceServiceImpl();
    }

    public void btnViewByStudentOnAction(ActionEvent actionEvent) throws IOException {
        DashBoardNavigation.setUi("teacherdashboard",viewAttendenceContext,"ViewAttendenceForm",user);
        DashBoardNavigation.load();
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
            ObservableList<CourseTM> courses = courseService.getCoursesByTeacherId(userId);
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

    public void setDateCountTable(){
        colDate.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,String>("date"));
        colCount.setCellValueFactory(new PropertyValueFactory<DateAttendenceTM,Integer>("count"));
        try {
            tblDateCount.setItems(FXCollections.observableArrayList(
                    attendenceService.getDatesAndCountOfPresentByCourseId(course.getId())));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cbClassesOnAction(ActionEvent actionEvent) {
        course = (CourseTM)cbClasses.getSelectionModel().getSelectedItem();
        if(course==null){
            return;
        }
        setDateCountTable();
        tblStudentDetail.getItems().clear();
    }

    public void tblDateCountOnMouseClickAction(MouseEvent mouseEvent) {
        DateAttendenceTM ob =(DateAttendenceTM) tblDateCount.getSelectionModel().getSelectedItem();
        if(ob==null){
            return;
        }
        setStudentStatusTable(course.getId(),ob.getDate());
    }

    public void setStudentStatusTable(String cId,String date){
        colStudentId.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("name"));
        colStatus.setCellValueFactory(new PropertyValueFactory<AttendenceTM,String>("status"));
        try {
            tblStudentDetail.setItems(attendenceService.getAttendenceByCourseIdAndDate(cId,date));
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
