package lk.ijse.sakya.controller.receptionist;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXProgressBar;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
//import org.opencv.core.Core;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import lk.ijse.sakya.interfaces.QrPerformance;
import lk.ijse.sakya.util.WebCamView;
import lk.ijse.sakya.thread.WebCamService;
import org.opencv.core.Mat;

public class QrScannerFormController {
    public BorderPane pane;
    public Label lblName;
    public Rectangle rectangle;
    public JFXProgressBar prograss;
    Mat matrix = null;
    public ImageView imgView;
    public Label lblId;
    private WebCamService service;
    private QrPerformance ob;
    public void initialize(){
        //rectangle.setVisible(false);
        prograss.setVisible(false);
        //imgView.setVisible(false);
        Webcam cam =Webcam.getWebcams().get(0);
        service = new WebCamService(cam);

        WebCamView view = new WebCamView(service);

        pane.getChildren().add(view.getView());

        //setOnCloseRequest(new )

        prograss.progressProperty().bind(service.progressProperty());

        service.messageProperty().addListener((a,old,c)->{
            if(c!=null){
                if(old==null){
                    String studentDetail = ob.getStudentDetail(c);
                    if(studentDetail!=null){
                        lblName.setText(studentDetail);
                    }
                    ob.qrIdRequestAction(c);
                }else
                if(!old.equals(c)) {
                    String studentDetail = ob.getStudentDetail(c);
                    if (studentDetail != null) {
                        lblName.setText(studentDetail);
                    }
                    ob.qrIdRequestAction(c);
                }
            }
        });
    }

    public void btnStartOnAction(ActionEvent actionEvent) {
        service.restart();
        prograss.setVisible(true);

    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        service.cancel();
        prograss.setVisible(false);
    }

    public void setController(QrPerformance ob){
        this.ob = ob;
    }
}
