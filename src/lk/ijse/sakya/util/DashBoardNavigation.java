package lk.ijse.sakya.util;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.sakya.dto.User;
import lk.ijse.sakya.interfaces.DashBoard;

import java.io.IOException;

public class DashBoardNavigation extends Task<Node> {
    public static String temp;
    public static AnchorPane pane;
    public static  DashBoardNavigation ob;
    public static boolean flag;
    public static User user;
    public static Stage window;

    private DashBoardNavigation(){

    }

    public static void setUi(String type,AnchorPane pane1,String ui) {
        pane=pane1;
        pane.getChildren().clear();
        temp = "../view/"+type+"/"+ui+".fxml";
        flag=true;
    }
    public static void logOut(AnchorPane pane1) {
        window =(Stage) pane1.getScene().getWindow();
        pane=null;

    }
    public static void setUi(String type, AnchorPane pane1, String ui, User user1)  {
        pane=pane1;
        pane.getChildren().clear();
        temp = "../view/"+type+"/"+ui+".fxml";
        user = user1;
        flag = false;

    }

    @Override
    protected Node call()  {
        try {
            if(pane==null){
                return FXMLLoader.load(DashBoardNavigation.class.getResource("../view/LoginForm.fxml"));
            }
            if(flag){
                updateProgress(50,100);
                return FXMLLoader.load(DashBoardNavigation.class.getResource(temp));

            }else{
                updateProgress(25,100);
                FXMLLoader loader = new FXMLLoader(DashBoardNavigation.class.getResource(temp));
                updateProgress(50,100);
                Parent load = loader.load();
                DashBoard controller = loader.getController();
                controller.setLoggedUser(user);
                updateProgress(75,100);
                return load;
            }
        }catch(IOException e){
            updateMessage("Ui Not Found");
        }
        return null;
    }

    public static DashBoardNavigation getInstance(){
       return new DashBoardNavigation();
    }

    public static void  load(){
        DashBoardNavigation instance = DashBoardNavigation.getInstance();
        instance.valueProperty().addListener((a, b, c)->{
            pane.getChildren().add(c);
        });
        instance.messageProperty().addListener((a,old,nw)->{
            new Alert(Alert.AlertType.ERROR,nw).show();
        });
        Thread t1 = new Thread(instance);
        t1.start();
    }
}
