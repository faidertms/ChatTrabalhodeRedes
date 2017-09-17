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
                Tipo tipo = mensagem.getTipo();
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        if (tipo.equals(Tipo.INDIVIDUAL)) {//**************
                        	try {
								individual();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        } else if (tipo.equals(Tipo.USUARIOSON) || tipo.equals(Tipo.ALTERARESTADO)) {//******************
                        	System.out.println("Sala:"+mensagem.getSala());
                        	System.out.println(mensagem.getEstados().size());
                        	((LayoutController)control.getController("Sala:"+mensagem.getSala())).atualizarEstado(mensagem);
                        	
                        	
                            //refreshOnlines(mensagem);
                        }else if (tipo.equals(Tipo.TODOS)){
                        		((LayoutController)control.getController("Sala:"+mensagem.getSala())).setText(mensagem.getNome() + " diz: " + mensagem.getTexto() + "\n");

                    	}else if (tipo.equals(Tipo.REMOVIDO)){//*********************
                        	try {
								removido();
							} catch (IOException e) {
								// TODO Auto-generated catch block
							}
                        }else if (tipo.equals(Tipo.KICK)){//********************
                        	control.unloadScreen("Sala:"+mensagem.getSala());
                        	control.erro("Expulso pelo administrador");
                        }else if(tipo.equals(Tipo.ATTSALAS)){//************************
                    		((LayoutController)control.getController("Sala:"+mensagem.getSala())).setSalas(mensagem.getSalasDisponiveis());
                        }
                    }
                });

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
