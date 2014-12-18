package br.com.clinicaintegrada.administrativo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ctr_tipo_internacao")
@NamedQueries({
    @NamedQuery(name = "TipoInternacao.findAll", query = "SELECT TI FROM TipoInternacao AS TI ORDER BY TI.descricao ASC "),
    @NamedQuery(name = "TipoInternacao.findName", query = "SELECT TI FROM TipoInternacao AS TI WHERE UPPER(TI.descricao) LIKE :pdescricao ORDER BY TI.descricao ASC ")
})
public class TipoInternacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public TipoInternacao() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoInternacao(Integer id, String descricao) {
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
