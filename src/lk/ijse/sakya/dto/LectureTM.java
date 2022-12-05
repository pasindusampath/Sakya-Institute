package lk.ijse.sakya.dto;

public class LectureTM {
    private String id;
    private String name;
    private String teacherName;

    public LectureTM(String id, String name, String teacherName) {
        this.id = id;
        this.name = name;
        this.teacherName = teacherName;
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
