package application;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

import application.Mensagem.Estado;
import application.Mensagem.Tipo;
import javafx.collections.ObservableList;

public class clientHandler implements Runnable {
	public clientHandler(Socket socket , ObservableList<Usuario> usuarioList,List<Sala> listaDeSala,Crypto crypt) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream (socket.getInputStream());
		this.usuarioList = usuarioList;
		this.socket = socket;
		this.listaDeSala = listaDeSala;
		this.crypt = crypt;
	}
	private List<Sala> listaDeSala;
	private ObservableList<Usuario> usuarioList;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;
    private Usuario usuario;
    boolean isConnect;
    private Crypto crypt;
    private SecretKey chaveDES = null;
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Mensagem mensagem = null;
		CryptoMensagem cryptomensagem = null;
		this.enviarPub(this.out);
		try {	
			while ((cryptomensagem = (CryptoMensagem) in.readObject()) != null) { 
				if(!isConnect){
					this.chaveDES = this.crypt.DescryptChave(cryptomensagem);
				}
				mensagem = crypt.DescryptMensagem(cryptomensagem,this.chaveDES);
				System.out.println(mensagem.getTipo());
				System.out.println(mensagem.getNome());
				Tipo tipo = mensagem.getTipo();
				
				if (tipo.equals(Tipo.ABRIRCONEXAO)) { //*********
					usuario = new Usuario(mensagem.getNome(),socket,Estado.DISPONIVEL,out,cryptomensagem.getPub(),this.chaveDES);
		            isConnect = conectar(mensagem);
		            if (isConnect) {
		            	this.usuarioList.add(usuario);   
		            	this.listaDeSala.get(0).getUsuarioList().add(usuario);        	
		            	usuario.getSalaAtivaUsuario().add(this.listaDeSala.get(0));
		                enviarSalas();
		                try {
							Thread.sleep(500); // evitaer bug
							enviarOnline(this.listaDeSala.get(0).getUsuarioList(),0);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		                
		            }
		            
		        } else if (tipo.equals(Tipo.DESCONECTARTOTAL)) { // tratado no controller
		        	desconectarTotal(mensagem);
		            return;
		        } else if (tipo.equals(Tipo.INDIVIDUAL)) {//***********************
		        	enviarParticular(mensagem);
		        } else if (tipo.equals(Tipo.TODOS)) {//***********************
		        	enviarParaTodos(mensagem,usuario.getSalaAtivaUsuario().get(usuario.getSalaAtivaUsuario().indexOf(new Sala(mensagem.getSala(),null))).getUsuarioList());
				
		        } else if (tipo.equals(Tipo.ALTERARESTADO)){//************************
		        	this.usuarioList.get(this.usuarioList.indexOf(usuario)).setStatus(mensagem.getEstado().name());;
			        for(Sala sala : usuario.getSalaAtivaUsuario()){
			        	mensagem.setSala(sala.getId());;
			        	enviarOnline(sala.getUsuarioList(),sala.getId());
			        }
		        } else if (tipo.equals(Tipo.ENTRARSALA)){
		        	Sala temp = listaDeSala.get(mensagem.getSala());
		        	temp.getUsuarioList().add(usuario);
		        	usuario.getSalaAtivaUsuario().add(temp);
		        	enviarOnline(temp.getUsuarioList(),temp.getId());
		        	
		        }else if (tipo.equals(Tipo.KICK)){//****************
		        	for(Sala sala : usuario.getSalaAtivaUsuario()){
		        		if(sala.getId() == mensagem.getSala()){
			        		if(sala.getAdministrador() != null && sala.getAdministrador().getNome().get().equals(mensagem.getNome())){ 
			        			Usuario usuarioTemp = sala.getUsuarioList().get(sala.getUsuarioList().indexOf(new Usuario(mensagem.getNameReserved())));
			        			usuarioTemp.getSalaAtivaUsuario().remove(sala);
			        			sala.getUsuarioList().remove(usuario);
			        			mensagem.setTipo(Tipo.KICK);
			        			this.enviarMensagem(mensagem, usuarioTemp);
			        			this.enviarOnline(sala.getUsuarioList(),sala.getId());
			        		}
		        		}
		        	}	
		        }else if (tipo.equals(Tipo.CRIARSALA)){
		        	Sala temp = new Sala(listaDeSala.size(),usuario);
		        	temp.getUsuarioList().add(usuario);
		        	usuario.getSalaAtivaUsuario().add(temp);
		        	listaDeSala.add(temp);
		        	this.enviarSalas();
		        	this.enviarOnline(temp.getUsuarioList(), temp.getId());
		        		        	
		        	
		        }else if (tipo.equals(Tipo.DESCONECTAR)){
		        	desconectarSala(mensagem);	
		        }
			}
		} catch (IOException | ClassNotFoundException e) {
			if(isConnect)
			this.desconectarTotal(mensagem);
		}
	}
	    private boolean conectar(Mensagem mensagem) {
	        if (usuarioList.isEmpty() || !(usuarioList.contains((usuario)))) { 
	        	mensagem.setTexto("");
	            mensagem.setTexto("ACEITA");
	            mensagem.setTipo(Tipo.ABRIRCONEXAO);
	            enviarMensagem(mensagem, this.usuario);
	            return true;
	        }else{ 
	            mensagem.setTexto("RECUSADA");
	            enviarMensagem(mensagem, this.usuario);
	            return false;
	        }

	    
	    }
	   
	    private void desconectarSala(Mensagem mensagem) { 
	    	List<Usuario> listaUsuarioMensagem;
	        mensagem.setTexto("até logo!");
	        listaUsuarioMensagem = usuario.getSalaAtivaUsuario().get(this.usuario.getSalaAtivaUsuario().indexOf(new Sala(mensagem.getSala(),null))).getUsuarioList();
	        enviarParaTodos(mensagem,listaUsuarioMensagem);
	        listaUsuarioMensagem.remove(this.usuario);
	        enviarOnline(listaUsuarioMensagem,mensagem.getSala());
	        System.out.println("User " + mensagem.getNome() + " saiu da sala");

	    }
	   
	    private void desconectarTotal(Mensagem mensagem) { //olhar dps
	    	this.usuarioList.remove(usuario);
	        mensagem.setTexto("até logo!");
	        for(Sala sala : this.usuario.getSalaAtivaUsuario()){
					     mensagem.setTexto("até logo!");
					     mensagem.setSala(sala.getId());
	        			 sala.getUsuarioList().remove(new Usuario(mensagem.getNome()));
	        		     enviarOnline(sala.getUsuarioList(),sala.getId());
	        		     enviarParaTodos(sala.getUsuarioList(),sala.getId());
	        }
	        System.out.println("User " + mensagem.getNome() + " saiu da sala");
	        try {
	        	Thread.sleep(1000);
				usuario.getSocket().close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        
	    }
	    
        private void enviarPub(ObjectOutputStream output) {
            try {
            	CryptoMensagem mensagem = new CryptoMensagem();
            	mensagem.setPub(this.crypt.getPub());
                output.writeObject(mensagem);
                output.flush();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }
	        
        private void enviarMensagem(Mensagem mensagem, Usuario usuario) {
            try {
                usuario.getOut().writeObject(this.crypt.cryptMensagem(mensagem, usuario.getChaveDES())); // add
                usuario.getOut().flush();
            } catch (IOException e) {
            	e.printStackTrace();
            }
        }

        private void enviarParticular(Mensagem mensagem) {
            for (Usuario kv: usuarioList) {
                if (kv.equals(new Usuario(mensagem.getNameReserved()))) {
                	this.enviarMensagem(mensagem, kv);
                }
            }
        }
        
        private void enviarParaTodos(List<Usuario> usuarioList,int sala) {
        	Mensagem mensagem = new Mensagem();
        	mensagem.setNome(usuario.getNome().get());
            mensagem.setTipo(Tipo.TODOS);
            mensagem.setSala(sala);
            mensagem.setTexto("até logo!");
        	
        	for (Usuario kv: usuarioList) {
                if (!kv.equals(new Usuario(mensagem.getNome()))) {
                	this.enviarMensagem(mensagem, kv);
                }
            }
        }

        private void enviarParaTodos(Mensagem mensagem, List<Usuario> usuarioList) {
            for (Usuario kv: usuarioList) {
                if (!kv.equals(new Usuario(mensagem.getNome()))) {
                    mensagem.setTipo(Tipo.TODOS);
                    this.enviarMensagem(mensagem, kv);
                }
            }
        }
        
        private void enviarSalas() {
        	List<String> salas = new ArrayList<String>();
        	for(Sala sala : this.listaDeSala){
        		salas.add("Sala:"+sala.id);
        	}
        	Mensagem mensagem = new Mensagem();
        	mensagem.setTipo(Tipo.ATTSALAS);
        	mensagem.setSalasDisponiveis(salas);
        	
            for (Usuario kv: this.usuarioList) {
            	this.enviarMensagem(mensagem, kv);
              }

        }
        
        
        private void enviarOnline(List<Usuario> usuarioList,int sala) { // vai ter que um for superior que pecorre todas as salas
            List<String> usuarios = new ArrayList<String>();// a mensagem pra cada sala
            List<Estado> estados = new ArrayList<Estado>();
            for (Usuario kv: usuarioList) {
            	usuarios.add(kv.getNome().get());
            	estados.add(Estado.valueOf(kv.getStatus().get()));
            }
            //AVALIAR
            Mensagem mensagem = new Mensagem();
            mensagem.setTipo(Tipo.USUARIOSON);
            mensagem.setEstados(estados);
            mensagem.setUsuarios(usuarios);
            mensagem.setSala(sala);
            for (Usuario kv: usuarioList) {
            	mensagem.setNome(kv.getNome().get());
                this.enviarMensagem(mensagem, kv);
            }
        }

}
