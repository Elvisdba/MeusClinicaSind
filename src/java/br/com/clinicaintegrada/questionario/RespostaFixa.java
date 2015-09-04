package br.com.clinicaintegrada.questionario;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "que_resposta_fixa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_questao", "ds_descricao"})
)
public class RespostaFixa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_questao", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Questao questao;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;

    public RespostaFixa() {
        this.id = null;
        this.questao = null;
        this.descricao = "";
    }

    public RespostaFixa(Integer id, Questao questao, String descricao) {
        this.id = id;
        this.questao = questao;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final RespostaFixa other = (RespostaFixa) obj;
        return true;
    }

    @Override
    public String toString() {
        return "RespostaFixa{" + "id=" + id + ", questao=" + questao + ", descricao=" + descricao + '}';
    }

}
