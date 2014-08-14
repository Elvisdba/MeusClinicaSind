package br.com.rtools.pessoa;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "pes_tipo_atendimento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "TipoAtendimento.findAll", query = "SELECT TA FROM TipoAtendimento AS TA WHERE TA.cliente.id = :p1 ORDER BY TA.descricao ASC "),
    @NamedQuery(name = "TipoAtendimento.findName", query = "SELECT TA FROM TipoAtendimento TA WHERE UPPER(TA.descricao) LIKE :pdescricao AND TA.cliente.id = :pcliente ORDER BY TA.descricao ASC ")
})
public class TipoAtendimento implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 50, nullable = false)
    private String descricao;

    public TipoAtendimento() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public TipoAtendimento(int id, Cliente cliente, String descricao) {
        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
    }

    @Override
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        hash = 17 * hash + Objects.hashCode(this.cliente);
        hash = 17 * hash + Objects.hashCode(this.descricao);
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
        final TipoAtendimento other = (TipoAtendimento) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.cliente, other.cliente)) {
            return false;
        }
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoAtendimento{" + "id=" + id + ", cliente=" + cliente + ", descricao=" + descricao + '}';
    }

}
