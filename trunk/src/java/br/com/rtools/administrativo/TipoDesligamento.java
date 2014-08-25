package br.com.clinicaintegrada.administrativo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ctr_tipo_desligamento")
@NamedQueries({
    @NamedQuery(name = "TipoDesligamento.findAll", query = "SELECT TD FROM TipoDesligamento AS TD ORDER BY TD.descricao ASC "),
    @NamedQuery(name = "TipoDesligamento.findName", query = "SELECT TD FROM TipoDesligamento AS TD WHERE UPPER(TD.descricao) LIKE :pdescricao ORDER BY TD.descricao ASC ")
})

public class TipoDesligamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public TipoDesligamento() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoDesligamento(int id, String descricao) {
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
}
