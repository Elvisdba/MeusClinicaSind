package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_USUARIO_ACESSO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CLIENTE", "ID_USUARIO", "ID_PERMISSAO"})
)
public class UsuarioAcesso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Permissao permissao;
    @Column(name = "IS_PERMITE", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean permite;

    public UsuarioAcesso() {
        this.id = -1;
        this.cliente = new Cliente();
        this.usuario = new Usuario();
        this.permissao = new Permissao();
        this.permite = false;
    }

    public UsuarioAcesso(int id, Cliente cliente, Usuario usuario, Permissao permissao, boolean permite) {
        this.id = id;
        this.cliente = cliente;
        this.usuario = usuario;
        this.permissao = permissao;
        this.permite = permite;
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

    public Permissao getPermissao() {
        return permissao;
    }

    public void setPermissao(Permissao permissao) {
        this.permissao = permissao;
    }

    public boolean isPermite() {
        return permite;
    }

    public void setPermite(boolean permite) {
        this.permite = permite;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
