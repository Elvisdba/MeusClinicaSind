package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_tipo_pagamento")
public class TipoPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;

    public TipoPagamento() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoPagamento(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TipoPagamento other = (TipoPagamento) obj;
        return true;
    }

    @Override
    public String toString() {
        return "TipoPagamento{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
