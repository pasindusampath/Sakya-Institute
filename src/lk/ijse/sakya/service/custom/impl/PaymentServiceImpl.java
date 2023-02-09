package lk.ijse.sakya.service.custom.impl;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import lk.ijse.sakya.dao.custom.PaymentDAO;
import lk.ijse.sakya.dao.custom.impl.PaymentDAOImpl;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Income;

import lk.ijse.sakya.entity.custom.Payment;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.custom.PaymentService;
import lk.ijse.sakya.thread.PrintBillTask;
import lk.ijse.sakya.thread.SendMail;


import java.io.File;
import java.nio.file.FileSystems;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentServiceImpl implements PaymentService {
    PaymentDAO paymentDAO;
    public PaymentServiceImpl(){
        paymentDAO=new PaymentDAOImpl();
    }
    @Override
    public HashMap getMonthlyIncomeForInstitute(int year) throws SQLException, ClassNotFoundException {
        return paymentDAO.getMonthlyIncomeForInstitute(year);
    }

    @Override
    public ArrayList<Income> getIncomeByMonth(int month) throws SQLException, ClassNotFoundException {
        return paymentDAO.getIncomeByMonth(month);
    }



    @Override
    public String getNewInvoiceId() throws SQLException, ClassNotFoundException {
        return paymentDAO.getNewInvoiceId();
    }

    @Override
    public boolean isAlreadyPaid(Payment payment) throws SQLException, ClassNotFoundException {
        return paymentDAO.isAlreadyPaid(payment);
    }

    public void sendMail(User loggedUser,File file){
        SendMail ob = new SendMail(loggedUser.getGmail(),"Check Your Requested Report ","Income Report",file);
        Thread t1 = new Thread(ob);
        t1.start();
    }

    @Override
    public void printBill(User user, JFXTextField txtStudentId, Label invoiceNo, Label lblTotal, JFXProgressBar progress, Student student){
        String billPath = FileSystems.getDefault().getPath("src/lk/ijse/sakya/report/Sakya.jrxml").toAbsolutePath().toString();
        String sql="SELECT  c.year as year ,s.name as sub_name,s.grade,u.name as teacherName, sp.month , sp.amount as" +
                " fee FROM student_payment sp  inner join course c on sp.c_id = c.id inner join subject s on c.sub_id" +
                " = s.id inner join user u on c.teacherId = u.id WHERE invoice_nu= '"+invoiceNo.getText()+"'";
        String path = FileSystems.getDefault().getPath("StudentPayments\\"+txtStudentId.getText()+invoiceNo.getText
                ()+".pdf").toAbsolutePath().toString();
        HashMap<String, Object> para=new HashMap<>();
        para.put("cashierName",user.getName());
        para.put("studentId",txtStudentId.getText());
        para.put("invoiceNo",invoiceNo.getText());
        para.put("total",lblTotal.getText());
        PrintBillTask ob = new PrintBillTask(billPath,sql,para,path);
        ob.valueProperty().addListener((a,b,c)->{
            if(c!=null){
                progress.progressProperty().unbind();
                progress.setVisible(false);
                SendMail mail = new SendMail(student.getGmail(),"Payment Success","Payment Done",c);
                Thread t1 = new Thread(mail);
                t1.start();
            }
        });
        progress.progressProperty().bind(ob.progressProperty());
        progress.setVisible(true);
        Thread t2 = new Thread(ob);
        t2.start();
    }

    @Override
    public boolean addPaymentRecords(ArrayList<Payment> list) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            for (Payment ob : list) {
                if (!paymentDAO.add(ob)) {
                    DBConnection.getInstance().getConnection().rollback();
                    return false;
                }

            }
            DBConnection.getInstance().getConnection().commit();
            return true;
        }catch (SQLException e) {
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }catch (ClassNotFoundException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    @Override
    public ArrayList<Income> getIncomeByMonth(int month, String userId) throws SQLException, ClassNotFoundException {
        return paymentDAO.getIncomeByMonth(month, userId);
    }

    @Override
    public HashMap getMonthlyIncomeForTeacher(int year, String teacherId) throws SQLException, ClassNotFoundException {
        return paymentDAO.getMonthlyIncomeForTeacher(year, teacherId);
    }


}
