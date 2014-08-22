package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_dependente",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_pessoa", "id_grau", "ds_nome"})
)
public class Dependente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_grau", referencedColumnName = "id", nullable = false)
    @OneToOne
    private GrauParentesco grauParentesco;
    @Column(name = "ds_nome", length = 30, nullable = true)
    private String nome;
    @Column(name = "ds_sexo", length = 30, nullable = true)
    private String sexo;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_nascimento")
    private Date nascimento;

    public Dependente() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.grauParentesco = new GrauParentesco();
        this.nome = "";
        this.sexo = "";
        this.nascimento = new Date();
    }

    public Dependente(int id, Pessoa pessoa, GrauParentesco grauParentesco, String nome, String sexo, Date nascimento) {
        this.id = id;
        this.pessoa = pessoa;
        this.grauParentesco = grauParentesco;
        this.nome = nome;
        this.sexo = sexo;
        this.nascimento = nascimento;
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

    public GrauParentesco getGrauParentesco() {
        return grauParentesco;
    }

    public void setGrauParentesco(GrauParentesco grauParentesco) {
        this.grauParentesco = grauParentesco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getNascimentoString() {
        return DataHoje.converteData(nascimento);
    }

    public void setNascimentoString(String nascimentoString) {
        this.nascimento = DataHoje.converte(nascimentoString);
    }

    public void selectedNascimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.nascimento = DataHoje.converte(format.format(event.getObject()));
    }
}
