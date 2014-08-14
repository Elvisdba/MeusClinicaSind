package br.com.rtools.sistema;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "sis_combustivel")
@NamedQueries({
    @NamedQuery(name = "Combustivel.findAll", query = "SELECT TC FROM Combustivel AS TC ORDER BY TC.descricao ASC "),
    @NamedQuery(name = "Combustivel.findName", query = "SELECT TC FROM Combustivel AS TC WHERE UPPER(TC.descricao) LIKE :pdescricao ORDER BY TC.descricao ASC ")
})
public class Combustivel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 15, unique = true)
    private String descricao;

    public Combustivel() {
        this.id = -1;
        this.descricao = "";
    }

    public Combustivel(int id, String descricao) {
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
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.descricao);
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
        final Combustivel other = (Combustivel) obj;
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
        return "Combustivel{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
