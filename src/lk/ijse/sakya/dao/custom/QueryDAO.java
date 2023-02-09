package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.SuperDAO;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.DateAttendenceTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface QueryDAO extends SuperDAO {
    ObservableList<AttendenceTM> getAllAttendenceByLectureId(String lId) throws SQLException, ClassNotFoundException;
    ObservableList<DateAttendenceTM> getAllAttendenceByStudentAndCourse(String sId, String cId) throws SQLException, ClassNotFoundException;
    ArrayList<DateAttendenceTM> getDatesAndCountOfPresentByCourseId(String cId) throws SQLException, ClassNotFoundException ;
    ObservableList<AttendenceTM> getAttendenceByCourseIdAndDate(String cId, String date) throws SQLException, ClassNotFoundException;
}
