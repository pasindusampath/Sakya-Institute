package lk.ijse.sakya.entity.custom;

import lk.ijse.sakya.entity.SuperEntity;

public class User implements SuperEntity {
    private String id;
    private String name;
    private String type;
    private String gmail;
    private String contact;
    private String password;
    private String dob;
    private String address;

    public User(String id, String name, String type, String gmail, String contact, String password, String dob, String address) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.gmail = gmail;
        this.contact = contact;
        this.password = password;
        this.dob = dob;
        this.address = address;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public User getInstance(){
        return new User(id,name,type,gmail,contact,"Access-Denied",dob,address);
    }
}
