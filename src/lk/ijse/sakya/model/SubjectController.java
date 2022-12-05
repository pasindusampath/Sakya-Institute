package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.Subject;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubjectController {
    public static boolean addSubject(Subject sub) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into subject values(?,?,?)",sub.getId(),sub.getName(),sub.getGrade());
    }

    public static String getNewUserId() throws SQLException, ClassNotFoundException {
        String lastOrderId=getLastSubjectId();
        if(lastOrderId==null){
            return "S-001";
        }else{
            String[] split=lastOrderId.split("[S][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newOrderId=String.format("S-%03d", lastDigits);
            return newOrderId;
        }
    }

    private static String getLastSubjectId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from subject order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    public static ObservableList<Subject> getSubjects(int grade) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from subject where grade = ?",grade);
        ObservableList<Subject> sList = FXCollections.observableArrayList();
        while(rs.next()){
            Subject temp = new Subject(rs.getString(1),rs.getString(2),rs.getInt(3));
            sList.add(temp);
        }
        return sList;
    }

    public static boolean updateSubject(Subject subject) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update subject set name = ? , grade = ? where id = ?",subject.getName(),
                subject.getGrade(),subject.getId());

    }

    public static boolean deleteSubject(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from subject where id = ?", id);
    }
}
