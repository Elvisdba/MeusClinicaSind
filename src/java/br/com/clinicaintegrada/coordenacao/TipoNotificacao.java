package br.com.clinicaintegrada.coordenacao;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_tipo_notificacao")
@NamedQueries({
    @NamedQuery(name = "TipoNotificacao.findAll", query = "SELECT TN FROM TipoNotificacao AS TN ORDER BY TN.descricao ASC "),
    @NamedQuery(name = "TipoNotificacao.findName", query = "SELECT TN FROM TipoNotificacao AS TN WHERE UPPER(TN.descricao) LIKE :pdescricao ORDER BY TN.descricao ASC ")
})
public class TipoNotificacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true, unique = true)
    private String descricao;

    public TipoNotificacao() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoNotificacao(Integer id, String descricao) {
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
