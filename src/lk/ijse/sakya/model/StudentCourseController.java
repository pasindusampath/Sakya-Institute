package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.StudentCourse;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCourseController {
    public static boolean addRecord(StudentCourse ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert Into student_course VALUES(?,?,?)",ob.getStId(),ob.getcId(),
                ob.getDate());
    }

    public static ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sc.c_id,c.year from student_course sc Inner Join Course c on c.id = " +
                "sc.c_id where sc.st_id = ?",studentId);
        ObservableList <CourseTM> ol = FXCollections.observableArrayList();
        while(rs.next()){
            ol.add(new CourseTM(rs.getString(1), rs.getInt(2)));
        }
        return ol;
    }
}
