package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.AttendenceDAO;
import lk.ijse.sakya.dao.custom.LectureDAO;
import lk.ijse.sakya.dao.custom.impl.AttendenceDAOImpl;
import lk.ijse.sakya.dao.custom.impl.LectureDAOImpl;

import lk.ijse.sakya.db.DBConnection;



import lk.ijse.sakya.dto.LectureTM;
import lk.ijse.sakya.entity.custom.Attendence;
import lk.ijse.sakya.entity.custom.Lecture;
import lk.ijse.sakya.service.custom.LectureService;

import java.sql.SQLException;
import java.util.ArrayList;

public class LectureServiceImpl implements LectureService {
    LectureDAO lectureDAO;
    AttendenceDAO attendenceDAO;

    public LectureServiceImpl() {
        lectureDAO=new LectureDAOImpl();
        attendenceDAO = new AttendenceDAOImpl();
    }

    @Override
    public boolean isLectureAlreadyAdded(String cId, String date) throws SQLException, ClassNotFoundException {
        return lectureDAO.isLectureAlreadyAdded(cId,date);
    }

    @Override
    public boolean addLecture(Lecture lecture, ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean flag = lectureDAO.add(lecture);
            if (flag) {
                boolean flag2 = attendenceDAO.add(ob); ;
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
    public String getNewLectureId() throws SQLException, ClassNotFoundException {
        return lectureDAO.getNewLectureId();
    }

    @Override
    public ObservableList<LectureTM> getLecturesByDate(String date) throws SQLException, ClassNotFoundException {
        return lectureDAO.getLecturesByDate(date);
    }
}
