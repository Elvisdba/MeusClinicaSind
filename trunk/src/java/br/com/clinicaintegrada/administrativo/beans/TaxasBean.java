package br.com.clinicaintegrada.administrativo.beans;

import br.com.clinicaintegrada.administrativo.Taxas;
import br.com.clinicaintegrada.administrativo.dao.TaxasDao;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
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
public class TaxasBean implements Serializable {

    private Taxas taxas;
    private List<Taxas> listTaxas;
    private List<SelectItem> listServicos;
    private Integer idServicos;

    @PostConstruct
    public void init() {
        taxas = new Taxas();
        listTaxas = new ArrayList<>();
        listServicos = new ArrayList<>();
        idServicos = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("taxasBean");
    }

    public void clear() {
        Sessions.remove("taxasBean");
    }

    public void save() {
        if (listServicos.isEmpty()) {
            Messages.warn("Validação", "Cadastrar serviços!");
            return;
        }
        Dao dao = new Dao();
        taxas.setServicos((Servicos) dao.find(new Servicos(), Integer.parseInt(listServicos.get(idServicos).getDescription())));
        Logger logger = new Logger();
        if (taxas.getId() == -1) {
            taxas.setCliente(SessaoCliente.get());
            TaxasDao taxasDao = new TaxasDao();
            if (taxasDao.existeTaxa(taxas)) {
                Messages.warn("Validação", "Taxa já existe!");
                return;
            }
            if (dao.save(taxas, true)) {
                logger.save(
                        "ID: " + taxas.getId()
                        + " - Serviços: [" + taxas.getServicos().getId() + "] - " + taxas.getServicos().getDescricao()
                        + " - Valor: " + taxas.getValorString()
                );
                listTaxas.clear();
                listServicos.clear();
                taxas = new Taxas();
                idServicos = 0;
                Messages.info("Sucesso", "Registro inserido!");
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        } else {
            Taxas t = (Taxas) dao.find(new Taxas(), taxas.getId());
            String beforeUpdate
                    = "ID: " + t.getId()
                    + " - Serviços: [" + t.getServicos().getId() + "] - " + t.getServicos().getDescricao()
                    + " - Valor: " + t.getValorString();
            if (dao.update(taxas, true)) {
                Messages.info("Sucesso", "Registro atualizado!");
                logger.update(beforeUpdate,
                        "ID: " + taxas.getId()
                        + " - Serviços: [" + taxas.getServicos().getId() + "] - " + taxas.getServicos().getDescricao()
                        + " - Valor: " + taxas.getValorString()
                );
                listTaxas.clear();
                listServicos.clear();
                taxas = new Taxas();
                idServicos = 0;
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }

    }

    public void edit(Taxas t) {
        listServicos.clear();
        idServicos = 0;
        getListServicos();
        taxas = new Taxas();
        taxas = t;
        listServicos.add(new SelectItem(listServicos.size(), taxas.getServicos().getDescricao(), "" + taxas.getServicos().getId()));
        for (int j = 0; j < listServicos.size(); j++) {
            if (taxas.getServicos().getId() == Integer.parseInt(listServicos.get(j).getDescription())) {
                idServicos = j;
                break;
            }
        }
    }

    public void delete() {
        delete(taxas);
    }

    public void delete(Taxas t) {
        taxas = t;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (dao.delete(t, true)) {
            logger.delete(
                    "ID: " + taxas.getId()
                    + " - Serviços: [" + taxas.getServicos().getId() + "] - " + taxas.getServicos().getDescricao()
                    + " - Valor: " + taxas.getValorString()
            );
            Messages.info("Sucesso", "Registro removido!");
            taxas = new Taxas();
            listServicos.clear();
            idServicos = 0;
            listTaxas.clear();
        } else {
            Messages.warn("Erro", "Erro ao remover registro!");
        }

    }

    public Taxas getTaxas() {
        return taxas;
    }

    public void setTaxas(Taxas taxas) {
        this.taxas = taxas;
    }

    public List<Taxas> getListTaxas() {
        if (listTaxas.isEmpty()) {
            TaxasDao taxasDao = new TaxasDao();
            listTaxas = taxasDao.pesquisaTodasTaxasPorCliente(SessaoCliente.get().getId());
        }
        return listTaxas;
    }

    public void setListTaxas(List<Taxas> listTaxas) {
        this.listTaxas = listTaxas;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            TaxasDao taxasDao = new TaxasDao();
            List<Servicos> list = taxasDao.findServicos(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listServicos.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

}
