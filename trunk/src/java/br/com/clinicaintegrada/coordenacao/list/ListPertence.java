package br.com.clinicaintegrada.coordenacao.list;

import br.com.clinicaintegrada.coordenacao.PertenceEntrada;
import br.com.clinicaintegrada.coordenacao.PertenceSaida;

public class ListPertence {

    private PertenceEntrada pertenceEntrada;
    private PertenceSaida pertenceSaida;

    public ListPertence() {
        this.pertenceEntrada = new PertenceEntrada();
        this.pertenceSaida = new PertenceSaida();
    }

    public ListPertence(PertenceEntrada pertenceEntrada, PertenceSaida pertenceSaida) {
        this.pertenceEntrada = pertenceEntrada;
        this.pertenceSaida = pertenceSaida;
    }

    public PertenceEntrada getPertenceEntrada() {
        return pertenceEntrada;
    }

    public void setPertenceEntrada(PertenceEntrada pertenceEntrada) {
        this.pertenceEntrada = pertenceEntrada;
    }

    public PertenceSaida getPertenceSaida() {
        return pertenceSaida;
    }

    public void setPertenceSaida(PertenceSaida pertenceSaida) {
        this.pertenceSaida = pertenceSaida;
    }

}
