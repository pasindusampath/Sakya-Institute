package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ExamStudentDAO;
import lk.ijse.sakya.dao.custom.impl.ExamStudentDAOImpl;

import lk.ijse.sakya.dto.ExamResultTM;

import lk.ijse.sakya.entity.custom.ExamStudent;
import lk.ijse.sakya.service.custom.ExamStudentService;

import java.sql.SQLException;
import java.util.ArrayList;

public class ExamStudentServiceImpl implements ExamStudentService {
    ExamStudentDAO examStudentDAO = new ExamStudentDAOImpl();

    @Override
    public ObservableList<ExamResultTM> getExamResultsByStudentIdAndCourseId(String courseId, String studentId) throws SQLException, ClassNotFoundException {
        return examStudentDAO.getExamResultsByStudentIdAndCourseId(courseId, studentId);
    }

    @Override
    public ObservableList<ExamResultTM> getExamStudentDetailsByExamId(String examId) throws SQLException, ClassNotFoundException {
        return examStudentDAO.getExamStudentDetailsByExamId(examId);
    }

    @Override
    public boolean updateStudentExamResult(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        return examStudentDAO.updateStudentExamResult(list);
    }

    @Override
    public boolean addStudentExamRecord(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        return examStudentDAO.add(list);
    }
}
