package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Correcao;
import br.com.clinicaintegrada.financeiro.Indice;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.dao.CorrecaoDao;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
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
public class CorrecaoBean implements Serializable {

    private int idServicos = 0;
    private int idIndices = 0;
    private Correcao correcao;
    private List<Correcao> listCorrecao;
    private List<SelectItem> listServicos;
    private List<SelectItem> listIndices;

    @PostConstruct
    public void init() {
        idServicos = 0;
        idIndices = 0;
        correcao = new Correcao();
        listCorrecao = new ArrayList<>();
        listServicos = new ArrayList<>();
        listIndices = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("correcaoBean");
    }

    public void save() {
        CorrecaoDao correcaoDao = new CorrecaoDao();
        DaoInterface di = new Dao();
        Logger novoLog = new Logger();
        Servicos servico = (Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(idServicos).getDescription()));
        correcao.setIndice((Indice) di.find(new Indice(), Integer.parseInt(getListIndices().get(idIndices).getDescription())));
        correcao.setServicos(servico);
        if (correcao.getId() == -1) {
            if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
                List dd = correcaoDao.findByServicoAndReferencias(servico, correcao.getReferenciaInicial(), correcao.getReferenciaFinal());
                if (Integer.parseInt(String.valueOf((Long) dd.get(0))) == 0) {
                    if (di.save(correcao, true)) {
                        novoLog.save(
                                "ID: " + correcao.getId()
                                + " - Índice: (" + correcao.getIndice().getId() + ") "
                                + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                                + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                                + " - Juros Diário: " + correcao.getJurosDiarios()
                                + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                                + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                                + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                                + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                                + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
                        );
                        Messages.info("Sucesso", "Correção Salva");
                        correcao = new Correcao();
                        idIndices = 0;
                        idServicos = 0;
                    } else {
                        Messages.warn("Erro", "Erro ao Salvar!");
                    }
                } else {
                    Messages.warn("Validação", "Correção já existente!");
                }
            } else {
                Messages.warn("Validação", "Referencia Invalida!");
            }
        } else if (DataHoje.validaReferencias(correcao.getReferenciaInicial(), correcao.getReferenciaFinal())) {
            Correcao c = (Correcao) di.find(correcao);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Índice: (" + c.getIndice().getId() + ") "
                    + " - Serviços: (" + c.getServicos().getId() + ") " + c.getServicos().getDescricao()
                    + " - Período: " + c.getReferenciaInicial() + " - " + c.getReferenciaFinal()
                    + " - Juros Diário: " + c.getJurosDiarios()
                    + " - Juros 1º Mês: " + c.getJurosPriMes()
                    + " - Juros >= 2º Mês: " + c.getJurosApartir2Mes()
                    + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                    + " - Multa 1º Mês: " + c.getMultaPriMes()
                    + " - Multa >= 2º Mês: " + c.getMultaApartir2Mes();
            if (di.update(correcao, true)) {
                novoLog.update(beforeUpdate,
                        "ID: " + correcao.getId()
                        + " - Índice: (" + correcao.getIndice().getId() + ") "
                        + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                        + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                        + " - Juros Diário: " + correcao.getJurosDiarios()
                        + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                        + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                        + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                        + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                        + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
                );
                Messages.info("Sucesso", "Correção Atualizada!");
                correcao = new Correcao();
                idIndices = 0;
                idServicos = 0;
            } else {
                Messages.warn("Erro", "Erro ao atualizar!");
            }
        } else {
            Messages.warn("Validação", "Referencia Invalida!");
        }
        listCorrecao.clear();
    }

    public String clear() {
        correcao = new Correcao();
        idIndices = 0;
        idServicos = 0;
        listCorrecao.clear();
        return "correcao";
    }

    public void delete(Correcao co) {
        correcao = co;
        DaoInterface di = new Dao();
        Logger novoLog = new Logger();
        if (di.delete(correcao, true)) {
            novoLog.delete(
                    "ID: " + correcao.getId()
                    + " - Índice: (" + correcao.getIndice().getId() + ") "
                    + " - Serviços: (" + correcao.getServicos().getId() + ") " + correcao.getServicos().getDescricao()
                    + " - Período: " + correcao.getReferenciaInicial() + " - " + correcao.getReferenciaFinal()
                    + " - Juros Diário: " + correcao.getJurosDiarios()
                    + " - Juros 1º Mês: " + correcao.getJurosPriMes()
                    + " - Juros >= 2º Mês: " + correcao.getJurosApartir2Mes()
                    + " - Multa por Funcionário: " + correcao.getMultaPorFuncionario()
                    + " - Multa 1º Mês: " + correcao.getMultaPriMes()
                    + " - Multa >= 2º Mês: " + correcao.getMultaApartir2Mes()
            );
            Messages.info("Sucesso", "Correção Excluida");
        } else {
            Messages.warn("Erro", "Erro ao excluir Correção!");
        }
        correcao = new Correcao();
        listCorrecao.clear();
    }

    public void edit(Correcao co) {
        correcao = co;
        for (int i = 0; i < getListServicos().size(); i++) {
            if (Integer.parseInt(getListServicos().get(i).getDescription()) == correcao.getServicos().getId()) {
                setIdServicos(i);
                break;
            }
        }
        for (int i = 0; i < getListIndices().size(); i++) {
            if (Integer.parseInt(getListIndices().get(i).getDescription()) == correcao.getIndice().getId()) {
                setIdIndices(i);
                break;
            }
        }
    }

    public List<Correcao> getListCorrecao() {
        if (listCorrecao.isEmpty()) {
            if (!getListServicos().isEmpty()) {
                CorrecaoDao correcaoDao = new CorrecaoDao();
                listCorrecao = correcaoDao.findAllByServico(Integer.parseInt(getListServicos().get(idServicos).getDescription()));
            }
        }
        return listCorrecao;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDao servicosDao = new ServicosDao();
            List select = servicosDao.findServicos(SessaoCliente.get().getId());
            for (int i = 0; i < select.size(); i++) {
                listServicos.add(new SelectItem(i,
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
            }
        }
        return listServicos;
    }

    public List<SelectItem> getListIndices() {
        if (listIndices.isEmpty()) {
            DaoInterface di = new Dao();
            List select = di.list(new Indice(), true);
            for (int i = 0; i < select.size(); i++) {
                listIndices.add(new SelectItem(i,
                        ((Indice) select.get(i)).getDescricao(),
                        Integer.toString(((Indice) select.get(i)).getId())));
            }
        }
        return listIndices;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdIndices() {
        return idIndices;
    }

    public void setIdIndices(int idIndices) {
        this.idIndices = idIndices;
    }

    public Correcao getCorrecao() {
        return correcao;
    }

    public void setCorrecao(Correcao correcao) {
        this.correcao = correcao;
    }

    public void setListCorrecao(List listCorrecao) {
        this.listCorrecao = listCorrecao;
    }
}
