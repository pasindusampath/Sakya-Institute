package lk.ijse.sakya.dao.custom;

import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.Income;
import lk.ijse.sakya.entity.custom.Payment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface PaymentDAO extends CrudDAO<Payment,String> {
    boolean add(ArrayList<Payment> list) throws SQLException, ClassNotFoundException;
    boolean isAlreadyPaid(Payment payment) throws SQLException, ClassNotFoundException;
    String getNewInvoiceId() throws SQLException, ClassNotFoundException;
    String getLastInvoiceId() throws SQLException, ClassNotFoundException;
    ArrayList<Income> getIncomeByMonth(int month) throws SQLException, ClassNotFoundException;
    HashMap getMonthlyIncomeForInstitute(int year) throws SQLException, ClassNotFoundException;
    ArrayList<Income> getIncomeByMonth(int month,String userId) throws SQLException, ClassNotFoundException;
    HashMap getMonthlyIncomeForTeacher(int year,String teacherId) throws SQLException, ClassNotFoundException;

}
