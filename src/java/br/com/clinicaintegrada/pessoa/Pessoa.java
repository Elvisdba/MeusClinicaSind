package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.utils.BaseEntity;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.persistence.*;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_pessoa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_nome", "ds_documento", "id_tipo_documento"})
)
public class Pessoa implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
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

    public Pessoa(Integer id, Cliente cliente, String nome, TipoDocumento tipoDocumento, String obs, String site, Date criacao,
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

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pessoa other = (Pessoa) obj;
        return true;
    }

    public String getFoto() {
        return getFoto(0);
    }

    public Fisica getFisica() {
        Fisica fisica = new Fisica();
        fisica.setPessoa(null);
        if (this.id != -1) {
            FisicaDao fisicaDao = new FisicaDao();
            fisica = fisicaDao.pesquisaFisicaPorPessoa(this.id);
            if (fisica.getId() != -1) {
                fisica = (Fisica) new Dao().rebind(fisica);
            }
            fisica.setPessoa(null);
        }
        return fisica;
    }

    public String getFotoResource() {
        if (this.id != -1) {
            FisicaDao fisicaDao = new FisicaDao();
            Fisica fisica = fisicaDao.pesquisaFisicaPorPessoa(this.id);
            String foto = "";
            if (fisica != null) {
                foto = "cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + this.id + "/" + fisica.getFoto() + ".png";
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "resources/" + foto);

                if (f.exists()) {
                    return foto;
                }

                foto = "cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + this.id + "/" + fisica.getFoto() + ".jpg";
                f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "resources/" + foto);

                if (f.exists()) {
                    return foto;
                }

                if (fisica.getSexo().equals("F")) {
                    foto = "images/user_female.png";
                } else {
                    foto = "images/user_male.png";
                }
            } else {
                JuridicaDao juridicaDao = new JuridicaDao();
                Juridica juridica = juridicaDao.pesquisaJuridicaPorPessoa(this.id);

                if (juridica != null) {
                    foto = "cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + this.id + "/" + juridica.getFoto() + ".png";
                    File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "resources/" + foto);

                    if (f.exists()) {
                        return foto;
                    }

                    foto = "cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + this.id + "/" + juridica.getFoto() + ".jpg";
                    f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "resources/" + foto);

                    if (f.exists()) {
                        return foto;
                    }
                }

                foto = "images/user_male.png";
            }
            return foto;
        }
        return "images/user_male.png";
    }

    /**
     * Retorna foto da pessoa
     *
     * @param waiting
     * @return
     */
    public String getFoto(Integer waiting) {
        if (waiting > 0) {
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException ex) {
            }
        }

        String foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/fotos/" + id + ".png";
        try {
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
            if (!f.exists()) {
                foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/fotos/" + id + ".jpg";
                f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
                if (!f.exists()) {
                    try {
                        if (getFisica().getSexo().equals("F")) {
                            foto = "/imagens/user_female.png";
                        } else {
                            foto = "/imagens/user_male.png";
                        }
                    } catch (Exception e) {
                        e.getMessage();
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            foto = "/imagens/user_male.png";
        }
        return foto;
    }

    public PessoaEndereco getPessoaEndereco() {
        PessoaEndereco pessoaEndereco = new PessoaEndereco();
        if (this.id != -1) {
            pessoaEndereco = new PessoaEnderecoDao().pesquisaPessoaEnderecoPorPessoaTipo(this.id, 4);
            if (pessoaEndereco == null) {
                pessoaEndereco = new PessoaEndereco();
            }
        }
        return pessoaEndereco;
    }

    @Override
    public String toString() {
        return "Pessoa{" + "id=" + id + ", cliente=" + cliente + ", nome=" + nome + ", tipoDocumento=" + tipoDocumento + ", obs=" + obs + ", site=" + site + ", telefone1=" + telefone1 + ", telefone2=" + telefone2 + ", telefone3=" + telefone3 + ", email1=" + email1 + ", email2=" + email2 + ", email3=" + email3 + ", documento=" + documento + ", login=" + login + ", senha=" + senha + ", criacao=" + criacao + '}';
    }

}
