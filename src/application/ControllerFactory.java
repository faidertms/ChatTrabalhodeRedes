package application;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ControllerFactory {
private HashMap<String, Node> screens = new HashMap<>();
private HashMap<String, Object> control = new HashMap<>();
private HashMap<String, Object> popStage = new HashMap<>();
private Stage stage; //Window
private Scene scene; //tela
    
    public ControllerFactory() {
        super();
    }

    public void addController(String nome, Node screen) {
        screens.put(nome, screen);
    }

    public Node getScreen(String nome) {
        return screens.get(nome);
    }
    
    public boolean existScreen(String nome){
    	return screens.containsKey(nome);
    }


    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            control.put(name,myLoader.getController());
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
        	System.out.println(name +" POP");
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            control.put(name,myLoader.getController());
            FactoryController controller = ((FactoryController) myLoader.getController());
            controller.setFactoryController(this);
            addController(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public void removerTodos(){
    	screens.clear(); 
        control.clear();
    	for(Map.Entry<String, Object> entry : this.popStage.entrySet()) {
    	    Stage dialogStage = (Stage)  entry.getValue();
    	    dialogStage.close();
    	    // do what you have to do here
    	    // In your case, an other loop.
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
            	stage.setTitle(nome);
            	scene = new Scene((Parent) screens.get(nome));
    			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    			stage.setScene(scene);
    			stage.show();
    			//stage.setMaximized(true);
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
	        if(tipo)
	        dialogStage.setTitle("Chat particular com :" + nome);
	        else
	        dialogStage.setTitle(nome);
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        this.popStage.put(nome, dialogStage);
	        //dialogStage.initOwner(stage);
	        Scene scene = new Scene((AnchorPane) screens.get(nome));
	        dialogStage.setScene(scene);
	        
           /* FactoryController controller = ((FactoryController) myLoader.getController());
            controller.setFactoryController(this);
	        controller.setDialogStage(dialogStage);*/
	        // Mostra a janela e espera até o usuário fechar.
	        //dialogStage.showAndWait();
	        dialogStage.show();
            return true;
        } else {
            System.out.println("Tela não carregada");
            return false;
        }

    }


    public Object getController(String nome){
    	
    	return control.get(nome);
    }
    public HashMap<String, Object> getControl() {
		return control;
	}

	public void setControl(HashMap<String, Object> control) {
		this.control = control;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public boolean unloadScreen(String name) {
        if (screens.remove(name) == null && this.control.remove(name) == null) {
            System.out.println("Não existe");
            return false;
        } else {
        	for(Map.Entry<String, Object> entry : this.popStage.entrySet()) {
        	    String key = entry.getKey();
        	    Stage dialogStage = (Stage)  entry.getValue();
        	    if(key.equals(name)){
        	    	dialogStage.close();
        	    }
        	    // do what you have to do here
        	    // In your case, an other loop.
        	}
            return true;
        }
    }
	
	public static void confirma(){
		Alert dialogoErro = new Alert(Alert.AlertType.CONFIRMATION);
        dialogoErro.setTitle("Ação");
        dialogoErro.setHeaderText("Ação Solicitada.");
        dialogoErro.setContentText("Ação Solicitada foi efetuada com sucesso.");
        dialogoErro.showAndWait();
	}
	
	public static void erro(String erro){
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
}

