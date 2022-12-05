package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.Exam;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExamController {
    public static boolean addExam(Exam exam) throws SQLException, ClassNotFoundException {
        try {
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            boolean flag = CrudUtil.execute("insert into exam values (?,?,?,?)", exam.getExamId(), exam.getClassId(),
                    exam.getModuleId(), exam.getDate());
            if (flag) {
                boolean flag2 = ExamStudentController.addStudentExamRecord(exam.getExamStudents());
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


    public static String getNewExamId() throws SQLException, ClassNotFoundException {
        String lastExamId=getLastExamId();
        if(lastExamId==null){
            return "E-0000000000001";
        }else{
            String[] split=lastExamId.split("[E][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newExamId=String.format("E-%013d", lastDigits);
            return newExamId;
        }
    }

    private static String getLastExamId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from exam order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static ObservableList<Exam> getAllExamsByClassIdAndModuleId(String cId,String mId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from exam where class_id = ? AND m_id = ? ", cId, mId);
        ObservableList<Exam> list = FXCollections.observableArrayList();
        while(rs.next()){
            list.add(new Exam(rs.getString(1),rs.getString(2), rs.getString(3),
                    rs.getString(4),null));
        }
        return list;
    }
}
