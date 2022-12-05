package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Attendence;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.DateAttendenceTM;
import lk.ijse.sakya.dto.Payment;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AttendenceController {
    public static boolean addAttendence(ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException {
        for (Attendence obj : ob) {
            if (!addAttendence(obj)) {
                return false;
            }
        }
        return true;
    }

    public static boolean addAttendence(Attendence ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into attendence values(?,?,?)", ob.getlId(), ob.getsId(),
                ob.getStatus());
    }

    public static ObservableList<AttendenceTM> getAllAttendenceByLectureId(String lId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT a.st_id,s.name,a.status,c.id from attendence a inner join student" +
                " s on a.st_id = s.id inner join lecture l on a.lec_id = l.id inner join course c on l.c_id = c.id " +
                "where a.lec_id = ?", lId);
        ObservableList<AttendenceTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            if (PaymentController.isAlreadyPaid(new Payment(rs.getString(1), rs.getString(4), LocalDate.now().getMonthValue()))) {
                list.add(new AttendenceTM(rs.getString(1), rs.getString(2), rs.getString(3), "Done"));
            } else {
                list.add(new AttendenceTM(rs.getString(1), rs.getString(2), rs.getString(3), "Not Yet"));
            }
            //System.out.println(rs.getString(1) +" - "+ rs.getString(2) +" - "+  rs.getString(3));
        }

        return list;
    }

    public static boolean updateAttendence(ArrayList<Attendence> list) throws SQLException, ClassNotFoundException {
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try{
            for (Attendence ob : list) {
                if (!updateAttendence(ob)) {
                    DBConnection.getInstance().getConnection().rollback();
                    return false;
                }
            }
            DBConnection.getInstance().getConnection().commit();
            return true;
        }catch (SQLException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }catch (ClassNotFoundException e){
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    private static boolean updateAttendence(Attendence ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update attendence set status = ? where lec_id = ? AND st_id = ?",
                ob.getStatus(), ob.getlId(), ob.getsId());
    }

    public static ObservableList<DateAttendenceTM> getAllAttendenceByStudentAndCourse(String sId, String cId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT l.date,a.status from lecture l inner join attendence a on l.id = a.lec_id inner join course c on l.c_id = c.id  where \n" +
                "a.st_id = ? AND c.id = ?", sId, cId);
        ObservableList<DateAttendenceTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            list.add(new DateAttendenceTM(rs.getString(1), rs.getString(2)));
        }
        return list;
    }

    public static ArrayList<DateAttendenceTM> getDatesAndCountOfPresentByCourseId(String cId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT l.date,count(a.status) from attendence a inner join lecture l on a.lec_id = l.id" +
                " inner join course c on l.c_id = c.id where c.id = ? AND  a.status = 'P'  group by " +
                "l.date;", cId);
        ArrayList<DateAttendenceTM> list = new ArrayList<>();
        while (rs.next()) {
            list.add(new DateAttendenceTM(rs.getString(1), rs.getInt(2)));
        }
        return list;
    }

    public static ObservableList<AttendenceTM> getAttendenceByCourseIdAndDate(String cId, String date) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT a.st_id,s.name,a.status from attendence a inner join student s on" +
                " a.st_id = s.id inner join lecture l on a.lec_id = l.id inner join course c on l.c_id = c.id where" +
                " c.id = ? AND l.date = ?", cId, date);
        ObservableList<AttendenceTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            AttendenceTM ob = new AttendenceTM(rs.getString(1), rs.getString(2),
                    rs.getString(3));
            list.add(ob);
        }
        return list;
    }

}
