package br.com.clinicaintegrada.relatorios.impressao;

import java.util.Date;

public class ParametroPessoa {

    private Integer pessoa_id;
    private Object pessoa_documento;
    private Object pessoa_nome;
    private Object pessoa_endereco_completo;
    private Object pessoa_logradouro;
    private Object pessoa_descricao_endereco;
    private Object pessoa_numero;
    private Object pessoa_complemento;
    private Object pessoa_bairro;
    private Object pessoa_cidade;
    private Object pessoa_uf;
    private Object pessoa_cep;
    private Object pessoa_telefone1;
    private Object pessoa_telefone2; // CELULAR 
    private Object pessoa_email1;
    private Object pessoa_cadastro;

    // EXTRAS
    private Object pessoa_telefone3;
    private Object pessoa_endereco_completo_2;
    private Object pessoa_endereco_completo_3;
    private Object pessoa_endereco_completo_4;
    private Object pessoa_email2;
    private Object pessoa_email3;

    public ParametroPessoa() {
        this.pessoa_id = null;
        this.pessoa_documento = "";
        this.pessoa_nome = "";
        this.pessoa_endereco_completo = "";
        this.pessoa_logradouro = "";
        this.pessoa_descricao_endereco = "";
        this.pessoa_numero = "";
        this.pessoa_complemento = "";
        this.pessoa_bairro = "";
        this.pessoa_cidade = "";
        this.pessoa_uf = "";
        this.pessoa_cep = "";
        this.pessoa_telefone1 = "";
        this.pessoa_telefone2 = "";
        this.pessoa_email1 = "";
        this.pessoa_cadastro = null;
        this.pessoa_telefone3 = "";
        this.pessoa_endereco_completo_2 = "";
        this.pessoa_endereco_completo_3 = "";
        this.pessoa_endereco_completo_4 = "";
        this.pessoa_email2 = "";
        this.pessoa_email3 = "";
    }

    public ParametroPessoa(Integer pessoa_id, Object pessoa_documento, Object pessoa_nome, Object pessoa_endereco_completo, Object pessoa_logradouro, Object pessoa_descricao_endereco, Object pessoa_numero, Object pessoa_complemento, Object pessoa_bairro, Object pessoa_cidade, Object pessoa_uf, Object pessoa_cep, Object pessoa_telefone1, Object pessoa_telefone2, Object pessoa_email1, Object pessoa_cadastro) {
        this.pessoa_id = pessoa_id;
        this.pessoa_documento = pessoa_documento;
        this.pessoa_nome = pessoa_nome;
        this.pessoa_endereco_completo = pessoa_endereco_completo;
        this.pessoa_logradouro = pessoa_logradouro;
        this.pessoa_descricao_endereco = pessoa_descricao_endereco;
        this.pessoa_numero = pessoa_numero;
        this.pessoa_complemento = pessoa_complemento;
        this.pessoa_bairro = pessoa_bairro;
        this.pessoa_cidade = pessoa_cidade;
        this.pessoa_uf = pessoa_uf;
        this.pessoa_cep = pessoa_cep;
        this.pessoa_telefone1 = pessoa_telefone1;
        this.pessoa_telefone2 = pessoa_telefone2;
        this.pessoa_email1 = pessoa_email1;
        this.pessoa_cadastro = pessoa_cadastro;
    }

    // EXTRAS
    public ParametroPessoa(Integer pessoa_id, Object pessoa_documento, Object pessoa_nome, Object pessoa_endereco_completo, Object pessoa_logradouro, Object pessoa_descricao_endereco, Object pessoa_numero, Object pessoa_complemento, Object pessoa_bairro, Object pessoa_cidade, Object pessoa_uf, Object pessoa_cep, Object pessoa_telefone1, Object pessoa_telefone2, Object pessoa_email1, Date pessoa_cadastro, Object pessoa_telefone3, Object pessoa_endereco_completo_2, Object pessoa_endereco_completo_3, Object pessoa_endereco_completo_4, Object pessoa_email2, Object pessoa_email3) {
        this.pessoa_id = pessoa_id;
        this.pessoa_documento = pessoa_documento;
        this.pessoa_nome = pessoa_nome;
        this.pessoa_endereco_completo = pessoa_endereco_completo;
        this.pessoa_logradouro = pessoa_logradouro;
        this.pessoa_descricao_endereco = pessoa_descricao_endereco;
        this.pessoa_numero = pessoa_numero;
        this.pessoa_complemento = pessoa_complemento;
        this.pessoa_bairro = pessoa_bairro;
        this.pessoa_cidade = pessoa_cidade;
        this.pessoa_uf = pessoa_uf;
        this.pessoa_cep = pessoa_cep;
        this.pessoa_telefone1 = pessoa_telefone1;
        this.pessoa_telefone2 = pessoa_telefone2;
        this.pessoa_email1 = pessoa_email1;
        this.pessoa_cadastro = pessoa_cadastro;
        this.pessoa_telefone3 = pessoa_telefone3;
        this.pessoa_endereco_completo_2 = pessoa_endereco_completo_2;
        this.pessoa_endereco_completo_3 = pessoa_endereco_completo_3;
        this.pessoa_endereco_completo_4 = pessoa_endereco_completo_4;
        this.pessoa_email2 = pessoa_email2;
        this.pessoa_email3 = pessoa_email3;
    }

    public Integer getPessoa_id() {
        return pessoa_id;
    }

    public void setPessoa_id(Integer pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public Object getPessoa_documento() {
        return pessoa_documento;
    }

    public void setPessoa_documento(Object pessoa_documento) {
        this.pessoa_documento = pessoa_documento;
    }

    public Object getPessoa_nome() {
        return pessoa_nome;
    }

    public void setPessoa_nome(Object pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }

    public Object getPessoa_endereco_completo() {
        return pessoa_endereco_completo;
    }

    public void setPessoa_endereco_completo(Object pessoa_endereco_completo) {
        this.pessoa_endereco_completo = pessoa_endereco_completo;
    }

    public Object getPessoa_logradouro() {
        return pessoa_logradouro;
    }

    public void setPessoa_logradouro(Object pessoa_logradouro) {
        this.pessoa_logradouro = pessoa_logradouro;
    }

    public Object getPessoa_descricao_endereco() {
        return pessoa_descricao_endereco;
    }

    public void setPessoa_descricao_endereco(Object pessoa_descricao_endereco) {
        this.pessoa_descricao_endereco = pessoa_descricao_endereco;
    }

    public Object getPessoa_numero() {
        return pessoa_numero;
    }

    public void setPessoa_numero(Object pessoa_numero) {
        this.pessoa_numero = pessoa_numero;
    }

    public Object getPessoa_bairro() {
        return pessoa_bairro;
    }

    public void setPessoa_bairro(Object pessoa_bairro) {
        this.pessoa_bairro = pessoa_bairro;
    }

    public Object getPessoa_cidade() {
        return pessoa_cidade;
    }

    public void setPessoa_cidade(Object pessoa_cidade) {
        this.pessoa_cidade = pessoa_cidade;
    }

    public Object getPessoa_uf() {
        return pessoa_uf;
    }

    public void setPessoa_uf(Object pessoa_uf) {
        this.pessoa_uf = pessoa_uf;
    }

    public Object getPessoa_cep() {
        return pessoa_cep;
    }

    public void setPessoa_cep(Object pessoa_cep) {
        this.pessoa_cep = pessoa_cep;
    }

    public Object getPessoa_telefone1() {
        return pessoa_telefone1;
    }

    public void setPessoa_telefone1(Object pessoa_telefone1) {
        this.pessoa_telefone1 = pessoa_telefone1;
    }

    public Object getPessoa_telefone2() {
        return pessoa_telefone2;
    }

    public void setPessoa_telefone2(Object pessoa_telefone2) {
        this.pessoa_telefone2 = pessoa_telefone2;
    }

    public Object getPessoa_email1() {
        return pessoa_email1;
    }

    public void setPessoa_email1(Object pessoa_email1) {
        this.pessoa_email1 = pessoa_email1;
    }

    public Object getPessoa_cadastro() {
        return pessoa_cadastro;
    }

    public void setPessoa_cadastro(Object pessoa_cadastro) {
        this.pessoa_cadastro = pessoa_cadastro;
    }

    public Object getPessoa_telefone3() {
        return pessoa_telefone3;
    }

    public void setPessoa_telefone3(Object pessoa_telefone3) {
        this.pessoa_telefone3 = pessoa_telefone3;
    }

    public Object getPessoa_endereco_completo_2() {
        return pessoa_endereco_completo_2;
    }

    public void setPessoa_endereco_completo_2(Object pessoa_endereco_completo_2) {
        this.pessoa_endereco_completo_2 = pessoa_endereco_completo_2;
    }

    public Object getPessoa_endereco_completo_3() {
        return pessoa_endereco_completo_3;
    }

    public void setPessoa_endereco_completo_3(Object pessoa_endereco_completo_3) {
        this.pessoa_endereco_completo_3 = pessoa_endereco_completo_3;
    }

    public Object getPessoa_endereco_completo_4() {
        return pessoa_endereco_completo_4;
    }

    public void setPessoa_endereco_completo_4(Object pessoa_endereco_completo_4) {
        this.pessoa_endereco_completo_4 = pessoa_endereco_completo_4;
    }

    public Object getPessoa_email2() {
        return pessoa_email2;
    }

    public void setPessoa_email2(Object pessoa_email2) {
        this.pessoa_email2 = pessoa_email2;
    }

    public Object getPessoa_email3() {
        return pessoa_email3;
    }

    public void setPessoa_email3(Object pessoa_email3) {
        this.pessoa_email3 = pessoa_email3;
    }

    public Object getPessoa_complemento() {
        return pessoa_complemento;
    }

    public void setPessoa_complemento(Object pessoa_complemento) {
        this.pessoa_complemento = pessoa_complemento;
    }

}
