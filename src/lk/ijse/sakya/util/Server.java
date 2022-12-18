package lk.ijse.sakya.util;

import lk.ijse.sakya.controller.receptionist.MarkAttendenceController;
import lk.ijse.sakya.interfaces.MobileQrPerformance;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int port=9001;
    public static class Handeler implements Runnable{
        private  MobileQrPerformance ob;
        private Socket soket;
        public Handeler(Socket socket, MobileQrPerformance ob1){
            this.soket=socket;
            ob=ob1;
        }
        @Override
        public void run() {
            try {

                while (true){
                    DataOutputStream out = new DataOutputStream(soket.getOutputStream());
                    BufferedReader in = new BufferedReader(new InputStreamReader(soket.getInputStream()));
                    String input = in.readLine();
                    if(input==null){
                        continue;
                    }
                    ob.onClientDataReceived(input);
                    out.writeBytes(ob.getStudentDetails(input)+'\n');
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
