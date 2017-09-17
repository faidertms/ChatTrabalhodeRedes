package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

	@Override
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
	    if (get!= null) {
	        get.closeSocket();
	        get = null;
	    }else{
	    	//erro();
	    }
	}
}
