package Package3; .Package3;

public class Studente {
    private String nome;
    private String cognome;

    public Studente() {
    }

    public void ciao() {
        System.out.println("Ciao");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
}