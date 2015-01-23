package br.com.clinicaintegrada.cobranca.FEBRABAN;

import br.com.clinicaintegrada.utils.AnaliseString;

/**
 * Versão subcpadr12-layout padrao V 08 07 - 21.09 - V1
 *
 * @author rtools2
 */
public class RegistroDetalheSegmentoP {

    /**
     * Header (240 bits)
     */
    // CONTROLE
    // 1-3
    private String codigoBancoCompensacao;
    // 4-7
    private String loteServico;
    // 8-8
    private String tipoRegistro;
    // SERVIÇO
    // 9-13
    private String sequencialLote;
    // 14-14
    private String segmento;
    // 15-15
    private String cnab;
    // 16-17
    private String codigoMovimentoRemessa;
    // CONTA CORRENTE
    // 18-22
    private String agencia;
    // 23
    private String agenciaDV;
    // 24-35
    private String conta;
    // 36-36
    private String contaDV;
    // 37-37
    private String DV;
    // 38-57
    private String nossoNumero;
    // CARACTERÍSTICA COBRANÇA
    // 58-58
    private String codigoCarteira;
    // 59-59
    private String formaCadastramento;
    // 60-60
    private String tipoDocumento;
    // 61-61
    private String emissaoBloqueto;
    // 62-62
    private String identificacaoBloqueto;
    // --
    // 63-77
    private String numeroDocumentoCobranca;
    // 78-85
    private String dataVencimentoTitulo;
    // 86-100
    private String valorTitulo;
    // 101-105
    private String agenciaCobradora;
    // 106-106
    private String agenciaCobradoraDV;
    // 107-108
    private String especieTitulo;
    // 109-109
    private String aceite;
    // 110-117
    private String dataEmissaoTitulo;
    // 118-118
    private String jurosMoraCodigo;
    // 119-126
    private String jurosMoraData;
    // 127-141
    private String jurosMora;
    // 142-142
    private String desconto1Codigo;
    // 143-150
    private String desconto1Data;
    // 151-165
    private String desconto1;
    // 166-180
    private String valorIOF;
    // 181-195
    private String valorAbantimento;
    // 196-220
    private String identificacaoTitulo;
    // 221-221
    private String codigoProtesto;
    // 222-223
    private String prazoProtesto;
    // 224-224
    private String codigoBaixa;
    // 225-227
    private String prazoBaixaDevolucao;
    // 228-229
    private String codigoMoeda;
    // 230-239
    private String numeroContrato;
    // 240-240
    private String usoLivreBancoEmpresa;
    //LINHA
    //public static String LINHA = "0000000000000P 0000000 000000000000                      00 0                00000000000000000000000000 00 000000000000000000000000000000000000000000000000000000000000000000000000000000                         0000   000000000000 ";
    public static String LINHA = "0000000000000  0000000 000000000000                      00 0                00000000000000000000000000 00 000000000000000000000000000000000000000000000000000000000000000000000000000000                         0000   000000000000 ";

    public RegistroDetalheSegmentoP() {
        this.codigoBancoCompensacao = "000";
        this.loteServico = "0000";
        this.tipoRegistro = "3";
        this.sequencialLote = "00000";
        this.segmento = "P";
        this.cnab = " ";
        this.codigoMovimentoRemessa = "00";
        this.agencia = "00000";
        this.agenciaDV = " ";
        this.conta = "000000000000";
        this.contaDV = " ";
        this.DV = " ";
        this.nossoNumero = "                    ";
        this.codigoCarteira = "0";
        this.formaCadastramento = "0";
        this.tipoDocumento = " ";
        this.emissaoBloqueto = "0";
        this.identificacaoBloqueto = " ";
        this.numeroDocumentoCobranca = "               ";
        this.dataVencimentoTitulo = "00000000";
        this.valorTitulo = "000000000000000";
        this.agenciaCobradora = "00000";
        this.agenciaCobradoraDV = " ";
        this.especieTitulo = "00";
        this.aceite = " ";
        this.dataEmissaoTitulo = "00000000";
        this.jurosMoraCodigo = "0";
        this.jurosMoraData = "00000000";
        this.jurosMora = "000000000000000";
        this.desconto1Codigo = "0";
        this.desconto1Data = "00000000";
        this.desconto1 = "000000000000000";
        this.valorIOF = "000000000000000";
        this.valorAbantimento = "000000000000000";
        this.identificacaoTitulo = "                         ";
        this.codigoProtesto = "0";
        this.prazoProtesto = "00";
        this.codigoBaixa = "0";
        this.prazoBaixaDevolucao = "   ";
        this.codigoMoeda = "00";
        this.numeroContrato = "0000000000";
        this.usoLivreBancoEmpresa = " ";
    }

    public RegistroDetalheSegmentoP(String codigoBancoCompensacao, String loteServico, String tipoRegistro, String sequencialLote, String segmento, String cnab, String codigoMovimentoRemessa, String agencia, String agenciaDV, String conta, String contaDV, String DV, String nossoNumero, String codigoCarteira, String formaCadastramento, String tipoDocumento, String emissaoBloqueto, String identificacaoBloqueto, String numeroDocumentoCobranca, String dataVencimentoTitulo, String valorTitulo, String agenciaCobradora, String agenciaCobradoraDV, String especieTitulo, String aceite, String dataEmissaoTitulo, String jurosMoraCodigo, String jurosMoraData, String jurosMora, String desconto1Codigo, String desconto1Data, String desconto1, String valorIOF, String valorAbantimento, String identificacaoTitulo, String codigoProtesto, String prazoProtesto, String codigoBaixa, String prazoBaixaDevolucao, String codigoMoeda, String numeroContrato, String usoLivreBancoEmpresa) {
        this.codigoBancoCompensacao = codigoBancoCompensacao;
        this.loteServico = loteServico;
        this.tipoRegistro = tipoRegistro;
        this.sequencialLote = sequencialLote;
        this.segmento = segmento;
        this.cnab = cnab;
        this.codigoMovimentoRemessa = codigoMovimentoRemessa;
        this.agencia = agencia;
        this.agenciaDV = agenciaDV;
        this.conta = conta;
        this.contaDV = contaDV;
        this.DV = DV;
        this.nossoNumero = nossoNumero;
        this.codigoCarteira = codigoCarteira;
        this.formaCadastramento = formaCadastramento;
        this.tipoDocumento = tipoDocumento;
        this.emissaoBloqueto = emissaoBloqueto;
        this.identificacaoBloqueto = identificacaoBloqueto;
        this.numeroDocumentoCobranca = numeroDocumentoCobranca;
        this.dataVencimentoTitulo = dataVencimentoTitulo;
        this.valorTitulo = valorTitulo;
        this.agenciaCobradora = agenciaCobradora;
        this.agenciaCobradoraDV = agenciaCobradoraDV;
        this.especieTitulo = especieTitulo;
        this.aceite = aceite;
        this.dataEmissaoTitulo = dataEmissaoTitulo;
        this.jurosMoraCodigo = jurosMoraCodigo;
        this.jurosMoraData = jurosMoraData;
        this.jurosMora = jurosMora;
        this.desconto1Codigo = desconto1Codigo;
        this.desconto1Data = desconto1Data;
        this.desconto1 = desconto1;
        this.valorIOF = valorIOF;
        this.valorAbantimento = valorAbantimento;
        this.identificacaoTitulo = identificacaoTitulo;
        this.codigoProtesto = codigoProtesto;
        this.prazoProtesto = prazoProtesto;
        this.codigoBaixa = codigoBaixa;
        this.prazoBaixaDevolucao = prazoBaixaDevolucao;
        this.codigoMoeda = codigoMoeda;
        this.numeroContrato = numeroContrato;
        this.usoLivreBancoEmpresa = usoLivreBancoEmpresa;
    }

    public String getCodigoBancoCompensacao() {
        return codigoBancoCompensacao;
    }

    public void setCodigoBancoCompensacao(String codigoBancoCompensacao) {
        this.codigoBancoCompensacao += codigoBancoCompensacao;
    }

    public String getLoteServico() {
        return loteServico;
    }

    public void setLoteServico(String loteServico) {
        this.loteServico += loteServico;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro += tipoRegistro;
    }

    public String getSequencialLote() {
        return sequencialLote;
    }

    public void setSequencialLote(String sequencialLote) {
        this.sequencialLote += sequencialLote;
    }

    public String getCnab() {
        return cnab;
    }

    public void setCnab(String cnab) {
        this.cnab += cnab;
    }

    public String getCodigoMovimentoRemessa() {
        return codigoMovimentoRemessa;
    }

    public void setCodigoMovimentoRemessa(String codigoMovimentoRemessa) {
        this.codigoMovimentoRemessa += codigoMovimentoRemessa;
    }

    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia += agencia;
    }

    public String getAgenciaDV() {
        return agenciaDV;
    }

    public void setAgenciaDV(String agenciaDV) {
        this.agenciaDV += agenciaDV;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta += conta;
    }

    public String getContaDV() {
        return contaDV;
    }

    public void setContaDV(String contaDV) {
        this.contaDV += contaDV;
    }

    public String getDV() {
        return DV;
    }

    public void setDV(String DV) {
        this.DV += DV;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero += nossoNumero;
    }

    public String getCodigoCarteira() {
        return codigoCarteira;
    }

    public void setCodigoCarteira(String codigoCarteira) {
        this.codigoCarteira += codigoCarteira;
    }

    public String getFormaCadastramento() {
        return formaCadastramento;
    }

    public void setFormaCadastramento(String formaCadastramento) {
        this.formaCadastramento += formaCadastramento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento += tipoDocumento;
    }

    public String getEmissaoBloqueto() {
        return emissaoBloqueto;
    }

    public void setEmissaoBloqueto(String emissaoBloqueto) {
        this.emissaoBloqueto += emissaoBloqueto;
    }

    public String getIdentificacaoBloqueto() {
        return identificacaoBloqueto;
    }

    public void setIdentificacaoBloqueto(String identificacaoBloqueto) {
        this.identificacaoBloqueto += identificacaoBloqueto;
    }

    public String getNumeroDocumentoCobranca() {
        return numeroDocumentoCobranca;
    }

    public void setNumeroDocumentoCobranca(String numeroDocumentoCobranca) {
        this.numeroDocumentoCobranca += numeroDocumentoCobranca;
    }

    public String getDataVencimentoTitulo() {
        return dataVencimentoTitulo;
    }

    public void setDataVencimentoTitulo(String dataVencimentoTitulo) {
        this.dataVencimentoTitulo += dataVencimentoTitulo;
    }

    public String getValorTitulo() {
        return valorTitulo;
    }

    public void setValorTitulo(String valorTitulo) {
        this.valorTitulo += valorTitulo;
    }

    public String getAgenciaCobradora() {
        return agenciaCobradora;
    }

    public void setAgenciaCobradora(String agenciaCobradora) {
        this.agenciaCobradora += agenciaCobradora;
    }

    public String getAgenciaCobradoraDV() {
        return agenciaCobradoraDV;
    }

    public void setAgenciaCobradoraDV(String agenciaCobradoraDV) {
        this.agenciaCobradoraDV += agenciaCobradoraDV;
    }

    public String getEspecieTitulo() {
        return especieTitulo;
    }

    public void setEspecieTitulo(String especieTitulo) {
        this.especieTitulo += especieTitulo;
    }

    public String getAceite() {
        return aceite;
    }

    public void setAceite(String aceite) {
        this.aceite += aceite;
    }

    public String getDataEmissaoTitulo() {
        return dataEmissaoTitulo;
    }

    public void setDataEmissaoTitulo(String dataEmissaoTitulo) {
        this.dataEmissaoTitulo += dataEmissaoTitulo;
    }

    public String getJurosMoraCodigo() {
        return jurosMoraCodigo;
    }

    public void setJurosMoraCodigo(String jurosMoraCodigo) {
        this.jurosMoraCodigo += jurosMoraCodigo;
    }

    public String getJurosMoraData() {
        return jurosMoraData;
    }

    public void setJurosMoraData(String jurosMoraData) {
        this.jurosMoraData += jurosMoraData;
    }

    public String getJurosMora() {
        return jurosMora;
    }

    public void setJurosMora(String jurosMora) {
        this.jurosMora += jurosMora;
    }

    public String getDesconto1Codigo() {
        return desconto1Codigo;
    }

    public void setDesconto1Codigo(String desconto1Codigo) {
        this.desconto1Codigo += desconto1Codigo;
    }

    public String getDesconto1Data() {
        return desconto1Data;
    }

    public void setDesconto1Data(String desconto1Data) {
        this.desconto1Data += desconto1Data;
    }

    public String getDesconto1() {
        return desconto1;
    }

    public void setDesconto1(String desconto1) {
        this.desconto1 += desconto1;
    }

    public String getValorIOF() {
        return valorIOF;
    }

    public void setValorIOF(String valorIOF) {
        this.valorIOF += valorIOF;
    }

    public String getValorAbantimento() {
        return valorAbantimento;
    }

    public void setValorAbantimento(String valorAbantimento) {
        this.valorAbantimento += valorAbantimento;
    }

    public String getIdentificacaoTitulo() {
        return identificacaoTitulo;
    }

    public void setIdentificacaoTitulo(String identificacaoTitulo) {
        this.identificacaoTitulo += identificacaoTitulo;
    }

    public String getCodigoProtesto() {
        return codigoProtesto;
    }

    public void setCodigoProtesto(String codigoProtesto) {
        this.codigoProtesto += codigoProtesto;
    }

    public String getPrazoProtesto() {
        return prazoProtesto;
    }

    public void setPrazoProtesto(String prazoProtesto) {
        this.prazoProtesto += prazoProtesto;
    }

    public String getCodigoBaixa() {
        return codigoBaixa;
    }

    public void setCodigoBaixa(String codigoBaixa) {
        this.codigoBaixa += codigoBaixa;
    }

    public String getPrazoBaixaDevolucao() {
        return prazoBaixaDevolucao;
    }

    public void setPrazoBaixaDevolucao(String prazoBaixaDevolucao) {
        this.prazoBaixaDevolucao += prazoBaixaDevolucao;
    }

    public String getCodigoMoeda() {
        return codigoMoeda;
    }

    public void setCodigoMoeda(String codigoMoeda) {
        this.codigoMoeda += codigoMoeda;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato += numeroContrato;
    }

    public String getUsoLivreBancoEmpresa() {
        return usoLivreBancoEmpresa;
    }

    public void setUsoLivreBancoEmpresa(String usoLivreBancoEmpresa) {
        this.usoLivreBancoEmpresa += usoLivreBancoEmpresa;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento += segmento;
    }

    public void write() {
        LINHA = "";
        LINHA += AnaliseString.removeDiff(this.codigoBancoCompensacao, 3);
        LINHA += AnaliseString.removeDiff(this.loteServico, 4);
        LINHA += AnaliseString.removeDiff(this.tipoRegistro, 1);
        LINHA += AnaliseString.removeDiff(this.sequencialLote, 5);
        LINHA += AnaliseString.removeDiff(this.segmento, 1, true);
        LINHA += AnaliseString.removeDiff(this.cnab, 1, true);
        LINHA += AnaliseString.removeDiff(this.codigoMovimentoRemessa, 2);
        LINHA += AnaliseString.removeDiff(this.agencia, 5);
        LINHA += AnaliseString.removeDiff(this.agenciaDV, 1, true);
        LINHA += AnaliseString.removeDiff(this.conta, 12);
        LINHA += AnaliseString.removeDiff(this.contaDV, 1, true);
        LINHA += AnaliseString.removeDiff(this.DV, 1, true);
        LINHA += AnaliseString.removeDiff(this.nossoNumero, 20);
        LINHA += AnaliseString.removeDiff(this.codigoCarteira, 1);
        LINHA += AnaliseString.removeDiff(this.formaCadastramento, 1);
        LINHA += AnaliseString.removeDiff(this.tipoDocumento, 1, true);
        LINHA += AnaliseString.removeDiff(this.emissaoBloqueto, 1);
        LINHA += AnaliseString.removeDiff(this.identificacaoBloqueto, 1, true);
        LINHA += AnaliseString.removeDiff(this.numeroDocumentoCobranca, 15, true);
        LINHA += AnaliseString.removeDiff(this.dataVencimentoTitulo, 8);
        LINHA += AnaliseString.removeDiff(this.valorTitulo, 15);
        LINHA += AnaliseString.removeDiff(this.agenciaCobradora, 5);
        LINHA += AnaliseString.removeDiff(this.agenciaCobradoraDV, 1, true);
        LINHA += AnaliseString.removeDiff(this.especieTitulo, 2);
        LINHA += AnaliseString.removeDiff(this.aceite, 1, true);
        LINHA += AnaliseString.removeDiff(this.dataEmissaoTitulo, 8);
        LINHA += AnaliseString.removeDiff(this.jurosMoraCodigo, 1);
        LINHA += AnaliseString.removeDiff(this.jurosMoraData, 8);
        LINHA += AnaliseString.removeDiff(this.jurosMora, 15);
        LINHA += AnaliseString.removeDiff(this.desconto1Codigo, 1);
        LINHA += AnaliseString.removeDiff(this.desconto1Data, 8);
        LINHA += AnaliseString.removeDiff(this.desconto1, 15);
        LINHA += AnaliseString.removeDiff(this.valorIOF, 15);
        LINHA += AnaliseString.removeDiff(this.valorAbantimento, 15);
        LINHA += AnaliseString.removeDiff(this.identificacaoTitulo, 25, true);
        LINHA += AnaliseString.removeDiff(this.codigoProtesto, 1);
        LINHA += AnaliseString.removeDiff(this.prazoProtesto, 2);
        LINHA += AnaliseString.removeDiff(this.codigoBaixa, 1);
        LINHA += AnaliseString.removeDiff(this.prazoBaixaDevolucao, 3, true);
        LINHA += AnaliseString.removeDiff(this.codigoMoeda, 2);
        LINHA += AnaliseString.removeDiff(this.numeroContrato, 10);
        LINHA += AnaliseString.removeDiff(this.usoLivreBancoEmpresa, 1, true);
    }
}
