package application;

import java.io.IOException;
import java.net.Socket;

import application.Mensagem.Estado;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Usuario {
	private StringProperty nome;
	private StringProperty ip;
	private StringProperty status;
	Socket socket;
	
	
	public Usuario(String nome, Socket socket, Estado estado) {
		super();
		this.socket = socket;
		this.nome = new SimpleStringProperty(nome);
		this.ip =  new SimpleStringProperty(socket.getInetAddress().getHostAddress());
		this.status =  new SimpleStringProperty(estado.name());
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
	public void setStatus(StringProperty status) {
		this.status = status;
	}
	public void closeSocket(){
		try {
			socket.getInputStream().close();
			Thread.sleep(1000);
			socket.close();
		} catch (IOException e) {
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
