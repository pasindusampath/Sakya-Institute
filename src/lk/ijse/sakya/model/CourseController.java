package lk.ijse.sakya.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lk.ijse.sakya.dto.Course;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
//Done
public class CourseController {
    public static String getNewCourseId() throws SQLException, ClassNotFoundException {
        String lastCourseId=getLastCourseId();
        if(lastCourseId==null){
            return "Course-00000001";
        }else{
            String[] split=lastCourseId.split("[C][o][u][r][s][e][-]");
            int lastDigits=Integer.parseInt(split[1]);
            lastDigits++;
            String newCourseId=String.format("Course-%08d", lastDigits);
            return newCourseId;
        }
    }

    private static String getLastCourseId() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT id from course order by id DESC limit 1");
        if(rs.next()){
            return rs.getString(1);
        }
        return null;
    }


    public static boolean addCourse(Course course) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into course values(?,?,?,?,?)",course.getId(),course.getSubId(),
                course.getYear(),course.getTeacherId(),course.getFee());
    }

    public static boolean updateCourse(Course course) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update course set sub_id = ?,year = ? ,teacherId = ? where id = ?"
        ,course.getSubId(),course.getYear(),course.getTeacherId(),course.getId());
    }


    public static ObservableList<CourseTM> getCourseDetailForTables() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,c.year,s.grade,s.name,c.teacherId,s.id from course c " +
                "Inner join subject s ON c.sub_id = s.id;");
        ObservableList<CourseTM> temp = FXCollections.observableArrayList();
        while(rs.next()){
            String id = rs.getString(1);
            int year = rs.getInt(2);
            int grade = rs.getInt(3);
            String subject = rs.getString(4);
            String teacherId = rs.getString(5);
            String subjectId = rs.getString(6);
            temp.add(new CourseTM(id,year,grade,subject,teacherId,subjectId));
        }
        return temp;
    }
    public static ObservableList<CourseTM> getCourseDetails() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,s.grade,s.name,c.year,u.name,s.id from course c inner join subject s ON c.sub_id = s.id inner join user u ON c.teacherId = u.id");
        ObservableList<CourseTM> temp = FXCollections.observableArrayList();
        while(rs.next()){
            String id = rs.getString(1);
            int grade = rs.getInt(2);
            String subject = rs.getString(3);
            int year = rs.getInt(4);
            String teacherId = rs.getString(5);
            String subjectId = rs.getString(6);
            temp.add(new CourseTM(id,year,grade,subject,teacherId,subjectId));
        }
        return temp;
    }
    public static boolean deleteCourse(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from course where id = ? ",id);
    }

    public static ObservableList<CourseTM> getCourseDetailsByStudentId(String stId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,c.year,s.grade,s.name,u.name,c.monthly_fee from course c inner join subject s on c.sub_id " +
                "= s.id inner join user u on  c.teacherId = u.id inner join student_course sc on c.id = sc.c_id where " +
                "sc.st_id =?", stId);
        ObservableList<CourseTM> list = FXCollections.observableArrayList();
        while(rs.next()) {
            String cId = rs.getString(1);
            int year = rs.getInt(2);
            int grade = rs.getInt(3);
            String suName = rs.getString(4);
            String tName = rs.getString(5);
            double fee = rs.getDouble(6);
            CourseTM ob = new CourseTM(cId,year,grade,suName,tName,fee);
            list.add(ob);
        }
        return list;
    }

    public static ObservableList<CourseTM> getCoursesByTeacherId(String teacherId) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,c.year,s.grade,s.name,s.id from course c inner join " +
                "subject s on c.sub_id = s.id where c.teacherId = ?", teacherId);
        ObservableList<CourseTM> list = FXCollections.observableArrayList();
        while (rs.next()){
            list.add(new CourseTM(rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getString(4),rs.getString(5)));
        }
        return list;
    }

    public static ObservableList<CourseTM> getCourseDetails(String searchBy,String text) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,s.grade,s.name,c.year,u.name,s.id from course c inner" +
                " join subject s ON c.sub_id = s.id inner join user u ON c.teacherId = u.id where "+searchBy+" like '%"+text+"%'");
        ObservableList<CourseTM> temp = FXCollections.observableArrayList();
        while(rs.next()){
            String id = rs.getString(1);
            int grade = rs.getInt(2);
            String subject = rs.getString(3);
            int year = rs.getInt(4);
            String teacherId = rs.getString(5);
            String subjectId = rs.getString(6);
            temp.add(new CourseTM(id,year,grade,subject,teacherId,subjectId));
        }
        return temp;
    }

    public static ObservableList<CourseTM> searchCourses(String searchBy,String text) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT c.id,c.year,s.grade,s.name,c.teacherId,s.id from course c " +
                "Inner join subject s ON c.sub_id = s.id where "+searchBy+" like '%"+text+"%';");
        ObservableList<CourseTM> temp = FXCollections.observableArrayList();
        while(rs.next()){
            String id = rs.getString(1);
            int year = rs.getInt(2);
            int grade = rs.getInt(3);
            String subject = rs.getString(4);
            String teacherId = rs.getString(5);
            String subjectId = rs.getString(6);
            temp.add(new CourseTM(id,year,grade,subject,teacherId,subjectId));
        }
        return temp;
    }
}
