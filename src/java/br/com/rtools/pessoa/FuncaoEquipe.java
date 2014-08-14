package br.com.rtools.pessoa;

import br.com.rtools.seguranca.Cliente;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "pes_equipe_funcao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_tipo_atendimento", "id_tipo_documento_profissao", "id_profissao"})
)
public class FuncaoEquipe implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_tipo_atendimento", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private TipoAtendimento tipoAtendimento;
    @JoinColumn(name = "id_tipo_documento_profissao", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private TipoDocumentoProfissao tipoDocumentoProfissao;
    @JoinColumn(name = "id_profissao", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Profissao profissao;

    public FuncaoEquipe() {
        this.id = -1;
        this.cliente = new Cliente();
        this.tipoAtendimento = new TipoAtendimento();
        this.tipoDocumentoProfissao = new TipoDocumentoProfissao();
        this.profissao = new Profissao();
    }

    public FuncaoEquipe(int id, Cliente cliente, TipoAtendimento tipoAtendimento, TipoDocumentoProfissao tipoDocumentoProfissao, Profissao profissao) {
        this.id = id;
        this.cliente = cliente;
        this.tipoAtendimento = tipoAtendimento;
        this.tipoDocumentoProfissao = tipoDocumentoProfissao;
        this.profissao = profissao;
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
        int hash = 3;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.cliente);
        hash = 71 * hash + Objects.hashCode(this.tipoAtendimento);
        hash = 71 * hash + Objects.hashCode(this.tipoDocumentoProfissao);
        hash = 71 * hash + Objects.hashCode(this.profissao);
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
        if (!Objects.equals(this.cliente, other.cliente)) {
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
        return true;
    }

    @Override
    public String toString() {
        return "FuncaoEquipe{" + "id=" + id + ", cliente=" + cliente + ", tipoAtendimento=" + tipoAtendimento + ", tipoDocumentoProfissao=" + tipoDocumentoProfissao + ", profissao=" + profissao + '}';
    }

}
