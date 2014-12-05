package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Caixa;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
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
public class CaixaBean implements Serializable {

    private int idFilial;
    private List<SelectItem> listaFiliais;
    private Caixa caixa;
    private List<Caixa> listaCaixa;

    @PostConstruct
    public void init() {
        idFilial = 0;
        listaFiliais = new ArrayList<>();
        caixa = new Caixa();
        listaCaixa = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("caixaBean");
    }

    public void save() {
        Dao di = new Dao();
        caixa.setFilial((Filial) di.find(new Filial(), Integer.valueOf(listaFiliais.get(idFilial).getDescription())));
        if (!di.save(caixa, true)) {
            Messages.warn("Erro", "Não foi possível salvar Caixa!");
        } else {
            Logger novoLog = new Logger();
            novoLog.save(
                    "ID: " + caixa.getId()
                    + " - Filial: (" + caixa.getFilial().getId() + ") " + caixa.getFilial().getFilial().getPessoa().getNome()
                    + " - Caixa: " + caixa.getCaixa()
                    + " - Descrição: " + caixa.getDescricao()
            );
            caixa = new Caixa();
            listaCaixa.clear();
            Messages.info("Sucesso", "Caixa adicionado com Sucesso!");
        }
    }

    public void delete(Caixa c) {
        Dao di = new Dao();
        if (!di.delete(c, true)) {
            Messages.warn("Erro", "Não foi possível excluir Caixa!");
        } else {
            Logger novoLog = new Logger();
            novoLog.delete(
                    "ID: " + c.getId()
                    + " - Filial: (" + c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Caixa: " + c.getCaixa()
                    + " - Descrição: " + c.getDescricao()
            );
            listaCaixa.clear();
            Messages.info("Sucesso", "Caixa excluido com Sucesso!");
        }
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            FilialDao filialDao = new FilialDao();
            List<Filial> list = (List<Filial>) filialDao.pesquisaTodasCliente();
            for (int i = 0; i < list.size(); i++) {
                listaFiliais.add(new SelectItem(i, list.get(i).getFilial().getPessoa().getDocumento() + " - " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }

    public Caixa getCaixa() {
        return caixa;
    }

    public void setCaixa(Caixa caixa) {
        this.caixa = caixa;
    }

    public List<Caixa> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            Dao di = new Dao();
            listaCaixa = di.list(new Caixa(), true);
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<Caixa> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }
}
