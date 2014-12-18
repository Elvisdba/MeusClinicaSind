package br.com.clinicaintegrada.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_rotina",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_rotina", "ds_nome_pagina"})
)
@NamedQueries({
    @NamedQuery(name = "Rotina.findAll", query = "SELECT ROT FROM Rotina AS ROT ORDER BY ROT.rotina ASC, ROT.pagina ASC "),
    @NamedQuery(name = "Rotina.findByRotina", query = "SELECT ROT FROM Rotina AS ROT WHERE UPPER(ROT.rotina) LIKE :p1 ORDER BY ROT.rotina ASC, ROT.pagina ASC ")
})
public class Rotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_rotina", length = 50, nullable = false)
    private String rotina;
    @Column(name = "ds_nome_pagina", length = 100, nullable = false)
    private String pagina;
    @Column(name = "ds_classe", length = 100)
    private String classe;
    @Column(name = "is_ativo", columnDefinition = "boolean default false")
    private Boolean ativo;

    public Rotina() {
        this.id = -1;
        this.rotina = "";
        this.pagina = "";
        this.classe = "";
        this.ativo = false;
    }

    public Rotina(Integer id, String rotina, String pagina, String classe, Boolean ativo) {
        this.id = id;
        this.rotina = rotina;
        this.pagina = pagina;
        this.classe = classe;
        this.ativo = ativo;

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRotina() {
        return rotina;
    }

    public void setRotina(String rotina) {
        this.rotina = rotina;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
