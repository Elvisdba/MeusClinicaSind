package br.com.clinicaintegrada.cobranca.FEBRABAN;

import br.com.clinicaintegrada.utils.AnaliseString;

/**
 * Versão subcpadr12-layout padrao V 08 07 - 21.09 - V1
 *
 * @author rtools2
 */
public class RegistroDetalheSegmentoQ {

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
    private String cnab1;
    // 16-17
    private String codigoMovimentoRemessa;
    // DADOS DO SACADO
    // 18-18
    private String tipoInscricao;
    // 19-33
    private String numeroInscricao;
    // 34-73
    private String nome;
    // 74-113
    private String endereco;
    // 114-128
    private String bairro;
    // 129-133
    private String cep;
    // 134-136
    private String sufixoCep;
    // 137-151
    private String cidade;
    // 152-153
    private String uf;
    // SACADOR / AVALISTA
    // 154-154
    private String tipoInscricaoSacAval;
    // 155-169
    private String numeroDocumentoSacAval;
    // 170-209
    private String nomeSacAval;
    // 210-121
    private String codigoBancoCompensasao;
    // 213-232
    private String nossoNumero;
    // 233-240
    private String cnab2;

    //LINHA
    public static String LINHA = "0000000000000  000000000000000000                                                                                               00000000                 0000000000000000                                        000                            ";

    public RegistroDetalheSegmentoQ() {
        this.codigoBancoCompensacao = "000";
        this.loteServico = "0000";
        this.tipoRegistro = "3";
        this.sequencialLote = "00000";
        this.segmento = "Q";
        this.cnab1 = " ";
        this.codigoMovimentoRemessa = "00";
        this.tipoInscricao = "0";
        this.numeroInscricao = "000000000000000";
        this.nome = "                                        ";
        this.endereco = "                                        ";
        this.bairro = "               ";
        this.cep = "00000";
        this.sufixoCep = "000";
        this.cidade = "               ";
        this.uf = "  ";
        this.tipoInscricaoSacAval = "0";
        this.numeroDocumentoSacAval = "000000000000000";
        this.nomeSacAval = "                                        ";
        this.codigoBancoCompensasao = "000";
        this.nossoNumero = "                    ";
        this.cnab2 = "        ";
    }

    public RegistroDetalheSegmentoQ(String codigoBancoCompensacao, String loteServico, String tipoRegistro, String sequencialLote, String segmento, String cnab1, String codigoMovimentoRemessa, String tipoInscricao, String numeroInscricao, String nome, String endereco, String bairro, String cep, String sufixoCep, String cidade, String uf, String tipoInscricaoSacAval, String numeroDocumentoSacAval, String nomeSacAval, String codigoBancoCompensasao, String nossoNumero, String cnab2) {
        this.codigoBancoCompensacao = codigoBancoCompensacao;
        this.loteServico = loteServico;
        this.tipoRegistro = tipoRegistro;
        this.sequencialLote = sequencialLote;
        this.segmento = segmento;
        this.cnab1 = cnab1;
        this.codigoMovimentoRemessa = codigoMovimentoRemessa;
        this.tipoInscricao = tipoInscricao;
        this.numeroInscricao = numeroInscricao;
        this.nome = nome;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cep = cep;
        this.sufixoCep = sufixoCep;
        this.cidade = cidade;
        this.uf = uf;
        this.tipoInscricaoSacAval = tipoInscricaoSacAval;
        this.numeroDocumentoSacAval = numeroDocumentoSacAval;
        this.nomeSacAval = nomeSacAval;
        this.codigoBancoCompensasao = codigoBancoCompensasao;
        this.nossoNumero = nossoNumero;
        this.cnab2 = cnab2;
    }

    public String getCodigoBancoCompensacao() {
        return codigoBancoCompensacao;
    }

    public void setCodigoBancoCompensacao(String codigoBancoCompensacao) {
        this.codigoBancoCompensacao = codigoBancoCompensacao;
    }

    public String getLoteServico() {
        return loteServico;
    }

    public void setLoteServico(String loteServico) {
        this.loteServico = loteServico;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getSequencialLote() {
        return sequencialLote;
    }

    public void setSequencialLote(String sequencialLote) {
        this.sequencialLote = sequencialLote;
    }

    public String getSegmento() {
        return segmento;
    }

    public void setSegmento(String segmento) {
        this.segmento = segmento;
    }

    public String getCnab1() {
        return cnab1;
    }

    public void setCnab1(String cnab1) {
        this.cnab1 = cnab1;
    }

    public String getCodigoMovimentoRemessa() {
        return codigoMovimentoRemessa;
    }

    public void setCodigoMovimentoRemessa(String codigoMovimentoRemessa) {
        this.codigoMovimentoRemessa = codigoMovimentoRemessa;
    }

    public String getTipoInscricao() {
        return tipoInscricao;
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public String getNumeroInscricao() {
        return numeroInscricao;
    }

    public void setNumeroInscricao(String numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getSufixoCep() {
        return sufixoCep;
    }

    public void setSufixoCep(String sufixoCep) {
        this.sufixoCep = sufixoCep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTipoInscricaoSacAval() {
        return tipoInscricaoSacAval;
    }

    public void setTipoInscricaoSacAval(String tipoInscricaoSacAval) {
        this.tipoInscricaoSacAval = tipoInscricaoSacAval;
    }

    public String getNumeroDocumentoSacAval() {
        return numeroDocumentoSacAval;
    }

    public void setNumeroDocumentoSacAval(String numeroDocumentoSacAval) {
        this.numeroDocumentoSacAval = numeroDocumentoSacAval;
    }

    public String getNomeSacAval() {
        return nomeSacAval;
    }

    public void setNomeSacAval(String nomeSacAval) {
        this.nomeSacAval = nomeSacAval;
    }

    public String getCodigoBancoCompensasao() {
        return codigoBancoCompensasao;
    }

    public void setCodigoBancoCompensasao(String codigoBancoCompensasao) {
        this.codigoBancoCompensasao = codigoBancoCompensasao;
    }

    public String getNossoNumero() {
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public String getCnab2() {
        return cnab2;
    }

    public void setCnab2(String cnab2) {
        this.cnab2 = cnab2;
    }

    public void write() {
        LINHA = "";
        LINHA += AnaliseString.removeDiff(codigoBancoCompensacao, 3);
        LINHA += AnaliseString.removeDiff(loteServico, 4);
        LINHA += AnaliseString.removeDiff(tipoRegistro, 1);
        LINHA += AnaliseString.removeDiff(sequencialLote, 5);
        LINHA += AnaliseString.removeDiff(segmento, 1, true);
        LINHA += AnaliseString.removeDiff(cnab1, 1, true);
        LINHA += AnaliseString.removeDiff(codigoMovimentoRemessa, 2);
        LINHA += AnaliseString.removeDiff(tipoInscricao, 1);
        LINHA += AnaliseString.removeDiff(numeroInscricao, 15);
        LINHA += AnaliseString.removeDiff(nome, 40, true);
        LINHA += AnaliseString.removeDiff(endereco, 40, true);
        LINHA += AnaliseString.removeDiff(bairro, 15, true);
        LINHA += AnaliseString.removeDiff(cep, 5);
        LINHA += AnaliseString.removeDiff(sufixoCep, 3);
        LINHA += AnaliseString.removeDiff(cidade, 15, true);
        LINHA += AnaliseString.removeDiff(uf, 2, true);
        LINHA += AnaliseString.removeDiff(tipoInscricaoSacAval, 1);
        LINHA += AnaliseString.removeDiff(numeroDocumentoSacAval, 15);
        LINHA += AnaliseString.removeDiff(nomeSacAval, 40, true);
        LINHA += AnaliseString.removeDiff(codigoBancoCompensasao, 3);
        LINHA += AnaliseString.removeDiff(nossoNumero, 20, true);
        LINHA += AnaliseString.removeDiff(cnab2, 8, true);
    }
}
