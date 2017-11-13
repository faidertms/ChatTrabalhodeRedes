package application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAKeyGenParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	private PrivateKey priv = null;
	private PublicKey pub = null;

	Crypto() {
		gerarChaves();
	}

	public PrivateKey getPriv() {
		return priv;
	}

	public void setPriv(PrivateKey priv) {
		this.priv = priv;
	}

	public PublicKey getPub() {
		return pub;
	}

	public void setPub(PublicKey pub) {
		this.pub = pub;
	}

	private static final int RSAKEYSIZE = 1024;

	public void gerarChaves() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(new RSAKeyGenParameterSpec(RSAKEYSIZE, RSAKeyGenParameterSpec.F4));
			KeyPair kpr = kpg.generateKeyPair();
			priv = kpr.getPrivate();
			pub = kpr.getPublic();
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	CryptoMensagem cryptMensagem(Mensagem mensagem, PublicKey servidor) {
		CryptoMensagem crypt = new CryptoMensagem();
		KeyGenerator keygenerator = null;
		try {
			keygenerator = KeyGenerator.getInstance("DES");
			SecretKey chaveDES = keygenerator.generateKey();
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			desCipher.init(Cipher.ENCRYPT_MODE, chaveDES);
			crypt.setMensagem(desCipher.doFinal(mensagemBytes(mensagem)));

			Cipher rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.ENCRYPT_MODE, servidor);
			crypt.setKeyCrypto(rsaCipher.doFinal(chaveDES.getEncoded()));
			return crypt;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	Mensagem DescryptMensagem(CryptoMensagem mensagem) {

		try {
			Cipher rsaCipher = Cipher.getInstance("RSA");
			rsaCipher.init(Cipher.DECRYPT_MODE, this.priv);
			byte chaveDES2[]= rsaCipher.doFinal(mensagem.getKeyCrypto());
			SecretKey chaveDES = new SecretKeySpec(chaveDES2, 0, chaveDES2.length, "DES");
			Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			desCipher.init(Cipher.DECRYPT_MODE, chaveDES);
			return (Mensagem) this.byteObject(desCipher.doFinal(mensagem.getMensagem()));
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	private byte[] mensagemBytes(Object obj) throws IOException {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutput out = new ObjectOutputStream(bos)) {
			out.writeObject(obj);
			return bos.toByteArray();
		}
	}

	private Object byteObject(byte crpt[]) {

		try {
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(crpt));
			return (Object) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
