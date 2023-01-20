package lk.ijse.sakya.entity.custom;

import lk.ijse.sakya.entity.SuperEntity;

import java.io.Serializable;

public class Subject implements SuperEntity {
    private String id;
    private String name;
    private int grade;

    public Subject(String id, String name, int grade) {
        this.setId(id);
        this.setName(name);
        this.setGrade(grade);
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

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }
}
