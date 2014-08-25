package br.com.clinicaintegrada.endereco;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "end_bairro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"}))
@NamedQueries({
    @NamedQuery(name = "Bairro.findAll", query = "SELECT BAI FROM Bairro AS BAI ORDER BY BAI.descricao ASC "),
    @NamedQuery(name = "Bairro.findName", query = "SELECT BAI FROM Bairro AS BAI WHERE UPPER(BAI.descricao) LIKE :pdescricao AND (BAI.cliente.id = :p1 OR BAI.cliente.id IS NULL) ORDER BY BAI.descricao ASC ")
})
public class Bairro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 70, nullable = false)
    private String descricao;

    public Bairro() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public Bairro(int id, Cliente cliente, String descricao) {
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
