package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Escala;
import br.com.clinicaintegrada.coordenacao.Resgate;
import br.com.clinicaintegrada.coordenacao.dao.EscalaDao;
import br.com.clinicaintegrada.coordenacao.dao.ResgateDao;
import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.Veiculo;
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
public class ResgateBean implements Serializable {

    private Resgate resgate;
    private Endereco endereco;
    private List<Resgate> listResgate;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] data;
    private String[] hora;
    private String type;
    private String filterType;
    private Boolean[] filter;
    private Boolean[] rendered;
    private String by;
    private String description;
    private String startFinish;

    @PostConstruct
    public void init() {
        resgate = new Resgate();
        endereco = new Endereco();
        listResgate = new ArrayList<>();
        index = new Integer[5];
        index[0] = null;
        index[1] = null;
        index[2] = null;
        index[3] = null;
        index[4] = null;
        data = new String[2];
        data[0] = "";
        data[1] = "";
        listSelectItem = new ArrayList[5];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        listSelectItem[3] = new ArrayList<>();
        listSelectItem[4] = new ArrayList<>();
        hora = new String[2];
        hora[0] = DataHoje.livre(new Date(), "HH:mm");
        hora[1] = "";
        type = "hoje";
        filterType = "hoje";
        filter = new Boolean[2];
        filter[0] = false;
        filter[1] = false;
        rendered = new Boolean[5];
        rendered[0] = false;
        rendered[1] = false;
        rendered[2] = false;
        rendered[3] = false;
        rendered[4] = false;
        by = "hoje";
        description = "";
        startFinish = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("resgateBean");
        Sessions.remove("typeSearch");
    }

    public void clear() {
        Sessions.remove("resgateBean");
    }

    public void clear(int tCase) {
        // LIMPAR DATAS
        if (tCase == 0) {
            Sessions.remove("resgateBean");
        }
        if (tCase == 1 || tCase == 2) {
            data[0] = "";
            data[1] = "";
        }
        if (tCase == 2) {
            listResgate.clear();
            resgate = new Resgate();
            hora[0] = DataHoje.livre(new Date(), "HH:mm");
            hora[1] = "";
        }
        if (tCase == 3) {
            resgate.setSolicitante(null);
        }
        if (tCase == 4) {
            resgate.setTecnico(null);
        }
        if (tCase == 5) {
            index[2] = null;
            index[3] = null;
            index[4] = null;
            listSelectItem[2].clear();
            listSelectItem[3].clear();
            listSelectItem[4].clear();
        }
        if (tCase == 6) {
            index[3] = null;
            index[4] = null;
            listSelectItem[3].clear();
            listSelectItem[4].clear();
        }
        if (tCase == 7) {
            index[4] = null;
            listSelectItem[4].clear();
        }
        if (tCase == 8) {
            if (resgate.getDataSaidaString().isEmpty() || hora[0].isEmpty()) {
                rendered[0] = false;
                rendered[1] = false;
                rendered[2] = false;
                rendered[3] = false;
                index[1] = null;
                index[2] = null;
                index[3] = null;
                index[4] = null;
                listSelectItem[1].clear();
                listSelectItem[2].clear();
                listSelectItem[3].clear();
                listSelectItem[4].clear();
            }
        }
        if (tCase == 9) {
            resgate.setMotorista(null);
        }
    }

    public void save() {
        if (resgate.getPaciente() == null) {
            Messages.warn("Validação", "Pesquisar paciente!");
            return;
        }
        resgate.setEndereco(endereco);
        if (resgate.getNumero().isEmpty()) {
            Messages.warn("Validação", "Informar número do endereço!");
            return;
        }
        if (resgate.getTecnico() == null) {
            Messages.warn("Validação", "Pesquisar técnico!");
            return;
        }
        if (resgate.getMotorista() == null) {
            Messages.warn("Validação", "Pesquisar motorista!");
            return;
        }
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (listSelectItem[0].isEmpty()) {
            Messages.warn("Validação", "Cadastrar veículo(s)!");
            return;
        }
        if (hora[0].isEmpty()) {
            Messages.warn("Validação", "Informar horário de saída!");
            return;
        }
//        if (!resgate.getDataSaidaString().isEmpty()) {
//            if (hora[1].isEmpty()) {
//                Messages.warn("Validação", "Informar data e horário de retorno!");
//                return;
//            }
//        }
        resgate.setHoraSaida(hora[0]);
        resgate.setHoraRetorno(hora[1]);
        if (resgate.getHoraRetorno().equals(resgate.getHoraSaida())) {
            Messages.warn("Validação", "A hora final deve ser maior que a hora inicial!");
            return;
        }
        if (index[0] == null) {
            Messages.warn("Validação", "Selecionar um veículo!");
            return;
        }
        resgate.setVeiculo(((Veiculo) dao.find(new Veiculo(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()))));
        if (endereco.getId() == -1) {
            Messages.warn("Validação", "Pesquisar endereço!");
            return;
        }
        if (index[1] == null) {
            Messages.warn("Validação", "Selecionar uma equipe de apoio!");
            return;
        }
        resgate.setApoio1(((Escala) dao.find(new Escala(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription()))));
        if (rendered[1]) {
            if (index[2] == null) {
                Messages.warn("Validação", "Selecionar uma equipe de apoio 2!");
                return;
            }
            resgate.setApoio2(((Escala) dao.find(new Escala(), Integer.parseInt(listSelectItem[2].get(index[2]).getDescription()))));
            if (rendered[2]) {
                if (index[3] == null) {
                    Messages.warn("Validação", "Selecionar uma equipe de apoio 3!");
                    return;
                }
                resgate.setApoio3(((Escala) dao.find(new Escala(), Integer.parseInt(listSelectItem[3].get(index[3]).getDescription()))));
                if (rendered[3]) {
                    if (index[4] == null) {
                        Messages.warn("Validação", "Selecionar uma equipe de apoio 4!");
                        return;
                    }
                    resgate.setApoio4(((Escala) dao.find(new Escala(), Integer.parseInt(listSelectItem[4].get(index[4]).getDescription()))));
                }
            }
        }
        if (resgate.getId() == -1) {
            resgate.setUsuario((Usuario) Sessions.getObject("sessaoUsuario"));
            ResgateDao resgateDao = new ResgateDao();
            if (resgateDao.exists(resgate.getPaciente().getId(), resgate.getDataSaidaString())) {
                Messages.warn("Validação", "O resgate para este paciente esta agendado para esta data!");
                return;
            }
            if (dao.save(resgate, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: [" + resgate.getId() + "]"
                //                        + " - Data Resgate: " + resgate.getDataResgateString()
                //                        + " - Função: " + resgate.getFuncaoResgate().getDescricao()
                //                        + " - Hora inicial: " + resgate.getHoraInicial()
                //                        + " - Hora final: " + resgate.getHoraFinal()
                );
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            Resgate e = (Resgate) dao.find(resgate);
            String beforeUpdate
                    = "ID: [" + e.getId() + "]";
            if (dao.update(resgate, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: [" + e.getId() + "]"
                );
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        delete(resgate);
        clear(0);
    }

    public void delete(Resgate r) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (r.getId() != -1) {
            if (dao.delete(r, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        "ID: [" + r.getId() + "]"
                );
                clear(0);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public String edit(Resgate r) {
        resgate = r;
        endereco = r.getEndereco();
        hora[0] = r.getHoraSaida();
        hora[1] = r.getHoraRetorno();
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (resgate.getVeiculo().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        if (resgate.getApoio1() != null) {
            rendered[0] = true;
            for (int i = 0; i < listSelectItem[1].size(); i++) {
                if (resgate.getApoio1().getId() == Integer.parseInt(listSelectItem[1].get(i).getDescription())) {
                    index[1] = i;
                    break;
                }
            }
        } else {
            index[1] = null;
            rendered[0] = false;
        }
        if (resgate.getApoio2() != null) {
            rendered[1] = true;
            for (int i = 0; i < listSelectItem[2].size(); i++) {
                if (resgate.getApoio2().getId() == Integer.parseInt(listSelectItem[2].get(i).getDescription())) {
                    index[2] = i;
                    break;
                }
            }
        } else {
            index[2] = null;
            rendered[1] = false;
        }
        if (resgate.getApoio3() != null) {
            rendered[2] = true;
            for (int i = 0; i < listSelectItem[3].size(); i++) {
                if (resgate.getApoio3().getId() == Integer.parseInt(listSelectItem[3].get(i).getDescription())) {
                    index[3] = i;
                    break;
                }
            }
        } else {
            index[3] = null;
            rendered[2] = false;
        }
        if (resgate.getApoio4() != null) {
            rendered[3] = true;
            for (int i = 0; i < listSelectItem[4].size(); i++) {
                if (resgate.getApoio4().getId() == Integer.parseInt(listSelectItem[4].get(i).getDescription())) {
                    index[4] = i;
                    break;
                }
            }
        } else {
            index[4] = null;
            rendered[3] = false;
        }
        Sessions.put("linkClicado", true);
        return "resgate";
    }

    /**
     * 0 - Veículo - Apoios {1,2,3,4}
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<Veiculo> list = dao.list(new Veiculo());
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!resgate.getDataSaidaString().isEmpty() && !hora[0].isEmpty()) {
            EscalaDao escalaDao = new EscalaDao();
            if (listSelectItem[1].isEmpty()) {
                rendered[0] = false;
                List<Escala> list = escalaDao.findAllForResgate(resgate.getDataSaidaString(), hora[0], "");
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        rendered[0] = true;
                    }
                    listSelectItem[1].add(new SelectItem(i, list.get(i).getEquipe().getPessoa().getNome() + " - Função: " + list.get(i).getFuncaoEscala().getDescricao(), "" + list.get(i).getId()));
                }
            }
            if (listSelectItem[2].isEmpty() && index[1] != null) {
                rendered[1] = false;
                List<Escala> list = escalaDao.findAllForResgate(resgate.getDataSaidaString(), hora[0], inIdEscala());
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        rendered[1] = true;
                    }
                    listSelectItem[2].add(new SelectItem(i, list.get(i).getEquipe().getPessoa().getNome() + " - Função: " + list.get(i).getFuncaoEscala().getDescricao(), "" + list.get(i).getId()));
                }
            }
            if (listSelectItem[3].isEmpty() && index[2] != null) {
                rendered[2] = false;
                List<Escala> list = escalaDao.findAllForResgate(resgate.getDataSaidaString(), hora[0], inIdEscala());
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        rendered[2] = true;
                    }
                    listSelectItem[3].add(new SelectItem(i, list.get(i).getEquipe().getPessoa().getNome() + " - Função: " + list.get(i).getFuncaoEscala().getDescricao(), "" + list.get(i).getId()));
                }
            }
            if (listSelectItem[4].isEmpty() && index[3] != null) {
                rendered[3] = false;
                List<Escala> list = escalaDao.findAllForResgate(resgate.getDataSaidaString(), hora[0], inIdEscala());
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        rendered[3] = true;
                    }
                    listSelectItem[4].add(new SelectItem(i, list.get(i).getEquipe().getPessoa().getNome() + " - Função: " + list.get(i).getFuncaoEscala().getDescricao(), "" + list.get(i).getId()));
                }
            }
        }
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    public String inIdEscala() {
        String in = "";
        if (index[1] != null) {
            in += Integer.parseInt(listSelectItem[1].get(index[1]).getDescription());
        }
        if (index[2] != null) {
            in += "," + Integer.parseInt(listSelectItem[2].get(index[2]).getDescription());
        }
        if (index[3] != null) {
            in += "," + Integer.parseInt(listSelectItem[3].get(index[3]).getDescription());
        }
        return in;
    }

    /**
     * 0 - Veículo - Apoios {1,2,3,4}
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public List<Resgate> getListResgate() {
        if (listResgate.isEmpty()) {
            getListSelectItem();
            Integer idVeiculo = null;
            Boolean filtroData = false;
            switch (by) {
                case "evento":
                    if (index[1] != null) {
                        idVeiculo = Integer.parseInt(listSelectItem[0].get(index[1]).getDescription());
                    }
                    break;
                case "hoje":
                    data[0] = DataHoje.data();
                    data[1] = "";
                    filtroData = true;
                    break;
                case "emissao":
                case "saida":
                case "entrada":
                    filtroData = true;
                    break;
            }
            ResgateDao resgateDao = new ResgateDao();
            listResgate = resgateDao.findAll(SessaoCliente.get().getId(), null, data[0], data[1], startFinish, by, description, idVeiculo, filtroData);
        }
        return listResgate;
    }

    public void setListResgate(List<Resgate> listResgate) {
        this.listResgate = listResgate;
    }

    public String[] getHora() {
        if (!hora[0].isEmpty()) {
            int ti = DataHoje.convertTimeToInteger(hora[0]);
            int tf = DataHoje.convertTimeToInteger(hora[1]);
            if (tf < ti) {
                if (!resgate.getDataRetornoString().isEmpty()) {
                    if (resgate.getDataRetornoString().equals(resgate.getDataSaidaString())) {
                        hora[1] = hora[0];
                    }
                }
            }
        }
        if (!resgate.getDataRetornoString().isEmpty()) {
            if (resgate.getDataRetornoString().equals(resgate.getDataSaidaString())) {
                if (!hora[1].isEmpty()) {
                    int ti = DataHoje.convertTimeToInteger(hora[0]);
                    int tf = DataHoje.convertTimeToInteger(hora[1]);
                    if (ti > tf) {
                        hora[0] = hora[1];
                    }
                }
            }
        }
        clear(8);
        return hora;
    }

    public void setHora(String[] hora) {
        this.hora = hora;
    }

    public Resgate getResgate() {
        if (Sessions.exists("fisicaPesquisa")) {
            if (Sessions.exists("typeSearch")) {
                switch (Sessions.getString("typeSearch", true)) {
                    case "paciente":
                        resgate.setPaciente(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
                        break;
                    case "solicitante":
                        resgate.setSolicitante(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
                        if (resgate.getPaciente() != null && resgate.getPaciente().getId() != -1) {
                            if (resgate.getSolicitante() != null && resgate.getSolicitante().getId() != -1) {
                                if (resgate.getPaciente().getId() == resgate.getSolicitante().getId()) {
                                    resgate.setSolicitante(null);
                                    Messages.warn("Validação", "O solicitante não pode ser igual ao paciente!");
                                }
                            }
                        }
                        break;
                }
            }
        }
        if (Sessions.exists("equipePesquisa")) {
            if (Sessions.exists("typeSearch")) {
                switch (Sessions.getString("typeSearch", true)) {
                    case "tecnico":
                        resgate.setTecnico(((Equipe) Sessions.getObject("equipePesquisa", true)));
                        break;
                    case "motorista":
                        resgate.setMotorista(((Equipe) Sessions.getObject("equipePesquisa", true)));
                        break;

                }
            }
        }
        return resgate;
    }

    public void setResgate(Resgate resgate) {
        this.resgate = resgate;
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

    public Boolean[] getFilter() {
        try {
            filter[1] = false;
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
        listResgate.clear();
    }

    public void searchFinish() {
        startFinish = "P";
        listResgate.clear();
    }

    public String getStartFinish() {
        return startFinish;
    }

    public void setStartFinish(String startFinish) {
        this.startFinish = startFinish;
    }

    public Endereco getEndereco() {
        if (Sessions.exists("enderecoPesquisa")) {
            endereco = (Endereco) Sessions.getObject("enderecoPesquisa", true);
        }
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public void listenerTypeSearch(String tCase) {
        Sessions.put("typeSearch", tCase);
    }

    public void listenerEdit() {
        rendered[4] = true;
    }

    public Boolean[] getRendered() {
        return rendered;
    }

    public void setRendered(Boolean[] rendered) {
        this.rendered = rendered;
    }

    public String getKmTotal() {
        if (resgate.getKmInicial() != 0 && resgate.getKmFinal() != 0) {
            if (resgate.getKmFinal() > resgate.getKmInicial()) {
                Float t = resgate.getKmFinal() - resgate.getKmInicial();
                return "" + t;
            }
        }
        return "0";
    }

}
