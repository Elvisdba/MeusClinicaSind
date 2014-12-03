package br.com.clinicaintegrada.financeiro.list;

public class ListMovimentoReceber {

    private boolean selected;
    private String servicos_descricao;
    private String tipo_servico_descricao;
    private String movimento_referencia;
    private String movimento_vencimento;
    private String movimento_valor;
    private String movimento_acrescimo;
    private String movimento_desconto;
    private String movimento_valor_calculado;
    private String baixa_data;
    private String baixa_valor;
    private String movimento_es;
    private String pessoa_nome;
    private String pessoa_id;
    private String movimento_id;
    private String lote_id;
    private String lote_lancamento;
    private String movimento_documento;
    private String movimento_dias;
    private String movimento_multa;
    private String movimento_juros;
    private String movimento_correcao;
    private String usuario_caixa;
    private String movimento_baixa;
    private String lote_documento;
    private String row_css;
    private boolean disabled;

    public ListMovimentoReceber(boolean selected, String servicos_descricao, String tipo_servico_descricao, String movimento_referencia, String movimento_vencimento, String movimento_valor, String movimento_acrescimo, String movimento_desconto, String movimento_valor_calculado, String baixa_data, String baixa_valor, String movimento_es, String pessoa_nome, String pessoa_id, String movimento_id, String lote_id, String lote_lancamento, String movimento_documento, String movimento_dias, String movimento_multa, String movimento_juros, String movimento_correcao, String usuario_caixa, String movimento_baixa, String lote_documento, String row_css, boolean disabled) {
        this.selected = selected;
        this.servicos_descricao = servicos_descricao;
        this.tipo_servico_descricao = tipo_servico_descricao;
        this.movimento_referencia = movimento_referencia;
        this.movimento_vencimento = movimento_vencimento;
        this.movimento_valor = movimento_valor;
        this.movimento_acrescimo = movimento_acrescimo;
        this.movimento_desconto = movimento_desconto;
        this.movimento_valor_calculado = movimento_valor_calculado;
        this.baixa_data = baixa_data;
        this.baixa_valor = baixa_valor;
        this.movimento_es = movimento_es;
        this.pessoa_nome = pessoa_nome;
        this.pessoa_id = pessoa_id;
        this.movimento_id = movimento_id;
        this.lote_id = lote_id;
        this.lote_lancamento = lote_lancamento;
        this.movimento_documento = movimento_documento;
        this.movimento_dias = movimento_dias;
        this.movimento_multa = movimento_multa;
        this.movimento_juros = movimento_juros;
        this.movimento_correcao = movimento_correcao;
        this.usuario_caixa = usuario_caixa;
        this.movimento_baixa = movimento_baixa;
        this.lote_documento = lote_documento;
        this.row_css = row_css;
        this.disabled = disabled;
    }

    public String getServicos_descricao() {
        return servicos_descricao;
    }

    public void setServicos_descricao(String servicos_descricao) {
        this.servicos_descricao = servicos_descricao;
    }

    public String getTipo_servico_descricao() {
        return tipo_servico_descricao;
    }

    public void setTipo_servico_descricao(String tipo_servico_descricao) {
        this.tipo_servico_descricao = tipo_servico_descricao;
    }

    public String getMovimento_referencia() {
        return movimento_referencia;
    }

    public void setMovimento_referencia(String movimento_referencia) {
        this.movimento_referencia = movimento_referencia;
    }

    public String getMovimento_vencimento() {
        return movimento_vencimento;
    }

    public void setMovimento_vencimento(String movimento_vencimento) {
        this.movimento_vencimento = movimento_vencimento;
    }

    public String getMovimento_valor() {
        return movimento_valor;
    }

    public void setMovimento_valor(String movimento_valor) {
        this.movimento_valor = movimento_valor;
    }

    public String getMovimento_acrescimo() {
        return movimento_acrescimo;
    }

    public void setMovimento_acrescimo(String movimento_acrescimo) {
        this.movimento_acrescimo = movimento_acrescimo;
    }

    public String getMovimento_desconto() {
        return movimento_desconto;
    }

    public void setMovimento_desconto(String movimento_desconto) {
        this.movimento_desconto = movimento_desconto;
    }

    public String getMovimento_valor_calculado() {
        return movimento_valor_calculado;
    }

    public void setMovimento_valor_calculado(String movimento_valor_calculado) {
        this.movimento_valor_calculado = movimento_valor_calculado;
    }

    public String getBaixa_data() {
        return baixa_data;
    }

    public void setBaixa_data(String baixa_data) {
        this.baixa_data = baixa_data;
    }

    public String getBaixa_valor() {
        return baixa_valor;
    }

    public void setBaixa_valor(String baixa_valor) {
        this.baixa_valor = baixa_valor;
    }

    public String getMovimento_es() {
        return movimento_es;
    }

    public void setMovimento_es(String movimento_es) {
        this.movimento_es = movimento_es;
    }

    public String getPessoa_nome() {
        return pessoa_nome;
    }

    public void setPessoa_nome(String pessoa_nome) {
        this.pessoa_nome = pessoa_nome;
    }

    public String getPessoa_id() {
        return pessoa_id;
    }

    public void setPessoa_id(String pessoa_id) {
        this.pessoa_id = pessoa_id;
    }

    public String getLote_id() {
        return lote_id;
    }

    public void setLote_id(String lote_id) {
        this.lote_id = lote_id;
    }

    public String getLote_lancamento() {
        return lote_lancamento;
    }

    public void setLote_lancamento(String lote_lancamento) {
        this.lote_lancamento = lote_lancamento;
    }

    public String getMovimento_documento() {
        return movimento_documento;
    }

    public void setMovimento_documento(String movimento_documento) {
        this.movimento_documento = movimento_documento;
    }

    public String getMovimento_dias() {
        return movimento_dias;
    }

    public void setMovimento_dias(String movimento_dias) {
        this.movimento_dias = movimento_dias;
    }

    public String getMovimento_multa() {
        return movimento_multa;
    }

    public void setMovimento_multa(String movimento_multa) {
        this.movimento_multa = movimento_multa;
    }

    public String getMovimento_juros() {
        return movimento_juros;
    }

    public void setMovimento_juros(String movimento_juros) {
        this.movimento_juros = movimento_juros;
    }

    public String getMovimento_correcao() {
        return movimento_correcao;
    }

    public void setMovimento_correcao(String movimento_correcao) {
        this.movimento_correcao = movimento_correcao;
    }

    public String getUsuario_caixa() {
        return usuario_caixa;
    }

    public void setUsuario_caixa(String usuario_caixa) {
        this.usuario_caixa = usuario_caixa;
    }

    public String getMovimento_baixa() {
        return movimento_baixa;
    }

    public void setMovimento_baixa(String movimento_baixa) {
        this.movimento_baixa = movimento_baixa;
    }

    public String getLote_documento() {
        return lote_documento;
    }

    public void setLote_documento(String lote_documento) {
        this.lote_documento = lote_documento;
    }

    public String getMovimento_id() {
        return movimento_id;
    }

    public void setMovimento_id(String movimento_id) {
        this.movimento_id = movimento_id;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getRow_css() {
        return row_css;
    }

    public void setRow_css(String row_css) {
        this.row_css = row_css;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}
