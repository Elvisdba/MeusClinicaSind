package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_profissao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_profissao", "ds_cbo"})
)
@NamedQueries({
    @NamedQuery(name = "Profissao.findAll", query = "SELECT P FROM Profissao AS P ORDER BY P.profissao ASC "),
    @NamedQuery(name = "Profissao.findName", query = "SELECT P FROM Profissao AS P WHERE UPPER(P.profissao) LIKE :pdescricao ORDER BY P.profissao ASC ")
})
public class Profissao implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ds_profissao", length = 200, nullable = false)
    private String profissao;
    @Column(name = "ds_cbo", length = 10, nullable = true)
    private String cbo;

    public Profissao() {
        this.id = -1;
        this.profissao = "";
        this.cbo = "";
    }

    public Profissao(int id, String profissao, String cbo) {
        this.id = id;
        this.profissao = profissao;
        this.profissao = cbo;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCbo() {
        return cbo;
    }

    public void setCbo(String cbo) {
        this.cbo = cbo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + (this.profissao != null ? this.profissao.hashCode() : 0);
        hash = 97 * hash + (this.cbo != null ? this.cbo.hashCode() : 0);
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
        final Profissao other = (Profissao) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.profissao == null) ? (other.profissao != null) : !this.profissao.equals(other.profissao)) {
            return false;
        }
        if ((this.cbo == null) ? (other.cbo != null) : !this.cbo.equals(other.cbo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Profissao{" + "id=" + id + ", profissao=" + profissao + ", cbo=" + cbo + '}';
    }

}
