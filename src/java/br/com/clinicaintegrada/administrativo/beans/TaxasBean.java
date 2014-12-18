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
    private List<SelectItem> listSelectItem;
    private Integer index;

    @PostConstruct
    public void init() {
        taxas = new Taxas();
        listTaxas = new ArrayList<>();
        listSelectItem = new ArrayList<>();
        index = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("taxasBean");
    }

    public void clear() {
        Sessions.remove("taxasBean");
    }

    public void save() {
        if (listSelectItem.isEmpty()) {
            Messages.warn("Validação", "Cadastrar serviços!");
            return;
        }
        Dao dao = new Dao();
        taxas.setServicos((Servicos) dao.find(new Servicos(), Integer.parseInt(listSelectItem.get(index).getDescription())));
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
                listSelectItem.clear();
                taxas = new Taxas();
                index = 0;
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
                listSelectItem.clear();
                taxas = new Taxas();
                index = 0;
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }

    }

    public void edit(Taxas t) {
        listSelectItem.clear();
        index = 0;
        getListSelectItem();
        taxas = new Taxas();
        taxas = t;
        listSelectItem.add(new SelectItem(listSelectItem.size(), taxas.getServicos().getDescricao(), "" + taxas.getServicos().getId()));
        for (int j = 0; j < listSelectItem.size(); j++) {
            if (taxas.getServicos().getId() == Integer.parseInt(listSelectItem.get(j).getDescription())) {
                index = j;
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
            listSelectItem.clear();
            index = 0;
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

    /**
     * Serviços
     * @return 
     */
    public List<SelectItem> getListSelectItem() {
        if (listSelectItem.isEmpty()) {
            TaxasDao taxasDao = new TaxasDao();
            List<Servicos> list = taxasDao.findServicos(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem> listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

}
