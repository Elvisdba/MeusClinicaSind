package br.com.clinicaintegrada.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_permissao_departamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_permissao", "id_nivel", "id_departamento"})
)
public class PermissaoDepartamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_permissao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Permissao permissao;
    @JoinColumn(name = "id_nivel", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Nivel nivel;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Departamento departamento;

    public PermissaoDepartamento() {
        this.id = -1;
        this.cliente = new Cliente();
        this.permissao = new Permissao();
        this.nivel = new Nivel();
        this.departamento = new Departamento();
    }

    public PermissaoDepartamento(Integer id, Cliente cliente, Permissao permissao, Nivel nivel, Departamento departamento) {
        this.id = id;
        this.cliente = cliente;
        this.permissao = permissao;
        this.nivel = nivel;
        this.departamento = departamento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
