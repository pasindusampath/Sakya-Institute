package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.ExamResultTM;
import lk.ijse.sakya.entity.custom.ExamStudent;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ExamStudentService {
    ObservableList<ExamResultTM> getExamResultsByStudentIdAndCourseId(String courseId, String studentId) throws
            SQLException, ClassNotFoundException;
    ObservableList<ExamResultTM> getExamStudentDetailsByExamId(String examId) throws SQLException,
            ClassNotFoundException;
    boolean updateStudentExamResult(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException;
    boolean addStudentExamRecord(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException;
}
