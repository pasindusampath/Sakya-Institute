package lk.ijse.sakya.controller.admindashboard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.sakya.entity.custom.User;

import lk.ijse.sakya.service.custom.UserService;
import lk.ijse.sakya.service.custom.impl.UserServiceImpl;
import lk.ijse.sakya.thread.SendMail;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Done
public class ManageUserFormController {
    UserService userService;
    public JFXTextField txtSearch;
    public JFXButton btnSearch;
    public JFXTextField txtName;
    public JFXTextField txtGmail;
    public JFXTextField txtContact;
    public JFXTextField txtAddress;
    public JFXComboBox cbType;
    public JFXButton btnUpdate;
    public JFXButton btnDelete;
    public JFXButton btnClear;
    public JFXDatePicker dpDob;
    public TableView tableUsers;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colContact;
    public JFXButton btnAdd;
    public boolean flag;
    public JFXProgressBar prograss;
    public RadioButton rdId;
    public RadioButton rdName;
    public RadioButton rdContact;
    public TableColumn colType;
    private lk.ijse.sakya.entity.custom.User selectedUser;

    public void initialize(){
        userService = new UserServiceImpl();
        String[] type = {"ADMIN","TEACHER","RECEPTIONIST"};
        ObservableList<String> list = FXCollections.observableArrayList(type);
        cbType.setItems(list);
        flag=false;
        prograss.setVisible(false);
        setTableUsers();
    }

    public void setTableUsers(){
        colId.setCellValueFactory(new PropertyValueFactory<User,String>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<User,String>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<User,String>("contact"));
        colType.setCellValueFactory(new PropertyValueFactory<User,String>("type"));
        try {
            ObservableList<lk.ijse.sakya.entity.custom.User> users = userService.getAllUsers();
            tableUsers.setItems(users);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Getting User Details Error - Database Error")
                    .show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"Getting User Details Error - Driver Error")
                    .show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws Exception {
        if(validation()) {
            String name = txtName.getText();
            String gmail = txtGmail.getText();
            String contact = txtContact.getText();
            String date = dpDob.getValue().toString();
            String address = txtAddress.getText();
            String type = cbType.getSelectionModel().getSelectedItem().toString();

            User temp = new User(selectedUser.getId(),name,type,gmail,contact,selectedUser.getPassword()
            ,date,address);

            lk.ijse.sakya.entity.custom.User user = new lk.ijse.sakya.entity.custom.User(selectedUser.getId(),name,type,gmail,contact,selectedUser.getPassword()
                    ,date,address);

            if(!gmail.equals(selectedUser.getGmail())){
                Stage stage = new Stage();
                URL resource = getClass().getResource("../../view/admindashboard/ValidateGmailForm.fxml");
                FXMLLoader f1=new FXMLLoader(resource);
                Parent load = null;
                try {
                    load = f1.load();
                } catch (IOException e) {
                    new Alert(Alert.AlertType.ERROR,"Ui Not Found").show();
                   return;
                }
                ValidateGmailFormController controller = f1.getController();
                controller.setLblGmail(txtGmail.getText() );
                stage.setScene(new Scene(load));
                stage.initOwner(btnAdd.getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                if(!controller.isValidated()){
                    new Alert(Alert.AlertType.ERROR,"Profile Update Failed Try Again").show();
                    return;
                }else{
                    userService.sendUserDetailsMail(prograss,temp.getInstance(),"Profile Updated Successfull");
                }
            }
            boolean flag = false;
            try {
                flag=userService.updateUser(user);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"User Updating Error - Database Error").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"User Updating Error - Driver Error").show();
            }
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"User Updated Successful").show();

                setTableUsers();
            }else{
                new Alert(Alert.AlertType.ERROR,"User Update Fail").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Fill All The Columns With Valid Details And Press ADD " +
                    "Button").show();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        if(selectedUser==null){
            new Alert(Alert.AlertType.ERROR,"Select User From Table First And Try This Option");
            return;
        }
        Alert a1 = new Alert(Alert.AlertType.CONFIRMATION,"Do You Want To Delete "+selectedUser.
                getName()+" From The System", ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = a1.showAndWait();
        String text = buttonType.get().getText();
        if(text.equalsIgnoreCase("no")){
            return;
        }
        lk.ijse.sakya.entity.custom.User user = new lk.ijse.sakya.entity.custom.User(selectedUser.getId(),
                selectedUser.getName(), selectedUser.getType(),selectedUser.getGmail()
                ,selectedUser.getContact(),selectedUser.getPassword(),selectedUser.getDob(),selectedUser.getAddress());
        try {
            boolean flag = userService.deleteUser(user);
            if(flag){
                new Alert(Alert.AlertType.INFORMATION,"User Deleted Successful").show();
                setTableUsers();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,"User Delete Failed - Database Error").show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,"User Delete Failed - Driver Error").show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtAddress.clear();
        txtContact.clear();
        txtName.clear();
        txtGmail.clear();
        dpDob.setValue(null);
        cbType.setValue(null);

        txtName.setUnFocusColor(Paint.valueOf("#2f1616"));
        txtGmail.setUnFocusColor(Paint.valueOf("#2f1616"));
        txtContact.setUnFocusColor(Paint.valueOf("#2f1616"));
        txtAddress.setUnFocusColor(Paint.valueOf("#2f1616"));
        dpDob.setDefaultColor(Paint.valueOf("#123632"));

        cbType.setUnFocusColor(Paint.valueOf("black"));
    }

    public void btnAddOnAction(ActionEvent actionEvent) throws Exception {

        if(validation()){
            try {
                Stage stage = new Stage();
                URL resource = getClass().getResource("/admindashboard/ValidateGmailForm.fxml");
                FXMLLoader f1=new FXMLLoader(resource);
                Parent load = f1.load();
                ValidateGmailFormController controller = f1.getController();
                controller.setLblGmail(txtGmail.getText() );
                stage.setScene(new Scene(load));
                stage.initOwner(btnAdd.getScene().getWindow());
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                if(!controller.isValidated()){
                    new Alert(Alert.AlertType.ERROR,"User Adding Failed Try Again").show();
                    return;
                }
                String newUserId = userService.getNewUserId();
                String name = txtName.getText();
                String gmail=txtGmail.getText();
                String contact=txtContact.getText();
                String date=dpDob.getValue().toString();
                String address=txtAddress.getText();
                Random r= new Random();
                int rand;
                do{
                    rand = r.nextInt(999999999);
                }while(!(rand>10000000));
                User u1 = new User(newUserId,name,cbType.getSelectionModel().getSelectedItem().toString()
                ,gmail,contact,String.valueOf(rand),date,address);
                userService.sendUserDetailsMail(prograss,u1,"Welcome To Sakya Smart Class System");
                boolean b = userService.addUser(u1);
                if(b){
                    new Alert(Alert.AlertType.INFORMATION,"User Added Succesful").show();
                    setTableUsers();
                    btnClearOnAction(null);
                }else{
                    new Alert(Alert.AlertType.ERROR,"User Not Added To Database Please Try Again").show();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"User Adding Failed - Database Error").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"User Adding Failed - Driver Error").show();
            }catch ( IOException e) {
                new Alert(Alert.AlertType.ERROR,"Ui Not Found - Error").show();
            }
        }else{
            new Alert(Alert.AlertType.ERROR,"Fill All The Columns With Valid Details And Press ADD" +
                    " Button").show();
        }
    }

    public boolean validation(){
        if(! txtName.getUnFocusColor().toString().equalsIgnoreCase("0x4d4dffff")){
            return false;
        }if(! txtGmail.getUnFocusColor().toString().equalsIgnoreCase("0x4d4dffff")){
            return false;
        }if(! txtContact.getUnFocusColor().toString().equalsIgnoreCase("0x4d4dffff")){
            return false;
        }if(! txtAddress.getUnFocusColor().toString().equalsIgnoreCase("0x4d4dffff")){
            return false;
        }
        if(cbType.getSelectionModel().getSelectedIndex()==-1){
            cbType.setUnFocusColor(Paint.valueOf("#ff0000"));
            return false;
        }else{
            cbType.setUnFocusColor(Paint.valueOf("#4d4dff"));
        }

        if(dpDob.getValue()==null){
            dpDob.setDefaultColor(Paint.valueOf("#ff0000"));
            dpDob.getStylesheets().clear();
            dpDob.getStylesheets().add("lk/ijse/sakya/stylesheet/datepicker/Error.css");
            return false;
        }else{
            dpDob.setDefaultColor(Paint.valueOf("#4d4dff"));
            dpDob.getStylesheets().clear();
            dpDob.getStylesheets().add("lk/ijse/sakya/stylesheet/datepicker/Normal.css");
        }

        return true;
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        if(rdContact.isSelected()){
            searchUser("contact");
        }
        if(rdName.isSelected()){
            searchUser("name");
        }
        if(rdId.isSelected()){
            searchUser("id");
        }
    }

    public void searchUser(String searchBy){
        try {
            tableUsers.setItems(userService.searchUser(searchBy,txtSearch.getText()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtNameKeyAction(KeyEvent keyEvent) {
        Pattern compile = Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$");
        Matcher matcher = compile.matcher(txtName.getText());
        if(matcher.find()) {
            txtName.setUnFocusColor(Paint.valueOf("#4d4dff"));
        }else{
            txtName.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void txtGmailKeyAction(KeyEvent keyEvent) {
        Pattern p1 = Pattern.compile("^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$");
        Matcher m1 = p1.matcher(txtGmail.getText());
        boolean b = m1.find();
        if(b){
            txtGmail.setUnFocusColor(Paint.valueOf("4d4dff"));

        }else{
            txtGmail.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void txtContactKeyAction(KeyEvent keyEvent) {
        Pattern p1 = Pattern.compile("^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|" +
                "41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|4|5|6|7|8)\\d)\\d{6}$");
        Matcher m1 = p1.matcher(txtContact.getText());
        boolean b = m1.find();
        if(b){
            txtContact.setUnFocusColor(Paint.valueOf("#4d4dff"));

        }else{
            txtContact.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    public void txtAddressKeyAction(KeyEvent keyEvent) {
        if(txtAddress.getText().length()>10){
            txtAddress.setUnFocusColor(Paint.valueOf("4d4dff"));
        }else{
            txtAddress.setUnFocusColor(Paint.valueOf("#ff0000"));
        }
    }

    /*public void sendUserDetailsMail(JFXProgressBar prograss,User user,String subject) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Use This Username and Password to Login to Sakya Smart Classroom Application");
        sb.append("");
        sb.append("                    Username : "+user.getGmail()+"");
        sb.append("                    Password : "+user.getPassword()+"");
        sb.append("                                                                              ");
        sb.append("                                                                              ");
        sb.append("                                                                              ");
        File file = generateQRCodeImage(user.getId());
        SendMail os = new SendMail(user.getGmail(),sb.toString(),subject,file);
        Thread ob = new Thread(os);
        ob.setDaemon(true);
        os.messageProperty().addListener((h, oldMessage, newMessage) -> {
            new Alert(Alert.AlertType.WARNING, newMessage).show();
            user.setPassword("12345678");
            try {
                UserController.updateUser(user);
                new Alert(Alert.AlertType.WARNING, "Login The System Using " +
                        "Username : "+user.getGmail()+"" +
                        "Password : "+user.getPassword()).show();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,"System Error - User - Database \n" +
                        "Contact Manufactures").show();
            } catch (ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR,"System Error - User - Driver \n" +
                        "Contact Manufactures").show();

            }
        });
        prograss.progressProperty().bind(os.progressProperty());
        prograss.visibleProperty().bind(os.runningProperty());
        ob.start();
    }

    public File generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);
        File outputfile = new File(FileSystems.getDefault().getPath("src\\" +"userQr\\"+barcodeText+".jpg").toAbsolutePath().toString());

        ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "jpg", outputfile);
        //sendMail(outputfile);
        return outputfile;
    }*/

    public void tableOnMouseClickAction(MouseEvent mouseEvent) {
        selectedUser= (lk.ijse.sakya.entity.custom.User) tableUsers.getSelectionModel().getSelectedItem();
        if(selectedUser==null){
            return;
        }
        txtName.setText(selectedUser.getName());
        txtGmail.setText(selectedUser.getGmail());
        txtContact.setText(selectedUser.getContact());
        txtAddress.setText(selectedUser.getAddress());
        dpDob.setValue(Date.valueOf(selectedUser.getDob()).toLocalDate());
        cbType.setValue(selectedUser.getType());

        txtName.setUnFocusColor(Paint.valueOf("#4d4dff"));
        txtGmail.setUnFocusColor(Paint.valueOf("#4d4dff"));
        txtContact.setUnFocusColor(Paint.valueOf("#4d4dff"));
        txtAddress.setUnFocusColor(Paint.valueOf("#4d4dff"));

        dpDob.setDefaultColor(Paint.valueOf("#4d4dff"));
        dpDob.getStylesheets().clear();
        dpDob.getStylesheets().add("lk/ijse/sakya/stylesheet/datepicker/Normal.css");

        cbType.setUnFocusColor(Paint.valueOf("#4d4dff"));
    }
}
