package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "que_grupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_questionario", "ds_descricao"})
)
public class QuestionarioGrupo implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_questionario", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Questionario questionario;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;
    @Column(name = "nr_ordem", nullable = false, columnDefinition = "integer default 0")
    private Integer ordem;

    public QuestionarioGrupo() {
        this.id = null;
        this.questionario = null;
        this.descricao = "";
        this.ordem = 0;
    }

    public QuestionarioGrupo(Integer id, Questionario questionario, String descricao, Integer ordem) {
        this.id = id;
        this.questionario = questionario;
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

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
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
        int hash = 5;
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
        final QuestionarioGrupo other = (QuestionarioGrupo) obj;
        return true;
    }

    @Override
    public String toString() {
        return "QuestionarioGrupo{" + "id=" + id + ", questionario=" + questionario + ", descricao=" + descricao + ", ordem=" + ordem + '}';
    }

}
