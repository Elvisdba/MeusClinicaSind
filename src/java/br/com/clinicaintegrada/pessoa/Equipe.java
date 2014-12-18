package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.DataHoje;
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
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_funcao_equipe", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private FuncaoEquipe funcaoEquipe;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_cadastro")
    private Date dataCadastro;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Column(name = "ds_documento", length = 50, nullable = false)
    private String documento;
    @Column(name = "is_ativo", columnDefinition = "boolean default false")
    private Boolean ativo;

    public Equipe() {
        this.id = -1;
        this.cliente = new Cliente();
        this.funcaoEquipe = new FuncaoEquipe();
        this.dataCadastro = new Date();
        this.pessoa = new Pessoa();
        this.documento = "";
        this.ativo = false;
    }

    public Equipe(Integer id, Cliente cliente, FuncaoEquipe funcaoEquipe, Date dataCadastro, Pessoa pessoa, String documento, Boolean ativo) {
        this.id = id;
        this.cliente = cliente;
        this.funcaoEquipe = funcaoEquipe;
        this.dataCadastro = dataCadastro;
        this.pessoa = pessoa;
        this.documento = documento;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void selectedDataCadastro(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataCadastro = DataHoje.converte(format.format(event.getObject()));
    }

}
