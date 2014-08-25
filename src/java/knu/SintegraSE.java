/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package knu;

public class SintegraSE {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected SintegraSE(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(SintegraSE obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        knuJNI.delete_SintegraSE(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setCod_erro(int value) {
    knuJNI.SintegraSE_cod_erro_set(swigCPtr, this, value);
  }

  public int getCod_erro() {
    return knuJNI.SintegraSE_cod_erro_get(swigCPtr, this);
  }

  public void setTam_motivos_restricao(int value) {
    knuJNI.SintegraSE_tam_motivos_restricao_set(swigCPtr, this, value);
  }

  public int getTam_motivos_restricao() {
    return knuJNI.SintegraSE_tam_motivos_restricao_get(swigCPtr, this);
  }

  public void setAtividade_economica(String value) {
    knuJNI.SintegraSE_atividade_economica_set(swigCPtr, this, value);
  }

  public String getAtividade_economica() {
    return knuJNI.SintegraSE_atividade_economica_get(swigCPtr, this);
  }

  public void setBairro(String value) {
    knuJNI.SintegraSE_bairro_set(swigCPtr, this, value);
  }

  public String getBairro() {
    return knuJNI.SintegraSE_bairro_get(swigCPtr, this);
  }

  public void setCep(String value) {
    knuJNI.SintegraSE_cep_set(swigCPtr, this, value);
  }

  public String getCep() {
    return knuJNI.SintegraSE_cep_get(swigCPtr, this);
  }

  public void setCnpj(String value) {
    knuJNI.SintegraSE_cnpj_set(swigCPtr, this, value);
  }

  public String getCnpj() {
    return knuJNI.SintegraSE_cnpj_get(swigCPtr, this);
  }

  public void setComplemento(String value) {
    knuJNI.SintegraSE_complemento_set(swigCPtr, this, value);
  }

  public String getComplemento() {
    return knuJNI.SintegraSE_complemento_get(swigCPtr, this);
  }

  public void setData_consulta(String value) {
    knuJNI.SintegraSE_data_consulta_set(swigCPtr, this, value);
  }

  public String getData_consulta() {
    return knuJNI.SintegraSE_data_consulta_get(swigCPtr, this);
  }

  public void setData_situacao_cadastral(String value) {
    knuJNI.SintegraSE_data_situacao_cadastral_set(swigCPtr, this, value);
  }

  public String getData_situacao_cadastral() {
    return knuJNI.SintegraSE_data_situacao_cadastral_get(swigCPtr, this);
  }

  public void setDesc_erro(String value) {
    knuJNI.SintegraSE_desc_erro_set(swigCPtr, this, value);
  }

  public String getDesc_erro() {
    return knuJNI.SintegraSE_desc_erro_get(swigCPtr, this);
  }

  public void setIe(String value) {
    knuJNI.SintegraSE_ie_set(swigCPtr, this, value);
  }

  public String getIe() {
    return knuJNI.SintegraSE_ie_get(swigCPtr, this);
  }

  public void setLogradouro(String value) {
    knuJNI.SintegraSE_logradouro_set(swigCPtr, this, value);
  }

  public String getLogradouro() {
    return knuJNI.SintegraSE_logradouro_get(swigCPtr, this);
  }

  public void setMunicipio(String value) {
    knuJNI.SintegraSE_municipio_set(swigCPtr, this, value);
  }

  public String getMunicipio() {
    return knuJNI.SintegraSE_municipio_get(swigCPtr, this);
  }

  public void setNumero(String value) {
    knuJNI.SintegraSE_numero_set(swigCPtr, this, value);
  }

  public String getNumero() {
    return knuJNI.SintegraSE_numero_get(swigCPtr, this);
  }

  public void setRazao(String value) {
    knuJNI.SintegraSE_razao_set(swigCPtr, this, value);
  }

  public String getRazao() {
    return knuJNI.SintegraSE_razao_get(swigCPtr, this);
  }

  public void setSituacao_cadastral(String value) {
    knuJNI.SintegraSE_situacao_cadastral_set(swigCPtr, this, value);
  }

  public String getSituacao_cadastral() {
    return knuJNI.SintegraSE_situacao_cadastral_get(swigCPtr, this);
  }

  public void setTelefone(String value) {
    knuJNI.SintegraSE_telefone_set(swigCPtr, this, value);
  }

  public String getTelefone() {
    return knuJNI.SintegraSE_telefone_get(swigCPtr, this);
  }

  public void setUf(String value) {
    knuJNI.SintegraSE_uf_set(swigCPtr, this, value);
  }

  public String getUf() {
    return knuJNI.SintegraSE_uf_get(swigCPtr, this);
  }

  public void setHtml(String value) {
    knuJNI.SintegraSE_html_set(swigCPtr, this, value);
  }

  public String getHtml() {
    return knuJNI.SintegraSE_html_get(swigCPtr, this);
  }

  public String getMotivoRestricao(int index) {
    return knuJNI.SintegraSE_getMotivoRestricao(swigCPtr, this, index);
  }

  public SintegraSE() {
    this(knuJNI.new_SintegraSE(), true);
  }

}