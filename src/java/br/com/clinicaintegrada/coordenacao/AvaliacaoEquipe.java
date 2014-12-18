package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_avaliacao_equipe",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_avaliacao", "id_funcao_equipe"})
)
public class AvaliacaoEquipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_avaliacao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Avaliacao avaliacao;
    @JoinColumn(name = "id_funcao_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne
    private FuncaoEquipe funcaoEquipe;

    public AvaliacaoEquipe() {
        this.id = -1;
        this.avaliacao = null;
        this.funcaoEquipe = null;
    }

    public AvaliacaoEquipe(Integer id, Avaliacao avaliacao, FuncaoEquipe funcaoEquipe) {
        this.id = id;
        this.avaliacao = avaliacao;
        this.funcaoEquipe = funcaoEquipe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    public FuncaoEquipe getFuncaoEquipe() {
        return funcaoEquipe;
    }

    public void setFuncaoEquipe(FuncaoEquipe funcaoEquipe) {
        this.funcaoEquipe = funcaoEquipe;
    }
}
