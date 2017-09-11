package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConexaoServidor {
	    
	    private Socket socket;
	    private ObjectOutputStream output;
	    private ObjectInputStream input;
	    
	    public Socket conexao() throws UnknownHostException, IOException {
	            this.socket = new Socket("localhost", 6666);
	            this.output = new ObjectOutputStream(socket.getOutputStream());
	            this.input = new ObjectInputStream(socket.getInputStream());
	            return socket;
	        }
	    
	 
	    
	    public Socket getSocket() {
			return socket;
		}

		public ObjectInputStream getInput() {
			return input;
		}



		public void enviar(Mensagem mensagem) {
	        try {
	            output.writeObject(mensagem);
	        } catch (IOException ex) {
	           
	        }
	    }
		
		public Mensagem receber() throws ClassNotFoundException, IOException{
			return (Mensagem) input.readObject();
		}
	}

