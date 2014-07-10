package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_ROTINA",
        uniqueConstraints = @UniqueConstraint(columnNames = {"DS_ROTINA", "DS_NOME_PAGINA"})
)
@NamedQueries({
    @NamedQuery(name = "Rotina.findAll", query = "SELECT ROT FROM Rotina AS ROT ORDER BY ROT.rotina ASC, ROT.pagina ASC "),
    @NamedQuery(name = "Rotina.findByRotina", query = "SELECT ROT FROM Rotina AS ROT WHERE ROT.rotina LIKE :p1 ORDER BY ROT.rotina ASC, ROT.pagina ASC ")
})
public class Rotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_ROTINA", length = 50, nullable = false)
    private String rotina;
    @Column(name = "DS_NOME_PAGINA", length = 100, nullable = false)
    private String pagina;
    @Column(name = "DS_CLASSE", length = 100)
    private String classe;
    @Column(name = "IS_ATIVO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ativo;

    public Rotina() {
        this.id = -1;
        this.rotina = "";
        this.pagina = "";
        this.classe = "";
        this.ativo = false;
    }

    public Rotina(int id, String rotina, String pagina, String classe, boolean ativo) {
        this.id = id;
        this.rotina = rotina;
        this.pagina = pagina;
        this.classe = classe;
        this.ativo = ativo;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRotina() {
        return rotina;
    }

    public void setRotina(String rotina) {
        this.rotina = rotina;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
