package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "PES_PESSOA_PROFISSAO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_FISICA", "ID_PROFISSAO"})
)
public class PessoaProfissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_FISICA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Fisica fisica;
    @JoinColumn(name = "ID_PROFISSAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Profissao profissao;

    public PessoaProfissao() {
        this.id = -1;
        this.fisica = new Fisica();
        this.profissao = new Profissao();
    }

    public PessoaProfissao(int id, Fisica fisica, Profissao profissao) {
        this.id = id;
        this.fisica = fisica;
        this.profissao = profissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
