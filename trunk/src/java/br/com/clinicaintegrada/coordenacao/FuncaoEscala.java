package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ROT_FUNCAO_ESCALA",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "FuncaoEscala.findAll", query = "SELECT FA FROM FuncaoEscala AS FA WHERE FA.cliente.id = :p1 ORDER BY FA.descricao ASC "),
    @NamedQuery(name = "FuncaoEscala.findName", query = "SELECT FA FROM FuncaoEscala FA WHERE UPPER(FA.descricao) LIKE :pdescricao AND FA.cliente.id = :pcliente ORDER BY FA.descricao ASC ")
})
public class FuncaoEscala implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 30, nullable = true)
    private String descricao;

    public FuncaoEscala() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public FuncaoEscala(int id, Cliente cliente, String descricao) {
        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
    }

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
}
