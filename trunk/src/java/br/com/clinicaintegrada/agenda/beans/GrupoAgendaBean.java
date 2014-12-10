package br.com.clinicaintegrada.agenda.beans;

import br.com.clinicaintegrada.agenda.GrupoAgenda;
import br.com.clinicaintegrada.agenda.dao.GrupoAgendaDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Sessions;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GrupoAgendaBean {

    private GrupoAgenda grupoAgenda;
    private String comoPesquisa;
    private String descPesquisa;
    private String msgConfirma;
    private String linkVoltar;

    @PostConstruct
    public void init() {
        grupoAgenda = new GrupoAgenda();
        comoPesquisa = "P";
        descPesquisa = "";
        msgConfirma = "";
        linkVoltar = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("grupoAgendaBean");
    }

    public void save() {
        GrupoAgendaDao grupoAgendaDB = new GrupoAgendaDao();
        Dao dao = new Dao();
        if (grupoAgenda.getId() == -1) {
            if (grupoAgenda.getDescricao().equals("")) {
                msgConfirma = "Digite um grupo de Agenda!";
            } else {
                if (grupoAgendaDB.idGrupoAgenda(grupoAgenda) == null) {
                    dao.openTransaction();
                    if (dao.save(grupoAgenda)) {
                        dao.commit();
                        msgConfirma = "Grupo de agenda salvo com Sucesso!";
                    } else {
                        dao.rollback();
                    }
                } else {
                    dao.rollback();
                    msgConfirma = "Este grupo de agenda já existe no Sistema.";
                }
            }
        } else {
            dao.openTransaction();
            if (dao.update(grupoAgenda)) {
                dao.commit();
                msgConfirma = "Grupo de agenda atualizado com Sucesso!";
            } else {
                dao.rollback();
            }
        }
    }

    public String clear() {
        grupoAgenda = new GrupoAgenda();
        msgConfirma = "";
        return "cadGrupoAgenda";
    }

    public void delete() {
        GrupoAgendaDao db = new GrupoAgendaDao();
        if (grupoAgenda.getId() != -1) {
            Dao dao = new Dao();
            grupoAgenda = db.pesquisaCodigo(grupoAgenda.getId());
            dao.openTransaction();
            if (dao.delete(grupoAgenda)) {
                dao.commit();
                msgConfirma = "Grupo de agenda Excluido com Sucesso!";
            } else {
                dao.rollback();
                msgConfirma = "Grupo de agenda não pode ser Excluido!";
            }
        }
        grupoAgenda = new GrupoAgenda();
    }

    public List getListaGrupoAgenda() {
        GrupoAgendaDao db = new GrupoAgendaDao();
        List list = db.pesquisaGrupoAgenda(descPesquisa, comoPesquisa);
        return list;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String pesquisarGrupoAgenda() {
        Sessions.put("urlRetorno", "cadGrupoAgenda");
        descPesquisa = "";
        return "pesquisaGrupoAgenda";
    }

    public String pesquisarGrupoAgendaParaAgenda() {
        Sessions.put("urlRetorno", "cadAgenda");
        return "pesquisaGrupoAgenda";
    }

    public String edit() {
        String result;
        Sessions.put("linkClicado", true);
        if (Sessions.getString("urlRetorno").equals("cadGrupoAgenda")) {
            result = "cadGrupoAgenda";
        } else {
            Sessions.put("grupoAgendaPesquisa", grupoAgenda);
            grupoAgenda = new GrupoAgenda();
            result = Sessions.getString("urlRetorno", true);
        }
        return result;
    }

    public String linkVoltar() {
        if (!Sessions.exists("urlRetorno")) {
            return "menuPrincipal";
        } else {
            return (String) Sessions.getString("urlRetorno");
        }
    }

    public String linkVoltarPesquisaGrupoAgenda() {
        linkVoltar = (String) Sessions.getString("urlRetorno");
        if (linkVoltar == null) {
            return "cadGrupoAgenda";
        } else {
            Sessions.remove("urlRetorno");
        }
        return linkVoltar;
    }

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}
