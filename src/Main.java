package src;


	
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


public class Main extends Application {
	
	@FXML
	Button bt;
	@FXML
	Label lable;
        @FXML
        ImageView img;
	@FXML
	PasswordField user_password;
	private static String password;
        private static String user;
	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Main.password = password;
	}

	public static String getUser() {
		return user;
	}

	public static void setUser(String user) {
		Main.user = user;
	}
	
	
	

	public void start(Stage stage) throws IOException {
            
                        
                     
			Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("TimeTraveler");
			stage.setResizable(false);
			stage.show();
                               
	}
	
	public static void main(String[] args) {
            
		launch(args);
	}
	@FXML
	private void login_do(ActionEvent e) throws IOException{
		Main.setPassword(user_password.getText());
		String[] command = {"bash","-c","echo " + Main.getPassword() +  " | sudo -S  echo test" };
		          Process p = Runtime.getRuntime().exec(command);
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                
                   Process u_n = Runtime.getRuntime().exec("whoami");
                   BufferedReader b = new BufferedReader(new InputStreamReader(u_n.getInputStream()));
                    Main.setUser(b.readLine());
		if(br.read()==-1){
                 lable.setText("Login fail!");
                 user_password.setText("");
                File file = new File("/icons/fail.png");
                img.setImage(new Image(file.getAbsolutePath()) );
                }
		else   {
                   
                
                   Parent root = FXMLLoader.load(getClass().getResource("/gui/main_window.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage_main = new Stage();
                    stage_main.setScene(scene);
                    stage_main.setTitle("Welcome to TimeTraveler");
                    stage_main.setResizable(false);
                    stage_main.show();
                    
                    Stage stage  = (Stage) bt.getScene().getWindow();
                    stage.close();
                  }
                
        }
        @FXML
        private void login_do_ENTER(KeyEvent e) throws IOException{
              if(e.getCode()==KeyCode.ENTER) this.login_do(null);  
        }
        
     
}
