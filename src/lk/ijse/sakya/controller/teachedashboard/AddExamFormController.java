package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import lk.ijse.sakya.dto.*;
import lk.ijse.sakya.entity.custom.ExamStudent;
import lk.ijse.sakya.entity.custom.Module;



import lk.ijse.sakya.service.custom.ExamService;
import lk.ijse.sakya.service.custom.StudentService;
import lk.ijse.sakya.service.custom.impl.ExamServiceImpl;
import lk.ijse.sakya.service.custom.impl.StudentServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
//Done
public class AddExamFormController {
    public Label lblCourseName;
    public Label lblModuleName;
    public JFXDatePicker datePicker;
    public Label lblExamId;
    public JFXCheckBox checkBox1;
    private CourseTM course;
    private Module module;
    private ExamService examService;
    private StudentService studentService;

    public void initialize(){
        setNewExamId();
        examService = new ExamServiceImpl();
        studentService = new StudentServiceImpl();
    }

    public void setNewExamId(){
        try {
            lblExamId.setText(examService.getNewExamId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnCreateExamOnAction(ActionEvent actionEvent) {
        try {
            ArrayList<String> students = studentService.getStudentsByCourseId(course.getId());
            ArrayList<ExamStudent> examStudents = new ArrayList<>();

            for(String id : students){
                examStudents.add(new ExamStudent(id,lblExamId.getText(),-1));
            }
            lk.ijse.sakya.entity.custom.Exam exam = new lk.ijse.sakya.entity.custom.Exam(lblExamId.getText(),course.getId(),module.getId(),String.valueOf(datePicker
                    .getValue()),
                    examStudents);

            boolean b = examService.addExam(exam);
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
