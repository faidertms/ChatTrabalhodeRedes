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

public class GuiHandler extends Task<ObservableList<String>>{
	@SuppressWarnings("unchecked")
	private Mensagem mensagem;
	private ControllerFactory control;
	private ConexaoServidor conexaoHandler;
	
	public ObservableList<String> call() throws Exception {
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                    	Tipo tipo = mensagem.getTipo();
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
                        	((LayoutController)control.getController("Sala:"+mensagem.getSala())).atualizarEstado(mensagem);

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

			return null;
        }
 
	public GuiHandler(ControllerFactory control, Mensagem mensagem,ConexaoServidor conexaoHandler) throws IOException {
		super();
		this.mensagem = mensagem;
		this.control = control;
		this.conexaoHandler = conexaoHandler;

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
