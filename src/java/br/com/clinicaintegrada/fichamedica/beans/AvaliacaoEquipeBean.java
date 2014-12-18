package br.com.clinicaintegrada.fichamedica.beans;

import br.com.clinicaintegrada.fichamedica.Avaliacao;
import br.com.clinicaintegrada.fichamedica.AvaliacaoEquipe;
import br.com.clinicaintegrada.coordenacao.ConfiguracaoCoordenacao;
import br.com.clinicaintegrada.fichamedica.dao.AvaliacaoEquipeDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import br.com.clinicaintegrada.pessoa.dao.FuncaoEquipeDao;
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
public class AvaliacaoEquipeBean implements Serializable {

    private AvaliacaoEquipe avaliacaoEquipe;
    private List<AvaliacaoEquipe> listAvaliacaoEquipe;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;

    @PostConstruct
    public void init() {
        avaliacaoEquipe = new AvaliacaoEquipe();
        listAvaliacaoEquipe = new ArrayList<>();
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        listSelectItem = new ArrayList[2];
        listSelectItem[0] = new ArrayList<>();
        listSelectItem[1] = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("avaliacaoEquipeBean");
    }

    public void clear() {
        Sessions.remove("avaliacaoEquipeBean");
    }

    public void clear(int tCase) {
        // TUDO
        if (tCase == 0) {
            Sessions.remove("avaliacaoEquipeBean");
        }
        // SOMENTE COMBO DOS TIPOS [1]
        if (tCase == 1) {
            listSelectItem[1].clear();
        }
        // NOVO
        if (tCase == 2) {
            avaliacaoEquipe = new AvaliacaoEquipe();
        }
        // QUANDO SELECIONAR UMA AVALIAÇÃO
        if (tCase == 2 || tCase == 3) {
            listAvaliacaoEquipe.clear();
            listSelectItem[1].clear();
        }
    }

    // NÃO ATUALIZA
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
        AvaliacaoEquipeDao avaliacaoEquipeDao = new AvaliacaoEquipeDao();
        avaliacaoEquipe.setAvaliacao((Avaliacao) dao.find(new Avaliacao(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription())));
        avaliacaoEquipe.setFuncaoEquipe((FuncaoEquipe) dao.find(new FuncaoEquipe(), Integer.parseInt(listSelectItem[1].get(index[1]).getDescription())));
        if (avaliacaoEquipeDao.exists(avaliacaoEquipe.getAvaliacao().getId(), avaliacaoEquipe.getFuncaoEquipe().getId())) {
            Messages.warn("Validação", "O avaliacaoEquipe para este paciente esta agendado para esta data!");
            return;
        }
        if (avaliacaoEquipe.getId() == -1) {
            if (dao.save(avaliacaoEquipe, true)) {
                Messages.info("Sucesso", "Registro adicionado.");
                logger.save(
                        "ID: [" + avaliacaoEquipe.getId() + "]"
                        + " - Avaliação : [" + avaliacaoEquipe.getAvaliacao().getId() + "] - "
                        + " - Função Equipe: [" + avaliacaoEquipe.getFuncaoEquipe().getId() + "] "
                );
                clear(2);
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            AvaliacaoEquipe ae = (AvaliacaoEquipe) dao.find(avaliacaoEquipe);
            String beforeUpdate
                    = "ID: [" + ae.getId() + "]"
                    + " - Avaliação : [" + ae.getAvaliacao().getId() + "] - "
                    + " - Função Equipe: [" + ae.getFuncaoEquipe().getId() + "] ";
            if (dao.update(avaliacaoEquipe, true)) {
                Messages.info("Sucesso", "Registro atualizado.");
                logger.update(beforeUpdate,
                        "ID: [" + avaliacaoEquipe.getId() + "]"
                        + " - Avaliação : [" + avaliacaoEquipe.getAvaliacao().getId() + "] - "
                        + " - Função Equipe: [" + avaliacaoEquipe.getFuncaoEquipe().getId() + "] "
                );
                clear(2);
            } else {
                Messages.warn("Erro", "Ao atualizar transferência!");
            }
        }
    }

    public void delete() {
        delete(avaliacaoEquipe);
        clear(0);
    }

    public void delete(AvaliacaoEquipe ae) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (ae.getId() != -1) {
            if (dao.delete(ae, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        "ID: [" + ae.getId() + "]"
                        + " - Avaliação : [" + ae.getAvaliacao().getId() + "] - "
                        + " - Função Equipe: [" + ae.getFuncaoEquipe().getId() + "] "
                );
                clear(3);
            } else {
                Messages.warn("Erro", "Ao remover registro!");
            }
        }
    }

    // ATUALIZA O HISTÓRICO (SOMENTE) - DESABILITADO
    public void update(AvaliacaoEquipe ae) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (ae.getId() != -1) {
            if (dao.update(ae, true)) {
                logger.update("",
                        "ID: [" + ae.getId() + "]"
                        + " - Avaliação : [" + ae.getAvaliacao().getId() + "] - "
                        + " - Função Equipe: [" + ae.getFuncaoEquipe().getId() + "] "
                );
                clear(3);
            }
        }
    }

    // CAMPO DESABILITADO - SOMENTE NO POSTGRESSQL 
    public String edit(Object o) {
        Dao dao = new Dao();
        avaliacaoEquipe = (AvaliacaoEquipe) dao.rebind(o);
        for (int i = 0; i < listSelectItem[0].size(); i++) {
            if (avaliacaoEquipe.getAvaliacao().getId() == Integer.parseInt(listSelectItem[0].get(i).getDescription())) {
                index[0] = i;
                break;
            }
        }
        for (int i = 0; i < listSelectItem[1].size(); i++) {
            if (avaliacaoEquipe.getFuncaoEquipe().getId() == Integer.parseInt(listSelectItem[1].get(i).getDescription())) {
                index[1] = i;
                break;
            }
        }
        return null;
    }

    /**
     * 0 - Avaliação - 1 Função Equipe (NOT IN --> Somente as disponíveis)
     *
     * @return
     */
    public List<SelectItem>[] getListSelectItem() {
        if (listSelectItem[0].isEmpty()) {
            Dao dao = new Dao();
            List<Avaliacao> list = dao.list(new Avaliacao());
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    index[0] = i;
                }
                listSelectItem[0].add(new SelectItem(i, list.get(i).getGrupoAvaliacao().getDescricao() + " - " + list.get(i).getTiposAvaliacao().getDescricao(), "" + list.get(i).getId()));
            }
        }
        if (!listSelectItem[0].isEmpty()) {
            if (listSelectItem[1].isEmpty()) {
                String l1;
                if (index[0] != null) {
                    FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
                    List<FuncaoEquipe> list = funcaoEquipeDao.findAllByClienteAndInAvaliacao(SessaoCliente.get().getId(), Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
                    for (int i = 0; i < list.size(); i++) {
                        if (i == 0) {
                            index[1] = i;
                        }
                        try {
                            l1 = list.get(i).getTipoAtendimento().getDescricao() + " - " + list.get(i).getProfissao().getProfissao();
                        } catch (Exception e) {
                            l1 = list.get(i).getProfissao().getProfissao();
                        }
                        listSelectItem[1].add(new SelectItem(i, l1, "" + list.get(i).getId()));
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
     * 0 - Avaliação - 1 Função Equipe
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
     * Traz listagem conforme avaliação selecionada.
     *
     * @return
     */
    public List<AvaliacaoEquipe> getListAvaliacaoEquipe() {
        if (listAvaliacaoEquipe.isEmpty()) {
            if (index[0] != null) {
                AvaliacaoEquipeDao avaliacaoEquipeDao = new AvaliacaoEquipeDao();
                listAvaliacaoEquipe = avaliacaoEquipeDao.findAllByAvaliacao(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
            }
        }
        return listAvaliacaoEquipe;
    }

    public void setListAvaliacaoEquipe(List<AvaliacaoEquipe> listAvaliacaoEquipe) {
        this.listAvaliacaoEquipe = listAvaliacaoEquipe;
    }

    public AvaliacaoEquipe getAvaliacaoEquipe() {
        return avaliacaoEquipe;
    }

    public void setAvaliacaoEquipe(AvaliacaoEquipe avaliacaoEquipe) {
        this.avaliacaoEquipe = avaliacaoEquipe;
    }
}
