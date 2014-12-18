package br.com.clinicaintegrada.endereco;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "end_logradouro")
@NamedQueries({
    @NamedQuery(name = "Logradouro.findAll", query = "SELECT LOGR FROM Logradouro AS LOGR ORDER BY LOGR.descricao ASC "),
    @NamedQuery(name = "Logradouro.findName", query = "SELECT LOGR FROM Logradouro AS LOGR WHERE UPPER(LOGR.descricao) LIKE :pdescricao ORDER BY LOGR.descricao ASC ")
})
public class Logradouro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public Logradouro() {
        this.id = -1;
        this.descricao = "";
    }

    public Logradouro(Integer id, String descricao) {
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
