package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.application.Platform;
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

public class LayoutController implements FactoryController, Initializable {
	@FXML
	private AnchorPane achorPane;
	@FXML
	private TextArea mensagemArea;
	@FXML
	private Button enviarButton;
	@FXML
	private Button sairButton;
	@FXML
	private Button ButtonSairTotal;
	@FXML
	private Button buttonKick;
	@FXML
	private Button buttonCriarSala;
	@FXML
	private TextArea textoChat;
	@FXML
	private ComboBox<String> comboSalas;
	@FXML
	private ListView<String> listaUsuario;
	@FXML
	private ChoiceBox<String> choiceEstado = new ChoiceBox<String>();
	private ObservableList<String> usuariosOnline = FXCollections.observableArrayList();
	private ObservableList<Estado> estados = FXCollections.observableArrayList();
	private int idSala;
	private ControllerFactory controller;

	private Boolean atualizar;
	private ConexaoServidor conexaoHandler;
	private String nome;
	private Mensagem mensagem = new Mensagem();
	

	public ListView<String> getListaUsuario() {
		return listaUsuario;
	}

	public void setListaUsuario(ListView<String> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}

	public void abrirNovaConversa(MouseEvent event) throws UnknownHostException, IOException {
		if (event.getClickCount() == 2) {
			String clicked = listaUsuario.getSelectionModel().getSelectedItem();
			if (clicked != null && !clicked.equals(nome)) {
				listaUsuario.getSelectionModel().clearSelection();
				controller.loadScreenPop(clicked, "LayoutIndividual.fxml");
				((LayoutIndividualController) controller.getController(clicked)).setUsuarioIndividual(conexaoHandler,
						clicked, nome);
				controller.setScreenPop(clicked, true);

			}
		}
	}

	public void initialize(URL location, ResourceBundle resources) {
		this.atualizar = true;
		this.comboSalas.getItems().add("Sala:" + idSala);
		this.comboSalas.setValue("Sala:" + idSala);
		this.listaUsuario.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> p) {

				ListCell<String> cell = new ListCell<String>() {

					@Override
					protected void updateItem(String t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t);
							atualizar = true;
							Estado estado = estados.get(usuariosOnline.indexOf(t));
							if(nome.equals(t)){
								System.out.println(estado.name());
								choiceEstado.getSelectionModel().select(estado.ordinal());
							}
							if (estado == Estado.DISPONIVEL) {
								this.remover();
								getStyleClass().add("Disponivel");
							} else if (estado == Estado.AUSENTE) {
								remover();
								getStyleClass().add("Ausente");
							} else if (estado == Estado.OCUPADO) {
								this.remover();
								getStyleClass().add("Ocupado");
							} else {
								this.remover();
							}
						} else {
							setText("");
							this.remover();
						}
						atualizar = false;
					}

					void remover() {
						getStyleClass().remove("Disponivel");
						getStyleClass().remove("Ausente");
						getStyleClass().remove("Ocupado");
					}

				};

				return cell;
			}
		});
		//Window theStage = source.getScene().getWindow();
		this.atualizar = false;
	}

	@FXML
	public void changeSala(ActionEvent event) throws IOException {
		if (!atualizar && this.comboSalas.getSelectionModel().getSelectedIndex() >= 0) {
			if (!this.controller.existScreen("Sala:" + this.comboSalas.getSelectionModel().getSelectedIndex())) {
				Mensagem mensagem = new Mensagem();
				mensagem.setSala(this.comboSalas.getSelectionModel().getSelectedIndex());
				mensagem.setTipo(Tipo.ENTRARSALA);
				mensagem.setNome(nome);
				this.conexaoHandler.enviar(mensagem);
				controller.loadScreenPop("Sala:" + this.comboSalas.getSelectionModel().getSelectedIndex(),
						"Layout.fxml");
				((LayoutController) controller
						.getController("Sala:" + this.comboSalas.getSelectionModel().getSelectedIndex()))
								.setJanelaSala(conexaoHandler, nome, mensagem.getSala(), this.comboSalas.getItems());
				controller.setScreenPop("Sala:" + this.comboSalas.getSelectionModel().getSelectedIndex(), false);
			}
			Platform.runLater(() -> this.comboSalas.getSelectionModel().select(idSala));
		}

	}

	// Event Listener on Button[#enviarButton].onAction
	@FXML
	public void enviarMensagem(ActionEvent event) {
		String text = this.mensagemArea.getText();
		if (!text.isEmpty()) {
			this.mensagem = new Mensagem();
			this.mensagem.setNome(nome);
			this.mensagem.setTexto(text);
			this.mensagem.setTipo(Tipo.TODOS);
			this.mensagem.setSala(this.idSala);
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
		} else if (num == 1) {
			this.mensagem.setEstado(Estado.AUSENTE);
		} else if (num == 2) {
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
		this.mensagem.setSala(idSala);
		this.controller.unloadScreen("Sala:" + this.idSala);
		if (this.controller.getScreens().size() == 0) {
			sairTotal(event);

		} else {
			this.conexaoHandler.enviar(mensagem);
		}
	}

	@FXML
	public void criarSala(ActionEvent event) {
		this.mensagem = new Mensagem();
		this.mensagem.setNome(nome);
		this.mensagem.setTipo(Tipo.CRIARSALA);

		int temp = this.comboSalas.getItems().size();
		this.comboSalas.getItems().add("Sala:" + temp);
		controller.loadScreenPop("Sala:" + temp, "Layout.fxml");
		((LayoutController) controller.getController("Sala:" + temp)).setJanelaSala(conexaoHandler, mensagem.getNome(),
				temp, this.comboSalas.getItems());
		controller.setScreenPop("Sala:" + temp, false);
		this.conexaoHandler.enviar(mensagem);

	}

	public void sairTotal(ActionEvent event) {
		this.mensagem = new Mensagem();
		this.mensagem.setNome(nome);
		this.mensagem.setTipo(Tipo.DESCONECTARTOTAL);
		this.conexaoHandler.enviar(mensagem);
		this.controller.removerTodos();
		if(event != null){
			this.controller.loadScreen("login", "Login.fxml");
			this.controller.setScreen("login");
		}
	}

	public void kick(ActionEvent event) {
		String clicked = listaUsuario.getSelectionModel().getSelectedItem();
		if (clicked != null && !clicked.equals(nome)) {

			this.mensagem = new Mensagem();
			this.mensagem.setNome(nome);
			this.mensagem.setNameReserved(clicked);
			this.mensagem.setSala(idSala);
			this.mensagem.setTipo(Tipo.KICK);
			this.conexaoHandler.enviar(mensagem);
		}
	}

	public void setConexao(ConexaoServidor conexaoHandler, String nome) throws IOException {
		this.conexaoHandler = conexaoHandler;
		this.nome = nome;
		ChatHandler task = new ChatHandler(conexaoHandler, controller);
		Thread thread = new Thread(task);
		thread.setDaemon(true); // atrlar thread
		thread.start();

	}

	public void setJanelaSala(ConexaoServidor conexaoHandler, String nome, int sala, ObservableList<String> salas) {
		List<String> listaDeSala = new ArrayList<String>();
		for(String idSala : salas){ // criar uma nova referencia
			listaDeSala.add(idSala);
		}
		this.atualizar = true;
		this.conexaoHandler = conexaoHandler;
		this.nome = nome;
		this.idSala = sala;
		this.comboSalas.getItems().clear();
		this.comboSalas.getItems().addAll(listaDeSala);
		//this.atualizar = false;
		Platform.runLater(() -> {this.comboSalas.getSelectionModel().select(idSala);this.atualizar = false;});
		

	}

	public void setFactoryController(ControllerFactory Controller) {
		this.controller = Controller;
		this.choiceEstado.getItems().addAll("Disponivel", "Ausente", "Ocupado");
		this.choiceEstado.getSelectionModel().selectFirst();
		choiceEstado.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				if(!atualizar){
					enviarEstado(arg2.intValue());
				}
			}
		});

	}

	public void setText(String texto) {
		textoChat.appendText(texto);
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public void atualizarEstado(Mensagem mensagem) {
		usuariosOnline.clear();
		estados.clear();
		estados.addAll(mensagem.getEstados());
		usuariosOnline.addAll(mensagem.getUsuarios()); // mudar pra outra
		listaUsuario.setItems(usuariosOnline);
		listaUsuario.refresh();
		
	}

	public ObservableList<Estado> getEstados() {
		return estados;
	}

	public void setEstados(ObservableList<Estado> estados) {
		this.estados = estados;
	}

	public ObservableList<String> getUsuariosOnline() {
		return usuariosOnline;
	}

	public void setUsuariosOnline(ObservableList<String> usuariosOnline) {
		this.usuariosOnline = usuariosOnline;
	}
	

	public void setSalas(List<String> salasDisponiveis) {
		String temp = this.comboSalas.getValue();
		this.atualizar = true;
		this.comboSalas.getItems().clear();
		this.comboSalas.getItems().addAll(salasDisponiveis);
		this.comboSalas.getSelectionModel().select(idSala);
		this.atualizar = false;
	}
}
