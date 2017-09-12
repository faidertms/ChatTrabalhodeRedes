package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import application.Mensagem.Estado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;

public class TelaControleController implements Initializable {
	@FXML
	private Button buttonRemover;
	@FXML
	private TableView<Usuario> usuariosTable;
	@FXML
	private TableColumn<Usuario, String> usuarioColuna;
	@FXML
	private TableColumn<Usuario, String> statusColuna;
	@FXML
	private TableColumn<Usuario, String> ipColuna;
	
	private Thread thread;
	
	ObservableList<Usuario> usuarioList = FXCollections.observableArrayList();
	private Map<String, ObjectOutputStream> online = new HashMap<String, ObjectOutputStream>();
	private Map<String, Estado> statusOnline = new HashMap<String, Estado>();

	public void initialize(URL location, ResourceBundle resources) {
		usuarioColuna.setCellValueFactory(cellData ->  cellData.getValue().getNome());
	    statusColuna.setCellValueFactory(cellData -> cellData.getValue().getStatus());
	    ipColuna.setCellValueFactory(cellData -> cellData.getValue().getIp());
	    usuariosTable.setItems(usuarioList);
	    ServidorChat chat = null;
		try {
			chat = new ServidorChat(usuarioList);
		} catch (IOException e) {
			
		}
	    thread = new Thread(chat);
	    thread.setDaemon(true);
	    thread.start();
	    
    }
	
	
	// Event Listener on Button[#buttonRemover].onAction
	@FXML
	public void removerUsuario(ActionEvent event) {
		Usuario get = usuariosTable.getSelectionModel().getSelectedItem();
		int select = usuariosTable.getSelectionModel().getSelectedIndex();
	    if (get!= null) {
	        online.remove(get.getNome().get());
	        statusOnline.remove(get.getNome().get());
	        get.closeSocket();
	        this.usuarioList.remove(select);
	        get = null;
	    }else{
	    	//erro();
	    }
	}

	public void setOnline(Map<String, ObjectOutputStream> online , Map<String, Estado> statusOnline ) {
		this.online = online;
		this.statusOnline = statusOnline;
	}
}
