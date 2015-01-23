package br.com.clinicaintegrada.cobranca.FEBRABAN;

import br.com.clinicaintegrada.utils.AnaliseString;

/**
 * Versão subcpadr12-layout padrao V 08 07 - 21.09 - V1
 *
 * @author rtools2
 */
public class RegistroHeaderLote {

    /**
     * Header (240 bits)
     */
    // CONTROLE
    private String codigoBancoCompensacao;
    private String loteServico;
    private String tipoRegistro;
    // SERVIÇO
    private String tipoOperacao;
    private String tipoServico;
    // USUO EXCLUSIVO FEBRABAN
    private String servicoCnab;
    private String layoutVersao;
    private String cnab1;
    // CNAB - Empresas
    // INSCRIÇÃO
    private String tipoInscricao;
    private String numeroInscricao;
    private String convenio;
    // CONTA CORRENTE
    private String agencia;
    private String agenciaDV;
    private String conta;
    private String contaDV;
    private String DV;
    private String nome;
    // NOME DA EMPRESA
    private String mensagem1;
    private String mensagem2;
    // CONTROLE DE COBRANÇA
    private String numeroRemessaRetorno;
    private String dataRemessaRetorno;
    private String dataCredito;
    private String cnab2;
    //LINHA
    public static String LINHA = "00000000000  000 00000000000000000000000000000000000000000000000000000000                                                                                                              000000000000000000000000                                 ";

    public RegistroHeaderLote() {
        this.codigoBancoCompensacao = "000";
        this.loteServico = "0000";
        this.tipoRegistro = "1";
        this.tipoOperacao = "0";
        this.tipoServico = "01";
        this.servicoCnab = "  ";
        this.layoutVersao = "045";
        this.cnab1 = " ";
        this.tipoInscricao = "0";
        this.numeroInscricao = "000000000000000";
        this.convenio = "00000000000000000000";
        this.agencia = "00000";
        this.agenciaDV = "0";
        this.conta = "000000000000";
        this.contaDV = "0";
        this.DV = "0";
        this.nome = "                              ";
        this.mensagem1 = "                                        ";
        this.mensagem2 = "                                        ";
        this.numeroRemessaRetorno = "00000000";
        this.dataRemessaRetorno = "00000000";
        this.dataCredito = "00000000";
        this.cnab2 = "                                ";
    }

    /**
     *
     * @param codigoBancoCompensacao
     * @param loteServico
     * @param tipoRegistro
     * @param tipoOperacao
     * @param tipoServico
     * @param servicoCnab
     * @param layoutVersao
     * @param cnab1
     * @param tipoInscricao
     * @param numeroInscricao
     * @param convenio
     * @param agencia
     * @param agenciaDV
     * @param conta
     * @param contaDV
     * @param DV
     * @param nome
     * @param mensagem1
     * @param mensagem2
     * @param numeroRemessaRetorno
     * @param dataRemessaRetorno
     * @param dataCredito
     * @param cnab2
     */
    public RegistroHeaderLote(String codigoBancoCompensacao, String loteServico, String tipoRegistro, String tipoOperacao, String tipoServico, String servicoCnab, String layoutVersao, String cnab1, String tipoInscricao, String numeroInscricao, String convenio, String agencia, String agenciaDV, String conta, String contaDV, String DV, String nome, String mensagem1, String mensagem2, String numeroRemessaRetorno, String dataRemessaRetorno, String dataCredito, String cnab2) {
        this.codigoBancoCompensacao = codigoBancoCompensacao;
        this.loteServico = loteServico;
        this.tipoRegistro = tipoRegistro;
        this.tipoOperacao = tipoOperacao;
        this.tipoServico = tipoServico;
        this.cnab1 = cnab1;
        this.layoutVersao = layoutVersao;
        this.tipoInscricao = tipoInscricao;
        this.numeroInscricao = numeroInscricao;
        this.convenio = convenio;
        this.agencia = agencia;
        this.agenciaDV = agenciaDV;
        this.conta = conta;
        this.contaDV = contaDV;
        this.DV = DV;
        this.nome = nome;
        this.mensagem1 = mensagem1;
        this.mensagem2 = mensagem2;
        this.numeroRemessaRetorno = numeroRemessaRetorno;
        this.dataRemessaRetorno = dataRemessaRetorno;
        this.dataCredito = dataCredito;
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
     * Tipo de Operação
     *
     * @return
     */
    public String getTipoOperacao() {
        return tipoOperacao;
    }

    /**
     * 1 Caracter - Posição: 9 - 9 - Alfanumérico (Alfa)
     *
     * @param tipoOperacao
     */
    public void setTipoOperacao(String tipoOperacao) {
        this.tipoOperacao += tipoOperacao;
    }

    /**
     * Tipo de Serviço
     *
     * @return
     */
    public String getTipoServico() {
        return tipoServico;
    }

    /**
     * 2 Caracteres - Posição: 10 - 11 - Default: '01' - Num
     *
     * @param tipoServico
     */
    public void setTipoServico(String tipoServico) {
        this.tipoServico += tipoServico;
    }

    public String getServicoCnab() {
        return servicoCnab;
    }

    /**
     * 2 Caracteres - Posição: 12 - 13 - Brancos - Alfa
     *
     * @param servicoCnab
     */
    public void setServicoCnab(String servicoCnab) {
        this.servicoCnab += servicoCnab;
    }

    /**
     * Nº da Versão do Layout do Lote
     *
     * @return
     */
    public String getLayoutVersao() {
        return layoutVersao;
    }

    /**
     * 3 Caracteres - Posição : 14 - 16 - Default: '045' - Num
     *
     * @param layoutVersao
     *
     */
    public void setLayoutVersao(String layoutVersao) {
        this.layoutVersao += layoutVersao;
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
     * 1 Caracteres - Posição: 17 - 17 - Brancos - Alfa
     *
     * @param cnab1
     */
    public void setCnab1(String cnab1) {
        this.cnab1 += cnab1;
    }

    /**
     * Tipo de Inscrição da Empresa
     *
     * @return
     */
    public String getTipoInscricao() {
        return tipoInscricao;
    }

    /**
     * 1 Caracteres - Posição: 18 - 18 - Num
     *
     * @param tipoInscricao
     */
    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao += tipoInscricao;
    }

    /**
     * Nº da Inscrição da Empresa
     *
     * @return
     */
    public String getNumeroInscricao() {
        return numeroInscricao;
    }

    /**
     * 15 Caracteres - Posição: 19 - 33 - Num
     *
     * @param numeroInscricao
     */
    public void setNumeroInscricao(String numeroInscricao) {
        this.numeroInscricao += numeroInscricao;
    }

    /**
     * Código do Convênio no Banco
     *
     * @return
     */
    public String getConvenio() {
        return convenio;
    }

    /**
     * 20 Caracteres - Posição: 34 - 53 - Alfa
     *
     * @param convenio
     */
    public void setConvenio(String convenio) {
        this.convenio += convenio;
    }

    /**
     * Agência Mantenedora da Conta
     *
     * @return
     */
    public String getAgencia() {
        return agencia;
    }

    /**
     * 5 Caracteres - Posição: 54 - 58 - Num
     *
     * @param agencia
     */
    public void setAgencia(String agencia) {
        this.agencia += agencia;
    }

    /**
     * Digito Verificador da Conta
     *
     * @return
     */
    public String getAgenciaDV() {
        return agenciaDV;
    }

    /**
     * 1 Caracteres - Posição: 59 - Alfa
     *
     * @param agenciaDV
     */
    public void setAgenciaDV(String agenciaDV) {
        this.agenciaDV += agenciaDV;
    }

    /**
     * Número da Conta Corrente
     *
     * @return
     */
    public String getConta() {
        return conta;
    }

    /**
     * 12 Caracteres - Posição: 60 - 71 - Num
     *
     * @param conta
     */
    public void setConta(String conta) {
        this.conta += conta;
    }

    /**
     * Digito Verificador da Conta
     *
     * @return
     */
    public String getContaDV() {
        return contaDV;
    }

    /**
     * 1 Caracteres - Posição: 72 - 72 - Alfa
     *
     * @param contaDV
     */
    public void setContaDV(String contaDV) {
        this.contaDV += contaDV;
    }

    /**
     * Digito Verificador Agência Conta
     *
     * @return
     */
    public String getDV() {
        return DV;
    }

    /**
     * 1 Caracteres - Posição: 73 - 73 - Alfa
     *
     * @param DV
     */
    public void setDV(String DV) {
        this.DV += DV;
    }

    /**
     * Nome da Empresa
     *
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * 30 Caracteres - Posição: 74 - 103 - Alfa
     *
     * @param nome
     */
    public void setNome(String nome) {
        this.nome += nome;
    }

    /**
     * Informação 1
     *
     * @return
     */
    public String getMensagem1() {
        return mensagem1;
    }

    /**
     * 40 Caracteres - Posição: 104 - 143 - Alfa
     *
     * @param mensagem1
     */
    public void setMensagem1(String mensagem1) {
        this.mensagem1 += mensagem1;
    }

    /**
     * Informação 2
     *
     * @return
     */
    public String getMensagem2() {
        return mensagem2;
    }

    /**
     * 40 Caracteres - Posição: 144 - 183 - Alfa
     *
     * @param mensagem2
     */
    public void setMensagem2(String mensagem2) {
        this.mensagem2 += mensagem2;
    }

    /**
     * Número Remessa/Retorno
     *
     * @return
     */
    public String getNumeroRemessaRetorno() {
        return numeroRemessaRetorno;
    }

    /**
     * 8 Caracteres - Posição: 184 - 191 - Num
     *
     * @param numeroRemessaRetorno
     */
    public void setNumeroRemessaRetorno(String numeroRemessaRetorno) {
        this.numeroRemessaRetorno += numeroRemessaRetorno;
    }

    /**
     * Data de Gravação Remessa/Retorno
     *
     * @return
     */
    public String getDataRemessaRetorno() {
        return dataRemessaRetorno;
    }

    /**
     * 8 Caracteres - Posição: 192 - 199 - Num
     *
     * @param dataRemessaRetorno
     */
    public void setDataRemessaRetorno(String dataRemessaRetorno) {
        this.dataRemessaRetorno += dataRemessaRetorno;
    }

    /**
     * Data do Crédito
     *
     * @return
     */
    public String getDataCredito() {
        return dataCredito;
    }

    /**
     * 8 Caracteres - Posição: 200 - 207 - Num
     *
     * @param dataCredito
     */
    public void setDataCredito(String dataCredito) {
        this.dataCredito += dataCredito;
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
     * 33 Caracteres - Posição: 208 - 240 - Brancos - Alfa
     *
     * @param cnab2
     */
    public void setCnab2(String cnab2) {
        this.cnab2 += cnab2;
    }

    /**
     * Rescreve a linha
     */
    public void write() {
        LINHA = "";
        LINHA += AnaliseString.removeDiff(this.codigoBancoCompensacao, 3);
        LINHA += AnaliseString.removeDiff(this.loteServico, 4);
        LINHA += AnaliseString.removeDiff(this.tipoRegistro, 1);
        LINHA += AnaliseString.removeDiff(this.tipoOperacao, 1);
        LINHA += AnaliseString.removeDiff(this.tipoServico, 2);
        LINHA += AnaliseString.removeDiff(this.servicoCnab, 2, true);
        LINHA += AnaliseString.removeDiff(this.layoutVersao, 3);
        LINHA += AnaliseString.removeDiff(this.cnab1, 1, true);
        LINHA += AnaliseString.removeDiff(this.tipoInscricao, 1);
        LINHA += AnaliseString.removeDiff(this.numeroInscricao, 15);
        LINHA += AnaliseString.removeDiff(this.convenio, 20);
        LINHA += AnaliseString.removeDiff(this.agencia, 5);
        LINHA += AnaliseString.removeDiff(this.agenciaDV, 1);
        LINHA += AnaliseString.removeDiff(this.conta, 12);
        LINHA += AnaliseString.removeDiff(this.contaDV, 1);
        LINHA += AnaliseString.removeDiff(this.DV, 1);
        LINHA += AnaliseString.removeDiff(this.nome, 30, true);
        LINHA += AnaliseString.removeDiff(this.mensagem1, 40, true);
        LINHA += AnaliseString.removeDiff(this.mensagem2, 40, true);
        LINHA += AnaliseString.removeDiff(this.numeroRemessaRetorno, 8);
        LINHA += AnaliseString.removeDiff(this.dataRemessaRetorno, 8);
        LINHA += AnaliseString.removeDiff(this.dataCredito, 8);
        LINHA += AnaliseString.removeDiff(this.cnab2, 33, true);
    }

}
