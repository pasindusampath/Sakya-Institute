package lk.ijse.sakya.service.custom;



import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lk.ijse.sakya.dto.Income;

import lk.ijse.sakya.entity.custom.Payment;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.SuperService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface PaymentService extends SuperService {
    HashMap getMonthlyIncomeForInstitute(int year) throws SQLException, ClassNotFoundException;
    ArrayList<Income> getIncomeByMonth(int month) throws SQLException, ClassNotFoundException;
    String getNewInvoiceId() throws SQLException, ClassNotFoundException;
    boolean isAlreadyPaid(Payment payment) throws SQLException, ClassNotFoundException;
    void printBill(User user, JFXTextField txtStudentId, Label invoiceNo, Label lblTotal, JFXProgressBar progress, Student student);
    boolean addPaymentRecords(ArrayList<Payment> list) throws SQLException, ClassNotFoundException;
    ArrayList<Income> getIncomeByMonth(int month,String userId) throws SQLException, ClassNotFoundException;
    HashMap getMonthlyIncomeForTeacher(int year,String teacherId) throws SQLException, ClassNotFoundException;
}
