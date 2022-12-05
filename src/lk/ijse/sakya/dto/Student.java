package lk.ijse.sakya.dto;

public class Student {
    private String id;
    private String name;
    private String dob;
    private String address;
    private String contact;
    private String gmail;
    private String p_gmail;
    private String p_contact;

    public Student(String id, String name, String dob, String address, String contact, String gmail,
                   String p_gmail, String p_contact) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.contact = contact;
        this.gmail = gmail;
        this.p_gmail = p_gmail;
        this.p_contact = p_contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getP_gmail() {
        return p_gmail;
    }

    public void setP_gmail(String p_gmail) {
        this.p_gmail = p_gmail;
    }

    public String getP_contact() {
        return p_contact;
    }

    public void setP_contact(String p_contact) {
        this.p_contact = p_contact;
    }
}
