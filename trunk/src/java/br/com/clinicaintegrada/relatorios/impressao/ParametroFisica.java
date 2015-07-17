package br.com.clinicaintegrada.relatorios.impressao;

import java.util.Date;

public class ParametroFisica extends ParametroPessoa {

    private Object fisica_rg;
    private Object fisica_nascimento;
    private Object fisica_sexo;
    private Integer fisica_idade;

    public ParametroFisica() {
        super();
        this.fisica_rg = "";
        this.fisica_nascimento = null;
        this.fisica_sexo = "";
        this.fisica_idade = 0;
    }

    public ParametroFisica(Object fisica_rg, Object fisica_nascimento, Object fisica_sexo, Integer fisica_idade, Integer pessoa_id, String pessoa_documento, String pessoa_nome, String pessoa_endereco_completo, String pessoa_logradouro, String pessoa_descricao_endereco, String pessoa_numero, String pessoa_complemento, String pessoa_bairro, String pessoa_cidade, String pessoa_uf, String pessoa_cep, String pessoa_telefone1, String pessoa_telefone2, String pessoa_email1, Date pessoa_cadastro) {
        super(pessoa_id, pessoa_documento, pessoa_nome, pessoa_endereco_completo, pessoa_logradouro, pessoa_descricao_endereco, pessoa_numero, pessoa_complemento, pessoa_bairro, pessoa_cidade, pessoa_uf, pessoa_cep, pessoa_telefone1, pessoa_telefone2, pessoa_email1, pessoa_cadastro);
        this.fisica_rg = fisica_rg;
        this.fisica_nascimento = fisica_nascimento;
        this.fisica_sexo = fisica_sexo;
        this.fisica_idade = fisica_idade;
    }

    public ParametroFisica(String fisica_rg, Object fisica_nascimento, Object fisica_sexo, Integer fisica_idade, Integer pessoa_id, String pessoa_documento, String pessoa_nome, String pessoa_endereco_completo, String pessoa_logradouro, String pessoa_descricao_endereco, String pessoa_numero, String pessoa_complemento, String pessoa_bairro, String pessoa_cidade, String pessoa_uf, String pessoa_cep, String pessoa_telefone1, String pessoa_telefone2, String pessoa_email1, Date pessoa_cadastro, String pessoa_telefone3, String pessoa_endereco_completo_2, String pessoa_endereco_completo_3, String pessoa_endereco_completo_4, String pessoa_email2, String pessoa_email3) {
        super(pessoa_id, pessoa_documento, pessoa_nome, pessoa_endereco_completo, pessoa_logradouro, pessoa_descricao_endereco, pessoa_numero, pessoa_complemento, pessoa_bairro, pessoa_cidade, pessoa_uf, pessoa_cep, pessoa_telefone1, pessoa_telefone2, pessoa_email1, pessoa_cadastro, pessoa_telefone3, pessoa_endereco_completo_2, pessoa_endereco_completo_3, pessoa_endereco_completo_4, pessoa_email2, pessoa_email3);
        this.fisica_rg = fisica_rg;
        this.fisica_nascimento = fisica_nascimento;
        this.fisica_sexo = fisica_sexo;
        this.fisica_idade = fisica_idade;
    }

    public Object getFisica_rg() {
        return fisica_rg;
    }

    public void setFisica_rg(Object fisica_rg) {
        this.fisica_rg = fisica_rg;
    }

    public Object getFisica_nascimento() {
        return fisica_nascimento;
    }

    public void setFisica_nascimento(Object fisica_nascimento) {
        this.fisica_nascimento = fisica_nascimento;
    }

    public Object getFisica_sexo() {
        return fisica_sexo;
    }

    public void setFisica_sexo(Object fisica_sexo) {
        this.fisica_sexo = fisica_sexo;
    }

    public Integer getFisica_idade() {
        return fisica_idade;
    }

    public void setFisica_idade(Integer fisica_idade) {
        this.fisica_idade = fisica_idade;
    }

}
