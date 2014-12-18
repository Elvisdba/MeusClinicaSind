package br.com.clinicaintegrada.agenda;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.BaseEntity;
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
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 100, nullable = false)
    private String descricao;

    public GrupoAgenda() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public GrupoAgenda(Integer id, Cliente cliente, String descricao) {
        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
    }

    @Override
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.cliente);
        hash = 97 * hash + Objects.hashCode(this.descricao);
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

    @Override
    public String toString() {
        return "GrupoAgenda{" + "id=" + id + ", cliente=" + cliente + ", descricao=" + descricao + '}';
    }

}
