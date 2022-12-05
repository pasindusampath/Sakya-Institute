package lk.ijse.sakya.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.interfaces.DashBoard;
import lk.ijse.sakya.util.DashBoardNavigation;

import javax.xml.soap.SAAJResult;
import java.io.IOException;

public class AdminDashBoardFormController implements DashBoard {
    public AnchorPane dashBoardContext;
    private User loggedUser;
    public void initialize() throws IOException {
        DashBoardNavigation.setUi("admindashboard",dashBoardContext,"ManageUserForm");
        load();
    }

    public void btnManageClassOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("admindashboard",dashBoardContext,"ManageClassForm");
        load();
    }

    public void btnManageUserOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.setUi("admindashboard",dashBoardContext,"ManageUserForm");
        load();
    }

    public void btnLogOutOnAction(ActionEvent actionEvent)  {
        DashBoardNavigation.logOut(dashBoardContext);
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

    public void btnViewIncomeOnAction(ActionEvent actionEvent) throws IOException {
        DashBoardNavigation.setUi("admindashboard",dashBoardContext,"ViewIncomeForm",loggedUser);
        load();
    }

    public void load(){
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a, b, c)->{
            dashBoardContext.getChildren().add(c);
        });
        instance.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,nw).show();
        });
        Thread t1 = new Thread(instance);
        t1.start();
    }
}
