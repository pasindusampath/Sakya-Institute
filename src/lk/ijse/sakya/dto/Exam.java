package lk.ijse.sakya.dto;

import java.util.ArrayList;

public class Exam {
    private String examId;
    private String classId;
    private String moduleId;
    private String date;

    public ArrayList<ExamStudent> getExamStudents() {
        return examStudents;
    }

    public void setExamStudents(ArrayList<ExamStudent> examStudents) {
        this.examStudents = examStudents;
    }

    private ArrayList<ExamStudent> examStudents;

    public Exam(String examId, String classId, String moduleId, String date, ArrayList<ExamStudent> examStudents) {
        this.examId = examId;
        this.classId = classId;
        this.moduleId = moduleId;
        this.date = date;
        this.examStudents = examStudents;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
