package application;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.layout.Pane;

public class LoginController  implements FactoryController{
	@FXML
	private Pane LoginController;
	@FXML
	private Button blogin;
	@FXML
	private TextField lusuario;
	@FXML
	private Label laMenss;
	
	private Mensagem mensagem = null;
	
	private ControllerFactory controller;
	private ConexaoServidor conexaoHandler;


	// Event Listener on Button[#blogin].onAction
	
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

    public void setFactoryController(ControllerFactory Controller){
        this.controller = Controller;
    }
    @FXML
	public void actProc(ActionEvent event) throws UnknownHostException, IOException, ClassNotFoundException {
		// TODO Autogenerated
		mensagem = new Mensagem();
		mensagem.setNome(lusuario.getText());
		mensagem.setTipo(Tipo.ABRIRCONEXAO);
		mensagem.setEstado(Estado.DISPONIVEL);
		mensagem.setSala(0);
		//enviar
        conexaoHandler = new ConexaoServidor();
        conexaoHandler.conexao();
        conexaoHandler.enviar(mensagem);
       // in = new ObjectInputStream(socket.getInputStream());
        mensagem = conexaoHandler.receber();//(Mensagem) in.readObject();
		 if (mensagem.getTexto().equals("RECUSADA")) {
	            lusuario.setText("");
	            laMenss.setText("Conex�o n�o realizada!\nTente novamente com um novo nome.");
	      }else{
	    	  controller.loadScreen("Sala:"+mensagem.getSala(),"Layout.fxml");     // posso chamar no login   
	    	  LayoutController inicial = (LayoutController) controller.getController("Sala:"+mensagem.getSala());
	    	  controller.setNome(mensagem.getNome());
	    	  inicial.setConexao(conexaoHandler,mensagem.getNome());
	    	  controller.setScreen("Sala:"+mensagem.getSala());
	    	  this.controller.unloadScreen("login");
	      }
	}

}
