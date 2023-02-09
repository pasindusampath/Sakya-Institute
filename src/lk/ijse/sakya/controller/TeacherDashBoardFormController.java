package lk.ijse.sakya.controller;

import javafx.event.ActionEvent;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.interfaces.DashBoard;
import lk.ijse.sakya.util.DashBoardNavigation;



public class TeacherDashBoardFormController implements DashBoard {
    public AnchorPane dashBoardContext;
    public ProgressIndicator progress;
    private User loggedUser;
    public  void initialize(){
        progress.setVisible(false);

    }

    public void btnManageResultOnAction(ActionEvent actionEvent) {
        DashBoardNavigation.setUi("teacherdashboard",dashBoardContext,"ManageExamResultForm",loggedUser);
        load();
    }

    public void btnManageSubjectsOnAction(ActionEvent actionEvent) {
        DashBoardNavigation.setUi("teacherdashboard",dashBoardContext,"ManageSubjectForm");
        load();
    }

    public void btnViewAttendenceOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("teacherdashboard",dashBoardContext,"ViewAttendenceForm1",loggedUser);
        load();
    }

    public void btnLogOutOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.logOut(dashBoardContext);
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a,old,nw)->{
            ((Stage)dashBoardContext.getScene().getWindow()).setScene(new Scene((Parent)nw));
        });
        instance.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,nw).show();
        });
        Thread t1 = new Thread(instance);
        t1.start();
    }

    @Override
    public void setLoggedUser(User user) {
        this.loggedUser = user;
        btnManageResultOnAction(null);
    }

    public void btnViewIncomeOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("teacherdashboard",dashBoardContext,"ViewIncomeForm",loggedUser);
        load();
    }

    public void load(){
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a, b, c)->{
            progress.setVisible(false);
            dashBoardContext.getChildren().add(c);
        });
        instance.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,nw).show();
        });
        progress.setVisible(true);
        Thread t1 = new Thread(instance);
        t1.start();
    }
}
