package com.axiastudio.suite.interoperabilita;

/**
 * User: tiziano
 * Date: 05/02/14
 * Time: 11:04
 */
public class Documento {

    String nome;
    String tipoRiferimento = "MIME"; // per ora gestiamo solo mime
    String oggetto;

    public Documento() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoRiferimento() {
        return tipoRiferimento;
    }

    public void setTipoRiferimento(String tipoRiferimento) {
        this.tipoRiferimento = tipoRiferimento;
    }

    public String getOggetto() {
        return oggetto;
    }

    public void setOggetto(String oggetto) {
        this.oggetto = oggetto;
    }
}
