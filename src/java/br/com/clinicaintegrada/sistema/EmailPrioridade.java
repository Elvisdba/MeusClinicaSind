package br.com.clinicaintegrada.sistema;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_email_prioridade")
public class EmailPrioridade implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 15, unique = true)
    private String descricao;

    public EmailPrioridade() {
        this.id = -1;
        this.descricao = "";
    }

    public EmailPrioridade(Integer id, String descricao) {
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
