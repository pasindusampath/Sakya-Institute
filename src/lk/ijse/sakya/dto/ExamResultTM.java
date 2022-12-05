package lk.ijse.sakya.dto;

import com.jfoenix.controls.JFXTextField;


public class ExamResultTM {
    private String id;
    private String name;
    private JFXTextField txtMark;

    public ExamResultTM(String id, String name, JFXTextField txtMark) {
        this.id = id;
        this.name = name;
        this.txtMark = txtMark;
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

    public JFXTextField getTxtMark() {
        return txtMark;
    }

    public void setTxtMark(JFXTextField txtMark) {
        this.txtMark = txtMark;
    }
}
