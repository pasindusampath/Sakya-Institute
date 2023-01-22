package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.LectureDAO;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Attendence;
import lk.ijse.sakya.dto.LectureTM;
import lk.ijse.sakya.entity.custom.Lecture;
import lk.ijse.sakya.model.AttendenceController;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LectureDAOImpl implements LectureDAO {
    @Override
    public boolean add(Lecture obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("No Implemented");
    }

    @Override
    public boolean update(Lecture obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("No Implemented");
    }

    @Override
    public Lecture search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("No Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("No Implemented");
    }

    @Override
    public String getNewLectureId() throws SQLException, ClassNotFoundException {
        String lastLectureId = getLastLectureId();
        if (lastLectureId == null) {
            return "L-0000000000001";
        } else {
            String[] split = lastLectureId.split("[L][-]");
            int lastDigits = Integer.parseInt(split[1]);
            lastDigits++;
            String newLectureId = String.format("L-%013d", lastDigits);
            return newLectureId;
        }
    }

    @Override
    public String getLastLectureId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from lecture order by id DESC limit 1");
        if (rs.next()) {
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public boolean add(Lecture lecture, ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean flag = CrudUtil.execute("insert into lecture values(?,?,?)", lecture.getId(), lecture.getDate(),
                    lecture.getcId());
            if (flag) {
                boolean flag2 = AttendenceController.addAttendence(ob);
                if (flag2) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                }
                DBConnection.getInstance().getConnection().rollback();
            }
            return false;
        } catch (SQLException e) {
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        }catch(ClassNotFoundException e) {
            DBConnection.getInstance().getConnection().rollback();
            throw e;
        } finally{
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    @Override
    public ObservableList<LectureTM> getLecturesByDate(String date) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT l.id,c.year,s.name,s.grade,u.name from lecture l inner join course c on l.c_id " +
                        "= c.id inner join subject s on c.sub_id = s.id inner join user u ON c.teacherId = u.id where l.date = ?;",
                date);

        ObservableList<LectureTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            String name = rs.getString(2) + "-" + rs.getString(4) + "-" + rs.getString(3);
            list.add(new LectureTM(rs.getString(1), name, rs.getString(5)));
        }

        return list;
    }

    @Override
    public boolean isLectureAlreadyAdded(String cId, String date) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from lecture where c_id = ? AND date = ?", cId, date);
        if(rs.next()){
            return true;
        }
        return false;
    }
}
