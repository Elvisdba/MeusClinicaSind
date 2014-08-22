package br.com.rtools.endereco;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "end_bairro")
@NamedQueries({
    @NamedQuery(name = "Bairro.pesquisaID", query = "SELECT BAI FROM Bairro AS BAI WHERE BAI.id = :pid"),
    @NamedQuery(name = "Bairro.findAll", query = "SELECT BAI FROM Bairro AS BAI ORDER BY BAI.descricao ASC "),
    @NamedQuery(name = "Bairro.findName", query = "SELECT BAI FROM Bairro AS BAI WHERE UPPER(BAI.descricao) LIKE :pdescricao ORDER BY BAI.descricao ASC ")
})
public class Bairro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 70, nullable = false, unique = true)
    private String descricao;

    public Bairro() {
        this.id = -1;
        this.descricao = "";
    }

    public Bairro(int id, String descricao) {
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