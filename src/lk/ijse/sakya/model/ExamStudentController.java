package lk.ijse.sakya.model;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.ExamResultTM;
import lk.ijse.sakya.dto.ExamStudent;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
//Done
public class ExamStudentController {
    public static boolean addStudentExamRecord(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        for (ExamStudent ob : list) {
            if (!addStudentExamRecord(ob)) {
                return false;
            }
        }
        return true;
    }

    private static boolean addStudentExamRecord(ExamStudent ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert Into exam_student values(?,?,?)", ob.getStudentId(), ob.getExamId(), ob.getMarks());
    }

    public static ObservableList<ExamResultTM> getExamStudentDetailsByExamId(String examId) throws SQLException, ClassNotFoundException {
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

    public static boolean updateStudentExamResult(ArrayList<ExamStudent> list) throws SQLException, ClassNotFoundException {
        DBConnection.getInstance().getConnection().setAutoCommit(false);
        try {
            for (ExamStudent ob : list) {
                if (!updateStudentExamResult(ob)) {
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

    private static boolean updateStudentExamResult(ExamStudent ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update exam_student set mark = ? where st_id = ? AND exam_id = ?", ob.getMarks()
                , ob.getStudentId(), ob.getExamId());
    }

    public static ObservableList<ExamResultTM> getExamResultsByStudentIdAndCourseId(String courseId, String studentId) throws SQLException, ClassNotFoundException {
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
