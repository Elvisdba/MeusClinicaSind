package br.com.clinicaintegrada.fichamedica.beans;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.fichamedica.dao.MenuEquipeTecnicaDao;
import br.com.clinicaintegrada.pessoa.dao.EquipeDao;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class MenuEquipeTecnicaBean implements Serializable {

    private Agendamento agendamento;
    private List<Agendamento> listAgendamento;
    private Integer indexStatus;
    private String[] data;
    private String by;
    private String description;
    private String startFinish;

    @PostConstruct
    public void init() {
        agendamento = new Agendamento();
        listAgendamento = new ArrayList<>();
        indexStatus = 1;
        data = new String[2];
        data[0] = "";
        data[1] = "";
        by = "";
        description = "";
        startFinish = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("menuEquipeTecnicaBean");
    }

    public void clear() {
        Sessions.remove("menuEquipeTecnicaBean");
    }

    public void clear(Integer tCase) {
        if (tCase == 0) {
            Sessions.remove("menuEquipeTecnicaBean");
        } else if (tCase == 1) {
            description = "";
            data[0] = "";
            data[1] = "";
            listAgendamento.clear();
        }
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public List<Agendamento> getListAgendamento() {
        if (listAgendamento.isEmpty()) {
            MenuEquipeTecnicaDao metd = new MenuEquipeTecnicaDao();
            Integer idStatus;
            if(indexStatus == null) {
                idStatus = 1;
                indexStatus = 1;
            } else if (indexStatus == 1) {
                idStatus = 1;
            } else if (indexStatus == 2) {
                idStatus = 2;
            } else if (indexStatus == 3) {
                idStatus = 3;
            } else if (indexStatus == 4) {
                idStatus = 4;
            } else if (indexStatus == 5) {
                idStatus = 5;
            } else if (indexStatus == 6) {
                idStatus = 6;
            } else {
                idStatus = 0;
            }
            listAgendamento = metd.findAllAgendamentoByEquipe(idStatus, ((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId(), data, null, "", description, false, getEnfermaria());
        }
        return listAgendamento;
    }

    public void setListAgendamento(List<Agendamento> listAgendamento) {
        this.listAgendamento = listAgendamento;
    }

    public void changeTab(TabChangeEvent event) {
        listAgendamento.clear();
        indexStatus = ((TabView) event.getComponent()).getActiveIndex();
        if(indexStatus == null) {
            indexStatus = 1;
        }
    }

    public Integer getIndexStatus() {
        return indexStatus;
    }

    public void setIndexStatus(Integer indexStatus) {
        this.indexStatus = indexStatus;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public void searchInit() {
        startFinish = "I";
        listAgendamento.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listAgendamento.clear();
    }

    public Boolean getEnfermaria() {
        EquipeDao equipeDao = new EquipeDao();
        return equipeDao.findByPessoaAndProfissao(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId(), 251) != null;
    }
}
