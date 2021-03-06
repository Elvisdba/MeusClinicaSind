package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_tipo_endereco")
@NamedQueries({
    @NamedQuery(name = "TipoEndereco.findAll", query = "SELECT TEND FROM TipoEndereco AS TEND ORDER BY TEND.descricao ASC "),
    @NamedQuery(name = "TipoEndereco.findName", query = "SELECT TEND FROM TipoEndereco AS TEND WHERE UPPER(TEND.descricao) LIKE :pdescricao ORDER BY TEND.descricao ASC ")
})
public class TipoEndereco implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public TipoEndereco() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoEndereco(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
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
        int hash = 3;
        hash = 29 * hash + this.id;
        hash = 29 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final TipoEndereco other = (TipoEndereco) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoEndereco{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
