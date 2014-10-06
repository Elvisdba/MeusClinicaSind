package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fin_tipo_servico")
@NamedQueries({
    @NamedQuery(name = "TipoServico.pesquisaID", query = "SELECT T FROM TipoServico AS T WHERE T.id = :pid"),
    @NamedQuery(name = "TipoServico.findAll", query = "SELECT T FROM TipoServico AS T ORDER BY T.descricao ASC "),
    @NamedQuery(name = "TipoServico.findName", query = "SELECT T FROM TipoServico AS T WHERE UPPER(T.descricao) LIKE :pdescricao ORDER BY T.descricao ASC ")
})
public class TipoServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = true, unique = true)
    private String descricao;

    public TipoServico() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoServico(int id, String descricao) {
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
