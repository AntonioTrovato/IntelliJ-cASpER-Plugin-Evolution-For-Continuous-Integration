package caspertrial;

public class Studente {
    public Studente(String nome, String maricola, Professore p) {
        this.nome = nome;
        this.maricola = maricola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMaricola() {
        return maricola;
    }

    public void setMaricola(String maricola) {
        this.maricola = maricola;
    }

    private String nome;
    private String maricola;
}
