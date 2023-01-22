package lk.ijse.sakya.entity.custom;

import lk.ijse.sakya.entity.SuperEntity;

public class Payment implements SuperEntity {
    private String invoiceId;
    private String studentId;
    private String courseId;
    private int month;
    private String date;
    private double amount;

    public Payment(String studentId, String courseId, int month) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.month = month;
    }

    public Payment(String studentId, String courseId, int month, String date, double amount, String invoiceId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.month = month;
        this.date = date;
        this.amount = amount;
        this.invoiceId = invoiceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }
}
