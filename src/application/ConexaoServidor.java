package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.PublicKey;

public class ConexaoServidor {

	private Socket socket;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Crypto crypt = null;
	private PublicKey pubServidor = null;

	public Socket conexao() throws UnknownHostException, IOException {
		this.socket = new Socket("localhost", 6666);
		this.output = new ObjectOutputStream(socket.getOutputStream());
		this.input = new ObjectInputStream(socket.getInputStream());
		this.crypt = new Crypto();
		return socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public void trocaChave() {
		try {
			this.pubServidor = ((CryptoMensagem) input.readObject()).getPub();// recebe a chave publica do servidor
		} catch (IOException | ClassNotFoundException ex) {

		}
	}

	public void iniciar(Mensagem mensagem) {
		CryptoMensagem crypt = this.crypt.cryptMensagem(mensagem, pubServidor);
		crypt.setPub(this.crypt.getPub());//manda a chave pública do cliente
		try {
			output.writeObject(crypt);
		} catch (IOException ex) {

		}
	}

	public void enviar(Mensagem mensagem) {
		try {
			output.writeObject(this.crypt.cryptMensagem(mensagem, pubServidor));
		} catch (IOException ex) {

		}
	}

	public Mensagem receber() throws ClassNotFoundException, IOException {
		return this.crypt.DescryptMensagem((CryptoMensagem) input.readObject());
	}
}
