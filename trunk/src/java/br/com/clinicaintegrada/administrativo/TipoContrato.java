package br.com.clinicaintegrada.administrativo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ctr_tipo_contrato")
@NamedQueries({
    @NamedQuery(name = "TipoContrato.findAll", query = "SELECT TC FROM TipoContrato AS TC ORDER BY TC.descricao ASC "),
    @NamedQuery(name = "TipoContrato.findName", query = "SELECT TC FROM TipoContrato AS TC WHERE UPPER(TC.descricao) LIKE :pdescricao ORDER BY TC.descricao ASC ")
})

public class TipoContrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public TipoContrato() {
        this.id = null;
        this.descricao = "";
    }

    public TipoContrato(Integer id, String descricao) {
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
