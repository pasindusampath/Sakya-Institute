package lk.ijse.sakya.dto;

public class ExamStudent {
    private String studentId;
    private String examId;
    private double marks;

    public ExamStudent(String studentId, String examId, double marks) {
        this.studentId = studentId;
        this.examId = examId;
        this.marks = marks;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
}
