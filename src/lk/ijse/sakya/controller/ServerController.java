package lk.ijse.sakya.controller;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import lk.ijse.sakya.interfaces.MobileQrPerformance;
import lk.ijse.sakya.util.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerController {
    private MobileQrPerformance ob;
    public Label lblIpAddress;
    Thread t1;
    Thread handle;
    Server.Handeler h;
    ServerSocket listner;
    public void initialize() throws UnknownHostException {
        lblIpAddress.setText(InetAddress.getLocalHost().toString());
         t1 = new Thread(){
            @Override
            public void run(){
                System.out.println("Server Started");

                try {
                    listner = new ServerSocket(9001);
                    while(true){
                        Socket socket = listner.accept();
                        System.out.println("Client Added");
                        System.out.println(socket.getInetAddress());
                        h=new Server.Handeler(socket,ob);
                        handle = new Thread(h);
                        handle.start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }
    public void closeConnections()  {
        try {
            listner.close();
            t1.stop();
            handle.stop();
            h.getSoket().close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //Server.Handeler.
    }

    public void setController(MobileQrPerformance ob){
        this.ob=ob;
    }

}
