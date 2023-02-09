package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.AttendenceDAO;
import lk.ijse.sakya.dao.custom.QueryDAO;
import lk.ijse.sakya.dao.custom.impl.AttendenceDAOImpl;

import lk.ijse.sakya.dao.custom.impl.QueryDAOImpl;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.DateAttendenceTM;
import lk.ijse.sakya.entity.custom.Attendence;
import lk.ijse.sakya.service.custom.AttendenceService;

import java.sql.SQLException;
import java.util.ArrayList;

public class AttendenceServiceImpl implements AttendenceService {
    AttendenceDAO attendenceDAO = new AttendenceDAOImpl();
    QueryDAO queryDAO = new QueryDAOImpl();
    @Override
    public ObservableList<AttendenceTM> getAllAttendenceByLectureId(String lId) throws SQLException, ClassNotFoundException {
        return queryDAO.getAllAttendenceByLectureId(lId);
    }

    @Override
    public boolean updateAttendence(ArrayList<Attendence> list) throws SQLException, ClassNotFoundException {
        return attendenceDAO.updateAttendence(list);
    }

    @Override
    public ObservableList<DateAttendenceTM> getAllAttendenceByStudentAndCourse(String sId, String cId) throws SQLException, ClassNotFoundException {
        return queryDAO.getAllAttendenceByStudentAndCourse(sId, cId);
    }

    @Override
    public ArrayList<DateAttendenceTM> getDatesAndCountOfPresentByCourseId(String cId) throws SQLException, ClassNotFoundException {
        return queryDAO.getDatesAndCountOfPresentByCourseId(cId);
    }

    @Override
    public ObservableList<AttendenceTM> getAttendenceByCourseIdAndDate(String cId, String date) throws SQLException, ClassNotFoundException {
        return queryDAO.getAttendenceByCourseIdAndDate(cId, date);
    }
}
