package lk.ijse.sakya.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import lk.ijse.sakya.thread.WebCamService;
//Credit
//https://gist.github.com/james-d/f826c9f38d53628114124a56fb7c4557#file-webcamservice-java
public class WebCamView {

    private final ImageView imageView ;
    private final WebCamService service ;
    private final Region view ;

    private final Label statusPlaceholder ;


    public WebCamView(WebCamService service) {
        this.service = service ;
        this.imageView = new ImageView();
        imageView.setPreserveRatio(true);
        // make the cam behave like a mirror:
        imageView.setScaleX(-1);

        this.statusPlaceholder = new Label();
        this.view = new Region() {
            {
                service.stateProperty().addListener((obs, oldState, newState) -> {
                    switch (newState) {
                        case READY:
                            statusPlaceholder.setText("Initializing");
                            getChildren().setAll(statusPlaceholder);
                            break ;
                        case SCHEDULED:
                            statusPlaceholder.setText("Waiting");
                            getChildren().setAll(statusPlaceholder);
                            break ;
                        case RUNNING:
                            imageView.imageProperty().unbind();
                            imageView.imageProperty().bind(service.valueProperty());
                            getChildren().setAll(imageView);
                            break ;
                        case CANCELLED:
                            //System.out.println("Cancelled");
                            imageView.imageProperty().unbind();
                            imageView.setImage(null);
                            statusPlaceholder.setText("Stopped");
                            getChildren().setAll(statusPlaceholder);
                            //System.out.println("Processed cancel in view");
                            break ;
                        case FAILED:
                            imageView.imageProperty().unbind();
                            statusPlaceholder.setText("Error");
                            getChildren().setAll(statusPlaceholder);
                            service.getException().printStackTrace();
                            break ;
                        case SUCCEEDED:
                            // unreachable...
                            imageView.imageProperty().unbind();
                            statusPlaceholder.setText("");
                            getChildren().clear();
                    }
                    requestLayout();
                });



            }

            @Override
            protected void layoutChildren() {
                super.layoutChildren();
                double w = 167;
                double h = 195;
                if (service.isRunning()) {
                    imageView.setFitWidth(w);
                    imageView.setFitHeight(h);
                    imageView.setPreserveRatio(false);
                    imageView.resizeRelocate(0, 0, w, h);
                } else {
                    double labelHeight = statusPlaceholder.prefHeight(w);
                    double labelWidth = statusPlaceholder.prefWidth(labelHeight);
                    statusPlaceholder.resizeRelocate((w - labelWidth)/2, (h-labelHeight)/2, labelWidth, labelHeight);
                }
            }

            @Override
            protected double computePrefWidth(double height) {
                return 200;
            }
            @Override
            protected double computePrefHeight(double width) {
                return 200;
            }
        };
    }

    public WebCamService getService() {
        return service ;
    }

    public Node getView() {
        return view ;
    }

}