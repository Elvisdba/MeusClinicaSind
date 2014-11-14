package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.administrativo.ModeloContrato;
import br.com.clinicaintegrada.administrativo.ModeloDocumentos;
import br.com.clinicaintegrada.administrativo.Taxas;
import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.administrativo.dao.ModeloContratoDao;
import br.com.clinicaintegrada.administrativo.dao.ModeloDocumentosDao;
import br.com.clinicaintegrada.administrativo.dao.TaxasDao;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.dao.ContratoDao;
import br.com.clinicaintegrada.financeiro.CondicaoPagamento;
import br.com.clinicaintegrada.financeiro.FStatus;
import br.com.clinicaintegrada.financeiro.FTipoDocumento;
import br.com.clinicaintegrada.financeiro.Lote;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.dao.LoteDao;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Diretorio;
import br.com.clinicaintegrada.utils.Download;
import br.com.clinicaintegrada.utils.HtmlToPDF;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.ValidDocuments;
import br.com.clinicaintegrada.utils.ValorExtenso;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class ContratoBean implements Serializable {

    private Contrato contrato;
    private Filial filialSessao;
    private List<Contrato> listContratos;
    private List<SelectItem> listFilial;
    private List<SelectItem> listFilialAtual;
    private List<SelectItem> listTipoInternacao;
    private List<SelectItem> listTipoDesligamento;
    private List<Movimento> listMovimento;
    private List<Movimento> listMovimentoContrato;
    private List<Movimento> listMovimentoTaxa;
    private int idFilial;
    private int idFilialAtual;
    private int idTipoInternacao;
    private int idTipoDesligamento;
    private boolean pesquisaResponsavel;
    private String saldoDevedor;
    private int diaVencimento;
    private List<SelectItem> listTaxas;
    private int idTaxa;
    private String valorServico;
    private String vencimentoString;
    private String valorTotalTaxa;
    private List<ModeloDocumentos> listModeloDocumentos;
    private String adicionarDias;
    private boolean disabledSave;
    private String calculaValorMovimentoAlterado;
    private List<SelectItem> listFTipoDocumento;
    private String idFTipoDocumento;
    private Movimento updateMovimento;
    private int indexList;

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
        listMovimento = new ArrayList<>();
        listMovimentoContrato = new ArrayList<>();
        listMovimentoTaxa = new ArrayList<>();
        idFilial = 0;
        idFilialAtual = 0;
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
    }

    @PreDestroy
    public void destroy() {
        clear();
        Sessions.remove("contratoBean");
        Sessions.remove("pessoaPesquisa");
        Sessions.remove("fisicaPesquisa");
        Sessions.remove("contratoPesquisa");
        Sessions.remove("pesquisaResponsavelContrato");
    }

    public void clear() {
        Sessions.remove("contratoBean");
    }

    public void clear(int tcase) {
        switch (tcase) {
            case 1:
                contrato.setResponsavel(new Pessoa());
                break;
            case 2:
                contrato.setPaciente(new Pessoa());
                break;
        }
    }

    public void save() {
        if (listTipoInternacao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipos de internação!");
            return;
        }
        if (disabledSave) {
            Messages.warn("Validação", getCalculaValorMovimentoAlterado());
            return;
        }
        Dao dao = new Dao();
        contrato.setTipoInternacao((TipoInternacao) dao.find(new TipoInternacao(), Integer.parseInt(listTipoInternacao.get(idTipoInternacao).getDescription())));
        Logger logger = new Logger();
        contrato.setFilial(MacFilial.getAcessoFilial().getFilial());
        contrato.setFilialAtual(MacFilial.getAcessoFilial().getFilial());
        dao.openTransaction();
        if (contrato.getId() == -1) {
            contrato.setCliente(SessaoCliente.get());
            contrato.setFilial(MacFilial.getAcessoFilial().getFilial());
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
                valorTotal = valorTotal + contrato.getValorTotal();
                if (!listMovimento.isEmpty()) {
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
                        + " - Filial Atual: [" + contrato.getFilialAtual().getFilial().getPessoa().getNome() + "] - " + contrato.getFilialAtual().getFilial().getPessoa().getNome()
                        + " - Responsável: [" + contrato.getResponsavel().getId() + "] - " + contrato.getResponsavel().getNome()
                        + " - Paciente: [" + contrato.getPaciente().getId() + "] - " + contrato.getPaciente().getNome()
                );
                Messages.info("Sucesso", "Registro inserido!");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        } else {
            if (listTipoDesligamento.isEmpty()) {
                Messages.warn("Validação", "Cadastrar tipos de desligamento!");
                return;
            }
            contrato.setTipoDesligamento((TipoDesligamento) dao.find(new TipoDesligamento(), Integer.parseInt(listTipoDesligamento.get(idTipoDesligamento).getDescription())));
            Contrato c = (Contrato) dao.find(new Contrato(), contrato.getId());
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Filial: [" + c.getFilial().getFilial().getPessoa().getId() + "] - " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Filial Atual: [" + c.getFilialAtual().getFilial().getPessoa().getNome() + "] - " + c.getFilialAtual().getFilial().getPessoa().getNome()
                    + " - Responsável: [" + c.getResponsavel().getId() + "] - " + c.getResponsavel().getNome()
                    + " - Paciente: [" + c.getPaciente().getId() + "] - " + c.getPaciente().getNome();
            if (dao.update(contrato)) {
                dao.commit();
                Messages.info("Sucesso", "Registro atualizado!");
                logger.update(beforeUpdate,
                        "ID: " + contrato.getId()
                        + " - Filial: [" + contrato.getFilial().getFilial().getPessoa().getId() + "] - " + contrato.getFilial().getFilial().getPessoa().getNome()
                        + " - Filial Atual: [" + contrato.getFilialAtual().getFilial().getPessoa().getNome() + "] - " + contrato.getFilialAtual().getFilial().getPessoa().getNome()
                        + " - Responsável: [" + contrato.getResponsavel().getId() + "] - " + contrato.getResponsavel().getNome()
                        + " - Paciente: [" + contrato.getPaciente().getId() + "] - " + contrato.getPaciente().getNome()
                );
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }

    }

    public String edit(Contrato c) {
        contrato = new Contrato();
        listContratos.clear();
        contrato = c;
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
        for (int i = 0; i < listTipoDesligamento.size(); i++) {
            if (contrato.getTipoDesligamento().getId() == Integer.parseInt(listTipoDesligamento.get(i).getDescription())) {
                idTipoDesligamento = i;
                break;
            }
        }
        listTaxas.clear();
        getListMovimentoTaxa();
        getListTaxas();
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        valorTotalTaxa = Moeda.converteR$Float(vt);
        calculaSaldoDevedor();
        Sessions.put("linkClicado", true);
        return "contrato";
    }

    public void delete() {
        delete(contrato);
    }

    public void delete(Contrato c) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        dao.openTransaction();
        if (contrato.getId() != -1) {
            for (int i = 0; i < listMovimento.size(); i++) {
                if (!dao.delete(listMovimento.get(i))) {
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
            if (contrato.getId() == -1) {
                saldoDevedor = Float.toString((contrato.getValorTotal()));
            } else {
                List<Movimento> list = getListMovimentoContrato();
                float d = 0;
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getBaixa() != null) {
                        d += list.get(i).getValor();
                    }
                }
                saldoDevedor = Float.toString((contrato.getValorTotal()) - d);
            }
            float result = (contrato.getValorTotal() - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
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
            }
        }
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public List<Contrato> getListContratos() {
        listContratos.clear();
        Dao dao = new Dao();
        listContratos = dao.list(new Contrato());
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
                if (!isFilial) {
                    if (macFilial.getFilial().getId() == list.get(i).getId()) {
                        idFilialAtual = i;
                        isFilial = true;
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
                listTipoInternacao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }

        }
        return listTipoInternacao;
    }

    public void setListTipoInternacao(List<SelectItem> listTipoInternacao) {
        this.listTipoInternacao = listTipoInternacao;
    }

    public List<SelectItem> getListTipoDesligamento() {
        if (listTipoDesligamento.isEmpty()) {
            Dao dao = new Dao();
            List<TipoDesligamento> list = (List<TipoDesligamento>) dao.list(new TipoDesligamento(), true);
            for (int i = 0; i < list.size(); i++) {
                listTipoDesligamento.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoDesligamento;
    }

    public void setListTipoDesligamento(List<SelectItem> listTipoDesligamento) {
        this.listTipoDesligamento = listTipoDesligamento;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdFilialAtual() {
        return idFilialAtual;
    }

    public void setIdFilialAtual(int idFilialAtual) {
        this.idFilialAtual = idFilialAtual;
    }

    public int getIdTipoInternacao() {
        return idTipoInternacao;
    }

    public void setIdTipoInternacao(int idTipoInternacao) {
        this.idTipoInternacao = idTipoInternacao;
    }

    public int getIdTipoDesligamento() {
        return idTipoDesligamento;
    }

    public void setIdTipoDesligamento(int idTipoDesligamento) {
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
        if (contrato.getId() == -1) {
            if (contrato.getQuantidadeParcelas() == 0) {
                Messages.warn("Validação", "Informar número de parcelas maior que 0, Ex. 1");
                return;
            }
            int addDias = 0;
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
            if (listMovimentoContrato.isEmpty()) {
                String nrCtrBoletoResp = "";
                for (int x = 0; x < (Integer.toString(contrato.getResponsavel().getId())).length(); x++) {
                    nrCtrBoletoResp += 0;
                }
                nrCtrBoletoResp += contrato.getResponsavel().getId();
                String nrCtrBoleto = "";
                boolean isEntrada = false;
                if (contrato.getValorEntrada() > 0) {
                    if (addDias > 0) {
                        m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, contrato.getDataCadastroString())));
                        isEntrada = true;
                    } else {
                        m.setVencimentoString(DataHoje.data());
                        m.setVencimento(contrato.getDataCadastro());
                    }
                    m.setPessoa(contrato.getResponsavel());
                    m.setTipoDocumento(s1.getTipoDocumento());
                    m.setServicos(s1);
                    m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                    m.setEs("E");
                    nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(m.getVencimentoString())));
                    m.setNrCtrBoleto(nrCtrBoleto);
                    m.setReferencia(DataHoje.dataReferencia(m.getVencimentoString()));
                    m.setDocumento("");
                    m.setLote(null);
                    m.setAtivo(true);
                    m.setEvento(null);
                    m.setValor(contrato.getValorEntrada());
                    m.setBaixa(null);
                    listMovimentoContrato.add(m);
                    m = new Movimento();
                }
                String dataVencimento = DataHoje.alteraDiaVencimento(diaVencimento, DataHoje.data());
                if (dataVencimento.equals("")) {
                    diaVencimento = diaVencimento - 1;
                    dataVencimento = DataHoje.alteraDiaVencimento(diaVencimento, DataHoje.data());
                    if (dataVencimento.equals("")) {
                        diaVencimento = diaVencimento - 1;
                        dataVencimento = DataHoje.alteraDiaVencimento(diaVencimento, DataHoje.data());
                        if (dataVencimento.equals("")) {
                            diaVencimento = diaVencimento - 1;
                            dataVencimento = DataHoje.alteraDiaVencimento(diaVencimento, DataHoje.data());
                        }
                    }
                }
                if (Moeda.substituiVirgulaFloat(getSaldoDevedor()) > 0) {
                    int vencto = 0;
                    int h = 0;
                    float v = (Moeda.substituiVirgulaFloat(getSaldoDevedor()) - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
                    for (int i = 0; i < contrato.getQuantidadeParcelas(); i++) {
                        if (!isEntrada && i == 0 && addDias > 0) {
                            m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                            m.setVencimento(DataHoje.converte(dh.incrementarDias(addDias, m.getVencimentoString())));
                        } else {
                            m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                        }
                        nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(m.getVencimentoString())));
                        m.setPessoa(contrato.getResponsavel());
                        m.setTipoDocumento(s1.getTipoDocumento());
                        m.setServicos(s1);
                        m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                        m.setEs("E");
                        m.setNrCtrBoleto(nrCtrBoleto);
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
                //listMovimento.addAll(listMovimentoTaxa);
                //listMovimentoContrato
            }
        }
    }

    public List<Movimento> getListMovimento() {
        if (contrato.getId() != -1) {
            if (listMovimento.isEmpty()) {
                LoteDao loteDao = new LoteDao();
                Lote l = loteDao.findLoteByContrato(contrato.getId());
                if (l != null) {
                    MovimentoDao movimentoDao = new MovimentoDao();
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
            }
        }
        return listMovimentoContrato;

    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public void addTaxa() {
        DataHoje dh = new DataHoje();
        Movimento m = new Movimento();
        Dao dao = new Dao();
        int tam = listMovimentoContrato.size();
        if (tam > 0) {
            tam = listMovimentoContrato.size() - 1;
        }
        Taxas t = ((Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(idTaxa).getDescription())));
        String nrCtrBoleto = listMovimentoContrato.get(tam).getNrCtrBoleto() + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimentoString)));
        m.setPessoa(contrato.getResponsavel());
        m.setTipoDocumento(t.getServicos().getTipoDocumento());
        m.setServicos(t.getServicos());
        m.setTipoServico((TipoServico) dao.find(new TipoServico(), 5));
        m.setEs("E");
        m.setNrCtrBoleto(nrCtrBoleto);
        m.setVencimento(DataHoje.converte(vencimentoString));
        m.setReferencia(DataHoje.dataReferencia(vencimentoString));
        m.setDocumento("");
        m.setLote(null);
        m.setAtivo(true);
        m.setEvento(null);
        m.setValorString(valorServico);
        m.setBaixa(null);
        listTaxas.clear();
        if (contrato.getId() != -1) {
            //m.setTipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 1));
            m.setLote(listMovimento.get(0).getLote());
            if (dao.save(m, true)) {
                listMovimento.clear();
                listMovimentoTaxa.clear();
                Messages.info("Sucesso", "Taxa adicionada!");
            } else {
                Messages.warn("Validação", "Erro ao adicionar taxa!");
            }
        } else {
            Messages.info("Sucesso", "Taxa adicionada!");
            listMovimentoTaxa.add(m);
        }
        getListMovimentoTaxa();
        getListTaxas();
        valorServico = "0,00";
        vencimentoString = DataHoje.data();
        valorTotalTaxa = "0,00";
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        valorTotalTaxa = Moeda.converteR$Float(vt);
    }

    public void removeTaxa(int index) {
        Servicos s = new Servicos();
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            if (i == index) {
                s = listMovimentoTaxa.get(i).getServicos();
                break;
            }
        }
        listTaxas.clear();
        Dao dao = new Dao();
        if (contrato.getId() != -1) {
            if (dao.delete(listMovimentoTaxa.get(index), true)) {
                listMovimentoTaxa.clear();
                listMovimento.clear();
                getListMovimentoTaxa();
                Messages.info("Sucesso", "Taxa removida!");
            } else {
                Messages.warn("Validação", "Erro ao remover taxa!");
            }
        } else {
            Messages.info("Sucesso", "Taxa removida!");
            listMovimentoTaxa.remove(index);
        }
        getListTaxas();
        float vt = 0;
        for (int i = 0; i < listMovimentoTaxa.size(); i++) {
            vt += listMovimentoTaxa.get(i).getValor();
        }
        valorTotalTaxa = Moeda.converteR$Float(vt);
        idTaxa = 0;

    }

    public void selectedServico() {
        Dao dao = new Dao();
        valorServico = ((Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(idTaxa).getDescription()))).getValorString();
    }

    public List<SelectItem> getListTaxas() {
        if (listTaxas.isEmpty()) {
            TaxasDao taxasDao = new TaxasDao();
            List<Taxas> list = taxasDao.pesquisaTodasTaxasPorCliente(SessaoCliente.get().getId());
            Dao dao = new Dao();
            if (list.size() != listMovimentoTaxa.size()) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0) {
                        valorServico = list.get(i).getValorString();
                        idTaxa = i;
                    }
                    if (listMovimentoTaxa.isEmpty()) {
                        if (i == 0) {
                            valorServico = list.get(i).getValorString();
                            idTaxa = i;
                        }
                        listTaxas.add(new SelectItem(i, list.get(i).getServicos().getDescricao(), "" + list.get(i).getId()));
                    } else {
                        for (int j = 0; j < listMovimentoTaxa.size(); j++) {
                            if (listMovimentoTaxa.get(j).getServicos().getId() != list.get(i).getServicos().getId()) {
                                if (j == 0) {
                                    valorServico = list.get(i).getValorString();
                                    idTaxa = j;
                                }
                                listTaxas.add(new SelectItem(j, list.get(i).getServicos().getDescricao(), "" + list.get(i).getId()));
                            }
                        }
                    }
                }
            }
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

    public int getIdTaxa() {
        return idTaxa;
    }

    public void setIdTaxa(int idTaxa) {
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
        if (contrato.getId() != -1) {
            Dao dao = new Dao();
            String contratoDiaSemana = "";
            ModeloContrato modeloContrato = new ModeloContrato();
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            modeloContrato = modeloContratoDao.pesquisaCodigoServico(1);
            if (modeloContrato == null) {
                Messages.warn("Sistema", "Não é possível gerar um contrato para este serviço. Para gerar um contrato acesse: Menu Escola > Suporte > Modelo Contrato.");
                return;
            }
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
                    if (listaMovimento.getServicos().getId() == t.getServicos().getId()) {
                        if (!t.isOcultaContrato()) {
                            valorTaxaString += " - Vencimento: " + listaMovimento.getVencimentoString() + " - " + listaMovimento.getServicos().getDescricao() + " - R$ " + listaMovimento.getValorString() + "; <br /><br />";
                            valorTotalTaxas += listaMovimento.getValor();
                            break;
                        }
                    }
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
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"));
                if (!dirFile.exists()) {
                    if (!Diretorio.criar("Arquivos/contrato")) {
                        return;
                    }
                }
                String fileName = "contrato" + DataHoje.hora().hashCode() + ".pdf";
                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/_" + fileName);
                boolean success = new File(filePDF).createNewFile();
                //boolean success = file.createNewFile();
                boolean delete = false;
                if (success) {
                    OutputStream os = new FileOutputStream(filePDF);
                    HtmlToPDF.convert(modeloContrato.getDescricao(), os);
                    os.flush();
                    os.close();
                    String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato");
                    try {
                        PdfReader reader = new PdfReader(filePDF);
                        int n = reader.getNumberOfPages();
                        FileOutputStream fileOutputStream = new FileOutputStream(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName));
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
                        download.baixar();
                        download.remover();
                        if (contrato.getId() != -1) {
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

    public void imprimirOutros(int idModeloContratoDocumentos) {

        if (contrato.getId() != -1) {
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
                File dirFile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/"));
                if (!dirFile.exists()) {
                    if (!Diretorio.criar("Arquivos/contrato")) {
                        return;
                    }
                }
                String fileName = "modelo_extra" + DataHoje.hora().hashCode() + ".pdf";
                String filePDF = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato/" + fileName);
                File file = new File(filePDF);
                boolean success = file.createNewFile();
                if (success) {
                    OutputStream os = new FileOutputStream(filePDF);
                    HtmlToPDF.convert(modeloContrato.getDescricao(), os);
                    os.close();
                    String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/contrato");
                    Download download = new Download(fileName, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                    download.baixar();
                    download.remover();
                }
            } catch (IOException e) {
                e.getMessage();
            } catch (com.itextpdf.text.DocumentException ex) {

            }
        }
    }

    public List<ModeloDocumentos> getListModeloDocumentos() {
        listModeloDocumentos.clear();
        ModeloDocumentosDao mdd = new ModeloDocumentosDao();
        listModeloDocumentos = mdd.pesquisaTodosPorRotina(73);
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
        if (contrato.getId() == -1) {
            disabledSave = false;
            float v = 0;
            float vn = 0;
            for (int i = 0; i < listMovimentoContrato.size(); i++) {
                v += listMovimentoContrato.get(i).getValor();
            }
            vn = v;
            vn = contrato.getValorTotal() - v;
            if (v != contrato.getValorTotal()) {
                disabledSave = true;
                String message = "";
                if (vn > 0) {
                    message = " Acrescentar ";
                } else {
                    message = " Remover ";
                }
                return "Existe uma difereça na soma das parcelas do contrato: " + message + " R$ " + Moeda.converteR$Float(vn) + ".  Corrigir para salvar.";
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

    public int getIndexList() {
        return indexList;
    }

    public void setIndexList(int indexList) {
        this.indexList = indexList;
    }
}
