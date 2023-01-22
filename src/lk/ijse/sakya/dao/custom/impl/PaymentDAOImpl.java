package lk.ijse.sakya.dao.custom.impl;

import lk.ijse.sakya.dao.custom.PaymentDAO;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Income;
import lk.ijse.sakya.entity.custom.Payment;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public boolean add(Payment payment) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into student_payment values(?,?,?,?,?,?)",payment.getStudentId(),payment.getCourseId(),
                payment.getMonth(),payment.getDate(),payment.getAmount(),payment.getInvoiceId());
    }

    @Override
    public boolean update(Payment obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public Payment search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean add(ArrayList<Payment> list) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            for (Payment ob : list) {
                if (!add(ob)) {
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
    public boolean isAlreadyPaid(Payment payment) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from student_payment where st_id = ? AND c_id = ? AND month = ? ", payment.getStudentId(),
                payment.getCourseId(), payment.getMonth());
        if(rs.next()){
            return true;
        }
        return false;
    }

    @Override
    public String getNewInvoiceId() throws SQLException, ClassNotFoundException {
        String lastInvoiceId=getLastInvoiceId();
        if(lastInvoiceId==null){
            return "I-0000000000001";
        }else{
            String[] split=lastInvoiceId.split("[I][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("I-%013d", lastDigits);
            return newOrderId;
        }
    }

    @Override
    public String getLastInvoiceId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT invoice_nu from student_payment order by invoice_nu DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ArrayList<Income> getIncomeByMonth(int month) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sp.c_id, c.year as course_year,s.name,s.grade,u.name,sum(sp.amount*20.00/100) as income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner join course c on sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on c.teacherId = u.id where sp.month = ?  group by sp.c_id,year1 having year1 = ? ", month, LocalDate.now().getYear());
        ArrayList<Income> list = new ArrayList<>();
        while(rs.next()){
            list.add(new Income(rs.getString(1),rs.getInt(2),rs.getString(3),
                    rs.getInt(4),rs.getString(5),rs.getDouble(6)));
        }
        return list;
    }

    @Override
    public HashMap getMonthlyIncomeForInstitute(int year) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sp.month,sum(sp.amount*20.00/100) as income,Year(sp.date) as year1 from " +
                "student_payment sp group by sp.month,year1 having year1 = ?", year);

        HashMap hs = new HashMap();
        while (rs.next()){
            hs.put(rs.getInt(1),rs.getDouble(2));
        }
        return hs;
    }

    @Override
    public ArrayList<Income> getIncomeByMonth(int month, String userId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sp.c_id, c.year as course_year,s.name,s.grade,u.name,sum(sp.amount*0.8) as income,extract(YEAR FROM sp.date) as year1  from student_payment sp inner join course c on sp.c_id = c.id inner join subject s on c.sub_id = s.id inner join user u on c.teacherId = u.id where sp.month = ? AND u.id = ?  group by sp.c_id,year1 having year1 = ? ", month, userId,LocalDate.now().getYear());
        ArrayList<Income> list = new ArrayList<>();
        while(rs.next()){
            list.add(new Income(rs.getString(1),rs.getInt(2),rs.getString(3),
                    rs.getInt(4),rs.getString(5),rs.getDouble(6)));
        }
        return list;
    }

    @Override
    public HashMap getMonthlyIncomeForTeacher(int year, String teacherId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sp.month,sum(sp.amount*0.8) as income,Year(sp.date) as " +
                "year1 from student_payment sp inner join course c on sp.c_id = c.id inner join user u on " +
                "c.teacherId=u.id where u.id = ?  group by sp.month,year1 having year1 = ?",teacherId, year);

        HashMap hs = new HashMap();
        while (rs.next()){
            hs.put(rs.getInt(1),rs.getDouble(2));
        }
        return hs;
    }
}
