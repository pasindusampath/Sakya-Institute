package lk.ijse.sakya.entity.custom;

import lk.ijse.sakya.entity.SuperEntity;

public class StudentCourse implements SuperEntity {
    private String stId;
    private String cId;
    private String date;

    public StudentCourse(String stId, String cId, String date) {
        this.stId = stId;
        this.cId = cId;
        this.date = date;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
