package br.com.clinicaintegrada.questionario;

import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "que_questionario",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_rotina", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "Questionario.findAll", query = "SELECT Q FROM Questionario AS Q ORDER BY Q.rotina.rotina ASC, Q.descricao ASC ")
})
public class Questionario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Rotina rotina;
    @Column(name = "ds_descricao", length = 255, nullable = false)
    private String descricao;

    public Questionario() {
        this.id = null;
        this.rotina = null;
        this.descricao = "";
    }

    public Questionario(Integer id, Rotina rotina, String descricao) {
        this.id = id;
        this.rotina = rotina;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
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
        hash = 17 * hash + Objects.hashCode(this.rotina);
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
        final Questionario other = (Questionario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.rotina, other.rotina)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Questionario{" + "id=" + id + ", rotina=" + rotina + ", descricao=" + descricao + '}';
    }

}
