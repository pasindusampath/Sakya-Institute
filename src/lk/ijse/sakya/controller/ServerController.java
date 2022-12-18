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
import java.util.ArrayList;

public class ServerController {
    private MobileQrPerformance ob;
    public Label lblIpAddress;
    Thread t1;
    Thread handleThread;
    Server.Handeler handler;
    ArrayList<Thread> handles;
    ArrayList<Server.Handeler> handelers;
    ServerSocket listner;
    public void initialize() throws UnknownHostException {
        handles= new ArrayList<>();
        handelers=new ArrayList<>();
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
                        handler=new Server.Handeler(socket,ob);
                        handleThread = new Thread(handler);
                        handelers.add(handler);
                        handles.add(handleThread);
                        handleThread.start();
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
            for(Thread ob:handles){
                ob.stop();
            }
            for(Server.Handeler ob:handelers){
                if(ob.getSoket().isClosed()){
                    System.out.println("Already Closed");
                    continue;
                }
                ob.getSoket().close();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        //Server.Handeler.
    }

    public void setController(MobileQrPerformance ob){
        this.ob=ob;
    }

}
