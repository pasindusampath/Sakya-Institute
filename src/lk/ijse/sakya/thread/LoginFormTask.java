package lk.ijse.sakya.thread;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.paint.Paint;

import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.interfaces.DashBoard;
import lk.ijse.sakya.model.UserController;


import java.io.IOException;
import java.sql.SQLException;

public class LoginFormTask extends Task<Parent> {
    JFXTextField txtUserName;
    JFXPasswordField txtPassword;
    public LoginFormTask(JFXTextField txtUserName, JFXPasswordField txtPassword) {
        this.txtUserName = txtUserName;
        this.txtPassword = txtPassword;
    }




    @Override
    protected Parent call()  {

        String gmail = txtUserName.getText();
        String password = txtPassword.getText();
        updateProgress(10, 100);
        //System.out.println(gmail);

        try {
            User user = UserController.searchUserByGmail(gmail);
            //System.out.println(user.getName());
            updateProgress(30, 100);
            if (user.getPassword().equals(password)) {
                updateProgress(50, 100);
                return verifyUser(user);
                //updateMessage("Log In Success");

            } else {
                txtPassword.setFocusColor(Paint.valueOf("0xff0000"));
                txtPassword.setUnFocusColor(Paint.valueOf("0xff0000"));
                updateMessage("Wrong Password");
                return null;
            }
        } catch (SQLException e) {
            updateMessage(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            updateMessage(e.getMessage());
            e.printStackTrace();
            return null;
        } catch (NullPointerException | IOException ex) {
            //updateMessage(ex.getMessage());
            updateProgress(100,100);
            ex.printStackTrace();
            return null;
        }
    }

    public Parent verifyUser(User user) throws IOException {
        switch (user.getType().toLowerCase()){
            case "admin" : return getUi("Admin",user);
            case "teacher" :  return getUi("Teacher",user);
            case "receptionist": return getUi("Reciptionist",user);
            default:updateMessage("Invalid User");return null;
        }
    }

    public Parent getUi(String type,User user)  {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/"+type+"DashboardForm.fxml"));
        updateProgress(70, 100);
        Parent load;
        try {
            load = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        updateProgress(90, 100);
        DashBoard controller = loader.getController();
        System.out.println("ui Found");
        updateProgress(95, 100);
        controller.setLoggedUser(user);
        return load;
    }

}
