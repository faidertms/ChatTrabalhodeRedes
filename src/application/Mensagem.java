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
    List<String> salasDisponiveis = new ArrayList<String>();
    private int sala;
    private Tipo tipo;

	private boolean motivo; // usado apenas para o maldito desconectar pode ser bool
    private Estado estado;

    public List<String> getSalasDisponiveis() {
		return salasDisponiveis;
	}

	public void setSalasDisponiveis(List<String> salasDisponiveis) {
		this.salasDisponiveis = salasDisponiveis;
	}
    
    
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

	public int getSala() {
		return sala;
	}

	public void setSala(int sala) {
		this.sala = sala;
	}

	public boolean isMotivo() {
		return motivo;
	}

	public void setMotivo(boolean motivo) {
		this.motivo = motivo;
	}

	public enum Tipo {//kick = admin removido = servidor usar no metodo remover
    	ABRIRCONEXAO,ATTSALAS,DESCONECTAR ,DESCONECTARTOTAL, INDIVIDUAL, TODOS, USUARIOSON,ALTERARESTADO,KICK,REMOVIDO,CRIARSALA,ENTRARSALA
    }
    
    public enum Estado {
    	AUSENTE, OCUPADO, DISPONIVEL
    }
}
