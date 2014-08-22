package br.com.clinicaintegrada.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_usuario_acesso",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_usuario", "id_permissao"})
)
public class UsuarioAcesso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_permissao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Permissao permissao;
    @Column(name = "is_permite", columnDefinition = "BOOLEAN DEFAULT FALSE")
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
