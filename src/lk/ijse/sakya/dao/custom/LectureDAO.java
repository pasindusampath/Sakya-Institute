package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.Attendence;
import lk.ijse.sakya.dto.LectureTM;
import lk.ijse.sakya.entity.custom.Lecture;

import java.sql.SQLException;
import java.util.ArrayList;

public interface LectureDAO extends CrudDAO<Lecture,String> {
    String getNewLectureId() throws SQLException, ClassNotFoundException;
    String getLastLectureId() throws SQLException, ClassNotFoundException;
    boolean add(Lecture lecture, ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException;
    ObservableList<LectureTM> getLecturesByDate(String date) throws SQLException, ClassNotFoundException;
    boolean isLectureAlreadyAdded(String cId,String date) throws SQLException, ClassNotFoundException;
}
