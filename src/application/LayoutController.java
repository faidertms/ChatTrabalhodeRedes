package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;


import javafx.scene.control.ListView;

import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class LayoutController implements FactoryController{
	@FXML
	private AnchorPane achorPane;
	@FXML
	private TextArea mensagemArea;
	@FXML
	private Button enviarButton;
	@FXML
	private Button sairButton;
	@FXML
	private TextArea textoChat;
	@FXML
	private ListView<String> listaUsuario;
	@FXML
	ChoiceBox<String> choiceEstado = new ChoiceBox<String>();
	
	ObservableList<String> usuariosOnline = FXCollections.observableArrayList();
	private ObservableList<Estado> estados = FXCollections.observableArrayList();
	
	private ControllerFactory controller;

	
	
	private ConexaoServidor conexaoHandler;
	private String nome;
	private Mensagem mensagem;
	
	public void abrirNovaConversa(MouseEvent event) throws UnknownHostException, IOException{
        if (event.getClickCount() == 2) {
            //Use ListView's getSelected Item
            String clicked = listaUsuario.getSelectionModel().getSelectedItem();
            if(clicked != null && !clicked.equals(nome)){
            	listaUsuario.getSelectionModel().clearSelection();
            	controller.loadScreenPop(clicked, "LayoutIndividual.fxml");
            	((LayoutIndividualController) controller.getController(clicked)).setUsuarioIndividual(conexaoHandler,clicked, nome);
            	controller.setScreenPop(clicked);
            	
            }
        }
	}
	

	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	// Event Listener on Button[#enviarButton].onAction
	@FXML
	public void enviarMensagem(ActionEvent event) {
		String text = this.mensagemArea.getText();
		if (!text.isEmpty()) {
	        this.mensagem = new Mensagem();
	        this.mensagem.setNome(nome);
	        this.mensagem.setTexto(text);
            this.mensagem.setTipo(Tipo.TODOS);;
            this.textoChat.appendText("Você disse: " + text + "\n");
            this.conexaoHandler.enviar(mensagem);
        }
        this.mensagemArea.setText("");
	}
	
	public void enviarEstado(int num) {
        this.mensagem = new Mensagem();
        this.mensagem.setNome(nome);
        this.mensagem.setTipo(Tipo.ALTERARESTADO);
		if (num == 0) {
	        this.mensagem.setEstado(Estado.DISPONIVEL);
        }else if(num == 1){
        	this.mensagem.setEstado(Estado.AUSENTE);
        }else if(num == 2){
        	this.mensagem.setEstado(Estado.OCUPADO);
        }
		this.conexaoHandler.enviar(mensagem);
        this.mensagemArea.setText("");
	}
	
	
	// Event Listener on Button[#sairButton].onAction
	@FXML
	public void sairConversa(ActionEvent event) {
		this.mensagem = new Mensagem();
        this.mensagem.setNome(nome);
        this.mensagem.setTipo(Tipo.DESCONECTAR);
        this.conexaoHandler.enviar(mensagem);
        
        this.controller.loadScreen("login", "Login.fxml");
        this.controller.setScreen("login");
        this.controller.unloadScreen("inicial");
	}
	
	public void setConexao(ConexaoServidor conexaoHandler,String nome) throws IOException{
		this.conexaoHandler = conexaoHandler;
		conexaoHandler.getSocket();
		this.nome = nome;
			//Thread thread = new Thread(new ChatHandler(listaUsuario, mensagemArea, usuariosOnline, conexaoHandler));
		//thread.start();
		ChatHandler task = new ChatHandler(listaUsuario, textoChat, conexaoHandler,controller);
		//task.setOnSucceeded(ev -> task.getValue());
		Thread thread = new Thread(task);
		thread.setDaemon(true); // atrlar thread
		thread.start();
		
		
	}

    public void setFactoryController(ControllerFactory Controller){
        this.controller = Controller;
        this.choiceEstado.getItems().addAll("Disponivel","Ausente","Ocupado");
        this.choiceEstado.getSelectionModel().selectFirst();
        choiceEstado.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
    		@Override
    		public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
    			enviarEstado(arg2.intValue());
    		}
            });
        
    }
}
