package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
	private StringProperty nome;
	private StringProperty ip;
	private StringProperty status;
	private ObjectOutputStream out;
	private Socket socket;
	private List<Sala> salaAtivaUsuario;
	private PublicKey pubUsuario = null;
	private SecretKey chaveDES = null;

	public Usuario(String nome, Socket socket, Estado estado, ObjectOutputStream objectOutputStream,PublicKey pubUsuario,SecretKey chaveDES ) {
		super();
		this.socket = socket;
		this.nome = new SimpleStringProperty(nome);
		this.ip = new SimpleStringProperty(socket.getInetAddress().getHostAddress());
		this.status = new SimpleStringProperty(estado.name());
		this.out = objectOutputStream;
		salaAtivaUsuario = new ArrayList<Sala>();
		this.pubUsuario = pubUsuario;
		this.chaveDES = chaveDES;
	}

	public PublicKey getPubUsuario() {
		return pubUsuario;
	}

	public void setPubUsuario(PublicKey pubUsuario) {
		this.pubUsuario = pubUsuario;
	}

	public ObjectOutputStream getOut() {
		return out;
	}

	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Usuario(String nome) { // só para comparação
		super();
		this.nome = new SimpleStringProperty(nome);
	}

	@Override
	public boolean equals(Object arg0) {
		return nome.get().equals(((Usuario) arg0).getNome().get());
	}

	public StringProperty getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = new SimpleStringProperty(nome);
	}

	public StringProperty getIp() {
		return ip;
	}

	public void setIp(StringProperty ip) {
		this.ip = ip;
	}

	public StringProperty getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status.set(status);
		;
	}

	public void closeSocket() {
		Mensagem mensagem = new Mensagem();
		mensagem.setNome(nome.get());
		mensagem.setTipo(Tipo.REMOVIDO);
		try {
			out.writeObject(mensagem);
			out.flush();
			Thread.sleep(1000);
			socket.close();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}

	}

	public List<Sala> getSalaAtivaUsuario() {
		return salaAtivaUsuario;
	}

	public void setSalaAtivaUsuario(List<Sala> salaAtivaUsuario) {
		this.salaAtivaUsuario = salaAtivaUsuario;
	}

	public SecretKey getChaveDES() {
		return chaveDES;
	}

	public void setChaveDES(SecretKey chaveDES) {
		this.chaveDES = chaveDES;
	}
}
