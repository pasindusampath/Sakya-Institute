package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.StudentCourseDAO;
import lk.ijse.sakya.dao.util.CrudUtil;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.entity.custom.StudentCourse;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentCourseDAOImpl implements StudentCourseDAO {
    @Override
    public boolean add(StudentCourse ob) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Insert Into student_course VALUES(?,?,?)",ob.getStId(),ob.getcId(),
                ob.getDate());
    }

    @Override
    public boolean update(StudentCourse obj) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public StudentCourse search(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public ObservableList<CourseTM> getCoursesByStudentId(String studentId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT sc.c_id,c.year from student_course sc Inner Join Course c on c.id = " +
                "sc.c_id where sc.st_id = ?",studentId);
        ObservableList <CourseTM> ol = FXCollections.observableArrayList();
        while(rs.next()){
            ol.add(new CourseTM(rs.getString(1), rs.getInt(2)));
        }
        return ol;
    }

    @Override
    public ArrayList<Student> getStudentsByCourseId(CourseTM course) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT s.id,s.name,s.dob,s.address,s.contact,s.gmail,s.p_mail,s.p_contact" +
                " from student_course sc Inner Join student s ON sc.st_id =s.id where sc.c_id = ?",course.getId());
        ArrayList<Student> list = new ArrayList<>();
        while(rs.next()){
            Student ob = new Student(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                    rs.getString(8));
            list.add(ob);
        }
        return list;
    }
}
