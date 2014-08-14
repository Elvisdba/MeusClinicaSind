package br.com.rtools.pessoa;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_equipe",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_funcao_equipe", "id_pessoa", "ds_documento"})
)
public class Equipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_funcao_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private FuncaoEquipe funcaoEquipe;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_cadastro")
    private Date cadastro;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Column(name = "ds_documento", length = 50, nullable = false)
    private String documento;
    @Column(name = "is_ativo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ativo;

    public Equipe() {
        this.id = -1;
        this.cliente = new Cliente();
        this.funcaoEquipe = new FuncaoEquipe();
        this.cadastro = new Date();
        this.pessoa = new Pessoa();
        this.documento = "";
        this.ativo = false;
    }

    public Equipe(int id, Cliente cliente, FuncaoEquipe funcaoEquipe, Date cadastro, Pessoa pessoa, String documento, boolean ativo) {
        this.id = id;
        this.cliente = cliente;
        this.funcaoEquipe = funcaoEquipe;
        this.cadastro = cadastro;
        this.pessoa = pessoa;
        this.documento = documento;
        this.ativo = ativo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FuncaoEquipe getFuncaoEquipe() {
        return funcaoEquipe;
    }

    public void setFuncaoEquipe(FuncaoEquipe funcaoEquipe) {
        this.funcaoEquipe = funcaoEquipe;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void selectedCadastro(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.cadastro = DataHoje.converte(format.format(event.getObject()));
    }

}
