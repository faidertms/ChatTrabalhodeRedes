package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
	private StringProperty nome;
	private StringProperty ip;
	private StringProperty status;
	private ObjectOutputStream out;
	Socket socket;
	
	
	public Usuario(String nome, Socket socket, Estado estado,ObjectOutputStream out) {
		super();
		this.socket = socket;
		this.nome = new SimpleStringProperty(nome);
		this.ip =  new SimpleStringProperty(socket.getInetAddress().getHostAddress());
		this.status =  new SimpleStringProperty(estado.name());
		this.out = out;
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
		this.status.set(status);;
	}
	public void closeSocket(){
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
}
