package lk.ijse.sakya.service.custom;



import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.service.SuperService;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentService extends SuperService {
    String getNewStudentId() throws SQLException, ClassNotFoundException;
    ArrayList<String> getStudentsByCourseId(String courseId) throws SQLException, ClassNotFoundException;
    Student searchStudent(String id)  throws SQLException, ClassNotFoundException;
    ObservableList<Student> getAllStudents() throws SQLException, ClassNotFoundException;
    boolean addStudent(Student student) throws SQLException, ClassNotFoundException;
    ObservableList<Student> searchStudent(String searchBy,String text) throws SQLException, ClassNotFoundException;
    boolean updateStudent(Student selectedStudent) throws SQLException, ClassNotFoundException;

    boolean deleteStudent(String id)throws SQLException, ClassNotFoundException;

    ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException;


}
