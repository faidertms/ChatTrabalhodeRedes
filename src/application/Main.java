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

	
	/*public static void main(String[] args) throws IOException {
		while(true){
		Socket socket = new Socket("localhost", 6666);
		
		if(socket.isConnected()){
			// um while q atualizar a cada seg
			// e uma condicional que envia a mensagem.
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			String msg = "OIE";
			
			out.write(msg.getBytes());
			out.flush();
			
			byte[] buffer = new byte[1024];
			in.read(buffer);
			System.out.println(new String(buffer));
			socket.close();
		}}
		

		
		
	}
}//*/
