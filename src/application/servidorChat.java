package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class servidorChat implements Runnable {

	int porta = 6666;
	private ServerSocket serverSocket;
	private InputStream in;
	private OutputStream out;
	private Map<String, ObjectOutputStream> onlines = new HashMap<String, ObjectOutputStream>();

	public servidorChat() throws IOException {
		super();
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
				acess = new Thread(new clientHandler(socket,onlines));
				acess.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public static void main(String[] args) throws IOException {
		servidorChat server = new servidorChat();
		server.run();

	}
}

