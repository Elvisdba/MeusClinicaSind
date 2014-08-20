package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.agenda.dao.GrupoAgendaDao;
import br.com.rtools.utilitarios.Dao;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class GrupoAgendaBean {

    private GrupoAgenda grupoAgenda = new GrupoAgenda();
    private String comoPesquisa = "P";
    private String descPesquisa = "";
    private String msgConfirma;
    private String linkVoltar;

    public GrupoAgendaBean() {
//        htmlTable = new HtmlDataTable();
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

    public String salvar() {
        GrupoAgendaDao grupoAgendaDB = new GrupoAgendaDao();
        Dao acumuladoDB = new Dao();
        if (grupoAgenda.getId() == -1) {
            if (grupoAgenda.getDescricao().equals("")) {
                msgConfirma = "Digite um grupo de Agenda!";
            } else {
                if (grupoAgendaDB.idGrupoAgenda(grupoAgenda) == null) {
                    acumuladoDB.openTransaction();
                    if (acumuladoDB.save(grupoAgenda)) {
                        acumuladoDB.commit();
                        msgConfirma = "Grupo de agenda salvo com Sucesso!";
                    } else {
                        acumuladoDB.rollback();
                    }
                } else {
                    acumuladoDB.rollback();
                    msgConfirma = "Este grupo de agenda já existe no Sistema.";
                }
            }
        } else {
            acumuladoDB.openTransaction();
            if (acumuladoDB.update(grupoAgenda)) {
                acumuladoDB.commit();
                msgConfirma = "Grupo de agenda atualizado com Sucesso!";
            } else {
                acumuladoDB.rollback();
            }
        }
        return null;
    }

    public String novo() {
        grupoAgenda = new GrupoAgenda();
        msgConfirma = "";
        return "cadGrupoAgenda";
    }

    public String excluir() {
        GrupoAgendaDao db = new GrupoAgendaDao();
        if (grupoAgenda.getId() != -1) {
            Dao acumuladoDB = new Dao();
            grupoAgenda = db.pesquisaCodigo(grupoAgenda.getId());
            acumuladoDB.openTransaction();
            if (acumuladoDB.delete(grupoAgenda)) {
                acumuladoDB.commit();
                msgConfirma = "Grupo de agenda Excluido com Sucesso!";
            } else {
                acumuladoDB.rollback();
                msgConfirma = "Grupo de agenda não pode ser Excluido!";
            }
        }
        grupoAgenda = new GrupoAgenda();
        return null;
    }

    public List getListaGrupoAgenda() {
        GrupoAgendaDao db = new GrupoAgendaDao();
        List result = null;
        result = db.pesquisaGrupoAgenda(descPesquisa, comoPesquisa);
        return result;
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

    public void refreshForm() {
    }

    public String pesquisarGrupoAgenda() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadGrupoAgenda");
        descPesquisa = "";
        return "pesquisaGrupoAgenda";
    }

    public String pesquisarGrupoAgendaParaAgenda() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "cadAgenda");
        return "pesquisaGrupoAgenda";
    }

    public String editar() {
        String result = "";
//        grupoAgenda = (GrupoAgenda) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno").equals("cadGrupoAgenda")) {
            result = "cadGrupoAgenda";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("grupoAgendaPesquisa", grupoAgenda);
            grupoAgenda = new GrupoAgenda();
            result = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        }
        return result;
    }

    public String linkVoltar() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "menuPrincipal";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String linkVoltarPesquisaGrupoAgenda() {
        linkVoltar = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if (linkVoltar == null) {
            return "cadGrupoAgenda";
        } else {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("urlRetorno");
        }
        return linkVoltar;
    }
}
