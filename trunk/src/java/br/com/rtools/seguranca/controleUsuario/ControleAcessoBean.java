package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.*;
import br.com.rtools.seguranca.dao.PermissaoDao;
import br.com.rtools.seguranca.dao.PermissaoDepartamentoDao;
import br.com.rtools.seguranca.dao.PermissaoUsuarioDao;
import br.com.rtools.seguranca.dao.RotinaDao;
import br.com.rtools.seguranca.dao.UsuarioAcessoDao;
import br.com.rtools.seguranca.dao.UsuarioDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Sessions;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class ControleAcessoBean implements Serializable {

    // static final long serialVersionUID = 7220145288109489651L;
    private String login = "";
    private Pessoa loginContribuinte = null;
    private int idModulo = 0;
    private String urlDestino;
    private HttpServletRequest paginaRequerida;
    private String showRendered = "true";
    private String msgConfirma = "";
    private int tipo = -1;
    private Modulo modulo = new Modulo();
    private Rotina rotina = new Rotina();
    private SegEvento evento = new SegEvento();
    private int idIndexPermissao = -1;
    private boolean bloqueiaMenu = false;

    public String getShowRendered() {
        return showRendered;
    }

    public void setShowRendered(String showRendered) {
        this.showRendered = showRendered;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getValidacao() throws IOException {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlDestino = paginaRequerida.getRequestURI();
        if (!Sessions.exists("indicaAcesso")) {
            redirectAcessoNegado();
            return null;
        }
        if (((String) Sessions.getString("indicaAcesso")).equals("local")) {
            if (urlDestino.equals("/ClinicaIntegrada/usuarioPerfil.jsf")) {
                return null;
            }
            if ((!verificarUsuario()) || !verificarPermissaoUsuario()) {
                redirectAcessoNegado();
                return null;
            } else {
                controleInterno(urlDestino);
                return null;
            }
        } else if (((String) Sessions.getString("indicaAcesso")).equals("web")) {
            if ((!verificarUsuarioAcessoWeb()) || !verificarTipoPagina()) {
                redirectAcessoNegado();
            } else {
                controleInterno(urlDestino);
                return null;
            }
        }
        return null;
    }

    public String controleInterno(String urlCaminho) throws IOException {
        String retorno = (String) Sessions.getString("urlControleInterno");
        if (retorno == null) {
            retorno = "";
        } else if (retorno.equals(converteURL(urlCaminho))) {
            Sessions.put("linkClicado", true);
        }
        if (!Sessions.exists("linkClicado")) {
            redirectAcessoNegado();
        } else {
            Sessions.remove("linkClicado");
            Sessions.put("urlControleInterno", converteURL(urlCaminho));
        }
        return null;
    }

    public void redirectAcessoNegado() throws IOException {
        if (Sessions.exists("sessaoUsuario") || Sessions.exists("sessaoUsuarioAcessoWeb")) {
            Sessions.put("urlRetorno", "acessoNegado");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/ClinicaIntegrada/acessoNegado.jsf");
        } else {
            Sessions.put("urlRetorno", "sessaoExpirou");
            FacesContext.getCurrentInstance().getExternalContext().redirect("/ClinicaIntegrada/sessaoExpirou.jsf");
        }
    }

    public boolean verificarUsuarioAcessoWeb() {
        UsuarioDao db = new UsuarioDao();
        Pessoa contri = null;
        Pessoa conta = null;
        if (Sessions.exists("sessaoUsuarioAcessoWeb")) {
            loginContribuinte = (Pessoa) Sessions.getObject("sessaoUsuarioAcessoWeb");
            contri = db.ValidaUsuarioContribuinteWeb(loginContribuinte.getId());
            conta = db.ValidaUsuarioContabilidadeWeb(loginContribuinte.getId());
        } else {
            loginContribuinte = null;
        }

        if ((loginContribuinte == null)) {
            return false;
        } else {
            if ((contri != null) && (conta != null)) {
                tipo = 0; //OS DOIS
            } else if (contri != null) {
                tipo = 1; //CONTRIBUINTES
            } else if (conta != null) {
                tipo = 2; //CONTABILIDADE
            } else {
                tipo = 3; //PATRONAL
            }
            return true;
        }
    }

    public boolean verificarTipoPagina() {
        String conv = converteURL(urlDestino);
        if (tipo == 0) {
            return true;
        } else if (conv.equals("menuPrincipalAcessoWeb") || conv.equals("webConfiguracoes")) {
            return true;
        } else if ((tipo == 1) && (conv.equals("webContribuinte") || conv.equals("webAgendamentoContribuinte") || conv.equals("webSolicitaREPIS"))) {
            return true;
        } else if ((tipo == 2) && (conv.equals("webContabilidade") || conv.equals("webAgendamentoContabilidade") || conv.equals("webSolicitaREPIS"))) {
            return true;
        } else if ((tipo == 3) && (conv.equals("webLiberacaoREPIS"))) {
            return true;
        }
        return false;
    }

    public boolean verificarPermissaoUsuario() {
        boolean retorno = false;
        PermissaoDao permissaoDao = new PermissaoDao();
        Dao dao = new Dao();
        Usuario u = (Usuario) Sessions.getObject("sessaoUsuario");
        if (!urlDestino.equals("/ClinicaIntegrada/menuPrincipal.jsf") && (u.getId() != 1 && !u.isAdministrador())) {

            //PESQUISA DE MODULOS-------------------------------------------------------------------------------------------
            if (Sessions.exists("idModulo")) {
                idModulo = Sessions.getInteger("idModulo");
                if (idModulo != 0) {
                    modulo = (Modulo) dao.find(new Modulo(), idModulo);
                }
            }

            //PESQUISA DE ROTINAS-------------------------------------------------------------------------------------------
            RotinaDao rotinaDao = new RotinaDao();
            rotina = rotinaDao.pesquisaRotinaPermissao(urlDestino);

            if (rotina == null) {
                rotina = new Rotina();
            }

            if (Sessions.exists("chamadaPaginaSimples")) {
                String[] lista = (String[]) Sessions.getStringVector("chamadaPaginaSimples");
                Sessions.remove("chamadaPaginaSimples");
                Rotina r = rotinaDao.pesquisaRotinaPermissaoPorClasse(lista[0]);
                if (r != null) {
                    rotina = new Rotina();
                    rotina = r;
                }

            }

            if (rotina.getId() == -1) {
                return false;
            }

            //PESQUISA DE EVENTOS-------------------------------------------------------------------------------------------
            evento = (SegEvento) dao.find(new SegEvento(), 4);

            if (evento == null) {
                evento = new SegEvento();
            }

            // PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------            
            if (Sessions.exists("sessaoUsuario")) {
                Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
                Permissao permissao;
                if (modulo.getId() != -1) {
                    permissao = permissaoDao.pesquisaPermissao(modulo.getId(), rotina.getId(), evento.getId(), SessaoCliente.get().getId());
                } else {
                    permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), evento.getId(), SessaoCliente.get().getId());
                }

                if (permissao != null) {
                    PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                    PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                    List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = false;
                        } else {
                            retorno = true;
                            break;
                        }
                    }
                    UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                    UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (usuarioAcesso != null) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = true;
                        } else {
                            retorno = false;
                        }
                    }

                } else {
                    retorno = false;
                }
            } else {
                return retorno;
            }
        } else {
            if (u.getId() != 1 || !u.isAdministrador()) {
                Sessions.remove("idModulo");
            }
            modulo = new Modulo();
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoSalvar() {
        Object object = (Object) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("object");
        if (object == null) {
            return true;
        }
        Class classe = object.getClass();
        Integer id = -1;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            // id = (Integer) metodo.invoke(object, null);
            id = (Integer) metodo.invoke(object, (Object[]) null);
        } catch (NoSuchMethodException e) {
            //System.out.println(erro);
        } catch (IllegalAccessException e) {
            //System.out.println(erro);
        } catch (InvocationTargetException e) {
            //System.out.println(erro);
        }
        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        boolean retorno = true;
        PermissaoDao permissaoDao = new PermissaoDao();
        if (user != null) {
            int idEvento;
            if (user.getId() != 1 && !user.isAdministrador()) {
                Permissao permissao;
                idModulo = modulo.getId();
                if (id == -1) {
                    if (modulo.getId() != -1) {
                        idEvento = 1;
                    } else {
                        idModulo = 2;
                        idEvento = 1;
                    }

                } else {
                    if (modulo.getId() != -1) {
                        idEvento = 3;
                    } else {
                        idModulo = 2;
                        idEvento = 3;
                    }
                }
                permissao = permissaoDao.pesquisaPermissao(idModulo, rotina.getId(), idEvento, SessaoCliente.get().getId());
                if (permissao != null) {
                    PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                    PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                    List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = true;
                        } else {
                            retorno = false;
                            break;
                        }
                    }
                    UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                    UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (usuarioAcesso != null) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = false;
                        } else {
                            retorno = true;
                        }
                    }

                } else {
                    retorno = true;
                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getSalvar(Object object, int idMod) throws IllegalArgumentException {
        if (object == null) {
            return false;
        }
        if (idMod == 0) {
            idMod = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo"));
        }
        Class classe = object.getClass();
        Integer id = -1;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            // id = (Integer) metodo.invoke(object, null);
            id = (Integer) metodo.invoke(object, (Object[]) null);
        } catch (NoSuchMethodException e) {
            //System.out.println(erro);
        } catch (IllegalAccessException e) {
            //System.out.println(erro);
        } catch (InvocationTargetException e) {
            //System.out.println(erro);
        }
        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        Dao dao = new Dao();
        Modulo m = (Modulo) dao.find(new Modulo(), idMod);
        boolean retorno = false;
        PermissaoDao permissaoDao = new PermissaoDao();
        if (user != null) {
            int idEvento;
            if (user.getId() != 1 && !user.isAdministrador()) {
                Permissao permissao;
                idModulo = m.getId();
                if (id == -1) {
                    if (m.getId() != -1) {
                        idEvento = 1;
                    } else {
                        idModulo = 2;
                        idEvento = 1;
                    }

                } else {
                    if (m.getId() != -1) {
                        idEvento = 3;
                    } else {
                        idModulo = 2;
                        idEvento = 3;
                    }
                }
                permissao = permissaoDao.pesquisaPermissao(idModulo, rotina.getId(), idEvento, SessaoCliente.get().getId());
                if (permissao != null) {
                    PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                    List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                    PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
                        PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                        if (permissaoDepartamento.getId() == -1) {
                            retorno = true;
                        } else {
                            retorno = false;
                            break;
                        }
                    }
                    UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                    UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (usuarioAcesso != null) {
                        if (usuarioAcesso.isPermite()) {
                            retorno = false;
                        } else {
                            retorno = true;
                        }
                    }
                } else {
                    retorno = true;
                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public boolean getBotaoExcluir() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = true;
        if (Sessions.exists("sessaoUsuario")) {
            Permissao permissao;
            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");

            if (user.getId() == 1 || user.isAdministrador()) {
                return false;
            }
            PermissaoDao permissaoDao = new PermissaoDao();
            if (modulo.getId() != -1) {
                permissao = permissaoDao.pesquisaPermissao(modulo.getId(), rotina.getId(), 2, SessaoCliente.get().getId());
            } else {
                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 2, SessaoCliente.get().getId());
            }

            if (permissao != null) {
                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                if (usuarioAcesso != null) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getExcluir(int idMod) {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        if (idMod == 0) {
            idMod = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo"));
        }
        Dao dao = new Dao();
        Modulo m = (Modulo) dao.find(new Modulo(), idMod);
        boolean retorno = false;
        if (Sessions.exists("sessaoUsuario")) {
            Permissao permissao;
            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");

            if (user.getId() == 1 && user.isAdministrador()) {
                return false;
            }
            PermissaoDao permissaoDao = new PermissaoDao();
            if (m.getId() != -1) {
                permissao = permissaoDao.pesquisaPermissao(m.getId(), rotina.getId(), 2, SessaoCliente.get().getId());
            } else {
                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 2, SessaoCliente.get().getId());
            }
            if (permissao != null) {
                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                if (usuarioAcesso != null) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean getBotaoAlterar() {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = false;
        if (Sessions.exists("sessaoUsuario")) {
            Permissao permissao;
            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");

            if (user.getId() == 1 && user.isAdministrador()) {
                return false;
            }
            PermissaoDao permissaoDao = new PermissaoDao();
            if (modulo.getId() != -1) {
                permissao = permissaoDao.pesquisaPermissao(modulo.getId(), rotina.getId(), 3, SessaoCliente.get().getId());
            } else {
                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 3, SessaoCliente.get().getId());
            }
            if (permissao != null) {
                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (permissaoDepartamento.getId() == -1) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
//                if (!retorno) {
                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                if (usuarioAcesso != null) {
                    if (usuarioAcesso.isPermite()) {
                        retorno = false;
                    } else {
                        retorno = true;
                    }
                }
//                }
            } else {
                retorno = true;
            }
        } else {
            retorno = true;
        }
        return retorno;
    }

    public boolean verificarUsuario() {
        if (Sessions.exists("sessaoUsuario")) {
            login = ((Usuario) Sessions.getObject("sessaoUsuario")).getLogin();
        } else {
            login = "";
        }
        if ((login == null) || (login.equals(""))) {
            return false;
        } else {
            return true;
        }
    }

    public String converteURL(String url) {
        String result;
        int iniURL, fimURL;
        iniURL = url.lastIndexOf("/");
        fimURL = url.lastIndexOf(".");
        result = url.substring(iniURL + 1, fimURL);
        return result;
    }

    public String Teste() {
        setShowRendered("false");
        return null;
    }

    public int getIdIndexPermissao() {
        return idIndexPermissao;
    }

    public void setIdIndexPermissao(int idIndexPermissao) {
        this.idIndexPermissao = idIndexPermissao;
    }
}
