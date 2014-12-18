package br.com.clinicaintegrada.fichamedica;

import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_grupo_avaliacao_funcao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo_avaliacao", "id_funcao_equipe"})
)
public class GrupoAvaliacaoFuncao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_grupo_avaliacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private GrupoAvaliacao grupoAvaliacao;
    @JoinColumn(name = "id_funcao_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne
    private FuncaoEquipe funcaoEquipe;

    public GrupoAvaliacaoFuncao() {
        this.id = null;
        this.grupoAvaliacao = null;
        this.funcaoEquipe = null;
    }

    public GrupoAvaliacaoFuncao(Integer id, GrupoAvaliacao grupoAvaliacao, FuncaoEquipe funcaoEquipe) {
        this.id = id;
        this.grupoAvaliacao = grupoAvaliacao;
        this.funcaoEquipe = funcaoEquipe;
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

    public FuncaoEquipe getFuncaoEquipe() {
        return funcaoEquipe;
    }

    public void setFuncaoEquipe(FuncaoEquipe funcaoEquipe) {
        this.funcaoEquipe = funcaoEquipe;
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
        final GrupoAvaliacaoFuncao other = (GrupoAvaliacaoFuncao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "GrupoAvaliacaoFuncao{" + "id=" + id + ", grupoAvaliacao=" + grupoAvaliacao + ", funcaoEquipe=" + funcaoEquipe + '}';
    }

}
