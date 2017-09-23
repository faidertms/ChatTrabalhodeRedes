package application;

import java.util.HashMap;
import java.util.Map;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ControllerFactory {
	private HashMap<String, Node> screens = new HashMap<>();
	private HashMap<String, Object> control = new HashMap<>();
	private HashMap<String, Object> popStage = new HashMap<>();
	private Stage stage; // Window
	private Scene scene; // tela
	private String nome;


	public boolean loadScreen(String name, String resource) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
			Parent loadScreen = (Parent) myLoader.load();
			control.put(name, myLoader.getController());
			FactoryController controller = ((FactoryController) myLoader.getController());
			controller.setFactoryController(this);
			addController(name, loadScreen);

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public boolean loadScreenPop(String name, String resource) {
		try {
			FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
			Parent loadScreen = (Parent) myLoader.load();
			control.put(name, myLoader.getController());
			FactoryController controller = ((FactoryController) myLoader.getController());
			controller.setFactoryController(this);
			addController(name, loadScreen);
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	public void removerTodos() {
		screens.clear();
		control.clear();
		for (Map.Entry<String, Object> entry : this.popStage.entrySet()) {
			Stage dialogStage = (Stage) entry.getValue();
			dialogStage.close();
		}

	}

	public boolean setScreen(final String nome) {
		if (screens.get(nome) != null) {
			if (scene == null) {
				scene = new Scene((Parent) screens.get(nome));
				stage.setTitle("Chat Publico");
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.show();

			} else {
				stage.setTitle("Usuario: " + this.nome + " conectado a "+nome);
				scene = new Scene((Parent) screens.get(nome));
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.show();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				      public void handle(WindowEvent we) {
				    	  if(!nome.equals("login"))
				    	  ((LayoutController) control.get(nome)).sairConversa(null);
				      }
				  }); 

			}
			return true;
		} else {
			System.out.println("Tela não carregada");
			return false;
		}

	}

	public boolean setScreenPop(final String nome, boolean tipo) {
		if (screens.get(nome) != null) {
			// Cria o palco dialogStage.
			Stage dialogStage = new Stage();
			if (tipo){
				dialogStage.setTitle("Chat particular com :" + nome);
			}else{
				dialogStage.setTitle("Usuario: " + this.nome + " conectado a "+nome);
				dialogStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				      public void handle(WindowEvent we) {
				    	  ((LayoutController) control.get(nome)).sairConversa(null);
				      }
				  });
			}
			dialogStage.initModality(Modality.WINDOW_MODAL);
			this.popStage.put(nome, dialogStage);
			Scene scene = new Scene((AnchorPane) screens.get(nome));
 
			dialogStage.setScene(scene);
			dialogStage.show();
			return true;
		} else {
			System.out.println("Tela não carregada");
			return false;
		}

	}


	public boolean unloadScreen(String name) {
		if (screens.size() > 1 && screens.remove(name) != null && this.control.remove(name) != null) {
			if (name.equals("Sala:0")) {
				stage.close();
				stage = null;
			} else {
				for (Map.Entry<String, Object> entry : this.popStage.entrySet()) {
					String key = entry.getKey();
					Stage dialogStage = (Stage) entry.getValue();
					if (key.equals(name)) {
						dialogStage.close();
					}
				}
			}
		} else if (screens.remove(name) != null && this.control.remove(name) != null) {
			if (!name.equals("Sala:0")) {
				for (Map.Entry<String, Object> entry : this.popStage.entrySet()) {
					String key = entry.getKey();
					Stage dialogStage = (Stage) entry.getValue();
					if (key.equals(name)) {
						stage = dialogStage;
					}
				}
			}
		} else {
			System.out.println("tela não existe");
		}
		return true;
	}

	public static void confirma() {
		Alert dialogoErro = new Alert(Alert.AlertType.CONFIRMATION);
		dialogoErro.setTitle("Ação");
		dialogoErro.setHeaderText("Ação Solicitada.");
		dialogoErro.setContentText("Ação Solicitada foi efetuada com sucesso.");
		dialogoErro.showAndWait();
	}

	public static void erro(String erro) {
		Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
		dialogoErro.setTitle("Diálogo de Error");
		dialogoErro.setHeaderText("Erro na Ação.");
		dialogoErro.setContentText(erro);
		dialogoErro.showAndWait();
	}

	public HashMap<String, Object> getPopStage() {
		return popStage;
	}

	public void setPopStage(HashMap<String, Object> popStage) {
		this.popStage = popStage;
	}
	
	public Object getController(String nome) {

		return control.get(nome);
	}

	public HashMap<String, Object> getControl() {
		return control;
	}

	public void setControl(HashMap<String, Object> control) {
		this.control = control;
	}

	public HashMap<String, Node> getScreens() {
		return screens;
	}

	public void setScreens(HashMap<String, Node> screens) {
		this.screens = screens;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ControllerFactory() {
		super();
	}

	public void addController(String nome, Node screen) {
		screens.put(nome, screen);
	}

	public Node getScreen(String nome) {
		return screens.get(nome);
	}

	public boolean existScreen(String nome) {
		return screens.containsKey(nome);
	}
}
