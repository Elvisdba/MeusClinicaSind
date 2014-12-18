package br.com.clinicaintegrada.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_pessoa_profissao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_fisica", "id_profissao"})
)
public class PessoaProfissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_fisica", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Fisica fisica;
    @JoinColumn(name = "id_profissao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Profissao profissao;

    public PessoaProfissao() {
        this.id = -1;
        this.fisica = new Fisica();
        this.profissao = new Profissao();
    }

    public PessoaProfissao(Integer id, Fisica fisica, Profissao profissao) {
        this.id = id;
        this.fisica = fisica;
        this.profissao = profissao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }
}
