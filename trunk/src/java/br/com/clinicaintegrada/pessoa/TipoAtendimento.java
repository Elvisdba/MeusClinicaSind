package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "pes_tipo_atendimento")
@NamedQueries({
    @NamedQuery(name = "TipoAtendimento.findAll", query = "SELECT TA FROM TipoAtendimento AS TA ORDER BY TA.descricao ASC "),
    @NamedQuery(name = "TipoAtendimento.findName", query = "SELECT TA FROM TipoAtendimento TA WHERE UPPER(TA.descricao) LIKE :pdescricao ORDER BY TA.descricao ASC ")
})
public class TipoAtendimento implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public TipoAtendimento() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoAtendimento(int id, String descricao) {
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
        hash = 43 * hash + this.id;
        hash = 43 * hash + Objects.hashCode(this.descricao);
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
        final TipoAtendimento other = (TipoAtendimento) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoAtendimento{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
