package br.com.clinicaintegrada.pessoa.utils;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.dao.UsuarioDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.File;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean(name = "pessoaUtilitariosBean")
@ViewScoped
public class PessoaUtilitarios implements Serializable {

    private Pessoa pessoa;
    private MacFilial macFilial = (MacFilial) Sessions.getObject("acessoFilial");
    private Usuario usuarioSessao = (Usuario) Sessions.getObject("sessaoUsuario");

    public PessoaUtilitarios(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaUtilitarios() {
        this.pessoa = new Pessoa();
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * Retorna pessoa jurídica da pessoa
     *
     * @return
     */
    public Juridica juridica() {
        Juridica juridica = new Juridica();
        if (this.pessoa.getId() != -1) {
            JuridicaDao juridicaDB = new JuridicaDao();
            juridicaDB.pesquisaJuridicaPorPessoa(this.pessoa.getId());
        }
        return juridica;
    }

    /**
     * Retorna pessoa física da pessoa
     *
     * @return
     */
    public Fisica fisica() {
        Fisica fisica = new Fisica();
        if (this.pessoa.getId() != -1) {
            FisicaDao fisicaDao = new FisicaDao();
            fisicaDao.pesquisaFisicaPorPessoa(this.pessoa.getId());
        }
        return fisica;
    }

    /**
     * Retorna usuário da pessoa
     *
     * @return
     */
    public Usuario usuario() {
        Usuario usuario1 = new Usuario();
        if (this.pessoa.getId() != -1) {
            UsuarioDao usuarioDao = new UsuarioDao();
            usuarioDao.pesquisaUsuarioPorPessoa(this.pessoa.getId());
        }
        return usuario1;
    }

    /**
     * Retorna usuário da pessoa
     *
     * @return
     */
    public Registro registro() {
        Pessoa pessoa1 = new Pessoa();
        pessoa1.setId(1);
        return registro(pessoa1);
    }

    /**
     * Retorna usuário da pessoa
     *
     * @param pessoa
     * @return
     */
    public Registro registro(Pessoa pessoa) {
        Registro registro = new Registro();
        if (pessoa.getId() != -1) {
            registro = (Registro) new Dao().find(new Registro(), 1);
        }
        return registro;
    }

    /**
     * Retorna macfilial da sessão
     *
     * @return
     */
    public MacFilial getMacFilial() {
        return macFilial;
    }

    /**
     * Retorna usuário da sessão
     *
     * @return
     */
    public Usuario getUsuarioSessao() {
        return usuarioSessao;
    }

    /**
     * Retorna foto da pessoa
     *
     * @param pessoa
     * @return
     */
    public String getFotoPessoaFisica(Integer pessoa) {
        Pessoa p = new Pessoa();
        p.setId(pessoa);
        return getFotoPessoaFisica(p, 0);
    }

    /**
     * Retorna foto da pessoa
     *
     * @param pessoa
     * @return
     */
    public String getFotoPessoaFisica(Pessoa pessoa) {
        return getFotoPessoaFisica(pessoa, 0);
    }

    /**
     * Retorna foto da pessoa
     *
     * @param pessoa
     * @param waiting
     * @return
     */
    public String getFotoPessoaFisica(Pessoa pessoa, Integer waiting) {
        if (waiting > 0) {
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException ex) {
            }
        }
        Fisica fis = new FisicaDao().pesquisaFisicaPorPessoa(pessoa.getId());
        String foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + pessoa.getId() + ".png";
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
        if (!f.exists()) {
            foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + pessoa.getId() + ".jpg";
            f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
            if (!f.exists()) {
                if(fis == null) {
                    foto = "/Imagens/user_male.png";                    
                } else {
                    if (fis.getSexo().equals("F")) {
                        foto = "/Imagens/user_female.png";
                    } else {
                        foto = "/Imagens/user_male.png";
                    }
                }
            }
        }
        return foto;
    }

    /**
     * Retorna foto da pessoa
     *
     * @param fisica
     * @return
     */
    public String getFotoPessoaFisica(Fisica fisica) {
        return getFotoPessoaFisica(fisica, 0);
    }

    /**
     * Retorna foto da pessoa
     *
     * @param fisica
     * @param waiting
     * @return
     */
    public String getFotoPessoaFisica(Fisica fisica, Integer waiting) {
        if (waiting > 0) {
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException ex) {
            }
        }
        String foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".png";
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
        if (!f.exists()) {
            foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Fotos/" + fisica.getPessoa().getId() + ".jpg";
            f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
            if (!f.exists()) {
                if (fisica.getSexo().equals("F")) {
                    foto = "/Imagens/user_female.png";
                } else {
                    foto = "/Imagens/user_male.png";
                }
            }
        }
        return foto;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public void setUsuarioSessao(Usuario usuarioSessao) {
        this.usuarioSessao = usuarioSessao;
    }
}
