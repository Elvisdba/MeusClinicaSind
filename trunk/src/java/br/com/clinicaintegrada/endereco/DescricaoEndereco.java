package br.com.clinicaintegrada.endereco;

import br.com.clinicaintegrada.seguranca.Cliente;
import javax.persistence.*;

@Entity
@Table(name = "end_descricao_endereco",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "ds_descricao"})
)
@NamedQueries({
    @NamedQuery(name = "DescricaoEndereco.pesquisaID", query = "SELECT DECE FROM DescricaoEndereco AS DECE WHERE DECE.id = :pid"),
    @NamedQuery(name = "DescricaoEndereco.findAll", query = "SELECT DECE FROM DescricaoEndereco AS DECE ORDER BY DECE.descricao ASC "),
    @NamedQuery(name = "DescricaoEndereco.findName", query = "SELECT DECE FROM DescricaoEndereco AS DECE WHERE UPPER(DECE.descricao) LIKE :pdescricao AND (DECE.cliente.id = :p1 OR DECE.cliente.id IS NULL) ORDER BY DECE.descricao ASC ")
})
public class DescricaoEndereco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cliente cliente;
    @Column(name = "ds_descricao", length = 100, nullable = false)
    private String descricao;

    public DescricaoEndereco() {
        this.id = -1;
        this.cliente = new Cliente();
        this.descricao = "";
    }

    public DescricaoEndereco(Integer id, Cliente cliente, String descricao) {
        this.id = id;
        this.cliente = cliente;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
