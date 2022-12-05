package lk.ijse.sakya.controller.admindashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.Course;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.Subject;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.SubjectController;
import lk.ijse.sakya.model.UserController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ManageClassFormController {
    public JFXButton btnAdd;
    public JFXComboBox cbTeachers;
    public JFXComboBox cbSubject;
    public JFXComboBox cbGrade;
    public JFXTextField txtCourseId;
    public TableView tblCourses;
    public TableColumn colCourseId;
    public TableColumn colYear;
    public TableColumn colGrade;
    public TableColumn colSubject;
    public TableColumn colTeacherId;
    public JFXComboBox cbYear;
    public JFXRadioButton rdCourseId;
    public JFXRadioButton rdYear;
    public JFXRadioButton rdSubject;
    public JFXRadioButton rdGrade;
    public JFXRadioButton rdTeacherId;
    public JFXTextField txtSearch;
    private CourseTM selectedCourse;

    public void initialize() {
        int year = LocalDate.now().getYear();
        String[] years = {String.valueOf(year), String.valueOf(year + 1)};
        cbYear.setItems(FXCollections.observableArrayList(years));

        String[] temp = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        ObservableList<String> gradeList = FXCollections.observableArrayList(temp);
        cbGrade.setItems(gradeList);
        setTeachersComboBox();
        setCourseTable();
        txtCourseId.setEditable(false);
    }

    public void setCourseTable() {
        colCourseId.setCellValueFactory(new PropertyValueFactory<CourseTM, String>("id"));
        colYear.setCellValueFactory(new PropertyValueFactory<CourseTM, Integer>("year"));
        colGrade.setCellValueFactory(new PropertyValueFactory<CourseTM, Integer>("grade"));
        colSubject.setCellValueFactory(new PropertyValueFactory<CourseTM, String>("name"));
        colTeacherId.setCellValueFactory(new PropertyValueFactory<CourseTM, String>("teacherId"));

        try {
            tblCourses.setItems(CourseController.getCourseDetailForTables());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Course Detail Error - Database Error").show();

        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Course Detail Error - Driver Error").show();
        }

    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        URL resource = getClass().getResource("../../view/admindashboard/AddClassForm.fxml");
        FXMLLoader f1 = new FXMLLoader(resource);
        Parent load = null;
        try {
            load = f1.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AddClassFormController controller = f1.getController();
        stage.setScene(new Scene(load));
        stage.initOwner(btnAdd.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        if(controller.isAdded){
            btnClearOnAction(null);
        }
    }

    public void cbGradeOnAction(ActionEvent actionEvent) {
        try {
            Object selectedItem = cbGrade.getSelectionModel().getSelectedItem();
            setSubjectComboBox(Integer.parseInt((String) selectedItem));
        } catch (NumberFormatException e) {

        }
    }

    public void tblCoursesMouseOnClickAction(MouseEvent mouseEvent) {
        CourseTM selectedItem = (CourseTM) tblCourses.getSelectionModel().getSelectedItem();
        selectedCourse = selectedItem;
        if(selectedCourse==null)return;
        int grade = selectedItem.getGrade();
        String subject = selectedItem.getName();
        String teacherId = selectedItem.getTeacherId();
        int year = selectedItem.getYear();
        txtCourseId.setText(selectedItem.getId());
        ObservableList items = cbGrade.getItems();
        int count = 0;
        for (Object temp : items) {
            if (grade == Integer.parseInt((String) temp)) {
                cbGrade.getSelectionModel().select(count);
                setSubjectComboBox(grade);
                break;
            }
            count++;
        }
        count = 0;
        ObservableList subjects = cbSubject.getItems();
        for (Object temp : subjects) {
            if (subject.equals(((Subject) temp).getName())) {
                cbSubject.getSelectionModel().select(count);
                break;
            }
            count++;
        }
        count = 0;
        ObservableList teachers = cbTeachers.getItems();
        for (Object temp : teachers) {
            if (teacherId.equals(((User) temp).getId())) {
                cbTeachers.getSelectionModel().select(count);
                break;
            }
            count++;
        }
        count = 0;
        ObservableList years = cbYear.getItems();
        for (Object temp : years) {
            if (year == Integer.parseInt(((String) temp))) {
                cbYear.getSelectionModel().select(count);
                break;
            }
            count++;
        }


    }

    public void setSubjectComboBox(int grade) {
        cbSubject.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((Subject) object).getName();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            cbSubject.setItems(SubjectController.getSubjects(grade));
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Course Detail Error - Database Error").show();

        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Course Detail Error - Driver Error").show();

        }

    }

    public void setTeachersComboBox() {
        cbTeachers.setConverter(new StringConverter() {
            @Override
            public String toString(Object object) {
                return ((User) object).getId();
            }

            @Override
            public Object fromString(String string) {
                return null;
            }
        });
        try {
            cbTeachers.setItems(UserController.getTeachers());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Teacher Detail Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Getting Teacher Detail Error - Driver Error").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.ERROR, "Select Course From The Table First").show();
            return;
        }
        String id = txtCourseId.getText();
        int year = Integer.parseInt((String) cbYear.getSelectionModel().getSelectedItem());
        String subjectId = ((Subject) cbSubject.getSelectionModel().getSelectedItem()).getId();
        String teacherId = ((User) cbTeachers.getSelectionModel().getSelectedItem()).getId();

        Course temp = new Course(id, subjectId, year, teacherId,-1);

        try {
            boolean flag = CourseController.updateCourse(temp);
            if (flag) {
                new Alert(Alert.AlertType.INFORMATION, "Course Detail Update Successful").show();
                setCourseTable();
                btnClearOnAction(null);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Update Course Detail Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Update Course Detail Error - Database Error").show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if (selectedCourse == null) {
            new Alert(Alert.AlertType.ERROR, "Select Course From The Table First").show();
        }

        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.WARNING, "Do You Want To Delete " +
                selectedCourse.getId() +" From The System", ButtonType.YES, ButtonType.NO).showAndWait();

        if(buttonType.get().getText().equalsIgnoreCase("no")){
            new Alert(Alert.AlertType.INFORMATION,"Subject Not Deleted").show();
            return;
        }

        try {
            boolean flag = CourseController.deleteCourse(txtCourseId.getText());
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"Subject Deleted").show();
                btnClearOnAction(null);

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Delete Course Detail Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Delete Course Detail Error - Driver Error").show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtCourseId.clear();
        cbGrade.getSelectionModel().select(null);
        cbSubject.getItems().clear();
        cbTeachers.getSelectionModel().select(null);
        cbYear.getSelectionModel().select(null);
        selectedCourse = null;
        setCourseTable();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        if(rdCourseId.isSelected()){
            search("c.id");
        }if(rdYear.isSelected()){
            search("c.year");
        }if(rdSubject.isSelected()){
            search("s.name");
        }if(rdGrade.isSelected()){
            search("s.grade");
        }if(rdTeacherId.isSelected()){
            search("c.teacherId");
        }
    }

    public void search(String searchBy){
        try {
            tblCourses.setItems(CourseController.searchCourses(searchBy,txtSearch.getText()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
