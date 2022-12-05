package lk.ijse.sakya.dto;

public class Lecture {
    private String id;
    private String date;
    private String cId;

    public Lecture(String id, String date, String cId) {
        this.id = id;
        this.date = date;
        this.cId = cId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }
}
