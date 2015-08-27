package br.com.clinicaintegrada.seguranca.controleUsuario;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.LiberaAcesso;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.dao.ClienteDao;
import br.com.clinicaintegrada.seguranca.dao.LiberaAcessoDao;
import br.com.clinicaintegrada.seguranca.dao.MacFilialDao;
import br.com.clinicaintegrada.seguranca.dao.RegistroDao;
import br.com.clinicaintegrada.seguranca.dao.UsuarioDao;
import br.com.clinicaintegrada.sistema.ContadorAcessos;
import br.com.clinicaintegrada.sistema.dao.AtalhoDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
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
    private List<String> images = new ArrayList<>();

    public String validacao() throws Exception {
        if (macFilial != null && macFilial.getId() != null) {
            Object objs[] = new Object[2];
            objs[0] = macFilial.getFilial();
            objs[1] = macFilial.getDepartamento();
            Sessions.put("acessoFilial", macFilial);
            filial = "( " + macFilial.getFilial().getFilial().getPessoa().getNome() + " / " + macFilial.getDepartamento().getDescricao() + " )";
            if (macFilial.getMesa() > 0) {
                filial += " - Guichê: " + macFilial.getMesa();
            }
            if (macFilial.getDescricao() != null && !macFilial.getDescricao().isEmpty()) {
                filial += " - " + macFilial.getDescricao();
            }
            if (macFilial.getCaixa() != null) {
                if (macFilial.getCaixa().getCaixa() > 0) {
                    filial += " - Caixa: " + macFilial.getCaixa().getCaixa();
                }
            }
        } else {
            Sessions.put("acessoFilial", null);
            filial = "";
        }
        LiberaAcesso liberaAcesso = null;
        Cliente c;
        if (SessaoCliente.get().getIdentifica().equals("ClinicaIntegradaProducao")) {
            c = new ClienteDao().findByIdentificador("ClinicaIntegrada");
        } else {
            c = new ClienteDao().findByIdentificador(SessaoCliente.get().getIdentifica());
        }
        Registro registro = new RegistroDao().pesquisaRegistroPorCliente(c.getId());
        String pagina = null;
        if (registro != null && !registro.getLiberaAcesso()) {
            if (!usuario.getLogin().equals("admin") && !usuario.getLogin().equals("FundacaoPenteado")) {
                if (macFilial == null || macFilial.getId() == null || macFilial.getId() == -1) {
                    msgErro = "Computador não registrado ou sem mac específicado.";
                    Messages.warn("Validação", msgErro);
                    return pagina;
                }
                liberaAcesso = new LiberaAcessoDao().findByMac(macFilial);
                if (liberaAcesso == null) {
                    msgErro = "Nenhum pedido de acesso encontrado. Rodar novamente o aplicativo de acesso.";
                    Messages.warn("Validação", msgErro);
                    return pagina;
                }
            }
        }
        Sessions.put("indicaAcesso", "local");
        UsuarioDao db = new UsuarioDao();
        String user = usuario.getLogin(), senh = usuario.getSenha();
        if (usuario.getLogin().equals("") || usuario.getLogin().equals("Usuario")) {
            msgErro = "@ Informar nome do usuário!";
            Messages.warn("Validação", msgErro);
            return pagina;
        }
        String usuario_login = usuario.getLogin();
        if (usuario.getLogin().equals("admin")) {
            if (SessaoCliente.get().getIdentifica().equals("FundacaoPenteado")) {
                usuario_login = SessaoCliente.get().getIdentifica();
            }
        }
        if (usuario.getSenha().equals("") || usuario.getSenha().equals("Senha")) {
            msgErro = "@ Informar senha!";
            Messages.warn("Validação", msgErro);
            return pagina;
        }
        usuario = db.ValidaUsuario(usuario_login, usuario.getSenha());
        Logger log = new Logger();
        if (usuario != null) {
            c = new Cliente();
            c = usuario.getCliente();
            if (SessaoCliente.get().getIdentifica().equals("ClinicaIntegradaProducao")) {
                c.setIdentifica("ClinicaIntegradaProducao");
            }
            if (!c.getAtivo()) {
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
            Dirs.create("");
            //log.novo("Usuário logou", "Usuário:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "";
        } else {
            //log.live("Login de acesso tentativa de acesso usr:" + user + "/sen: " + senh);
            usuario = new Usuario();
            msgErro = "@ Usuário e/ou Senha inválidas! Tente novamente.";
            Messages.warn("Validação", msgErro);
        }
        if (liberaAcesso != null) {
            new Dao().delete(liberaAcesso, true);
        }
        return pagina;
    }

    public String getValidacaoIndex() throws IOException {
        if (Sessions.exists("sessaoCliente")) {
            Sessions.remove("conexao");
        }
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String requestCliente;
        Cliente c = new Cliente();
        if (request.getParameter("cliente") != null && Sessions.exists("sessaoCliente") && !request.getParameter("cliente").equals(((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica())) {
            requestCliente = request.getParameter("cliente");
            c.setIdentifica(requestCliente);
        } else {
            if (request.getParameter("cliente") == null) {
                c.setIdentifica("ClinicaIntegrada");
                requestCliente = "ClinicaIntegrada";
            } else {
                requestCliente = request.getParameter("cliente");
                c.setIdentifica(requestCliente);
            }
        }
        if (!requestCliente.equals("")) {
            if (Sessions.exists("sessaoCliente")) {
                Sessions.remove("sessaoCliente");
            }
            if (Sessions.exists("acessoFilial")) {
                Sessions.remove("acessoFilial");
            }
            Sessions.put("sessaoCliente", c);
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

    public String getFilialDep() throws IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //  filialDep = request.getRequestURL().toString();
        // filialDep = requestFilial.getQueryString();
        filialDep = request.getParameter("filial");
        if (filialDep == null && macFilial.getId() != null) {
            return "";
        }
        if (filialDep != null) {
            MacFilialDao macFilialDao = new MacFilialDao();
            macFilial = macFilialDao.pesquisaMac(filialDep);
            if (macFilial != null) {
                Sessions.put("acessoFilial", macFilial);
                filialDep = getMacFilial().getFilial().getFilial().getPessoa().getNome();
                // redireciona(filialDep);
            } else {
                filialDep = "Filial sem Registro";
            }
        }
        return filialDep;
    }

    public void redireciona(String regir) throws IOException {
        if (!filialDep.isEmpty() && !filialDep.equals("Filial sem Registro")) {
            String retorno = "";
            if (Sessions.exists("sessaoCliente")) {
                retorno = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica() + "/";
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(retorno);
        }
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
        return nomeCliente.equals("Rtools") || nomeCliente.equals("ClinicaIntegrada");
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
            images = new ArrayList<>();
            images.add("1.jpg");
        }
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

}
