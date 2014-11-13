package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_servicos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_descricao", "id_cliente"})
)
public class Servicos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 20, nullable = false, unique = true)
    private String descricao;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private FTipoDocumento tipoDocumento;

    public Servicos() {
        this.id = -1;
        this.descricao = "";
        this.cliente = new Cliente();
        this.tipoDocumento = new FTipoDocumento();
    }

    public Servicos(int id, String descricao, Cliente cliente, FTipoDocumento tipoDocumento) {
        this.id = id;
        this.descricao = descricao;
        this.cliente = cliente;
        this.tipoDocumento = tipoDocumento;
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

    public FTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(FTipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
