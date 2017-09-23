package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;

public class ServidorChat implements Runnable {

	int porta = 6666;
	private ServerSocket serverSocket;
	ObservableList<Usuario> usuarioList ; // usuarios totais 
	private List<Sala> listaDeSala;
	public ServidorChat(ObservableList<Usuario> usuarioList) throws IOException {
		super();
		this.usuarioList = usuarioList;
		this.listaDeSala = new ArrayList<Sala>();
		this.serverSocket = new ServerSocket(porta);
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.listaDeSala.add(new Sala(0,null)); // sala inicial
		Thread acess = null;
		System.out.println("Começando");
		while(true){
			Socket socket;
			try {
				System.out.println("Esperando uma ação");
				socket = serverSocket.accept();
				acess = new Thread(new clientHandler(socket,this.usuarioList,this.listaDeSala));
				acess.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}

