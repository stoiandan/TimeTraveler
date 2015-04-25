package src;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            String[] command = {"bash", "-c", "mkdir -p ~/.snaps"};
            Runtime.getRuntime().exec(command);
            String[] command2 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S mkdir -p ~/.snaps/{home,root}"};
            Runtime.getRuntime().exec(command2);
        } catch (Exception e) {
        }
        try {
            File home_f = new File("/etc/cron.hourly/TimeTraveler_home_daemon");
            if (home_f.exists()) {
                radio_home.setSelected(true);
            } else {
                radio_home.setSelected(false);
            }
        } catch (Exception e) {

        }
        try {
            File home_f = new File("/etc/cron.hourly/TimeTraveler_root_daemon");
            if (home_f.exists()) {
                radio_root.setSelected(true);
            } else {
                radio_root.setSelected(false);
            }
        } catch (Exception e) {

        }
    }

    @FXML
    CheckBox radio_home;
    @FXML
    CheckBox radio_root;
    @FXML
    Label msg;
    @FXML
    Button b1;
    @FXML
    MenuItem p1;
    @FXML
    MenuItem p2;
    public static String path;

    @FXML
    private void help_about(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/DialogBox.fxml"));
        Scene scene = new Scene(root);
        Stage stage_main = new Stage();
        stage_main.setScene(scene);
        stage_main.setTitle("About");
        stage_main.setResizable(false);
        stage_main.show();
    }

    @FXML
    private void snapsoht_now(ActionEvent e) throws IOException, InterruptedException {
        Wizard snapshot_noww = new Wizard();
        snapshot_noww.init();
        Wizard.con = this;

    }

    @FXML
    private void automatic_home(ActionEvent e) throws IOException, URISyntaxException, InterruptedException {
        if (radio_home.isSelected() == true) {
            InputStream f = getClass().getResourceAsStream("/scripts/TimeTraveler_home_daemon");
            OutputStream r = new FileOutputStream(new File("/home/" + Main.getUser() + "/.snaps/TimeTraveler_home_daemon"));
            int i = f.read();
            while (i != -1) {
                r.write(i);
                i = f.read();
            }
            f.close();
            r.close();

            String[] command0 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  echo sudo btrfs subvolume snapshot -r /home/ /home/" + Main.getUser() + "/.snaps/home/'$(date +\"%S:%M:%H_%d-%m-%y\")'   >>   /home/" + Main.getUser() + "/.snaps/TimeTraveler_home_daemon "};
            Process p = Runtime.getRuntime().exec(command0);
            p.wait();
            String[] command1 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  mv /home/" + Main.getUser() + "/.snaps/TimeTraveler_home_daemon /etc/cron.hourly/TimeTraveler_home_daemon "};
            Process q = Runtime.getRuntime().exec(command1);
            q.wait();
            String[] command2 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  chmod +x  /etc/cron.hourly/TimeTraveler_home_daemon "};
           Process t = Runtime.getRuntime().exec(command2);
           t.wait();
            msg.setText("home snapshotting \nstarted!");

        } else {
            String[] command = {"bash", "-c", " echo " + Main.getPassword() + " | sudo -S  rm  /etc/cron.hourly/TimeTraveler_home_daemon"};
            Runtime.getRuntime().exec(command);
            msg.setText("home snapshotting \nstopped!");

        }
    }

    @FXML
    private void automatic_root(ActionEvent e) throws IOException, InterruptedException {
        if (radio_root.isSelected() == true) {
            InputStream f = getClass().getResourceAsStream("/scripts/TimeTraveler_root_daemon");
            OutputStream r = new FileOutputStream(new File("/home/" + Main.getUser() + "/.snaps/TimeTraveler_root_daemon"));
            int i = f.read();
            while (i != -1) {
                r.write(i);
                i = f.read();
            }
            f.close();
            r.close();

            String[] command0 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  echo sudo btrfs subvolume snapshot -r / /home/" + Main.getUser() + "/.snaps/root/'$(date +\"%S:%M:%H_%d-%m-%y\")'   >>   /home/" + Main.getUser() + "/.snaps/TimeTraveler_root_daemon "};
            Process p = Runtime.getRuntime().exec(command0);
            p.wait();
            String[] command1 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  mv /home/" + Main.getUser() + "/.snaps/TimeTraveler_root_daemon /etc/cron.hourly/TimeTraveler_root_daemon "};
            Process q = Runtime.getRuntime().exec(command1);
            q.wait();
            String[] command2 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  chmod +x  /etc/cron.hourly/TimeTraveler_root_daemon "};
            Process t = Runtime.getRuntime().exec(command2);
            t.wait();
            msg.setText("home snapshotting \nstarted!");

        } else {
            String[] command = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S  rm  /etc/cron.hourly/TimeTraveler_root_daemon  "};
            Runtime.getRuntime().exec(command);
            msg.setText("root snapshotting \nstopped!");

        }

    }

    @FXML
    private void licese_show(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/gui/mitlicense.fxml"));
        Scene scene = new Scene(root);
        Stage stage_main = new Stage();
        stage_main.setScene(scene);
        stage_main.setTitle("Mit License");
        stage_main.setResizable(false);
        stage_main.show();
    }

    @FXML
    private void restore_snapshot(ActionEvent e) throws IOException, InterruptedException {
        String path = "/home/" + Main.getUser() + "/.snaps/";
        String h_dir = path + "home";
        String r_dir = path + "root";
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("TimeTraveler Restoration Wizard");
        dc.setInitialDirectory(new File(path));
        File file = dc.showDialog(null);
        if (file.getParent().equals(r_dir)) {
            Controller.path = file.getPath();
            msg.setText("Restoring now...");
            b1.setDisable(true);
            p1.setDisable(true);
            p2.setDisable(true);
            Thread r = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S rsync -vaAxX --exclude=tmp --exclude=run  --exclude=dev  --delete-after   " + Controller.path + "/ /"};
                        Process p = Runtime.getRuntime().exec(command);
                        p.waitFor();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                msg.setText("Restoration is done!");
                                b1.setDisable(false);
                                p1.setDisable(false);
                                p2.setDisable(false);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Error!");
                    }
                }
            });
            r.start();
        } else if (file.getParent().equals(h_dir)) {
            Controller.path = file.getPath();
            msg.setText("Restoring now...");
            b1.setDisable(true);
            p1.setDisable(true);
            p2.setDisable(true);
            Thread r = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S rsync -vaAxX --delete-after  " + Controller.path + "/" + Main.getUser() + " /home"};
                        Process p = Runtime.getRuntime().exec(command);
                        p.waitFor();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                msg.setText("Restoration is done!");
                                b1.setDisable(false);
                                p1.setDisable(false);
                                p2.setDisable(false);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Error!");
                    }
                }
            });
            r.setDaemon(true);
            r.start();
        } else {
            msg.setText("Location is not \n  valid!");
        }
    }

    @FXML
    private void del_specific(ActionEvent e) throws IOException {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("TimeTraveler Specific Delete Wizard");
        dc.setInitialDirectory(new File("/home/" + Main.getUser() + "/.snaps/"));
        File f = dc.showDialog(null);
        if (f.getAbsolutePath().contains("/home/" + Main.getUser() + "/.snaps/")) {
            String[] command = {"bash", "-c", "echo " + Main.getPassword() + "| sudo -S btrfs subvolume delete " + f.getAbsolutePath()};
            Process p = Runtime.getRuntime().exec(command);
            msg.setText("Snapshot has \nbeen deleted!");
        } else {
            msg.setText("Invalide Location!");
        }
    }

    @FXML
    private void delete_a_h(ActionEvent e) throws IOException {
        String[] command = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S btrfs subvolume delete ~/.snaps/home/*"};
        Runtime.getRuntime().exec(command);

    }

    @FXML
    private void delete_a_r(ActionEvent e) throws IOException {
        String[] command = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S btrfs subvolume delete ~/.snaps/root/*"};
        Runtime.getRuntime().exec(command);
    }

    @FXML
    private void navigate(ActionEvent e) throws IOException {
        String path = "/home/" + Main.getUser() + "/.snaps/";
        String h_dir = path + "home";
        String r_dir = path + "root";
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("TimeTraveler Navigate in Snapshots");
        dc.setInitialDirectory(new File(path));
        File file = dc.showDialog(null);
    }

    @FXML
    private void safe_r(ActionEvent e) throws IOException {
        String path = "/home/" + Main.getUser() + "/.snaps/";
        String h_dir = path + "home";
        String r_dir = path + "root";
        FileChooser dc = new FileChooser();
        dc.setTitle("TimeTraveler Safe Restoration Wizard");

        dc.setInitialDirectory(new File(path));
        File file = dc.showOpenDialog((Stage) msg.getScene().getWindow());
        if ((file.getPath().contains(r_dir) || file.getPath().contains(h_dir)) && (!file.getParent().equals(h_dir) && !file.getParent().equals(r_dir)) && (!file.getPath().equals(h_dir) && !file.getPath().equals(r_dir))) {
            String[] command = {"bash", "-c", "mkdir ~/timetraveler_safe_restoration"};
            Runtime.getRuntime().exec(command);
            Controller.path = file.getAbsolutePath();
            msg.setText("Restoring...");
            b1.setDisable(true);
            p1.setDisable(true);
            p2.setDisable(true);
            Thread r = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] cmd2 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S rsync -vaAxX " + Controller.path + " ~/timetraveler_safe_restoration"};
                        Process p = Runtime.getRuntime().exec(cmd2);
                        p.waitFor();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                msg.setText("Safe Restoration is done!");
                                b1.setDisable(false);
                                p1.setDisable(false);
                                p2.setDisable(false);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Error!");
                    }
                }
            });
            r.setDaemon(true);
            r.start();
        } else {
            msg.setText("Location is not \nwithin a snapshot!");
        }

    }

    @FXML
    private void safe_r_f(ActionEvent e) throws IOException {
        String path = "/home/" + Main.getUser() + "/.snaps/";
        String h_dir = path + "home";
        String r_dir = path + "root";
        DirectoryChooser dc = new DirectoryChooser();
        dc.setTitle("TimeTraveler Safe Restoration Wizard");

        dc.setInitialDirectory(new File(path));
        File file = dc.showDialog(null);
        if ((file.getPath().contains(r_dir) || file.getPath().contains(h_dir)) && (!file.getParent().equals(h_dir) && !file.getParent().equals(r_dir)) && (!file.getPath().equals(h_dir) && !file.getPath().equals(r_dir))) {
            String[] command = {"bash", "-c", "mkdir ~/timetraveler_safe_restoration"};
            Runtime.getRuntime().exec(command);
            Controller.path = file.getAbsolutePath();
            msg.setText("Restoring...");
            b1.setDisable(true);
            p1.setDisable(true);
            p2.setDisable(true);
            Thread r = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String[] cmd2 = {"bash", "-c", "echo " + Main.getPassword() + " | sudo -S rsync -vaAxX " + Controller.path + " ~/timetraveler_safe_restoration"};
                        Process p = Runtime.getRuntime().exec(cmd2);
                        p.waitFor();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                msg.setText("Safe Restoration is done!");
                                b1.setDisable(false);
                                p1.setDisable(false);
                                p2.setDisable(false);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Error!");
                    }
                }
            });
            r.setDaemon(true);
            r.start();
        } else {
            msg.setText("Location is not \nwithin a snapshot!");
        }
    }
}
