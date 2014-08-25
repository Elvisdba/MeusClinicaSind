package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.seguranca.Cliente;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.dao.MacFilialDao;
import br.com.rtools.seguranca.dao.UsuarioDao;
import br.com.rtools.sistema.ContadorAcessos;
import br.com.rtools.sistema.dao.AtalhoDao;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class ControleUsuarioBean implements Serializable {

    private Usuario usuario = new Usuario();
    private MacFilial macFilial = new MacFilial();
    private String login = "";
    private String linkVoltar;
    private int fimURL;
    private int iniURL;
    private String paginaDestino;
    private String alerta;
    private static String cliente;
    private static String bloqueiaMenu;
    private String filial;
    private String filialDep = "";
    private String msgErro = "";
    private List<ContadorAcessos> listaContador = new ArrayList();
    private List<String> images = new ArrayList<String>();

    public String validacao() throws Exception {
        String pagina = null;
        Sessions.put("indicaAcesso", "local");
        UsuarioDao db = new UsuarioDao();
        String user = usuario.getLogin(), senh = usuario.getSenha();
        if (usuario.getLogin().equals("") || usuario.getLogin().equals("Usuario")) {
            msgErro = "@ Informar nome do usuário!";
            Messages.warn("Validação", msgErro);
            return pagina;
        }
        if (usuario.getSenha().equals("") || usuario.getSenha().equals("Senha")) {
            msgErro = "@ Informar senha!";
            Messages.warn("Validação", msgErro);
            return pagina;
        }
        usuario = db.ValidaUsuario(usuario.getLogin(), usuario.getSenha());
        Logger log = new Logger();
        if (usuario != null) {
            Cliente c = usuario.getCliente();
            if (!c.isAtivo()) {
                msgErro = "@ Cliente inátivo.";
                return pagina;
            }
            pagina = "menuPrincipal";
            Sessions.put("sessaoUsuario", usuario);
            Sessions.put("usuarioLogin", usuario.getLogin());
            Sessions.put("userName", usuario.getLogin());
            Sessions.put("linkClicado", true);
            Sessions.put("acessoCadastro", false);
            login = ((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getNome() + " - "
                    + ((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getTipoDocumento().getDescricao() + ": "
                    + ((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getDocumento();
            Sessions.put("sessaoCliente", c);
            Diretorio.criar("");
            log.novo("Usuário logou", "Usuário:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "";
        } else {
            log.live("Login de acesso tentativa de acesso usr:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "@ Usuário e/ou Senha inválidas! Tente novamente.";
            Messages.warn("Validação", msgErro);
        }
        return pagina;
    }

    public String getValidacaoIndex() throws IOException {
        if (Sessions.exists("sessaoCliente")) {
            Sessions.remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente = "";
        Cliente cliente = new Cliente();
        if (request.getParameter("cliente") != null && Sessions.exists("sessaoCliente") && !request.getParameter("cliente").equals(((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica())) {
            requestCliente = request.getParameter("cliente");
            cliente.setIdentifica(requestCliente);
        } else {
            if (request.getParameter("cliente") == null) {
                cliente.setIdentifica("ClinicaIntegrada");
                requestCliente = "ClinicaIntegrada";
            } else {
                requestCliente = request.getParameter("cliente");
                cliente.setIdentifica(requestCliente);
            }
        }
        if (!requestCliente.equals("")) {
            if (Sessions.exists("sessaoCliente")) {
                Sessions.remove("sessaoCliente");
            }
            if (Sessions.exists("acessoFilial")) {
                Sessions.remove("acessoFilial");
            }
            Sessions.put("sessaoCliente", cliente);
        } else {
            if (Sessions.exists("sessaoCliente")) {
                Sessions.remove("sessaoCliente");
            }
            if (Sessions.exists("acessoFilial")) {
                Sessions.remove("acessoFilial");
            }
        }
        return null;
    }

    public String getValidaIndex() {
        if (Sessions.exists("sessaoCliente")) {
            Sessions.remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente;
        if (request.getParameter("cliente") != null && request.getParameter("cliente") != FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoCliente")) {
            FacesContext conext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) conext.getExternalContext().getSession(false);
            session.invalidate();
            requestCliente = request.getParameter("cliente");
        } else {
            requestCliente = "ClinicaIntegrada";
        }
        if (!requestCliente.equals("")) {
            if (Sessions.exists("sessaoCliente")) {
                Sessions.remove("sessaoCliente");
            }
            if (Sessions.exists("acessoFilial")) {
                Sessions.remove("acessoFilial");
            }
            Sessions.put("sessaoCliente", requestCliente);
        } else {
            if (Sessions.exists("sessaoCliente")) {
                Sessions.remove("sessaoCliente");
            }
            if (Sessions.exists("acessoFilial")) {
                Sessions.remove("acessoFilial");
            }
        }
        return null;
    }

    public void refreshForm(String retorno) throws IOException {
    }

    public String voltarDoAcessoNegado() {
        linkVoltar = Sessions.getString("urlRetorno");
        if (linkVoltar == null) {
            return "index";
        } else {
            return converteURL();
        }
    }

    public String converteURL() {
        String url = linkVoltar;
        iniURL = url.lastIndexOf("/");
        fimURL = url.lastIndexOf(".");
        if (iniURL != -1 && fimURL != -1) {
            paginaDestino = url.substring(iniURL + 1, fimURL);
        } else {
            paginaDestino = url;
        }
        return paginaDestino;
    }

    public void carregar() {
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlIndex", request.getQueryString());
        filialDep = "oi";
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getFilialDep() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //  filialDep = request.getRequestURL().toString();
        // filialDep = requestFilial.getQueryString();
        filialDep = request.getParameter("filial");
        if (filialDep != null) {
            MacFilialDao macFilialDao = new MacFilialDao();
            setMacFilial(macFilialDao.pesquisaMac(filialDep));

            if (getMacFilial() != null) {
                filialDep = getMacFilial().getFilial().getFilial().getPessoa().getNome();
            } else {
                filialDep = "Filial sem Registro";
            }
        }
        return filialDep;
    }

    public void setFilialDep(String filialDep) {
        this.filialDep = filialDep;
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public List<ContadorAcessos> getListaContador() {
        if (Sessions.exists("sessaoUsuario")) {
            Usuario usu = ((Usuario) Sessions.getObject("sessaoUsuario"));
//            RotinaDB db = new RotinaDBToplink();
            AtalhoDao dao = new AtalhoDao();
            listaContador.clear();
            //listaRotina = db.pesquisaAcessosOrdem();
            listaContador = dao.listaAcessosUsuario(usu.getId());
        }
        return listaContador;
    }

    public void setListaContador(List<ContadorAcessos> listaContador) {
        this.listaContador = listaContador;
    }

    public static String getCliente() {
        if (Sessions.exists("sessaoCliente")) {
            cliente = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
        }
        return cliente;
    }

    public String getClienteString() {
        String novoCliente = "";
        if (Sessions.exists("sessaoCliente")) {
            novoCliente = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
        }
        return novoCliente;
    }

    public boolean isBoqueiaMenu() {
        String nomeCliente = getClienteString();
        if (nomeCliente.equals("Rtools") || nomeCliente.equals("ClinicaIntegrada")) {
            return true;
        }
        return false;
    }

    public static void setBloqueiaMenu(String aBloqueiaMenu) {
        bloqueiaMenu = aBloqueiaMenu;
    }

    public void removeSessionsModuloMenuPrincipal() {
        if (Sessions.exists("idModulo")) {
            Sessions.remove("idModulo");
        }
    }

    public List<String> getImages() {
        if (images.isEmpty()) {
            images = new ArrayList<String>();
            images.add("1.jpg");
        }
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}