package br.com.clinicaintegrada.filters;

import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleAcessoBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.dao.MacFilialDao;
import br.com.clinicaintegrada.seguranca.dao.UsuarioDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthorizationListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isLoginPage
                = (currentPage.lastIndexOf("index.xhtml") > -1
                || currentPage.lastIndexOf("indexLogin.xhtml") > -1
                || currentPage.lastIndexOf("indexLoginExpirou.xhtml") > -1
                || currentPage.lastIndexOf("indexAcessoWeb.xhtml") > -1);
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object currentUser = session.getAttribute("userName");
        if (!isLoginPage && currentUser == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "indexLoginExpirou");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
//        FacesContext facesContext = event.getFacesContext();
//        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
//        Object currentUser = session.getAttribute("sessaoUsuario");
//        if (currentUser == null) {
//            HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
//            try {
//                //obtem a lista de cookies
//                Cookie[] cookies = request.getCookies();
//                //foreach
//                String login = "";
//                String password = "";
//                String active = "";
//                String cliente = "";
//                String mac = "";
//                for (Cookie cookie : cookies) {
//                    if (cookie.getName().trim().equals("us3r5l0g1ncl1n1c4")) {
//                        login = cookie.getValue();
//                    } else if (cookie.getName().trim().equals("us3r5p4ssw0rdcl1n1c4")) {
//                        password = cookie.getValue();
//                    } else if (cookie.getName().trim().equals("us3r51sr3m3mb3rcl1n1c4")) {
//                        active = cookie.getValue();
//                    } else if (cookie.getName().trim().equals("cl1ent3cl1n1c4")) {
//                        cliente = cookie.getValue();
//                    } else if (cookie.getName().trim().equals("m4ccl1n1c4")) {
//                        mac = cookie.getValue();
//                    }
//                }
//                if (active != null && !active.equals("") && active.equals("1")) {
//                    if ((login != null && !login.equals("")) && (password != null && !password.equals(""))) {
//                        UsuarioDao userDao = new UsuarioDao();
//                        Usuario u = userDao.ValidaUsuario(login, password);
//                        if (u == null) {
//                            Messages.warn("Validação", "Usuário / senha inválidos!");
//                            return;
//                        }
//                        if (!u.getAtivo()) {
//                            u = null;
//                            Messages.warn("Validação", "Seu cadastro encontra-se inátivo!");
//                            Messages.warn("-", "!Solicite uma nova senha e reative seu cadastro.");
//                            return;
//                        }
//                        Sessions.put("sessaoCliente", u.getCliente());
//                        Sessions.put("sessaoUsuario", u);
//                        Sessions.put("userName", u.getPessoa().getNome());
//                        if (mac != null) {
//                            MacFilial macFilial = new MacFilialDao().pesquisaMac(mac);
//                            ControleUsuarioBean controleUsuarioBean = new ControleUsuarioBean();
//                            Object objs[] = new Object[2];
//                            objs[0] = macFilial.getFilial();
//                            objs[1] = macFilial.getDepartamento();
//                            Sessions.put("acessoFilial", macFilial);
//                            String filial = "( " + macFilial.getFilial().getFilial().getPessoa().getNome() + " / " + macFilial.getDepartamento().getDescricao() + " )";
//                            if (macFilial.getMesa() > 0) {
//                                filial += " - Guichê: " + macFilial.getMesa();
//                            }
//                            if (macFilial.getDescricao() != null && !macFilial.getDescricao().isEmpty()) {
//                                filial += " - " + macFilial.getDescricao();
//                            }
//                            if (macFilial.getCaixa() != null) {
//                                if (macFilial.getCaixa().getCaixa() > 0) {
//                                    filial += " - Caixa: " + macFilial.getCaixa().getCaixa();
//                                }
//                            }
//                            controleUsuarioBean.setFilial(filial);
//                            Sessions.put("acessoFilial", macFilial);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.getStackTrace();
//            }
//        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
