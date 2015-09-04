package br.com.clinicaintegrada.questionario;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "que_subgrupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_grupo", "ds_descricao"})
)
public class QuestionarioSubgrupo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", nullable = false)
    @OneToOne
    private QuestionarioGrupo grupo;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;

    public QuestionarioSubgrupo() {
        this.id = null;
        this.grupo = new QuestionarioGrupo();
        this.descricao = "";
    }

    public QuestionarioSubgrupo(Integer id, QuestionarioGrupo grupo, String descricao) {
        this.id = id;
        this.grupo = grupo;
        this.descricao = descricao;
    }

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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.grupo);
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
        final QuestionarioSubgrupo other = (QuestionarioSubgrupo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.grupo, other.grupo)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "QuestionarioSubgrupo{" + "id=" + id + ", grupo=" + grupo + ", descricao=" + descricao + '}';
    }

}
