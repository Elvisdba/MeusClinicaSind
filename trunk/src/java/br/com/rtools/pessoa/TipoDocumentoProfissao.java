package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_tipo_documento_profissao")
@NamedQuery(name = "TipoDocumentoProfissao.findAll", query = "SELECT TDP FROM TipoDocumentoProfissao AS TDP ORDER BY TDP.descricao ASC ")
public class TipoDocumentoProfissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_profissao", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne(fetch = FetchType.EAGER)
    private Profissao profissao;
    @Column(name = "ds_descricao", length = 20, nullable = false)
    private String descricao;
    @Column(name = "ds_mascara", length = 20)
    private String mascara;

    public TipoDocumentoProfissao() {
        this.id = -1;
        this.profissao = new Profissao();
        this.descricao = "";
        this.mascara = "";
    }

    public TipoDocumentoProfissao(int id, Profissao profissao, String descricao, String mascara) {
        this.id = id;
        this.profissao = profissao;
        this.descricao = descricao;
        this.mascara = mascara;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

}
