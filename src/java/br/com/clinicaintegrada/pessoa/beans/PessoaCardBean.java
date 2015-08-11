package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.PessoaEmpresa;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEmpresaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.EmailPessoa;
import br.com.clinicaintegrada.sistema.beans.EmailBean;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class PessoaCardBean implements Serializable {

    private String cliente = "";
    private Fisica fisica = new Fisica();
    private Juridica juridica = new Juridica();
    private Pessoa pessoa = new Pessoa();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
    private String[] imagensTipo = new String[]{"jpg", "jpeg", "png", "gif"};

    public void cardPessoa(int idPessoa) {
        close();
        Dao dao = new Dao();
        pessoa = (Pessoa) dao.find(new Pessoa(), idPessoa);
        if (pessoa == null) {
            pessoa = new Pessoa();
        }
    }

    public void cardFisica(int idPessoa) {
        close();
        FisicaDao fisicaDao = new FisicaDao();
        fisica = (Fisica) fisicaDao.pesquisaFisicaPorPessoa(idPessoa);
        if (fisica == null) {
            fisica = new Fisica();
        }
    }

    public void cardJuridica(int idPessoa) {
        close();
        JuridicaDao juridicaDao = new JuridicaDao();
        juridica = (Juridica) juridicaDao.pesquisaJuridicaPorPessoa(idPessoa);
        if (juridica == null) {
            juridica = new Juridica();
        }
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getImagemJuridica(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + juridica.getPessoa().getId() + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + juridica.getPessoa().getId() + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemJuridica(String caminho, int idPessoa) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + idPessoa + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + idPessoa + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemFisica(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        if (fisica != null && fisica.getPessoa().getId() != -1) {
            for (String imagensTipo1 : imagensTipo) {
                File file = new File(arquivo + "/" + fisica.getPessoa().getId() + "." + imagensTipo1);
                if (file.exists()) {
                    return caminhoTemp + "/" + fisica.getPessoa().getId() + "." + imagensTipo1;
                }
            }
        }
        return "";
    }

    public String getImagemFisica(String caminho, int idPessoa) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + idPessoa + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + idPessoa + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String getImagemPessoa(String caminho) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        if (pessoa != null && pessoa.getId() != -1) {
            for (String imagensTipo1 : imagensTipo) {
                File file = new File(arquivo + "/" + pessoa.getId() + "." + imagensTipo1);
                if (file.exists()) {
                    return caminhoTemp + "/" + pessoa.getId() + "." + imagensTipo1;
                }
            }
        }
        return "";
    }

    public String getImagemPessoa(String caminho, int idPessoa) {
        String caminhoTemp = "/Cliente/" + getCliente() + "/" + caminho;
        String arquivo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + caminho);
        for (String imagensTipo1 : imagensTipo) {
            File file = new File(arquivo + "/" + idPessoa + "." + imagensTipo1);
            if (file.exists()) {
                return caminhoTemp + "/" + idPessoa + "." + imagensTipo1;
            }
        }
        return "";
    }

    public String[] getImagensTipo() {
        return imagensTipo;
    }

    public void setImagensTipo(String[] imagensTipo) {
        this.imagensTipo = imagensTipo;
    }

    public String getCliente() {
        cliente = SessaoCliente.get().getIdentifica();
        return cliente;
    }

    public void close() {
        juridica = new Juridica();
        fisica = new Fisica();
        pessoa = new Pessoa();
        pessoaEndereco = new PessoaEndereco();
        pessoaEmpresa = new PessoaEmpresa();
    }

    public PessoaEndereco getPessoaEndereco() {
        if (pessoaEndereco.getId() == -1) {
            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
            if (fisica != null && fisica.getId() != -1) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(fisica.getPessoa().getId(), 4);
            } else if (juridica != null && juridica.getId() != null) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(juridica.getPessoa().getId(), 4);
            } else if (pessoa != null && pessoa.getId() != -1) {
                pessoaEndereco = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(pessoa.getId(), 4);
            }
            if (pessoaEndereco == null) {
                pessoaEndereco = new PessoaEndereco();
            }
        }
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public PessoaEmpresa getPessoaEmpresa() {
        if (pessoaEmpresa.getId() == -1) {
            PessoaEmpresaDao pessoaEmpresaDao = new PessoaEmpresaDao();
            if (fisica != null && fisica.getId() != -1) {
                pessoaEmpresa = (PessoaEmpresa) pessoaEmpresaDao.pesquisaPessoaEmpresaPorFisica(fisica.getId());
            } else if (pessoa != null && pessoa.getId() != -1) {
                pessoaEmpresa = (PessoaEmpresa) pessoaEmpresaDao.pesquisaPessoaEmpresaPorPessoa(pessoa.getId());
            }
            if (pessoaEmpresa == null) {
                pessoaEmpresa = new PessoaEmpresa();
            }            
        }
        return pessoaEmpresa;
    }

    public void setPessoaEmpresa(PessoaEmpresa pessoaEmpresa) {
        this.pessoaEmpresa = pessoaEmpresa;
    }

    public String enviaEmail(int idPessoa) throws IOException {
        String urlDestino = ((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getRequestURI();
        ChamadaPaginaBean chamadaPaginaBean = new ChamadaPaginaBean();
        urlDestino = chamadaPaginaBean.converteURL(urlDestino);
        return enviaEmail(idPessoa, urlDestino);
    }

    public String enviaEmail(int idPessoa, String urlRetorno) throws IOException {
        Dao dao = new Dao();
        Pessoa p = (Pessoa) dao.find(new Pessoa(), idPessoa);
        if (p == null || p.getEmail1().isEmpty()) {
            return null;
        }
        EmailBean emailBean = new EmailBean();
        emailBean.destroy();
        emailBean.init();
        emailBean.newMessage();
        emailBean.getEmail().setAssunto("Contato");
        emailBean.getEmail().setRotina((Rotina) dao.find(new Rotina(), 112));
        emailBean.getEmail().setMensagem("Prezada Sr(a) " + p.getNome());
        emailBean.setEmailPessoa(new EmailPessoa(-1, emailBean.getEmail(), p, p.getEmail1(), "", "", null, DataHoje.livre(new Date(), "H:m")));
        emailBean.addEmail();
        emailBean.setUrlRetorno(urlRetorno);
        Sessions.put("emailBean", emailBean);
        return ((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).pesquisa("email");
    }

}
