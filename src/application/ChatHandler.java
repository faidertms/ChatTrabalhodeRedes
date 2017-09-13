package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
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
	private ListView<String> listaUsuario;
	private TextArea textoChat;
	private ObservableList<String> usuariosOnline = FXCollections.observableArrayList();
	private ObservableList<Estado> estados = FXCollections.observableArrayList();
	private ConexaoServidor conexaoHandler;
	private ControllerFactory control;
	Set<String> usuarios;
	
	ObjectInputStream input;
	public ObservableList<String> call() throws Exception {
        mensagem = null;
            while ((mensagem = (Mensagem) input.readObject()) != null) {
            	System.out.println("recebi");
                Tipo tipo = mensagem.getTipo();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        if (tipo.equals(Tipo.INDIVIDUAL)) {
                        	try {
								individual();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (tipo.equals(Tipo.USUARIOSON) || tipo.equals(Tipo.ALTERARESTADO)) {
                        	usuariosOnline.clear();
                        	estados.clear();
                        	estados.addAll(mensagem.getEstados());
                        	usuariosOnline.addAll(mensagem.getUsuarios()); // mudar pra outra
                        	listaUsuario.setItems(usuariosOnline);
                        	listaUsuario.refresh();
                            //refreshOnlines(mensagem);
                        }else if (tipo.equals(Tipo.TODOS)){
                        	textoChat.appendText(mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n");
                        }else if (tipo.equals(Tipo.REMOVIDO)){
                        	try {
								removido();
							} catch (IOException e) {
								// TODO Auto-generated catch block
							}
                        }
                    }
                });

            }
			return usuariosOnline;
        }
 
	public ChatHandler(ListView<String> listaUsuario, TextArea textoChat, ConexaoServidor conexaoHandler, ControllerFactory control) throws IOException {
		super();
		this.listaUsuario = listaUsuario;
		this.textoChat = textoChat;
		this.conexaoHandler = conexaoHandler;
		input = conexaoHandler.getInput();
		this.control = control;
		
        this.listaUsuario.setCellFactory(new Callback<ListView<String>, ListCell<String>>(){

            @Override
            public ListCell<String> call(ListView<String> p) {

                ListCell<String> cell = new ListCell<String>(){

                    @Override
                    protected void updateItem(String t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null ) {
                            setText( t);
                            Estado estado = estados.get(usuariosOnline.indexOf(t));
                            	if( estado == Estado.DISPONIVEL){
                            		this.remover();
                                    getStyleClass().add("Disponivel");
                                }else if(estado == Estado.AUSENTE){
                                	remover();
                                	getStyleClass().add("Ausente");
                                } else if(estado == Estado.OCUPADO){
                                	this.remover();
                                	getStyleClass().add("Ocupado");
                                }else{
                                	this.remover();
                                }
                        } else {
                            setText("");
                            this.remover();
                        }
                    }
                    
                    void remover(){
                    	getStyleClass().remove("Disponivel");
                    	getStyleClass().remove("Ausente");
                    	getStyleClass().remove("Ocupado");
                    }

                };

                return cell;
            }
        });
		
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
        	control.setScreenPop(mensagem.getNome());
    	}else{
    		((LayoutIndividualController) control.getController(mensagem.getNome())).setText((mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n"));
    	}
	}
		
	}
