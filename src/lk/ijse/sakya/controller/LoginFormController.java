package lk.ijse.sakya.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jdk.jfr.internal.tool.Main;
import lk.ijse.sakya.controller.admindashboard.ValidateGmailFormController;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.model.UserController;
import lk.ijse.sakya.thread.LoginFormTask;
import lk.ijse.sakya.thread.SendMail;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFormController {
    public JFXTextField txtUserName;
    public AnchorPane loginFormContext;
    public JFXPasswordField txtPassword;
    public JFXProgressBar progressBar;
    public JFXButton btnLogIn;
    public String sourcePath ;

    public void initialize() {
        progressBar.setVisible(false);
    }

    public void btnLogInOnAction(ActionEvent actionEvent) throws IOException {
        task();

    }

    public void task() {
        if (!isValid()) {
            new Alert(Alert.AlertType.ERROR, "Enter Valid Details").show();
            return;
        }
        progressBar.setVisible(false);
        btnLogIn.setDisable(true);
        LoginFormTask t1 = new LoginFormTask(txtUserName, txtPassword);

        t1.messageProperty().addListener((a, b, c) -> {
            new Alert(Alert.AlertType.ERROR, c).show();
            progressBar.progressProperty().unbind();
            progressBar.setVisible(false);
            btnLogIn.setDisable(false);
            System.out.println(c);
        });

        t1.valueProperty().addListener((a, b, c) -> {
            if (c == null) return;
            Stage window = (Stage) loginFormContext.getScene().getWindow();
            btnLogIn.setDisable(false);
            window.setScene(new Scene(c));
        });

        progressBar.progressProperty().bind(t1.progressProperty());

        Thread thread = new Thread(t1);
        progressBar.setVisible(true);
        thread.start();
    }

    public void txtUserNameKeyAction(KeyEvent keyEvent) {
        Pattern p1 = Pattern.compile("^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$");
        Matcher m1 = p1.matcher(txtUserName.getText());
        boolean b = m1.find();
        if (b) {
            txtUserName.setUnFocusColor(Paint.valueOf("4d4dff"));

        } else {
            txtUserName.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void txtPasswordKeyAction(KeyEvent keyEvent) {
        if (txtPassword.getText().length() < 8) {
            txtPassword.setUnFocusColor(Paint.valueOf("#ff0000"));
        } else {
            txtPassword.setUnFocusColor(Paint.valueOf("4d4dff"));
        }
    }

    public boolean isValid() {
        if (!txtUserName.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())) {
            //System.out.println();
            return false;
        }
        if (!txtPassword.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())) {
            //System.out.println();
            return false;
        }
        return true;
    }

    public void forgetPasswordOnAction(ActionEvent actionEvent) {
        try {
            String path = "";
            User user = UserController.searchUserByGmail(txtUserName.getText());
            if (user == null) {
                new Alert(Alert.AlertType.ERROR, "Enter Your Gmail Address That Used To Create Account in system").show();
                return;
            }
            Stage stage = new Stage();
            sourcePath = isProgramRunnedFromJar() ?   "src/lk/ijse/sakya/view/admindashboard/ValidateGmailForm.fxml" : "../view/admindashboard/ValidateGmailForm.fxml" ;
            URL resource = getClass().getResource(sourcePath);
            FXMLLoader f1 = new FXMLLoader(resource);
            Parent load = f1.load();
            ValidateGmailFormController controller = f1.getController();
            controller.setLblGmail(user.getGmail());
            stage.setScene(new Scene(load));
            stage.initOwner( btnLogIn.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            if (!controller.isValidated()) {
                new Alert(Alert.AlertType.ERROR, "Password Reset Failed ").show();
                return;
            }
            Random r= new Random();
            int rand;
            do{
                rand = r.nextInt(999999999);
            }while(!(rand>10000000));
            user.setPassword(String.valueOf(rand));
            boolean b = UserController.resetPassword(user);
            if(b){
                SendMail ob = new SendMail(user.getGmail(), "Password Reset Success",
                        "Your New Password is : "+user.getPassword());
                Thread t1 = new Thread(ob);
                t1.start();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isProgramRunnedFromJar() {
        try {
            File x = getCurrentJarFileLocation();
            if (x.getAbsolutePath().contains("target" + File.separator + "classes")) {
                return false;
            } else {
                return true;
            }
        }catch (NullPointerException e){
            return false;
        }
    }
    public File getCurrentJarFileLocation() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch(URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }
}
