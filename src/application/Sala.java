package application;

import java.util.ArrayList;
import java.util.List;

public class Sala {
	int id;
	Usuario administrador;
	List<Usuario> usuarioList ;
	
	public Sala(int id, Usuario administrador) {
		super();
		this.id = id;
		this.administrador = administrador;
		this.usuarioList = new ArrayList<Usuario>() ;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Usuario getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Usuario administrador) {
		this.administrador = administrador;
	}

	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}
	
	@Override
	public boolean equals(Object arg0) {
		return (id == ((Sala) arg0).getId());
	}
}
