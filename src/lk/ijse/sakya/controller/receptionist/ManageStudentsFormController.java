package lk.ijse.sakya.controller.receptionist;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lk.ijse.sakya.db.DBConnection;
import lk.ijse.sakya.dto.CourseTM;
import lk.ijse.sakya.dto.RegistrationFee;
import lk.ijse.sakya.dto.Student;
import lk.ijse.sakya.dto.StudentCourse;
import lk.ijse.sakya.interfaces.QrPerformance;
import lk.ijse.sakya.model.CourseController;
import lk.ijse.sakya.model.RegistrationFeeController;
import lk.ijse.sakya.model.StudentController;
import lk.ijse.sakya.model.StudentCourseController;
import lk.ijse.sakya.thread.LoadQrUiTask;
import lk.ijse.sakya.thread.SendMail;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.mail.util.ByteArrayDataSource;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageStudentsFormController implements QrPerformance {
    public JFXTextField txtName;
    public JFXDatePicker datePicker1;
    public JFXTextField txtAddress;
    public JFXTextField txtGmail;
    public JFXTextField txtPGmail;
    public JFXTextField txtMobileNo;
    public JFXTextField txtPMobileNo;
    public JFXButton btnClear1;
    public JFXButton btnAdd;
    public JFXTextField txtStudentId;
    public JFXTextField txtName1;
    public JFXDatePicker datePicker;
    public JFXTextField txtAddress1;
    public JFXTextField txtGmail1;
    public JFXTextField txtPGmail1;
    public JFXTextField txtContact1;
    public JFXTextField txtPContact1;
    public TableView tblStudents;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colMobileName;
    public TableView tblCourses;
    public TableColumn colCourseId1;
    public TableColumn colGrade1;
    public TableColumn colSubject1;
    public TableColumn colYear1;
    public TableColumn colTeacherName;
    public JFXTextField txtStudentId1;
    public JFXButton btnLoadCourses;
    public JFXTextField txtCourseId;
    public TableColumn colCourseId2;
    public TableColumn colYear2;
    public TableView tblStudentCourse;
    public JFXRadioButton rdId;
    public JFXRadioButton rdName;
    public JFXTextField txtSearch;
    public JFXTextField txtFee;
    public JFXButton btnQr;
    public JFXButton btnQr2;
    private Student selectedStudent;
    private CourseTM selectedCourse;
    private boolean flag = false;

    public void initialize(){
        txtStudentId.setEditable(false);
        setNewStudentId();
        setStudentTable();
        setCourseTable();
    }

    public void setCourseTable(){
        colCourseId1.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("id"));
        colGrade1.setCellValueFactory(new PropertyValueFactory<CourseTM,Integer>("grade"));
        colSubject1.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("name"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("teacherId"));
        colYear1.setCellValueFactory(new PropertyValueFactory<CourseTM,Integer>("year"));

        try {
            tblCourses.setItems(CourseController.getCourseDetails());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnAddStudentOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(validation0()){
            if(datePicker1.getValue()==null){
                new Alert(Alert.AlertType.ERROR,"Enter Valid Details For Birthday").show();
                return;
            }
            String id = txtStudentId.getText();
            String name = txtName.getText();
            String dob =String.valueOf(datePicker1.getValue());
            String address = txtAddress.getText();
            String contact = txtMobileNo.getText();
            String gmail = txtGmail.getText();
            String p_gmail = txtPGmail.getText();
            String p_contact = txtPMobileNo.getText();


            Student student = new Student(id,name,dob,address,contact,gmail,p_gmail,p_contact);
            RegistrationFee fee = new RegistrationFee(id,Double.parseDouble(txtFee.getText()),
                    String.valueOf(LocalDate.now()));
            try {
                DBConnection.getInstance().getConnection().setAutoCommit(false);
                boolean flag = StudentController.addStudent(student);
                if(flag){
                    boolean flag2 = RegistrationFeeController.addData(fee);
                    if(flag2){
                        DBConnection.getInstance().getConnection().commit();
                        new Alert(Alert.AlertType.INFORMATION,"Student Added Successful").show();
                        File file = generateQRCodeImage(student.getId());
                        sendMail(file,student);
                        btnClearOnAction(null);
                        setNewStudentId();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Student Adding Failed").show();
                        DBConnection.getInstance().getConnection().rollback();
                    }
                }
            } catch (SQLException e) {
                DBConnection.getInstance().getConnection().rollback();
                new Alert(Alert.AlertType.ERROR,"Student Adding Failed - Database Error").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"Student Adding Failed - Driver Error").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,"Qr Genaration Error").show();
            }finally {
                DBConnection.getInstance().getConnection().setAutoCommit(true);
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Enter Valid Details For Red Fields").show();
        }
    }

    public File generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        File outputfile = new File("image.jpg");
        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "jpg", outputfile);
        //sendMail(outputfile);
        return outputfile;
    }

    public void sendMail(File file,Student ob){
        SendMail mail = new SendMail(ob.getGmail(),"Use This Code For Marking Attendence" +
                "And Make Payment On Sakya Smart Class System","Welcome To Sakya Smart Class System",file);
        mail.messageProperty().addListener((a,b,c)->{
            new Alert(Alert.AlertType.ERROR,c).show();
        });

        mail.valueProperty().addListener((a,b,c)->{
            if(c){
                new Alert(Alert.AlertType.INFORMATION,"Success").show();

            }else{
                new Alert(Alert.AlertType.ERROR,"Error").show();
            }
        });
        Thread t = new Thread(mail);
        t.start();

    }

    public void btnClearOnAction(ActionEvent actionEvent){
        txtName.clear();
        setTextFieldUnFocusColour(txtName,"000000");
        datePicker1.setValue(null);
        txtAddress.clear();
        setTextFieldUnFocusColour(txtAddress,"000000");
        txtMobileNo.clear();
        setTextFieldUnFocusColour(txtMobileNo,"000000");
        txtGmail.clear();
        setTextFieldUnFocusColour(txtGmail,"000000");
        txtPGmail.clear();
        setTextFieldUnFocusColour(txtPGmail,"000000");
        txtPMobileNo.clear();
        setTextFieldUnFocusColour(txtPMobileNo,"000000");
        txtFee.clear();
        setTextFieldUnFocusColour(txtFee,"000000");
    }

    public void setNewStudentId(){

        try {
            txtStudentId.setText(StudentController.getNewStudentId());
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting new Student Id Error - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting new Student Id Error - Driver Error").show();
        }

    }

    public void setStudentTable(){
        colId.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Student,String>("name"));
        colMobileName.setCellValueFactory(new PropertyValueFactory<Student,String>("contact"));
        try {
            tblStudents.setItems(StudentController.getAllStudents());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if(selectedStudent==null){
            new Alert(Alert.AlertType.ERROR,"Select Student From the Table").show();
            return;
        }
        if(validation()){
            selectedStudent.setName(txtName1.getText());
            selectedStudent.setDob(String.valueOf(datePicker.getValue()));
            selectedStudent.setAddress(txtAddress1.getText());
            selectedStudent.setGmail(txtGmail1.getText());
            selectedStudent.setP_gmail(txtPGmail1.getText());
            selectedStudent.setContact(txtContact1.getText());
            selectedStudent.setP_contact(txtPContact1.getText());
            try {
                boolean flag = StudentController.updateStudent(selectedStudent);
                if(flag){
                    setStudentTable();
                    new Alert(Alert.AlertType.INFORMATION,"Student Details Successfully updated").show();
                    btnClearOnAction1(null);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Enter Valid Details For Red Fields").show();
        }

    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if(selectedStudent==null){
            new Alert(Alert.AlertType.ERROR,"Select Student From the Table").show();
            return;
        }
        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.WARNING, "Do You Want To Delete " + selectedStudent.getId() + " " +
                "From The System", ButtonType.YES, ButtonType.NO).showAndWait();

        if(buttonType.get().getText().equalsIgnoreCase("no")){
            new Alert(Alert.AlertType.INFORMATION,"Student Not Deleted").show();
            return;
        }
        try {
            boolean flag = StudentController.deleteStudent(selectedStudent.getId());
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"Student Deleted Successful").show();
                btnClearOnAction1(null);
                selectedStudent=null;
                setStudentTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnClearOnAction1(ActionEvent actionEvent) {
        txtName1.clear();
        setTextFieldUnFocusColour(txtName1,"ffffff");
        datePicker.setValue(null);
        txtAddress1.clear();
        setTextFieldUnFocusColour(txtAddress1,"ffffff");
        txtGmail1.clear();
        setTextFieldUnFocusColour(txtGmail1,"ffffff");
        txtPGmail1.clear();
        setTextFieldUnFocusColour(txtPGmail1,"ffffff");
        txtContact1.clear();
        setTextFieldUnFocusColour(txtContact1,"ffffff");
        txtPContact1.clear();
        setTextFieldUnFocusColour(txtPContact1,"ffffff");
        selectedStudent = null;
        tblStudents.getSelectionModel().select(null);
    }

    public void setTextFieldUnFocusColour(JFXTextField temp,String value){
        temp.setUnFocusColor(Paint.valueOf(value));
    }

    public void tblStudentOnMouseClickAction(MouseEvent mouseEvent) {
        selectedStudent = (Student) tblStudents.getSelectionModel().getSelectedItem();
        if(selectedStudent==null){
            return;
        }
        txtName1.setText(selectedStudent.getName());
        setTextFieldUnFocusColour(txtName1,"4d4dff");
        datePicker.setValue(LocalDate.parse(selectedStudent.getDob()));
        txtAddress1.setText(selectedStudent.getAddress());
        setTextFieldUnFocusColour(txtAddress1,"4d4dff");
        txtGmail1.setText(selectedStudent.getGmail());
        setTextFieldUnFocusColour(txtGmail1,"4d4dff");
        txtPGmail1.setText(selectedStudent.getP_gmail());
        setTextFieldUnFocusColour(txtPGmail1,"4d4dff");
        txtContact1.setText(selectedStudent.getContact());
        setTextFieldUnFocusColour(txtContact1,"4d4dff");
        txtPContact1.setText(selectedStudent.getP_contact());
        setTextFieldUnFocusColour(txtPContact1,"4d4dff");
    }

    public void ManageStudentTabRequestOnAction(Event event) {
        setStudentTable();
    }

    public void tblCourseMouseOnClickAction(MouseEvent mouseEvent) {
        selectedCourse = (CourseTM) tblCourses.getSelectionModel().getSelectedItem();
        if(selectedCourse==null){
            return;
        }
        txtCourseId.setText(selectedCourse.getId());
    }

    public void btnAddCourseOAction(ActionEvent actionEvent) {
        String text = txtStudentId1.getText();

        try {
            if(StudentController.searchStudent(text)==null){
                new Alert(Alert.AlertType.ERROR,"Invalid Student Id").show();
                return;
            }
            if(selectedCourse==null){
                new Alert(Alert.AlertType.ERROR,"Select Course From Table Or Type Course Id On" +
                        "Course Id Text Field").show();
                return;
            }
            StudentCourse ob = new StudentCourse(text,selectedCourse.getId(),String.valueOf(LocalDate.now()));
            boolean flag = StudentCourseController.addRecord(ob);
            if(flag){
                selectedCourse=null;
                tblCourses.getSelectionModel().select(null);
                new Alert(Alert.AlertType.INFORMATION,"Student Registered to Course Successful").show();

            }else{

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            String[] split = e.getMessage().split(" ");
            System.out.println(split[0]);
            if(split[0].equalsIgnoreCase("duplicate")){
                new Alert(Alert.AlertType.ERROR,"This Student " +
                        "Have Already Registered For This Course").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Student Adding Failed "+split[0]).show();
            }

        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Student Adding Failed - Driver Error").show();

        }
        setStudentCourseTable(text);

    }

    public void btnLoadCoursesOnAction(ActionEvent actionEvent) {
        setStudentCourseTable(txtStudentId1.getText());
    }

    public void setStudentCourseTable(String studentId){
        colCourseId2.setCellValueFactory(new PropertyValueFactory<CourseTM,String>("id"));
        colYear2.setCellValueFactory(new PropertyValueFactory<CourseTM,Integer>("year"));
        try {
            if(StudentController.searchStudent(txtStudentId1.getText())==null){
                new Alert(Alert.AlertType.ERROR,"Invalid Student Id").show();
                tblStudentCourse.getItems().clear();
                return;
            }
            tblStudentCourse.setItems(StudentCourseController.getCoursesByStudentId(studentId));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnQrOnAction(ActionEvent actionEvent) {
        flag = true;
        qrScanner();
        flag=false;
    }

    public void qrScanner(){
        btnQr.setDisable(true);
        btnQr2.setDisable(true);
        Stage stage= new Stage();
        LoadQrUiTask tsk = new LoadQrUiTask(this,txtFee.getScene().getWindow(),stage);
        tsk.valueProperty().addListener((a,b,c)->{
            stage.setScene(c);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(txtFee.getScene().getWindow());
            stage.showAndWait();
            btnQr.setDisable(false);
            btnQr2.setDisable(false);
        });
        tsk.messageProperty().addListener((a,b,c)->{
            System.out.println(c);
            btnQr.setDisable(false);
            btnQr2.setDisable(false);
        });
        Thread t = new Thread(tsk);
        t.setDaemon(false);
        t.start();
    }

    @Override
    public void qrIdRequestAction(String id) {
        if(!flag){
            txtStudentId1.setText(id);
            btnLoadCoursesOnAction(null);
        }else{
            ObservableList<Student> items = tblStudents.getItems();
            int count = 0;
            for(Student ob : items){
                if(ob.getId().equals(id)){
                    tblStudents.getSelectionModel().select(count);
                    selectedStudent=ob;
                    tblStudentOnMouseClickAction(null);
                    return;
                }
                count++;
            }
        }
    }

    @Override
    public String getStudentDetail(String id) {
        try {
            Student student = StudentController.searchStudent(id);
            if(student!=null) {
                return student.getName();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void txtNameKeyAction(KeyEvent keyEvent) {
        nameValidateion(txtName);
    }

    public void nameValidateion(JFXTextField temp){
        Pattern p1 = Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
        Matcher m1 = p1.matcher(temp.getText());
        boolean b = m1.find();
        if(b){
            temp.setUnFocusColor(Paint.valueOf("0x4d4dffff"));

        }else{
            temp.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void txtAddressKeyAction(KeyEvent keyEvent) {

    }

    public void txtGmailKeyAction(KeyEvent keyEvent) {
        gmailValidate(txtGmail);

    }

    public void gmailValidate(JFXTextField temp){
        Pattern p1 = Pattern.compile("^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$");
        Matcher m1 = p1.matcher(temp.getText());
        boolean b = m1.find();
        if(b){
            temp.setUnFocusColor(Paint.valueOf("0x4d4dffff"));

        }else{
            temp.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void mobileNoValidate(JFXTextField temp){
        Pattern p1 = Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|" +
                "41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$");
        Matcher m1 = p1.matcher(temp.getText());
        boolean b = m1.find();
        if(b){
            temp.setUnFocusColor(Paint.valueOf("0x4d4dffff"));
        }else{
            temp.setUnFocusColor(Paint.valueOf("#ff0000"));
        }

    }

    public void txtMobileNoKeyAction(KeyEvent keyEvent) {
        mobileNoValidate(txtMobileNo);
    }

    public void txtPGmailKeyAction(KeyEvent keyEvent) {
        gmailValidate(txtPGmail);
    }

    public void txtPMobileNoKeyAction(KeyEvent keyEvent) {
        mobileNoValidate(txtPMobileNo);
    }

    public boolean validation0(){
        if(!txtName.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            System.out.println();
            return false;
        }
        if(!txtGmail.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtPGmail.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtMobileNo.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtPMobileNo.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtFee.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        return true;
    }

    public void txtName1KeyAction(KeyEvent keyEvent) {
        nameValidateion(txtName1);
    }

    public void txtGmail1KeyAction(KeyEvent keyEvent) {
        gmailValidate(txtGmail1);
    }

    public void txtPGmail1KeyAction(KeyEvent keyEvent) {
        gmailValidate(txtPGmail1);
    }

    public void txtContact1KeyAction(KeyEvent keyEvent) {
        mobileNoValidate(txtContact1);
    }

    public void txtPContact1KeyAction(KeyEvent keyEvent) {
        mobileNoValidate(txtPContact1);
    }

    public boolean validation(){
        if(!txtName1.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            System.out.println();
            return false;
        }
        if(!txtGmail1.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtPGmail1.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtContact1.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        if(!txtPContact1.getUnFocusColor().toString().equalsIgnoreCase(Paint.valueOf("0x4d4dffff").toString())){
            return false;
        }
        return true;
    }

    public void btnQrOnAction1(ActionEvent actionEvent) {
        flag = false;
        txtSearch.clear();
        btnSearchOnAction(null);
        qrScanner();
        flag = true;
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        String text = "%"+txtSearch.getText()+"%";
        String searchBy = "";
        if(rdId.isSelected()){
            searchBy = "id";
        }else{
            searchBy = "name";
        }
        try {
            if(text==null){
                tblStudents.setItems(StudentController.getAllStudents());
                return;
            }
            tblStudents.setItems(StudentController.searchStudent(searchBy,text));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void txtFeeOnAction(KeyEvent keyEvent) {
        try{
            double v = Double.parseDouble(txtFee.getText());
            txtFee.setUnFocusColor(Paint.valueOf("0x4d4dffff"));
        }catch(NumberFormatException ex){
            txtFee.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }
}
