package lk.ijse.sakya.dto;

public class DateAttendenceTM {
    private String date;
    private String status;
    private int count;

    public DateAttendenceTM(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public DateAttendenceTM(String date, String status) {
        this.date = date;
        this.status = status;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
