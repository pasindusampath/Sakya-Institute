package lk.ijse.sakya.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dao.custom.StudentDAO;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Student;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentDAOImpl implements StudentDAO {
    @Override
    public boolean add(Student student) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into student values(?,?,?,?,?,?,?,?)",student.getId(),
                student.getName(),student.getDob(),student.getAddress(),student.getContact(),
                student.getGmail(),student.getP_gmail(),student.getP_contact());
    }

    @Override
    public boolean update(Student student) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update student set name = ? ,dob = ?,address = ?, contact = ? ,gmail = ?, " +
                        "p_mail = ? , p_contact = ? where id = ?",student.getName(),student.getDob(),student.getAddress(),
                student.getContact(),student.getGmail(),student.getP_gmail(),student.getP_contact(),student.getId());
    }

    @Override
    public Student search(String id) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from student where id = ?", id);
        Student temp = null;
        if(rs.next()) {
            temp = new Student(rs.getString(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),
                    rs.getString(8));
        }
        return temp;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from student where id = ? ",id);
    }

    @Override
    public String getNewStudentId() throws SQLException, ClassNotFoundException {
        String lastStudentId=getLastStudentId();
        if(lastStudentId==null){
            return "St-000000000001";
        }else{
            String[] split=lastStudentId.split("[S][t][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newStudentId=String.format("St-%012d", lastDigits);
            return newStudentId;
        }
    }

    @Override
    public String getLastStudentId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from student order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }

    @Override
    public ObservableList<Student> getAllStudents() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from student");
        ObservableList<Student> list = FXCollections.observableArrayList();
        while (rs.next()){
            Student temp = new Student(rs.getString(1), rs.getString(2),rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                    rs.getString(8) );
            list.add(temp);
        }
        return list;
    }

    @Override
    public ArrayList<String> getStudentsByCourseId(String courseId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT s.id from student_course sc Inner Join " +
                "student s ON sc.st_id =s.id where sc.c_id = ?",courseId);
        ArrayList<String> list = new ArrayList<>();
        while (rs.next()){
            list.add(rs.getString(1));
        }
        return list;
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

    @Override
    public ObservableList<Student> searchStudent(String searchBy, String text) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT * from student where " + searchBy + " like ?", text);
        ObservableList<Student> list = FXCollections.observableArrayList();
        while (rs.next()){
            Student temp = new Student(rs.getString(1), rs.getString(2),rs.getString(3),
                    rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),
                    rs.getString(8));
            list.add(temp);
        }
        return list;
    }
}
