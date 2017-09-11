package application;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Mensagem implements Serializable {
    
    private String nome; // talvez so login
    private String texto;
    private String Login;
    private Set<String> totalOnlines = new HashSet<String>();
    private Tipo tipo;

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

    public Set<String> getTotalOnlines() {
        return totalOnlines;
    }

    public void setTotalOnline(Set<String> setOnlines) {
        this.totalOnlines = setOnlines;
    }

    public Tipo  getTipo() {
        return tipo;
    }

    public void setTipo(Tipo  action) {
        this.tipo = action;
    }
        
    public enum Tipo {
    	ABRIRCONEXAO, DESCONECTAR, INDIVIDUAL, TODOS, USUARIOSON
    }
}
