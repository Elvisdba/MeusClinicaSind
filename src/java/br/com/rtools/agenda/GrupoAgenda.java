package br.com.rtools.agenda;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "age_grupo_agenda",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"}))
@NamedQuery(name = "GrupoAgenda.findAll", query = "SELECT GRA FROM GrupoAgenda GRA WHERE GRA.cliente.id = :p1 ORDER BY GRA.descricao ASC ")
public class GrupoAgenda implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 100, nullable = false, unique = true)
    private String descricao;

    public GrupoAgenda() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public GrupoAgenda(int id, Cliente cliente, String descricao) {
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
        hash = 29 * hash + this.id;
        hash = 29 * hash + Objects.hashCode(this.cliente);
        hash = 29 * hash + Objects.hashCode(this.descricao);
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
        final GrupoAgenda other = (GrupoAgenda) obj;
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

}
