package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentDAO extends CrudDAO<Student,String> {
    String getNewStudentId() throws SQLException, ClassNotFoundException;
    String getLastStudentId() throws SQLException, ClassNotFoundException;
    ObservableList<Student> getAllStudents() throws SQLException, ClassNotFoundException;
    ArrayList<String> getStudentsByCourseId(String courseId) throws SQLException, ClassNotFoundException;
    ArrayList<Student> getStudentsByCourseId(CourseTM course) throws SQLException, ClassNotFoundException;
    ObservableList<Student> searchStudent(String searchBy, String text) throws SQLException, ClassNotFoundException;


}
