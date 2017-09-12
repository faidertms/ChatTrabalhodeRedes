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
	public clientHandler(Socket socket, Map<String, ObjectOutputStream> online,Map<String, Estado> statusOnline , ObservableList<Usuario> usuarioList) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream (socket.getInputStream());
		this.usuarioList = usuarioList;
		this.online = online;
		this.socket = socket;
		this.statusOnline = statusOnline;
	}
	private Map<String, ObjectOutputStream> online = new HashMap<String, ObjectOutputStream>();
	private Map<String, Estado> statusOnline = new HashMap<String, Estado>();
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
                	this.usuarioList.add(new Usuario(mensagem.getNome(),socket,mensagem.getEstado()));
                    online.put(mensagem.getNome(), out);
                    statusOnline.put(mensagem.getNome(), mensagem.getEstado());
                    enviarOnline();
                }
            } else if (tipo.equals(Tipo.DESCONECTAR)) {
                desconectar(mensagem, out);
                //enviarOnline();
                return;
            } else if (tipo.equals(Tipo.INDIVIDUAL)) {
            	enviarParticular(mensagem);
            } else if (tipo.equals(Tipo.TODOS)) {
            	enviarParaTodos(mensagem);
            }
				
			}
			} catch (IOException | ClassNotFoundException e) {
				this.timeOutOuRemovido(mensagem, out);
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	
	}
	    private boolean conectar(Mensagem mensagem, ObjectOutputStream out) {
	        if (online.size() == 0 || !(online.containsKey(mensagem.getNome()))) {
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
	    
	    private void timeOutOuRemovido(Mensagem mensagem, ObjectOutputStream out) {
	        mensagem.setTexto("até logo!");
	        mensagem.setTipo(Tipo.TODOS);// ou todos
	        enviarParaTodos(mensagem);
	        mensagem.setTipo(Tipo.REMOVIDO);
	        enviarParticular(mensagem);
	        System.out.println("User " + mensagem.getNome() + " saiu da sala");
	        //online.remove(mensagem.getNome());
	        this.statusOnline.remove(mensagem.getNome());
	        this.usuarioList.remove(new Usuario(mensagem.getNome()));
	        enviarOnline();
	    }
	    
	    private void desconectar(Mensagem mensagem, ObjectOutputStream out) {
	        online.remove(mensagem.getNome());
	        this.statusOnline.remove(mensagem.getNome());
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
            for (Map.Entry<String, ObjectOutputStream> kv : online.entrySet()) {
                if (kv.getKey().equals(mensagem.getNameReserved())) {
                    try {
                        kv.getValue().writeObject(mensagem);
                        kv.getValue().flush();
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }

        private void enviarParaTodos(Mensagem mensagem) {
            for (Map.Entry<String, ObjectOutputStream> kv : online.entrySet()) {
                if (!kv.getKey().equals(mensagem.getNome())) {
                    mensagem.setTipo(Tipo.TODOS);
                    try {
                        kv.getValue().writeObject(mensagem);
                        kv.getValue().flush();
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }
        
        private void enviarOnline() {
            List<String> usuarios = new ArrayList<String>();
            List<Estado> estados = new ArrayList<Estado>();
            for (Map.Entry<String, Estado> kv : statusOnline.entrySet()) {
            	usuarios.add(kv.getKey());
            	estados.add(kv.getValue());
            }
            //AVALIAR
            Mensagem mensagem = new Mensagem();
            mensagem.setTipo(Tipo.USUARIOSON);
            mensagem.setEstados(estados);
            mensagem.setUsuarios(usuarios);

            for (Map.Entry<String, ObjectOutputStream> kv : online.entrySet()) {
            	mensagem.setNome(kv.getKey());
                try {
                    kv.getValue().writeObject(mensagem);
                    kv.getValue().flush();
                } catch (IOException e) {
                	e.printStackTrace();
                	}
            }
        }

}
