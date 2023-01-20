package lk.ijse.sakya.dao.custom;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.CrudDAO;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Course;
import lk.ijse.sakya.entity.custom.Subject;

import java.sql.SQLException;

public interface CourseDAO extends CrudDAO<Course,String> {
    String getNewCourseId() throws SQLException, ClassNotFoundException;
    String getLastCourseId() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetailForTables() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetails() throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetailsByStudentId(String stId) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCoursesByTeacherId(String teacherId) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> getCourseDetails(String searchBy,String text) throws SQLException, ClassNotFoundException;
    ObservableList<CourseTM> searchCourses(String searchBy,String text) throws SQLException, ClassNotFoundException;
}
