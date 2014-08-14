package br.com.rtools.sistema;

import br.com.rtools.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_veiculo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_tipo_combustivel", "ds_descricao", "ds_placa"})
)
public class Veiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_tipo_combustivel", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Combustivel combustivel;
    @Column(name = "ds_descricao", length = 50, nullable = false)
    private String descricao;
    @Column(name = "ds_placa", length = 50, nullable = false)
    private String placa;
    @Column(name = "is_ativo", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean ativo;

    public Veiculo() {
        this.id = -1;
        this.cliente = new Cliente();
        this.combustivel = new Combustivel();
        this.descricao = "";
        this.placa = "";
        this.ativo = false;
    }

    public Veiculo(int id, Cliente cliente, Combustivel combustivel, String descricao, String placa, boolean ativo) {
        this.id = id;
        this.cliente = cliente;
        this.combustivel = combustivel;
        this.descricao = descricao;
        this.placa = placa;
        this.ativo = ativo;
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

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
