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
@Table(name = "fin_tipo_documento")
@NamedQueries({
    @NamedQuery(name = "FTipoDocumento.pesquisaID", query = "SELECT FTP FROM FTipoDocumento AS FTP WHERE FTP.id = :pid"),
    @NamedQuery(name = "FTipoDocumento.findAll", query = "SELECT FTP FROM FTipoDocumento AS FTP ORDER BY FTP.descricao ASC "),
    @NamedQuery(name = "FTipoDocumento.findName", query = "SELECT FTP FROM FTipoDocumento AS FTP WHERE FTP.descricao = :pdescricao ORDER BY FTP.descricao ASC ")
})
public class FTipoDocumento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = false)
    private String descricao;

    public FTipoDocumento() {
        this.id = -1;
        this.descricao = "";
    }

    public FTipoDocumento(int id, String descricao) {
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
