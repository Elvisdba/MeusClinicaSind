package br.com.clinicaintegrada.seguranca.controleUsuario;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.AnaliseString;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.MenuLinks;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class ChamadaPaginaBean implements Serializable {

    private static HttpServletRequest paginaRequerida = null;
    private static Boolean carregaPagina = true;
    private Boolean linkClicado = false;
    private static String urlAtual = "";
    private String penultimoLink = "";
    private String paginaDestino;
    private List<MenuLinks> menuLinks = new ArrayList<>();
    private Integer nivelLink = 0;
    private Integer fimURL;
    private Integer iniURL;
    private static Integer tipoPagina = 0;
    private final Integer SEGURANCA = 1;
    private final Integer CADASTRO = 2;
    private final Integer ADMINISTRATIVO = 3;
    private final Integer FICHA_TECNICA = 4;
    private final Integer COORDENACAO = 5;
    private final Integer FINANCEIRO = 6;

    private boolean renderPesquisa = true;
    private List<Rotina> listRotina = new ArrayList<>();
    private Boolean disabled = false;

    @PreDestroy
    public void destroy() {
        Sessions.remove("chamadaPaginaBean");
    }

    // NOVO MÉTODO - EM ESTUDO
//    public synchronized String chamadaMenu(String pagina) {
//        return metodoGenerico(0, pagina);
//    }
    public synchronized String pesquisa(String pagina) {
        return metodoGenerico(1, pagina);
    }

    public synchronized String pesquisa(String pagina, String bean) throws IOException {
        Sessions.remove(bean);
        Sessions.remove(pagina + "Bean");
        return metodoGenerico(1, pagina);
    }

    /**
     * <p>
     * <strong>Página</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("pesquisaAgendaTelefone"); Não é
     * necessário no Bean espefícar o nome completo.</p>
     *
     * @param pagina (Nome da página)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized String pagina(String pagina) throws IOException {
        Sessions.remove(pagina + "Bean");
        String redirect = metodoGenerico(2, pagina);
        return redirect;
    }

    /**
     * <p>
     * <strong>Página</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("pagina", "agendaTelefone"); Não é
     * necessário no Bean espefícar o nome completo.</p>
     *
     * @param pagina (Nome da página)
     * @param bean (Bean)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized String pagina(String pagina, String bean) throws IOException {
        Sessions.remove(bean);
        Sessions.remove(bean + "Bean");
        String redirect = metodoGenerico(2, pagina);
        return redirect;
    }

    /**
     * <p>
     * <strong>Página</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("pesquisaAgendaTelefone"); Não é
     * necessário no Bean espefícar o nome completo.</p>
     *
     * @param pagina (Nome da página)
     * @param tStatic (Quando estático)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized static String pagina(String pagina, Boolean tStatic) throws IOException {
        Sessions.remove(pagina + "Bean");
        String redirect = metodoGenerico(2, pagina);
        return redirect;
    }

    /**
     * <p>
     * <strong>Página</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("pagina", "agendaTelefone"); Não é
     * necessário no Bean espefícar o nome completo.</p>
     *
     * @param pagina (Nome da página)
     * @param bean (Bean)
     *
     * @author Bruno
     * @param tStatic (Quando estático)
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized static String pagina(String pagina, String bean, Boolean tStatic) throws IOException {
        Sessions.remove(bean);
        Sessions.remove(bean + "Bean");
        String redirect = metodoGenerico(2, pagina);
        return redirect;
    }

    /**
     * <p>
     * <strong>Página</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("pesquisaAgendaTelefone",
     * "agendaTelefone", true); Não é necessário no Bean espefícar o nome
     * completo.</p>
     *
     * @param pagina (Nome da página)
     * @param bean (Bean)
     * @param isCadastro (Default false)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized String pagina(String pagina, String bean, boolean isCadastro) throws IOException {
        Sessions.remove(bean + "Bean");
        if (isCadastro) {
            Sessions.put("acessoCadastro", true);
        }
        String redirect = metodoGenerico(2, pagina);
        return redirect;
    }

    /**
     * <p>
     * <strong>Relatório</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("relatorio); Não é necessário no Bean
     * espefícar o nome completo.</p>
     *
     * @param relatorio (Nome do relatorio)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized String relatorio(String relatorio) throws IOException {
        Sessions.remove(relatorio + "Bean");
        String redirect = metodoGenerico(3, relatorio);
        return redirect;
    }

    /**
     * <p>
     * <strong>Relatório</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("relatorio", "Bean"); Não é necessário
     * no Bean espefícar o nome completo.</p>
     *
     * @param relatorio (Nome do relatorio)
     * @param bean (Bean)
     *
     * @author Bruno
     *
     * @return List
     * @throws java.io.IOException
     */
    public synchronized String relatorio(String relatorio, String bean) throws IOException {
        Sessions.remove(bean + "Bean");
        String redirect = metodoGenerico(3, relatorio);
        return redirect;
    }

    /**
     * <p>
     * <strong>Página Simples</strong></p>
     * <p>
     * <strong>Exemplo:</strong> pagina("Classe", "Titulo");</p>
     *
     * @param objeto (Nome da classe)
     * @param titulo (Titulo)
     *
     * @author Bruno
     *
     * @return List
     */
    public synchronized String paginaSimples(String objeto, String titulo) {
        return simples(objeto, titulo);
    }

    public void atualizaAcessos(String url) {
        RotinaDao db = new RotinaDao();
        Rotina rotina = db.pesquisaAcesso(url);
        if (rotina == null) {
            return;
        }
        Usuario usuario = new Usuario();
        if (Sessions.exists("sessaoUsuario")) {
            usuario = (Usuario) Sessions.getObject("sessaoUsuario");
        }
    }

    // CHAMADA DE PAGINAS GENERICAS-------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    public synchronized Object chamadaGenerica(String pagina) {
        pagina = converteURL(pagina);
        Object object = null;
        try {
            object = this.getClass().getMethod(pagina).invoke(this);
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
        }
        return object;
    }

    public static String metodoGenerico(int tipo, String pagina) {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlAtual = converteURL(paginaRequerida.getRequestURI());
        Sessions.put("urlRetorno", urlAtual);
        Sessions.put("linkClicado", true);
        carregaPagina = true;
        tipoPagina = tipo;
        //atualizaAcessos('"' + "/ClinicaIntegrada/" + pagina + ".jsf" + '"');
        String cliente = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
        String userName;
        if (Sessions.exists("userName")) {
            userName = (String) Sessions.getString("userName");
            String logaccess = " Cliente: " + cliente + " - Usuário: " + userName + " - Página: " + pagina;
        }
        return pagina;
    }

    // SEGURANÇA
    public synchronized String permissao() {
        Sessions.remove("permissaoBean");
        Sessions.put("idModulo", SEGURANCA);
        return metodoGenerico(2, "permissao");
    }

    public synchronized String rotina() {
        Sessions.remove("rotinaBean");
        Sessions.put("idModulo", SEGURANCA);
        return metodoGenerico(2, "rotina");
    }

    public synchronized String permissaoDepartamento() {
        Sessions.remove("permissaoBean");
        Sessions.put("idModulo", SEGURANCA);
        return metodoGenerico(2, "permissaoDepartamento");
    }

    public synchronized String registroEmpresarial() {
        Sessions.remove("registroEmpresarialBean");
        return metodoGenerico(2, "registroEmpresarial");
    }

    public synchronized String evento() {
        Sessions.remove("eventoBean");
        Sessions.put("idModulo", SEGURANCA);
        return simples("Evento", "Evento");
    }

    public synchronized String modulo() {
        Sessions.remove("moduloBean");
        Sessions.put("idModulo", SEGURANCA);
        return simples("Modulo", "Modulo");
    }

    public synchronized String departamento() {
        Sessions.remove("departamentoBean");
        Sessions.put("idModulo", SEGURANCA);
        return simples("Departamento", "Departamento");
    }

//
    // CHAMADA DE MENUS 
    // 
    public synchronized String menuPrincipal() {
        return metodoGenerico(0, "menuPrincipal");
    }

    public synchronized String menuAdministrativo() {
        Sessions.put("idModulo", ADMINISTRATIVO);
        return metodoGenerico(0, "menuAdministrativo");
    }

    public synchronized String menuEquipeTecnica() {
        Sessions.put("idModulo", FICHA_TECNICA);
        return metodoGenerico(0, "menuEquipeTecnica");
    }

    public synchronized String menuCoordenacao() {
        Sessions.put("idModulo", COORDENACAO);
        return metodoGenerico(0, "menuCoordenacao");
    }

    public synchronized String menuFinanceiro() {
        Sessions.put("idModulo", FINANCEIRO);
        return metodoGenerico(0, "menuFinanceiro");
    }

    //
    // CADASTROS SIMPLES    
    //
    public synchronized String simples() {
        return metodoGenerico(2, "simples");
    }

    public synchronized String logradouro() {
        return simples("Logradouro", "Logradouro");
    }

    public synchronized String descricaoEndereco() {
        return simples("DescricaoEndereco", "Descrição do Endereço");
    }

    public synchronized String bairro() {
        return simples("Bairro", "Bairro");
    }

    public synchronized String tipoEndereco() {
        return simples("TipoEndereco", "Tipo de Endereço");
    }

    public synchronized String tipoDocumento() {
        return simples("TipoDocumento", "Tipo de Documento");
    }

    public synchronized String grupoAgenda() {
        return simples("GrupoAgenda", "Grupo Agenda");
    }

    public synchronized String bandas() {
        return simples("Banda", "Cadastro Bandas");
    }

    public synchronized String nivel() {
        return simples("Nivel", "Cadastro Níveis");
    }

    public synchronized String cor() {
        return simples("Cor", "Cores");
    }

    public String simples(String object, String title) {
        String[] lista = new String[]{object, title};
        Sessions.put("cadastroSimples", lista);
        Sessions.remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    // OUTROS
    // Lista Breadcrumbs e Menu url de retornos de páginas
    public String converteURLNome(String strURLNome) {
        if (strURLNome.equals("simples")) {
            if (Sessions.exists("chamadaPaginaSimples")) {
                String[] simplesString = (String[]) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaSimples");
                return simplesString[1];
            }
        }
        RotinaDao rotinaDB = new RotinaDao();
        Rotina r = rotinaDB.pesquisaRotinaPorPagina(strURLNome, true);
        String nomePagina = " Menu ";
        if (r.getId() != -1) {
            if (!r.getRotina().equals("")) {
                nomePagina = AnaliseString.converterCapitalize(r.getRotina());
                return nomePagina;
            }
        }
        return nomePagina;
    }

    public static String converteURL(String urlDest) {
        return urlDest.substring(urlDest.lastIndexOf("/") + 1, urlDest.lastIndexOf("."));
    }

//    @SuppressWarnings("unchecked")S
//    public DataObject getDtObjectLabel() {
//        return dtObjectLabel;
//    }
//
//    public void setDtObjectLabel(DataObject dtObjectLabel) {
//        this.dtObjectLabel = dtObjectLabel;
//    }
    public void refreshForm() {
    }

    public String getControleLinks() {
        String urlDestino = ((HttpServletRequest) (FacesContext.getCurrentInstance().getExternalContext().getRequest())).getRequestURI();
        String linkAtual = converteURL(urlDestino);
        String linkTeste = (String) Sessions.getString("urlRetorno");
        getControleLinks(urlDestino, linkAtual, linkTeste);
        return null;
    }

    public void getControleLinks(String urlDestino, String linkAtual, String linkTeste) {
        try {
            if (linkTeste == null) {
                linkTeste = "";
            }
            if (linkAtual.equals("acessoNegado") || !Sessions.exists("indicaAcesso")) {
                carregaPagina = false;
                return;
            }
            if (linkAtual.equals("sessaoExpirou") || !Sessions.exists("indicaAcesso")) {
                carregaPagina = false;
                return;
            }
            if (linkAtual.equals("menuPrincipal") || linkAtual.equals("menuPrincipalAcessoWeb") || linkAtual.equals("menuPrincipalSuporteWeb")) {
                carregaPagina = true;
                nivelLink = 0;
            }
            if (carregaPagina) {
                linkClicado = false;
                boolean isNivel = false;
                boolean acessoCadastro = (Boolean) Sessions.getBoolean("acessoCadastro");
                if (nivelLink >= 0 && nivelLink <= 9) {
                    isNivel = true;
                    carregaPagina = false;
                }
                if (nivelLink >= 3 && nivelLink <= 9) {
                    Sessions.put("acessoCadastro", false);
                }
                int nivel = 0;
                switch (nivelLink) {
                    case 0:
                        switch ((String) Sessions.getString("indicaAcesso")) {
                            case "local":
                                limparMenuLinks(-1);
                                menuLinks.add(new MenuLinks(0, "menuPrincipal", "Menu Principal", true));
                                isNivel = false;
                                break;
                            case "web":
                                limparMenuLinks(-1);
                                menuLinks.add(new MenuLinks(0, "menuPrincipalAcessoWeb", "Menu Principal", true));
                                limparMenuLinks(-1);
                                isNivel = false;
                                break;
                            case "suporteWeb":
                                menuLinks.add(new MenuLinks(0, "menuPrincipalSuporteWeb", "Menu Principal Suporte Web", true));
                                isNivel = false;
//                            dtObject.setArgumento0("menuPrincipalSuporteWeb");
//                            dtObjectLabel.setArgumento0("Menu Principal Suporte Web");
                                break;
                        }
                        nivel = nivelLink;
                        nivelLink = 1;
                        break;
                    case 1:
                        nivel = nivelLink;
                        nivelLink = 2;
                        break;
                    case 2:
                        nivel = nivelLink;
                        nivelLink = 3;
                        break;
                    case 3:
                        if (acessoCadastro) {
                            nivel = 2;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 4;
                        }
                        break;
                    case 4:
                        if (acessoCadastro) {
                            nivel = 3;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 5;
                        }
                        break;
                    case 5:
                        if (acessoCadastro) {
                            nivel = 4;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 6;
                        }
                        break;
                    case 6:
                        if (acessoCadastro) {
                            nivel = 5;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 7;
                        }
                        break;
                    case 7:
                        if (acessoCadastro) {
                            nivel = 6;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 8;
                        }
                        break;
                    case 8:
                        if (acessoCadastro) {
                            nivel = 7;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 9;
                        }
                        break;
                    case 9:
                        if (acessoCadastro) {
                            nivel = 8;
                            menuLinks.remove(nivel);
                        } else {
                            nivel = nivelLink;
                            nivelLink = 10;
                        }
                        break;
                }
                if (acessoCadastro) {
//                    int count = 0;
//                    List<MenuLinks> listRemove = new ArrayList<MenuLinks>();
//                    for (int i = 0; i < menuLinks.size(); i++) {
//                        if (menuLinks.get(i).getLink().equals(linkAtual)) {
//                            listRemove.add(menuLinks.get(i));
//                            count++;
//                        }
//                    }
//                    if (count > 0) {
//                        for (int i = 0; i < menuLinks.size(); i++) {
//                            for (int x = 0; x < listRemove.size(); x++) {
//                                count = 0;
//                                if (menuLinks.get(i).getLink().equals(listRemove.get(x).getLink())) {
//                                    if (count == 0) {
//                                        count = 1;
//                                    }
//                                    if (count == 1) {
//                                        menuLinks.remove(i);
//                                    }
//                                }
//                            }
//                        }
//                    }
                }
                if (isNivel) {
                    menuLinks.add(nivel, new MenuLinks(nivel, linkAtual, converteURLNome(linkAtual), true));
                }
            } else if (linkTeste.equals(linkAtual)) {
                for (int x = 0; x < menuLinks.size(); x++) {
                    if (menuLinks.get(x).getLink().equals(linkTeste)) {
                        limparMenuLinks(x);
                        break;
                    }
                }
            } else {
                for (int x = 0; x < menuLinks.size(); x++) {
                    if (menuLinks.get(x).getLink().equals(linkAtual)) {
                        limparMenuLinks(x);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Logger logger = new Logger();
            logger.novo("controleLinks", e.getMessage());
        }
    }

    public List<Rotina> getListRotina() {
        Dao dao = new Dao();
        if (listRotina.isEmpty()) {
            listRotina = dao.list(new Rotina(), true);
        }
        return listRotina;
    }

    public void setListRotina(List<Rotina> listaRotina) {
        this.listRotina = listaRotina;
    }

    public Boolean isRenderPesquisa() {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlAtual = converteURL(paginaRequerida.getRequestURI());
        if (urlAtual.equals("menuPrincipalAcessoWeb")) {
            renderPesquisa = false;
        }
        return renderPesquisa;
    }

    public void setRenderPesquisa(Boolean renderPesquisa) {
        this.renderPesquisa = renderPesquisa;
    }

    public List<MenuLinks> getMenuLinks() {
        return menuLinks;
    }

    public void setMenuLinks(List<MenuLinks> menuLinks) {
        this.menuLinks = menuLinks;
    }

    public void limparMenuLinks(int indice) {
        for (int i = 0; i < menuLinks.size(); i++) {
            if (indice < i) {
                menuLinks.remove(i);
            }
            if (indice == -1) {
                menuLinks.clear();
                break;
            }
        }
    }

    public String cliqueMenuLinks(int i) {
        linkClicado = true;
        Sessions.put("linkClicado", true);
        if (menuLinks.get(i).getLink().equals("")) {
            menuLinks.get(i - 1).setIndice(i);
            return menuLinks.get(i - 1).getLink();
        }
        nivelLink = i + 1;
        menuLinks.get(i).setIndice(i);
        return menuLinks.get(i).getLink();
    }

    public String getUrlAtual() {
        return urlAtual;
    }

    public void setUrlAtual(String urlAtual) {
        ChamadaPaginaBean.urlAtual = urlAtual;
    }

    public String getPenultimoLink() {
        if (menuLinks.isEmpty() || menuLinks.size() == 1) {
            penultimoLink = "menuPrincipal";
        } else {
            int posicao = menuLinks.size() - 2;
            penultimoLink = menuLinks.get(posicao).getLink();
        }
        return penultimoLink;
    }

    public void setPenultimoLink(String penultimoLink) {
        this.penultimoLink = penultimoLink;
    }

    /**
     * Link Clicado
     */
    public static void link() {
        Sessions.put("linkClicado", true);
    }

    /**
     * Botão voltar
     *
     * @return
     */
    public String back() {
        Sessions.put("linkClicado", true);
        return urlAtual;
    }

    /**
     * Botão voltar
     *
     * @param destroyBean (Apaga a sessão do bean)
     * @return
     */
    public String back(String destroyBean) {
        Sessions.remove(destroyBean);
        Sessions.put("linkClicado", true);
        return urlAtual;
    }

    public void backButton() {
        this.disabled = true;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }
}
