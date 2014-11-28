package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "boletos_vw")
public class BoletosVw implements Serializable {

    @Id
    @Column(name = "seq")
    private Long id;
    @JoinColumn(name = "id_fin_lote", referencedColumnName = "id")
    @ManyToOne
    private Lote lote;
    @JoinColumn(name = "id_fin_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;
    @Column(name = "nr_ctr_boleto")
    private String nrCtrBoleto;
    @JoinColumn(name = "id_lote_boleto", referencedColumnName = "id")
    @ManyToOne
    private LoteBoleto loteBoleto;
    @Temporal(TemporalType.DATE)
    @Column(name = "processamento")
    private Date processamento;
    @Column(name = "logo_banco")
    private String logoBanco;
    @Column(name = "logo")
    private String logo;
    @Column(name = "logo_informativo")
    private String logoInformativo;
    @Column(name = "logo_verso")
    private String logoVerso;
    @Column(name = "codigo")
    private Integer codigo;
    @Column(name = "responsavel")
    private String responsavel;
    @Temporal(TemporalType.DATE)
    @Column(name = "vencimento")
    private Date vencimento;
    @Column(name = "grupo_categoria")
    private String grupoCategoria;
    @Column(name = "categoria")
    private String categoria;
    @Column(name = "servico")
    private String servico;
    @JoinColumn(name = "id_beneficiario", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "nome_beneficiario")
    private String nomeBeneficiario;
    @Column(name = "valor")
    private Double valor;
    @Column(name = "mensalidades_corrigidas")
    private Integer mensalidadesCorrigidas;
    @Column(name = "mensagem_boleto")
    private String mensagemBoleto;
    @Column(name = "banco")
    private String banco;
    @Column(name = "agencia")
    private String agencia;
    @Column(name = "cedente")
    private String cedente;
    @Column(name = "boleto")
    private String boleto;
    @Column(name = "email")
    private String email;
    @Column(name = "nome_filial")
    private String nomeFilial;
    @Column(name = "silte_filial")
    private String silteFilial;
    @Column(name = "cnpj_filial")
    private String cnpjFilial;
    @Column(name = "tel_filial")
    private String telefoneFilial;
    @Column(name = "endereco_filial")
    private String enderecoFilial;
    @Column(name = "bairro_filial")
    private String bairroFilial;
    @Column(name = "cidade_filial")
    private String cidadeFilial;
    @Column(name = "uf_filial")
    private String ufFilial;
    @Column(name = "cep_filial")
    private String cepFilial;
    @Column(name = "logradouro_responsavel")
    private String logradouroResponsavel;
    @Column(name = "endereco_responsavel")
    private String enderecoResponsavel;
    @Column(name = "cep_responsavel")
    private String cepResponsavel;
    @Column(name = "uf_responsavel")
    private String ufResponsavel;
    @Column(name = "cidade_responsavel")
    private String cidadeResponsavel;
    @Column(name = "informativo")
    private String informativo;
    @Column(name = "local_pagamento")
    private String localPagamento;

    public BoletosVw() {
        this.id = null;
        this.lote = null;
        this.movimento = null;
        this.nrCtrBoleto = "";
        this.loteBoleto = null;
        this.processamento = null;
        this.logoBanco = "";
        this.logo = "";
        this.logoInformativo = "";
        this.logoVerso = "";
        this.codigo = null;
        this.responsavel = "";
        this.vencimento = null;
        this.grupoCategoria = "";
        this.categoria = "";
        this.servico = "";
        this.pessoa = null;
        this.nomeBeneficiario = "";
        this.valor = null;
        this.mensalidadesCorrigidas = null;
        this.mensagemBoleto = "";
        this.banco = "";
        this.agencia = "";
        this.cedente = "";
        this.boleto = "";
        this.email = "";
        this.nomeFilial = "";
        this.silteFilial = "";
        this.cnpjFilial = "";
        this.telefoneFilial = "";
        this.enderecoFilial = "";
        this.bairroFilial = "";
        this.cidadeFilial = "";
        this.ufFilial = "";
        this.cepFilial = "";
        this.logradouroResponsavel = "";
        this.enderecoResponsavel = "";
        this.cepResponsavel = "";
        this.ufResponsavel = "";
        this.cidadeResponsavel = "";
        this.informativo = "";
        this.localPagamento = "";
    }

    public BoletosVw(Long id, Lote lote, Movimento movimento, String nrCtrBoleto, LoteBoleto loteBoleto, Date processamento, String logoBanco, String logo, String logoInformativo, String logoVerso, Integer codigo, String responsavel, Date vencimento, String grupoCategoria, String categoria, String servico, Pessoa pessoa, String nomeBeneficiario, Double valor, Integer mensalidadesCorrigidas, String mensagemBoleto, String banco, String agencia, String cedente, String boleto, String email, String nomeFilial, String silteFilial, String cnpjFilial, String telefoneFilial, String enderecoFilial, String bairroFilial, String cidadeFilial, String ufFilial, String cepFilial, String logradouroResponsavel, String enderecoResponsavel, String cepResponsavel, String ufResponsavel, String cidadeResponsavel, String informativo, String localPagamento) {
        this.id = id;
        this.lote = lote;
        this.movimento = movimento;
        this.nrCtrBoleto = nrCtrBoleto;
        this.loteBoleto = loteBoleto;
        this.processamento = processamento;
        this.logoBanco = logoBanco;
        this.logo = logo;
        this.logoInformativo = logoInformativo;
        this.logoVerso = logoVerso;
        this.codigo = codigo;
        this.responsavel = responsavel;
        this.vencimento = vencimento;
        this.grupoCategoria = grupoCategoria;
        this.categoria = categoria;
        this.servico = servico;
        this.pessoa = pessoa;
        this.nomeBeneficiario = nomeBeneficiario;
        this.valor = valor;
        this.mensalidadesCorrigidas = mensalidadesCorrigidas;
        this.mensagemBoleto = mensagemBoleto;
        this.banco = banco;
        this.agencia = agencia;
        this.cedente = cedente;
        this.boleto = boleto;
        this.email = email;
        this.nomeFilial = nomeFilial;
        this.silteFilial = silteFilial;
        this.cnpjFilial = cnpjFilial;
        this.telefoneFilial = telefoneFilial;
        this.enderecoFilial = enderecoFilial;
        this.bairroFilial = bairroFilial;
        this.cidadeFilial = cidadeFilial;
        this.ufFilial = ufFilial;
        this.cepFilial = cepFilial;
        this.logradouroResponsavel = logradouroResponsavel;
        this.enderecoResponsavel = enderecoResponsavel;
        this.cepResponsavel = cepResponsavel;
        this.ufResponsavel = ufResponsavel;
        this.cidadeResponsavel = cidadeResponsavel;
        this.informativo = informativo;
        this.localPagamento = localPagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getNrCtrBoleto() {
        return nrCtrBoleto;
    }

    public void setNrCtrBoleto(String nrCtrBoleto) {
        this.nrCtrBoleto = nrCtrBoleto;
    }

    public LoteBoleto getLoteBoleto() {
        return loteBoleto;
    }

    public void setLoteBoleto(LoteBoleto loteBoleto) {
        this.loteBoleto = loteBoleto;
    }

    public Date getProcessamento() {
        return processamento;
    }

    public void setProcessamento(Date processamento) {
        this.processamento = processamento;
    }

    public String getProcessamentoString() {
        return DataHoje.converteData(processamento);
    }

    public void setProcessamento(String processamentoString) {
        this.processamento = DataHoje.converte(processamentoString);
    }

    public String getLogoBanco() {
        return logoBanco;
    }

    public void setLogoBanco(String logoBanco) {
        this.logoBanco = logoBanco;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoInformativo() {
        return logoInformativo;
    }

    public void setLogoInformativo(String logoInformativo) {
        this.logoInformativo = logoInformativo;
    }

    public String getLogoVerso() {
        return logoVerso;
    }

    public void setLogoVerso(String logoVerso) {
        this.logoVerso = logoVerso;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getVencimentoString() {
        return DataHoje.converteData(vencimento);
    }

    public void setVencimentoString(String vencimentoString) {
        this.vencimento = DataHoje.converte(vencimentoString);
    }

    public String getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(String grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getServico() {
        return servico;
    }

    public void setServico(String servico) {
        this.servico = servico;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valorString) {
        this.valor = Moeda.converteUS$ToDouble(valorString);
    }

    public Integer getMensalidadesCorrigidas() {
        return mensalidadesCorrigidas;
    }

    public void setMensalidadesCorrigidas(Integer mensalidadesCorrigidas) {
        this.mensalidadesCorrigidas = mensalidadesCorrigidas;
    }

    public String getMensagemBoleto() {
        return mensagemBoleto;
    }

    public void setMensagemBoleto(String mensagemBoleto) {
        this.mensagemBoleto = mensagemBoleto;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getCedente() {
        return cedente;
    }

    public void setCedente(String cedente) {
        this.cedente = cedente;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public String getSilteFilial() {
        return silteFilial;
    }

    public void setSilteFilial(String silteFilial) {
        this.silteFilial = silteFilial;
    }

    public String getCnpjFilial() {
        return cnpjFilial;
    }

    public void setCnpjFilial(String cnpjFilial) {
        this.cnpjFilial = cnpjFilial;
    }

    public String getTelefoneFilial() {
        return telefoneFilial;
    }

    public void setTelefoneFilial(String telefoneFilial) {
        this.telefoneFilial = telefoneFilial;
    }

    public String getEnderecoFilial() {
        return enderecoFilial;
    }

    public void setEnderecoFilial(String enderecoFilial) {
        this.enderecoFilial = enderecoFilial;
    }

    public String getBairroFilial() {
        return bairroFilial;
    }

    public void setBairroFilial(String bairroFilial) {
        this.bairroFilial = bairroFilial;
    }

    public String getCidadeFilial() {
        return cidadeFilial;
    }

    public void setCidadeFilial(String cidadeFilial) {
        this.cidadeFilial = cidadeFilial;
    }

    public String getUfFilial() {
        return ufFilial;
    }

    public void setUfFilial(String ufFilial) {
        this.ufFilial = ufFilial;
    }

    public String getCepFilial() {
        return cepFilial;
    }

    public void setCepFilial(String cepFilial) {
        this.cepFilial = cepFilial;
    }

    public String getLogradouroResponsavel() {
        return logradouroResponsavel;
    }

    public void setLogradouroResponsavel(String logradouroResponsavel) {
        this.logradouroResponsavel = logradouroResponsavel;
    }

    public String getEnderecoResponsavel() {
        return enderecoResponsavel;
    }

    public void setEnderecoResponsavel(String enderecoResponsavel) {
        this.enderecoResponsavel = enderecoResponsavel;
    }

    public String getCepResponsavel() {
        return cepResponsavel;
    }

    public void setCepResponsavel(String cepResponsavel) {
        this.cepResponsavel = cepResponsavel;
    }

    public String getUfResponsavel() {
        return ufResponsavel;
    }

    public void setUfResponsavel(String ufResponsavel) {
        this.ufResponsavel = ufResponsavel;
    }

    public String getCidadeResponsavel() {
        return cidadeResponsavel;
    }

    public void setCidadeResponsavel(String cidadeResponsavel) {
        this.cidadeResponsavel = cidadeResponsavel;
    }

    public String getInformativo() {
        return informativo;
    }

    public void setInformativo(String informativo) {
        this.informativo = informativo;
    }

    public String getLocalPagamento() {
        return localPagamento;
    }

    public void setLocalPagamento(String localPagamento) {
        this.localPagamento = localPagamento;
    }
}
