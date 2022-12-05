package lk.ijse.sakya.dto;

public class AttendenceTM {
    private String studentId;
    private String name;
    private String status;
    private String payment;

    public AttendenceTM(String studentId, String name, String status) {
        this.studentId = studentId;
        this.name = name;
        this.status = status;
    }

    public AttendenceTM(String sId, String name, String status, String payment) {
        this.studentId = sId;
        this.name = name;
        this.status = status;
        this.payment = payment;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String sId) {
        this.studentId = sId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }
}
