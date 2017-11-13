package application;

import java.io.Serializable;
import java.security.PublicKey;

public class CryptoMensagem implements Serializable{
	private byte[] mensagem = null;
	private byte[] keyCrypto = null;
	PublicKey pub = null;
	
	public PublicKey getPub() {
		return pub;
	}
	public void setPub(PublicKey pub) {
		this.pub = pub;
	}
	public byte[] getMensagem() {
		return mensagem;
	}
	public void setMensagem(byte[] mensagem) {
		this.mensagem = mensagem;
	}
	public byte[] getKeyCrypto() {
		return keyCrypto;
	}
	public void setKeyCrypto(byte[] keyCrypto) {
		this.keyCrypto = keyCrypto;
	}
	

}
