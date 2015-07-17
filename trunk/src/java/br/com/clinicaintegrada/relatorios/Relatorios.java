package br.com.clinicaintegrada.relatorios;

import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "sis_relatorios",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_nome", "id_rotina"})
)
@NamedQueries({
    @NamedQuery(name = "Relatorios.findAll", query = "SELECT R FROM Relatorios AS R ORDER BY R.nome ASC")
})
public class Relatorios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "ds_nome", length = 100, nullable = false)
    private String nome;
    @Column(name = "ds_jasper", length = 50, nullable = false)
    private String jasper;
    @Column(name = "ds_qry", length = 1000)
    private String qry;
    @Column(name = "is_por_folha", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean porFolha;
    @Column(name = "ds_nome_grupo", length = 100)
    private String nomeGrupo;
    @Column(name = "is_excel", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean excel;
    @Column(name = "ds_campos_excel", length = 255)
    private String camposExcel;

    public Relatorios() {
        this.id = null;
        this.rotina = new Rotina();
        this.nome = "";
        this.jasper = "";
        this.qry = "";
        this.porFolha = false;
        this.nomeGrupo = "";
        this.excel = false;
        this.camposExcel = "";
    }

    public Relatorios(Integer id, Rotina rotina, String nome, String jasper, String qry, Boolean porFolha, String nomeGrupo, Boolean excel, String camposExcel) {
        this.id = id;
        this.rotina = rotina;
        this.nome = nome;
        this.jasper = jasper;
        this.qry = qry;
        this.porFolha = porFolha;
        this.nomeGrupo = nomeGrupo;
        this.excel = excel;
        this.camposExcel = camposExcel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJasper() {
        return jasper;
    }

    public void setJasper(String jasper) {
        this.jasper = jasper;
    }

    public String getQry() {
        return qry;
    }

    public void setQry(String qry) {
        this.qry = qry;
    }

    public Boolean getPorFolha() {
        return porFolha;
    }

    public void setPorFolha(Boolean porFolha) {
        this.porFolha = porFolha;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public String getCamposExcel() {
        return camposExcel;
    }

    public void setCamposExcel(String camposExcel) {
        this.camposExcel = camposExcel;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.rotina);
        hash = 59 * hash + Objects.hashCode(this.nome);
        hash = 59 * hash + Objects.hashCode(this.jasper);
        hash = 59 * hash + Objects.hashCode(this.qry);
        hash = 59 * hash + Objects.hashCode(this.porFolha);
        hash = 59 * hash + Objects.hashCode(this.nomeGrupo);
        hash = 59 * hash + Objects.hashCode(this.excel);
        hash = 59 * hash + Objects.hashCode(this.camposExcel);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Relatorios other = (Relatorios) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.rotina, other.rotina)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.jasper, other.jasper)) {
            return false;
        }
        if (!Objects.equals(this.qry, other.qry)) {
            return false;
        }
        if (!Objects.equals(this.porFolha, other.porFolha)) {
            return false;
        }
        if (!Objects.equals(this.nomeGrupo, other.nomeGrupo)) {
            return false;
        }
        if (!Objects.equals(this.excel, other.excel)) {
            return false;
        }
        if (!Objects.equals(this.camposExcel, other.camposExcel)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Relatorios{" + "id=" + id + ", rotina=" + rotina + ", nome=" + nome + ", jasper=" + jasper + ", qry=" + qry + ", porFolha=" + porFolha + ", nomeGrupo=" + nomeGrupo + ", excel=" + excel + ", camposExcel=" + camposExcel + '}';
    }

}
