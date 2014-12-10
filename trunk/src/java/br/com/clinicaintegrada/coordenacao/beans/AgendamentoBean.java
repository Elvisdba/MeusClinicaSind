package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.Evento;
import br.com.clinicaintegrada.coordenacao.dao.AgendamentoDao;
import br.com.clinicaintegrada.coordenacao.dao.EventoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AgendamentoBean implements Serializable {

    private Agendamento agendamento;
    private Agendamento agendamentoEdit;
    private List<Agendamento> listAgendamento;
    private List<Agendamento> listAgendamentoConsulta;
    private Contrato contrato;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] data;
    private String horaAgenda;
    private String by;
    private String description;
    private Boolean actives;
    private String startFinish;

    @PostConstruct
    public void init() {
        agendamento = new Agendamento();
        agendamentoEdit = new Agendamento();
        listAgendamento = new ArrayList<>();
        listAgendamentoConsulta = new ArrayList<>();
        contrato = new Contrato();
        index = new Integer[3];
        data = new String[2];
        data[0] = DataHoje.data();
        data[1] = DataHoje.data();
        index[0] = null;
        index[1] = null;
        index[2] = null;
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        horaAgenda = DataHoje.livre(new Date(), "HH:mm");
        by = "hoje";
        description = "";
        startFinish = "";
        actives = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("agendamentoBean");
        Sessions.remove("agendamentoPesquisa");
    }

    public void clear() {
        Sessions.remove("agendamentoBean");
    }

    public void clear(int tCase) {
        if (tCase == 0 || tCase == 1) {
            agendamento = new Agendamento();
            index[0] = null;
            horaAgenda = DataHoje.livre(new Date(), "HH:mm");
        }
        if (tCase == 1) {
            Sessions.remove("agendamentoBean");
        }
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        agendamento.setEvento((Evento) dao.find(new Evento(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription())));
        if (contrato.getId() == -1) {
            Messages.warn("Validação", "Pesquisar contrato!");
            return;
        }
        if (horaAgenda.isEmpty()) {
            Messages.warn("Validação", "Informar horário válido!");
            return;
        }
        agendamento.setHoraAgenda(horaAgenda);
        if (agendamento.getId() == -1) {
            agendamento.setAgendador((Usuario) Sessions.getObject("sessaoUsuario"));
            agendamento.setContrato(contrato);
            AgendamentoDao agendamentoDao = new AgendamentoDao();
            if (agendamentoDao.exists(contrato.getId(), agendamento.getAgenda(), agendamento.getHoraAgenda())) {
                Messages.warn("Validação", "Agendamento já cadastrado!");
                return;
            }
            if (dao.save(agendamento, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: " + agendamento.getId() + "]"
                        + " - Contrato: [" + agendamento.getContrato()
                        + " - Agenda: " + agendamento.getAgendaString()
                        + " - Hora: " + agendamento.getHoraAgenda()
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            Agendamento a = (Agendamento) dao.find(agendamento);
            String beforeUpdate
                    = "ID: " + a.getId() + "]"
                    + " - Contrato: [" + a.getContrato()
                    + " - Agenda: " + a.getAgendaString()
                    + " - Hora: " + a.getHoraAgenda();
            if (dao.update(agendamento, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: " + agendamento.getId() + "]"
                        + " - Contrato: [" + agendamento.getContrato()
                        + " - Agenda: " + agendamento.getAgendaString()
                        + " - Hora: " + agendamento.getHoraAgenda()
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        delete(agendamento);
        clear(0);
    }

    public void delete(Agendamento a) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (a.getId() != -1) {
            if (dao.delete(a, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        "ID: " + a.getId() + "]"
                        + " - Contrato: [" + a.getContrato()
                        + " - Agenda: " + a.getAgendaString()
                        + " - Hora: " + a.getHoraAgenda()
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public String edit(Agendamento a) {
        agendamento = a;
        horaAgenda = a.getHoraAgenda();
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (a.getEvento().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        return null;
    }

    public String editConsulta(Agendamento a) {
        agendamentoEdit = a;
        agendamentoEdit.setHoraAgenda(DataHoje.livre(new Date(), "HH:mm"));
        agendamentoEdit.setAgenda(new Date());
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (a.getEvento().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        return null;
    }

    public Contrato getContrato() {
        if (Sessions.exists("contratoPesquisa")) {
            getListAgendamento();
            contrato = (Contrato) Sessions.getObject("contratoPesquisa", true);
        }
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    /**
     * 0 - Grupo Evento; 1 - Evento
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            EventoDao eventoDao = new EventoDao();
            List<Evento> list = eventoDao.findAllByGrupoEvento(2);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * 0 - Evento
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public List<Agendamento> getListAgendamento() {
        if (listAgendamento.isEmpty()) {
            if (contrato.getId() != -1) {
                AgendamentoDao agendamentoDao = new AgendamentoDao();
                listAgendamento = agendamentoDao.findAllByContrato(contrato.getId(), actives);
            }
        }
        return listAgendamento;
    }

    public void setListAgendamento(List<Agendamento> listAgendamento) {
        this.listAgendamento = listAgendamento;
    }

    public String getHoraAgenda() {
        return horaAgenda;
    }

    public void setHoraAgenda(String horaAgenda) {
        if (!horaAgenda.isEmpty()) {
            this.horaAgenda = DataHoje.validaHora(horaAgenda);
        } else {
            this.horaAgenda = horaAgenda;
        }
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public List<Agendamento> getListAgendamentoConsulta() {
        if (listAgendamentoConsulta.isEmpty()) {
            AgendamentoDao agendamentoDao = new AgendamentoDao();
            getListSelectItem();
            Integer idEvento = null;
            switch (by) {
                case "evento":
                    if (index[1] != null) {
                        idEvento = Integer.parseInt(listSelectItem[0].get(index[1]).getDescription());
                    }
                    break;
                case "hoje":
                    data[0] = DataHoje.data();
                    data[1] = "";
                    break;
            }
            listAgendamentoConsulta = agendamentoDao.findAll(startFinish, by, description, data[0], data[1], SessaoCliente.get().getId(), idEvento);
        }
        return listAgendamentoConsulta;
    }

    public void setListAgendamentoConsulta(List<Agendamento> listAgendamentoConsulta) {
        this.listAgendamentoConsulta = listAgendamentoConsulta;
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

    public Boolean getActives() {
        return actives;
    }

    public void setActives(Boolean actives) {
        this.actives = actives;
    }

    public Agendamento getAgendamentoEdit() {
        return agendamentoEdit;
    }

    public void setAgendamentoEdit(Agendamento agendamentoEdit) {
        this.agendamentoEdit = agendamentoEdit;
    }

    public void searchInit() {
        startFinish = "I";
        listAgendamentoConsulta.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listAgendamentoConsulta.clear();
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

}
