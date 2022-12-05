package lk.ijse.sakya.dto;

public class PaymentTM {


    private String courseId;
    private String courseName;
    private double amount;

    public PaymentTM(String courseId, String courseName, double amount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.amount = amount;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
