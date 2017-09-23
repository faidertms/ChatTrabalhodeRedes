package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import java.io.IOException;
import java.net.UnknownHostException;

import application.Mensagem.Tipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ListView;

import javafx.scene.control.TextArea;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LayoutIndividualController implements FactoryController {
	@FXML
	private AnchorPane achorPane;
	@FXML
	private TextArea textoChat;
	@FXML
	private TextArea mensagemArea;
	@FXML
	private ListView<String> listaUsuario;
	@FXML
	private Button enviarButton;
	@FXML
	private Button sairButton;

	private ControllerFactory controller;
	
	private ConexaoServidor conexaoHandler;
	private String nome;
	private Mensagem mensagem;
	private String nomeAlvo;
	
	ObservableList<String> usuariosOnline = FXCollections.observableArrayList();
	
	// Event Listener on Button[#enviarButton].onAction
	@FXML
	public void enviarMensagem(ActionEvent event) {
		String text = this.mensagemArea.getText();
		if (!text.isEmpty()) {
	        this.mensagem = new Mensagem();
	        this.mensagem.setNome(nome);
	        this.mensagem.setTexto(text);
            this.mensagem.setNameReserved(nomeAlvo);
            this.mensagem.setTipo(Tipo.INDIVIDUAL);
            this.textoChat.appendText("Você disse: " + text + "\n");
            this.conexaoHandler.enviar(mensagem);
		}
		this.mensagemArea.setText("");
	}
	// Event Listener on Button[#sairButton].onAction
	@FXML
	public void sairConversa(ActionEvent event) {
		
		controller.unloadScreen(nomeAlvo);
		//this.controller.unloadScreen("Sala:" + this.idSala);
		if (this.controller.getScreens().size() == 0) {
			this.mensagem = new Mensagem();
			this.mensagem.setNome(nome);
			this.mensagem.setTipo(Tipo.DESCONECTARTOTAL);
			this.conexaoHandler.enviar(mensagem);
			this.controller.removerTodos();
			this.controller.loadScreen("login", "Login.fxml");
			this.controller.setScreen("login");

		} else {
			Stage stage = (Stage) achorPane.getScene().getWindow();
			stage.close();
		}
		
		
	}
	
	public void setUsuarioIndividual(ConexaoServidor conexaoHandler,String nomeAlvo , String nome) throws UnknownHostException, IOException{
		this.usuariosOnline.add(nome);
		this.usuariosOnline.add(nomeAlvo);
		this.nomeAlvo = nomeAlvo;
		this.nome = nome;
		this.listaUsuario.setItems(usuariosOnline);
		this.conexaoHandler = conexaoHandler;
	}
	
	public void setText(String texto){
		textoChat.appendText(texto);
	}

	
    public void setFactoryController(ControllerFactory Controller){
        this.controller = Controller;
    }
    
}
