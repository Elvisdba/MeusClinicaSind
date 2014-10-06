package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.administrativo.Taxas;
import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
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
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.ValidDocuments;
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

    @PostConstruct
    public void init() {
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
                if (!listMovimento.isEmpty()) {
                    Lote lote = new Lote();
                    lote.setCliente(SessaoCliente.get());
                    lote.setValor(contrato.getValorTotal());
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
            listMovimentoContrato.clear();
            DataHoje dh = new DataHoje();
            Movimento m = new Movimento();
            if (listMovimentoContrato.isEmpty()) {
                Dao dao = new Dao();
                String nrCtrBoletoResp = "";
                for (int x = 0; x < (Integer.toString(contrato.getResponsavel().getId())).length(); x++) {
                    nrCtrBoletoResp += 0;
                }
                nrCtrBoletoResp += contrato.getResponsavel().getId();
                String nrCtrBoleto = "";
                if (contrato.getValorEntrada() > 0) {
                    m.setVencimento(contrato.getDataCadastro());
                    m.setPessoa(contrato.getResponsavel());
                    m.setfTipoDocumento(null);
                    m.setServicos((Servicos) dao.find(new Servicos(), 1));
                    m.setTipoServico((TipoServico) dao.find(new TipoServico(), 1));
                    m.setEs("E");
                    nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(m.getVencimentoString())));
                    m.setNrCtrBoleto(nrCtrBoleto);
                    m.setVencimentoString(DataHoje.data());
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
                        m.setVencimentoString(dh.incrementarMeses(i + 1, dataVencimento));
                        nrCtrBoleto = nrCtrBoletoResp + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(m.getVencimentoString())));
                        m.setPessoa(contrato.getResponsavel());
                        m.setfTipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 2));
                        m.setServicos((Servicos) dao.find(new Servicos(), 1));
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
        String nrCtrBoleto = listMovimentoContrato.get(tam).getNrCtrBoleto() + Long.toString(DataHoje.calculoDosDias(DataHoje.converte("07/10/1997"), DataHoje.converte(vencimentoString)));
        m.setPessoa(contrato.getResponsavel());
        m.setfTipoDocumento(null);
        m.setServicos(((Taxas) dao.find(new Taxas(), Integer.parseInt(listTaxas.get(idTaxa).getDescription()))).getServicos());
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
            m.setfTipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 1));
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
        if (contrato.getId() != -1) {
            contrato.setImpresso(true);
            Dao dao = new Dao();
            dao.update(contrato, true);
        }
    }
}
