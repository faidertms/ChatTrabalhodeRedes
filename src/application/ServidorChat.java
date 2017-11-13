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
	private Crypto crypt;
	public ServidorChat(ObservableList<Usuario> usuarioList) throws IOException {
		super();
		this.usuarioList = usuarioList;
		this.listaDeSala = new ArrayList<Sala>();
		this.serverSocket = new ServerSocket(porta);
		this.crypt = new Crypto();
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.listaDeSala.add(new Sala(0,null)); // sala inicial
		Thread acess = null;
		System.out.println("Come�ando");
		while(true){
			Socket socket;
			try {
				System.out.println("Esperando uma a��o");
				socket = serverSocket.accept();
				acess = new Thread(new clientHandler(socket,this.usuarioList,this.listaDeSala,this.crypt));
				acess.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
}

