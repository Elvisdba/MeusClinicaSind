package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_grupo_evento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "GrupoEvento.findAll", query = "SELECT GE FROM GrupoEvento AS GE WHERE GE.cliente.id = :p1 ORDER BY GE.descricao ASC "),
    @NamedQuery(name = "GrupoEvento.findName", query = "SELECT GE FROM GrupoEvento GE WHERE UPPER(GE.descricao) LIKE :pdescricao AND GE.cliente.id = :p1 ORDER BY GE.descricao ASC ")
})
public class GrupoEvento implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 30, nullable = true)
    private String descricao;

    public GrupoEvento() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public GrupoEvento(int id, Cliente cliente, String descricao) {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final GrupoEvento other = (GrupoEvento) obj;
        return true;
    }

    @Override
    public String toString() {
        return "GrupoEvento{" + "id=" + id + ", cliente=" + cliente + ", descricao=" + descricao + '}';
    }

}