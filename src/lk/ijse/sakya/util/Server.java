package lk.ijse.sakya.util;

import lk.ijse.sakya.controller.receptionist.MarkAttendenceController;

import java.io.*;
import java.net.Socket;

public class Server {
    private static final int port=9001;
    private static MarkAttendenceController ob;
    private Handeler h1;


    public static class Handeler implements Runnable{
        private Socket soket;
        public Handeler(Socket socket){
            this.soket=socket;
        }
        @Override
        public void run() {
            try {
                DataOutputStream out = new DataOutputStream(soket.getOutputStream());
                while (true){
                    BufferedReader in = new BufferedReader(new InputStreamReader(soket.getInputStream()));
                    String input = in.readLine();
                    if(input==null)continue;
                    System.out.println(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        public Socket getSoket(){
            return soket;
        }
    }
}
