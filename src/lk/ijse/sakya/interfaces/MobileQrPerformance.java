package lk.ijse.sakya.interfaces;

public interface MobileQrPerformance extends QrPerformance {
     String getStudentDetails(String id);
     void onClientDataReceived(String id);
}
