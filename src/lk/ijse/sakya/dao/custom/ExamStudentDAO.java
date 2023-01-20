package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.ExamResultTM;
import lk.ijse.sakya.entity.custom.ExamStudent;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ExamStudentDAO extends CrudDAO<ExamStudent,String> {
    boolean add(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException;
    ObservableList<ExamResultTM> getExamStudentDetailsByExamId(String examId) throws SQLException, ClassNotFoundException;
    boolean updateStudentExamResult(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException;
    ObservableList<ExamResultTM> getExamResultsByStudentIdAndCourseId(String courseId, String studentId) throws SQLException, ClassNotFoundException;
}
