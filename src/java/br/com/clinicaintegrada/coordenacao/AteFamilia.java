package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_familia",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_equipe"})
)
public class AteFamilia implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe equipe;
    @Column(name = "nr_ordem")
    private Integer ordem;
    @Column(name = "is_ponteiro", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean ponteiro;

    public AteFamilia() {
        this.id = null;
        this.equipe = null;
        this.ordem = null;
        this.ponteiro = false;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Boolean getPonteiro() {
        return ponteiro;
    }

    public void setPonteiro(Boolean ponteiro) {
        this.ponteiro = ponteiro;
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
        final AteFamilia other = (AteFamilia) obj;
        return true;
    }

    @Override
    public String toString() {
        return "AteFamilia{" + "id=" + id + ", equipe=" + equipe + ", ordem=" + ordem + ", ponteiro=" + ponteiro + '}';
    }

}
