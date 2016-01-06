package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "que_subgrupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo", "ds_descricao"})
)
public class QuestionarioSubgrupo implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", nullable = false)
    @OneToOne
    private QuestionarioGrupo grupo;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;
    @Column(name = "nr_ordem", nullable = false, columnDefinition = "integer default 0")
    private Integer ordem;

    public QuestionarioSubgrupo() {
        this.id = null;
        this.grupo = new QuestionarioGrupo();
        this.descricao = "";
        this.ordem = 0;
    }

    public QuestionarioSubgrupo(Integer id, QuestionarioGrupo grupo, String descricao, Integer ordem) {
        this.id = id;
        this.grupo = grupo;
        this.descricao = descricao;
        this.ordem = ordem;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestionarioGrupo getGrupo() {
        return grupo;
    }

    public void setGrupo(QuestionarioGrupo grupo) {
        this.grupo = grupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
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
        final QuestionarioSubgrupo other = (QuestionarioSubgrupo) obj;
        return true;
    }

    @Override
    public String toString() {
        return "QuestionarioSubgrupo{" + "id=" + id + ", grupo=" + grupo + ", descricao=" + descricao + ", ordem=" + ordem + '}';
    }

}
