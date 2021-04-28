package caspertrial;

public class Professore {
    public Professore(String materia, String aula) {
        this.materia = materia;
        this.aula = aula;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    private String materia;
    private String aula;
}
