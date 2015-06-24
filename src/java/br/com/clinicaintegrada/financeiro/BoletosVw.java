package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.util.Date;

public class BoletosVw implements Serializable {

    private Long id;
    private Integer lote;
    private Integer movimento;
    private String nrCtrBoleto;
    private Integer loteBoleto;
    private Date processamento;
    private String logoBanco;
    private String logo;
    private String logoInformativo;
    private String logoVerso;
    private Integer codigo;
    private String responsavel;
    private Date vencimento;
    private String grupoCategoria;
    private String categoria;
    private String servico;
    private Integer pessoa;
    private String nomeBeneficiario;
    private Double valor;
    private Integer mensalidadesCorrigidas;
    private String mensagemBoleto;
    private String banco;
    private String agencia;
    private String cedente;
    private String boleto;
    private String email;
    private String nomeFilial;
    private String silteFilial;
    private String cnpjFilial;
    private String telefoneFilial;
    private String enderecoFilial;
    private String bairroFilial;
    private String cidadeFilial;
    private String ufFilial;
    private String cepFilial;
    private String logradouroResponsavel;
    private String enderecoResponsavel;
    private String cepResponsavel;
    private String ufResponsavel;
    private String cidadeResponsavel;
    private String informativo;
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

    public BoletosVw(Long id, Integer lote, Integer movimento, String nrCtrBoleto, Integer loteBoleto, Date processamento, String logoBanco, String logo, String logoInformativo, String logoVerso, Integer codigo, String responsavel, Date vencimento, String grupoCategoria, String categoria, String servico, Integer pessoa, String nomeBeneficiario, Double valor, Integer mensalidadesCorrigidas, String mensagemBoleto, String banco, String agencia, String cedente, String boleto, String email, String nomeFilial, String silteFilial, String cnpjFilial, String telefoneFilial, String enderecoFilial, String bairroFilial, String cidadeFilial, String ufFilial, String cepFilial, String logradouroResponsavel, String enderecoResponsavel, String cepResponsavel, String ufResponsavel, String cidadeResponsavel, String informativo, String localPagamento) {
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

    public Integer getLote() {
        return lote;
    }

    public void setLote(Integer lote) {
        this.lote = lote;
    }

    public Integer getMovimento() {
        return movimento;
    }

    public void setMovimento(Integer movimento) {
        this.movimento = movimento;
    }

    public String getNrCtrBoleto() {
        return nrCtrBoleto;
    }

    public void setNrCtrBoleto(String nrCtrBoleto) {
        this.nrCtrBoleto = nrCtrBoleto;
    }

    public Integer getLoteBoleto() {
        return loteBoleto;
    }

    public void setLoteBoleto(Integer loteBoleto) {
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

    public void setProcessamentoString(String processamentoString) {
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

    public Integer getPessoa() {
        return pessoa;
    }

    public void setPessoa(Integer pessoa) {
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
