package br.com.clinicaintegrada.questionario;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "que_questao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_subgrupo", "ds_descricao"})
)
public class Questao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_subgrupo", referencedColumnName = "id", nullable = false)
    @OneToOne
    private QuestionarioSubgrupo subgrupo;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;

    public Questao() {
        this.id = null;
        this.subgrupo = null;
        this.descricao = "";
    }

    public Questao(Integer id, QuestionarioSubgrupo subgrupo, String descricao) {
        this.id = id;
        this.subgrupo = subgrupo;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public QuestionarioSubgrupo getSubgrupo() {
        return subgrupo;
    }

    public void setSubgrupo(QuestionarioSubgrupo subgrupo) {
        this.subgrupo = subgrupo;
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
        hash = 17 * hash + Objects.hashCode(this.subgrupo);
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
        final Questao other = (Questao) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.subgrupo, other.subgrupo)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Questao{" + "id=" + id + ", subgrupo=" + subgrupo + ", descricao=" + descricao + '}';
    }

}
