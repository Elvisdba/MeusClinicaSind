package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Departamento;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_lote")
public class Lote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date emissao;
    @Column(name = "ds_pag_rec", length = 1, nullable = true)
    private String pagRec;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date lancamento;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @OneToOne
    private Departamento departamento;
    @Column(name = "nr_valor", length = 10, nullable = false)
    private float valor;
    @JoinColumn(name = "id_condicao_pagamento", referencedColumnName = "id")
    @OneToOne
    private CondicaoPagamento condicaoPagamento;
    @Column(name = "ds_historico", length = 2000)
    private String historico;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filial;
    @Column(name = "nr_desconto", length = 10)
    private float desconto;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id")
    @OneToOne
    private FTipoDocumento ftipoDocumento;
    @Column(name = "ds_documento", length = 100)
    private String documento;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @OneToOne
    private FStatus status;

    public Lote() {
        this.id = -1;
        this.contrato = new Contrato();
        this.cliente = new Cliente();
        this.emissao = new Date();
        this.pagRec = "";
        this.lancamento = new Date();
        this.pessoa = new Pessoa();
        this.departamento = new Departamento();
        this.valor = 0;
        this.condicaoPagamento = new CondicaoPagamento();
        this.historico = "";
        this.filial = new Filial();
        this.desconto = 0;
        this.ftipoDocumento = new FTipoDocumento();
        this.documento = "";
        this.status = new FStatus();
    }

    public Lote(int id, Contrato contrato, Cliente cliente, Date emissao, String pagRec, Date lancamento, Pessoa pessoa, Departamento departamento, float valor, CondicaoPagamento condicaoPagamento, String historico, Filial filial, float desconto, FTipoDocumento ftipoDocumento, String documento, FStatus status) {
        this.id = id;
        this.contrato = contrato;
        this.cliente = cliente;
        this.emissao = emissao;
        this.pagRec = pagRec;
        this.lancamento = lancamento;
        this.pessoa = pessoa;
        this.departamento = departamento;
        this.valor = valor;
        this.condicaoPagamento = condicaoPagamento;
        this.historico = historico;
        this.filial = filial;
        this.desconto = desconto;
        this.ftipoDocumento = ftipoDocumento;
        this.documento = documento;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public String getPagRec() {
        return pagRec;
    }

    public void setPagRec(String pagRec) {
        this.pagRec = pagRec;
    }

    public Date getLancamento() {
        return lancamento;
    }

    public void setLancamento(Date lancamento) {
        this.lancamento = lancamento;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public CondicaoPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public FTipoDocumento getFtipoDocumento() {
        return ftipoDocumento;
    }

    public void setFtipoDocumento(FTipoDocumento ftipoDocumento) {
        this.ftipoDocumento = ftipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }

}
