package br.com.clinicaintegrada.coordenacao.list;

import br.com.clinicaintegrada.coordenacao.PertenceEntrada;
import br.com.clinicaintegrada.coordenacao.PertenceSaida;

public class ListPertence {

    private PertenceEntrada pertenceEntrada;
    private PertenceSaida pertenceSaida;
    private Integer saida;
    private Integer saldo;

    public ListPertence() {
        this.pertenceEntrada = new PertenceEntrada();
        this.pertenceSaida = new PertenceSaida();
        this.saldo = 0;
        this.saida = 0;
    }

    public ListPertence(PertenceEntrada pertenceEntrada, PertenceSaida pertenceSaida, Integer saida, Integer saldo) {
        this.pertenceEntrada = pertenceEntrada;
        this.pertenceSaida = pertenceSaida;
        this.saida = saida;
        this.saldo = saldo;
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

    public Integer getSaida() {
        return saida;
    }

    public void setSaida(Integer saida) {
        this.saida = saida;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

}
