package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.concurrent.Task;
import application.Mensagem.Tipo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ChatHandler extends Task<ObservableList<String>>{
	@SuppressWarnings("unchecked")
	private Mensagem mensagem;
	private ListView<String> listaUsuario;
	private TextArea textoChat;
	private ObservableList<String> usuariosOnline = FXCollections.observableArrayList();
	private ConexaoServidor conexaoHandler;
	private ControllerFactory control;
	
	ObjectInputStream input;
	public ObservableList<String> call() throws Exception {
        mensagem = null;
            while ((mensagem = (Mensagem) input.readObject()) != null) {
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
                        } else if (tipo.equals(Tipo.USUARIOSON)) {
                        	usuariosOnline.clear();
                        	usuariosOnline.addAll(mensagem.getTotalOnlines()); // mudar pra outra
                        	listaUsuario.setItems(usuariosOnline);
                        	listaUsuario.refresh();
                            //refreshOnlines(mensagem);
                        }else if (tipo.equals(Tipo.TODOS)){
                        	textoChat.appendText(mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n");
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
		
	}
	
	public void individual() throws UnknownHostException, IOException{
    	System.out.println(mensagem.getNome());
    	
    	if(!control.existScreen(mensagem.getNome())){
    		System.out.println("erro é aqui será");
        	control.loadScreenPop(mensagem.getNome(), "Layout.fxml");
        	((LayoutController) control.getController(mensagem.getNome())).setUsuarioIndividual(conexaoHandler,mensagem.getNome(), mensagem.getNameReserved());
        	((LayoutController) control.getController(mensagem.getNome())).setText((mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n"));
        	control.setScreenPop(mensagem.getNome());
    	}else{
    		((LayoutController) control.getController(mensagem.getNome())).setText((mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n"));
    	}
	}
		
	}
