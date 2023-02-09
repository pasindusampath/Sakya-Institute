package lk.ijse.sakya.service.custom;

import com.jfoenix.controls.JFXProgressBar;
import lk.ijse.sakya.entity.custom.User;

import java.util.HashMap;

public interface PrintBillService {
    void printBill(User loggedUser, JFXProgressBar progress, boolean mail, String billPath, String sql, String savePath, HashMap<String,Object> para);
}
