package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Notificacao;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.PertenceEntrada;
import br.com.clinicaintegrada.coordenacao.TipoNotificacao;
import br.com.clinicaintegrada.coordenacao.dao.NotificacaoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class NotificacaoBean implements Serializable {

    private Notificacao notificacao;
    private List<Notificacao> listNotificacao;
    private List<Notificacao> listNotificacaoConsulta;
    private Contrato contrato;
    private Equipe equipe;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] data;
    private String type;
    private Boolean[] filter;
    private String by;
    private String description;
    private String startFinish;

    @PostConstruct
    public void init() {
        notificacao = new Notificacao();
        listNotificacao = new ArrayList<>();
        listNotificacaoConsulta = new ArrayList<>();
        equipe = new Equipe();
        contrato = new Contrato();
        data = new String[2];
        data[0] = "";
        data[1] = "";
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        listSelectItem = new ArrayList[1];
        listSelectItem[0] = new ArrayList<>();
        type = "equipe_id";
        filter = new Boolean[2];
        filter[0] = false;
        filter[1] = false;
        by = "hoje";
        description = "";
        startFinish = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("notificacaoBean");
    }

    public void clear() {
        Sessions.remove("notificacaoBean");
    }

    public void clear(int tCase) {
        // LIMPAR DATAS
        if (tCase == 0) {
            Sessions.remove("notificacaoBean");
        }
        if (tCase == 1 || tCase == 2) {
            data[0] = "";
            data[1] = "";
        }
        if (tCase == 2) {
            listNotificacao.clear();
            notificacao.setId(-1);
            notificacao.setEquipe(null);
            notificacao.setMotivo("");
        }
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (index[0] == null) {
            Messages.warn("Validação", "Selecionar uma tipo de notificação!");
            return;
        }
        if (listSelectItem[0].isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipos de notificação!");
            return;
        }
        notificacao.setTipoNotificacao(((TipoNotificacao) dao.find(new TipoNotificacao(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()))));
        if (notificacao.getContrato() == null || notificacao.getContrato().getId() == -1) {
            Messages.warn("Validação", "Pesquisar contrato!");
            return;
        }
        if (notificacao.getEquipe() == null || notificacao.getEquipe().getId() == -1) {
            Messages.warn("Validação", "Pesquisar equipe!");
            return;
        }
        if (notificacao.getId() == -1) {
            notificacao.setDataLancamento(DataHoje.dataHoje());
            NotificacaoDao notificacaoDao = new NotificacaoDao();
            if (notificacaoDao.exists(notificacao.getContrato().getId(), notificacao.getEquipe().getId(), notificacao.getTipoNotificacao().getId(), notificacao.getDataLancamento())) {
                Messages.warn("Validação", "Notificacao já cadastrado!");
                return;
            }
            if (dao.save(notificacao, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: [" + notificacao.getId() + "]"
                        + " - Contrato: [" + notificacao.getContrato().getId() + "]" + notificacao.getContrato().getPaciente().getNome()
                        + " - Equipe: [" + notificacao.getEquipe().getId() + "]" + notificacao.getEquipe().getPessoa().getNome()
                        + " - Tipo Notificação: [" + notificacao.getTipoNotificacao().getId() + "]" + notificacao.getTipoNotificacao().getDescricao()
                        + " - Data Notificacao: " + notificacao.getDataLancamentoString()
                );
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            Notificacao n = (Notificacao) dao.find(notificacao);
            String beforeUpdate
                    = "ID: [" + n.getId() + "]"
                    + " - Contrato: [" + n.getContrato().getId() + "]" + n.getContrato().getPaciente().getNome()
                    + " - Equipe: [" + n.getEquipe().getId() + "]" + n.getEquipe().getPessoa().getNome()
                    + " - Tipo Notificação: [" + n.getTipoNotificacao().getId() + "]" + n.getTipoNotificacao().getDescricao()
                    + " - Data Notificacao: " + n.getDataLancamentoString();
            if (dao.update(notificacao, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: [" + notificacao.getId() + "]"
                        + " - Contrato: [" + notificacao.getContrato().getId() + "]" + notificacao.getContrato().getPaciente().getNome()
                        + " - Equipe: [" + notificacao.getEquipe().getId() + "]" + notificacao.getEquipe().getPessoa().getNome()
                        + " - Tipo Notificação: [" + notificacao.getTipoNotificacao().getId() + "]" + notificacao.getTipoNotificacao().getDescricao()
                        + " - Data Notificacao: " + notificacao.getDataLancamentoString()
                );
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        delete(notificacao);
        clear(0);
    }

    public void delete(Notificacao o) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (o.getId() != -1) {
            if (dao.delete(o, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        "ID: [" + o.getId() + "]"
                        + " - Contrato: [" + o.getContrato().getId() + "]" + o.getContrato().getPaciente().getNome()
                        + " - Equipe: [" + o.getEquipe().getId() + "]" + o.getEquipe().getPessoa().getNome()
                        + " - Tipo Notificação: [" + o.getTipoNotificacao().getId() + "]" + o.getTipoNotificacao().getDescricao()
                        + " - Data Notificacao: " + o.getDataLancamentoString()
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public String edit(Object o) {
        Dao dao = new Dao();
        notificacao = (Notificacao) dao.rebind(o);
        contrato = notificacao.getContrato();
        equipe = notificacao.getEquipe();
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (notificacao.getTipoNotificacao().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        return null;
    }

    /**
     * 0 - TipoNotificacao
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<TipoNotificacao> list = dao.list(new TipoNotificacao());
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
     * 0 - Tipo Notificação 1 - Tipo Notificação (Filtro)
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public List<Notificacao> getListNotificacao() {
        if (listNotificacao.isEmpty()) {
            if (notificacao.getContrato() != null && notificacao.getContrato().getId() != -1) {
                NotificacaoDao notificacaoDao = new NotificacaoDao();
                listNotificacao = notificacaoDao.findAll(SessaoCliente.get().getId(), notificacao.getContrato().getId(), "contrato", data[0], data[1]);
            }
        }
        return listNotificacao;
    }

    public void setListNotificacao(List<Notificacao> listNotificacao) {
        this.listNotificacao = listNotificacao;
    }

    public Notificacao getNotificacao() {
        if (Sessions.exists("contratoPesquisa")) {
            notificacao.setContrato((Contrato) Sessions.getObject("contratoPesquisa", true));
        }
        if (Sessions.exists("equipePesquisa")) {
            notificacao.setEquipe((Equipe) Sessions.getObject("equipePesquisa", true));
        }
        return notificacao;
    }

    public void setNotificacao(Notificacao notificacao) {
        this.notificacao = notificacao;
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

        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Boolean[] getFilter() {
        try {
            filter[1] = notificacao.getContrato() != null && notificacao.getContrato().getId() != -1;
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

    public void searchInit() {
        startFinish = "I";
        listNotificacaoConsulta.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listNotificacaoConsulta.clear();
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public List<Notificacao> getListNotificacaoConsulta() {
        if (listNotificacaoConsulta.isEmpty()) {
            getListSelectItem();
            Integer idFuncaoNotificacao = null;
            Boolean filtroData = false;
            switch (by) {
                case "tipoNotificacao":
                    if (index[1] != null) {
                        idFuncaoNotificacao = Integer.parseInt(listSelectItem[0].get(index[1]).getDescription());
                    }
                    break;
                case "hoje":
                    data[0] = DataHoje.data();
                    data[1] = "";
                    filtroData = true;
                    break;
                case "lancamento":
                    filtroData = true;
                    break;
            }
            NotificacaoDao notificacaoDao = new NotificacaoDao();
            listNotificacaoConsulta = notificacaoDao.findAll(SessaoCliente.get().getId(), null, data[0], data[1], startFinish, by, description, idFuncaoNotificacao, filtroData);
        }
        return listNotificacaoConsulta;
    }

    public void setListNotificacaoConsulta(List<Notificacao> listNotificacaoConsulta) {
        this.listNotificacaoConsulta = listNotificacaoConsulta;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

}
