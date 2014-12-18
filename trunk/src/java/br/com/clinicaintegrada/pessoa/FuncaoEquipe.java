package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "pes_funcao_equipe",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_tipo_atendimento", "id_tipo_documento_profissao", "id_profissao", "id_cliente"})
)
@NamedQuery(name = "FuncaoEquipe.findAll", query = "SELECT FE FROM FuncaoEquipe AS FE ORDER BY FE.profissao.profissao ASC, FE.tipoDocumentoProfissao.descricao ASC")
public class FuncaoEquipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_tipo_atendimento", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER)
    private TipoAtendimento tipoAtendimento;
    @JoinColumn(name = "id_tipo_documento_profissao", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private TipoDocumentoProfissao tipoDocumentoProfissao;
    @JoinColumn(name = "id_profissao", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Profissao profissao;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;

    public FuncaoEquipe() {
        this.id = -1;
        this.tipoAtendimento = new TipoAtendimento();
        this.tipoDocumentoProfissao = new TipoDocumentoProfissao();
        this.profissao = new Profissao();
        this.cliente = new Cliente();
    }

    public FuncaoEquipe(Integer id, TipoAtendimento tipoAtendimento, TipoDocumentoProfissao tipoDocumentoProfissao, Profissao profissao, Cliente cliente) {
        this.id = id;
        this.tipoAtendimento = tipoAtendimento;
        this.tipoDocumentoProfissao = tipoDocumentoProfissao;
        this.profissao = profissao;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TipoAtendimento getTipoAtendimento() {
        return tipoAtendimento;
    }

    public void setTipoAtendimento(TipoAtendimento tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public TipoDocumentoProfissao getTipoDocumentoProfissao() {
        return tipoDocumentoProfissao;
    }

    public void setTipoDocumentoProfissao(TipoDocumentoProfissao tipoDocumentoProfissao) {
        this.tipoDocumentoProfissao = tipoDocumentoProfissao;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.tipoAtendimento);
        hash = 97 * hash + Objects.hashCode(this.tipoDocumentoProfissao);
        hash = 97 * hash + Objects.hashCode(this.profissao);
        hash = 97 * hash + Objects.hashCode(this.getCliente());
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
        final FuncaoEquipe other = (FuncaoEquipe) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.tipoAtendimento, other.tipoAtendimento)) {
            return false;
        }
        if (!Objects.equals(this.tipoDocumentoProfissao, other.tipoDocumentoProfissao)) {
            return false;
        }
        if (!Objects.equals(this.profissao, other.profissao)) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        return true;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
