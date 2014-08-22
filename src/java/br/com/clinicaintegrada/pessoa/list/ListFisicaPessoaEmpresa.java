package br.com.clinicaintegrada.pessoa.list;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.PessoaEmpresa;

public class ListFisicaPessoaEmpresa {

    private Fisica fisica;
    private PessoaEmpresa pessoaEmpresa;

    public ListFisicaPessoaEmpresa(Fisica fisica, PessoaEmpresa pessoaEmpresa) {
        this.fisica = fisica;
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

}
