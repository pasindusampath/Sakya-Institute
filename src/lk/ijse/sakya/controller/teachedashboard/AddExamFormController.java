package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.sakya.dto.*;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.ExamController;
import lk.ijse.sakya.model.StudentController;

import java.sql.SQLException;
import java.util.ArrayList;

public class AddExamFormController {
    public Label lblCourseName;
    public Label lblModuleName;
    public JFXDatePicker datePicker;
    public Label lblExamId;
    public JFXCheckBox checkBox1;
    private CourseTM course;
    private Module module;

    public void initialize(){
        setNewExamId();
    }

    public void setNewExamId(){
        try {
            lblExamId.setText(ExamController.getNewExamId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnCreateExamOnAction(ActionEvent actionEvent) {
        try {
            ArrayList<String> students = StudentController.getStudentsByCourseId(course.getId());
            ArrayList<ExamStudent> examStudents = new ArrayList<>();

            for(String id : students){
                examStudents.add(new ExamStudent(id,lblExamId.getText(),-1));
            }
            Exam exam = new Exam(lblExamId.getText(),course.getId(),module.getId(),String.valueOf(datePicker
                    .getValue()),
                    examStudents);

            boolean b = ExamController.addExam(exam);
            if(b){
                new Alert(Alert.AlertType.INFORMATION,"Exam Create Success").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Exam Create Fail").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void setCourseAndModule(CourseTM course, Module module){

        this.course = course;
        this.module = module;
        lblCourseName.setText(course.getCourseName());
        lblModuleName.setText(module.getName());
    }
}
