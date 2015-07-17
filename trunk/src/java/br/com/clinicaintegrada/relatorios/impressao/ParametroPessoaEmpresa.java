package br.com.clinicaintegrada.relatorios.impressao;

import java.util.Date;

public class ParametroPessoaEmpresa extends ParametroPessoa {

    private String pessoa_empresa_nome;
    private String pessoa_empresa_documento;
    private String pessoa_empresa_endereco;
    private String pessoa_empresa_logradouro;
    private String pessoa_empresa_descricao_endereco;
    private String pessoa_empresa_numero;
    private String pessoa_empresa_complemento;
    private String pessoa_empresa_bairro;
    private String pessoa_empresa_cidade;
    private String pessoa_empresa_uf;
    private String pessoa_empresa_cep;

    public ParametroPessoaEmpresa() {
        super();
        this.pessoa_empresa_nome = "";
        this.pessoa_empresa_documento = "";
        this.pessoa_empresa_endereco = "";
        this.pessoa_empresa_logradouro = "";
        this.pessoa_empresa_descricao_endereco = "";
        this.pessoa_empresa_numero = "";
        this.pessoa_empresa_complemento = "";
        this.pessoa_empresa_bairro = "";
        this.pessoa_empresa_cidade = "";
        this.pessoa_empresa_uf = "";
        this.pessoa_empresa_cep = "";
    }

    public ParametroPessoaEmpresa(String pessoa_empresa_nome, String pessoa_empresa_documento, String pessoa_empresa_endereco, String pessoa_empresa_logradouro, String pessoa_empresa_descricao_endereco, String pessoa_empresa_numero, String pessoa_empresa_complemento, String pessoa_empresa_bairro, String pessoa_empresa_cidade, String pessoa_empresa_uf, String pessoa_empresa_cep, Integer pessoa_id, String pessoa_documento, String pessoa_nome, String pessoa_endereco_completo, String pessoa_logradouro, String pessoa_descricao_endereco, String pessoa_numero, String pessoa_complemento, String pessoa_bairro, String pessoa_cidade, String pessoa_uf, String pessoa_cep, String pessoa_telefone1, String pessoa_telefone2, String pessoa_email1, Date pessoa_cadastro) {
        super(pessoa_id, pessoa_documento, pessoa_nome, pessoa_endereco_completo, pessoa_logradouro, pessoa_descricao_endereco, pessoa_numero, pessoa_complemento, pessoa_bairro, pessoa_cidade, pessoa_uf, pessoa_cep, pessoa_telefone1, pessoa_telefone2, pessoa_email1, pessoa_cadastro);
        this.pessoa_empresa_nome = pessoa_empresa_nome;
        this.pessoa_empresa_documento = pessoa_empresa_documento;
        this.pessoa_empresa_endereco = pessoa_empresa_endereco;
        this.pessoa_empresa_logradouro = pessoa_empresa_logradouro;
        this.pessoa_empresa_descricao_endereco = pessoa_empresa_descricao_endereco;
        this.pessoa_empresa_numero = pessoa_empresa_numero;
        this.pessoa_empresa_complemento = pessoa_empresa_complemento;
        this.pessoa_empresa_bairro = pessoa_empresa_bairro;
        this.pessoa_empresa_cidade = pessoa_empresa_cidade;
        this.pessoa_empresa_uf = pessoa_empresa_uf;
        this.pessoa_empresa_cep = pessoa_empresa_cep;
    }

    public ParametroPessoaEmpresa(String pessoa_empresa_nome, String pessoa_empresa_documento, String pessoa_empresa_endereco, String pessoa_empresa_logradouro, String pessoa_empresa_descricao_endereco, String pessoa_empresa_numero, String pessoa_empresa_complemento, String pessoa_empresa_bairro, String pessoa_empresa_cidade, String pessoa_empresa_uf, String pessoa_empresa_cep, Integer pessoa_id, String pessoa_documento, String pessoa_nome, String pessoa_endereco_completo, String pessoa_logradouro, String pessoa_descricao_endereco, String pessoa_numero, String pessoa_complemento, String pessoa_bairro, String pessoa_cidade, String pessoa_uf, String pessoa_cep, String pessoa_telefone1, String pessoa_telefone2, String pessoa_email1, Date pessoa_cadastro, String pessoa_telefone3, String pessoa_endereco_completo_2, String pessoa_endereco_completo_3, String pessoa_endereco_completo_4, String pessoa_email2, String pessoa_email3) {
        super(pessoa_id, pessoa_documento, pessoa_nome, pessoa_endereco_completo, pessoa_logradouro, pessoa_descricao_endereco, pessoa_numero, pessoa_complemento, pessoa_bairro, pessoa_cidade, pessoa_uf, pessoa_cep, pessoa_telefone1, pessoa_telefone2, pessoa_email1, pessoa_cadastro, pessoa_telefone3, pessoa_endereco_completo_2, pessoa_endereco_completo_3, pessoa_endereco_completo_4, pessoa_email2, pessoa_email3);
        this.pessoa_empresa_nome = pessoa_empresa_nome;
        this.pessoa_empresa_documento = pessoa_empresa_documento;
        this.pessoa_empresa_endereco = pessoa_empresa_endereco;
        this.pessoa_empresa_logradouro = pessoa_empresa_logradouro;
        this.pessoa_empresa_descricao_endereco = pessoa_empresa_descricao_endereco;
        this.pessoa_empresa_numero = pessoa_empresa_numero;
        this.pessoa_empresa_complemento = pessoa_empresa_complemento;
        this.pessoa_empresa_bairro = pessoa_empresa_bairro;
        this.pessoa_empresa_cidade = pessoa_empresa_cidade;
        this.pessoa_empresa_uf = pessoa_empresa_uf;
        this.pessoa_empresa_cep = pessoa_empresa_cep;
    }

    public String getPessoa_empresa_nome() {
        return pessoa_empresa_nome;
    }

    public void setPessoa_empresa_nome(String pessoa_empresa_nome) {
        this.pessoa_empresa_nome = pessoa_empresa_nome;
    }

    public String getPessoa_empresa_documento() {
        return pessoa_empresa_documento;
    }

    public void setPessoa_empresa_documento(String pessoa_empresa_documento) {
        this.pessoa_empresa_documento = pessoa_empresa_documento;
    }

    public String getPessoa_empresa_endereco() {
        return pessoa_empresa_endereco;
    }

    public void setPessoa_empresa_endereco(String pessoa_empresa_endereco) {
        this.pessoa_empresa_endereco = pessoa_empresa_endereco;
    }

    public String getPessoa_empresa_logradouro() {
        return pessoa_empresa_logradouro;
    }

    public void setPessoa_empresa_logradouro(String pessoa_empresa_logradouro) {
        this.pessoa_empresa_logradouro = pessoa_empresa_logradouro;
    }

    public String getPessoa_empresa_descricao_endereco() {
        return pessoa_empresa_descricao_endereco;
    }

    public void setPessoa_empresa_descricao_endereco(String pessoa_empresa_descricao_endereco) {
        this.pessoa_empresa_descricao_endereco = pessoa_empresa_descricao_endereco;
    }

    public String getPessoa_empresa_numero() {
        return pessoa_empresa_numero;
    }

    public void setPessoa_empresa_numero(String pessoa_empresa_numero) {
        this.pessoa_empresa_numero = pessoa_empresa_numero;
    }

    public String getPessoa_empresa_complemento() {
        return pessoa_empresa_complemento;
    }

    public void setPessoa_empresa_complemento(String pessoa_empresa_complemento) {
        this.pessoa_empresa_complemento = pessoa_empresa_complemento;
    }

    public String getPessoa_empresa_bairro() {
        return pessoa_empresa_bairro;
    }

    public void setPessoa_empresa_bairro(String pessoa_empresa_bairro) {
        this.pessoa_empresa_bairro = pessoa_empresa_bairro;
    }

    public String getPessoa_empresa_cidade() {
        return pessoa_empresa_cidade;
    }

    public void setPessoa_empresa_cidade(String pessoa_empresa_cidade) {
        this.pessoa_empresa_cidade = pessoa_empresa_cidade;
    }

    public String getPessoa_empresa_uf() {
        return pessoa_empresa_uf;
    }

    public void setPessoa_empresa_uf(String pessoa_empresa_uf) {
        this.pessoa_empresa_uf = pessoa_empresa_uf;
    }

    public String getPessoa_empresa_cep() {
        return pessoa_empresa_cep;
    }

    public void setPessoa_empresa_cep(String pessoa_empresa_cep) {
        this.pessoa_empresa_cep = pessoa_empresa_cep;
    }

}
