package br.com.rtools.pessoa.list;

import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.PessoaEmpresa;

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
