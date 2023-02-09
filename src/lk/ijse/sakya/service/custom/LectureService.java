package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;

import lk.ijse.sakya.dto.LectureTM;
import lk.ijse.sakya.entity.custom.Attendence;
import lk.ijse.sakya.entity.custom.Lecture;
import lk.ijse.sakya.service.SuperService;


import java.sql.SQLException;
import java.util.ArrayList;

public interface LectureService extends SuperService {
    boolean isLectureAlreadyAdded(String cId,String date) throws SQLException, ClassNotFoundException;
    boolean addLecture(Lecture lecture, ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException;

    String getNewLectureId()throws SQLException, ClassNotFoundException;

    ObservableList<LectureTM> getLecturesByDate(String date) throws SQLException, ClassNotFoundException;
}
