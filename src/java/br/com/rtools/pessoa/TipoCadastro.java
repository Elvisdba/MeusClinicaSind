package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "PES_TIPO_CADASTRO")
@NamedQueries({
    @NamedQuery(name = "TipoCadastro.findAll", query = "SELECT TDOC FROM TipoCadastro AS TDOC ORDER BY TDOC.descricao ASC "),
    @NamedQuery(name = "TipoCadastro.findName", query = "SELECT TDOC FROM TipoCadastro AS TDOC WHERE UPPER(TDOC.descricao) LIKE :pdescricao ORDER BY TDOC.descricao ASC ")
})
public class TipoCadastro implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false, unique = true)
    private String descricao;

    public TipoCadastro() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoCadastro(int id, String descricao) {
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
        int hash = 3;
        hash = 67 * hash + this.id;
        hash = 67 * hash + Objects.hashCode(this.descricao);
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
        final TipoCadastro other = (TipoCadastro) obj;
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
        return "TipoCadastro{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
