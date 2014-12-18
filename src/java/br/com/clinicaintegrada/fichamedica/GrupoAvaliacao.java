package br.com.clinicaintegrada.fichamedica;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_grupo_avaliacao")
@NamedQueries({
    @NamedQuery(name = "GrupoAvaliacao.findAll", query = "SELECT GA FROM GrupoAvaliacao AS GA ORDER BY GA.descricao ASC "),
    @NamedQuery(name = "GrupoAvaliacao.findName", query = "SELECT GA FROM GrupoAvaliacao GA WHERE UPPER(GA.descricao) LIKE :pdescricao ORDER BY GA.descricao ASC ")
})
public class GrupoAvaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public GrupoAvaliacao() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoAvaliacao(int id, String descricao) {
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
        final GrupoAvaliacao other = (GrupoAvaliacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "GrupoAvaliacao{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
