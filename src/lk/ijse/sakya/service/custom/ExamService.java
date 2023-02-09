package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.entity.custom.Exam;
import lk.ijse.sakya.service.SuperService;

import java.sql.SQLException;

public interface ExamService extends SuperService {
    String getNewExamId() throws SQLException, ClassNotFoundException;
    boolean addExam(Exam exam) throws SQLException, ClassNotFoundException;
    ObservableList<Exam> getAllExamsByClassIdAndModuleId(String cId, String mId) throws SQLException, ClassNotFoundException;
}
