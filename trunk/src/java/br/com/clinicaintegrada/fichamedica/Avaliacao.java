package br.com.clinicaintegrada.fichamedica;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_avaliacao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo_avaliacao", "id_tipo_avaliacao"})
)
@NamedQuery(name = "findAll", query = "SELECT A FROM Avaliacao AS A ORDER BY A.grupoAvaliacao.descricao, A.tiposAvaliacao.descricao ")
public class Avaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_grupo_avaliacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private GrupoAvaliacao grupoAvaliacao;
    @JoinColumn(name = "id_tipo_avaliacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private TiposAvaliacao tiposAvaliacao;
    @Column(name = "is_historico", columnDefinition = "boolean default false")
    private Boolean historico;

    public Avaliacao() {
        this.id = -1;
        this.grupoAvaliacao = null;
        this.tiposAvaliacao = null;
        this.historico = false;
    }

    public Avaliacao(Integer id, GrupoAvaliacao grupoAvaliacao, TiposAvaliacao tiposAvaliacao, Boolean historico) {
        this.id = id;
        this.grupoAvaliacao = grupoAvaliacao;
        this.tiposAvaliacao = tiposAvaliacao;
        this.historico = historico;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GrupoAvaliacao getGrupoAvaliacao() {
        return grupoAvaliacao;
    }

    public void setGrupoAvaliacao(GrupoAvaliacao grupoAvaliacao) {
        this.grupoAvaliacao = grupoAvaliacao;
    }

    public TiposAvaliacao getTiposAvaliacao() {
        return tiposAvaliacao;
    }

    public void setTiposAvaliacao(TiposAvaliacao tiposAvaliacao) {
        this.tiposAvaliacao = tiposAvaliacao;
    }

    public Boolean getHistorico() {
        return historico;
    }

    public void setHistorico(Boolean historico) {
        this.historico = historico;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Avaliacao other = (Avaliacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Avaliacao{" + "id=" + id + ", grupoAvaliacao=" + grupoAvaliacao + ", tiposAvaliacao=" + tiposAvaliacao + ", historico=" + historico + '}';
    }

}
