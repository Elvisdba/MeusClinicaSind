package br.com.clinicaintegrada.fichamedica.beans;

import br.com.clinicaintegrada.fichamedica.dao.AvaliacaoDao;
import br.com.clinicaintegrada.fichamedica.Avaliacao;
import br.com.clinicaintegrada.fichamedica.GrupoAvaliacao;
import br.com.clinicaintegrada.fichamedica.TiposAvaliacao;
import br.com.clinicaintegrada.fichamedica.dao.TiposAvaliacaoDao;
import br.com.clinicaintegrada.logSistema.Logger;
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
public class AvaliacaoBean implements Serializable {

    private Avaliacao avaliacao;
    private List<Avaliacao> listAvaliacao;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;

    @PostConstruct
    public void init() {
        avaliacao = new Avaliacao();
        listAvaliacao = new ArrayList<>();
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("avaliacaoBean");
    }

    public void clear() {
        Sessions.remove("avaliacaoBean");
    }

    public void clear(int tCase) {
        // TUDO
        if (tCase == 0) {
            Sessions.remove("avaliacaoBean");
        }
        // SOMENTE COMBO DOS TIPOS DE AVALIAÇÃO [1]
        if (tCase == 1) {
            listSelectItem[1].clear();
        }
        // NOVO CADASTRO
        if (tCase == 2) {
            avaliacao = new Avaliacao();
        }
        // COMBO DOS TIPOS DE AVALIAÇÃO [1] E LISTA DAS AVALIAÇÕES
        if (tCase == 2 || tCase == 3) {
            listAvaliacao.clear();
            listSelectItem[1].clear();
        }
    }

    // NÃO ATUALIZA (CAMPOS FIXOS) - SOMENTE NO POSTGRESQL
    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (listSelectItem[0].isEmpty() || listSelectItem[1].isEmpty()) {
            Messages.warn("Sistema", "Cadastrar grupos e tipos de avaliação! Consultar administrador do sistema.");
            return;
        }
        if (index[0] == null) {
            Messages.warn("Validação", "Selecionar grupo de avaliação!");
            return;
        }
        if (index[1] == null) {
            Messages.warn("Validação", "Selecionar tipo de avaliação!");
            return;
        }
        AvaliacaoDao avaliacaoDao = new AvaliacaoDao();
        avaliacao.setGrupoAvaliacao((GrupoAvaliacao) dao.find(new GrupoAvaliacao(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription())));
        avaliacao.setTiposAvaliacao((TiposAvaliacao) dao.find(new TiposAvaliacao(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription())));
        if (avaliacaoDao.exists(avaliacao.getGrupoAvaliacao().getId(), avaliacao.getTiposAvaliacao().getId())) {
            Messages.warn("Validação", "O avaliacao para este paciente esta agendado para esta data!");
            return;
        }
        if (avaliacao.getId() == -1) {
            if (dao.save(avaliacao, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: [" + avaliacao.getId() + "]"
                        + " - Grupo Avaliação : [" + avaliacao.getGrupoAvaliacao().getId() + "] - " + avaliacao.getGrupoAvaliacao().getDescricao()
                        + " - Tipo Avaliação : [" + avaliacao.getTiposAvaliacao().getId() + "] - " + avaliacao.getTiposAvaliacao().getDescricao()
                        + " - Histórico: " + avaliacao.getHistorico()
                );
                clear(2);
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            Avaliacao a = (Avaliacao) dao.find(avaliacao);
            String beforeUpdate
                    = "ID: [" + avaliacao.getId() + "]"
                    + " - Grupo Avaliação : [" + a.getGrupoAvaliacao().getId() + "] - " + a.getGrupoAvaliacao().getDescricao()
                    + " - Tipo Avaliação : [" + a.getTiposAvaliacao().getId() + "] - " + a.getTiposAvaliacao().getDescricao()
                    + " - Histórico: " + a.getHistorico();
            if (dao.update(avaliacao, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: [" + avaliacao.getId() + "]"
                        + " - Grupo Avaliação : [" + avaliacao.getGrupoAvaliacao().getId() + "] - " + avaliacao.getGrupoAvaliacao().getDescricao()
                        + " - Tipo Avaliação : [" + avaliacao.getTiposAvaliacao().getId() + "] - " + avaliacao.getTiposAvaliacao().getDescricao()
                        + " - Histórico: " + avaliacao.getHistorico()
                );
                clear(2);
            } else {
                Messages.warn("Erro", "Ao atualizar transferência!");
            }
        }
    }

    public void delete() {
        delete(avaliacao);
        clear(0);
    }

    // NÃO DELETA (CAMPOS FIXOS) - SOMENTE NO POSTGRESQL
    public void delete(Avaliacao a) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (a.getId() != -1) {
            if (dao.delete(a, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        "ID: [" + a.getId() + "]"
                        + " - Grupo Avaliação : [" + a.getGrupoAvaliacao().getId() + "] - " + a.getGrupoAvaliacao().getDescricao()
                        + " - Tipo Avaliação : [" + a.getTiposAvaliacao().getId() + "] - " + a.getTiposAvaliacao().getDescricao()
                        + " - Histórico: " + a.getHistorico()
                );
                clear(3);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    // ATUALIZA SOMENTE O HISTÓRICO
    public void update(Avaliacao o) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (o.getId() != -1) {
            if (dao.update(o, true)) {
                logger.update("",
                        "ID: [" + o.getId() + "]"
                        + " - Grupo Avaliação : [" + o.getGrupoAvaliacao().getId() + "] - " + o.getGrupoAvaliacao().getDescricao()
                        + " - Tipo Avaliação : [" + o.getTiposAvaliacao().getId() + "] - " + o.getTiposAvaliacao().getDescricao()
                        + " - Histórico: " + o.getHistorico()
                );
            }
            listAvaliacao.clear();
        }
    }

    // NÃO EDITA (CAMPOS FIXOS) - SOMENTE NO POSTGRESQL
    public String edit(Object o) {
        Dao dao = new Dao();
        avaliacao = (Avaliacao) dao.rebind(o);
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (avaliacao.getGrupoAvaliacao().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        for (int i = 0; i < listSelectItem[1].size(); i++) {
            if (avaliacao.getTiposAvaliacao().getId() == Integer.parseInt(listSelectItem[1].get(i).getDescription())) {
                index[1] = i;
                break;
            }
        }
        return null;
    }

    /**
     * 0 - Grupo de Avaliação - 1 Tipos de Avaliação
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<GrupoAvaliacao> list = dao.list(new GrupoAvaliacao(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listSelectItem[0].isEmpty()) {
            if (listSelectItem[1].isEmpty()) {
                if (index[0] != null) {
                    TiposAvaliacaoDao tiposAvaliacaoDao = new TiposAvaliacaoDao();
                    List<TiposAvaliacao> list = tiposAvaliacaoDao.findAllByGrupoForAvaliacao(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            index[1] = i;
                        }
                        listSelectItem[1].add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
                    }
                }
            }
        }
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    /**
     * 0 - Grupo de Avaliação - 1 Tipos de Avaliação (NOT IN --> Somente as
     * disponíveis)
     *
     * @return
     */
    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    /**
     * Traz somente as avaliações conforme grupo selecionado.
     *
     * @return
     */
    public List<Avaliacao> getListAvaliacao() {
        if (listAvaliacao.isEmpty()) {
            if (index[0] != null) {
                AvaliacaoDao avaliacaoDao = new AvaliacaoDao();
                listAvaliacao = avaliacaoDao.findAllByGrupo(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
            }
        }
        return listAvaliacao;
    }

    public void setListAvaliacao(List<Avaliacao> listAvaliacao) {
        this.listAvaliacao = listAvaliacao;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }
}
