package application;
	
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
		public void start(Stage primaryStage) {
			try {
				ControllerFactory mainContainer = new ControllerFactory();
		        mainContainer.loadScreen("login", "Login.fxml");// posso retira dps
		        mainContainer.setStage(primaryStage);
		        mainContainer.setScreen("login");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args) {
			launch(args);
		}
	}

// check quando for fechar com X , verifica se � a ultima janela se for avisar o servidor !!!