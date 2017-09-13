package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class clientHandler implements Runnable {
	public clientHandler(Socket socket , ObservableList<Usuario> usuarioList) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream (socket.getInputStream());
		this.usuarioList = usuarioList;
		this.socket = socket;
	}
	private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    ObservableList<Usuario> usuarioList;
	
	@Override
	public void run() {

		// TODO Auto-generated method stub
		Mensagem mensagem = null;
		try {	
			while ((mensagem = (Mensagem) in.readObject()) != null) { 
				System.out.println(mensagem.getTipo());
				System.out.println(mensagem.getNome());
				Tipo tipo = mensagem.getTipo();
				if (tipo.equals(Tipo.ABRIRCONEXAO)) {
		            boolean isConnect = conectar(mensagem, out);
		            if (isConnect) {
		            	this.usuarioList.add(new Usuario(mensagem.getNome(),socket,mensagem.getEstado(),out));
		                enviarOnline();
		            }
		        } else if (tipo.equals(Tipo.DESCONECTAR)) {
		            desconectar(mensagem, out);
		            return;
		        } else if (tipo.equals(Tipo.INDIVIDUAL)) {
		        	enviarParticular(mensagem);
		        } else if (tipo.equals(Tipo.TODOS) || tipo.equals(Tipo.ALTERARESTADO)) {
		        	enviarParaTodos(mensagem);
				
		        }
			}
		} catch (IOException | ClassNotFoundException e) {
			this.desconectar(mensagem, out);
		}
	}
	    private boolean conectar(Mensagem mensagem, ObjectOutputStream out) {
	        if (usuarioList.isEmpty() || !(usuarioList.equals(new Usuario(mensagem.getNome())))) {
	        	mensagem.setTexto("");
	            mensagem.setTexto("ACEITA");
	            enviarMensagem(mensagem, out);
	            return true;
	        }else{ 
	            mensagem.setTexto("RECUSADA");
	            enviarMensagem(mensagem, out);
	            return false;
	        }

	    
	    }
	    
	    private void desconectar(Mensagem mensagem, ObjectOutputStream out) {
	        //online.remove(mensagem.getNome());
	        //this.statusOnline.remove(mensagem.getNome());
	        this.usuarioList.remove(new Usuario(mensagem.getNome()));
	        mensagem.setTexto("até logo!");
	        mensagem.setTipo(Tipo.TODOS);// ou todos
	        enviarParaTodos(mensagem);
	        enviarOnline();
	        System.out.println("User " + mensagem.getNome() + " saiu da sala");
	    }
	        
        private void enviarMensagem(Mensagem mensagem, ObjectOutputStream output) {
            try {
                output.writeObject(mensagem);
                output.flush();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }

        private void enviarParticular(Mensagem mensagem) {
            for (Usuario kv: usuarioList) {
                if (kv.equals(new Usuario(mensagem.getNameReserved()))) {
                    try {
                        kv.getOut().writeObject(mensagem);
                        kv.getOut().flush();
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }

        private void enviarParaTodos(Mensagem mensagem) {
            for (Usuario kv: usuarioList) {
                if (!kv.equals(new Usuario(mensagem.getNome()))) {
                    mensagem.setTipo(Tipo.TODOS);
                    try {
                    	   kv.getOut().writeObject(mensagem);
                           kv.getOut().flush();
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }
        
        private void enviarOnline() {
            List<String> usuarios = new ArrayList<String>();
            List<Estado> estados = new ArrayList<Estado>();
            for (Usuario kv: usuarioList) {
            	usuarios.add(kv.getNome().get());
            	estados.add(Estado.valueOf(kv.getStatus().get()));
            }
            //AVALIAR
            Mensagem mensagem = new Mensagem();
            mensagem.setTipo(Tipo.USUARIOSON);
            mensagem.setEstados(estados);
            mensagem.setUsuarios(usuarios);

            for (Usuario kv: usuarioList) {
            	mensagem.setNome(kv.getNome().get());
                try {
                    kv.getOut().writeObject(mensagem);
                    kv.getOut().flush();
                } catch (IOException e) {
                	e.printStackTrace();
                	}
            }
        }

}
