package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.StudentCourseDAO;
import lk.ijse.sakya.dao.custom.StudentDAO;
import lk.ijse.sakya.dao.custom.impl.StudentCourseDAOImpl;
import lk.ijse.sakya.dao.custom.impl.StudentDAOImpl;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.service.custom.StudentCourseService;
import lk.ijse.sakya.service.custom.StudentService;

import java.sql.SQLException;
import java.util.ArrayList;

public class StudentServiceImpl implements StudentService {
    StudentDAO studentDAO = new StudentDAOImpl();
    StudentCourseDAO studentCourseDAO = new StudentCourseDAOImpl();

    @Override
    public String getNewStudentId() throws SQLException, ClassNotFoundException {
        return studentDAO.getNewStudentId();
    }

    @Override
    public ArrayList<String> getStudentsByCourseId(String courseId) throws SQLException, ClassNotFoundException {
        return studentDAO.getStudentsByCourseId(courseId);
    }

    @Override
    public Student searchStudent(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.searchStudent(id);
    }

    @Override
    public ObservableList<Student> getAllStudents() throws SQLException, ClassNotFoundException {
        return studentDAO.getAllStudents();
    }

    @Override
    public boolean addStudent(Student student) throws SQLException, ClassNotFoundException {
        return studentDAO.add(student);
    }

    @Override
    public ObservableList<Student> searchStudent(String searchBy, String text) throws SQLException, ClassNotFoundException {
        return studentDAO.searchStudent(searchBy, text);
    }

    @Override
    public boolean updateStudent(Student student) throws SQLException, ClassNotFoundException {
        return studentDAO.update(student);
    }

    @Override
    public boolean deleteStudent(String id) throws SQLException, ClassNotFoundException {
        return studentDAO.delete(id);
    }

    @Override
    public ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException {
        return studentCourseDAO.getCoursesByStudentId(studentId);
    }




}
