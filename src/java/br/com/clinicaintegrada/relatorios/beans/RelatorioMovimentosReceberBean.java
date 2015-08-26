package br.com.clinicaintegrada.relatorios.beans;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.principal.DBExternal;
import br.com.clinicaintegrada.relatorios.RelatorioOrdem;
import br.com.clinicaintegrada.relatorios.Relatorios;
import br.com.clinicaintegrada.relatorios.dao.RelatorioDao;
import br.com.clinicaintegrada.relatorios.dao.RelatorioMovimentosDao;
import br.com.clinicaintegrada.relatorios.dao.RelatorioOrdemDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Jasper;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.PF;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.component.accordionpanel.AccordionPanel;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class RelatorioMovimentosReceberBean implements Serializable {

    private Boolean[] filter;
    private Boolean[] disabled;
    private Integer[] index;
    private Integer contrato_numero;
    private Pessoa pessoa;
    /**
     * 0 - Relatórios; 1 - Relatório Ordem; 2 - Filiais
     */
    private List<SelectItem>[] listSelectItem;
    private Date dataContrato[];
    private Date dataVencimento[];
    private Date dataBaixa[];
    private String tipoRelatorio;
    private String tipo = "paciente";
    private String indexAccordion;
    private String order;
    private Relatorios relatorios;
    private Map<String, Integer> listServicos;
    private List selectedServicos;

    @PostConstruct
    public void init() {
        filter = new Boolean[5];
        filter[0] = false; // CONTRATO NÚMERO
        filter[1] = false; // PESSOA
        filter[2] = false; // SERVIÇOS
        filter[3] = false; // DATA
        filter[4] = false; // FILIAL
        listSelectItem = new ArrayList[3];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        dataContrato = new Date[2];
        dataContrato[0] = DataHoje.dataHoje();
        dataContrato[1] = DataHoje.dataHoje();
        dataVencimento = new Date[2];
        dataVencimento[0] = DataHoje.dataHoje();
        dataVencimento[1] = DataHoje.dataHoje();
        dataBaixa = new Date[2];
        dataBaixa[0] = DataHoje.dataHoje();
        dataBaixa[1] = DataHoje.dataHoje();
        index = new Integer[3];
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        contrato_numero = null;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        order = "PA.nome";
        pessoa = new Pessoa();
        relatorios = new Relatorios();
        selectedServicos = null;
        listServicos = null;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("relatorioMovimentosReceber");
        Sessions.remove("pessoaPesquisa");
    }

    public void print() throws FileNotFoundException {
        print(0);
    }

    public void print(int tcase) throws FileNotFoundException {
        Relatorios r = null;
        if (!getListRelatorios().isEmpty()) {
            Dao dao = new Dao();
            if (!getListRelatorios().isEmpty()) {
                r = (Relatorios) dao.find(new Relatorios(), index[0]);
            }
        } else {
            Messages.info("Sistema", "Nenhum relatório encontrado!");
            return;
        }
        if (r == null) {
            return;
        }
        String detalheRelatorio = "";
        Integer pessoa_id = null;
        String data_contrato_I = "";
        String data_contrato_F = "";
        String data_vencimento_I = "";
        String data_vencimento_F = "";
        String data_baixa_I = "";
        String data_baixa_F = "";
        List listDetalhePesquisa = new ArrayList();
        if (filter[0]) {
            listDetalhePesquisa.add("Contrato nº " + contrato_numero);
        }
        if (filter[1]) {
            if (pessoa.getId() != -1) {
                pessoa_id = pessoa.getId();
                listDetalhePesquisa.add("Pessoa " + tipo + ": " + pessoa.getDocumento() + " - " + pessoa.getNome());
            }
        }
        if (filter[3]) {
            data_contrato_I = DataHoje.converteData(dataContrato[0]);
            data_contrato_F = DataHoje.converteData(dataContrato[1]);
            data_vencimento_I = DataHoje.converteData(dataVencimento[0]);
            data_vencimento_F = DataHoje.converteData(dataVencimento[1]);
            data_baixa_I = DataHoje.converteData(dataBaixa[0]);
            data_baixa_F = DataHoje.converteData(dataBaixa[1]);
        }
        if (filter[4]) {
            listDetalhePesquisa.add("Filial: " + ((Filial) new Dao().find(new Filial(), index[2])).getFilial().getPessoa().getNome());
        }
        RelatorioMovimentosDao rmd = new RelatorioMovimentosDao();
        rmd.setRelatorios(r);
        rmd.setIS_QUERY(r.getMontaQuery());
        String in_servicos = inIdServicos();
        String in_filial = "" + index[2];
        rmd.setRelatorios(r);
        List list;
        if (!getListaRelatorioOrdem().isEmpty()) {
            RelatorioOrdem ro = (RelatorioOrdem) new Dao().find(new RelatorioOrdem(), index[1]);
            if (ro != null) {
                rmd.setOrder(ro.getQuery());
            }
        }
        if(index[0] == 13) {
            list = rmd.findResume(SessaoCliente.get().getId(), contrato_numero, tipo, pessoa_id, in_servicos, data_contrato_I, data_contrato_F, data_vencimento_I, data_vencimento_F, data_baixa_I, data_baixa_F, in_filial);
        } else {
            list = rmd.find(SessaoCliente.get().getId(), contrato_numero, tipo, pessoa_id, in_servicos, data_contrato_I, data_contrato_F, data_vencimento_I, data_vencimento_F, data_baixa_I, data_baixa_F, in_filial);
        }
        DBExternal dBExternal = new DBExternal();
        if (!r.getMontaQuery() && rmd.getQUERY().isEmpty()) {
            if (list.isEmpty()) {
                Messages.info("Sistema", "Não existem registros para o relatório selecionado");
                return;
            }
        } else {
            if (r.getMontaQuery()) {
                dBExternal.setDatabase(SessaoCliente.get().getPersistence());
                dBExternal.setPassword("989899");
                dBExternal.setUrl("192.168.1.60");
                Jasper.IS_REPORT_CONNECTION = true;
                Jasper.dbe = dBExternal;
                Jasper.IS_QUERY_STRING = r.getMontaQuery();
            }
        }
        if (listDetalhePesquisa.isEmpty()) {
            detalheRelatorio += "Pesquisar todos registros!";
        } else {
            detalheRelatorio += "";
            for (int i = 0; i < listDetalhePesquisa.size(); i++) {
                if (i == 0) {
                    detalheRelatorio += "" + listDetalhePesquisa.get(i).toString();
                } else {
                    detalheRelatorio += "; " + listDetalhePesquisa.get(i).toString();
                }
            }
        }
        if (r.getMontaQuery()) {
            if (!rmd.getQUERY().isEmpty()) {
                if (!r.getQueryString().isEmpty()) {
                    Jasper.QUERY_STRING = r.getQueryString();
                } else {
                    Jasper.QUERY_STRING = rmd.getQUERY();
                }
            }
        }
        Jasper.TITLE = r.getNome();
        Map map = new HashMap();
        map.put("relatorio_detalhes", detalheRelatorio);
        Jasper.printReports(r.getJasper(), r.getNome() + " " + (tipo != null ? tipo : ""), (Collection) new ArrayList(), map);

    }

    public List<SelectItem> getListRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao relatorioDao = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) relatorioDao.findByRotina(120);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = list.get(i).getId();
                }
                listSelectItem[0].add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public String getTipoRelatorio() {
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        clear();
        this.tipoRelatorio = tipoRelatorio;
    }

    public void tipoRelatorioChange(TabChangeEvent event) {
        tipoRelatorio = event.getTab().getTitle();
        indexAccordion = ((AccordionPanel) event.getComponent()).getActiveIndex();
        if (tipoRelatorio.equals("Simples")) {
            clear();
        }
    }

    public void clear() {
        if (!filter[0]) {
            contrato_numero = null;
        }
        if (!filter[1]) {
            pessoa = new Pessoa();
        }
        if (!filter[2]) {
            selectedServicos = null;
            listServicos = null;
        }
        if (!filter[3]) {
            dataContrato = new Date[2];
            dataContrato[0] = DataHoje.dataHoje();
            dataContrato[1] = DataHoje.dataHoje();
            dataVencimento = new Date[2];
            dataVencimento[0] = DataHoje.dataHoje();
            dataVencimento[1] = DataHoje.dataHoje();
            dataBaixa = new Date[2];
            dataBaixa[0] = DataHoje.dataHoje();
            dataBaixa[1] = DataHoje.dataHoje();
        }
        if (!filter[4]) {
            index[2] = null;
            listSelectItem[2].clear();
        }
    }

    public void clear(Integer tcase) {
        switch (tcase) {
            case 0:
                break;
            case 1:
                listSelectItem[1].clear();
                break;
        }
    }

    public void close(String close) {
        switch (close) {
            case "contrato_numero":
                contrato_numero = null;
                filter[0] = false;
                break;
            case "pessoa":
                tipo = null;
                filter[1] = false;
                pessoa = new Pessoa();
                break;
            case "servicos":
                filter[2] = false;
                listServicos = null;
                selectedServicos = null;
                break;
            case "data":
                filter[3] = false;
                dataContrato = new Date[2];
                dataContrato[0] = DataHoje.dataHoje();
                dataContrato[1] = DataHoje.dataHoje();
                dataVencimento = new Date[2];
                dataVencimento[0] = DataHoje.dataHoje();
                dataVencimento[1] = DataHoje.dataHoje();
                dataBaixa = new Date[2];
                dataBaixa[0] = DataHoje.dataHoje();
                dataBaixa[1] = DataHoje.dataHoje();
                break;
            case "filial":
                listSelectItem[2].clear();
                index[2] = null;
                break;
        }
        PF.update("form_relatorio:id_panel");
    }

    public String getIndexAccordion() {
        return indexAccordion;
    }

    public void setIndexAccordion(String indexAccordion) {
        this.indexAccordion = indexAccordion;
    }

    public List<SelectItem>[] getListSelectItem() {
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * <strong>Index</strong>
     * <ul>
     * <li>[0] Tipos de Relatórios</li>
     * <li>[1] List[SelectItem] Convenção Período</li>
     * </ul>
     *
     * @return Integer
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    /**
     * <strong>Filtros</strong>
     * <ul>
     * <li>[0] PESQUISA FÍSICA/li>
     * <li>[1] CIDADE</li>
     * <li>[2] DATAS</li>
     * <li>[3] -- </li>
     * <li>[4] SEXO</li>
     * <li>[5] IDADE</li>
     * </ul>
     *
     * @return boolean
     */
    public Boolean[] getFilter() {
        return filter;
    }

    public void setFilter(Boolean[] filter) {
        this.filter = filter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Pessoa getPessoa() {
        if (Sessions.exists("pessoaPesquisa")) {
            pessoa = ((Pessoa) Sessions.getObject("pessoaPesquisa", true));
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Relatorios getRelatorios() {
        try {
            if (relatorios != null) {
                if (!Objects.equals(relatorios.getId(), index[0])) {
                    Jasper.EXPORT_TO = false;
                }
                relatorios = (Relatorios) new Dao().find(new Relatorios(), index[0]);
            }
        } catch (Exception e) {
            relatorios = new Relatorios();
            Jasper.EXPORT_TO = false;
        }
        return relatorios;
    }

    public String inIdServicos() {
        String ids = "";
        if (selectedServicos != null) {
            for (int i = 0; i < selectedServicos.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedServicos.get(i);
                } else {
                    ids += "," + selectedServicos.get(i);
                }
            }
        }
        return ids;
    }

    public List<SelectItem> getListaRelatorioOrdem() {
        if (listSelectItem[1].isEmpty()) {
            if (index[0] != null && !getListRelatorios().isEmpty()) {
                RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
                List<RelatorioOrdem> list = relatorioOrdemDao.findAllByRelatorio(index[0]);
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        index[1] = list.get(i).getId();
                    }
                    if (list.get(i).getPrincipal()) {
                        index[1] = list.get(i).getId();
                    }
                    listSelectItem[1].add(new SelectItem(list.get(i).getId(), list.get(i).getNome()));
                }
            }
        }
        return listSelectItem[1];
    }

    public void selectedDataVencimentoI(SelectEvent event) {
        this.dataVencimento[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataVencimentoF(SelectEvent event) {
        this.dataVencimento[1] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataBaixaI(SelectEvent event) {
        this.dataBaixa[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataBaixaF(SelectEvent event) {
        this.dataBaixa[1] = DataHoje.converteSelectEvent(event);
    }

    public List<SelectItem> getListFiliais() {
        if (listSelectItem[2].isEmpty()) {
            List<Filial> list = (List<Filial>) new FilialDao().pesquisaTodasCliente();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[2] = list.get(i).getId();
                }
                listSelectItem[2].add(new SelectItem(list.get(i).getId(), list.get(i).getFilial().getPessoa().getNome()));
            }
            if (listSelectItem[2].isEmpty()) {
                listSelectItem[2] = new ArrayList<>();
            }
        }
        return listSelectItem[2];
    }

    public Date[] getDataContrato() {
        return dataContrato;
    }

    public void setDataContrato(Date[] dataContrato) {
        this.dataContrato = dataContrato;
    }

    public void selectedDataContratoI(SelectEvent event) {
        this.dataContrato[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataContratoF(SelectEvent event) {
        this.dataContrato[1] = DataHoje.converteSelectEvent(event);
    }

    public void listener(Integer tcase) {
    }

    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public Integer getContrato_numero() {
        return contrato_numero;
    }

    public void setContrato_numero(Integer contrato_numero) {
        this.contrato_numero = contrato_numero;
    }

    public Date[] getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date[] dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date[] getDataBaixa() {
        return dataBaixa;
    }

    public void setDataBaixa(Date[] dataBaixa) {
        this.dataBaixa = dataBaixa;
    }

    public void setRelatorios(Relatorios relatorios) {
        this.relatorios = relatorios;
    }

    public Map<String, Integer> getListServicos() {
        if (listServicos == null) {
            listServicos = null;
            listServicos = new LinkedHashMap<>();
            List<Servicos> list = new ServicosDao().findAllByMovimentoGroup(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listServicos.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listServicos;
    }

    public void setListServicos(Map<String, Integer> listServicos) {
        this.listServicos = listServicos;
    }

    public List getSelectedServicos() {
        return selectedServicos;
    }

    public void setSelectedServicos(List selectedServicos) {
        this.selectedServicos = selectedServicos;
    }
}
