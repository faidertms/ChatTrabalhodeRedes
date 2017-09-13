package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import application.Mensagem.Estado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ServidorChat implements Runnable {

	int porta = 6666;
	private ServerSocket serverSocket;
	ObservableList<Usuario> usuarioList ;
	public ServidorChat(ObservableList<Usuario> usuarioList) throws IOException {
		super();
		this.usuarioList = usuarioList;
		this.serverSocket = new ServerSocket(porta);
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub

		Thread acess = null;
		System.out.println("Começando");
		while(true){
			Socket socket;
			try {
				System.out.println("Esperando uma ação");
				socket = serverSocket.accept();
				acess = new Thread(new clientHandler(socket,this.usuarioList));
				acess.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}

