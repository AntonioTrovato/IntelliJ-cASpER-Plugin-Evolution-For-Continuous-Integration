package caspertrial;

import java.lang.System;

public class Studente extends Persona{
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

    private String nome;
    private String cognome;
}