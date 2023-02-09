package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.StudentCourseDAO;
import lk.ijse.sakya.dao.custom.impl.StudentCourseDAOImpl;
import lk.ijse.sakya.dto.CourseTM;

import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.StudentCourse;
import lk.ijse.sakya.service.custom.StudentCourseService;

import java.sql.SQLException;
import java.util.ArrayList;

public class StudentCourseServiceImpl implements StudentCourseService {
    StudentCourseDAO studentCourseDAO = new StudentCourseDAOImpl();

    @Override
    public boolean addRecord(StudentCourse ob) throws SQLException, ClassNotFoundException {
        return studentCourseDAO.add(ob);
    }

    @Override
    public ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException {
        return studentCourseDAO.getCoursesByStudentId(studentId);
    }

    @Override
    public ArrayList<Student> getStudentsByCourseId(CourseTM course) throws SQLException, ClassNotFoundException {
        return studentCourseDAO.getStudentsByCourseId(course);
    }
}
