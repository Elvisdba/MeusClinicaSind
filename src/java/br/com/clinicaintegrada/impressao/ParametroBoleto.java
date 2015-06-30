package br.com.clinicaintegrada.impressao;

import java.math.BigDecimal;

public class ParametroBoleto {

    // CEDENTE
    private String cedente_nome;
    private String cedente_documento;
    private String cedente_codigo;
    private String cedente_endereco;
    private String cedente_numero;
    private String cedente_complemento;
    private String cedente_bairro;
    private String cedente_cidade;
    private String cedente_uf;
    private String cedente_cep;
    private String cedente_logo;
    private String cedente_site;
    private String cedente_email1;
    private String cedente_telefone;

    // SACADO
    private Integer sacado_codigo;
    private String sacado_nome;
    private String sacado_documento;
    private String sacado_logradouro;
    private String sacado_endereco;
    private String sacado_numero;
    private String sacado_complemento;
    private String sacado_bairro;
    private String sacado_cidade;
    private String sacado_uf;
    private String sacado_cep;

    // BANCO
    private String banco_codigo;
    private String banco_logo;
    private String banco_uso;
    private String banco_nosso_numero;
    private String banco_codigo_agencia;

    // HEADER
    private String servico;
    private String tipo;

    // BOLETO - LAYOUT BB
    private String referencia;
    private BigDecimal valor;
    private BigDecimal valor_total;
    private String representacao_numerica;
    private String vencimento;
    private String data_documento;
    private String moeda;
    private String especie;
    private String especie_documento;
    private String aceite;
    private String carteira;
    private String exercicio;
    private String boleto;
    private String mensagem;
    private String mensagem_boleto;
    private String serrilha;
    private String texto_titulo;
    private String caminho_verso;
    private String local_pagamento;
    private String descricao_servico;

    // EXTRAS
    private Boolean imprime_verso;
    private String layout;
    private String codigo_barras;
    private Integer contrato_numero;

    public ParametroBoleto(String cedente_nome, String cedente_documento, String cedente_codigo, String cedente_endereco, String cedente_numero, String cedente_complemento, String cedente_bairro, String cedente_cidade, String cedente_uf, String cedente_cep, String cedente_logo, String cedente_site, String cedente_email1, String cedente_telefone, Integer sacado_codigo, String sacado_nome, String sacado_documento, String sacado_logradouro, String sacado_endereco, String sacado_numero, String sacado_complemento, String sacado_bairro, String sacado_cidade, String sacado_uf, String sacado_cep, String banco_codigo, String banco_logo, String banco_uso, String banco_nosso_numero, String banco_codigo_agencia, String servico, String tipo, String referencia, BigDecimal valor, BigDecimal valor_total, String representacao_numerica, String vencimento, String data_documento, String moeda, String especie, String especie_documento, String aceite, String carteira, String exercicio, String boleto, String mensagem, String mensagem_boleto, String serrilha, String texto_titulo, String caminho_verso, String local_pagamento, String descricao_servico, Boolean imprime_verso, String layout, String codigo_barras, Integer contrato_numero) {
        this.cedente_nome = cedente_nome;
        this.cedente_documento = cedente_documento;
        this.cedente_codigo = cedente_codigo;
        this.cedente_endereco = cedente_endereco;
        this.cedente_numero = cedente_numero;
        this.cedente_complemento = cedente_complemento;
        this.cedente_bairro = cedente_bairro;
        this.cedente_cidade = cedente_cidade;
        this.cedente_uf = cedente_uf;
        this.cedente_cep = cedente_cep;
        this.cedente_logo = cedente_logo;
        this.cedente_site = cedente_site;
        this.cedente_email1 = cedente_email1;
        this.cedente_telefone = cedente_telefone;
        this.sacado_codigo = sacado_codigo;
        this.sacado_nome = sacado_nome;
        this.sacado_documento = sacado_documento;
        this.sacado_logradouro = sacado_logradouro;
        this.sacado_endereco = sacado_endereco;
        this.sacado_numero = sacado_numero;
        this.sacado_complemento = sacado_complemento;
        this.sacado_bairro = sacado_bairro;
        this.sacado_cidade = sacado_cidade;
        this.sacado_uf = sacado_uf;
        this.sacado_cep = sacado_cep;
        this.banco_codigo = banco_codigo;
        this.banco_logo = banco_logo;
        this.banco_uso = banco_uso;
        this.banco_nosso_numero = banco_nosso_numero;
        this.banco_codigo_agencia = banco_codigo_agencia;
        this.servico = servico;
        this.tipo = tipo;
        this.referencia = referencia;
        this.valor = valor;
        this.valor_total = valor_total;
        this.representacao_numerica = representacao_numerica;
        this.vencimento = vencimento;
        this.data_documento = data_documento;
        this.moeda = moeda;
        this.especie = especie;
        this.especie_documento = especie_documento;
        this.aceite = aceite;
        this.carteira = carteira;
        this.exercicio = exercicio;
        this.boleto = boleto;
        this.mensagem = mensagem;
        this.mensagem_boleto = mensagem_boleto;
        this.serrilha = serrilha;
        this.texto_titulo = texto_titulo;
        this.caminho_verso = caminho_verso;
        this.local_pagamento = local_pagamento;
        this.descricao_servico = descricao_servico;
        this.imprime_verso = imprime_verso;
        this.layout = layout;
        this.codigo_barras = codigo_barras;
        this.contrato_numero = contrato_numero;
    }

    public String getCedente_nome() {
        return cedente_nome;
    }

    public void setCedente_nome(String cedente_nome) {
        this.cedente_nome = cedente_nome;
    }

    public String getCedente_documento() {
        return cedente_documento;
    }

    public void setCedente_documento(String cedente_documento) {
        this.cedente_documento = cedente_documento;
    }

    public String getCedente_codigo() {
        return cedente_codigo;
    }

    public void setCedente_codigo(String cedente_codigo) {
        this.cedente_codigo = cedente_codigo;
    }

    public String getCedente_endereco() {
        return cedente_endereco;
    }

    public void setCedente_endereco(String cedente_endereco) {
        this.cedente_endereco = cedente_endereco;
    }

    public String getCedente_numero() {
        return cedente_numero;
    }

    public void setCedente_numero(String cedente_numero) {
        this.cedente_numero = cedente_numero;
    }

    public String getCedente_complemento() {
        return cedente_complemento;
    }

    public void setCedente_complemento(String cedente_complemento) {
        this.cedente_complemento = cedente_complemento;
    }

    public String getCedente_bairro() {
        return cedente_bairro;
    }

    public void setCedente_bairro(String cedente_bairro) {
        this.cedente_bairro = cedente_bairro;
    }

    public String getCedente_cidade() {
        return cedente_cidade;
    }

    public void setCedente_cidade(String cedente_cidade) {
        this.cedente_cidade = cedente_cidade;
    }

    public String getCedente_uf() {
        return cedente_uf;
    }

    public void setCedente_uf(String cedente_uf) {
        this.cedente_uf = cedente_uf;
    }

    public String getCedente_cep() {
        return cedente_cep;
    }

    public void setCedente_cep(String cedente_cep) {
        this.cedente_cep = cedente_cep;
    }

    public String getCedente_logo() {
        return cedente_logo;
    }

    public void setCedente_logo(String cedente_logo) {
        this.cedente_logo = cedente_logo;
    }

    public String getSacado_nome() {
        return sacado_nome;
    }

    public void setSacado_nome(String sacado_nome) {
        this.sacado_nome = sacado_nome;
    }

    public String getSacado_documento() {
        return sacado_documento;
    }

    public void setSacado_documento(String sacado_documento) {
        this.sacado_documento = sacado_documento;
    }

    public String getSacado_endereco() {
        return sacado_endereco;
    }

    public void setSacado_endereco(String sacado_endereco) {
        this.sacado_endereco = sacado_endereco;
    }

    public String getSacado_numero() {
        return sacado_numero;
    }

    public void setSacado_numero(String sacado_numero) {
        this.sacado_numero = sacado_numero;
    }

    public String getSacado_complemento() {
        return sacado_complemento;
    }

    public void setSacado_complemento(String sacado_complemento) {
        this.sacado_complemento = sacado_complemento;
    }

    public String getSacado_bairro() {
        return sacado_bairro;
    }

    public void setSacado_bairro(String sacado_bairro) {
        this.sacado_bairro = sacado_bairro;
    }

    public String getSacado_cidade() {
        return sacado_cidade;
    }

    public void setSacado_cidade(String sacado_cidade) {
        this.sacado_cidade = sacado_cidade;
    }

    public String getSacado_uf() {
        return sacado_uf;
    }

    public void setSacado_uf(String sacado_uf) {
        this.sacado_uf = sacado_uf;
    }

    public String getSacado_cep() {
        return sacado_cep;
    }

    public void setSacado_cep(String sacado_cep) {
        this.sacado_cep = sacado_cep;
    }

    public String getBanco_codigo() {
        return banco_codigo;
    }

    public void setBanco_codigo(String banco_codigo) {
        this.banco_codigo = banco_codigo;
    }

    public String getBanco_logo() {
        return banco_logo;
    }

    public void setBanco_logo(String banco_logo) {
        this.banco_logo = banco_logo;
    }

    public String getBanco_uso() {
        return banco_uso;
    }

    public void setBanco_uso(String banco_uso) {
        this.banco_uso = banco_uso;
    }

    public String getBanco_nosso_numero() {
        return banco_nosso_numero;
    }

    public void setBanco_nosso_numero(String banco_nosso_numero) {
        this.banco_nosso_numero = banco_nosso_numero;
    }

    public String getBanco_codigo_agencia() {
        return banco_codigo_agencia;
    }

    public void setBanco_codigo_agencia(String banco_codigo_agencia) {
        this.banco_codigo_agencia = banco_codigo_agencia;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getRepresentacao_numerica() {
        return representacao_numerica;
    }

    public void setRepresentacao_numerica(String representacao_numerica) {
        this.representacao_numerica = representacao_numerica;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getData_documento() {
        return data_documento;
    }

    public void setData_documento(String data_documento) {
        this.data_documento = data_documento;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getEspecie_documento() {
        return especie_documento;
    }

    public void setEspecie_documento(String especie_documento) {
        this.especie_documento = especie_documento;
    }

    public String getAceite() {
        return aceite;
    }

    public void setAceite(String aceite) {
        this.aceite = aceite;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getExercicio() {
        return exercicio;
    }

    public void setExercicio(String exercicio) {
        this.exercicio = exercicio;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem_boleto() {
        return mensagem_boleto;
    }

    public void setMensagem_boleto(String mensagem_boleto) {
        this.mensagem_boleto = mensagem_boleto;
    }

    public String getSerrilha() {
        return serrilha;
    }

    public void setSerrilha(String serrilha) {
        this.serrilha = serrilha;
    }

    public String getTexto_titulo() {
        return texto_titulo;
    }

    public void setTexto_titulo(String texto_titulo) {
        this.texto_titulo = texto_titulo;
    }

    public String getCaminho_verso() {
        return caminho_verso;
    }

    public void setCaminho_verso(String caminho_verso) {
        this.caminho_verso = caminho_verso;
    }

    public String getLocal_pagamento() {
        return local_pagamento;
    }

    public void setLocal_pagamento(String local_pagamento) {
        this.local_pagamento = local_pagamento;
    }

    public String getDescricao_servico() {
        return descricao_servico;
    }

    public void setDescricao_servico(String descricao_servico) {
        this.descricao_servico = descricao_servico;
    }

    public Boolean getImprime_verso() {
        return imprime_verso;
    }

    public void setImprime_verso(Boolean imprime_verso) {
        this.imprime_verso = imprime_verso;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public Integer getContrato_numero() {
        return contrato_numero;
    }

    public void setContrato_numero(Integer contrato_numero) {
        this.contrato_numero = contrato_numero;
    }

    public String getCedente_site() {
        return cedente_site;
    }

    public void setCedente_site(String cedente_site) {
        this.cedente_site = cedente_site;
    }

    public String getCedente_email1() {
        return cedente_email1;
    }

    public void setCedente_email1(String cedente_email1) {
        this.cedente_email1 = cedente_email1;
    }

    public String getCedente_telefone() {
        return cedente_telefone;
    }

    public void setCedente_telefone(String cedente_telefone) {
        this.cedente_telefone = cedente_telefone;
    }

    public BigDecimal getValor_total() {
        return valor_total;
    }

    public void setValor_total(BigDecimal valor_total) {
        this.valor_total = valor_total;
    }

    public String getSacado_logradouro() {
        return sacado_logradouro;
    }

    public void setSacado_logradouro(String sacado_logradouro) {
        this.sacado_logradouro = sacado_logradouro;
    }

    public Integer getSacado_codigo() {
        return sacado_codigo;
    }

    public void setSacado_codigo(Integer sacado_codigo) {
        this.sacado_codigo = sacado_codigo;
    }

}
