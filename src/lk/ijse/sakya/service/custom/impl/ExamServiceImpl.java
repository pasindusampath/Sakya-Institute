package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ExamDAO;
import lk.ijse.sakya.dao.custom.impl.ExamDAOImpl;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.ExamResultTM;
import lk.ijse.sakya.entity.custom.Exam;
import lk.ijse.sakya.service.custom.ExamService;
import lk.ijse.sakya.service.custom.ExamStudentService;

import java.sql.SQLException;

public class ExamServiceImpl implements ExamService {
    ExamDAO examDAO = new ExamDAOImpl();
    ExamStudentService examStudentService = new ExamStudentServiceImpl();
    @Override
    public String getNewExamId() throws SQLException, ClassNotFoundException {
        return examDAO.getNewExamId();
    }

    @Override
    public boolean addExam(Exam exam) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean flag = examDAO.add(exam);
            if (flag) {
                boolean flag2 = examStudentService.addStudentExamRecord(exam.getExamStudents());
                if (flag2) {
                    DBConnection.getInstance().getConnection().commit();
                    return true;
                } else {
                    DBConnection.getInstance().getConnection().rollback();
                }
            } else {
                DBConnection.getInstance().getConnection().rollback();
            }
        }catch (SQLException e){
            DBConnection.getInstance().getConnection().rollback();
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }

        return false;
    }

    @Override
    public ObservableList<Exam> getAllExamsByClassIdAndModuleId(String cId, String mId) throws SQLException, ClassNotFoundException {
        return examDAO.getAllExamsByClassIdAndModuleId(cId, mId);
    }

}
