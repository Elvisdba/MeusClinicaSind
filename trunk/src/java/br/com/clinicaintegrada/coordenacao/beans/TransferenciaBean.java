package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.dao.TransferenciaDao;
import br.com.clinicaintegrada.coordenacao.Transferencia;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
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
public class TransferenciaBean implements Serializable {

    private Transferencia transferencia;
    private List<Transferencia> listTransferencia;
    private List<Transferencia> listTransferenciaConsulta;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] data;
    private String[] hora;
    private String type;
    private Boolean[] filter;
    private Boolean transfer;
    private String by;
    private String description;
    private String startFinish;
    private Integer[] idTransfer;

    @PostConstruct
    public void init() {
        transferencia = new Transferencia();
        listTransferencia = new ArrayList<>();
        listTransferenciaConsulta = new ArrayList<>();
        index = new Integer[4];
        index[0] = null;
        index[1] = null;
        index[2] = null;
        index[3] = null;
        data = new String[2];
        idTransfer = new Integer[2];
        idTransfer[0] = null;
        idTransfer[1] = null;
        data[0] = "";
        data[1] = "";
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        hora = new String[2];
        hora[0] = DataHoje.livre(new Date(), "HH:mm");
        hora[1] = "";
        type = "hoje";
        by = "hoje";
        description = "";
        startFinish = "";
        transfer = false;
        filter = new Boolean[1];
        filter[0] = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("transferenciaBean");
    }

    public void clear() {
        Sessions.remove("transferenciaBean");
    }

    public void clear(int tCase) {
        if (tCase == 0) {
            Sessions.remove("transferenciaBean");
        }
        if (tCase == 1) {
            listSelectItem[1].clear();
        }
        if (tCase == 2) {
            if (!transferencia.getDataSaidaString().isEmpty()) {
                if (transferencia.getDataChegadaString().isEmpty()) {
                    hora[1] = "";
                } else {
                    Integer n1 = DataHoje.converteDataParaInteger(transferencia.getDataSaidaString());
                    Integer n2 = DataHoje.converteDataParaInteger(transferencia.getDataChegadaString());
                    if (n2 < n1) {
                        transferencia.setDataChegadaString(transferencia.getDataSaidaString());
                    }
                }
            }
        }
    }

    public void save() {
        if (transferencia.getContrato() == null) {
            Messages.warn("Validação", "Pesquisar contrato!");
            return;
        }
        if (transferencia.getEquipe() == null) {
            Messages.warn("Validação", "Pesquisar equipe!");
            return;
        }
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (listSelectItem[0].isEmpty() || listSelectItem[1].isEmpty()) {
            Messages.warn("Validação", "Cadastrar filiais(s)! É necessário mais de uma.");
            return;
        }
        if (index[0] == null) {
            Messages.warn("Validação", "Selecionar filial de saída!");
            return;
        }
        if (index[1] == null) {
            Messages.warn("Validação", "Selecionar filial de chegada!");
            return;
        }
        if (transferencia.getDataSaidaString().isEmpty()) {
            Messages.warn("Validação", "Informar data de saída!");
            return;
        }
        if (hora[0].isEmpty()) {
            Messages.warn("Validação", "Informar horário de saída!");
            return;
        }
        transferencia.setHoraSaida(hora[0]);
        transferencia.setHoraChegada(hora[1]);
        if (transferencia.getHoraChegada().equals(transferencia.getHoraSaida())) {
            Messages.warn("Validação", "A hora final deve ser maior que a hora inicial!");
            return;
        }
        transferencia.setFilialAtual(((Filial) dao.find(new Filial(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()))));
        transferencia.setFilialDestino(((Filial) dao.find(new Filial(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription()))));
        if (!transferencia.getDataChegadaString().isEmpty()) {
            transferencia.getContrato().setFilialAtual(transferencia.getFilialDestino());
        } else {
            transferencia.getContrato().setFilialAtual(transferencia.getFilialAtual());
        }
        if (transferencia.getId() == -1) {
            TransferenciaDao transferenciaDao = new TransferenciaDao();
            if (transferenciaDao.exists(transferencia.getContrato().getId(), transferencia.getFilialAtual().getId(), transferencia.getFilialDestino().getId(), transferencia.getDataSaidaString())) {
                Messages.warn("Validação", "O transferencia para este paciente esta agendado para esta data!");
                return;
            }
            dao.openTransaction();
            if (dao.save(transferencia)) {
                if (dao.update(transferencia.getContrato())) {
                    dao.commit();
                    Messages.info("Sucesso", "Transferência adicionada");
                    logger.save(
                            "ID: [" + transferencia.getId() + "]"
                            + " - Filial Atual: [" + transferencia.getFilialAtual().getId() + "] - " + transferencia.getFilialAtual().getFilial().getPessoa().getNome()
                            + " - Filial Destino: [" + transferencia.getFilialDestino().getId() + "] - " + transferencia.getFilialDestino().getFilial().getPessoa().getNome()
                            + " - Data Saída: " + transferencia.getDataSaidaString()
                            + " - Horário: " + transferencia.getHoraSaida()
                            + " - Data Chegada: " + transferencia.getDataChegadaString()
                            + " - Horário: " + transferencia.getHoraChegada()
                            + " - Contrato (Filial Atual):[ " + transferencia.getContrato().getFilialAtual().getId() + "] - " + transferencia.getContrato().getFilialAtual().getFilial().getPessoa().getNome()
                    );
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Ao atualizar filial do contrato!");
                }
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao adicionar transferência!");
            }
        } else {
            Transferencia t = (Transferencia) dao.find(transferencia);
            String beforeUpdate
                    = "ID: [" + t.getId() + "]"
                    + " - Filial Atual: [" + t.getFilialAtual().getId() + "] - " + t.getFilialAtual().getFilial().getPessoa().getNome()
                    + " - Filial Destino: [" + t.getFilialDestino().getId() + "] - " + t.getFilialDestino().getFilial().getPessoa().getNome()
                    + " - Data Saída: " + t.getDataSaidaString()
                    + " - Horário: " + t.getHoraSaida()
                    + " - Data Chegada: " + t.getDataChegadaString()
                    + " - Horário: " + t.getHoraChegada()
                    + " - Contrato (Filial Atual):[ " + t.getContrato().getFilialAtual().getId() + "] - " + t.getContrato().getFilialAtual().getFilial().getPessoa().getNome();
            dao.openTransaction();
            if (dao.update(transferencia)) {
                if (dao.update(transferencia.getContrato())) {
                    dao.commit();
                    Messages.info("Sucesso", "Transferência atualizada");
                    logger.update(beforeUpdate,
                            "ID: [" + transferencia.getId() + "]"
                            + " - Filial Atual: [" + transferencia.getFilialAtual().getId() + "] - " + transferencia.getFilialAtual().getFilial().getPessoa().getNome()
                            + " - Filial Destino: [" + transferencia.getFilialDestino().getId() + "] - " + transferencia.getFilialDestino().getFilial().getPessoa().getNome()
                            + " - Data Saída: " + transferencia.getDataSaidaString()
                            + " - Horário: " + transferencia.getHoraSaida()
                            + " - Data Chegada: " + transferencia.getDataChegadaString()
                            + " - Horário: " + transferencia.getHoraChegada()
                            + " - Contrato (Filial Atual):[ " + transferencia.getContrato().getFilialAtual().getId() + "] - " + transferencia.getContrato().getFilialAtual().getFilial().getPessoa().getNome()
                    );
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Ao atualizar filial do contrato!");
                }
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao atualizar transferência!");
            }
        }
    }

    public void delete() {
        delete(transferencia);
        clear(0);
    }

    public void delete(Transferencia t) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (t.getId() != -1) {
            dao.openTransaction();
            if (dao.delete(t)) {
                t.getContrato().setFilialAtual(t.getFilialAtual());
                if (dao.update(t.getContrato())) {
                    dao.commit();
                    Messages.info("Sucesso", "Registro removido");
                    logger.delete(
                            "ID: [" + t.getId() + "]"
                            + " - Filial Atual: [" + t.getFilialAtual().getId() + "] - " + t.getFilialAtual().getFilial().getPessoa().getNome()
                            + " - Filial Destino: [" + t.getFilialDestino().getId() + "] - " + t.getFilialDestino().getFilial().getPessoa().getNome()
                            + " - Data Saída: " + t.getDataSaidaString()
                            + " - Horário: " + t.getHoraSaida()
                            + " - Data Chegada: " + t.getDataChegadaString()
                            + " - Horário: " + t.getHoraChegada()
                            + " - Contrato (Filial Atual):[ " + t.getContrato().getFilialAtual().getId() + "] - " + t.getContrato().getFilialAtual().getFilial().getPessoa().getNome()
                    );
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Ao atualizar filial do contrato!");
                }
                clear(0);
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public String edit(Transferencia t) {
        transferencia = t;
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (transferencia.getFilialAtual().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        for (int i = 0; i < listSelectItem[1].size(); i++) {
            if (transferencia.getFilialDestino().getId() == Integer.parseInt(listSelectItem[1].get(i).getDescription())) {
                index[1] = i;
                break;
            }
        }
        Sessions.put("linkClicado", true);
        return "transferencia";
    }

    /**
     * 0 - Filial Atual - 1 Filial Destino
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        FilialDao filialDao = new FilialDao();
        List<Filial> list = filialDao.pesquisaTodasCliente();
        if (listSelectItem[0].isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    if (!transfer) {
                        index[0] = i;
                    } else {
                        if (idTransfer[1] == list.get(i).getId()) {
                            index[0] = i;
                        }
                    }
                } else {
                    if (idTransfer[1] != null) {
                        if (idTransfer[1] == list.get(i).getId()) {
                            index[0] = i;
                        }
                    }
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        if (listSelectItem[1].isEmpty()) {
            if (index[0] != null) {
                int j = 0;
                Integer id = Integer.parseInt(listSelectItem[0].get(index[0]).getDescription());
                for (int i = 0; i < list.size(); i++) {
                    if (id != list.get(i).getId()) {
                        if (!transfer) {
                            if (j == 0) {
                                index[1] = j;
                            }
                            listSelectItem[1].add(new SelectItem(j, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
                            j++;
                        } else {
                            if (idTransfer[1] == list.get(i).getId()) {
                                index[1] = j;
                                listSelectItem[1].add(new SelectItem(j, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
                                j++;
                            }
                        }
                    }
                }
            }
        }
        transfer = false;
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * 0 - Filial Atual - 1 - Filial Destino
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public List<Transferencia> getListTransferencia() {
        if (listTransferencia.isEmpty()) {
            if (transferencia.getContrato() != null) {
                TransferenciaDao transferenciaDao = new TransferenciaDao();
                listTransferencia = transferenciaDao.findAll(SessaoCliente.get().getId(), data[0], data[1], transferencia.getContrato().getId());
            }
        }
        return listTransferencia;
    }

    public void setListTransferencia(List<Transferencia> listTransferencia) {
        this.listTransferencia = listTransferencia;
    }

    public String[] getHora() {
        if (!hora[0].isEmpty()) {
            int ti = DataHoje.convertTimeToInteger(hora[0]);
            int tf = DataHoje.convertTimeToInteger(hora[1]);
            if (tf < ti) {
                if (!transferencia.getDataChegadaString().isEmpty()) {
                    if (transferencia.getDataChegadaString().equals(transferencia.getDataSaidaString())) {
                        hora[1] = hora[0];
                    }
                }
            }
        }
        if (!transferencia.getDataChegadaString().isEmpty()) {
            if (transferencia.getDataChegadaString().equals(transferencia.getDataSaidaString())) {
                if (!hora[1].isEmpty()) {
                    int ti = DataHoje.convertTimeToInteger(hora[0]);
                    int tf = DataHoje.convertTimeToInteger(hora[1]);
                    if (ti > tf) {
                        hora[0] = hora[1];
                    }
                }
            }
        }
        return hora;
    }

    public void setHora(String[] hora) {
        this.hora = hora;
    }

    public Transferencia getTransferencia() {
        if (Sessions.exists("contratoPesquisa")) {
            transferencia.setContrato((Contrato) Sessions.getObject("contratoPesquisa", true));
            if (!listSelectItem[0].isEmpty()) {
                listSelectItem[0].clear();
                getListSelectItem();
                for (int i = 0; i < listSelectItem[0].size(); i++) {
                    if (transferencia.getContrato().getFilialAtual().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                        index[0] = i;
                    }
                }
            }
        }
        if (Sessions.exists("equipePesquisa")) {
            transferencia.setEquipe((Equipe) Sessions.getObject("equipePesquisa", true));
        }
        return transferencia;
    }

    public void setTransferencia(Transferencia transferencia) {
        this.transferencia = transferencia;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void searchInit() {
        startFinish = "I";
        listTransferencia.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listTransferencia.clear();
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public void transferFilial() {
        if (index[0] != null && index[1] != null) {
            idTransfer[0] = Integer.parseInt(listSelectItem[0].get(index[0]).getDescription());
            idTransfer[1] = Integer.parseInt(listSelectItem[1].get(index[1]).getDescription());
            transfer = true;
            listSelectItem[0].clear();
            listSelectItem[1].clear();
            getListSelectItem();
        }
    }

    public Boolean getTransfer() {
        return transfer;
    }

    public void setTransfer(Boolean transfer) {
        this.transfer = transfer;
    }

    public Boolean[] getFilter() {
        try {
            filter[0] = transferencia.getContrato() != null && transferencia.getContrato().getId() != -1;
        } catch (Exception e) {
            filter[0] = false;
        }
        return filter;
    }

    public void setFilter(Boolean[] filter) {
        this.filter = filter;
    }

    public List<Transferencia> getListTransferenciaConsulta() {
        if (listTransferencia.isEmpty()) {
            getListSelectItem();
            Integer idFilialAtual = null;
            Integer idFilialDestino = null;
            if (index[2] != null) {
                idFilialAtual = Integer.parseInt(listSelectItem[0].get(index[2]).getDescription());
            }
            if (index[3] != null) {
                idFilialDestino = Integer.parseInt(listSelectItem[0].get(index[3]).getDescription());
            }
            switch (by) {
                case "hoje":
                    data[0] = DataHoje.data();
                    data[1] = "";
                    break;
                case "saida":
                case "chegada":
                case "lancamento":
                    break;
            }
            TransferenciaDao transferenciaDao = new TransferenciaDao();
            listTransferenciaConsulta = transferenciaDao.findAll(SessaoCliente.get().getId(), data[0], data[1], startFinish, by, description, idFilialAtual, idFilialDestino);
        }
        return listTransferenciaConsulta;
    }

    public void setListTransferenciaConsulta(List<Transferencia> listTransferenciaConsulta) {
        this.listTransferenciaConsulta = listTransferenciaConsulta;
    }

}
