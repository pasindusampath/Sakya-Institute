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

public class ReciptionistDashboardFormController implements DashBoard {
    public AnchorPane dashBoardContext;
    public ProgressIndicator progress;

    private User loggedUser;
    public void initialize()  {
        progress.setVisible(false);
        btnManageStudentOnAction(null);
    }

    public void btnManageStudentOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("reciptionist",dashBoardContext,"ManageStudentsForm");
        load();
    }

    public void btnMarkAttendenceOnAction(ActionEvent actionEvent) {
        DashBoardNavigation.setUi("reciptionist",dashBoardContext,"MarkAttendenceForm");
        load();
    }

    public void btnMakePaymentOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("reciptionist",dashBoardContext,"MakePaymentForm",loggedUser);
        load();
    }

    public void btnViewStudentDetailsOnAction(ActionEvent actionEvent)  {
        /*if(mDetails==null){
            mDetails=FXMLLoader.load(getClass().getResource("../view/reciptionist/ViewStudentDetailsForm.fxml"));
        }
        dashBoardContext.getChildren().clear();
        dashBoardContext.getChildren().add(mDetails);*/
        DashBoardNavigation.setUi("reciptionist",dashBoardContext,"ViewStudentDetailsForm");
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
    }

    public void load(){
        dashBoardContext.getChildren().clear();
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a, b, c)->{
            progress.setVisible(false);
            dashBoardContext.getChildren().clear();
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
