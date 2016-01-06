package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "que_questao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_subgrupo", "ds_descricao"})
)
public class Questao implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_subgrupo", referencedColumnName = "id", nullable = false)
    @OneToOne
    private QuestionarioSubgrupo subgrupo;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;
    @Column(name = "nr_ordem", nullable = false, columnDefinition = "integer default 0")
    private Integer ordem;

    public Questao() {
        this.id = null;
        this.subgrupo = null;
        this.descricao = "";
        this.ordem = 0;
    }

    public Questao(Integer id, QuestionarioSubgrupo subgrupo, String descricao, Integer ordem) {
        this.id = id;
        this.subgrupo = subgrupo;
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
        final Questao other = (Questao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Questao{" + "id=" + id + ", subgrupo=" + subgrupo + ", descricao=" + descricao + ", ordem=" + ordem + '}';
    }

}
