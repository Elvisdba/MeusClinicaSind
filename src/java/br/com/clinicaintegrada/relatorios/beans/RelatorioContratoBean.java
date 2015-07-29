package br.com.clinicaintegrada.relatorios.beans;

import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.endereco.dao.CidadeDao;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.principal.DBExternal;
import br.com.clinicaintegrada.relatorios.RelatorioOrdem;
import br.com.clinicaintegrada.relatorios.Relatorios;
import br.com.clinicaintegrada.relatorios.dao.RelatorioContratoDao;
import br.com.clinicaintegrada.relatorios.dao.RelatorioDao;
import br.com.clinicaintegrada.relatorios.dao.RelatorioOrdemDao;
import br.com.clinicaintegrada.relatorios.impressao.ParametroFisica;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
// import br.com.clinicaintegrada.utils.AnaliseString;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
public class RelatorioContratoBean implements Serializable {

    private Boolean[] filter;
    private Integer[] idade;
    private Integer[] index;
    private Fisica paciente;
    private Fisica responsavel;
    /**
     * 0 - Relatórios; 1 - Relatório Ordem; 2 - Filiais
     */
    private List<SelectItem>[] listSelectItem;
    private Date dataContrato[];
    private Date dataInternacao[];
    private Date dataDesligamento[];
    private Date dataNascimento[];
    private String tipoRelatorio;
    private String tipo;
    private String indexAccordion;
    private String order;
    private String sexo;
    private Relatorios relatorios;
    private Map<String, Integer> listCidades;
    private List selectedCidades;
    private Map<String, Integer> listTipoInternacao;
    private List selectedTipoInternacao;
    private Map<String, Integer> listTipoDesligamento;
    private List selectedTipoDesligamento;

    @PostConstruct
    public void init() {
        filter = new Boolean[12];
        filter[0] = false; // PESQUISA FÍSICA
        filter[1] = false; // CIDADE
        filter[2] = false; // DATAS
        filter[3] = false; // FILIAL
        filter[4] = false; // SEXO        
        filter[5] = false; // IDADE
        listSelectItem = new ArrayList[3];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
        listSelectItem[2] = new ArrayList<>();
        dataContrato = new Date[2];
        dataContrato[0] = DataHoje.dataHoje();
        dataContrato[1] = DataHoje.dataHoje();
        dataDesligamento = new Date[2];
        dataDesligamento[0] = DataHoje.dataHoje();
        dataDesligamento[1] = DataHoje.dataHoje();
        dataInternacao = new Date[2];
        dataInternacao[0] = DataHoje.dataHoje();
        dataInternacao[1] = DataHoje.dataHoje();
        dataNascimento = new Date[2];
        dataNascimento[0] = DataHoje.dataHoje();
        dataNascimento[1] = DataHoje.dataHoje();
        index = new Integer[3];
        index[0] = 0;
        index[1] = 0;
        index[2] = 0;
        idade = new Integer[2];
        idade[0] = 0;
        idade[1] = 0;
        tipoRelatorio = "Simples";
        indexAccordion = "Simples";
        order = "PA.nome";
        paciente = new Fisica();
        responsavel = new Fisica();
        sexo = "";
        tipo = "todos";
        relatorios = new Relatorios();
        selectedCidades = null;
        listCidades = null;
        selectedTipoInternacao = null;
        listTipoInternacao = null;
        selectedTipoDesligamento = null;
        listTipoDesligamento = null;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("relatorioFisicaBean");
        Sessions.remove("fisicaPesquisa");
        Sessions.remove("tipo_pesquisa_fisica");
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
        Integer paciente_id = null;
        Integer responsavel_id = null;
        String data_contrato_I = "";
        String data_contrato_F = "";
        String data_rescisao_I = "";
        String data_rescisao_F = "";
        String data_cadastro_I = "";
        String data_cadastro_F = "";
        String data_nascimento_I = "";
        String data_nascimento_F = "";
        String sexoString = "";
        String dReferencia = "";
        List listDetalhePesquisa = new ArrayList();
        if (filter[2]) {
            data_contrato_I = DataHoje.converteData(dataContrato[0]);
            data_contrato_F = DataHoje.converteData(dataContrato[1]);
            data_rescisao_I = DataHoje.converteData(dataDesligamento[0]);
            data_rescisao_F = DataHoje.converteData(dataDesligamento[1]);
            data_cadastro_I = DataHoje.converteData(dataInternacao[0]);
            data_cadastro_F = DataHoje.converteData(dataInternacao[1]);
            data_nascimento_I = DataHoje.converteData(dataNascimento[0]);
            data_nascimento_F = DataHoje.converteData(dataNascimento[1]);
        }
        if (filter[4]) {
            switch (sexo) {
                case "M":
                    sexoString = "Masculino";
                    break;
                case "F":
                    sexoString = "Feminino";
                    break;
                default:
                    sexoString = "Todos";
                    break;
            }
            listDetalhePesquisa.add(" Sexo: " + sexoString + "");
        }
        if (paciente.getId() != -1) {
            paciente_id = paciente.getId();
            // listDetalhePesquisa.add(" Empresa por Física CPF: " + aluno.getDocumento() + " - " + aluno.getNome());
        }
        if (responsavel.getId() != -1) {
            responsavel_id = responsavel.getId();
            // listDetalhePesquisa.add(" Empresa por Física CPF: " + aluno.getDocumento() + " - " + aluno.getNome());
        }
        RelatorioContratoDao rcd = new RelatorioContratoDao();
        rcd.setRelatorios(r);
        rcd.setIS_QUERY(r.getMontaQuery());
        String in_cidades = inIdCidades();
        String in_tipo_internacao = inIdTipoInternacao();
        String in_tipo_desligamento = inIdTipoDesligamento();
        String in_filial_atual = "" + index[2];
        rcd.setRelatorios(r);
        List list;
        list = rcd.find(paciente_id, paciente_id, responsavel_id, in_cidades, data_contrato_I, data_contrato_F, data_contrato_I, data_contrato_F, data_rescisao_I, data_rescisao_F, data_nascimento_I, data_nascimento_F, idade, sexo, in_filial_atual, in_tipo_internacao, in_tipo_desligamento);
        DBExternal dBExternal = new DBExternal();
        if (!r.getMontaQuery() && rcd.getQUERY().isEmpty()) {
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
        List<ParametroFisica> pfs = new ArrayList<>();
        ParametroFisica pf;
        // ÍNDICE DA LISTA
        for (int i = 0; i < list.size(); i++) {
            // POSIÇÕES DOS ÍNDICES
            List object = (List) list.get(i);
            pf = new ParametroFisica();
            // PESSOA
            pf.setPessoa_nome(object.get(1));
            pf.setPessoa_documento(object.get(2));
            pf.setPessoa_telefone1(object.get(16));
            pf.setPessoa_telefone2(object.get(18));
            pf.setPessoa_cadastro(object.get(7));
            pf.setPessoa_email1(object.get(17));

            // PESSOA ENDEREÇO 
            String enderecoString = "";
            // LOGRADOURO
            if (object.get(8) == null) {
                pf.setPessoa_logradouro(object.get(8).toString());
                enderecoString += " " + object.get(8).toString();
            }
            // DESCRICAO ENDEREÇO
            if (object.get(9) == null) {
                pf.setPessoa_descricao_endereco(object.get(9).toString());
                enderecoString += " " + object.get(9).toString();
            }
            // NÚMERO
            if (object.get(10) == null) {
                pf.setPessoa_numero(object.get(10).toString());
                enderecoString += ", " + object.get(10).toString();
            }
            // COMPLEMENTO
            if (object.get(11) == null) {
                pf.setPessoa_complemento(object.get(11).toString());
                enderecoString += " " + object.get(11).toString();
            }
            // BAIRRO
            if (object.get(12) != null) {
                pf.setPessoa_bairro(object.get(12));
                enderecoString += " - " + object.get(12).toString();
            }
            // CIDADE
            pf.setPessoa_cidade(object.get(13));
            // UF
            if (object.get(14) != null) {
                pf.setPessoa_uf(object.get(12));
            }
            // CEP
            if (object.get(15) != null) {
                pf.setPessoa_cep(object.get(15));
                enderecoString += " - CEP: " + object.get(15).toString();
            }
            pf.setPessoa_endereco_completo(enderecoString);

            // FÍSICA
            pf.setFisica_nascimento(object.get(3));
            pf.setFisica_rg(object.get(6));
            pf.setFisica_sexo(object.get(5));
            pfs.add(pf);
        }
        if (!pfs.isEmpty() || r.getMontaQuery()) {
            if (r.getExcel()) {
                Jasper.EXCEL_FIELDS = r.getCamposExcel();
            } else {
                Jasper.EXCEL_FIELDS = "";
            }
            if (r.getMontaQuery()) {
                if (!rcd.getQUERY().isEmpty()) {
                    if (!r.getQueryString().isEmpty()) {
                        Jasper.QUERY_STRING = r.getQueryString();
                    } else {
                        Jasper.QUERY_STRING = rcd.getQUERY();
                    }
                }
            }
            Jasper.TITLE = r.getNome();
            Jasper.printReports(r.getJasper(), "paciente", (Collection) pfs);
        }

    }

    public List<SelectItem> getListRelatorios() {
        if (listSelectItem[0].isEmpty()) {
            RelatorioDao relatorioDao = new RelatorioDao();
            List<Relatorios> list = (List<Relatorios>) relatorioDao.findByRotina(62);
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
            paciente = new Fisica();
        }
        if (!filter[1]) {
            selectedCidades = null;
            listCidades = null;
        }
        if (!filter[2]) {
            dataContrato = new Date[2];
            dataContrato[0] = DataHoje.dataHoje();
            dataContrato[1] = DataHoje.dataHoje();
            dataDesligamento = new Date[2];
            dataDesligamento[0] = DataHoje.dataHoje();
            dataDesligamento[1] = DataHoje.dataHoje();
            dataInternacao = new Date[2];
            dataInternacao[0] = DataHoje.dataHoje();
            dataInternacao[1] = DataHoje.dataHoje();
            dataNascimento = new Date[2];
            dataNascimento[0] = DataHoje.dataHoje();
            dataNascimento[1] = DataHoje.dataHoje();
        }
        if (!filter[3]) {
            index[2] = null;
            listSelectItem[2].clear();
        }
        if (!filter[4]) {
            sexo = "";
        }
        if (!filter[5]) {
            idade[0] = 0;
            idade[1] = 0;
        }
        if (!filter[6]) {
            responsavel = new Fisica();
        }
        if (!filter[7]) {
            selectedTipoInternacao = null;
            listTipoInternacao = null;
        }
        if (!filter[8]) {
            selectedTipoDesligamento = null;
            listTipoDesligamento = null;
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
            case "paciente":
                filter[0] = false;
                paciente = new Fisica();
                break;
            case "cidade":
                filter[1] = false;
                selectedCidades = null;
                listCidades = null;
                break;
            case "data":
                filter[2] = false;
                dataContrato = new Date[2];
                dataContrato[0] = DataHoje.dataHoje();
                dataContrato[1] = DataHoje.dataHoje();
                dataDesligamento = new Date[2];
                dataDesligamento[0] = DataHoje.dataHoje();
                dataDesligamento[1] = DataHoje.dataHoje();
                dataInternacao = new Date[2];
                dataInternacao[0] = DataHoje.dataHoje();
                dataInternacao[1] = DataHoje.dataHoje();
                dataNascimento = new Date[2];
                dataNascimento[0] = DataHoje.dataHoje();
                dataNascimento[1] = DataHoje.dataHoje();
                break;
            case "filial":
                filter[3] = false;
                listSelectItem[2].clear();
                index[2] = null;
                break;
            case "sexo":
                sexo = "";
                filter[4] = false;
                break;
            case "idade":
                filter[5] = false;
                idade[0] = 0;
                idade[1] = 0;
                break;
            case "responsavel":
                filter[6] = false;
                responsavel = new Fisica();
                break;
            case "tipo_internacao":
                filter[7] = false;
                selectedTipoInternacao = null;
                listTipoInternacao = null;
                break;
            case "tipo_desligamento":
                filter[8] = false;
                selectedTipoDesligamento = null;
                listTipoDesligamento = null;
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

    public Fisica getPaciente() {
        if (Sessions.exists("fisicaPesquisa")) {
            if (Sessions.exists("tipo_pesquisa_fisica")) {
                if (Sessions.getString("tipo_pesquisa_fisica").equals("responsavel")) {
                    paciente = ((Fisica) Sessions.getObject("fisicaPesquisa", true));
                }
            }
        }
        return paciente;
    }

    public void setPaciente(Fisica paciente) {
        this.paciente = paciente;
    }

    public Fisica getResponsavel() {
        if (Sessions.exists("fisicaPesquisa")) {
            if (Sessions.exists("tipo_pesquisa_fisica")) {
                if (Sessions.getString("tipo_pesquisa_fisica").equals("responsavel")) {
                    responsavel = ((Fisica) Sessions.getObject("fisicaPesquisa", true));
                }
            }
        }
        return responsavel;
    }

    public void setResponsavel(Fisica responsavel) {
        this.responsavel = responsavel;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
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
                if (relatorios.getId() != index[0]) {
                    Jasper.EXPORT_TO_EXCEL = false;
                }
                relatorios = (Relatorios) new Dao().find(new Relatorios(), index[0]);
            }
        } catch (Exception e) {
            relatorios = new Relatorios();
            Jasper.EXPORT_TO_EXCEL = false;
        }
        return relatorios;
    }

    public Integer[] getIdade() {
        return idade;
    }

    public void setIdade(Integer[] idade) {
        this.idade = idade;
    }

    public String getIdadeInicial() {
        return Integer.toString(idade[0]);
    }

    public void setIdadeInicial(String idadeInicial) {
        Integer idadeInteger = 0;
        try {
            if (Integer.parseInt(idadeInicial) < 0) {
                idadeInteger = 0;
            }
            if (idade[1] < 0 && idade[1] <= Integer.parseInt(idadeInicial)) {
                idadeInteger = Integer.parseInt(idadeInicial) + 1;
            } else {
                idadeInteger = Integer.parseInt(idadeInicial);
            }
        } catch (NumberFormatException e) {
            idadeInteger = 0;
        }
        idade[0] = idadeInteger;
        if (idade[0] > 0) {
            if (idade[1] == 0) {
            } else if (idade[1] < idade[0]) {
                idade[1] = idade[0];
            }

        }
    }

    public String getIdadeFinal() {
        return Integer.toString(idade[1]);
    }

    public void setIdadeFinal(String idadeFinal) {
        if (idadeFinal.isEmpty()) {
            idade[1] = 0;
            return;
        }
        Integer idadeInteger = 0;
        try {
            if (idade[0] < 0) {
                idade[0] = 0;
            }
            if (Integer.parseInt(idadeFinal) < 0 && Integer.parseInt(idadeFinal) < idade[0]) {
                idadeInteger = idade[0] + 1;
            } else {
                idadeInteger = Integer.parseInt(idadeFinal);
            }
        } catch (NumberFormatException e) {
            idadeInteger = idade[0];
        }
        idade[1] = idadeInteger;
        if (idade[1] > 150) {
            idadeInteger = 150;
        }
        if (idade[1] < idade[0] && idade[1] != 0) {
            idade[1] = idade[0];
        }
    }

    public Map<String, Integer> getListCidades() {
        if (listCidades == null) {
            selectedCidades = null;
            listCidades = new LinkedHashMap<>();
            List<Cidade> list = (List<Cidade>) new CidadeDao().findCidadesByPessoaEnderecoGroup(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listCidades.put(list.get(i).getCidade(), list.get(i).getId());
            }
        }
        return listCidades;
    }

    public void setListCidades(Map<String, Integer> listCidades) {
        this.listCidades = listCidades;
    }

    public Map<String, Integer> getListTipoInternacao() {
        if (listTipoInternacao == null) {
            selectedTipoInternacao = null;
            listTipoInternacao = new LinkedHashMap<>();
            List<TipoInternacao> list = (List<TipoInternacao>) new Dao().list(new TipoInternacao(), true);
            for (int i = 0; i < list.size(); i++) {
                listTipoInternacao.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listTipoInternacao;
    }

    public void setListTipoInternacao(Map<String, Integer> listTipoInternacao) {
        this.listTipoInternacao = listTipoInternacao;
    }

    public Map<String, Integer> getListTipoDesligamento() {
        if (listTipoDesligamento == null) {
            selectedTipoDesligamento = null;
            listTipoDesligamento = new LinkedHashMap<>();
            List<TipoDesligamento> list = (List<TipoDesligamento>) new Dao().list(new TipoDesligamento(), true);
            for (int i = 0; i < list.size(); i++) {
                listTipoDesligamento.put(list.get(i).getDescricao(), list.get(i).getId());
            }
        }
        return listTipoDesligamento;
    }

    public void setListDesligamento(Map<String, Integer> listTipoDesligamento) {
        this.listTipoDesligamento = listTipoDesligamento;
    }

    public String inIdCidades() {
        String ids = "";
        if (selectedCidades != null) {
            for (int i = 0; i < selectedCidades.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedCidades.get(i);
                } else {
                    ids += "," + selectedCidades.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdTipoInternacao() {
        String ids = "";
        if (selectedTipoInternacao != null) {
            for (int i = 0; i < selectedTipoInternacao.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedTipoInternacao.get(i);
                } else {
                    ids += "," + selectedTipoInternacao.get(i);
                }
            }
        }
        return ids;
    }

    public String inIdTipoDesligamento() {
        String ids = "";
        if (selectedTipoDesligamento != null) {
            for (int i = 0; i < selectedTipoDesligamento.size(); i++) {
                if (ids.isEmpty()) {
                    ids = "" + selectedTipoDesligamento.get(i);
                } else {
                    ids += "," + selectedTipoDesligamento.get(i);
                }
            }
        }
        return ids;
    }

    public List<SelectItem> getListaRelatorioOrdem() {
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
        return listSelectItem[1];
    }

    public List getSelectedCidades() {
        return selectedCidades;
    }

    public void setSelectedCidades(List selectedCidades) {
        this.selectedCidades = selectedCidades;
    }

    public Date[] getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date[] dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date[] getDataInternacao() {
        return dataInternacao;
    }

    public void setDataInternacao(Date[] dataInternacao) {
        this.dataInternacao = dataInternacao;
    }

    public void selectedDataInternacaoI(SelectEvent event) {
        this.dataInternacao[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataInternacaoF(SelectEvent event) {
        this.dataInternacao[1] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataNascimentoI(SelectEvent event) {
        this.dataNascimento[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataNascimentoF(SelectEvent event) {
        this.dataNascimento[1] = DataHoje.converteSelectEvent(event);
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

    public Date[] getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(Date[] dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }

    public void selectedDataDesligamentoI(SelectEvent event) {
        this.dataDesligamento[0] = DataHoje.converteSelectEvent(event);
    }

    public void selectedDataDesligamentoF(SelectEvent event) {
        this.dataDesligamento[1] = DataHoje.converteSelectEvent(event);
    }

    public void listener(Integer tcase) {
        if (tcase == 1) {
            Sessions.put("tipo_pesquisa_fisica", "paciente");
        } else if (tcase == 2) {
            Sessions.put("tipo_pesquisa_fisica", "responsavel");
        }
    }

    public List getSelectedTipoInternacao() {
        return selectedTipoInternacao;
    }

    public void setSelectedTipoInternacao(List selectedTipoInternacao) {
        this.selectedTipoInternacao = selectedTipoInternacao;
    }

    public void setListTipoDesligamento(Map<String, Integer> listTipoDesligamento) {
        this.listTipoDesligamento = listTipoDesligamento;
    }

    public List getSelectedTipoDesligamento() {
        return selectedTipoDesligamento;
    }

    public void setSelectedTipoDesligamento(List selectedTipoDesligamento) {
        this.selectedTipoDesligamento = selectedTipoDesligamento;
    }
}
