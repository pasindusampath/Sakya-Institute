package lk.ijse.sakya.service.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.CourseDTO;

import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Course;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.entity.custom.User;

import java.sql.SQLException;

public interface CourseService {
    boolean addCourse(CourseDTO course) throws SQLException, ClassNotFoundException;
    void sendMail(User teacher, Course course, Subject subject);
    String getNewCourseId() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetailForTables() throws SQLException, ClassNotFoundException;


}
