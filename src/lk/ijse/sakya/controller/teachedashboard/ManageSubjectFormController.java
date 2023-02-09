package lk.ijse.sakya.controller.teachedashboard;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import lk.ijse.sakya.entity.custom.Module;
import lk.ijse.sakya.entity.custom.Subject;


import lk.ijse.sakya.service.custom.ModuleService;
import lk.ijse.sakya.service.custom.SubjectService;
import lk.ijse.sakya.service.custom.impl.ModuleServiceImpl;
import lk.ijse.sakya.service.custom.impl.SubjectServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

public class ManageSubjectFormController {
    public Label lblnewSubjectId;
    public JFXTextField txtSubjectName;
    public JFXComboBox cbGrade;
    public JFXTextField txtNewSubjectName;
    public TableView tblSubjects;
    public TableColumn colSubjectId;
    public TableColumn colSubjectName;
    public Label lblSubjectId;
    public JFXTextField txtNewModuleName;
    public Label lblNewModuleId;
    public TableView tblModule;
    public TableColumn colModuleID;
    public TableColumn colModuleName;
    public JFXTextField txtModuleName;
    public Label lblModuleId;
    public Label lblSelectedSubject;
    private int selectedGrade;
    private Subject selectedSubject;
    private Module selectedModule;
    private SubjectService subjectService;
    private ModuleService moduleService;


    public void initialize() {
        moduleService = new ModuleServiceImpl();
        subjectService = new SubjectServiceImpl();
        String[] temp = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        ObservableList<String> gradeList = FXCollections.observableArrayList(temp);
        cbGrade.setItems(gradeList);

        setLabels();

    }

    public void btnAddSubjectOnAction(ActionEvent actionEvent) {
        if (cbGrade.getSelectionModel().getSelectedIndex() == -1) {
            new Alert(Alert.AlertType.ERROR, "Select Grade From Above Combo Box").show();
            return;
        }
        if(txtNewSubjectName.getText().length()<5){
            new Alert(Alert.AlertType.ERROR, "Subject Name Should Be grater than 5 characters").show();
            return;

        }
        lk.ijse.sakya.entity.custom.Subject temp = new lk.ijse.sakya.entity.custom.Subject(lblnewSubjectId.getText(), txtNewSubjectName.getText(), selectedGrade);
        try {
            boolean flag = subjectService.addSubject(temp);
            if (flag) {
                new Alert(Alert.AlertType.INFORMATION, "Subject Added Successful").show();
                setLabels();
                setSubjectTable();
                txtNewSubjectName.clear();
            }else{
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Add Subject Failed - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Add Subject Failed - Driver Error").show();
        }

    }

    public void setLabels() {
        try {
            lblnewSubjectId.setText(subjectService.getNewSubjectId());
            lblNewModuleId.setText(moduleService.getNewModuleId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Get New ID Failed - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Get New ID Failed - Driver Error").show();
        }
    }

    public void setSubjectTable() {
        colSubjectId.setCellValueFactory(new PropertyValueFactory<Subject, String>("id"));
        colSubjectName.setCellValueFactory(new PropertyValueFactory<Subject, String>("name"));

        try {
            ObservableList<lk.ijse.sakya.entity.custom.Subject> subjects = subjectService.getSubjects(selectedGrade);
            tblSubjects.setItems(subjects);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Subject Details Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Subject Details Error - Driver Error").show();
        }


    }


    public void setModuleTable() {
        colModuleID.setCellValueFactory(new PropertyValueFactory<Module, String>("id"));
        colModuleName.setCellValueFactory(new PropertyValueFactory<Module, String>("name"));
        try {
            ObservableList<Module> mList = moduleService.getModuleOfSubect(selectedSubject.getId());
            tblModule.setItems(mList);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Module Details Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Module Details Error - Driver Error").show();
        }
    }

    public void cbGradeOnAction(ActionEvent actionEvent) {
        selectedGrade = Integer.parseInt(cbGrade.getSelectionModel().getSelectedItem().toString());
        selectedSubject=null;
        setSubjectTable();
        tblModule.getItems().clear();
        lblSubjectId.setText("");
        lblModuleId.setText("");
        txtModuleName.clear();
        txtSubjectName.clear();
        lblSelectedSubject.setText("");
    }

    public void tblSubOnMouseClickAction(MouseEvent mouseEvent) {
        if (tblSubjects.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }

        lk.ijse.sakya.entity.custom.Subject s = (lk.ijse.sakya.entity.custom.Subject) tblSubjects.getSelectionModel().getSelectedItem();
        selectedSubject = new Subject(s.getId(),s.getName(),s.getGrade());


        lblSelectedSubject.setText("Grade "+selectedGrade+" - "+selectedSubject.getName());
        txtSubjectName.setText(selectedSubject.getName());
        lblSubjectId.setText(selectedSubject.getId());
        selectedModule = null;
        lblModuleId.setText("");
        txtModuleName.clear();
        setModuleTable();

    }

    public void btnUpdateSubjectOnAction(ActionEvent actionEvent) {
        if (selectedSubject == null) {
            new Alert(Alert.AlertType.ERROR, "No Subject Selected For Updating").show();
            return;
        }
        if(txtSubjectName.getText().length()<5){
            new Alert(Alert.AlertType.ERROR, "Subject Name Should Be grater than 5 characters").show();
            return;

        }
        selectedSubject.setName(txtSubjectName.getText());
        try {
            lk.ijse.sakya.entity.custom.Subject sub = new lk.ijse.sakya.entity.custom.Subject(selectedSubject.getId(),selectedSubject.getName(),selectedSubject.getGrade());
            boolean flag = subjectService.updateSubject(sub);
            if (flag) {
                tblSubjects.refresh();
                tblSubjects.getSelectionModel().select(null);
                lblSelectedSubject.setText("Grade "+selectedGrade+" - "+selectedSubject.getName());
                new Alert(Alert.AlertType.INFORMATION, "Subject Name Updated Successful").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Subject Name Update Failed").show();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Subject Name Update Failed-Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Subject Name Update Failed-Driver Error").show();
        }

    }

    public void btnAddModuleOnAction(ActionEvent actionEvent) {
        String id = lblNewModuleId.getText();
        String name = txtNewModuleName.getText();
        if(selectedSubject==null){
            new Alert(Alert.AlertType.ERROR, "Select Subject From Subject Table First").show();
            return;
        }
        if(name.length()<5){
            new Alert(Alert.AlertType.ERROR, "Module Name Should Be grater than 5 characters").show();
            return;
        }

        try {
            boolean flag = moduleService.addModule(new lk.ijse.sakya.entity.custom.Module(id, name, selectedSubject.getId()));
            if (flag) {
                new Alert(Alert.AlertType.INFORMATION, "Module Saved Successful").show();
                setModuleTable();
                setLabels();
                txtNewModuleName.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Operation Failed").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Module Saving Failed-Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Module Saving Failed-Driver Error").show();
        }
    }

    public void btnUpdateModuleOnAction(ActionEvent actionEvent) {
        if (selectedSubject == null || selectedModule == null) {
            new Alert(Alert.AlertType.ERROR, "No Module Selected For Updating").show();
            return;
        }
        if(txtModuleName.getText().length()<5){
            new Alert(Alert.AlertType.ERROR, "Module Name Should Be grater than 5 characters").show();
            return;
        }
        selectedModule.setName(txtModuleName.getText());
        tblModule.refresh();
        try {
            lk.ijse.sakya.entity.custom.Module module = new lk.ijse.sakya.entity.custom.Module(selectedModule.getId()
            ,selectedModule.getName(),selectedModule.getSubId());
            boolean flag = moduleService.updateModule(module);
            if(flag){
                txtModuleName.clear();
                lblModuleId.setText("");
                selectedModule=null;
                tblModule.getSelectionModel().select(null);
                new Alert(Alert.AlertType.INFORMATION,"Module Update Successful").show();

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Module Updating Failed-Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Module Updating Failed-Driver Error").show();
        }
    }

    public void btnDeleteModuleOnAction(ActionEvent actionEvent) {
        if(selectedModule==null || selectedSubject==null){
            new Alert(Alert.AlertType.ERROR, "No Module Selected For Deleting").show();
            return;
        }
        Alert al = new Alert(Alert.AlertType.WARNING,"Do You Want To Delete "+selectedModule.getName()+" From"
                +" The System", ButtonType.OK,ButtonType.CANCEL);
        Optional<ButtonType> buttonType = al.showAndWait();
        if(buttonType.get().getText().equalsIgnoreCase("cancel")){
            new Alert(Alert.AlertType.ERROR, "Module Not Deleted").show();
            return;
        }
        try {
            lk.ijse.sakya.entity.custom.Module module = new lk.ijse.sakya.entity.custom.Module(selectedModule.getId(),selectedModule.getName()
            ,selectedModule.getSubId());
            boolean flag = moduleService.deleteModule(module);
            if(flag){
                setModuleTable();
                setLabels();
                txtModuleName.clear();
                lblModuleId.setText("");
                selectedModule=null;
                new Alert(Alert.AlertType.INFORMATION,"Module Delete Successful").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Module Delete Error").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Module Delete Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Module Delete Error - Driver Error").show();
        }
    }

    public void tblModuleMouseClickOnAction(MouseEvent mouseEvent) {
        if (tblModule.getSelectionModel().getSelectedIndex() == -1) {
            return;
        }
        selectedModule = (Module) tblModule.getSelectionModel().getSelectedItem();
        txtModuleName.setText(selectedModule.getName());
        lblModuleId.setText(selectedModule.getId());
    }

    public void btnDeleteSubjectOnAction(ActionEvent actionEvent) {
        if (selectedSubject == null){
            new Alert(Alert.AlertType.ERROR, "No Subject Selected For Deleting").show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do You want to delete " + selectedSubject.getName()
                + " From the system.....?", ButtonType.OK, ButtonType.CANCEL);
        Optional<ButtonType> buttonType = alert.showAndWait();
        String text = buttonType.get().getText();
        if (text.equalsIgnoreCase("cancel")) {
            new Alert(Alert.AlertType.ERROR, "Subject Not Deleted").show();
            return;
        }
        try {
            boolean flag = subjectService.deleteSubject(selectedSubject.getId());
            if(flag){
                lblSubjectId.setText("");
                txtSubjectName.clear();
                selectedSubject=null;
                selectedModule=null;
                txtModuleName.setText("");
                lblModuleId.setText("");
                setLabels();
                setSubjectTable();
                tblModule.getItems().clear();
                lblSelectedSubject.setText("");
                new Alert(Alert.AlertType.INFORMATION, "Subject Delete Successful").show();

            }else{
                new Alert(Alert.AlertType.ERROR, "Subject Delete Failed-Database Error").show();

            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Subject Delete Failed-Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Subject Delete Failed-Driver Error").show();
        }

    }
}
