package br.com.clinicaintegrada.seguranca.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Dao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
@SuppressWarnings("unchecked")
public class RotinaBean implements Serializable {

    private Rotina rotina;
    private String message;
    private String descricaoPesquisa;
    private List<Rotina> listRotina;

    @PostConstruct
    public void init() {
        rotina = new Rotina();
        message = "";
        descricaoPesquisa = "";
        listRotina = new ArrayList<Rotina>();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void save() {
        DaoInterface di = new Dao();
        Logger logger = new Logger();
        if (rotina.getId() == -1) {
            if (rotina.getRotina().equals("")) {
                message = "Digite uma Rotina!";
            } else {
                RotinaDao rotinaDB = new RotinaDao();
                if (!rotinaDB.existeRotina(rotina)) {
                    di.openTransaction();
                    if (di.save(rotina)) {
                        di.commit();
                        logger.save("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.getAtivo());
                        message = "Registro salvo com sucesso";
                        listRotina.clear();
                        descricaoPesquisa = "";
                    } else {
                        di.rollback();
                        message = "Erro ao inserir registro!";
                    }
                } else {
                    message = "Esta Rotina já existe no Sistema.";
                }
            }
        } else {
            Rotina r = (Rotina) di.find(rotina);
            String beforeUpdate = "ID: " + r.getId() + " - Rotina: " + r.getRotina() + " - Página: " + r.getRotina() + " - Ativa: " + r.getAtivo();
            di.openTransaction();
            if (di.update(rotina)) {
                logger.update(beforeUpdate, "ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.getAtivo());
                di.commit();
                listRotina.clear();
                descricaoPesquisa = "";
                message = "Registro atualizado com sucesso";
            } else {
                di.rollback();
                message = "Erro ao atualizar registro!";
            }
        }
    }

    public void clear() {
        Sessions.remove("rotinaBean");
    }

    public void delete() {
        DaoInterface di = new Dao();
        Logger logger = new Logger();
        if (rotina.getId() != -1) {
            di.openTransaction();
            if (di.delete(rotina)) {
                logger.delete("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.getAtivo());
                di.commit();
                descricaoPesquisa = "";
                listRotina.clear();
                message = "Registro excluido com sucesso";
            } else {
                di.rollback();
                message = "Esta registro não pode ser excluido!";
            }
        }
        rotina = new Rotina();
    }

    public String edit(Object o) {
        DaoInterface di = new Dao();
        rotina = new Rotina();
        rotina = (Rotina) di.rebind(o);
        Sessions.put("rotinaPesquisa", rotina);
        Sessions.put("linkClicado", true);
        if (Sessions.exists("urlRetorno")) {
            if (!Sessions.getString("urlRetorno").equals("menuPrincipal")) {
                return (String) Sessions.getString("urlRetorno");
            }
        } else {
            return "rotina";
        }
        return null;
    }

    public List<Rotina> getListRotina() {
        if (listRotina.isEmpty()) {
            Dao dao = new Dao();
            if (descricaoPesquisa.equals("")) {
                listRotina = dao.list(new Rotina(), true);
            } else {
                listRotina = dao.listQuery(new Rotina(), "findByRotina", new Object[]{"%" + descricaoPesquisa.toUpperCase() + "%"});
            }
        }
        return listRotina;
    }

    public void setListRotina(List<Rotina> listRotina) {
        this.listRotina = listRotina;
    }

    public String rotinaAtiva(boolean ativo) {
        if (ativo) {
            return "Ativo";
        }
        return "Inativo";
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public static Rotina getRotinaAtual() {
        try {
            return new RotinaDao().pesquisaRotinaPorPagina(curlURL(), true);
        } catch (Exception e) {
            return null;
        }
    }

    public static String curlURL() {
        String curl = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        return curl.substring(curl.lastIndexOf("/") + 1, curl.lastIndexOf("."));
    }
}
