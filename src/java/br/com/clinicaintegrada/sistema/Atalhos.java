package br.com.clinicaintegrada.sistema;

import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SIS_ATALHOS")
public class Atalhos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "DS_SIGLA", nullable = false)
    private String sigla;

    public Atalhos() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.rotina = new Rotina();
        this.sigla = "";
    }

    public Atalhos(int id, Pessoa pessoa, Rotina rotina, String sigla) {
        this.id = id;
        this.pessoa = pessoa;
        this.rotina = rotina;
        this.sigla = sigla;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
