package lk.ijse.sakya.controller.admindashboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.shape.Rectangle;

import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.thread.SendMail;

import java.util.Random;


public class ValidateGmailFormController {
    public Label lblGmail;
    public JFXTextField txtOtp;
    public JFXButton btnOk;
    public JFXButton btnGetOtp;
    public Rectangle rec1;
    public ProgressIndicator prograss;

    private int x;
    private boolean flag;

    public void initialize(){
        prograss.setVisible(false);
        rec1.setVisible(false);
        flag=false;
    }

    public void btnGetOtpOnAction(ActionEvent actionEvent) {
        btnGetOtp.setDisable(true);
        Random r=new Random();
        x=r.nextInt(9999999);
        SendMail os = new SendMail(lblGmail.getText(),"Verify Your Gmail Address","Use This Code For Creating Account in Sakya Smart Classroom <br></br><h1>"+x+"</h1>");
        Thread ob = new Thread(os);
        ob.setDaemon(true);
        os.messageProperty().addListener((h, oldMessage, newMessage) -> {
            new Alert(Alert.AlertType.WARNING, newMessage).show();
            btnGetOtp.setDisable(false);
        });
        os.valueProperty().addListener((q, oldValue, newValue) -> {
            rec1.setVisible(false);
            btnGetOtp.setDisable(false);
            if(newValue) {
                new Alert(Alert.AlertType.INFORMATION,"Mail Sen Successful").show();
            }else{

            }
        });
        prograss.progressProperty().bind(os.progressProperty());
        prograss.visibleProperty().bind(os.runningProperty());
        ob.start();
        rec1.setVisible(true);
    }

    public void setLblGmail(String mail ){
        //this.ob=ob;
        lblGmail.setText(mail);
    }

    public boolean isValidated(){
        return flag;
    }

    public void btnOkOnAction(ActionEvent actionEvent) {
        int rand = -1;
        try{
            rand=Integer.parseInt(txtOtp.getText());
        }catch(Exception ex){
            new Alert(Alert.AlertType.ERROR,"Wrong Otp Please Try Again").show();
            return;
        }
        if(rand==x){
            rec1.setVisible(true);
            new Alert(Alert.AlertType.INFORMATION,"Congratulations Now You get Mail From the system use the username and password on your mail to access the System").show();

            flag=true;
        }else{
            new Alert(Alert.AlertType.ERROR,"Wrong Otp Please Try Again").show();
            flag=false;
        }
    }

}
