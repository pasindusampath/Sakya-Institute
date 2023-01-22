package lk.ijse.sakya.entity.custom;

import lk.ijse.sakya.entity.SuperEntity;

public class Module implements SuperEntity {
    private String id;
    private String name;
    private String subId;

    public Module(String id, String name, String subId) {
        this.setId(id);
        this.setName(name);
        this.setSubId(subId);
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

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }
}
