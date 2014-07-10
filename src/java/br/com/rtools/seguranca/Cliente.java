package br.com.rtools.seguranca;


import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "SEG_CLIENTE")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_NOME_CLIENTE", length = 300)
    private String nomeCliente;
    @Column(name = "DS_PERSISTENCE", length = 200)
    private String persistence;
    @Column(name = "DS_CAMINHO_SISTEMA", length = 200)
    private String caminhoSistema;
    @Column(name = "DS_IDENTIFICA", length = 100, unique = true)
    private String identifica;
    @Column(name = "ID_JURIDICA")
    private int idJuridica;
    @Column(name = "NR_ACESSO")
    private int acessos;
    @Column(name = "DT_CADASTRO")
    @Temporal(TemporalType.DATE)
    private Date cadastro;
    @Column(name = "IS_ATIVO")
    private boolean ativo;
    @Column(name = "DS_HOST", length = 300)
    private String host;
    @Column(name = "DS_SENHA", length = 300)
    private String senha;

    public Cliente() {
        this.id = -1;
        this.nomeCliente = "";
        this.persistence = "";
        this.caminhoSistema = "";
        this.identifica = "";
        this.idJuridica = -1;
        this.acessos = 0;
        this.cadastro = new Date();
        this.ativo = false;
        this.host = "";
        this.senha = "";
    }

    public Cliente(int id, String nomeCliente, String persistence, String caminhoSistema, String identifica, int idJuridica, int acessos, Date cadastro, boolean ativo, String host, String senha) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.persistence = persistence;
        this.caminhoSistema = caminhoSistema;
        this.identifica = identifica;
        this.idJuridica = idJuridica;
        this.acessos = acessos;
        this.cadastro = cadastro;
        this.ativo = ativo;
        this.host = host;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public String getCaminhoSistema() {
        return caminhoSistema;
    }

    public void setCaminhoSistema(String caminhoSistema) {
        this.caminhoSistema = caminhoSistema;
    }

    public String getIdentifica() {
        return identifica;
    }

    public void setIdentifica(String identifica) {
        this.identifica = identifica;
    }

    public int getIdJuridica() {
        return idJuridica;
    }

    public void setJuridica(int idJuridica) {
        this.idJuridica = idJuridica;
    }

    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos = acessos;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
