package br.com.clinicaintegrada.seguranca;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_nivel")
@NamedQueries({
    @NamedQuery(name = "Nivel.findAll", query = "SELECT NIV FROM Nivel NIV ORDER BY NIV.descricao ASC "),
    @NamedQuery(name = "Nivel.findName", query = "SELECT NIV FROM Nivel NIV WHERE UPPER(NIV.descricao) LIKE :pdescricao ORDER BY NIV.descricao ASC ")
})
public class Nivel implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public Nivel() {
        this.id = -1;
        this.descricao = "";
    }

    public Nivel(Integer id, String descricao) {
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
}
