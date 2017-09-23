package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import application.Mensagem.Estado;
import javafx.concurrent.Task;
import application.Mensagem.Tipo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

public class ChatHandler extends Task<ObservableList<String>>{
	@SuppressWarnings("unchecked")
	private Mensagem mensagem;
	private ConexaoServidor conexaoHandler;
	private ControllerFactory control;
	private ObjectInputStream input;
	
	public ObservableList<String> call() throws Exception {
        mensagem = null;
            while ((mensagem = (Mensagem) input.readObject()) != null) {
            	System.out.println(mensagem.getTipo());
            	System.out.println("recebi");
        		GuiHandler task = new GuiHandler(control,mensagem,this.conexaoHandler);
        		Thread thread = new Thread(task);
        		thread.setDaemon(true); // atrlar thread
        		thread.start();
            }
			return null;
        }
 
	public ChatHandler(ConexaoServidor conexaoHandler, ControllerFactory control) throws IOException {
		super();
		//this.textoChat = textoChat;
		this.conexaoHandler = conexaoHandler;
		input = conexaoHandler.getInput();
		this.control = control;

	}
	
	public void removido() throws UnknownHostException, IOException{
	  control.removerTodos();
      this.control.loadScreen("login", "Login.fxml");
      this.control.setScreen("login");
      control.erro("Você foi removido do Servidor");
	}
	
	public void individual() throws UnknownHostException, IOException{
    	System.out.println(mensagem.getNome());
    	
    	if(!control.existScreen(mensagem.getNome())){
        	control.loadScreenPop(mensagem.getNome(), "LayoutIndividual.fxml");
        	((LayoutIndividualController) control.getController(mensagem.getNome())).setUsuarioIndividual(conexaoHandler,mensagem.getNome(), mensagem.getNameReserved());
        	((LayoutIndividualController) control.getController(mensagem.getNome())).setText((mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n"));
        	control.setScreenPop(mensagem.getNome(),true);
    	}else{
    		((LayoutIndividualController) control.getController(mensagem.getNome())).setText((mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n"));
    	}
	}
		
	}
