package br.com.clinicaintegrada.seguranca.controleUsuario;

import br.com.clinicaintegrada.seguranca.PermissaoUsuario;
import br.com.clinicaintegrada.seguranca.UsuarioAcesso;
import br.com.clinicaintegrada.seguranca.Permissao;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.SegEvento;
import br.com.clinicaintegrada.seguranca.PermissaoDepartamento;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.dao.PermissaoDao;
import br.com.clinicaintegrada.seguranca.dao.PermissaoDepartamentoDao;
import br.com.clinicaintegrada.seguranca.dao.PermissaoUsuarioDao;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.seguranca.dao.UsuarioAcessoDao;
import br.com.clinicaintegrada.seguranca.dao.UsuarioDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Sessions;
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
public class ControleAcessoBean {

    // static final long serialVersionUID = 7220145288109489651L;
    private String login = "";
    private Pessoa loginContribuinte = null;
    private int idModulo = 0;
    private String urlDestino;
    private HttpServletRequest paginaRequerida;
    private String showRendered = "true";
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
        }
        return false;
    }

    public boolean verificarPermissaoUsuario() {
        boolean retorno = false;
        PermissaoDao permissaoDao = new PermissaoDao();
        Dao dao = new Dao();
        Usuario u = (Usuario) Sessions.getObject("sessaoUsuario");
        if (!urlDestino.equals("/ClinicaIntegrada/menuPrincipal.jsf") && (u.getId() != 1 && !u.getAdministrador())) {

            //PESQUISA DE MODULOS-------------------------------------------------------------------------------------------
            if (Sessions.exists("idModulo")) {
                idModulo = Sessions.getInteger("idModulo");
                if (idModulo != 0) {
                    modulo = (Modulo) dao.find(new Modulo(), idModulo);
                }
            }

            //PESQUISA DE ROTINAS-------------------------------------------------------------------------------------------
            RotinaDao rotinaDao = new RotinaDao();
            rotina = rotinaDao.pesquisaRotinaPermissao(converteURL(urlDestino) + ".jsf");

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
                        if (permissaoDepartamento == null) {
                            retorno = false;
                        } else {
                            retorno = true;
                            break;
                        }
                    }
                    UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                    UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
                    if (usuarioAcesso != null) {
                        if (usuarioAcesso.getPermite()) {
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
            if (u.getId() != 1 || !u.getAdministrador()) {
                Sessions.remove("idModulo");
            }
            modulo = new Modulo();
            retorno = true;
        }
        return retorno;
    }

    public Boolean getSave() {
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
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //System.out.println(erro);
        }
        //System.out.println(erro);
        //System.out.println(erro);

        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        boolean retorno = true;
        PermissaoDao permissaoDao = new PermissaoDao();
        if (user != null) {
            int idEvento;
            if (user.getId() != 1 && !user.getAdministrador()) {
                Permissao permissao;
                idModulo = modulo.getId();
                if (id == null || id == -1) {
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
                retorno = verificaPermissao(null, idModulo, rotina.getId(), idEvento);
//                permissao = permissaoDao.pesquisaPermissao(idModulo, rotina.getId(), idEvento, SessaoCliente.get().getId());
//                if (permissao != null) {
//                    PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
//                    PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
//                    List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
//                    for (int i = 0; i < permissaoUsuarios.size(); i++) {
//                        PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
//                        if (permissaoDepartamento.getId() == -1) {
//                            retorno = true;
//                        } else {
//                            retorno = false;
//                            break;
//                        }
//                    }
//                    UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
//                    UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
//                    if (usuarioAcesso != null) {
//                        if (usuarioAcesso.isPermite()) {
//                            retorno = false;
//                        } else {
//                            retorno = true;
//                        }
//                    }
//
//                } else {
//                    retorno = true;
//                }
            } else {
                retorno = false;
            }
        } else {
            retorno = false;
        }
        return retorno;
    }

    public Boolean getSalvar(Object object, int idMod) throws IllegalArgumentException {
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
            if (user.getId() != 1 && !user.getAdministrador()) {
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
                        if (usuarioAcesso.getPermite()) {
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

    public boolean view() {
        return verificaPermissao(4);
    }

    public boolean view(String pageName) {
        return verificaPermissao(pageName, 4);
    }

    public Boolean getDelete() {
        return verificaPermissao(2);
//        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
//        boolean retorno = true;
//        if (Sessions.exists("sessaoUsuario")) {
//            Permissao permissao;
//            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
//
//            if (user.getId() == 1 || user.getAdministrador()) {
//                return false;
//            }
//            PermissaoDao permissaoDao = new PermissaoDao();
//            if (modulo.getId() != -1) {
//                permissao = permissaoDao.pesquisaPermissao(modulo.getId(), rotina.getId(), 2, SessaoCliente.get().getId());
//            } else {
//                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 2, SessaoCliente.get().getId());
//            }
//
//            if (permissao != null) {
//                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
//                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
//                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
//                for (int i = 0; i < permissaoUsuarios.size(); i++) {
//                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
//                    if (permissaoDepartamento.getId() == -1) {
//                        retorno = true;
//                    } else {
//                        retorno = false;
//                        break;
//                    }
//                }
//                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
//                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
//                if (usuarioAcesso != null) {
//                    if (usuarioAcesso.isPermite()) {
//                        retorno = false;
//                    } else {
//                        retorno = true;
//                    }
//                }
//            } else {
//                retorno = true;
//            }
//        } else {
//            retorno = true;
//        }
//        return retorno;
    }

//    public boolean isDelete(int idMod) {
//        return verificaPermissao(idMod, 0, 2);
    //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
//        if (idMod == 0) {
//            idMod = Integer.parseInt((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("idModulo"));
//        }
//        Dao dao = new Dao();
//        Modulo m = (Modulo) dao.find(new Modulo(), idMod);
//        boolean retorno = false;
//        if (Sessions.exists("sessaoUsuario")) {
//            Permissao permissao;
//            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
//
//            if (user.getId() == 1 && user.getAdministrador()) {
//                return false;
//            }
//            PermissaoDao permissaoDao = new PermissaoDao();
//            if (m.getId() != -1) {
//                permissao = permissaoDao.pesquisaPermissao(m.getId(), rotina.getId(), 2, SessaoCliente.get().getId());
//            } else {
//                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 2, SessaoCliente.get().getId());
//            }
//            if (permissao != null) {
//                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
//                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
//                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
//                for (int i = 0; i < permissaoUsuarios.size(); i++) {
//                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
//                    if (permissaoDepartamento.getId() == -1) {
//                        retorno = true;
//                    } else {
//                        retorno = false;
//                        break;
//                    }
//                }
//                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
//                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
//                if (usuarioAcesso != null) {
//                    if (usuarioAcesso.isPermite()) {
//                        retorno = false;
//                    } else {
//                        retorno = true;
//                    }
//                }
//            } else {
//                retorno = true;
//            }
//        } else {
//            retorno = true;
//        }
//        return retorno;
//    }
//    public boolean isUpdate() {
//        return verificaPermissao(3);
    //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
//        boolean retorno = false;
//        if (Sessions.exists("sessaoUsuario")) {
//            Permissao permissao;
//            Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
//
//            if (user.getId() == 1 && user.getAdministrador()) {
//                return false;
//            }
//            PermissaoDao permissaoDao = new PermissaoDao();
//            if (modulo.getId() != -1) {
//                permissao = permissaoDao.pesquisaPermissao(modulo.getId(), rotina.getId(), 3, SessaoCliente.get().getId());
//            } else {
//                permissao = permissaoDao.pesquisaPermissao(2, rotina.getId(), 3, SessaoCliente.get().getId());
//            }
//            if (permissao != null) {
//                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
//                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
//                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
//                for (int i = 0; i < permissaoUsuarios.size(); i++) {
//                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), SessaoCliente.get().getId());
//                    if (permissaoDepartamento.getId() == -1) {
//                        retorno = true;
//                    } else {
//                        retorno = false;
//                        break;
//                    }
//                }
////                if (!retorno) {
//                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
//                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), SessaoCliente.get().getId());
//                if (usuarioAcesso != null) {
//                    if (usuarioAcesso.isPermite()) {
//                        retorno = false;
//                    } else {
//                        retorno = true;
//                    }
//                }
////                }
//            } else {
//                retorno = true;
//            }
//        } else {
//            retorno = true;
//        }
    // return retorno;
//    }
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

    public void setIdIndexPermissao(Integer idIndexPermissao) {
        this.idIndexPermissao = idIndexPermissao;
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(Integer idEvento) {
        return verificaPermissao(null, 0, 0, idEvento);

    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param pageName
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(String pageName, Integer idEvento) {
        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        if (user.getId() == 1 || user.getAdministrador()) {
            return false;
        }
        RotinaDao rotinaDao = new RotinaDao();
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pageName);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pageName);
        }
        if (r == null) {
            return false;
        }
        return verificaPermissao(null, 0, r.getId(), idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param pageName Nome da página da rotina
     * @param idEvento
     * @return
     */
    public boolean verificarPermissao(String pageName, Integer idEvento) {
        return verificaPermissao(pageName, idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param usuario Class
     * @param pageName Nome da página da rotina
     * @param idEvento
     * @return
     */
    public boolean verificarPermissao(Usuario usuario, String pageName, Integer idEvento) {
        RotinaDao rotinaDao = new RotinaDao();
        String pagina = pageName;
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pagina);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pagina);
        }
        if (r == null) {
            return false;
        }
        return verificaPermissao(usuario, 0, r.getId(), idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param usuario
     * @param pageName
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(Usuario usuario, String pageName, Integer idEvento) {
        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        if (user.getId() == 1) {
            return false;
        }
        RotinaDao rotinaDao = new RotinaDao();
        String pagina = pageName;
        Rotina r = rotinaDao.pesquisaRotinaPorPagina(pagina);
        if (r == null) {
            r = rotinaDao.pesquisaRotinaPorAcao(pagina);
        }
        if (r == null) {
            return false;
        }
        return verificaPermissao(usuario, 0, r.getId(), idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param idRotina
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(Integer idRotina, Integer idEvento) {
        return verificaPermissao(null, 0, idRotina, idEvento);
    }

    /**
     * 1 - Inclusão; 2 - Exclusão; 3 - Alteração; 4 - Consulta
     *
     * @param usuario
     * @param idModulo
     * @param idRotina
     * @param idEvento
     * @return
     */
    public boolean verificaPermissao(Usuario usuario, Integer idModulo, Integer idRotina, Integer idEvento) {
        //PESQUISA DE PERMISSAO-------------------------------------------------------------------------------------------
        boolean retorno = true;
        Usuario user = (Usuario) Sessions.getObject("sessaoUsuario");
        if (usuario != null) {
            user = usuario;
        }
        if (Sessions.exists("sessaoUsuario") || user != null) {
            Permissao permissao;
            int idCliente = SessaoCliente.get().getId();
            if (user.getId() == 1 || user.getAdministrador()) {
                return false;
            }
            if (idModulo == 0) {
                idModulo = modulo.getId();
            }
            if (idRotina == 0 || idRotina == -1) {
                idRotina = rotina.getId();
            }
            PermissaoDao permissaoDao = new PermissaoDao();
            if (idModulo != -1) {
                permissao = permissaoDao.pesquisaPermissao(idModulo, idRotina, idEvento, idCliente);
            } else {
                permissao = permissaoDao.pesquisaPermissao(2, idRotina, idEvento, idCliente);
            }
            if (permissao != null) {
                PermissaoUsuarioDao permissaoUsuarioDao = new PermissaoUsuarioDao();
                PermissaoDepartamentoDao permissaoDepartamentoDao = new PermissaoDepartamentoDao();
                List<PermissaoUsuario> permissaoUsuarios = permissaoUsuarioDao.listaPermissaoUsuario(user.getId());
                for (int i = 0; i < permissaoUsuarios.size(); i++) {
                    PermissaoDepartamento permissaoDepartamento = permissaoDepartamentoDao.pesquisaPermissaoDepartamento(permissaoUsuarios.get(i).getDepartamento().getId(), permissaoUsuarios.get(i).getNivel().getId(), permissao.getId(), idCliente);
                    if (permissaoDepartamento == null) {
                        retorno = true;
                    } else {
                        retorno = false;
                        break;
                    }
                }
                UsuarioAcessoDao usuarioAcessoDao = new UsuarioAcessoDao();
                UsuarioAcesso usuarioAcesso = usuarioAcessoDao.pesquisaUsuarioAcesso(user.getId(), permissao.getId(), idCliente);
                if (usuarioAcesso != null) {
                    if (usuarioAcesso.getPermite()) {
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
}
