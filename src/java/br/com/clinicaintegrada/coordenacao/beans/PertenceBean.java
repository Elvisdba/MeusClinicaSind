package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.PertenceEntrada;
import br.com.clinicaintegrada.coordenacao.PertenceGrupo;
import br.com.clinicaintegrada.coordenacao.PertenceSaida;
import br.com.clinicaintegrada.coordenacao.dao.PertenceDao;
import br.com.clinicaintegrada.coordenacao.list.ListPertence;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
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
public class PertenceBean implements Serializable {

    private PertenceEntrada pertenceEntrada;
    private PertenceEntrada pertenceEntradaEdit;
    private PertenceSaida pertenceSaida;
    private Equipe responsavelEntrada;
    private Equipe responsavelSaida;
    private Contrato contrato;
    private List<ListPertence> listPertences;
    private List<PertenceSaida> listPertenceSaida;
    private List<SelectItem> listSelectItem;
    private Integer[] index;
    private boolean lock;
    private boolean modal;
    private boolean habilitaPertenceGrupo;
    private boolean comSaldo;

    @PostConstruct
    public void init() {
        pertenceEntrada = new PertenceEntrada();
        pertenceEntradaEdit = new PertenceEntrada();
        pertenceSaida = new PertenceSaida();
        responsavelEntrada = new Equipe();
        responsavelSaida = new Equipe();
        contrato = new Contrato();
        lock = false;
        listPertences = new ArrayList<>();
        listPertenceSaida = new ArrayList<>();
        modal = false;
        modal = habilitaPertenceGrupo;
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        listSelectItem = new ArrayList<>();
        comSaldo = true;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("pertenceBean");
        Sessions.remove("equipeBean");
        Sessions.remove("equipePesquisa");
        Sessions.remove("tipoPesquisa");
    }

    public void clear() {
        Sessions.remove("pertenceBean");
    }

    public void clear(int tCase) {
        if (tCase == 1) {
            pertenceEntrada = new PertenceEntrada();
            index[0] = null;
        } else if (tCase == 2) {
            responsavelEntrada = new Equipe();
        } else if (tCase == 3) {
            contrato = new Contrato();
            listPertences.clear();
            pertenceEntrada = new PertenceEntrada();
            index[0] = null;
            listSelectItem.clear();
            modal = false;            
        } else if (tCase == 4) {
            modal = false;
            pertenceSaida = new PertenceSaida();
            responsavelSaida = new Equipe();
        } else if (tCase == 5) {
            if (!lock) {
                contrato = new Contrato();
            }
            responsavelEntrada = new Equipe();
            index[0] = null;
            listSelectItem.clear();
            listPertences.clear();
            habilitaPertenceGrupo = false;
            pertenceEntrada = new PertenceEntrada();
        } else {
            pertenceSaida = new PertenceSaida();
        }
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (habilitaPertenceGrupo) {
            if (!listSelectItem.isEmpty()) {
                if (index[0] != null) {
                    pertenceEntrada.setPertenceGrupo((PertenceGrupo) dao.find(new PertenceGrupo(), Integer.parseInt(listSelectItem.get(index[0]).getDescription())));
                } else {
                    pertenceEntrada.setPertenceGrupo(null);
                }
            } else {
                pertenceEntrada.setPertenceGrupo(null);
            }
        } else {
            pertenceEntrada.setPertenceGrupo(null);
        }
        if (pertenceEntrada.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        if (pertenceEntrada.getQuantidade() <= 0) {
            Messages.warn("Validação", "Quantidade deve ser maior que zero!");
            return;
        }
        if (contrato.getId() == -1) {
            Messages.warn("Validação", "Pesquisar um contrato válido!");
            return;
        }
        pertenceEntrada.setContrato(contrato);
        if (responsavelEntrada.getId() == -1) {
            Messages.warn("Validação", "Pesquisar um resposável!");
            return;
        }
        pertenceEntrada.setResponsavel(responsavelEntrada);
        if (pertenceEntrada.getId() == -1) {
            pertenceEntrada.setContrato(pertenceEntrada.getContrato());
            PertenceDao pertenceDao = new PertenceDao();
            if (pertenceDao.exists(pertenceEntrada.getContrato().getId(), pertenceEntrada.getDescricao(), pertenceEntrada.getEntradaString())) {
                Messages.warn("Validação", "Pertence já cadastrado!");
                return;
            }
            if (dao.save(pertenceEntrada, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " Entrada - ID: " + pertenceEntrada.getId() + "]"
                        + " - Contrato: [" + pertenceEntrada.getContrato()
                        + " - Responsável: [" + pertenceEntrada.getResponsavel().getId() + "]" + pertenceEntrada.getResponsavel().getPessoa().getNome()
                        + " - Descrição: " + pertenceEntrada.getDescricao()
                        + " - Entrada: " + pertenceEntrada.getEntradaString()
                        + " - Quantidade: " + pertenceEntrada.getQuantidade()
                );
                listPertences.clear();
                if (lock) {
                    clear(1);
                }
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            PertenceEntrada pe = (PertenceEntrada) dao.find(pertenceEntrada);
            String beforeUpdate
                    = " Entrada - ID: " + pe.getId() + "]"
                    + " - Contrato: [" + pe.getContrato()
                    + " - Responsável: [" + pe.getResponsavel().getId() + "]" + pe.getResponsavel().getPessoa().getNome()
                    + " - Descrição: " + pe.getDescricao()
                    + " - Entrada: " + pe.getEntrada()
                    + " - Quantidade: " + pe.getQuantidade();
            if (dao.update(pertenceEntrada, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " Entrada- ID: " + pertenceEntrada.getId() + "]"
                        + " - Contrato: [" + pertenceEntrada.getContrato()
                        + " - Responsável: [" + pertenceEntrada.getResponsavel().getId() + "]" + pertenceEntrada.getResponsavel().getPessoa().getNome()
                        + " - Descrição: " + pertenceEntrada.getDescricao()
                        + " - Entrada: " + pertenceEntrada.getEntradaString()
                        + " - Quantidade: " + pertenceEntrada.getQuantidade()
                );
                listPertences.clear();
                if (lock) {
                    clear(1);
                }
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void add() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (pertenceSaida.getId() == -1) {
            PertenceDao pertenceDao = new PertenceDao();
            int saidas = pertenceDao.countPertenceSaida(pertenceEntradaEdit.getId());
            int total = saidas + pertenceSaida.getQuantidade();
            if (total > pertenceEntradaEdit.getQuantidade()) {
                Messages.warn("Validação", "Quantidade excedida!");
                pertenceSaida.setQuantidade(pertenceEntradaEdit.getQuantidade() - saidas);
            }
            pertenceSaida.setPertenceEntrada(pertenceEntradaEdit);
            if (pertenceSaida.getQuantidade() == 0) {
                Messages.warn("Validação", "Não existe quantidade disponível!");
                return;
            }
            pertenceSaida.setResponsavel(pertenceEntradaEdit.getResponsavel());
            if (dao.save(pertenceSaida, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " Saída - ID: " + pertenceSaida.getId() + "]"
                        + " - Contrato: [" + pertenceSaida.getPertenceEntrada().getId() + "]"
                        + " - Responsável: [" + pertenceSaida.getResponsavel().getId() + "]" + pertenceSaida.getResponsavel().getPessoa().getNome()
                        + " - Descrição: " + pertenceSaida.getQuantidade()
                        + " - Entrada: " + pertenceSaida.getSaida()
                        + " - Quantidade: " + pertenceSaida.getQuantidade()
                );
                listPertenceSaida.clear();
                listPertences.clear();
                pertenceSaida = new PertenceSaida();
                modal = false;
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        }
    }

    public void delete(PertenceSaida ps) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (ps.getId() != -1) {
            if (dao.delete(ps, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " Saída - ID: " + ps.getId() + "]"
                        + " - Contrato: [" + ps.getPertenceEntrada().getId() + "]"
                        + " - Responsável: [" + ps.getResponsavel().getId() + "]" + ps.getResponsavel().getPessoa().getNome()
                        + " - Descrição: " + ps.getQuantidade()
                        + " - Entrada: " + ps.getSaida()
                        + " - Quantidade: " + ps.getQuantidade()
                );
                listPertenceSaida.clear();
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    public void delete(PertenceEntrada pe) {
        pertenceEntrada = pe;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (pe.getId() != -1) {
            if (dao.delete(pe, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " Entrada - ID: " + pertenceEntrada.getId() + "]"
                        + " - Contrato: [" + pertenceEntrada.getContrato()
                        + " - Responsável: [" + pertenceEntrada.getResponsavel().getId() + "]" + pertenceEntrada.getResponsavel().getPessoa().getNome()
                        + " - Descrição: " + pertenceEntrada.getDescricao()
                        + " - Entrada: " + pertenceEntrada.getEntradaString()
                        + " - Quantidade: " + pertenceEntrada.getQuantidade()
                );
                listPertences.clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public String edit(ListPertence lp) {
        pertenceEntrada = lp.getPertenceEntrada();
        contrato = pertenceEntrada.getContrato();
        responsavelEntrada = pertenceEntrada.getResponsavel();
        if (pertenceEntrada.getPertenceGrupo() != null) {
            habilitaPertenceGrupo = true;
            for (int i = 0; i < listSelectItem.size(); i++) {
                if (pertenceEntrada.getPertenceGrupo().getId() == Integer.parseInt(listSelectItem.get(i).getDescription())) {
                    index[0] = i;
                    break;
                }
            }
        } else {
            habilitaPertenceGrupo = false;
            index = null;
        }
        lock = true;
        return null;
    }

    public void showPertenceSaida(PertenceEntrada pe) {
        modal = true;
        pertenceEntradaEdit = pe;
        listPertenceSaida.clear();
    }

    public Equipe getResponsavelEntrada() {
        if (Sessions.exists("equipePesquisa")) {
            if (Sessions.exists("tipoPesquisa")) {
                if (Sessions.getObject("tipoPesquisa").equals("entrada")) {
                    responsavelEntrada = (Equipe) Sessions.getObject("equipePesquisa", true);
                }
            }
        }
        return responsavelEntrada;
    }

    public void setResponsavelEntrada(Equipe responsavelEntrada) {
        this.responsavelEntrada = responsavelEntrada;
    }

    public Equipe getResponsavelSaida() {
        if (Sessions.exists("equipePesquisa")) {
            if (Sessions.exists("tipoPesquisa")) {
                if (Sessions.getObject("tipoPesquisa").equals("saida")) {
                    responsavelSaida = (Equipe) Sessions.getObject("equipePesquisa", true);
                }
            }
        }
        return responsavelSaida;
    }

    public void setResponsavelSaida(Equipe responsavelSaida) {
        this.responsavelSaida = responsavelSaida;
    }

    public Contrato getContrato() {
        if (Sessions.exists("contratoPesquisa")) {
            getListPertences();
            contrato = (Contrato) Sessions.getObject("contratoPesquisa", true);
        }
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public PertenceSaida getPertenceSaida() {
        return pertenceSaida;
    }

    public void setPertenceSaida(PertenceSaida pertenceSaida) {
        this.pertenceSaida = pertenceSaida;
    }

    public PertenceEntrada getPertenceEntrada() {
        return pertenceEntrada;
    }

    public void setPertenceEntrada(PertenceEntrada pertenceEntrada) {
        this.pertenceEntrada = pertenceEntrada;
    }

    public void listenerLock(String tCase, int id) {
        Sessions.put("lock", true);
        Sessions.put("linkClicado", true);
        if (tCase.equals("contrato")) {
            Dao dao = new Dao();
            Contrato c = (Contrato) dao.find(new Contrato(), id);
            Sessions.put("contratoPesquisa", c);
        }
    }

    public boolean isLock() {
        if (Sessions.exists("lock")) {
            lock = Sessions.getBoolean("lock", true);
        }
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public List<ListPertence> getListPertences() {
        if (listPertences.isEmpty()) {
            if (contrato.getId() != -1) {
                PertenceDao pertenceDao = new PertenceDao();
                if (index[1] != null) {
                    pertenceDao.setPertenceGrupo(Integer.parseInt(listSelectItem.get(index[1]).getDescription()));
                } else {
                    pertenceDao.setPertenceGrupo(null);
                }
                List<PertenceEntrada> list = pertenceDao.findAllByContrato(contrato.getId());
                int saida = 0;
                int saldo = 0;
                for (int i = 0; i < list.size(); i++) {
                    saida = pertenceDao.countPertenceSaida(list.get(i).getId());
                    saldo = list.get(i).getQuantidade() - saida;
                    if (saldo < 0) {
                        saldo = 0;
                    }
                    if (comSaldo) {
                        if (saldo > 0) {
                            listPertences.add(new ListPertence(list.get(i), new PertenceSaida(), saida, saldo));
                        }
                    } else {
                        listPertences.add(new ListPertence(list.get(i), new PertenceSaida(), saida, saldo));
                    }
                }
            }
        }
        return listPertences;
    }

    public void setListPertences(List<ListPertence> listPertences) {
        this.listPertences = listPertences;
    }

    public PertenceEntrada getPertenceEntradaEdit() {
        return pertenceEntradaEdit;
    }

    public void setPertenceEntradaEdit(PertenceEntrada pertenceEntradaEdit) {
        this.pertenceEntradaEdit = pertenceEntradaEdit;
    }

    public List<PertenceSaida> getListPertenceSaida() {
        if (pertenceEntradaEdit.getId() != -1) {
            PertenceDao pertenceDao = new PertenceDao();
            listPertenceSaida = pertenceDao.findSaidaAllByEntrada(pertenceEntradaEdit.getId());
        }
        return listPertenceSaida;
    }

    public void setListPertenceSaida(List<PertenceSaida> listPertenceSaida) {
        this.listPertenceSaida = listPertenceSaida;
    }

    public void listernetResponsavel(String tCase) {
        switch (tCase) {
            case "entrada":
                Sessions.put("tipoPesquisa", tCase);
                break;
            case "saida":
                Sessions.put("tipoPesquisa", tCase);
                break;
        }
    }

    public boolean isModal() {
        if (Sessions.exists("tipoPesquisa")) {
            if (Sessions.getString("tipoPesquisa", true).equals("saida")) {
                pertenceSaida.setResponsavel(responsavelSaida);
                modal = true;
            }
        }
        return modal;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public boolean isHabilitaPertenceGrupo() {
        return habilitaPertenceGrupo;
    }

    public void setHabilitaPertenceGrupo(boolean habilitaPertenceGrupo) {
        this.habilitaPertenceGrupo = habilitaPertenceGrupo;
    }

    public List<SelectItem> getListSelectItem() {
        if (listSelectItem.isEmpty()) {
            Dao dao = new Dao();
            List<PertenceGrupo> list = (List<PertenceGrupo>) dao.listByCliente(new PertenceGrupo());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem> listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public boolean isComSaldo() {
        return comSaldo;
    }

    public void setComSaldo(boolean comSaldo) {
        this.comSaldo = comSaldo;
    }

}
