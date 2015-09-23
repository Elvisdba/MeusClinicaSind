package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.configuracao.ConfiguracaoCoordenacao;
import br.com.clinicaintegrada.configuracao.dao.ConfiguracaoCoordenacaoDao;
import br.com.clinicaintegrada.coordenacao.Escala;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.FuncaoEscala;
import br.com.clinicaintegrada.coordenacao.dao.EscalaDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
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
public class EscalaBean implements Serializable {

    private Escala escala;
    private List<Escala> listEscala;
    private List<Escala> listEscalaConsulta;
    private Contrato contrato;
    private Equipe equipe;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] data;
    private String[] hora;
    private String type;
    private String filterType;
    private Boolean[] filter;
    private String by;
    private String description;
    private Boolean disabled;
    private String startFinish;

    @PostConstruct
    public void init() {
        escala = new Escala();
        listEscala = new ArrayList<>();
        listEscalaConsulta = new ArrayList<>();
        equipe = new Equipe();
        contrato = new Contrato();
        index = new Integer[3];
        data = new String[2];
        data[0] = "";
        data[1] = "";
        index[0] = null;
        index[1] = null;
        index[2] = null;
        listSelectItem = new ArrayList[1];
        listSelectItem[0] = new ArrayList<>();
        hora = new String[2];
        hora[0] = DataHoje.livre(new Date(), "HH:mm");
        hora[1] = DataHoje.livre(new Date(), "HH:mm");
        type = "equipe_id";
        filterType = "equipe_id";
        filter = new Boolean[2];
        filter[0] = false;
        filter[1] = false;
        by = "hoje";
        description = "";
        startFinish = "";
        disabled = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("escalaBean");
    }

    public void clear() {
        Sessions.remove("escalaBean");
    }

    public void clear(int tCase) {
        // LIMPAR DATAS
        if (tCase == 0) {
            Sessions.remove("escalaBean");
        }
        if (tCase == 1 || tCase == 2) {
            data[0] = "";
            data[1] = "";
        }
        if (tCase == 2) {
            listEscala.clear();
            contrato = new Contrato();
            escala = new Escala();
            hora[0] = DataHoje.livre(new Date(), "HH:mm");
            hora[1] = DataHoje.livre(new Date(), "HH:mm");
        }
    }

    public void save() {
        ConfiguracaoCoordenacao cc = new ConfiguracaoCoordenacaoDao().findByCliente(SessaoCliente.get().getId());
        if (cc == null) {
            Messages.warn("Sistema", "Configurar Coordernação! Segurança > Configuração > Coordenação");
            return;
        }
        if (escala.getDataEscalaString().isEmpty()) {
            Messages.warn("Validação", "Informar data da escala!");
            return;
        }
        Dao dao = new Dao();
        // INICIA - CONFIGURAÇÃO > COORDENAÇÃO
        DataHoje dataHoje = new DataHoje();
        Integer dt1 = DataHoje.converteDataParaInteger(DataHoje.data());
        Integer dt2 = DataHoje.converteDataParaInteger(escala.getDataEscalaString());
        Integer dt3 = dt2;
        Integer dt4 = DataHoje.converteDataParaInteger(dataHoje.incrementarMeses(cc.getEscalaMaxMesesEscala(), DataHoje.data()));
        // VERIFICA SE PERMITE ESCALA RETROATIVO
        if (cc.getAgendamentoDataRetroativo() != null) {
            dt2 = DataHoje.converteDataParaInteger(DataHoje.converteData(cc.getAgendamentoDataRetroativo()));
            if (dt2 < dt1) {
                dt2 = dt3;
            }
        }
        if (dt2 <= dt1) {
            Messages.warn("Validação", "A data da escala não deve ser superior a data de hoje!");
            return;
        }
        // VERIFICA PERÍODO MÁXIMO DE MESES PARA AGENDAMENTO FUTURO
        if (dt3 > dt4) {
            Messages.warn("Validação", "A data da escala não deve ser superior a " + cc.getEscalaMaxMesesEscala() + " mese(s)");
            return;
        }
        // TERMINA - CONFIGURAÇÃO > COORDENAÇÃO        
        Logger logger = new Logger();
        if (index[0] == null) {
            Messages.warn("Validação", "Selecionar uma função!");
            return;
        }
        escala.setFuncaoEscala(((FuncaoEscala) dao.find(new FuncaoEscala(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()))));
        if (type.equals("equipe_id")) {
            if (equipe.getId() == -1) {
                Messages.warn("Validação", "Pesquisar equipe!");
                return;
            }
            escala.setEquipe(equipe);
        } else {
            if (contrato.getId() == null) {
                Messages.warn("Validação", "Pesquisar contrato!");
                return;
            }
            escala.setPaciente(contrato);
        }
        if (hora[0].isEmpty()) {
            Messages.warn("Validação", "Informar horário inicial válido!");
            return;
        }
        if (hora[1].isEmpty()) {
            Messages.warn("Validação", "Informar horário final válido!");
            return;
        }
        escala.setHoraInicial(hora[0]);
        escala.setHoraFinal(hora[1]);
        if (escala.getHoraInicial().equals(escala.getHoraFinal())) {
            Messages.warn("Validação", "A hora final deve ser maior que a hora inicial!");
            return;
        }
        String pacienteOuEquipe;
        if (escala.getId() == -1) {
            int id;
            EscalaDao escalaDao = new EscalaDao();
            if (type.equals("equipe_id")) {
                id = escala.getEquipe().getId();
            } else {
                id = escala.getPaciente().getId();
            }
            if (escalaDao.exists(type, id, escala.getDataEscala())) {
                Messages.warn("Validação", "Escala já cadastrado!");
                return;
            }
            if (dao.save(escala, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                if (escala.getPaciente() != null && escala.getPaciente().getId() != -1) {
                    pacienteOuEquipe = " - Paciente: [" + escala.getPaciente().getId() + "]";
                } else {
                    pacienteOuEquipe = " - Equipe: [" + escala.getEquipe().getId() + "]";
                }
                logger.save(
                        "ID: [" + escala.getId() + "]"
                        + pacienteOuEquipe
                        + " - Data Escala: " + escala.getDataEscalaString()
                        + " - Função: " + escala.getFuncaoEscala().getDescricao()
                        + " - Hora inicial: " + escala.getHoraInicial()
                        + " - Hora final: " + escala.getHoraFinal()
                );
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            Escala e = (Escala) dao.find(escala);
            if (escala.getPaciente() != null && e.getPaciente().getId() != -1) {
                pacienteOuEquipe = " - Paciente: [" + e.getPaciente().getId() + "]";
            } else {
                pacienteOuEquipe = " - Equipe: [" + e.getEquipe().getId() + "]";
            }
            String beforeUpdate
                    = "ID: [" + e.getId() + "]"
                    + pacienteOuEquipe
                    + " - Data Escala: " + e.getDataEscalaString()
                    + " - Função: " + e.getFuncaoEscala().getDescricao()
                    + " - Hora inicial: " + e.getHoraInicial()
                    + " - Hora final: " + e.getHoraFinal();
            if (dao.update(escala, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                if (escala.getPaciente() != null && escala.getPaciente().getId() != -1) {
                    pacienteOuEquipe = " - Paciente: [" + escala.getPaciente().getId() + "]";
                } else {
                    pacienteOuEquipe = " - Equipe: [" + escala.getEquipe().getId() + "]";
                }
                logger.update(beforeUpdate,
                        "ID: [" + e.getId() + "]"
                        + pacienteOuEquipe
                        + " - Data Escala: " + escala.getDataEscalaString()
                        + " - Função: " + escala.getFuncaoEscala().getDescricao()
                        + " - Hora inicial: " + escala.getHoraInicial()
                        + " - Hora final: " + escala.getHoraFinal()
                );
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        delete(escala);
        clear(0);
    }

    public void delete(Escala e) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (e.getId() != -1) {
            if (dao.delete(e, true)) {
                String pacienteOuEquipe;
                Messages.info("Sucesso", "Registro removido");
                if (e.getPaciente() != null && e.getPaciente().getId() != -1) {
                    pacienteOuEquipe = " - Paciente: [" + e.getPaciente().getId() + "]";
                } else {
                    pacienteOuEquipe = " - Equipe: [" + e.getEquipe().getId() + "]";
                }
                logger.delete(
                        "ID: [" + e.getId() + "]"
                        + pacienteOuEquipe
                        + " - Data Escala: " + e.getDataEscalaString()
                        + " - Função: " + e.getFuncaoEscala().getDescricao()
                        + " - Hora inicial: " + e.getHoraInicial()
                        + " - Hora final: " + e.getHoraFinal()
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public String edit(Object o) {
        if (escala.getPaciente() != null) {
            contrato = escala.getPaciente();
            type = "contrato";
        } else {
            equipe = escala.getEquipe();
            type = "equipe_id";
        }
        Dao dao = new Dao();
        escala = (Escala) dao.rebind(o);
        hora[0] = escala.getHoraInicial();
        hora[1] = escala.getHoraFinal();
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (escala.getFuncaoEscala().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        return null;
    }

    public Contrato getContrato() {
        if (Sessions.exists("contratoPesquisa")) {
            contrato = (Contrato) Sessions.getObject("contratoPesquisa", true);
        }
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    /**
     * 0 - FuncaoEscala
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<FuncaoEscala> list = dao.list(new FuncaoEscala());
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

    public List<Escala> getListEscala() {
        if (listEscala.isEmpty()) {
            Integer id = null;
            switch (type) {
                case "contrato":
                    if (contrato != null) {
                        if (contrato.getId() != -1) {
                            id = contrato.getId();
                        }
                    }
                    break;
                case "equipe_id":
                    if (equipe != null) {
                        if (equipe.getId() != -1) {
                            id = equipe.getId();
                        }
                    }
                    break;
            }
            EscalaDao escalaDao = new EscalaDao();
            listEscala = escalaDao.findAll(SessaoCliente.get().getId(), id, type, data[0], data[1]);
        }
        return listEscala;
    }

    public void setListEscala(List<Escala> listEscala) {
        this.listEscala = listEscala;
    }

    public String[] getHora() {
        if (!hora[0].isEmpty()) {
            int ti = DataHoje.convertTimeToInteger(hora[0]);
            int tf = DataHoje.convertTimeToInteger(hora[1]);
            if (tf < ti) {
                hora[1] = hora[0];
            }
        }
        if (!hora[1].isEmpty()) {
            int ti = DataHoje.convertTimeToInteger(hora[0]);
            int tf = DataHoje.convertTimeToInteger(hora[1]);
            if (ti > tf) {
                hora[0] = hora[1];
            }
        }
        return hora;
    }

    public void setHora(String[] hora) {
        this.hora = hora;
    }

    public Escala getEscala() {
        return escala;
    }

    public void setEscala(Escala escala) {
        this.escala = escala;
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

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public Equipe getEquipe() {
        if (Sessions.exists("equipePesquisa")) {
            equipe = (Equipe) Sessions.getObject("equipePesquisa", true);
        }
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Boolean[] getFilter() {
        try {
            filter[1] = (contrato != null && equipe != null) && (contrato.getId() != -1 || equipe.getId() != -1);
        } catch (Exception e) {
            filter[1] = false;
        }
        return filter;
    }

    public void setFilter(Boolean[] filter) {
        this.filter = filter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public void searchInit() {
        startFinish = "I";
        listEscalaConsulta.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listEscalaConsulta.clear();
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public List<Escala> getListEscalaConsulta() {
        if (listEscalaConsulta.isEmpty()) {
            getListSelectItem();
            Integer idFuncaoEscala = null;
            Boolean filtroData = false;
            switch (by) {
                case "evento":
                    if (index[1] != null) {
                        idFuncaoEscala = Integer.parseInt(listSelectItem[0].get(index[1]).getDescription());
                    }
                    break;
                case "hoje":
                    data[0] = DataHoje.data();
                    data[1] = "";
                    filtroData = true;
                    break;
                case "periodo":
                case "lancamento":
                    filtroData = true;
                    break;
            }
            EscalaDao escalaDao = new EscalaDao();
            listEscalaConsulta = escalaDao.findAll(SessaoCliente.get().getId(), null, data[0], data[1], startFinish, by, description, idFuncaoEscala, filtroData);
        }
        return listEscalaConsulta;
    }

    public void setListEscalaConsulta(List<Escala> listEscalaConsulta) {
        this.listEscalaConsulta = listEscalaConsulta;
    }

    /**
     * @param idFuncaoEscala
     */
    public void defineFuncaoEscala(Integer idFuncaoEscala) {
        if (!getListSelectItem()[0].isEmpty()) {
            disabled = true;
            for (int i = 0; i < listSelectItem[0].size(); i++) {
                if (Integer.parseInt(listSelectItem[0].get(i).getDescription()) == idFuncaoEscala) {
                    index[1] = i;
                    break;
                }
            }
        }
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

}
