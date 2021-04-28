package caspertrial;

public class Studente {
    public Studente(String matricola, String dipartimento) {
        this.matricola = matricola;
        this.dipartimento = dipartimento;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getDipartimento() {
        return dipartimento;
    }

    public void setDipartimento(String dipartimento) {
        this.dipartimento = dipartimento;
    }

    public String getMateria() {
        return this.professore.getMateria();
    }

    public void setMateria(String materia) {
        this.professore.setMateria(materia);
    }

    public String getAula() {
        return this.professore.getAula();
    }

    public void setAula(String aula) {
        this.professore.setAula(aula);
    }

    private String matricola;
    private String dipartimento;
    private Professore professore;
}
