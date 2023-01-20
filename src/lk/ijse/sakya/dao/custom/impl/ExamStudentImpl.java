package lk.ijse.sakya.dao.custom.impl;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.ExamStudentDAO;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.ExamResultTM;
import lk.ijse.sakya.entity.custom.ExamStudent;
import lk.ijse.sakya.model.ExamStudentController;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExamStudentImpl implements ExamStudentDAO {

    @Override
    public boolean add(ExamStudent ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert Into exam_student values(?,?,?)", ob.getStudentId(), ob.getExamId(), ob.getMarks());
    }

    @Override
    public boolean update(ExamStudent ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update exam_student set mark = ? where st_id = ? AND exam_id = ?", ob.getMarks()
                , ob.getStudentId(), ob.getExamId());
    }

    @Override
    public ExamStudent search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean add(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        for (ExamStudent ob : list) {
            if (!add(ob)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ObservableList<ExamResultTM> getExamStudentDetailsByExamId(String examId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT s.id,s.name,es.mark from exam_student es inner join student s on es.st_id = s.id" +
                " where es.exam_id = ?", examId);
        ObservableList<ExamResultTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            String mark = rs.getString(3);
            if (mark.equals("-1.00")) {
                mark = "AB";
            }
            JFXTextField temp = new JFXTextField(mark);
            //temp.setStyle("../stylesheet/");
            temp.getStylesheets().add(ExamStudentController.class.getResource("../stylesheet/TextField.css").toExternalForm());
            list.add(new ExamResultTM(rs.getString(1), rs.getString(2), temp));
        }
        return list;
    }

    @Override
    public boolean updateStudentExamResult(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try {
            for (ExamStudent ob : list) {
                if (!update(ob)) {
                    DBConnection.getInstance().getConnection().rollback();
                    return false;
                }
            }
            DBConnection.getInstance().getConnection().commit();
        } catch (SQLException e) {
            DBConnection.getInstance().getConnection().rollback();
            return false;
        } finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }

        return true;

    }

    @Override
    public ObservableList<ExamResultTM> getExamResultsByStudentIdAndCourseId(String courseId, String studentId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT e.id as exam_id,m.name as module_name, es.mark as result " +
                "from exam_student es inner join exam e on es.exam_id = e.id inner join module m on e.m_id " +
                "= m.id where es.st_id = ? And e.class_id = ?",studentId,courseId);
        ObservableList<ExamResultTM> list = FXCollections.observableArrayList();
        while (rs.next()) {
            String mark = rs.getString(3);
            if (mark.equals("-1.00")) {
                mark = "AB";
            }
            JFXTextField temp = new JFXTextField(mark);
            //temp.setStyle("../stylesheet/");
            temp.setEditable(false);
            temp.getStylesheets().add(ExamStudentController.class.getResource("../stylesheet/TextField.css")
                    .toExternalForm());
            list.add(new ExamResultTM(rs.getString(1), rs.getString(2), temp));
        }
        return list;
    }
}
