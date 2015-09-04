package br.com.clinicaintegrada.questionario;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "que_grupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_questionario", "ds_descricao"})
)
public class QuestionarioGrupo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_questionario", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Questionario questionario;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;

    public QuestionarioGrupo() {
        this.id = null;
        this.questionario = null;
        this.descricao = "";
    }

    public QuestionarioGrupo(Integer id, Questionario questionario, String descricao) {
        this.id = id;
        this.questionario = questionario;
        this.descricao = descricao;
    }

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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.questionario);
        hash = 17 * hash + Objects.hashCode(this.descricao);
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
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.questionario, other.questionario)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QuestionarioGrupo{" + "id=" + id + ", questionario=" + questionario + ", descricao=" + descricao + '}';
    }

}
