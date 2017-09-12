package application;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mensagem implements Serializable {
    
    private String nome; // talvez so login
    private String texto;
    private String Login;
    //private Set<String> totalOnlines = new HashSet<String>();
    List<String> usuarios = new ArrayList<String>();
    List<Estado> estados = new ArrayList<Estado>();
    
    private Tipo tipo;
    private Estado estado;

    public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String text) {
        this.texto = text;
    }

    public String getNameReserved() {
        return Login;
    }

    public void setNameReserved(String nameReserved) {
        this.Login = nameReserved;
    }


    public Tipo  getTipo() {
        return tipo;
    }

    public void setTipo(Tipo  action) {
        this.tipo = action;
    }
        

	public List<String> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<String> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public enum Tipo {
    	ABRIRCONEXAO, DESCONECTAR, INDIVIDUAL, TODOS, USUARIOSON,ALTERARESTADO,KICK,REMOVIDO
    }
    
    public enum Estado {
    	AUSENTE, OCUPADO, DISPONIVEL
    }
}
