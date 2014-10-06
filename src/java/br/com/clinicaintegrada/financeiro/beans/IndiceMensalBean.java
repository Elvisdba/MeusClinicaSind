package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Indice;
import br.com.clinicaintegrada.financeiro.IndiceMensal;
import br.com.clinicaintegrada.financeiro.dao.IndiceMensalDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.sistema.Mes;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Sessions;
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
public class IndiceMensalBean implements Serializable {

    private IndiceMensal indiceMensal;
    private List<IndiceMensal> listaIndiceMensal;
    private List<SelectItem> listIndices;
    private List<SelectItem> listAnos;
    private List<SelectItem> listMeses;
    private int indiceMes;
    private int ano;
    private String valor;
    private int idIndice;
    private boolean limpar;

    @PostConstruct
    public void init() {
        indiceMensal = new IndiceMensal();
        listaIndiceMensal = new ArrayList<>();
        listIndices = new ArrayList<>();
        listAnos = new ArrayList<>();
        listMeses = new ArrayList<>();
        indiceMes = 0;
        ano = Integer.parseInt(DataHoje.livre(new Date(), "YY"));
        valor = "";
        idIndice = 0;
        limpar = false;

    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("indiceMensalBean");
    }

    public String clear() {
        indiceMensal = new IndiceMensal();
        limpar = false;
        return "indiceMensal";
    }

    public List<SelectItem> getListIndices() {
        if (listIndices.isEmpty()) {
            Dao dao = new Dao();
            List<Indice> list = (List<Indice>) dao.list(new Indice(), true);
            for (int i = 0; i < list.size(); i++) {
                listIndices.add(new SelectItem(i, ((Indice) list.get(i)).getDescricao(), Integer.toString(((Indice) list.get(i)).getId())));
            }
        }
        return listIndices;
    }

    public void setListIndices(List<SelectItem> listIndices) {
        this.listIndices = listIndices;
    }

    public List<SelectItem> getListMeses() {
        if (listMeses.isEmpty()) {
            Dao dao = new Dao();
            List<Mes> list = (List<Mes>) dao.list(new Mes());
            for (int i = 0; i < list.size(); i++) {
                listMeses.add(new SelectItem(i, ((Mes) list.get(i)).getDescricao(), Integer.toString(((Mes) list.get(i)).getId())));
            }
        }
        return listMeses;
    }

    public void setListMeses(List<SelectItem> listMeses) {
        this.listMeses = listMeses;
    }

    public void save() {
        if (valor.isEmpty()) {
            Messages.warn("Validação", "Digite um valor válido!");
            return;
        }

        IndiceMensalDao indiceMensalDao = new IndiceMensalDao();
        Logger logger = new Logger();
        Dao dao = new Dao();
        indiceMensal.setAno(Integer.valueOf(getListAnos().get(ano).getDescription()));
        indiceMensal.setValor(Moeda.substituiVirgulaFloat(valor));
        indiceMensal.setIndice((Indice) dao.find(new Indice(), Integer.parseInt(getListIndices().get(idIndice).getDescription())));
        indiceMensal.setMes((Mes) dao.find(new Mes(), Integer.parseInt(getListMeses().get(indiceMes).getDescription())));
        if (indiceMensalDao.existIndiceMensal(indiceMensal.getIndice().getId(), indiceMensal.getAno(), indiceMensal.getMes().getId()).isEmpty()) {
            if (dao.save(indiceMensal, true)) {
                logger.save(
                        "ID: " + indiceMensal.getId()
                        + " - Índice: (" + indiceMensal.getIndice().getId() + ") " + indiceMensal.getIndice().getDescricao()
                        + " - Mês: " + indiceMensal.getMes()
                        + " - Ano: " + indiceMensal.getAno()
                        + " - Valor: " + indiceMensal.getValor()
                );
                Messages.info("Sucesso", "Registro inserido");
            } else {
                Messages.info("Erro", "Ao inserir registro!");
            }
        } else {
            Messages.warn("Erro", "Índice mensal já cadastrado!");
        }
        indiceMensal = new IndiceMensal();
        listaIndiceMensal.clear();
    }

    public void delete(IndiceMensal im) {
        indiceMensal = im;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (dao.delete(indiceMensal, true)) {
            logger.delete(
                    "ID: " + indiceMensal.getId()
                    + " - Índice: (" + indiceMensal.getIndice().getId() + ") " + indiceMensal.getIndice().getDescricao()
                    + " - Mês: " + indiceMensal.getMes()
                    + " - Ano: " + indiceMensal.getAno()
                    + " - Valor: " + indiceMensal.getValor()
            );
            Messages.info("Sucesso", "Registro removido");
            indiceMensal = new IndiceMensal();
            listaIndiceMensal.clear();
            clear(1);
        } else {
            Messages.warn("Erro", "Ao remover registro!");
        }
    }

    public IndiceMensal getIndiceMensal() {
        return indiceMensal;
    }

    public void setIndiceMensal(IndiceMensal indiceMensal) {
        this.indiceMensal = indiceMensal;
    }

    public List<IndiceMensal> getListaIndiceMensal() {
        if (!getListIndices().isEmpty()) {
            IndiceMensalDao indiceMensalDao = new IndiceMensalDao();
            listaIndiceMensal = indiceMensalDao.findIndiceMensalByIndice(Integer.valueOf(getListIndices().get(idIndice).getDescription()));
        }
        return listaIndiceMensal;
    }

    public void clear(int tcase) {
        if (tcase == 1) {
            clear();
        }
        listaIndiceMensal.clear();
        indiceMensal = new IndiceMensal();
        valor = "";
        ano = 0;
        indiceMes = 0;
    }

    public void setListaIndiceMensal(List<IndiceMensal> listaIndiceMensal) {
        this.listaIndiceMensal = listaIndiceMensal;
    }

    public int getIndiceMes() {
        return indiceMes;
    }

    public void setIndiceMes(int indiceMes) {
        this.indiceMes = indiceMes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }

    public List<SelectItem> getListAnos() {
        if (listAnos.isEmpty()) {
            int an = Integer.valueOf(DataHoje.livre(new Date(), "YY"));
            for (int o = 0; o < 6; o++) {
                listAnos.add(new SelectItem(o, String.valueOf(an), String.valueOf(an)));
                an = an - 1;
            }
        }
        return listAnos;
    }

    public void setListAnos(List<SelectItem> listAnos) {
        this.listAnos = listAnos;
    }
}
