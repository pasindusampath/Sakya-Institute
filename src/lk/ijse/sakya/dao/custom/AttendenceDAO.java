package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.AttendenceTM;
import lk.ijse.sakya.dto.DateAttendenceTM;
import lk.ijse.sakya.entity.custom.Attendence;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AttendenceDAO extends CrudDAO<Attendence,String> {
    boolean add(ArrayList<Attendence> ob) throws SQLException, ClassNotFoundException;
    boolean updateAttendence(ArrayList<Attendence> list) throws SQLException, ClassNotFoundException;


    }
