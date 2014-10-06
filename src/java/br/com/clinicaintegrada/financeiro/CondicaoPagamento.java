package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fin_condicao_pagamento")
@NamedQueries({
    @NamedQuery(name = "CondicaoPagamento.pesquisaID", query = "SELECT CP FROM CondicaoPagamento AS CP WHERE CP.id = :pid"),
    @NamedQuery(name = "CondicaoPagamento.findAll", query = "SELECT CP FROM CondicaoPagamento AS CP ORDER BY CP.descricao ASC "),
    @NamedQuery(name = "CondicaoPagamento.findName", query = "SELECT CP FROM CondicaoPagamento AS CP WHERE CP.descricao = :pdescricao ORDER BY CP.descricao ASC ")
})
public class CondicaoPagamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, unique = true)
    private String descricao;

    public CondicaoPagamento() {
        this.id = -1;
        this.descricao = "";
    }

    public CondicaoPagamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
