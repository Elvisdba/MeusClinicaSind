package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_grupo_evento")
@NamedQueries({
    @NamedQuery(name = "GrupoEvento.findAll", query = "SELECT GE FROM GrupoEvento AS GE ORDER BY GE.descricao ASC "),
    @NamedQuery(name = "GrupoEvento.findName", query = "SELECT GE FROM GrupoEvento GE WHERE UPPER(GE.descricao) LIKE :pdescricao ORDER BY GE.descricao ASC ")
})
public class GrupoEvento implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public GrupoEvento() {
        this.id = -1;
        this.descricao = "";
    }

    public GrupoEvento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
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
        final GrupoEvento other = (GrupoEvento) obj;
        return true;
    }

    @Override
    public String toString() {
        return "GrupoEvento{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
