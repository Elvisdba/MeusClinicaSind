package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.dao.ContratoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.ValidDocuments;
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
public class ContratoBean implements Serializable {

    private Contrato contrato;
    private Filial filialSessao;
    private List<Contrato> listContratos;
    private List<SelectItem> listFilial;
    private List<SelectItem> listFilialAtual;
    private List<SelectItem> listTipoInternacao;
    private List<SelectItem> listTipoDesligamento;
    private int idFilial;
    private int idFilialAtual;
    private int idTipoInternacao;
    private int idTipoDesligamento;
    private boolean pesquisaResponsavel;
    private String saldoDevedor;

    @PostConstruct
    public void init() {
        contrato = new Contrato();
        filialSessao = new Filial();
        listContratos = new ArrayList<>();
        listFilial = new ArrayList<>();
        listFilialAtual = new ArrayList<>();
        listTipoInternacao = new ArrayList<>();
        listTipoDesligamento = new ArrayList<>();
        idFilial = 0;
        idFilialAtual = 0;
        idTipoInternacao = 0;
        idTipoDesligamento = 0;
        pesquisaResponsavel = false;
        saldoDevedor = "0";
        calculaSaldoDevedor();
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
//        if (listFilial.isEmpty()) {
//            Messages.warn("Validação", "Cadastrar filiais!");
//            return;
//        }
//        if (listFilialAtual.isEmpty()) {
//            Messages.warn("Validação", "Cadastrar filiais!");
//            return;
//        }
        if (listTipoInternacao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipos de internação!");
            return;
        }
        if (listTipoDesligamento.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipos de desligamento!");
            return;
        }
        Dao dao = new Dao();
        contrato.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(listFilial.get(idFilial).getDescription())));
        contrato.setFilialAtual((Filial) dao.find(new Filial(), Integer.parseInt(listFilialAtual.get(idFilialAtual).getDescription())));
        contrato.setTipoInternacao((TipoInternacao) dao.find(new TipoInternacao(), Integer.parseInt(listTipoInternacao.get(idTipoInternacao).getDescription())));
        contrato.setTipoDesligamento((TipoDesligamento) dao.find(new TipoDesligamento(), Integer.parseInt(listTipoDesligamento.get(idTipoDesligamento).getDescription())));
        Logger logger = new Logger();
        contrato.setFilialAtual(MacFilial.getAcessoFilial().getFilial());
        if (contrato.getId() != -1) {
            contrato.setCliente(SessaoCliente.get());
            contrato.setFilial(MacFilial.getAcessoFilial().getFilial());
            ContratoDao contratoDao = new ContratoDao();
            if (contratoDao.existeContrato(contrato)) {
                Messages.warn("Validação", "Contrato já existe!");
                return;
            }
            if (dao.save(contrato)) {
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
            } else {
                dao.rollback();
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public void calculaSaldoDevedor() {
        if (contrato.getValorTotal() > 0) {
            float result = (contrato.getValorTotal() - contrato.getValorEntrada()) / contrato.getQuantidadeParcelas();
            saldoDevedor = Float.toString(result);
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
        return saldoDevedor;
    }

    public void setSaldoDevedor(String saldoDevedor) {
        this.saldoDevedor = saldoDevedor;
    }
}
