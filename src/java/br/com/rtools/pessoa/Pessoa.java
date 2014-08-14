package br.com.rtools.pessoa;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_pessoa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_nome", "ds_documento", "id_tipo_documento"})
)
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;
    @Column(name = "ds_nome", length = 150, nullable = false)
    private String nome;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private TipoDocumento tipoDocumento;
    @Column(name = "ds_obs", length = 500, nullable = true)
    private String obs;
    @Column(name = "ds_site", length = 50, nullable = true)
    private String site;
    @Column(name = "ds_telefone1", length = 20, nullable = true)
    private String telefone1;
    @Column(name = "ds_telefone2", length = 20)
    private String telefone2;
    @Column(name = "ds_telefone3", length = 20)
    private String telefone3;
    @Column(name = "ds_email1", length = 50, nullable = true)
    private String email1;
    @Column(name = "ds_email2", length = 50)
    private String email2;
    @Column(name = "ds_email3", length = 50)
    private String email3;
    @Column(name = "ds_documento", length = 30, nullable = false)
    private String documento;
    @Column(name = "ds_login", length = 50, nullable = true)
    private String login;
    @Column(name = "ds_senha", length = 50, nullable = true)
    private String senha;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_criacao")
    private Date criacao;

    public Pessoa() {
        this.id = -1;
        this.cliente = new Cliente();
        this.nome = "";
        this.tipoDocumento = new TipoDocumento();
        this.obs = "";
        this.site = "";
        criacao = new Date();
        this.telefone1 = "";
        this.telefone2 = "";
        this.telefone3 = "";
        this.email1 = "";
        this.email2 = "";
        this.email3 = "";
        this.documento = "";
        this.login = "";
        this.senha = "";
    }

    public Pessoa(int id, Cliente cliente, String nome, TipoDocumento tipoDocumento, String obs, String site, Date criacao,
            String telefone1, String telefone2, String telefone3, String email1, String email2, String email3, String documento, String login, String senha) {
        this.id = id;
        this.cliente = cliente;
        this.nome = nome;
        this.tipoDocumento = tipoDocumento;
        this.obs = obs;
        this.site = site;
        this.criacao = criacao;
        this.telefone1 = telefone1;
        this.telefone2 = telefone2;
        this.telefone3 = telefone3;
        this.email1 = email1;
        this.email2 = email2;
        this.email3 = email3;
        this.documento = documento;
        this.login = login;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getCriacao() {
        return criacao;
    }

    public void setCriacao(Date criacao) {
        this.criacao = criacao;
    }

    public String getCriacaoString() {
        return DataHoje.converteData(criacao);
    }

    public void setCriacaoString(String criacaoString) {
        this.criacao = DataHoje.converte(criacaoString);
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

    public void selectedCriacao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.criacao = DataHoje.converte(format.format(event.getObject()));
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
