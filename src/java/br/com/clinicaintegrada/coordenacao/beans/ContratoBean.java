package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.administrativo.ModeloContrato;
import br.com.clinicaintegrada.administrativo.ModeloDocumentos;
import br.com.clinicaintegrada.administrativo.Taxas;
import br.com.clinicaintegrada.administrativo.TipoContrato;
import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.administrativo.dao.ModeloContratoDao;
import br.com.clinicaintegrada.administrativo.dao.ModeloDocumentosDao;
import br.com.clinicaintegrada.administrativo.dao.TaxasDao;
import br.com.clinicaintegrada.coordenacao.AteFamilia;
import br.com.clinicaintegrada.coordenacao.AteFamiliaContrato;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.dao.AteFamiliaContratoDao;
import br.com.clinicaintegrada.coordenacao.dao.AteFamiliaDao;
import br.com.clinicaintegrada.coordenacao.dao.ContratoDao;
import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.CondicaoPagamento;
import br.com.clinicaintegrada.financeiro.FStatus;
import br.com.clinicaintegrada.financeiro.FTipoDocumento;
import br.com.clinicaintegrada.financeiro.Lote;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.beans.ImpressaoBoleto;
import br.com.clinicaintegrada.financeiro.beans.MovimentosReceberBean;
import br.com.clinicaintegrada.financeiro.dao.BoletoDao;
import br.com.clinicaintegrada.financeiro.dao.LoteDao;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Fotos;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.beans.FotosEvolucaoBean;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.FotosDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Compact;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Download;
import br.com.clinicaintegrada.utils.HtmlToPDF;
import br.com.clinicaintegrada.utils.Mask;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.PF;
import br.com.clinicaintegrada.utils.PasswordGenerator;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.ValidDocuments;
import br.com.clinicaintegrada.utils.ValorExtenso;
import br.com.clinicaintegrada.utils.dao.FunctionsDao;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.lowagie.text.Element;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.TimeLimitExceededException;
import javax.servlet.ServletContext;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ContratoBean implements Serializable {

    private Contrato contrato;
    private Filial filialSessao;
    private String saldoDevedor;
    private String valorServico;
    private String vencimentoString;
    private String valorTotalTaxa;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String idFTipoDocumento;
    private String adicionarDias;
    private List<SelectItem> listTaxas;
    private List<ModeloDocumentos> listModeloDocumentos;
    private String calculaValorMovimentoAlterado;
    private List<SelectItem> listFTipoDocumento;
    private List<SelectItem> listTipoContrato;
    private List<Contrato> listContratos;
    private List<SelectItem> listFilial;
    private List<SelectItem> listFilialAtual;
    private List<SelectItem> listTipoInternacao;
    private List<SelectItem> listTipoDesligamento;
    private List<Movimento> listMovimento;
    private List<Movimento> listMovimentoContrato;
    private List<Movimento> listMovimentoTaxa;
    private Movimento updateMovimento;
    private Integer idFilial;
    private Integer idFilialAtual;
    private Integer idTipoInternacao;
    private Integer idTipoDesligamento;
    private Integer idTipoContrato;
    private int idTaxa;
    private int indexList;
    private int diaVencimento;
    private boolean pesquisaResponsavel;
    private boolean disabledSave;
    private Boolean imprimeVerso;
    private Boolean[] disabled;
    private String[] mensagem;
    private Integer numeroParcelasTaxa;
    private Juridica cobranca2;
    private Float valorTotalResponsavel;
    private Float valorTotalCobranca2;

    @PostConstruct
    public void init() {
        disabledSave = false;
        contrato = new Contrato();
        filialSessao = new Filial();
        listContratos = new ArrayList<>();
        listFilial = new ArrayList<>();
        listFilialAtual = new ArrayList<>();
        listTipoInternacao = new ArrayList<>();
        listTipoDesligamento = new ArrayList<>();
        listTipoContrato = new ArrayList<>();
        listMovimento = new ArrayList<>();
        listMovimentoContrato = new ArrayList<>();
        listMovimentoTaxa = new ArrayList<>();
        idFilial = 0;
        idFilialAtual = 0;
        idTipoContrato = 0;
        idTipoInternacao = 0;
        idTipoDesligamento = 0;
        pesquisaResponsavel = false;
        saldoDevedor = "0";
        diaVencimento = Integer.parseInt(DataHoje.livre(new Date(), "dd"));
        calculaSaldoDevedor();
        listTaxas = new ArrayList<>();
        idTaxa = 0;
        valorServico = "0,00";
        valorTotalTaxa = "0,00";
        vencimentoString = DataHoje.data();
        listModeloDocumentos = new ArrayList<>();
        listFTipoDocumento = new ArrayList<>();
        adicionarDias = "0";
        calculaValorMovimentoAlterado = "0";
        idFTipoDocumento = null;
        updateMovimento = new Movimento();
        indexList = -1;
        descricaoPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        imprimeVerso = false;
        mensagem = new String[2];
        mensagem[0] = "";
        mensagem[1] = "";
        numeroParcelasTaxa = 1;
        disabled = new Boolean[]{false, false};
        cobranca2 = new Juridica();
        valorTotalResponsavel = new Float(0);
        valorTotalCobranca2 = new Float(0);
    }

    @PreDestroy
    public void destroy() {
        clear();
        Sessions.remove("contratoBean");
        Sessions.remove("pessoaPesquisa");
        Sessions.remove("fisicaPesquisa");
        Sessions.remove("contratoPesquisa");
        Sessions.remove("pesquisaResponsavelContrato");
        Sessions.remove("movimentosReceberDisabled");
    }

    public String clear() {
        Sessions.remove("contratoBean");
        return "contrato";
    }

    public void action(int tcase) {
        switch (tcase) {
            // RESCISÃO CONTRATUAL
            case 1:
                contrato.setDataRescisao(DataHoje.dataHoje());
                break;
            // DESFAZER RESCISÃO CONTRATUAL
            case 2:
                contrato.setDataRescisao(null);
                contrato.setTipoDesligamento(null);
                listTipoDesligamento.clear();
                getListTipoDesligamento();
                contrato.setObservacaoRescisao("");
                break;

        }
    }

    /**
     * 5 - Entidade
     *
     * @param tcase
     */
    public void clear(int tcase) {
        switch (tcase) {
            case 1:
                contrato.setResponsavel(new Pessoa());
                contrato.setPaciente(new Pessoa());
                contrato.setCobranca2(new Pessoa());
                break;
            case 2:
                contrato.setPaciente(new Pessoa());
                contrato.setCobranca2(new Pessoa());
                break;
            // DESFAZER RESCISÃO
            case 3:
                contrato.setPaciente(new Pessoa());
                break;
            case 4:
                listModeloDocumentos.clear();
                break;
            case 5:
                contrato.setCobranca2(new Pessoa());
                break;
        }
    }

    public void save() {
        try {
            if (contrato.getId() != null) {
                Contrato c = (Contrato) new Dao().find(new Contrato(), contrato.getId());
                if (c == null) {
                    Messages.warn("Sistema", "Erro ao inserir cadastro, por favor limpe os dados e tente novamente!");
                    return;
                }
            }
            if (listTipoInternacao.isEmpty()) {
                Messages.warn("Validação", "Cadastrar tipos de internação!");
                return;
            }
            if (contrato.getDataCadastroString().isEmpty()) {
                Messages.warn("Validação", "Informar data de cadastro!");
                return;
            }
            if (contrato.getResponsavel().getId() == -1) {
                Messages.warn("Validação", "Pesquisar responsável!");
                return;
            }
            if (contrato.getPaciente().getId() == -1) {
                Messages.warn("Validação", "Pesquisar paciente!");
                return;
            }
            Dao dao = new Dao();
            contrato.setTipoContrato(getTipoContrato());
            if (contrato.getTipoContrato().getId() != 2) {
                if (contrato.getValorTotal() == 0) {
                    Messages.warn("Validação", "Informar o valor do contrato!");
                    return;
                }
            }
            if (contrato.getTipoContrato().getId() == 3) {
                if (contrato.getCobranca2().getId() == -1) {
                    Messages.warn("Validação", "Pesquisar responsável 2!");
                    contrato.setValorTotal2(valorTotalCobranca2);
                    return;
                }
            } else {
                contrato.setCobranca2(null);
            }
            if (disabledSave) {
                Messages.warn("Validação", getCalculaValorMovimentoAlterado());
                return;

            }
            contrato.setTipoInternacao((TipoInternacao) dao.find(new TipoInternacao(), Integer.parseInt(listTipoInternacao.get(idTipoInternacao).getDescription())));
            Logger logger = new Logger();
            dao.openTransaction();
            FunctionsDao functionsDao = new FunctionsDao();
            if (contrato.getSenha().isEmpty()) {
                contrato.setSenha(PasswordGenerator.create());
            }
            if (contrato.getId() == null) {
                contrato.setCliente(SessaoCliente.get());
                contrato.setFilial(MacFilial.getAcessoFilial().getFilial());
                contrato.setFilialAtual((Filial) dao.find(new Filial(), Integer.parseInt(listFilialAtual.get(idFilialAtual).getDescription())));
                ContratoDao contratoDao = new ContratoDao();
                if (contratoDao.existeContrato(contrato)) {
                    Messages.warn("Validação", "Contrato já existe!");
                    return;
                }
                contrato.setTipoDesligamento(null);
                if (dao.save(contrato)) {
                    listMovimento.addAll(listMovimentoContrato);
                    listMovimento.addAll(listMovimentoTaxa);
                    float valorTotal = 0;
                    if (!listMovimentoTaxa.isEmpty()) {
                        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
                            valorTotal += listMovimentoTaxa.get(i).getValor();
                        }
                    }
                    valorTotal = valorTotal + contrato.getValorTotal() + contrato.getValorTotal2();
                    Lote lote = new Lote();
                    lote.setCliente(SessaoCliente.get());
                    lote.setValor(valorTotal);
                    lote.setDepartamento(null);
                    lote.setDocumento("");
                    lote.setEmissao(new Date());
                    lote.setFilial(contrato.getFilial());
                    lote.setLancamento(null);
                    lote.setContrato(contrato);
                    lote.setStatus((FStatus) dao.find(new FStatus(), 1));
                    lote.setFtipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 1));
                    lote.setPagRec("");
                    lote.setPessoa(contrato.getResponsavel());
                    if (listMovimento.size() == 1) {
                        lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 1));
                    } else {
                        lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 2));
                    }
                    if (!dao.save(lote)) {
                        Messages.warn("Erro", "Ao gerar lote!");
                        dao.rollback();
                        return;
                    }
                    if (contrato.getTipoContrato().getId() == 2) {
                        DataHoje dh = new DataHoje();
                        boolean isEntrada = false;
                        Servicos s1 = (Servicos) dao.find(new Servicos(), 1);
                        Movimento m = new Movimento();
                        int addDias;
                        try {
                            addDias = Integer.parseInt(adicionarDias);
                        } catch (Exception e) {
                            addDias = 0;
                        }
                        if (addDias > 0) {
                            m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, contrato.getDataCadastroString())));
                            isEntrada = true;
                        } else {
                            m.setVencimento(contrato.getDataCadastro());
                        }
                        m.setPessoa(contrato.getResponsavel());
                        m.setTipoDocumento(s1.getTipoDocumento());
                        m.setServicos(s1);
                        m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                        m.setEs("E");
                        m.setNrCtrBoleto(null);
                        m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                        m.setDocumento("");
                        m.setLote(lote);
                        m.setAtivo(true);
                        m.setEvento(null);
                        m.setValor(0);
                        m.setBaixa(null);
                        if (addDias > 0) {
                            m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, contrato.getDataCadastroString())));
                            isEntrada = true;
                        } else {
                            m.setVencimento(contrato.getDataCadastro());
                        }
                        if (!dao.save(m)) {
                            Messages.warn("Erro", "Ao salvar movimento!");
                            dao.rollback();
                            return;
                        }
                    }
                    if (!listMovimento.isEmpty()) {
                        for (int i = 0; i < listMovimento.size(); i++) {
                            listMovimento.get(i).setLote(lote);
                            if (!dao.save(listMovimento.get(i))) {
                                Messages.warn("Erro", "Ao gerar movimento!");
                                dao.rollback();
                                return;
                            }
                        }
                    }
                    dao.commit();
                    logger.save(
                            "ID: " + contrato.getId()
                            + " - Filial: [" + contrato.getFilial().getFilial().getPessoa().getId() + "] - " + contrato.getFilial().getFilial().getPessoa().getNome()
                            + " - Filial Atual: [" + contrato.getFilialAtual().getFilial().getPessoa().getId() + "] - " + contrato.getFilialAtual().getFilial().getPessoa().getNome()
                            + " - Tipo Contrato: [" + contrato.getTipoContrato().getId() + "] - " + contrato.getTipoContrato().getDescricao()
                            + " - Responsável: [" + contrato.getResponsavel().getId() + "] - " + contrato.getResponsavel().getNome()
                            + " - Paciente: [" + contrato.getPaciente().getId() + "] - " + contrato.getPaciente().getNome()
                    );
                    Messages.info("Sucesso", "Registro inserido!");
                    functionsDao.gerarBoletos();
                    listTaxas.clear();
                    getListMovimentoTaxa();
                    getListTaxas();
                    listMovimento.clear();
                    listMovimentoTaxa.clear();
                    listMovimentoContrato.clear();
                    getListMovimentoContrato();
                    updateAtePessoa();
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Erro ao inserir registro!");
                }
            } else {
                if (contrato.getDataRescisao() != null) {
                    if (getListTipoDesligamento().isEmpty()) {
                        Messages.warn("Validação", "Cadastrar tipos de desligamento!");
                        return;
                    }
                    contrato.setTipoDesligamento((TipoDesligamento) dao.find(new TipoDesligamento(), Integer.parseInt(listTipoDesligamento.get(idTipoDesligamento).getDescription())));
                }
                Contrato c = (Contrato) dao.find(new Contrato(), contrato.getId());
                String beforeUpdate
                        = "ID: " + c.getId()
                        + " - Filial: [" + c.getFilial().getFilial().getPessoa().getId() + "] - " + c.getFilial().getFilial().getPessoa().getNome()
                        + " - Filial Atual: [" + c.getFilialAtual().getFilial().getPessoa().getId() + "] - " + c.getFilialAtual().getFilial().getPessoa().getNome()
                        + " - Tipo Contrato: [" + contrato.getTipoContrato().getId() + "] - " + contrato.getTipoContrato().getDescricao()
                        + " - Responsável: [" + c.getResponsavel().getId() + "] - " + c.getResponsavel().getNome()
                        + " - Paciente: [" + c.getPaciente().getId() + "] - " + c.getPaciente().getNome();
                if (dao.update(contrato)) {
                    dao.commit();
                    Messages.info("Sucesso", "Registro atualizado!");
                    logger.update(beforeUpdate,
                            "ID: " + contrato.getId()
                            + " - Filial: [" + contrato.getFilial().getFilial().getPessoa().getId() + "] - " + contrato.getFilial().getFilial().getPessoa().getNome()
                            + " - Filial Atual: [" + contrato.getFilialAtual().getFilial().getPessoa().getId() + "] - " + contrato.getFilialAtual().getFilial().getPessoa().getNome()
                            + " - Tipo Contrato: [" + contrato.getTipoContrato().getId() + "] - " + contrato.getTipoContrato().getDescricao()
                            + " - Responsável: [" + contrato.getResponsavel().getId() + "] - " + contrato.getResponsavel().getNome()
                            + " - Paciente: [" + contrato.getPaciente().getId() + "] - " + contrato.getPaciente().getNome()
                    );
                    functionsDao.gerarBoletos();
                    listTaxas.clear();
                    getListMovimentoTaxa();
                    getListTaxas();
                    listMovimento.clear();
                    listMovimentoTaxa.clear();
                    listMovimentoContrato.clear();
                    getListMovimentoContrato();
                    updateAtePessoa();
                } else {
                    dao.rollback();
                    Messages.warn("Erro", "Erro ao atualizar registro!");
                }
            }

            if (contrato.getId()
                    != -1) {
                new FotosEvolucaoBean().execute(contrato);
            }
        } catch (Exception e) {
            Messages.warn("Sistema", e.getMessage());
        }

    }

    public String edit(Object o) throws IOException {
        return edit(o, null);
    }

    public String edit(Object o, String redirect) throws IOException {
        contrato = new Contrato();
        Dao dao = new Dao();
        contrato = (Contrato) dao.rebind(o);
        if (Sessions.getString("urlRetorno").equals("questionarioCoordenacao")) {
            Sessions.put("linkClicado", true);
            Sessions.put("contratoPesquisa", contrato);
            return "questionarioCoordenacao";
        }
        if (Sessions.getString("urlRetorno").equals("movimentosReceber") || redirect != null) {
            List<Pessoa> list = new ArrayList();
            list.add(contrato.getResponsavel());
            if (contrato.getCobranca2() != null) {
                list.add(contrato.getCobranca2());
            }
            if (redirect == null) {
                Sessions.put("contratoPesquisa", contrato);
                Sessions.put("pessoaPesquisaList", list);
                Sessions.put("linkClicado", true);
                return (String) Sessions.getString("urlRetorno");
            } else {
                Sessions.remove("movimentosReceberBean");
                Sessions.remove("movimentosReceberDisabled");
                Sessions.remove("pessoaPesquisa");
                Sessions.remove("listMovimentos");
                Sessions.remove("pessoaPesquisaList");
                Sessions.put("pessoaPesquisaList", list);
                MovimentosReceberBean movimentosReceberBean = new MovimentosReceberBean();
                movimentosReceberBean.init();
                Sessions.put("MovimentosReceberBean", movimentosReceberBean);
                Sessions.put("contratoPesquisa", contrato);
                Sessions.put("movimentosReceberDisabled", true);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pagina(redirect);
            }

        }
        listContratos.clear();
        for (int i = 0; i < listFilial.size(); i++) {
            if (contrato.getFilial().getId() == Integer.parseInt(listFilial.get(i).getDescription())) {
                idFilial = i;
                break;
            }
        }
        for (int i = 0; i < listFilialAtual.size(); i++) {
            if (contrato.getFilialAtual().getId() == Integer.parseInt(listFilialAtual.get(i).getDescription())) {
                idFilialAtual = i;
                break;
            }
        }
        for (int i = 0; i < listTipoInternacao.size(); i++) {
            if (contrato.getTipoInternacao().getId() == Integer.parseInt(listTipoInternacao.get(i).getDescription())) {
                idTipoInternacao = i;
                break;
            }
        }
        if (contrato.getTipoDesligamento() != null) {
            for (int i = 0; i < listTipoDesligamento.size(); i++) {
                if (contrato.getTipoDesligamento().getId() == Integer.parseInt(listTipoDesligamento.get(i).getDescription())) {
                    idTipoDesligamento = i;
                    break;
                }
            }
        }
        if (contrato.getTipoContrato() != null) {
            for (int i = 0; i < listTipoContrato.size(); i++) {
                if (contrato.getTipoContrato().getId() == Integer.parseInt(listTipoContrato.get(i).getDescription())) {
                    idTipoContrato = i;
                    break;
                }
            }
        }
        find(1);
        saldoDevedor = "";
        listMovimento.clear();
        getListMovimento();
        listMovimentoContrato.clear();
        getListMovimentoContrato();
        listTaxas.clear();
        getListMovimentoTaxa();
        listMovimentoTaxa.clear();
        getListMovimentoTaxa();
        getListTaxas();
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        valorTotalTaxa = Moeda.converteR$Float(vt);
        Sessions.put("linkClicado", true);
        listener(2);
        calculaValoresTotais(3);
        if (Sessions.exists("urlRetorno")) {
            if (!Sessions.getString("urlRetorno").equals("menuPrincipal")) {
                Sessions.put("contratoPesquisa", contrato);
                return (String) Sessions.getString("urlRetorno");
            }
        } else {
            return "contrato";
        }
        return "contrato";
    }

    public void delete() {
        delete(contrato);
    }

    public void delete(Contrato c) {
        Dao dao = new Dao();
        MovimentoDao movimentoDao = new MovimentoDao();
        List<Movimento> list = movimentoDao.findMovimentosByLote(listMovimento.get(0).getLote().getId());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBaixa() != null) {
                Messages.warn("Erro", "Não é possível excluir contratos com movimentos baixados. Somente rescisão!");
                return;
            }
        }
        Logger logger = new Logger();
        dao.openTransaction();
        if (contrato.getId() != null) {
            FotosDao fotosDao = new FotosDao();
            List<Fotos> listFotos = fotosDao.findFotosByContrato(contrato.getId());
            for (int i = 0; i < listFotos.size(); i++) {
                File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica() + "/imagens/evolucao/" + listFotos.get(i).getId() + ".png"));
                try {
                    if (file.exists()) {
                        if (file.delete()) {
                            if (!dao.delete(listFotos.get(i))) {
                                Messages.warn("Erro", "Erro ao remover foto!");
                                dao.rollback();
                                return;
                            }
                        } else {
                            Messages.warn("Erro", "Erro ao remover foto!");
                            dao.rollback();
                            return;
                        }
                    } else {
                        if (!dao.delete(listFotos.get(i))) {
                            Messages.warn("Erro", "Erro ao remover foto!");
                            dao.rollback();
                            return;
                        }
                    }
                } catch (Exception e) {
                    Messages.warn("Erro", "Erro ao remover foto!");
                    dao.rollback();
                    return;
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (!dao.delete(list.get(i))) {
                    Messages.warn("Erro", "Erro ao remover movimento!");
                    dao.rollback();
                    return;
                }
            }
            if (!dao.delete(listMovimento.get(0).getLote())) {
                Messages.warn("Erro", "Erro ao remover lote!");
                dao.rollback();
                return;
            }
            if (dao.delete(contrato)) {
                dao.commit();
                logger.delete(
                        "ID: " + contrato.getId()
                        + " - Filial: [" + contrato.getFilial().getFilial().getPessoa().getId() + "] - " + contrato.getFilial().getFilial().getPessoa().getNome()
                        + " - Filial Atual: [" + contrato.getFilialAtual().getFilial().getPessoa().getNome() + "] - " + contrato.getFilialAtual().getFilial().getPessoa().getNome()
                        + " - Tipo Contrato: [" + contrato.getTipoContrato().getId() + "] - " + contrato.getTipoContrato().getDescricao()
                        + " - Responsável: [" + contrato.getResponsavel().getId() + "] - " + contrato.getResponsavel().getNome()
                        + " - Paciente: [" + contrato.getPaciente().getId() + "] - " + contrato.getPaciente().getNome()
                );
                Messages.info("Sucesso", "Registro removido!");
                contrato = new Contrato();
                listContratos.clear();
                clear();
                saldoDevedor = "0";
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public void calculaSaldoDevedor() {
        if (contrato.getValorTotal() > 0) {
            if (contrato.getValorEntrada() == 0 && contrato.getQuantidadeParcelas() > 1) {
                adicionarDias = "0";
            }
            List<Movimento> list = getListMovimentoTaxa();
            float t = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getBaixa() == null) {
                    t += list.get(i).getValor();
                }
            }
            //list.clear();
            if (contrato.getId() == null) {
                saldoDevedor = Float.toString((contrato.getValorTotal()) + t);
            } else {
                list = getListMovimentoContrato();
                float d = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBaixa() != null) {
                        d += list.get(i).getValor();
                    }
                }
                saldoDevedor = Float.toString((contrato.getValorTotal()) + t - d);
            }
            geraParcelas();
        } else {
            saldoDevedor = "0";
        }
    }

    public void findPessoaPorDocumento(boolean responsavel) {
        FisicaDao fisicaDao = new FisicaDao();
        List<Fisica> listFisicas;
        if (responsavel) {
            if (ValidDocuments.isValidoCPF(contrato.getResponsavel().getDocumento())) {
                Messages.warn("Validação", "Documento inválido");
                return;
            }
            listFisicas = fisicaDao.pesquisaFisicaPorDocumento(contrato.getResponsavel().getDocumento(), true);
            if (!listFisicas.isEmpty()) {
                contrato.setResponsavel(listFisicas.get(0).getPessoa());
            }
        } else {
            if (ValidDocuments.isValidoCPF(contrato.getPaciente().getDocumento())) {
                Messages.warn("Validação", "Documento inválido");
                return;
            }
            listFisicas = fisicaDao.pesquisaFisicaPorDocumento(contrato.getPaciente().getDocumento(), true);
            if (!listFisicas.isEmpty()) {
                contrato.setPaciente(listFisicas.get(0).getPessoa());
            }
        }
    }

    public Contrato getContrato() {
        if (Sessions.exists("contratoPesquisa")) {
            contrato = (Contrato) Sessions.getObject("contratoPesquisa", true);
        }
        if (Sessions.exists("fisicaPesquisa")) {
            if (pesquisaResponsavel) {
                contrato.setResponsavel(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
                pesquisaResponsavel = false;
            } else {
                contrato.setPaciente(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
                if (contrato.getPaciente().getId().equals(contrato.getResponsavel().getId())) {
                    contrato.setPaciente(new Pessoa());
                    Messages.warn("Validação", "Responsável e paciente devem ser pessoas diferentes!");
                }
            }
        }
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public List<Contrato> getListContratos() {
        return listContratos;
    }

    public void setListContratos(List<Contrato> listContratos) {
        this.listContratos = listContratos;
    }

    public List<SelectItem> getListFilial() {
        if (listFilial.isEmpty()) {
            FilialDao filialDao = new FilialDao();
            List<Filial> list = (List<Filial>) filialDao.pesquisaTodasCliente();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    if (MacFilial.getAcessoFilial() != null && MacFilial.getAcessoFilial().getId() != null) {
                        if (MacFilial.getAcessoFilial().getFilial().getId().equals(list.get(i).getId())) {
                            idFilial = i;
                        }
                    } else {
                        idFilial = i;
                    }
                }
                listFilial.add(new SelectItem(i, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listFilial;
    }

    public void setListFilial(List<SelectItem> listFilial) {
        this.listFilial = listFilial;
    }

    public List<SelectItem> getListFilialAtual() {
        if (listFilialAtual.isEmpty()) {
            FilialDao filialDao = new FilialDao();
            List<Filial> list = (List<Filial>) filialDao.pesquisaTodasCliente();
            MacFilial macFilial = MacFilial.getAcessoFilial();
            boolean isFilial = false;
            for (int i = 0; i < list.size(); i++) {
                if (MacFilial.getAcessoFilial() != null && MacFilial.getAcessoFilial().getId() != null) {
                    if (!isFilial) {
                        if (macFilial.getFilial().getId().equals(list.get(i).getId())) {
                            idFilialAtual = i;
                            isFilial = true;
                        }
                    }
                } else {
                    if (i == 0) {
                        idFilialAtual = i;
                    }
                }
                listFilialAtual.add(new SelectItem(i, list.get(i).getFilial().getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listFilialAtual;
    }

    public void setListFilialAtual(List<SelectItem> listFilialAtual) {
        this.listFilialAtual = listFilialAtual;
    }

    public List<SelectItem> getListTipoInternacao() {
        if (listTipoInternacao.isEmpty()) {
            Dao dao = new Dao();
            List<TipoInternacao> list = (List<TipoInternacao>) dao.list(new TipoInternacao(), true);
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getDescricao().toUpperCase().contains("INVOLUNTÁRIA")) {
                    idTipoInternacao = i;
                }
                listTipoInternacao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }

        }
        return listTipoInternacao;
    }

    public void setListTipoInternacao(List<SelectItem> listTipoInternacao) {
        this.listTipoInternacao = listTipoInternacao;
    }

    public List<SelectItem> getListTipoDesligamento() {
        if (contrato.getDataRescisao() != null) {
            if (listTipoDesligamento.isEmpty()) {
                Dao dao = new Dao();
                List<TipoDesligamento> list = (List<TipoDesligamento>) dao.list(new TipoDesligamento(), true);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getDescricao().equals("RESCISÃO")) {
                        idTipoDesligamento = i;
                    }
                    listTipoDesligamento.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                }
            }
        }
        return listTipoDesligamento;
    }

    public void setListTipoDesligamento(List<SelectItem> listTipoDesligamento) {
        this.listTipoDesligamento = listTipoDesligamento;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public Integer getIdFilialAtual() {
        return idFilialAtual;
    }

    public void setIdFilialAtual(Integer idFilialAtual) {
        this.idFilialAtual = idFilialAtual;
    }

    public Integer getIdTipoInternacao() {
        return idTipoInternacao;
    }

    public void setIdTipoInternacao(Integer idTipoInternacao) {
        this.idTipoInternacao = idTipoInternacao;
    }

    public Integer getIdTipoDesligamento() {
        return idTipoDesligamento;
    }

    public void setIdTipoDesligamento(Integer idTipoDesligamento) {
        this.idTipoDesligamento = idTipoDesligamento;
    }

    public Filial getFilialSessao() {
        return filialSessao;
    }

    public void setFilialSessao(Filial filialSessao) {
        this.filialSessao = filialSessao;
    }

    public void putTipoPesquisaPessoaFisica(boolean is) {
        pesquisaResponsavel = is;
    }

    public String getSaldoDevedor() {
        return Moeda.converteR$(saldoDevedor);
    }

    public void setSaldoDevedor(String saldoDevedor) {
        if (Moeda.substituiVirgulaFloat(saldoDevedor) < 0) {
            saldoDevedor = "0";
        }
        this.saldoDevedor = saldoDevedor;
    }

    public void geraParcelas() {
        if (contrato.getId() == null) {
            if (contrato.getQuantidadeParcelas() == 0) {
                Messages.warn("Validação", "Informar número de parcelas maior que 0, Ex. 1");
                return;
            }
            int addDias;
            try {
                addDias = Integer.parseInt(adicionarDias);
            } catch (Exception e) {
                addDias = 0;
            }
            listMovimentoContrato.clear();
            DataHoje dh = new DataHoje();
            Movimento m = new Movimento();
            Dao dao = new Dao();
            Servicos s1 = (Servicos) dao.find(new Servicos(), 1);
            if (contrato.getValorEntrada() > valorTotalResponsavel && contrato.getValorEntrada2() >= 0) {
                contrato.setValorEntrada(new Float(0));
            } else if (contrato.getValorEntrada2() > valorTotalCobranca2 && contrato.getValorEntrada() >= 0) {
                contrato.setValorEntrada2(new Float(0));
            }
            if (listMovimentoContrato.isEmpty()) {
                boolean isEntrada = false;
                if (contrato.getValorEntrada() > 0) {
                    if (addDias > 0) {
                        m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, contrato.getDataCadastroString())));
                        isEntrada = true;
                    } else {
                        m.setVencimento(contrato.getDataCadastro());
                    }
                    m.setPessoa(contrato.getResponsavel());
                    m.setTipoDocumento(s1.getTipoDocumento());
                    m.setServicos(s1);
                    m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                    m.setEs("E");
                    m.setNrCtrBoleto(null);
                    m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                    m.setDocumento("");
                    m.setLote(null);
                    m.setAtivo(true);
                    m.setEvento(null);
                    m.setValor(contrato.getValorEntrada());
                    m.setBaixa(null);
                    m.setEntrada(true);
                    listMovimentoContrato.add(m);
                    m = new Movimento();
                }
                String dataVencimento = "";
                if (contrato.getDataCadastroString().equals(DataHoje.data())) {
                    dataVencimento = DataHoje.alterDay(diaVencimento, DataHoje.data());
                } else {
                    dataVencimento = DataHoje.alterDay(diaVencimento, contrato.getDataCadastroString());
                }
                if (dataVencimento.equals("")) {
                    diaVencimento = diaVencimento - 1;
                    dataVencimento = DataHoje.alterDay(diaVencimento, DataHoje.data());
                    if (dataVencimento.equals("")) {
                        diaVencimento = diaVencimento - 1;
                        dataVencimento = DataHoje.alterDay(diaVencimento, DataHoje.data());
                        if (dataVencimento.equals("")) {
                            diaVencimento = diaVencimento - 1;
                            dataVencimento = DataHoje.alterDay(diaVencimento, DataHoje.data());
                        }
                    }
                }
                if (Moeda.substituiVirgulaFloat(getSaldoDevedor()) > 0) {
                    int vencto = 0;
                    int h = 0;
                    // float v = (Moeda.substituiVirgulaFloat(getSaldoDevedor()) - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
                    float v = 0;
                    if (valorTotalCobranca2 == 0) {
                        v = (Moeda.substituiVirgulaFloat(contrato.getValorTotalString()) - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
                    } else if (valorTotalCobranca2 > 0) {
                        v = (Moeda.substituiVirgulaFloat(contrato.getValorTotalString()) - contrato.getValorEntrada() - valorTotalCobranca2) / contrato.getQuantidadeParcelas();
                    }
                    if (valorTotalResponsavel > 0 && v > 0) {
                        for (int i = 0; i < contrato.getQuantidadeParcelas(); i++) {
                            if (!isEntrada && i == 0 && addDias > 0) {
                                m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                                m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, m.getVencimentoString())));
                            } else {
                                m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                            }
                            m.setPessoa(contrato.getResponsavel());
                            m.setTipoDocumento(s1.getTipoDocumento());
                            m.setServicos(s1);
                            m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                            m.setEs("E");
                            m.setNrCtrBoleto(null);
                            m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                            m.setDocumento("");
                            m.setLote(null);
                            m.setAtivo(true);
                            m.setEvento(null);
                            m.setValor(v);
                            m.setBaixa(null);
                            listMovimentoContrato.add(m);
                            m = new Movimento();
                        }
                    }
                }
                TipoContrato tc = getTipoContrato();
                if (valorTotalCobranca2 > 0) {
                    if (contrato.getValorEntrada2() > 0) {
                        if (tc.getId().equals(3)) {
                            if (addDias > 0) {
                                m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, contrato.getDataCadastroString())));
                                isEntrada = true;
                            } else {
                                m.setVencimento(contrato.getDataCadastro());
                            }
                            m.setPessoa(contrato.getCobranca2());
                            m.setTipoDocumento(s1.getTipoDocumento());
                            m.setServicos(s1);
                            m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                            m.setEs("E");
                            m.setNrCtrBoleto(null);
                            m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                            m.setDocumento("");
                            m.setLote(null);
                            m.setAtivo(true);
                            m.setEvento(null);
                            m.setValor(contrato.getValorEntrada2());
                            m.setBaixa(null);
                            m.setEntrada(true);
                            listMovimentoContrato.add(m);
                            m = new Movimento();
                        }
                    }
                }
                if (tc.getId().equals(3)) {
                    if (contrato.getValorTotal2() > 0 || contrato.getValorEntrada2() > 0) {
                        int vencto = 0;
                        int h = 0;
                        // float v = (Moeda.substituiVirgulaFloat(getSaldoDevedor()) - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
                        float v = (contrato.getValorTotal2() - contrato.getValorEntrada2()) / contrato.getQuantidadeParcelas();
                        if (v > 0) {
                            for (int i = 0; i < contrato.getQuantidadeParcelas(); i++) {
                                if (!isEntrada && i == 0 && addDias > 0) {
                                    m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                                    m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, m.getVencimentoString())));
                                } else {
                                    m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                                }
                                m.setPessoa(contrato.getCobranca2());
                                m.setTipoDocumento(s1.getTipoDocumento());
                                m.setServicos(s1);
                                m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                                m.setEs("E");
                                m.setNrCtrBoleto(null);
                                m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                                m.setDocumento("");
                                m.setLote(null);
                                m.setAtivo(true);
                                m.setEvento(null);
                                m.setValor(v);
                                m.setBaixa(null);
                                listMovimentoContrato.add(m);
                                m = new Movimento();
                            }
                        }
                    }
                }
                //listMovimento.addAll(listMovimentoTaxa);
                //listMovimentoContrato
            }
        }
    }

    public List<Movimento> getListMovimento() {
        if (contrato.getId() != null) {
            if (listMovimento.isEmpty()) {
                LoteDao loteDao = new LoteDao();
                Lote l = loteDao.findLoteByContrato(contrato.getId());
                if (l != null) {
                    MovimentoDao movimentoDao = new MovimentoDao();
                    movimentoDao.setOrder(" M.vencimento ");
                    listMovimento = movimentoDao.findMovimentosByLote(l.getId());
                }
            }
        }
        return listMovimento;
    }

    public void setListMovimento(List<Movimento> listMovimento) {
        this.listMovimento = listMovimento;
    }

    public List<Movimento> getListMovimentoContrato() {
        if (listMovimentoContrato.isEmpty()) {
            getListMovimento();
            if (!listMovimento.isEmpty()) {
                for (int i = 0; i < listMovimento.size(); i++) {
                    if (listMovimento.get(i).getTipoServico().getId() != 5) {
                        listMovimentoContrato.add(listMovimento.get(i));
                    }
                }
                Integer count = 0;
                Boolean r = false;
                Boolean c = false;
                if (contrato.getValorEntrada() > 0) {
                    count++;
                    r = true;
                }
                if (contrato.getCobranca2() != null) {
                    if (contrato.getValorEntrada2() > 0) {
                        count++;
                        c = true;
                    }
                }
                for (int y = 0; y < listMovimentoContrato.size(); y++) {
                    if (listMovimentoContrato.get(y).getValor() > 0) {
                        if (r) {

                            if (listMovimentoContrato.get(y).getValor() == contrato.getValorEntrada()) {
                                listMovimentoContrato.get(y).setEntrada(true);
                                r = false;
                            }
                        }
                        if (c) {
                            if (listMovimentoContrato.get(y).getValor() == contrato.getValorEntrada2()) {
                                listMovimentoContrato.get(y).setEntrada(true);
                                c = false;
                            }
                        }
                    }
                }
                if (count > 0) {
                    for (int x = 0; x < count; x++) {
                    }
                }
            }
        }
        return listMovimentoContrato;

    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public void addTaxa() {
        DataHoje dh = new DataHoje();
        Dao dao = new Dao();
        int tam = listMovimentoContrato.size();
        if (tam > 0) {
            tam = listMovimentoContrato.size() - 1;
        }
        float valorTaxaParcelada = 0;
        int size = 0;
        if (numeroParcelasTaxa == 0) {
            size = 1;
        } else {
            size = numeroParcelasTaxa;
        }
        if (!valorServico.isEmpty()) {
            valorTaxaParcelada = Moeda.converteUS$(valorServico) / numeroParcelasTaxa;
        } else {
//            if (numeroParcelasTaxa > 1 && valorServico.isEmpty()) {
//                Messages.warn("Validação", "Informar valor da taxa, quando houver número de parcelas maior que 1!");
//                return;
//            }
        }
        Taxas t = ((Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(idTaxa).getDescription())));
        listTaxas.remove(idTaxa);
        Boolean success = null;
        dao.openTransaction();
        for (int i = 0; i < size; i++) {
            Movimento m = new Movimento();
            m.setPessoa(contrato.getResponsavel());
            m.setTipoDocumento(t.getServicos().getTipoDocumento());
            m.setServicos(t.getServicos());
            m.setTipoServico((TipoServico) dao.find(new TipoServico(), 5));
            m.setEs("E");
            m.setNrCtrBoleto(null);
            if (i == 0) {
                m.setVencimento(DataHoje.converte(vencimentoString));
            } else {
                m.setVencimentoString(dh.incrementarMeses(i + 1, vencimentoString));
            }
            m.setReferencia(DataHoje.dataReferencia(vencimentoString));
            m.setDocumento("");
            m.setLote(null);
            m.setAtivo(true);
            m.setEvento(null);
            m.setValor(valorTaxaParcelada);
            if (m.getValor() <= 0) {
                Messages.warn("Erro", "Valor da taxa deve ser superior a 0!");
                return;
            }
            m.setBaixa(null);
            if (contrato.getId() != null) {
                if (i == 0) {
                    success = false;
                }
                m.setLote(listMovimento.get(0).getLote());
                if (dao.save(m)) {
                    listMovimentoTaxa.add(m);
                    listMovimento.add(m);
                    success = true;
                } else {
                    Messages.warn("Erro", "Erro ao adicionar taxa!");
                    dao.rollback();
                    return;
                }
            } else {
                success = true;
                listMovimentoTaxa.add(m);
            }
        }
        List<?> list = listTaxas;
        List<SelectItem> listSelectItem = new ArrayList();
        for (int j = 0; j < list.size(); j++) {
            listSelectItem.add(new SelectItem(j, listTaxas.get(j).getLabel(), listTaxas.get(j).getDescription()));
        }
        listTaxas = listSelectItem;
        idTaxa = 0;
        getListMovimentoTaxa();
        // getListTaxas();
        valorServico = "0,00";
        vencimentoString = DataHoje.data();
        valorTotalTaxa = "0,00";
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        valorTotalTaxa = Moeda.converteR$Float(vt);
        float sd = Moeda.converteUS$(saldoDevedor);
        saldoDevedor = Moeda.converteR$Float(sd + Moeda.converteUS$(valorServico));
        if (success != null) {
            if (contrato.getId() != null) {
                if (success) {
                    listTaxas.clear();
                    getListTaxas();
                    dao.commit();
                    FunctionsDao functionsDao = new FunctionsDao();
                    functionsDao.gerarBoletos();
                    listMovimento.clear();
                    listMovimentoTaxa.clear();
                    Messages.info("Sucesso", "Taxa adicionada!");
                }
            } else {
                if (success) {
                    getListTaxas();
                    Messages.info("Sucesso", "Taxa adicionada!");
                }
            }
        }
        calculaSaldoDevedor();
        selectedServico();
        numeroParcelasTaxa = 1;
    }

    public void removeTaxa(Servicos s) {
        float vs = 0;
        Boolean success = null;
        List<Movimento> listRemove = new ArrayList<>();
        Dao dao = new Dao();
        dao.openTransaction();
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            if (contrato.getId() != null) {
                if (i == 0) {
                    success = false;
                }
                if (s.getId() == listMovimentoTaxa.get(i).getServicos().getId()) {
                    listRemove.add(listMovimentoTaxa.get(i));
                    vs += listMovimentoTaxa.get(i).getValor();
                    if (dao.delete(listMovimentoTaxa.get(i))) {
                        success = true;
                    } else {
                        Messages.warn("Validação", "Erro ao remover taxa!");
                        dao.rollback();
                        return;
                    }
                }
            } else {
                if (s.getId() == listMovimentoTaxa.get(i).getServicos().getId()) {
                    listRemove.add(listMovimentoTaxa.get(i));
                    vs += listMovimentoTaxa.get(i).getValor();
                }
            }
        }
        listMovimentoTaxa.removeAll(listRemove);
        listTaxas.add(new SelectItem(listTaxas.size(), s.getDescricao(), "" + new TaxasDao().findTaxaByServicos(s.getId()).getId()));
        idTaxa = 0;
        getListTaxas();
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        float sd = Moeda.converteUS$(saldoDevedor);
        valorTotalTaxa = Moeda.converteR$Float(vt);
        saldoDevedor = Moeda.converteR$Float(sd - vs);
        idTaxa = 0;
        if (success != null) {
            if (success) {
                dao.commit();
                listMovimentoTaxa.clear();
                listMovimento.clear();
                listTaxas.clear();
                getListMovimentoTaxa();
                Messages.info("Sucesso", "Taxa removida!");
            } else {
                dao.rollback();
            }
        }
        try {
            selectedServico();
        } catch (Exception e) {

        }
    }

    public void selectedServico() {
        Dao dao = new Dao();
        valorServico = ((Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(idTaxa).getDescription()))).getValorString();
    }

    public List<SelectItem> getListTaxas() {
        if (listTaxas.isEmpty()) {
            TaxasDao taxasDao = new TaxasDao();
            List<Taxas> list;
            Boolean b = false;
            if (contrato.getId() == null) {
                list = taxasDao.pesquisaTodasTaxasPorCliente(SessaoCliente.get().getId());
                if (list.size() == listMovimentoTaxa.size()) {
                    list.clear();
                }
            } else {
                list = taxasDao.pesquisaTodasTaxasPorClienteContrato(SessaoCliente.get().getId(), contrato.getId());
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    valorServico = list.get(i).getValorString();
                    idTaxa = i;
                }
                listTaxas.add(new SelectItem(i, list.get(i).getServicos().getDescricao(), "" + list.get(i).getId()));
            }
//            if (list.size() != listMovimentoTaxa.size()) {
//                for (int i = 0; i < list.size(); i++) {
//                    if (i == 0) {
//                        valorServico = list.get(i).getValorString();
//                        idTaxa = i;
//                    }
//                    if (listMovimentoTaxa.isEmpty() || !b) {
//                        if (i == 0) {
//                            valorServico = list.get(i).getValorString();
//                            idTaxa = i;
//                        }
//                        listTaxas.add(new SelectItem(i, list.get(i).getServicos().getDescricao(), "" + list.get(i).getId()));
//                        b = true;
//                    } else {
//                        for (int j = 0; j < listMovimentoTaxa.size(); j++) {
//                            if (listMovimentoTaxa.get(j).getServicos().getId() != list.get(i).getServicos().getId()) {
//                                if (j == 0) {
//                                    valorServico = list.get(i).getValorString();
//                                    idTaxa = j;
//                                }
//                                listTaxas.add(new SelectItem(j, list.get(i).getServicos().getDescricao(), "" + list.get(i).getId()));
//                            }
//                        }
//                    }
//                }
//            }
        }
        return listTaxas;
    }

    public void setListTaxas(List<SelectItem> listTaxas) {
        this.listTaxas = listTaxas;
    }

    public String getValorServico() {
        return valorServico;
    }

    public void setValorServico(String valorServico) {
        this.valorServico = valorServico;
    }

    public Integer getIdTaxa() {
        return idTaxa;
    }

    public void setIdTaxa(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    public String getVencimentoString() {
        return vencimentoString;
    }

    public void setVencimentoString(String vencimentoString) {
        this.vencimentoString = vencimentoString;
    }

    public String getValorTotalTaxa() {
        return valorTotalTaxa;
    }

    public void setValorTotalTaxa(String valorTotalTaxa) {
        this.valorTotalTaxa = valorTotalTaxa;
    }

    public List<Movimento> getListMovimentoTaxa() {
        if (listMovimentoTaxa.isEmpty()) {
            getListMovimento();
            if (!listMovimento.isEmpty()) {
                for (int i = 0; i < listMovimento.size(); i++) {
                    if (listMovimento.get(i).getTipoServico().getId() == 5) {
                        listMovimentoTaxa.add(listMovimento.get(i));
                    }
                }
            }
        }
        return listMovimentoTaxa;
    }

    public void setListMovimentoTaxa(List<Movimento> listMovimentoTaxa) {
        this.listMovimentoTaxa = listMovimentoTaxa;
    }

    public void setListMovimentoContrato(List<Movimento> listMovimentoContrato) {
        this.listMovimentoContrato = listMovimentoContrato;
    }

    public void imprimir() {
        LoteDao loteDao = new LoteDao();
        Lote lote = loteDao.findLoteByContrato(contrato.getId());
        if (lote == null) {
            Messages.warn("Sistema", "Necessário gerar movimento para imprimir esse contrato!");
            return;
        }
        if (contrato.getId() != null) {
            Dao dao = new Dao();
            String contratoDiaSemana = "";
            ModeloContrato modeloContrato = new ModeloContrato();
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            modeloContrato = modeloContratoDao.pesquisaCodigoServico(1);
            if (modeloContrato == null) {
                Messages.warn("Sistema", "Não é possível gerar um contrato para este serviço. Para gerar um contrato acesse: Menu Escola > Suporte > Modelo Contrato.");
                return;
            }
            FisicaDao fisicaDao = new FisicaDao();
            Fisica responsavelFisica = fisicaDao.pesquisaFisicaPorPessoa(contrato.getResponsavel().getId());
            Fisica pacienteFisica = fisicaDao.pesquisaFisicaPorPessoa(contrato.getResponsavel().getId());
            List listaDiaSemana = new ArrayList();
            int periodoMeses = 0;
            String periodoMesesExtenso;
            if (periodoMeses == 0) {
                periodoMesesExtenso = "mês atual";
            } else {
                ValorExtenso valorExtenso = new ValorExtenso();
                valorExtenso.setNumber((double) periodoMeses);
                periodoMesesExtenso = (valorExtenso.toString()).replace("reais", "");
            }
            for (int i = 0; i < listaDiaSemana.size(); i++) {
                if (i == 0) {
                    contratoDiaSemana = listaDiaSemana.get(i).toString();
                } else {
                    contratoDiaSemana += " , " + listaDiaSemana.get(i).toString();
                }
            }
            String enderecoPacienteString = "";
            String bairroPacienteString = "";
            String cidadePacienteString = "";
            String estadoPacienteString = "";
            String cepPacienteString = "";
            String enderecoResponsavelString = "";
            String bairroResponsavelString = "";
            String cidadeResponsavelString = "";
            String estadoResponsavelString = "";
            String cepResponsavelString = "";
            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
            PessoaEndereco pessoaEnderecoPaciente = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(contrato.getPaciente().getId(), 1);

            int idTipoEndereco = -1;
            if (pessoaEnderecoPaciente != null) {
                enderecoPacienteString = pessoaEnderecoPaciente.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoPaciente.getNumero();
                bairroPacienteString = pessoaEnderecoPaciente.getEndereco().getBairro().getDescricao();
                cidadePacienteString = pessoaEnderecoPaciente.getEndereco().getCidade().getCidade();
                estadoPacienteString = pessoaEnderecoPaciente.getEndereco().getCidade().getUf();
                cepPacienteString = pessoaEnderecoPaciente.getEndereco().getCep();
            }
            if (!Objects.equals(contrato.getResponsavel().getId(), contrato.getPaciente().getId())) {
                // Tipo Documento - CPF
                if (contrato.getResponsavel().getTipoDocumento().getId() == 1) {
                    idTipoEndereco = 1;
                    // Tipo Documento - CNPJ
                } else if (contrato.getResponsavel().getTipoDocumento().getId() == 2) {
                    idTipoEndereco = 3;
                }
            } else {
                enderecoResponsavelString = enderecoPacienteString;
                bairroResponsavelString = bairroPacienteString;
                cidadeResponsavelString = cidadePacienteString;
                estadoResponsavelString = estadoPacienteString;
                cepResponsavelString = cepPacienteString;
            }
            PessoaEndereco pessoaEnderecoResponsavel = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(contrato.getResponsavel().getId(), idTipoEndereco);
            if (pessoaEnderecoResponsavel != null) {
                enderecoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoResponsavel.getNumero();
                if (!pessoaEnderecoResponsavel.getComplemento().isEmpty()) {
                    enderecoResponsavelString += " - Complemento " + pessoaEnderecoResponsavel.getNumero();

                }
                bairroResponsavelString = pessoaEnderecoResponsavel.getEndereco().getBairro().getDescricao();
                cidadeResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getCidade();
                estadoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getUf();
                cepResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCep();
            }

//          PACIENTE
            String rgp = pacienteFisica.getRg();
            if (pacienteFisica.getRg().isEmpty()) {
                rgp = " -- ";
            }
            String telp = "";
            if (!contrato.getPaciente().getTelefone1().isEmpty()) {
                telp += contrato.getPaciente().getTelefone1();
            }
            if (!contrato.getPaciente().getTelefone2().isEmpty()) {
                telp += " - " + contrato.getPaciente().getTelefone2();
            }
            if (!contrato.getPaciente().getTelefone3().isEmpty()) {
                telp += " - " + contrato.getPaciente().getTelefone3();
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$paciente", contrato.getPaciente().getNome()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$enderecoPaciente", (enderecoPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$bairroPaciente", (bairroPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cidadePaciente", (cidadePacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoPaciente", (estadoPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cepPaciente", (cepPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nascimentoPaciente", pacienteFisica.getNascimento()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$emailPaciente", (contrato.getPaciente().getEmail1())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cpfPaciente", (contrato.getPaciente().getDocumento())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$rgPaciente", rgp));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$telefonesPaciente", telp));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoCivilPaciente", pacienteFisica.getEstadoCivil()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$naturalidadePaciente", pacienteFisica.getNaturalidade()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nacionalidadePaciente", pacienteFisica.getNacionalidade()));
//
////          RESPONSÁVEL
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$responsavel", (contrato.getResponsavel().getNome())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cpfResponsavel", contrato.getResponsavel().getDocumento()));
            String rgr = responsavelFisica.getRg();
            if (responsavelFisica.getRg().isEmpty()) {
                rgr = " -- ";
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$rgResponsavel", rgr));
            String telr = "";
            if (!contrato.getResponsavel().getTelefone1().isEmpty()) {
                telr += contrato.getResponsavel().getTelefone1();
            }
            if (!contrato.getResponsavel().getTelefone2().isEmpty()) {
                telr += " - " + contrato.getResponsavel().getTelefone2();
            }
            if (!contrato.getResponsavel().getTelefone3().isEmpty()) {
                telr += " - " + contrato.getResponsavel().getTelefone3();
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$telefonesResponsavel", telr));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoCivilResponsavel", responsavelFisica.getEstadoCivil()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$enderecoResponsavel", enderecoResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$bairroResponsavel", bairroResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cidadeResponsavel", cidadeResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoResponsavel", estadoResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cepResponsavel", cepResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$emailResponsavel", contrato.getResponsavel().getEmail1()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nascimentoResponsavel", responsavelFisica.getNascimento()));
            responsavelFisica.setNaturalidade(responsavelFisica.getNaturalidade().replace("<", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nacionalidadeResponsavel", responsavelFisica.getNacionalidade()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$naturalidadeResponsavel", responsavelFisica.getNaturalidade()));
//            
////          CONTRATO
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$contrato", (Integer.toString(contrato.getId()))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$mesesExtenso", (periodoMesesExtenso)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$meses", (Integer.toString(periodoMeses))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$ano", (DataHoje.livre(DataHoje.dataHoje(), "yyyy"))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$servico", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$diaSemana", (contratoDiaSemana)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataExtenso", (DataHoje.dataExtenso(DataHoje.data()))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$parcelas", contrato.getQuantidadeParcelasString()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$diaVencimento", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataContrato", DataHoje.dataExtenso(contrato.getDataCadastroString())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$periodoDiasInternacao", contrato.getPrevisaoDiasString()));
//            
////          FINANCEIRO
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorParcela", (Moeda.converteR$Float((contrato.getValorTotal() - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas()))));
            String valorTaxaString = "";
            float valorTotalTaxas = 0;
            String listaValores = "";
            String listaValoresComData = "";
            int z = 1;
            int quantidadeDivisao = 0;
            if (!listMovimentoTaxa.isEmpty()) {
                if (listMovimentoTaxa.get(0).getTipoServico().getId() == 5) {
                    if (listMovimentoTaxa.size() > 1) {
                        quantidadeDivisao = listMovimentoTaxa.size() - 1;
                    } else {
                        quantidadeDivisao = listMovimentoTaxa.size();
                    }
                } else {
                    if (listMovimentoTaxa.size() > 1) {
                        quantidadeDivisao = listMovimentoTaxa.size();
                    }
                }
            }
            float valorTotalComEntrada = contrato.getValorTotal() - contrato.getValorEntrada();
            if (valorTotalComEntrada > 0) {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorTotalComEntrada", Moeda.converteR$Float(valorTotalComEntrada)));
            } else {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorTotalComEntrada", "Sem entrada"));
            }
            if (contrato.getQuantidadeParcelas() > 1) {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$parcelado", "X"));
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$avista", ""));
            } else {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$parcelado", ""));
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$avista", "X"));
            }
            if (contrato.getValorEntrada() > 0) {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorEntrada", "<strong>Valor da entrada: R$ " + contrato.getValorEntradaString() + "</strong>"));
            } else {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorEntrada", ""));
            }
            float valorDesc = 0;
            if (!listMovimentoTaxa.isEmpty()) {
                valorTaxaString += "Taxas: <br /><br />";
            }
            int l = 0;
            Taxas t = new Taxas();
            for (Movimento listaMovimento : listMovimentoTaxa) {
                for (l = 0; l < listTaxas.size(); l++) {
                    t = (Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(l).getDescription()));
                    if (!t.getOcultaContrato()) {
                        valorTaxaString += " - Vencimento: " + listaMovimento.getVencimentoString() + " - " + listaMovimento.getServicos().getDescricao() + " - R$ " + listaMovimento.getValorString() + "; <br /><br />";
                        valorTotalTaxas += listaMovimento.getValor();
                        break;
                    }
//                    if (listaMovimento.getServicos().getId() == t.getServicos().getId()) {
//                    }
                }
            }
            String valorTotalTaxaString = " -- ";
            if (valorTotalTaxas > 0) {
                valorTotalTaxaString = "<strong>Valor total taxa(s): R$ " + Moeda.converteR$Float(valorTotalTaxas) + " - ; </strong>";
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$listaTaxas", valorTaxaString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorTotalTaxas", valorTotalTaxaString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$valorTotal", contrato.getValorTotalString()));
            boolean isEntrada = false;
            for (Movimento listaMovimento : listMovimentoContrato) {
                if (contrato.getValorEntrada() > 0 && !isEntrada) {
                    isEntrada = true;
                } else {
                    listaValores += "Parcela nº" + z + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor()) + "; <br />";
                    listaValoresComData += z + "º - " + listaMovimento.getVencimentoString() + " - Valor: R$ " + Moeda.converteR$Float(listaMovimento.getValor()) + "; <br />";
                    modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$vencimentoParcela" + z, listaMovimento.getVencimentoString()));
                    z++;
                }
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$listaValoresComData", listaValoresComData));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$listaValores", "Valor total das taxas: R$ " + listaValores));

            // ADICIONAIS
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("<br>", "<br />"));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replaceAll("(<img[^>]*[^/]>)(?!\\s*</img>)", "$1</img>"));
            try {
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/"));
                if (!dirFile.exists()) {
                    if (!Dirs.create("arquivos/contrato")) {
                        return;
                    }
                }
                UUID uuidX = UUID.randomUUID();
                String uuid = uuidX.toString().replace("-", "_");
                String fileName = "contrato_" + uuid + ".pdf";
                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/_" + fileName);
                boolean success = new File(filePDF).createNewFile();
                //boolean success = file.createNewFile();
                boolean delete = false;
                if (success) {
                    OutputStream os = new FileOutputStream(filePDF);
                    HtmlToPDF.convert(modeloContrato.getDescricao(), os);
                    os.flush();
                    os.close();
                    String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato");
                    try {
                        PdfReader reader = new PdfReader(filePDF);
                        int n = reader.getNumberOfPages();
                        FileOutputStream fileOutputStream = new FileOutputStream(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/" + fileName));
                        PdfStamper stamp = new PdfStamper(reader, fileOutputStream);
                        int i = 0;
                        PdfContentByte over;
                        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                        while (i < n) {
                            i++;
                            over = stamp.getOverContent(i);
                            over.beginText();
                            over.setFontAndSize(bf, 10);
                            over.showTextAligned(Element.ALIGN_LEFT, "" + i, 570, 20, 0);
                            over.endText();
                        }
                        stamp.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        Download download = new Download(fileName, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                        download.open();
                        download.close();
                        if (contrato.getId() != null) {
                            contrato.setImpresso(true);
                            dao.update(contrato, true);
                        }
                    } catch (Exception de) {
                        de.printStackTrace();
                    }
                    boolean remove = false;
                    try {
                        remove = new File(filePDF).delete();
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (com.itextpdf.text.DocumentException ex) {

            }
        }
    }

    public void imprimirOutros() {
        UUID uuidX = UUID.randomUUID();
        String uuid = uuidX.toString().replace("-", "_");
        String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato");
        List list = new ArrayList();
        for (int i = 0; i < listModeloDocumentos.size(); i++) {
            if (listModeloDocumentos.get(i).getSelected()) {
                String name = imprimirOutros(listModeloDocumentos.get(i).getModeloContrato().getId(), true);
                list.add(name);
            }
        }
        //Compact.OUT_FILE = fileName + PART_NAME + "_" + idUsuario + "_" + uuid + "." + COMPRESS_EXTENSION;
        String filename = "arquivos_contrato" + "_" + contrato.getId() + "." + "zip";
        Compact.OUT_FILE = filename;
        Compact.setListFiles(list);
        String realPath = "/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/";
        Compact.PATH_OUT_FILE = realPath;
        try {
            Compact.toZip();
        } catch (IOException e) {
        }
        Download download = new Download(filename, pathPasta, "application/zip, application/octet-stream", FacesContext.getCurrentInstance());
        download.open();
        download.open();
        download.close();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                File f = new File(list.get(i).toString());
                f.delete();
            }
        }
    }

    public void imprimirOutros(int idModeloContratoDocumentos) {
        imprimirOutros(idModeloContratoDocumentos, false);
    }

    public String imprimirOutros(int idModeloContratoDocumentos, Boolean compact) {

        if (contrato.getId() != null) {
            Dao dao = new Dao();
            String contratoDiaSemana = "";
            ModeloContrato modeloContrato = new ModeloContrato();
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();

            String horaInicial;
            String horaFinal;
            FisicaDao fisicaDao = new FisicaDao();
            Fisica responsavelFisica = fisicaDao.pesquisaFisicaPorPessoa(contrato.getResponsavel().getId());
            Fisica pacienteFisica = fisicaDao.pesquisaFisicaPorPessoa(contrato.getResponsavel().getId());
            List listaDiaSemana = new ArrayList();
            int periodoMeses = 0;
            String periodoMesesExtenso;
            if (periodoMeses == 0) {
                periodoMesesExtenso = "mês atual";
            } else {
                ValorExtenso valorExtenso = new ValorExtenso();
                valorExtenso.setNumber((double) periodoMeses);
                periodoMesesExtenso = (valorExtenso.toString()).replace("reais", "");
            }
            for (int i = 0; i < listaDiaSemana.size(); i++) {
                if (i == 0) {
                    contratoDiaSemana = listaDiaSemana.get(i).toString();
                } else {
                    contratoDiaSemana += " , " + listaDiaSemana.get(i).toString();
                }
            }
            String enderecoPacienteString = "";
            String bairroPacienteString = "";
            String cidadePacienteString = "";
            String estadoPacienteString = "";
            String cepPacienteString = "";
            String enderecoResponsavelString = "";
            String bairroResponsavelString = "";
            String cidadeResponsavelString = "";
            String estadoResponsavelString = "";
            String cepResponsavelString = "";
            PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
            PessoaEndereco pessoaEnderecoPaciente = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(contrato.getPaciente().getId(), 1);

            int idTipoEndereco = -1;
            if (pessoaEnderecoPaciente != null) {
                enderecoPacienteString = pessoaEnderecoPaciente.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoPaciente.getNumero();
                bairroPacienteString = pessoaEnderecoPaciente.getEndereco().getBairro().getDescricao();
                cidadePacienteString = pessoaEnderecoPaciente.getEndereco().getCidade().getCidade();
                estadoPacienteString = pessoaEnderecoPaciente.getEndereco().getCidade().getUf();
                cepPacienteString = pessoaEnderecoPaciente.getEndereco().getCep();
            }
            if (contrato.getResponsavel().getId() != contrato.getPaciente().getId()) {
                // Tipo Documento - CPF
                if (contrato.getResponsavel().getTipoDocumento().getId() == 1) {
                    idTipoEndereco = 1;
                    // Tipo Documento - CNPJ
                } else if (contrato.getResponsavel().getTipoDocumento().getId() == 2) {
                    idTipoEndereco = 3;
                }
            } else {
                enderecoResponsavelString = enderecoPacienteString;
                bairroResponsavelString = bairroPacienteString;
                cidadeResponsavelString = cidadePacienteString;
                estadoResponsavelString = estadoPacienteString;
                cepResponsavelString = cepPacienteString;
            }
            PessoaEndereco pessoaEnderecoResponsavel = (PessoaEndereco) pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(contrato.getResponsavel().getId(), idTipoEndereco);
            if (pessoaEnderecoResponsavel != null) {
                enderecoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getEnderecoSimplesToString() + ", " + pessoaEnderecoResponsavel.getNumero();
                bairroResponsavelString = pessoaEnderecoResponsavel.getEndereco().getBairro().getDescricao();
                cidadeResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getCidade();
                estadoResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCidade().getUf();
                cepResponsavelString = pessoaEnderecoResponsavel.getEndereco().getCep();
            }

//          PACIENTE
            String rgp = pacienteFisica.getRg();
            if (pacienteFisica.getRg().isEmpty()) {
                rgp = " -- ";
            }
            String telp = "";
            if (!contrato.getPaciente().getTelefone1().isEmpty()) {
                telp += contrato.getPaciente().getTelefone1();
            }
            if (!contrato.getPaciente().getTelefone2().isEmpty()) {
                telp += " - " + contrato.getPaciente().getTelefone2();
            }
            if (!contrato.getPaciente().getTelefone3().isEmpty()) {
                telp += " - " + contrato.getPaciente().getTelefone3();
            }
            modeloContrato = (ModeloContrato) dao.find(new ModeloContrato(), idModeloContratoDocumentos);
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$paciente", contrato.getPaciente().getNome()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$enderecoPaciente", (enderecoPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$bairroPaciente", (bairroPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cidadePaciente", (cidadePacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoPaciente", (estadoPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cepPaciente", (cepPacienteString)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nascimentoPaciente", pacienteFisica.getNascimento()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$emailPaciente", (contrato.getPaciente().getEmail1())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cpfPaciente", (contrato.getPaciente().getDocumento())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$rgPaciente", rgp));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$telefonesPaciente", telp));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoCivilPaciente", pacienteFisica.getEstadoCivil()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$naturalidadePaciente", pacienteFisica.getNaturalidade()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nacionalidadePaciente", pacienteFisica.getNacionalidade()));
//
////          RESPONSÁVEL
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$responsavel", (contrato.getResponsavel().getNome())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cpfResponsavel", contrato.getResponsavel().getDocumento()));
            String rgr = responsavelFisica.getRg();
            if (responsavelFisica.getRg().isEmpty()) {
                rgr = " -- ";
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$rgResponsavel", rgr));
            String telr = "";
            if (!contrato.getResponsavel().getTelefone1().isEmpty()) {
                telr += contrato.getResponsavel().getTelefone1();
            }
            if (!contrato.getResponsavel().getTelefone2().isEmpty()) {
                telr += " - " + contrato.getResponsavel().getTelefone2();
            }
            if (!contrato.getResponsavel().getTelefone3().isEmpty()) {
                telr += " - " + contrato.getResponsavel().getTelefone3();
            }
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$telefonesResponsavel", telr));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoCivilResponsavel", responsavelFisica.getEstadoCivil()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$enderecoResponsavel", enderecoResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$bairroResponsavel", bairroResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cidadeResponsavel", cidadeResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$estadoResponsavel", estadoResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$cepResponsavel", cepResponsavelString));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$emailResponsavel", contrato.getResponsavel().getEmail1()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nascimentoResponsavel", responsavelFisica.getNascimento()));
            responsavelFisica.setNaturalidade(responsavelFisica.getNaturalidade().replace("<", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$nacionalidadeResponsavel", responsavelFisica.getNacionalidade()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$naturalidadeResponsavel", responsavelFisica.getNaturalidade()));
//            
//          CONTRATO
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$contrato", (Integer.toString(contrato.getId()))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$mesesExtenso", (periodoMesesExtenso)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$meses", (Integer.toString(periodoMeses))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$ano", (DataHoje.livre(DataHoje.dataHoje(), "yyyy"))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$servico", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$diaSemana", (contratoDiaSemana)));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataExtenso", (DataHoje.dataExtenso(DataHoje.data()))));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$parcelas", contrato.getQuantidadeParcelasString()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$diaVencimento", ""));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataContrato", contrato.getDataCadastroString()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataContratoExtenso", DataHoje.dataExtenso(contrato.getDataCadastroString())));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$periodoDiasInternacao", contrato.getPrevisaoDiasString()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$motivoSaida", contrato.getObservacaoRescisao()));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$dataRescisao", contrato.getDataRescisaoString()));
            if (contrato.getTipoDesligamento() != null) {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$tipoRescisao", contrato.getTipoDesligamento().getDescricao()));
            } else {
                modeloContrato.setDescricao(modeloContrato.getDescricao().replace("$tipoRescisao", " -- "));
            }

            // ADICIONAIS
            modeloContrato.setDescricao(modeloContrato.getDescricao().replace("<br>", "<br />"));
            modeloContrato.setDescricao(modeloContrato.getDescricao().replaceAll("(<img[^>]*[^/]>)(?!\\s*</img>)", "$1</img>"));
            try {
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/"));
                if (!dirFile.exists()) {
                    if (!Dirs.create("arquivos/contrato")) {
                        return null;
                    }
                }
                String modelo = modeloContrato.getTitulo().replace(" ", "_");
                modelo = modelo.toLowerCase();
                String fileName = "modelo_" + modelo + "_" + DataHoje.hora().hashCode() + ".pdf";
                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato/" + fileName);
                File file = new File(filePDF);
                boolean success = file.createNewFile();
                if (success) {
                    OutputStream os = new FileOutputStream(filePDF);
                    HtmlToPDF.convert(modeloContrato.getDescricao(), os);
                    os.close();
                    if (compact) {
                        return filePDF;
                    }
                    String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/contrato");
                    Download download = new Download(fileName, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                    download.open();
                    download.close();
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (com.itextpdf.text.DocumentException ex) {

            }
        }
        return null;
    }

    public List<ModeloDocumentos> getListModeloDocumentos() {
        if (listModeloDocumentos.isEmpty()) {
            ModeloDocumentosDao mdd = new ModeloDocumentosDao();
            listModeloDocumentos = mdd.pesquisaTodosPorRotina(73);
        }
        return listModeloDocumentos;
    }

    public void setListModeloDocumentos(List<ModeloDocumentos> listModeloDocumentos) {
        this.listModeloDocumentos = listModeloDocumentos;
    }

    public String getAdicionarDias() {
        return adicionarDias;
    }

    public void setAdicionarDias(String adicionarDias) {
        this.adicionarDias = adicionarDias;
    }

    public String getCalculaValorMovimentoAlterado() {
        if (contrato.getId() == null) {
            if (!listMovimentoContrato.isEmpty()) {
                try {
                    disabledSave = false;
                    BigDecimal vc = new BigDecimal(0);
                    Float vn = new Float(0);
                    Float v = new Float(0);
                    for (int i = 0; i < listMovimentoContrato.size(); i++) {
                        vc = vc.add(new BigDecimal(Float.toString(listMovimentoContrato.get(i).getValor())));
                    }
                    v = Float.parseFloat(vc.toString());
                    vn = v;
                    if (!v.equals(contrato.getValorTotal())) {
                        disabledSave = true;
                        if (vn > 0) {
                            return "Existe uma difereça na soma das parcelas do contrato: Acrescentar R$ " + Moeda.converteR$Float(vn) + ".  Corrigir para salvar.";
                        } else if (vn < 0) {
                            if (!listMovimentoContrato.isEmpty()) {
                                return "Existe uma difereça na soma das parcelas do contrato: Remover R$ " + Moeda.converteR$Float(vn) + ".  Corrigir para salvar.";
                            }
                        } else {
                            return "";
                        }
                    }
                } catch (Exception e) {
                    return "Erro no valor";
                }
            }
        }
        return "";
    }

    public boolean isDisabledSave() {
        return disabledSave;
    }

    public void setDisabledSave(boolean disabledSave) {
        this.disabledSave = disabledSave;
    }

    public void setCalculaValorMovimentoAlterado(String calculaValorMovimentoAlterado) {
        this.calculaValorMovimentoAlterado = calculaValorMovimentoAlterado;
    }

    public List<SelectItem> getListFTipoDocumento() {
        if (listFTipoDocumento.isEmpty()) {
            Dao dao = new Dao();
            List<FTipoDocumento> list = (List<FTipoDocumento>) dao.find("FTipoDocumento", new int[]{2, 13});
            for (int i = 0; i < list.size(); i++) {
                listFTipoDocumento.add(new SelectItem(list.get(i).getId(), list.get(i).getDescricao()));
            }
        }
        return listFTipoDocumento;
    }

    public void setListFTipoDocumento(List<SelectItem> listFTipoDocumento) {
        this.listFTipoDocumento = listFTipoDocumento;
    }

    public void editMovimento(int index, Movimento m) {
        indexList = index;
        updateMovimento = m;
        idFTipoDocumento = "" + m.getTipoDocumento().getId();
    }

    public void updateFTipoDocumento(int tipo) {
        Dao dao = new Dao();
        if (tipo == 0) {
            listMovimentoContrato.get(indexList).setTipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), Integer.parseInt(idFTipoDocumento)));
            if (listMovimentoContrato.get(indexList).getId() != -1) {
                dao.update(listMovimentoContrato.get(indexList), true);
            }
        } else {
            listMovimentoTaxa.get(indexList).setTipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), Integer.parseInt(idFTipoDocumento)));
            if (listMovimentoTaxa.get(indexList).getId() != -1) {
                dao.update(listMovimentoTaxa.get(indexList), true);
            }
        }
        updateMovimento = new Movimento();
        indexList = 0;
        idFTipoDocumento = null;
    }

    public String getIdFTipoDocumento() {
        return idFTipoDocumento;
    }

    public void setIdFTipoDocumento(String idFTipoDocumento) {
        this.idFTipoDocumento = idFTipoDocumento;
    }

    public Movimento getUpdateMovimento() {
        return updateMovimento;
    }

    public void setUpdateMovimento(Movimento updateMovimento) {
        this.updateMovimento = updateMovimento;
    }

    public Integer getIndexList() {
        return indexList;
    }

    public void setIndexList(Integer indexList) {
        this.indexList = indexList;
    }

    public void printBoletos() {
        ImpressaoBoleto.IMPRIME_VERSO = imprimeVerso;
        ImpressaoBoleto.IMPRIME_VERSO_FIM = true;
        ImpressaoBoleto.printByLote(listMovimento.get(0).getLote().getId());
        ImpressaoBoleto.IMPRIME_VERSO = false;
    }

    public void printBoletos(String nrCtrBoleto) {
        ImpressaoBoleto.IMPRIME_VERSO = imprimeVerso;
        ImpressaoBoleto.IMPRIME_VERSO_FIM = true;
        ImpressaoBoleto.printByNrCtrBoleto(nrCtrBoleto);
        ImpressaoBoleto.IMPRIME_VERSO = false;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listContratos.clear();
        loadContratos();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listContratos.clear();
        loadContratos();
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        if (porPesquisa.equals("contrato")) {
            try {
                Integer.parseInt(descricaoPesquisa);
            } catch (NumberFormatException e) {
                descricaoPesquisa = "";
            }
        }
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getMask() {
        String mask = porPesquisa;

        if (mask.equals("paciente_documento") || mask.equals("responsavel_documento")) {
            mask = "cpf";
        }
        return Mask.getMascaraPesquisa(mask, true);
    }

    public void loadContratos() {
        ContratoDao contratoDao = new ContratoDao();
        contratoDao.setCliente(SessaoCliente.get().getId());
        listContratos = (List<Contrato>) contratoDao.find(porPesquisa, comoPesquisa, descricaoPesquisa);
    }

    public void putValidacao(String validacao_tipo) {
        Sessions.remove("remove_ids");
        if (validacao_tipo.equals("responsavel")) {
            pesquisaResponsavel = true;
            Sessions.put("validacao_tipo", validacao_tipo);
            if (contrato.getResponsavel().getId() != null && contrato.getResponsavel().getId() != -1) {
                Sessions.put("remove_ids", contrato.getResponsavel().getId());
            }
        } else {
            pesquisaResponsavel = false;
            Sessions.put("validacao_tipo", validacao_tipo);
            if (contrato.getResponsavel().getId() != null && contrato.getResponsavel().getId() != -1) {
                Sessions.put("remove_ids", "" + contrato.getResponsavel().getId());
            }
        }
    }

    public Boolean getImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(Boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public String[] getMensagem() {
        return mensagem;
    }

    public void setMensagem(String[] mensagem) {
        this.mensagem = mensagem;
    }

    public Boolean disabledPrint(Movimento m) {
        if (m.getLote() != null) {
            if (m.getLote().getContrato().getId() == null) {
                return true;
            } else if (m.getBaixa() != null) {
                return true;
            }
        }
        return false;
    }

    public void saveMensagemBoleto(Integer tcase) {
        BoletoDao boletoDao = new BoletoDao();
        String msg = "";
        if (!mensagem[0].isEmpty()) {
            msg = mensagem[0];
            mensagem[0] = "";
        } else if (!mensagem[1].isEmpty()) {
            msg = mensagem[1];
            mensagem[1] = "";
        }
        if (tcase == 0) {
            for (Movimento listMovimento1 : listMovimento) {
                Boleto boleto = boletoDao.findBoletoByNrCtrBoleto(listMovimento1.getNrCtrBoleto());
                if (boleto != null) {
                    boleto.setMensagem(msg);
                    new Dao().update(boleto, true);
                }
            }
        } else if (tcase == 1) {
            Boleto boleto = boletoDao.findBoletoByNrCtrBoleto(updateMovimento.getNrCtrBoleto());
            if (boleto != null) {
                boleto.setMensagem(msg);
                new Dao().update(boleto, true);
            }
        }
        updateMovimento = new Movimento();
    }

    public Integer getNumeroParcelasTaxa() {
        return numeroParcelasTaxa;
    }

    public void setNumeroParcelasTaxa(Integer numeroParcelasTaxa) {
        this.numeroParcelasTaxa = numeroParcelasTaxa;
    }

    public String getNumeroParcelasTaxaString() {
        return Integer.toString(numeroParcelasTaxa);
    }

    public void setNumeroParcelasTaxaString(String numeroParcelasTaxaString) {
        try {
            Integer np = Integer.parseInt(numeroParcelasTaxaString);
            if (np < 0) {
                return;
            }
            if (Integer.parseInt(numeroParcelasTaxaString) > contrato.getQuantidadeParcelas()) {
//                this.numeroParcelasTaxa = contrato.getQuantidadeParcelas();
//                Messages.warn("Validação", "Número de parcelas da taxa deve ser menor ou igual ao número de parcelas do contrato!");
                this.numeroParcelasTaxa = Integer.parseInt(numeroParcelasTaxaString);
            } else {
                this.numeroParcelasTaxa = Integer.parseInt(numeroParcelasTaxaString);
            }
        } catch (Exception e) {

        }
    }

    /**
     * <ul>
     * <li>1 - Dia de Vencimento;</li>
     * </ul>
     *
     * @param tcase
     */
    public void listener(Integer tcase) {
        switch (tcase) {
            case 1:
                if (contrato.getDataCadastroString().equals(DataHoje.data())) {
                    diaVencimento = Integer.parseInt(DataHoje.livre(new Date(), "dd"));
                } else {
                    diaVencimento = Integer.parseInt(DataHoje.livre(contrato.getDataCadastro(), "dd"));
                }
                break;
            case 2:
                Integer tipo_contrato_id = getTipoContrato().getId();
                if (tipo_contrato_id == 2) {
                    contrato.setValorEntrada(new Float(0));
                    contrato.setValorTotal(new Float(0));
                    contrato.setQuantidadeParcelas(0);
                    disabled[0] = true;
                } else {
                    disabled[0] = false;
                }
                if (tipo_contrato_id == 3) {
                    disabled[1] = true;
                } else {
                    disabled[1] = false;
                }
                break;
            case 3:
                diaVencimento = Integer.parseInt(DataHoje.livre(contrato.getDataCadastro(), "d"));
                listMovimento.clear();
                calculaSaldoDevedor();
                break;
        }
    }

    public void selectedDataCadastro(SelectEvent event) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
            contrato.setDataCadastro(DataHoje.converte(format.format(event.getObject())));
            listener(1);
        } catch (Exception e) {

        }
    }

    public List<SelectItem> getListTipoContrato() {
        if (listTipoContrato.isEmpty()) {
            Dao dao = new Dao();
            List<TipoContrato> list = (List<TipoContrato>) dao.list(new TipoContrato());
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idTipoContrato = i;
                }
                listTipoContrato.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoContrato;
    }

    public void setListTipoContrato(List<SelectItem> listTipoContrato) {
        this.listTipoContrato = listTipoContrato;
    }

    public Integer getIdTipoContrato() {
        return idTipoContrato;
    }

    public void setIdTipoContrato(Integer idTipoContrato) {
        this.idTipoContrato = idTipoContrato;
    }

    /**
     * 1 - Social 2 - Entidade
     *
     * @return
     */
    public Boolean[] getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean[] disabled) {
        this.disabled = disabled;
    }

    public Juridica getCobranca2() {
        if (Sessions.exists("juridicaPesquisa")) {
            contrato.setCobranca2(((Juridica) Sessions.getObject("juridicaPesquisa", true)).getPessoa());
            find(1);

        }
        return cobranca2;
    }

    public TipoContrato getTipoContrato() {
        TipoContrato tc = (TipoContrato) new Dao().find(new TipoContrato(), Integer.parseInt(listTipoContrato.get(idTipoContrato).getDescription()));
        if (tc == null) {
            return new TipoContrato();
        } else {
            return tc;
        }
    }

    /**
     * 1 - Pesquisa pessoa jurídica;
     *
     * @param tcase
     */
    public void find(Integer tcase) {
        switch (tcase) {
            case 1:
                if (contrato.getCobranca2() != null) {
                    cobranca2 = new JuridicaDao().pesquisaJuridicaPorPessoa(contrato.getCobranca2().getId());
                }
                break;
        }

    }

    public Float getValorTotalResponsavel() {
        return valorTotalResponsavel;
    }

    public void setValorTotalResponsavel(Float valorTotalResponsavel) {
        this.valorTotalResponsavel = valorTotalResponsavel;
    }

    public String getValorTotalResponsavelString() {
        return Moeda.converteR$Float(valorTotalResponsavel);
    }

    public void setValorTotalResponsavelString(String valorTotalResponsavelString) {
        this.valorTotalResponsavel = Moeda.converteUS$(valorTotalResponsavelString);
        if (this.valorTotalResponsavel < 0) {
            this.valorTotalResponsavel = new Float(0);
        }
        if (valorTotalResponsavel > contrato.getValorTotal()) {
            valorTotalResponsavel = new Float(0);
            valorTotalCobranca2 = new Float(0);
        }
    }

    public Float getValorTotalCobranca2() {
        return valorTotalResponsavel;
    }

    public void setValorTotalCobranca2(Float valorTotalCobranca2) {
        this.valorTotalCobranca2 = valorTotalCobranca2;
    }

    public String getValorTotalCobranca2String() {
        return Moeda.converteR$Float(valorTotalCobranca2);
    }

    public void setValorTotalCobranca2String(String valorTotalCobrancaString) {
        this.valorTotalCobranca2 = Moeda.converteUS$(valorTotalCobrancaString);
        if (this.valorTotalCobranca2 < 0) {
            this.valorTotalCobranca2 = new Float(0);
        }
        if (valorTotalCobranca2 > contrato.getValorTotal()) {
            valorTotalCobranca2 = new Float(0);
            valorTotalResponsavel = contrato.getValorTotal();
        }
    }

    public void calculaValoresTotais() {
        calculaValoresTotais(0);
    }

    public void calculaValoresTotais(Integer tcase) {
        if (tcase == 0) {
            valorTotalResponsavel = contrato.getValorTotal();
            valorTotalCobranca2 = new Float(0);
            contrato.setValorEntrada(new Float(0));
            contrato.setValorEntrada2(new Float(0));
        }
        if (tcase == 1) {
            if (valorTotalResponsavel == 0 && valorTotalCobranca2 == 0) {
                valorTotalCobranca2 = contrato.getValorTotal();
            }
            if (valorTotalResponsavel > 0 && !valorTotalResponsavel.equals(contrato.getValorTotal()) && valorTotalCobranca2 == 0) {
                valorTotalCobranca2 = contrato.getValorTotal() - valorTotalResponsavel;
            }
            if (valorTotalResponsavel > 0 && !valorTotalResponsavel.equals(contrato.getValorTotal()) && valorTotalCobranca2 > 3) {
                valorTotalCobranca2 = contrato.getValorTotal() - valorTotalResponsavel;
            }
        }
        if (tcase == 2) {
            if (valorTotalCobranca2 == 0) {
                valorTotalResponsavel = contrato.getValorTotal();
            }
            if (valorTotalCobranca2 > contrato.getValorTotal()) {
                valorTotalResponsavel = contrato.getValorTotal();
                valorTotalCobranca2 = new Float(0);
            }
            if (valorTotalCobranca2 >= valorTotalResponsavel) {
                valorTotalResponsavel = contrato.getValorTotal() - valorTotalCobranca2;
            }
            if (valorTotalCobranca2 < valorTotalResponsavel) {
                valorTotalResponsavel = contrato.getValorTotal() - valorTotalCobranca2;
            }
        }
        if (tcase == 3) {
            float vt = contrato.getValorTotal();
            float vt2 = 0;
            float vc = 0;
            float vr = 0;
            if (contrato.getValorTotal2() == 0) {
                valorTotalResponsavel = contrato.getValorTotal();
                valorTotalCobranca2 = new Float(0);
            } else if (contrato.getValorTotal2() != 0) {
                vr = contrato.getValorTotal() - contrato.getValorTotal2();
                if (vr == 0) {
                    valorTotalCobranca2 = contrato.getValorTotal();
                } else if (vr > contrato.getValorTotal2()) {
                    valorTotalResponsavel = vr;
                    valorTotalCobranca2 = contrato.getValorTotal() - vr;
                } else if (vr < contrato.getValorTotal2()) {
                    valorTotalResponsavel = contrato.getValorTotal() - contrato.getValorTotal2();
                    valorTotalCobranca2 = contrato.getValorTotal2();
                }
            }
        }
        contrato.setValorTotal2(valorTotalCobranca2);
        calculaSaldoDevedor();
    }

    public void alterDataVencimentoContrato(Integer index, Movimento m) {
        if (index == 0) {
            if (DataHoje.converteDataParaInteger(m.getVencimentoString()) < DataHoje.converteDataParaInteger(contrato.getDataCadastroString())) {
                m.setVencimentoString(contrato.getDataCadastroString());
            }
        } else {
            try {
                if (m.getId() == listMovimentoContrato.get(index - 1).getId() && m.getPessoa().getId().equals(listMovimentoContrato.get(index - 1).getPessoa().getId())) {
                    if (DataHoje.converteDataParaInteger(m.getVencimentoString()) >= DataHoje.converteDataParaInteger(listMovimentoContrato.get(index - 1).getVencimentoString())) {
                        listMovimentoContrato.get(index).setVencimentoString(m.getVencimentoString());
                    } else {
                        listMovimentoContrato.get(index).setVencimentoString(m.getVencimentoMemoriaString());
                    }
                }
            } catch (Exception e) {

            }
            try {
                if (m.getId() == listMovimentoContrato.get(index + 1).getId() && m.getPessoa().getId().equals(listMovimentoContrato.get(index + 1).getPessoa().getId())) {
                    if (DataHoje.converteDataParaInteger(m.getVencimentoString()) <= DataHoje.converteDataParaInteger(listMovimentoContrato.get(index + 1).getVencimentoString())) {
                        listMovimentoContrato.get(index).setVencimentoString(m.getVencimentoString());
                    } else {
                        listMovimentoContrato.get(index).setVencimentoString(m.getVencimentoMemoriaString());
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public void alterDataVencimentoTaxa(Integer index, Movimento m) {
        if (index == 0) {
            if (DataHoje.converteDataParaInteger(m.getVencimentoString()) < DataHoje.converteDataParaInteger(contrato.getDataCadastroString())) {
                m.setVencimentoString(contrato.getDataCadastroString());
            }
        } else {
            try {
                if (m.getId() == listMovimentoTaxa.get(index - 1).getId() && m.getPessoa().getId().equals(listMovimentoTaxa.get(index - 1).getPessoa().getId())) {
                    if (DataHoje.converteDataParaInteger(m.getVencimentoString()) >= DataHoje.converteDataParaInteger(listMovimentoTaxa.get(index - 1).getVencimentoString())) {
                        listMovimentoTaxa.get(index).setVencimentoString(m.getVencimentoString());
                    } else {
                        listMovimentoTaxa.get(index).setVencimentoString(m.getVencimentoMemoriaString());
                    }
                }
            } catch (Exception e) {

            }
            try {
                if (m.getId() == listMovimentoTaxa.get(index + 1).getId() && m.getPessoa().getId().equals(listMovimentoTaxa.get(index + 1).getPessoa().getId())) {
                    if (DataHoje.converteDataParaInteger(m.getVencimentoString()) <= DataHoje.converteDataParaInteger(listMovimentoTaxa.get(index + 1).getVencimentoString())) {
                        listMovimentoTaxa.get(index).setVencimentoString(m.getVencimentoString());
                    } else {
                        listMovimentoTaxa.get(index).setVencimentoString(m.getVencimentoMemoriaString());
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    public Date getDataCadastro() {
        return contrato.getDataCadastro();
    }

    public void setDataCadastro(Date dataCadastro) {
        if (!DataHoje.converteData(dataCadastro).equals(DataHoje.data())) {
            Boolean update = false;
            if (!DataHoje.converteData(dataCadastro).equals(contrato.getDataCadastroString())) {
                update = true;
            }
            contrato.setDataCadastro(dataCadastro);
            if (contrato.getDataCadastro() != null) {
                if (!listMovimentoContrato.isEmpty()) {
                    listener(3);
                    if (update) {
                        PF.update("form_contrato");
                    }
                }
            }

        } else {
            contrato.setDataCadastro(dataCadastro);
        }
    }

    public void updateAtePessoa() {
        Dao dao = new Dao();
        try {
            AteFamiliaContrato ateFamiliaContrato = new AteFamiliaContratoDao().findByContrato(contrato.getId());
            dao.openTransaction();
            if (ateFamiliaContrato == null) {
                ateFamiliaContrato = new AteFamiliaContrato();
                AteFamiliaDao ateFamiliaDao = new AteFamiliaDao();
                List<AteFamilia> listAteFamilia = ateFamiliaDao.findByCliente(SessaoCliente.get().getId());
                if (!listAteFamilia.isEmpty()) {
                    AteFamilia atual = null;
                    for (int i = 0; i < listAteFamilia.size(); i++) {
                        if (listAteFamilia.get(i).getPonteiro()) {
                            atual = listAteFamilia.get(i);
                            break;
                        }
                    }
                    if (atual == null) {
                        atual = listAteFamilia.get(0);
                    }
                    ateFamiliaContrato.setEquipe(atual.getEquipe());
                    ateFamiliaContrato.setContrato(contrato);
                    if (dao.save(ateFamiliaContrato)) {
                        AteFamilia af = ateFamiliaDao.nextOrdemByCliente(atual.getOrdem(), contrato.getCliente().getId());
                        for (int i = 0; i < listAteFamilia.size(); i++) {
                            if (listAteFamilia.get(i).getId().equals(af.getId())) {
                                listAteFamilia.get(i).setPonteiro(true);
                            } else {
                                listAteFamilia.get(i).setPonteiro(false);
                            }
                            if (!dao.update(listAteFamilia.get(i))) {
                                dao.rollback();
                                break;
                            }
                        }
                        dao.commit();
                    } else {
                        dao.rollback();
                    }
                }
            } else {
                dao.rollback();
            }
        } catch (Exception e) {
            dao.rollback();
        }
    }
}
