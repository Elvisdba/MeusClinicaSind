package br.com.clinicaintegrada.impressao;

public class ParametroBoleto {

    private String cliente_logo;
    private String cliente_nome;
    private String codigo;
    private String responsavel;
    private String vencimento;
    private String servico;
    private String valor;
    private String valor_total;
    private String valor_atrasada;
    private String valor_vencimento;
    private String logo_promocao;
    private String logo_banco;
    private String mensagem;
    private String agencia;
    private String representacao;
    private String codigo_cedente;
    private String nosso_numero;
    private String processamento;
    private String codigo_barras;
    private String serrilha;
    private String endereco_responsavel;
    private String endereco_filial;
    private String complemento_responsavel;
    private String complemento_filial;
    private String cnpj_filial;
    private String telefone_filial;
    private String email_filial;
    private String site_filial;
    private String logo_verso;
    private String local_pagamento;
    private String informativo;

    public ParametroBoleto(String cliente_logo, String cliente_nome, String codigo, String responsavel, String vencimento, String servico, String valor, String valor_total, String valor_atrasada, String valor_vencimento, String logo_promocao, String logo_banco, String mensagem, String agencia, String representacao, String codigo_cedente, String nosso_numero, String processamento, String codigo_barras, String serrilha, String endereco_responsavel, String endereco_filial, String complemento_responsavel, String complemento_filial, String cnpj_filial, String telefone_filial, String email_filial, String site_filial, String logo_verso, String local_pagamento, String informativo) {
        this.cliente_logo = cliente_logo;
        this.cliente_nome = cliente_nome;
        this.codigo = codigo;
        this.responsavel = responsavel;
        this.vencimento = vencimento;
        this.servico = servico;
        this.valor = valor;
        this.valor_total = valor_total;
        this.valor_atrasada = valor_atrasada;
        this.valor_vencimento = valor_vencimento;
        this.logo_promocao = logo_promocao;
        this.logo_banco = logo_banco;
        this.mensagem = mensagem;
        this.agencia = agencia;
        this.representacao = representacao;
        this.codigo_cedente  = codigo_cedente;
        this.nosso_numero = nosso_numero;
        this.processamento = processamento;
        this.codigo_barras = codigo_barras;
        this.serrilha = serrilha;
        this.endereco_responsavel = endereco_responsavel;
        this.endereco_filial = endereco_filial;
        this.complemento_responsavel = complemento_responsavel;
        this.complemento_filial = complemento_filial;
        this.cnpj_filial = cnpj_filial;
        this.telefone_filial = telefone_filial;
        this.email_filial = email_filial;
        this.site_filial = site_filial;
        this.logo_verso = logo_verso;
        this.local_pagamento = local_pagamento;
        this.informativo = informativo;
    }

    public String getCliente_logo() {
        return cliente_logo;
    }

    public void setCliente_logo(String cliente_logo) {
        this.cliente_logo = cliente_logo;
    }

    public String getCliente_nome() {
        return cliente_nome;
    }

    public void setCliente_nome(String cliente_nome) {
        this.cliente_nome = cliente_nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getValor_total() {
        return valor_total;
    }

    public void setValor_total(String valor_total) {
        this.valor_total = valor_total;
    }

    public String getValor_atrasada() {
        return valor_atrasada;
    }

    public void setValor_atrasada(String valor_atrasada) {
        this.valor_atrasada = valor_atrasada;
    }

    public String getValor_vencimento() {
        return valor_vencimento;
    }

    public void setValor_vencimento(String valor_vencimento) {
        this.valor_vencimento = valor_vencimento;
    }

    public String getLogo_promocao() {
        return logo_promocao;
    }

    public void setLogo_promocao(String logo_promocao) {
        this.logo_promocao = logo_promocao;
    }

    public String getLogo_banco() {
        return logo_banco;
    }

    public void setLogo_banco(String logo_banco) {
        this.logo_banco = logo_banco;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getRepresentacao() {
        return representacao;
    }

    public void setRepresentacao(String representacao) {
        this.representacao = representacao;
    }

    public String getCodigo_cedente() {
        return codigo_cedente;
    }

    public void setCodigo_cedente(String codigo_cedente) {
        this.codigo_cedente = codigo_cedente;
    }

    public String getNosso_numero() {
        return nosso_numero;
    }

    public void setNosso_numero(String nosso_numero) {
        this.nosso_numero = nosso_numero;
    }

    public String getProcessamento() {
        return processamento;
    }

    public void setProcessamento(String processamento) {
        this.processamento = processamento;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public void setCodigo_barras(String codigo_barras) {
        this.codigo_barras = codigo_barras;
    }

    public String getSerrilha() {
        return serrilha;
    }

    public void setSerrilha(String serrilha) {
        this.serrilha = serrilha;
    }

    public String getEndereco_responsavel() {
        return endereco_responsavel;
    }

    public void setEndereco_responsavel(String endereco_responsavel) {
        this.endereco_responsavel = endereco_responsavel;
    }

    public String getEndereco_filial() {
        return endereco_filial;
    }

    public void setEndereco_filial(String endereco_filial) {
        this.endereco_filial = endereco_filial;
    }

    public String getComplemento_responsavel() {
        return complemento_responsavel;
    }

    public void setComplemento_responsavel(String complemento_responsavel) {
        this.complemento_responsavel = complemento_responsavel;
    }

    public String getComplemento_filial() {
        return complemento_filial;
    }

    public void setComplemento_filial(String complemento_filial) {
        this.complemento_filial = complemento_filial;
    }

    public String getCnpj_filial() {
        return cnpj_filial;
    }

    public void setCnpj_filial(String cnpj_filial) {
        this.cnpj_filial = cnpj_filial;
    }

    public String getTelefone_filial() {
        return telefone_filial;
    }

    public void setTelefone_filial(String telefone_filial) {
        this.telefone_filial = telefone_filial;
    }

    public String getEmail_filial() {
        return email_filial;
    }

    public void setEmail_filial(String email_filial) {
        this.email_filial = email_filial;
    }

    public String getSite_filial() {
        return site_filial;
    }

    public void setSite_filial(String site_filial) {
        this.site_filial = site_filial;
    }

    public String getLogo_verso() {
        return logo_verso;
    }

    public void setLogo_verso(String logo_verso) {
        this.logo_verso = logo_verso;
    }

    public String getLocal_pagamento() {
        return local_pagamento;
    }

    public void setLocal_pagamento(String local_pagamento) {
        this.local_pagamento = local_pagamento;
    }

    public String getInformativo() {
        return informativo;
    }

    public void setInformativo(String informativo) {
        this.informativo = informativo;
    }

}
