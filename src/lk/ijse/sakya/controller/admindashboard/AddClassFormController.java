package lk.ijse.sakya.controller.admindashboard;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mysql.cj.result.ValueFactory;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import lk.ijse.sakya.dto.Course;
import lk.ijse.sakya.dto.Subject;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.SubjectController;
import lk.ijse.sakya.model.UserController;
import lk.ijse.sakya.thread.SendMail;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class AddClassFormController {
    public JFXComboBox cbGrade;
    public JFXComboBox cbSubject;
    public TableView tblTeachers;
    public TableColumn colTeacherId;
    public TableColumn colTeacherName;
    public TableColumn colContact;
    public JFXComboBox cbYear;
    public Label lblCourseId;
    public AnchorPane context;
    public boolean isAdded;
    public JFXTextField txtMonthlyFee;

    public void initialize(){
        isAdded = false;
        String[] temp = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
        cbGrade.setItems(FXCollections.observableArrayList(temp));

        int year = LocalDate.now().getYear();
        String[] temp1 = {String.valueOf(year),String.valueOf(year+1)};

        cbYear.setItems(FXCollections.observableArrayList(temp1));
        setNewCourseIdLabel();


        colTeacherId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<User,String>("contact"));

        try {
            tblTeachers.setItems(UserController.getTeachers());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Teacher Details Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting Teacher Details Error - Driver Error").show();
        }
    }
    public void setNewCourseIdLabel(){
        try {
            lblCourseId.setText(CourseController.getNewCourseId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting New Course Id Error - Database Error").show();
            ((Stage)context.getScene().getWindow()).close();

        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting New Course Id Error - Driver Error").show();
        }
    }

    public void btnCreateCourseOnAction(ActionEvent actionEvent) {
        Subject subject= (Subject) cbSubject.getSelectionModel().getSelectedItem();
        String grade = (String) cbGrade.getSelectionModel().getSelectedItem();
        String year = (String) cbYear.getSelectionModel().getSelectedItem();
        User user = (User) tblTeachers.getSelectionModel().getSelectedItem();
        if(subject==null || grade == null || year == null){
            new Alert(Alert.AlertType.ERROR,"Check All Combo Box").show();
            return;
        }
        if(user==null){
            new Alert(Alert.AlertType.ERROR,"Select Teacher From The Table ").show();
            return;
        }
        Course temp = new Course(lblCourseId.getText(),subject.getId(),Integer.parseInt(year),user.getId(),
                Double.parseDouble(txtMonthlyFee.getText()));
        try {
            boolean flag = CourseController.addCourse(temp);
            if(flag){
                sendMail(user,temp,subject);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Course Added Successful\nDo You Want " +
                        "To Add New Course", ButtonType.YES,ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();
                if(buttonType.get().getText().equalsIgnoreCase("no")){
                    ((Stage)context.getScene().getWindow()).close();
                }
                isAdded=true;
                setNewCourseIdLabel();
                cbYear.getSelectionModel().select(null);
                cbGrade.getSelectionModel().select(null);
                cbSubject.getItems().clear();
                tblTeachers.getSelectionModel().select(null);
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Add Course Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Add Course Error - Driver Error").show();
        }
    }

    public void cbGradeOnAction(ActionEvent actionEvent) {
        try {

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
                cbSubject.setItems(SubjectController.getSubjects(Integer.parseInt(cbGrade.getSelectionModel().getSelectedItem().toString())));
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Getting Subject Details Error - Database Error");
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Getting Subject Details Error - Driver Error");
            }
        }catch(NullPointerException e){

        }
    }

    public void sendMail(User teacher,Course course,Subject subject){
        SendMail o = new SendMail(teacher.getGmail(),"Your Course Added Successful",
                "Course ID : "+course.getId()+"<br></br>"+
                "Subject ID : "+course.getSubId()+"<br></br>"+
                "Grade     : "+subject.getGrade()+"<br></br>"+
                "Year      :"+course.getYear());
        o.valueProperty().addListener((q, oldValue, newValue) -> {
            if(newValue) {
                new Alert(Alert.AlertType.INFORMATION,"Mail Sent Successful").show();
            }
        });
        Thread t = new Thread(o);
        t.start();
    }
}
