package lk.ijse.sakya.dto;

public class Attendence {
    private String lId;
    private String sId;
    private String status;
    public Attendence(String lId, String sId, String status) {
        this.lId = lId;
        this.sId = sId;
        this.status = status;
    }

    public String getlId() {
        return lId;
    }

    public void setlId(String lId) {
        this.lId = lId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
