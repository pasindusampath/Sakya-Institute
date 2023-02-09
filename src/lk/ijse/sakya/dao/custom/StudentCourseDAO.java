package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.StudentCourse;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentCourseDAO extends CrudDAO<StudentCourse, String> {
    ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException;
    ArrayList<Student> getStudentsByCourseId(CourseTM course) throws SQLException, ClassNotFoundException;
}
