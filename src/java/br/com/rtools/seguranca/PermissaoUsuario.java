package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_PERMISSAO_USUARIO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CLIENTE", "ID_USUARIO", "ID_NIVEL", "ID_DEPARTAMENTO"})
)
public class PermissaoUsuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_NIVEL", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Nivel nivel;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Departamento departamento;

    public PermissaoUsuario() {
        this.id = -1;
        this.cliente = new Cliente();
        this.usuario = new Usuario();
        this.nivel = new Nivel();
        this.departamento = new Departamento();
    }

    public PermissaoUsuario(int id, Cliente cliente, Usuario usuario, Nivel nivel, Departamento departamento) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.nivel = nivel;
        this.departamento = departamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
