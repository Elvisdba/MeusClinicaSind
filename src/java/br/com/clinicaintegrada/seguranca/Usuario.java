package br.com.clinicaintegrada.seguranca;

import br.com.clinicaintegrada.pessoa.Pessoa;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT U FROM Usuario U ORDER BY U.pessoa.nome ASC, U.login ASC ")
})
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Column(name = "ds_login", length = 30, nullable = false, unique = true)
    private String login;
    @Column(name = "ds_senha", length = 6, nullable = false)
    private String senha;
    @Column(name = "is_ativo", columnDefinition = "boolean default false")
    private Boolean ativo;
    @Column(name = "ds_email", length = 255)
    private String email;
    @Column(name = "is_administrador", columnDefinition = "boolean default false")
    private Boolean administrador;
    @Column(name = "is_acesso_externo", columnDefinition = "boolean default false")
    private Boolean acessoExterno;

    public Usuario() {
        this.id = null;
        this.cliente = new Cliente();
        this.pessoa = new Pessoa();
        this.login = "";
        this.senha = "";
        this.ativo = false;
        this.email = "";
        this.administrador = false;
        this.acessoExterno = false;
    }

    public Usuario(Integer id, Cliente cliente, Pessoa pessoa, String login, String senha, Boolean ativo, String email, Boolean administrador, Boolean acessoExterno) {
        this.id = id;
        this.cliente = cliente;
        this.pessoa = pessoa;
        this.login = login;
        this.senha = senha;
        this.ativo = ativo;
        this.email = email;
        this.administrador = administrador;
        this.acessoExterno = acessoExterno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", pessoa=" + pessoa + ", login=" + login + ", ativo=" + ativo + ", email=" + email + '}';
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public Boolean getAcessoExterno() {
        return acessoExterno;
    }

    public void setAcessoExterno(Boolean acessoExterno) {
        this.acessoExterno = acessoExterno;
    }
}
