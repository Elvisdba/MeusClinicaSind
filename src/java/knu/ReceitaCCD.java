/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.7
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package knu;

public class ReceitaCCD {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ReceitaCCD(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ReceitaCCD obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        knuJNI.delete_ReceitaCCD(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setCod_erro(int value) {
    knuJNI.ReceitaCCD_cod_erro_set(swigCPtr, this, value);
  }

  public int getCod_erro() {
    return knuJNI.ReceitaCCD_cod_erro_get(swigCPtr, this);
  }

  public void setDesc_erro(String value) {
    knuJNI.ReceitaCCD_desc_erro_set(swigCPtr, this, value);
  }

  public String getDesc_erro() {
    return knuJNI.ReceitaCCD_desc_erro_get(swigCPtr, this);
  }

  public void setCnpj(String value) {
    knuJNI.ReceitaCCD_cnpj_set(swigCPtr, this, value);
  }

  public String getCnpj() {
    return knuJNI.ReceitaCCD_cnpj_get(swigCPtr, this);
  }

  public void setCpf(String value) {
    knuJNI.ReceitaCCD_cpf_set(swigCPtr, this, value);
  }

  public String getCpf() {
    return knuJNI.ReceitaCCD_cpf_get(swigCPtr, this);
  }

  public void setNome(String value) {
    knuJNI.ReceitaCCD_nome_set(swigCPtr, this, value);
  }

  public String getNome() {
    return knuJNI.ReceitaCCD_nome_get(swigCPtr, this);
  }

  public void setEmitida(String value) {
    knuJNI.ReceitaCCD_emitida_set(swigCPtr, this, value);
  }

  public String getEmitida() {
    return knuJNI.ReceitaCCD_emitida_get(swigCPtr, this);
  }

  public void setValida(String value) {
    knuJNI.ReceitaCCD_valida_set(swigCPtr, this, value);
  }

  public String getValida() {
    return knuJNI.ReceitaCCD_valida_get(swigCPtr, this);
  }

  public void setCodigo(String value) {
    knuJNI.ReceitaCCD_codigo_set(swigCPtr, this, value);
  }

  public String getCodigo() {
    return knuJNI.ReceitaCCD_codigo_get(swigCPtr, this);
  }

  public void setHtml(String value) {
    knuJNI.ReceitaCCD_html_set(swigCPtr, this, value);
  }

  public String getHtml() {
    return knuJNI.ReceitaCCD_html_get(swigCPtr, this);
  }

  public void setTexto(String value) {
    knuJNI.ReceitaCCD_texto_set(swigCPtr, this, value);
  }

  public String getTexto() {
    return knuJNI.ReceitaCCD_texto_get(swigCPtr, this);
  }

  public ReceitaCCD() {
    this(knuJNI.new_ReceitaCCD(), true);
  }

}