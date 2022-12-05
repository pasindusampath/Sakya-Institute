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
import lk.ijse.sakya.dto.Attendence;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.Lecture;
import lk.ijse.sakya.dto.StudentCourse;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.LectureController;
import lk.ijse.sakya.model.StudentController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public void initialize(){
        setCourseDetailsTable();
        setNewLectureId();
        datePicker.setValue(LocalDate.now());
    }

    public void btnCreateLectureOnAction(ActionEvent actionEvent) {
        String id = lblLectureID.getText();
        String date = String.valueOf(datePicker.getValue());
        String cId = txtClassId.getText();
        try {
            if(LectureController.isLectureAlreadyAdded(cId,date)){
               new Alert(Alert.AlertType.ERROR,"Lecture Already Added").show();
               return;
            }
            ArrayList<String> sIdList = StudentController.getStudentsByCourseId(cId);
            ArrayList<Attendence> atList = new ArrayList<>();
            for(String sid : sIdList){
                atList.add(new Attendence(id,sid,"AB"));
            }
            boolean flag = LectureController.addLecture(new Lecture(id, date, cId),atList);
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
            lblLectureID.setText(LectureController.getNewLectureId());
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
            tblCourseDetails.setItems(CourseController.getCourseDetails());
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
            ObservableList<CourseTM> courseDetails = CourseController.getCourseDetails(searchBy,
                    txtSearch.getText());
            tblCourseDetails.setItems(courseDetails);;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Course Details ERROR -  Driver Error").show();;
        }
    }
}
