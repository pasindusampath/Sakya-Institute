package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.CourseTM;

import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.StudentCourse;
import lk.ijse.sakya.service.SuperService;


import java.sql.SQLException;
import java.util.ArrayList;

public interface StudentCourseService extends SuperService {
    boolean addRecord(StudentCourse ob) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException;
    ArrayList<Student> getStudentsByCourseId(CourseTM course) throws SQLException, ClassNotFoundException;

}
