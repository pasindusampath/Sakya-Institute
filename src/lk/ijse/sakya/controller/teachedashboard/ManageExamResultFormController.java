package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.*;
import lk.ijse.sakya.entity.custom.Exam;
import lk.ijse.sakya.entity.custom.Module;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.interfaces.DashBoard;

import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.service.custom.ExamService;
import lk.ijse.sakya.service.custom.ExamStudentService;
import lk.ijse.sakya.service.custom.ModuleService;
import lk.ijse.sakya.service.custom.impl.CourseServiceImpl;
import lk.ijse.sakya.service.custom.impl.ExamServiceImpl;
import lk.ijse.sakya.service.custom.impl.ExamStudentServiceImpl;
import lk.ijse.sakya.service.custom.impl.ModuleServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageExamResultFormController implements DashBoard {
    public TableView tblStudentResults;
    public TableColumn colStudentId;
    public TableColumn colStudentName;
    public TableColumn colMark;
    public JFXComboBox cbClasses;
    public JFXComboBox cbModules;
    public JFXComboBox cbExams;
    public JFXButton btnLoadExamDetails;
    private User user;
    private CourseTM selectedItem;
    private Module selectedModule;
    private Exam selectedExam;
    private CourseService courseService;
    private ModuleService moduleService;
    private ExamService examService;
    private ExamStudentService examStudentService = new ExamStudentServiceImpl();

    public void initialize(){
        courseService = new CourseServiceImpl();
        moduleService = new ModuleServiceImpl();
        examService = new ExamServiceImpl();
        //userId = "U-001";

    }

    public void setClassesComboBox(){
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
            ObservableList<CourseTM> courses = courseService.getCoursesByTeacherId(user.getId());
            //pasindub32@gmail.comSystem.out.println(user.getId());
            for(CourseTM co : courses){
                co.setCourseName(co.getYear()+" - "+co.getGrade()+" - "+co.getName());
                //System.out.println(co.getCourseName());
            }
            cbClasses.setItems(courses);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setModuleComboBox(String subjectId){
        cbModules.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((Module)object).getName();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            cbModules.setItems(moduleService.getModuleOfSubect(subjectId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void cbClassesOnAction(ActionEvent actionEvent) {
        selectedItem =(CourseTM) cbClasses.getSelectionModel().getSelectedItem();
        if(selectedItem==null){
            return;
        }
        setModuleComboBox(selectedItem.getSub_id());
        cbExams.getItems().clear();
        tblStudentResults.getItems().clear();
        selectedModule=null;
        selectedExam = null;
    }

    public void cbModulesOnAction(ActionEvent actionEvent) {
        selectedModule = null;
        lk.ijse.sakya.entity.custom.Module temp = (lk.ijse.sakya.entity.custom.Module) cbModules.getSelectionModel().getSelectedItem();
        if(temp==null){
            return;
        }
        selectedModule = new Module(temp.getId(),temp.getName(),temp.getSubId());
        setExamComboBox();
        selectedExam = null;
        tblStudentResults.getItems().clear();
    }

    public void setExamComboBox(){
        cbExams.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((lk.ijse.sakya.entity.custom.Exam)object).getExamId();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            cbExams.setItems(examService.getAllExamsByClassIdAndModuleId(selectedItem.getId(),selectedModule
                    .getId()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void cbExamsOnAction(ActionEvent actionEvent) {
        lk.ijse.sakya.entity.custom.Exam temp = (lk.ijse.sakya.entity.custom.Exam) cbExams.getSelectionModel().getSelectedItem();
        if(temp==null){

            return;
        }
        selectedExam = new Exam(temp.getExamId(), temp.getClassId(), temp.getModuleId(), temp.getDate(), temp.getExamStudents());
        setResultTable(selectedExam.getExamId());
    }

    public void setResultTable(String examId){
        colStudentId.setCellValueFactory(new PropertyValueFactory<ExamResultTM,String>("id"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<ExamResultTM,String>("name"));
        colMark.setCellValueFactory(new PropertyValueFactory<ExamResultTM, JFXTextField>("txtMark"));

        try {
            tblStudentResults.setItems(examStudentService.getExamStudentDetailsByExamId(examId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveExamDetailsOnAction(ActionEvent actionEvent) {
        ObservableList<ExamResultTM> items = tblStudentResults.getItems();
        ArrayList<lk.ijse.sakya.entity.custom.ExamStudent> list = new ArrayList<>();
        int count = 0;
        for(ExamResultTM ob : items){
            double mark = -1;
            if(!ob.getTxtMark().getText().equalsIgnoreCase("AB")){
                try {
                    mark = Double.parseDouble(ob.getTxtMark().getText());
                } catch (NumberFormatException e) {
                    new Alert(Alert.AlertType.ERROR, "Invalid Mark Input For " + ob.getName()).show();
                    ob.getTxtMark().requestFocus();
                    return;
                }
            }
            if(mark>=-1 && mark<=100) {
                list.add(new lk.ijse.sakya.entity.custom.ExamStudent(ob.getId(), selectedExam.getExamId(),mark));
            }else if(mark!=-1){
                new Alert(Alert.AlertType.ERROR,"Invalid Mark Input For "+ob.getName()).show();
                ob.getTxtMark().requestFocus();
            }
            count++;
        }

        try {
            //-------------------------------------------------------------------------------------------------------------
            boolean b = examStudentService.updateStudentExamResult(list);
            if(b){
                new Alert(Alert.AlertType.INFORMATION,"Saved Success").show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void btnCreateExamOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("./teacherdashboard/" +
                "AddExamForm.fxml"));
        try {
            Parent load = loader.load();
            AddExamFormController controller = loader.getController();
            controller.setCourseAndModule(selectedItem,selectedModule);
            stage.setScene(new Scene(load));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnLoadExamDetails.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLoggedUser(User user) {
       this.user = user;
       setClassesComboBox();

    }
}
