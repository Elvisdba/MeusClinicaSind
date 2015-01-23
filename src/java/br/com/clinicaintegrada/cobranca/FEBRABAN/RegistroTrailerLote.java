package br.com.clinicaintegrada.cobranca.FEBRABAN;

import br.com.clinicaintegrada.utils.AnaliseString;

/**
 * Versão subcpadr12-layout padrao V 08 07 - 21.09 - V1
 *
 * @author rtools2
 */
public class RegistroTrailerLote {

    /**
     * Trailer (240 bits) - Ultima linha
     */
    // CONTROLE
    private String codigoBancoCompensacao;
    private String loteServico;
    private String tipoRegistro;
    // USU EXCLUSIVO FEBRABAN
    private String cnab1;
    // OUTRAS INFORMAÇÕES
    private String quantidadeRegistrosLote;
    private String quantidadeTitulosCobrancaSimples;
    private String valorTotalTitulosCarteiraSimples;
    private String quantidadeTitulosCobrancaVinculada;
    private String valorTotalTitulosCarteiraVinculada;
    private String quantidadeTitulosCobrancaCaucionada;
    private String valorTotalTitulosCarteiraCaucionada;
    private String quantidadeTitulosCobrancaDescontada;
    private String valorTotalTitulosCarteiraDescontada;
    private String numeroAviso;
    private String cnab2;
    //LINHA
    public static String LINHA = "00000000         0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000                                                                                                                     ";

    public RegistroTrailerLote() {
        this.codigoBancoCompensacao = "000";
        this.loteServico = "0000";
        this.tipoRegistro = "0";
        this.cnab1 = "         ";
        this.quantidadeRegistrosLote = "000000";
        this.quantidadeTitulosCobrancaSimples = "000000";
        this.valorTotalTitulosCarteiraSimples = "000000000000000";
        this.quantidadeTitulosCobrancaVinculada = "000000";
        this.valorTotalTitulosCarteiraVinculada = "000000000000000";
        this.quantidadeTitulosCobrancaCaucionada = "000000";
        this.valorTotalTitulosCarteiraCaucionada = "000000000000000";
        this.quantidadeTitulosCobrancaDescontada = "000000";
        this.valorTotalTitulosCarteiraDescontada = "000000000000000";
        this.numeroAviso = "00000000";
        this.cnab2 = "                                                                                                                     ";
    }

    public RegistroTrailerLote(String codigoBancoCompensacao, String loteServico, String tipoRegistro, String cnab1, String quantidadeRegistrosLote, String quantidadeTitulosCobrancaSimples, String valorTotalTitulosCarteiraSimples, String quantidadeTitulosCobrancaVinculada, String valorTotalTitulosCarteiraVinculada, String quantidadeTitulosCobrancaCaucionada, String valorTotalTitulosCarteiraCaucionada, String quantidadeTitulosCobrancaDescontada, String valorTotalTitulosCarteiraDescontada, String numeroAviso, String cnab2) {
        this.codigoBancoCompensacao = codigoBancoCompensacao;
        this.loteServico = loteServico;
        this.tipoRegistro = tipoRegistro;
        this.cnab1 = cnab1;
        this.quantidadeRegistrosLote = quantidadeRegistrosLote;
        this.quantidadeTitulosCobrancaSimples = quantidadeTitulosCobrancaSimples;
        this.valorTotalTitulosCarteiraSimples = valorTotalTitulosCarteiraSimples;
        this.quantidadeTitulosCobrancaVinculada = quantidadeTitulosCobrancaVinculada;
        this.valorTotalTitulosCarteiraVinculada = valorTotalTitulosCarteiraVinculada;
        this.quantidadeTitulosCobrancaCaucionada = quantidadeTitulosCobrancaCaucionada;
        this.valorTotalTitulosCarteiraCaucionada = valorTotalTitulosCarteiraCaucionada;
        this.quantidadeTitulosCobrancaDescontada = quantidadeTitulosCobrancaDescontada;
        this.valorTotalTitulosCarteiraDescontada = valorTotalTitulosCarteiraDescontada;
        this.numeroAviso = numeroAviso;
        this.cnab2 = cnab2;
    }

    /**
     * Código do banco na compensação
     *
     * @return
     */
    public String getCodigoBancoCompensacao() {
        return codigoBancoCompensacao;
    }

    /**
     * 3 Caracteres - Posição: 1 - 3 - Num
     *
     * @param codigoBancoCompensacao
     */
    public void setCodigoBancoCompensacao(String codigoBancoCompensacao) {
        this.codigoBancoCompensacao += codigoBancoCompensacao;
    }

    /**
     * Lote do serviço
     *
     * @return
     */
    public String getLoteServico() {
        return loteServico;
    }

    /**
     * 4 Caracteres - Posição: 4 - 7 - Num
     *
     * @param loteServico
     */
    public void setLoteServico(String loteServico) {
        this.loteServico += loteServico;
    }

    /**
     * Tipo de Registro
     *
     * @return
     */
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * 1 Caracteres - Posição: 8 - 8 - Default '1' - Num
     *
     * @param tipoRegistro
     */
    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro += tipoRegistro;
    }

    /**
     * Uso Exclusivo FEBRABAN/CNAB
     *
     * @return
     */
    public String getCnab1() {
        return cnab1;
    }

    /**
     * 9 Caracteres - Posição: 9 - 17 - Brancos - Alfa
     *
     * @param cnab1
     */
    public void setCnab1(String cnab1) {
        this.cnab1 += cnab1;
    }

    // TOTALIZAÇÃO DAS COBRANÇAS SIMPLES
    /**
     * Quantidade de Registros no Lote
     *
     * @return
     */
    public String getQuantidadeRegistrosLote() {
        return quantidadeRegistrosLote;
    }

    /**
     * 6 Caracteres - Posição: 18 - 23 - Num
     *
     * @param quantidadeRegistrosLote
     */
    public void setQuantidadeRegistrosLote(String quantidadeRegistrosLote) {
        this.quantidadeRegistrosLote += quantidadeRegistrosLote;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getQuantidadeTitulosCobrancaSimples() {
        return quantidadeTitulosCobrancaSimples;
    }

    /**
     * 6 Caracteres - Posição: 24 - 29 - Num
     *
     * @param quantidadeTitulosCobrancaSimples
     */
    public void setQuantidadeTitulosCobrancaSimples(String quantidadeTitulosCobrancaSimples) {
        this.quantidadeTitulosCobrancaSimples += quantidadeTitulosCobrancaSimples;
    }

    /**
     * Valor Total dos Titulos em Carteira
     *
     * @return
     */
    public String getValorTotalTitulosCarteiraSimples() {
        return valorTotalTitulosCarteiraSimples;
    }

    /**
     * 15 Caracteres - Posição: 30 - 46 - Num
     *
     * @param valorTotalTitulosCarteiraSimples
     */
    public void setValorTotalTitulosCarteiraSimples(String valorTotalTitulosCarteiraSimples) {
        this.valorTotalTitulosCarteiraSimples += valorTotalTitulosCarteiraSimples;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getQuantidadeTitulosCobrancaVinculada() {
        return quantidadeTitulosCobrancaVinculada;
    }

    /**
     * 6 Caracteres - Posição: 47 - 52 - Num
     *
     * @param quantidadeTitulosCobrancaVinculada
     */
    public void setQuantidadeTitulosCobrancaVinculada(String quantidadeTitulosCobrancaVinculada) {
        this.quantidadeTitulosCobrancaVinculada += quantidadeTitulosCobrancaVinculada;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getValorTotalTitulosCarteiraVinculada() {
        return valorTotalTitulosCarteiraVinculada;
    }

    /**
     * 15 Caracteres - Posição: 53 - 69 - Num
     *
     * @param valorTotalTitulosCarteiraVinculada
     */
    public void setValorTotalTitulosCarteiraVinculada(String valorTotalTitulosCarteiraVinculada) {
        this.valorTotalTitulosCarteiraVinculada += valorTotalTitulosCarteiraVinculada;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getQuantidadeTitulosCobrancaCaucionada() {
        return quantidadeTitulosCobrancaCaucionada;
    }

    /**
     * 6 Caracteres - Posição: 70 - 75 - Num
     *
     * @param quantidadeTitulosCobrancaCaucionada
     */
    public void setQuantidadeTitulosCobrancaCaucionada(String quantidadeTitulosCobrancaCaucionada) {
        this.quantidadeTitulosCobrancaCaucionada += quantidadeTitulosCobrancaCaucionada;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getValorTotalTitulosCarteiraCaucionada() {
        return valorTotalTitulosCarteiraCaucionada;
    }

    /**
     * 15 Caracteres - Posição: 76 - 92 - Num
     *
     * @param valorTotalTitulosCarteiraCaucionada
     */
    public void setValorTotalTitulosCarteiraCaucionada(String valorTotalTitulosCarteiraCaucionada) {
        this.valorTotalTitulosCarteiraCaucionada += valorTotalTitulosCarteiraCaucionada;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getQuantidadeTitulosCobrancaDescontada() {
        return quantidadeTitulosCobrancaDescontada;
    }

    /**
     * 6 Caracteres - Posição: 93 - 98 - Num
     *
     * @param quantidadeTitulosCobrancaDescontada
     */
    public void setQuantidadeTitulosCobrancaDescontada(String quantidadeTitulosCobrancaDescontada) {
        this.quantidadeTitulosCobrancaDescontada += quantidadeTitulosCobrancaDescontada;
    }

    /**
     * Quantidade de Titulos em Cobrança
     *
     * @return
     */
    public String getValorTotalTitulosCarteiraDescontada() {
        return valorTotalTitulosCarteiraDescontada;
    }

    /**
     * 15 Caracteres - Posição: 99 - 115 - Alfa
     *
     * @param valorTotalTitulosCarteiraDescontada
     */
    public void setValorTotalTitulosCarteiraDescontada(String valorTotalTitulosCarteiraDescontada) {
        this.valorTotalTitulosCarteiraDescontada += valorTotalTitulosCarteiraDescontada;
    }

    /**
     * Número do Aviso de Lançamento
     *
     * @return
     */
    public String getNumeroAviso() {
        return numeroAviso;
    }

    /**
     * 8 Caracteres - Posição: 116 - 123 - Alfa
     *
     * @param numeroAviso
     */
    public void setNumeroAviso(String numeroAviso) {
        this.numeroAviso += numeroAviso;
    }

    /**
     * Uso Exclusivo FEBRABAN/CNAB
     *
     * @return
     */
    public String getCnab2() {
        return cnab2;
    }

    /**
     * 117 Caracteres - Posição: 124 - 240 - Brancos - Alfa
     *
     * @param cnab2
     */
    public void setCnab2(String cnab2) {
        this.cnab2 += cnab2;
    }

    /**
     * Escreve a linha
     */
    public void write() {
        LINHA = "";
        LINHA += AnaliseString.removeDiff(this.codigoBancoCompensacao, 3);
        LINHA += AnaliseString.removeDiff(this.loteServico, 4);
        LINHA += AnaliseString.removeDiff(this.tipoRegistro, 1);
        LINHA += AnaliseString.removeDiff(this.cnab1, 9, true);
        LINHA += AnaliseString.removeDiff(this.quantidadeRegistrosLote, 6);
        LINHA += AnaliseString.removeDiff(this.quantidadeTitulosCobrancaSimples, 6);
        LINHA += AnaliseString.removeDiff(this.valorTotalTitulosCarteiraSimples, 15);
        LINHA += AnaliseString.removeDiff(this.quantidadeTitulosCobrancaVinculada, 6);
        LINHA += AnaliseString.removeDiff(this.valorTotalTitulosCarteiraVinculada, 15);
        LINHA += AnaliseString.removeDiff(this.quantidadeTitulosCobrancaCaucionada, 6);
        LINHA += AnaliseString.removeDiff(this.valorTotalTitulosCarteiraCaucionada, 15);
        LINHA += AnaliseString.removeDiff(this.quantidadeTitulosCobrancaDescontada, 6);
        LINHA += AnaliseString.removeDiff(this.valorTotalTitulosCarteiraDescontada, 15);
        LINHA += AnaliseString.removeDiff(this.numeroAviso, 8);
        LINHA += AnaliseString.removeDiff(this.cnab2, 117, true);
    }

    public static String getLINHA() {
        return LINHA;
    }

    public static void setLINHA(String aLINHA) {
        LINHA = aLINHA;
    }
}
