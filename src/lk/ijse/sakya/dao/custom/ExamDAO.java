package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.entity.custom.Exam;

import java.sql.SQLException;

public interface ExamDAO extends CrudDAO<Exam,String> {
    String getNewExamId() throws SQLException, ClassNotFoundException;
    String getLastExamId() throws SQLException, ClassNotFoundException;
    ObservableList<Exam> getAllExamsByClassIdAndModuleId(String cId, String mId) throws SQLException, ClassNotFoundException;
}
