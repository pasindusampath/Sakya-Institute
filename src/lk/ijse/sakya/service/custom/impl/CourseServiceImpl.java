package lk.ijse.sakya.service.custom.impl;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import lk.ijse.sakya.dao.custom.CourseDAO;

import lk.ijse.sakya.dao.custom.impl.CourseDAOImpl;
import lk.ijse.sakya.dto.CourseDTO;


import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.entity.custom.Course;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.entity.custom.User;
import lk.ijse.sakya.service.custom.CourseService;
import lk.ijse.sakya.thread.SendMail;

import java.sql.SQLException;

public class CourseServiceImpl implements CourseService {
    CourseDAO ob;

    public CourseServiceImpl(){
        ob=new CourseDAOImpl();
    }


    @Override
    public boolean addCourse(CourseDTO course) throws SQLException, ClassNotFoundException {
        boolean add = ob.add(course.getCourse());
        if(add){
            sendMail(course.getUser(), course.getCourse(), course.getSubject());
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void sendMail(User teacher, Course course, Subject subject){
        SendMail o = new SendMail(teacher.getGmail(),"Your Course Added Successful",
                "Course ID : "+course.getId()+"<br></br>"+
                        "Subject ID : "+course.getSubId()+"<br></br>"+
                        "Grade     : "+subject.getGrade()+"<br></br>"+
                        "Year      :"+course.getYear());
        o.valueProperty().addListener((q, oldValue, newValue) -> {
            if(newValue) {
                new Alert(Alert.AlertType.INFORMATION,"Mail Sent Successful").show();
            }
        });
        Thread t = new Thread(o);
        t.start();
    }

    @Override
    public String getNewCourseId() throws SQLException, ClassNotFoundException {
        return ob.getNewCourseId();
    }

    @Override
    public ObservableList<CourseTM> getCourseDetailForTables() throws SQLException, ClassNotFoundException {
        return ob.getCourseDetailForTables();
    }

    @Override
    public boolean updateCourse(Course course) throws SQLException, ClassNotFoundException {
        return ob.update(new Course(course.getId(),course.getSubId(),course.getYear(),course.getTeacherId(),course.getYear()));
    }

    @Override
    public boolean deleteCourse(String id) throws SQLException, ClassNotFoundException {
        return ob.delete(id);
    }

    @Override
    public ObservableList<CourseTM> searchCourses(String searchBy, String text) throws SQLException, ClassNotFoundException {
        return ob.searchCourses(searchBy,text);
    }

    @Override
    public ObservableList<CourseTM> getCourseDetails() throws SQLException, ClassNotFoundException {
        return ob.getCourseDetails();
    }

    @Override
    public ObservableList<CourseTM> getCourseDetails(String searchBy, String text) throws SQLException, ClassNotFoundException {
        return ob.getCourseDetails(searchBy,text);
    }

    @Override
    public ObservableList<CourseTM> getCourseDetailsByStudentId(String stId) throws SQLException, ClassNotFoundException {
        return ob.getCourseDetailsByStudentId(stId);
    }

    @Override
    public ObservableList<CourseTM> getCoursesByTeacherId(String teacherId) throws SQLException, ClassNotFoundException {
        return ob.getCoursesByTeacherId(teacherId);
    }
}
