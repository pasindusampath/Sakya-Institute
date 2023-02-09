package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.CourseDTO;

import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Course;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.SuperService;

import java.sql.SQLException;

public interface CourseService extends SuperService {
    boolean addCourse(CourseDTO course) throws SQLException, ClassNotFoundException;
    void sendMail(User teacher, Course course, Subject subject);
    String getNewCourseId() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetailForTables() throws SQLException, ClassNotFoundException;
    boolean updateCourse(Course course) throws SQLException, ClassNotFoundException;
    boolean deleteCourse(String id) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> searchCourses(String searchBy,String text) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetails() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetails(String searchBy,String text) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetailsByStudentId(String stId) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCoursesByTeacherId(String teacherId) throws SQLException, ClassNotFoundException;

}
