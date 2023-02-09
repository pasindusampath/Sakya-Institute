package lk.ijse.sakya.thread;


import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import lk.ijse.sakya.controller.receptionist.QrScannerFormController;
import lk.ijse.sakya.service.interfaces.QrPerformance;

public class LoadQrUiTask extends Task<Scene> {
    QrPerformance con;
    Window owner;
    Stage stage;
    public LoadQrUiTask(QrPerformance ob, Window owner,Stage stage){
        con=ob;
        this.owner = owner;
        this.stage = stage;
    }


    @Override
    protected Scene call() throws Exception {
        //System.out.println("Call");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reciptionist/QrScannerForm.fxml"));
            Parent load = loader.load();
            QrScannerFormController controller = loader.getController();
            controller.setController(con);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    controller.btnStopOnAction(null);
                }

            });
            //System.out.println("Call 2");
            return new Scene(load);
            //stage.setScene(new Scene(load));
        } catch (Exception e) {
            updateMessage(e.getMessage());
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        //System.out.println("Call 3");
        return null;
    }
}
