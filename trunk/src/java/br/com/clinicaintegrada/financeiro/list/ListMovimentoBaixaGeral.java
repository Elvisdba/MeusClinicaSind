package br.com.clinicaintegrada.financeiro.list;

import br.com.clinicaintegrada.financeiro.Cartao;
import br.com.clinicaintegrada.financeiro.ChequePag;
import br.com.clinicaintegrada.financeiro.ChequeRec;
import br.com.clinicaintegrada.financeiro.Plano5;
import br.com.clinicaintegrada.financeiro.TipoPagamento;
import br.com.clinicaintegrada.utils.Moeda;

public class ListMovimentoBaixaGeral {

    private String vencimento;
    private Float valor;
    private String documento;
    private TipoPagamento tipoPagamento;
    private ChequePag chequePag;
    private ChequeRec chequeRec;
    private Plano5 plano5;
    private Cartao cartao;

    public ListMovimentoBaixaGeral(String vencimento, String valorString, String documento, TipoPagamento tipoPagamento) {
        this.vencimento = vencimento;
        this.valor = Moeda.converteUS$(valorString);
        this.documento = documento;
        this.tipoPagamento = tipoPagamento;
    }

    public ListMovimentoBaixaGeral(String vencimento, String valorString, String documento, TipoPagamento tipoPagamento, ChequePag chequePag) {
        this.vencimento = vencimento;
        this.valor = Moeda.converteUS$(valorString);
        this.documento = documento;
        this.tipoPagamento = tipoPagamento;
        this.chequePag = chequePag;
    }

    public ListMovimentoBaixaGeral(String vencimento, String valorString, String documento, TipoPagamento tipoPagamento, ChequeRec chequeRec) {
        this.vencimento = vencimento;
        this.valor = Moeda.converteUS$(valorString);
        this.documento = documento;
        this.tipoPagamento = tipoPagamento;
        this.chequeRec = chequeRec;
    }

    public ListMovimentoBaixaGeral(String vencimento, String valorString, String documento, TipoPagamento tipoPagamento, ChequePag chequePag, Cartao cartao) {
        this.vencimento = vencimento;
        this.valor = Moeda.converteUS$(valorString);
        this.documento = documento;
        this.tipoPagamento = tipoPagamento;
        this.chequePag = chequePag;
        this.cartao = cartao;
    }

    public ListMovimentoBaixaGeral(String vencimento, String valorString, String documento, TipoPagamento tipoPagamento, ChequePag chequePag, Plano5 plano5) {
        this.vencimento = vencimento;
        this.valor = Moeda.converteUS$(valorString);
        this.documento = documento;
        this.tipoPagamento = tipoPagamento;
        this.chequePag = chequePag;
        this.plano5 = plano5;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valorString) {
        this.valor = Moeda.converteUS$(valorString);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public ChequePag getChequePag() {
        return chequePag;
    }

    public void setChequePag(ChequePag chequePag) {
        this.chequePag = chequePag;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public ChequeRec getChequeRec() {
        return chequeRec;
    }

    public void setChequeRec(ChequeRec chequeRec) {
        this.chequeRec = chequeRec;
    }

}
