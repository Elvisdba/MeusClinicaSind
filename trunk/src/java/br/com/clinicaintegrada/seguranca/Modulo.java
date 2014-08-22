package br.com.clinicaintegrada.seguranca;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_modulo")
@NamedQueries({
    @NamedQuery(name = "Modulo.findAll", query = "SELECT MODU FROM Modulo AS MODU ORDER BY MODU.descricao ASC"),
    @NamedQuery(name = "Modulo.findName", query = "SELECT MODU FROM Modulo AS MODU WHERE UPPER(MODU.descricao) LIKE :pdescricao ORDER BY MODU.descricao ASC ")
})
public class Modulo implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public Modulo() {
        this.id = -1;
        this.descricao = "";
    }

    public Modulo(int id, String descricao) {
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
}
