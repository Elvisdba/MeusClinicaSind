package br.com.clinicaintegrada.agenda;

import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.TipoEndereco;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "age_agenda",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_grupo_agenda", "ds_nome"})
)
public class Agenda implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_grupo_agenda", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private GrupoAgenda grupoAgenda;
    @Column(name = "ds_nome", length = 100)
    private String nome;
    @Column(name = "ds_email", length = 500)
    private String email1;
    @Column(name = "ds_email2", length = 500)
    private String email2;
    @Column(name = "ds_obs", length = 8000)
    private String observacao;
    @Column(name = "ds_complemento", length = 50)
    private String complemento;
    @Column(name = "ds_numero", length = 30)
    private String numero;
    @JoinColumn(name = "id_tipo_endereco", referencedColumnName = "id")
    @ManyToOne
    private TipoEndereco tipoEndereco;
    @JoinColumn(name = "id_endereco", referencedColumnName = "id")
    @ManyToOne
    private Endereco endereco;

    public Agenda() {
        this.id = -1;
        this.cliente = new Cliente();
        this.pessoa = new Pessoa();
        this.grupoAgenda = new GrupoAgenda();
        this.nome = "";
        this.email1 = "";
        this.email2 = "";
        this.observacao = "";
        this.complemento = "";
        this.numero = "";
        this.tipoEndereco = new TipoEndereco();
        this.endereco = new Endereco();
    }

    public Agenda(int id, Cliente cliente, Pessoa pessoa, GrupoAgenda grupoAgenda, String nome, String email1, String email2, String observacao, String complemento, String numero, TipoEndereco tipoEndereco, Endereco endereco) {
        this.id = id;
        this.cliente = cliente;
        this.pessoa = pessoa;
        this.grupoAgenda = grupoAgenda;
        this.nome = nome;
        this.email1 = email1;
        this.email2 = email2;
        this.observacao = observacao;
        this.complemento = complemento;
        this.numero = numero;
        this.tipoEndereco = tipoEndereco;
        this.endereco = endereco;
    }

    @Override
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

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.cliente);
        hash = 97 * hash + Objects.hashCode(this.pessoa);
        hash = 97 * hash + Objects.hashCode(this.grupoAgenda);
        hash = 97 * hash + Objects.hashCode(this.nome);
        hash = 97 * hash + Objects.hashCode(this.email1);
        hash = 97 * hash + Objects.hashCode(this.email2);
        hash = 97 * hash + Objects.hashCode(this.observacao);
        hash = 97 * hash + Objects.hashCode(this.complemento);
        hash = 97 * hash + Objects.hashCode(this.numero);
        hash = 97 * hash + Objects.hashCode(this.tipoEndereco);
        hash = 97 * hash + Objects.hashCode(this.endereco);
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
        final Agenda other = (Agenda) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.pessoa, other.pessoa)) {
            return false;
        }
        if (!Objects.equals(this.grupoAgenda, other.grupoAgenda)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.email1, other.email1)) {
            return false;
        }
        if (!Objects.equals(this.email2, other.email2)) {
            return false;
        }
        if (!Objects.equals(this.observacao, other.observacao)) {
            return false;
        }
        if (!Objects.equals(this.complemento, other.complemento)) {
            return false;
        }
        if (!Objects.equals(this.numero, other.numero)) {
            return false;
        }
        if (!Objects.equals(this.tipoEndereco, other.tipoEndereco)) {
            return false;
        }
        if (!Objects.equals(this.endereco, other.endereco)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Agenda{" + "id=" + id + ", cliente=" + cliente + ", pessoa=" + pessoa + ", grupoAgenda=" + grupoAgenda + ", nome=" + nome + ", email1=" + email1 + ", email2=" + email2 + ", observacao=" + observacao + ", complemento=" + complemento + ", numero=" + numero + ", tipoEndereco=" + tipoEndereco + ", endereco=" + endereco + '}';
    }

}
