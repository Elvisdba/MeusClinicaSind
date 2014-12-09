package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_pertences_grupo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"})
)
@NamedQuery(name = "PertenceGrupo.findAll", query = "SELECT PG FROM PertenceGrupo PG WHERE PG.cliente.id = :p1 ORDER BY PG.descricao ASC ")
public class PertenceGrupo implements Serializable, BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 30, nullable = true)
    private String descricao;

    public PertenceGrupo() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public PertenceGrupo(int id, Cliente cliente, String descricao) {
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
        int hash = 5;
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
        final PertenceGrupo other = (PertenceGrupo) obj;
        return true;
    }

    @Override
    public String toString() {
        return "PertenceGrupo{" + "id=" + id + ", cliente=" + cliente + ", descricao=" + descricao + '}';
    }
}
