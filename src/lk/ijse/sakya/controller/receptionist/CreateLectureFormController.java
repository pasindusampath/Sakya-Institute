package lk.ijse.sakya.controller.receptionist;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import lk.ijse.sakya.dto.CourseTM;


import lk.ijse.sakya.entity.custom.Attendence;



import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.service.custom.LectureService;
import lk.ijse.sakya.service.custom.StudentService;
import lk.ijse.sakya.service.custom.impl.CourseServiceImpl;
import lk.ijse.sakya.service.custom.impl.LectureServiceImpl;
import lk.ijse.sakya.service.custom.impl.StudentServiceImpl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
//Done
public class CreateLectureFormController {
    public TableView tblCourseDetails;
    public TableColumn colClassId;
    public TableColumn colTeacherName;
    public TableColumn colSubject;
    public TableColumn colGrade;
    public JFXTextField txtClassId;
    public JFXDatePicker datePicker;
    public Label lblLectureID;
    public JFXRadioButton rdClassId;
    public JFXRadioButton rdTeacherId;
    public JFXRadioButton rdSubjectName;
    public JFXTextField txtSearch;
    private CourseTM selectedCourse;
    private LectureService lectureService;
    private CourseService courseService;
    private StudentService studentService;

    public void initialize(){
        courseService = new CourseServiceImpl();
        lectureService = new LectureServiceImpl();
        studentService = new StudentServiceImpl();
        setCourseDetailsTable();
        setNewLectureId();
        datePicker.setValue(LocalDate.now());
    }

    public void btnCreateLectureOnAction(ActionEvent actionEvent) {
        String id = lblLectureID.getText();
        String date = String.valueOf(datePicker.getValue());
        String cId = txtClassId.getText();
        try {
            if(lectureService.isLectureAlreadyAdded(cId,date)){
               new Alert(Alert.AlertType.ERROR,"Lecture Already Added").show();
               return;
            }
            ArrayList<String> sIdList =studentService.getStudentsByCourseId(cId);
            ArrayList<Attendence> atList = new ArrayList<>();
            for(String sid : sIdList){
                atList.add(new Attendence(id,sid,"AB"));
            }
            boolean flag = lectureService.addLecture(new lk.ijse.sakya.entity.custom.Lecture(id, date, cId),atList);
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"Lecture Created").show();
                setNewLectureId();
                selectedCourse = null;
                tblCourseDetails.getSelectionModel().select(null);
                txtClassId.clear();
            }else{
                new Alert(Alert.AlertType.ERROR,"Lecture Not Created").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Lecture Not Created Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Lecture Not Created Driver Error").show();
        }
    }

    public void setNewLectureId(){
        try {
            lblLectureID.setText(lectureService.getNewLectureId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCourseDetailsTable(){
        colClassId.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("id"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("teacherId"));
        colSubject.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("name"));
        colGrade.setCellValueFactory(new PropertyValueFactory<CourseTM,Integer>("grade"));
        try {
            tblCourseDetails.setItems(courseService.getCourseDetails());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Driver Error").show();;
        }
    }

    public void tblCourseDetailsOnMouseClickAction(MouseEvent mouseEvent) {
        selectedCourse =(CourseTM)tblCourseDetails.getSelectionModel().getSelectedItem();
        if(selectedCourse==null){
            return;
        }
        txtClassId.setText(selectedCourse.getId());
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        if(rdClassId.isSelected()){
            search("c.id");
        }
        if(rdTeacherId.isSelected()){
            search("u.name");
        }
        if(rdSubjectName.isSelected()){
            search("s.name");
        }
    }
    public void search(String searchBy){
        try {
            ObservableList<CourseTM> courseDetails = courseService.getCourseDetails(searchBy,
                    txtSearch.getText());
            tblCourseDetails.setItems(courseDetails);;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Driver Error").show();;
        }
    }
}
