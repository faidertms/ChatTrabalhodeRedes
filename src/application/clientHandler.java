package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import application.Mensagem.Tipo;

public class clientHandler implements Runnable {
	public clientHandler(Socket socket, Map<String, ObjectOutputStream> onlines) throws IOException {
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream (socket.getInputStream());
		this.onlines = onlines;
	}
	private Map<String, ObjectOutputStream> onlines = new HashMap<String, ObjectOutputStream>();
	private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
	
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
                	System.out.println("Aqui2");
                    onlines.put(mensagem.getNome(), out);
                    enviarOnline();
                }
            } else if (tipo.equals(Tipo.DESCONECTAR)) {
                desconectar(mensagem, out);
                enviarOnline();
                return;
            } else if (tipo.equals(Tipo.INDIVIDUAL)) {
            	enviarParticular(mensagem);
            } else if (tipo.equals(Tipo.TODOS)) {
            	enviarParaTodos(mensagem);
            }
				
			}
			} catch (IOException | ClassNotFoundException e) {
				desconectar(mensagem,out);
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
	
	}
	    private boolean conectar(Mensagem mensagem, ObjectOutputStream out) {
	        if (onlines.size() == 0 || !(onlines.containsKey(mensagem.getNome()))) {
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
	        onlines.remove(mensagem.getNome());

	        mensagem.setTexto("até logo!");
	        mensagem.setTipo(Tipo.TODOS);// ou todos
	        enviarParaTodos(mensagem);
	        enviarOnline();
	        System.out.println("User " + mensagem.getNome() + " saiu da sala");
	    }
	        
        private void enviarMensagem(Mensagem mensagem, ObjectOutputStream output) {
            try {
                output.writeObject(mensagem);
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }

        private void enviarParticular(Mensagem mensagem) {
            for (Map.Entry<String, ObjectOutputStream> kv : onlines.entrySet()) {
                if (kv.getKey().equals(mensagem.getNameReserved())) {
                    try {
                        kv.getValue().writeObject(mensagem);
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }

        private void enviarParaTodos(Mensagem mensagem) {
            for (Map.Entry<String, ObjectOutputStream> kv : onlines.entrySet()) {
                if (!kv.getKey().equals(mensagem.getNome())) {
                    mensagem.setTipo(Tipo.TODOS);
                    try {
                        kv.getValue().writeObject(mensagem);
                    } catch (IOException e) {
                    	e.printStackTrace();
                    }
                }
            }
        }
        
        private void enviarOnline() {
            Set<String> setNames = new HashSet<String>();
            for (Map.Entry<String, ObjectOutputStream> kv : onlines.entrySet()) {
                setNames.add(kv.getKey());
            }
            //AVALIAR
            Mensagem mensagem = new Mensagem();
            mensagem.setTipo(Tipo.USUARIOSON);
            mensagem.setTotalOnline(setNames);

            for (Map.Entry<String, ObjectOutputStream> kv : onlines.entrySet()) {
            	mensagem.setNome(kv.getKey());
                try {
                    kv.getValue().writeObject(mensagem);
                } catch (IOException e) {
                	e.printStackTrace();
                	}
            }
        }

}
