package br.com.clinicaintegrada.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_grau_parentesco")
@NamedQueries({
    @NamedQuery(name = "GrauParentesco.findAll", query = "SELECT GP FROM GrauParentesco AS GP ORDER BY GP.descricao ASC "),
    @NamedQuery(name = "GrauParentesco.findName", query = "SELECT GP FROM GrauParentesco AS GP WHERE UPPER(GP.descricao) LIKE :pdescricao ORDER BY GP.descricao ASC ")
})
public class GrauParentesco implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public GrauParentesco() {
        this.id = -1;
        this.descricao = "";
    }

    public GrauParentesco(Integer id, String descricao) {
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
}
