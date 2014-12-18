package br.com.clinicaintegrada.fichamedica;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_tipos_avaliacao")
@NamedQueries({
    @NamedQuery(name = "TiposAvaliacao.findAll", query = "SELECT TA FROM TiposAvaliacao AS TA ORDER BY TA.descricao ASC "),
    @NamedQuery(name = "TiposAvaliacao.findName", query = "SELECT TA FROM TiposAvaliacao TA WHERE UPPER(TA.descricao) LIKE :pdescricao ORDER BY TA.descricao ASC ")
})
public class TiposAvaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true, updatable = true)
    private String descricao;

    public TiposAvaliacao() {
        this.id = -1;
        this.descricao = "";
    }

    public TiposAvaliacao(Integer id, String descricao) {
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
        final TiposAvaliacao other = (TiposAvaliacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "GrupoAvaliacao{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
