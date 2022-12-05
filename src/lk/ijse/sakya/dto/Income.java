package lk.ijse.sakya.dto;

public class Income {
    private String courseId;
    private int year;
    private String subName;
    private int grade;
    private String teacherName;
    private double amount;

    public Income(String courseId, int year, String subName, int grade, String teacherName, double amount) {
        this.courseId = courseId;
        this.year = year;
        this.subName = subName;
        this.grade = grade;
        this.teacherName = teacherName;
        this.amount = amount;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
