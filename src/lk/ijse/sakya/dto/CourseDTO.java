package lk.ijse.sakya.dto;

import lk.ijse.sakya.entity.custom.Course;
import lk.ijse.sakya.entity.custom.Subject;
import lk.ijse.sakya.entity.custom.User;

public class CourseDTO {
    private Course course;
    private User user;
    private Subject subject;

    public CourseDTO(Course course, User user, Subject subject) {
        this.course = course;
        this.user = user;
        this.subject = subject;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
