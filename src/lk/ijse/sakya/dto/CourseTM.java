package lk.ijse.sakya.dto;

public class CourseTM {
    private String id;
    private int year;
    private int grade;
    private String name;
    private String teacherId;
    private String sub_id;
    private double fee;
    private String courseName;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public CourseTM(String id, int year, int grade, String name,String subId) {
        this.id = id;
        this.year = year;
        this.grade = grade;
        this.name = name;
        this.sub_id = subId;
    }

    public CourseTM(String id, int year, int grade, String name, String teacherId, double fee) {
        this.id = id;
        this.year = year;
        this.grade = grade;
        this.name = name;
        this.teacherId = teacherId;
        this.fee = fee;
    }

    public CourseTM(String id, int year, int grade, String name, String teacherId, String sub_id) {
        this.setId(id);
        this.setYear(year);
        this.setGrade(grade);
        this.setName(name);
        this.setTeacherId(teacherId);
        this.setSub_id(sub_id);
    }

    public CourseTM(String id, int year) {
        this.id = id;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getSub_id() {
        return sub_id;
    }

    public void setSub_id(String sub_id) {
        this.sub_id = sub_id;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
