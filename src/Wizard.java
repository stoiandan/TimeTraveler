package src;



import src.Main;
import src.Controller;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Wizard {

    @FXML
    RadioButton home;
    @FXML
    RadioButton root;
    @FXML
    Label warning;
    @FXML
    TextField name;
    @FXML
    Label warn2;
    public static Controller con;

    public void init() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/gui/wizard.fxml"));
        Scene scene = new Scene(root);
        Stage st1 = new Stage();
        st1.setScene(scene);
        st1.setTitle("TimeTraveler Wizard Guide");
        st1.setResizable(false);
        st1.show();

    }

    @FXML
    private void root_select(ActionEvent e) {
        if (home.isSelected() == true && root.isSelected() == true) {
            home.setSelected(false);
        }

    }

    @FXML
    private void home_select(ActionEvent e) {
        if (home.isSelected() == true && root.isSelected() == true) {
            root.setSelected(false);
        }
    }

    @FXML
    private void create_snapshot(ActionEvent e) throws IOException, InterruptedException {
        if ( (this.cpath()==true) && (this.cname() == true) ) {
            if (this.first() == true) {
                if (home.isSelected() == true) {
                    if (name.getText().length() == 0) {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S btrfs subvolume snapshot -r /home  ~/.snaps/home/$(date +\"%S+%M+%H-%d-%m-%y\" )"};
                        Runtime.getRuntime().exec(command);
                    } else {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S btrfs subvolume snapshot -r /home  ~/.snaps/home/" + name.getText()};
                        Runtime.getRuntime().exec(command);
                    }
                } else {
                    if (name.getText().length() == 0) {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S btrfs subvolume snapshot -r / ~/.snaps/root/$(date +\"%S+%M+%H-%d-%m-%y\" )"};
                        Runtime.getRuntime().exec(command);
                    } else {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S btrfs subvolume snapshot -r / ~/.snaps/root/" + name.getText()};
                        Runtime.getRuntime().exec(command);
                    }
                }
                Stage stage = (Stage) name.getScene().getWindow();
                stage.close();
                con.msg.setText("Wizard successfully\n completed");
            } else {
                warning.setText("Snapshot already exists!");
            }
        }
    }

    boolean cpath() {
        if (home.isSelected() == false && root.isSelected() == false) {
            warning.setText("You must choose a folder!");
            return false;
        } else {
            warning.setText("Choose folder:");
            return true;
        }

    }

    boolean cname() {
        String check = name.getText();
        if ( (check.contains(" ") == true) ) {
            warn2.setText("Invalide Name!");
 
            return false;
        } else {
            warn2.setText("Snapshot name (or let it default):");
            return true;
        }

    }

    boolean first() {
        String path = "/home/" + Main.getUser() +"/";
        if (home.isSelected() == true) {
            path += ".snaps/home/";
        } else {
            path += ".snaps/root/";
        }

        path += name.getText();
        File f = new File(path);
        if (f.exists()) {
            if(name.getText().equals("")==true){
                return true;
            }
            return false;
        }
        return true;
    }

}
