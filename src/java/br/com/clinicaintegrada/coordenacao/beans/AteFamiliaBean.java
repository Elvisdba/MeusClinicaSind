package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.AteFamilia;
import br.com.clinicaintegrada.coordenacao.dao.AteFamiliaDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@SessionScoped
public class AteFamiliaBean implements Serializable {

    private AteFamilia ateFamilia;
    private List<AteFamilia> listAteFamilia;
    @ManagedProperty("#{selectedAteFamilia}")
    private AteFamilia selectedAteFamilia;

    @PostConstruct
    public void init() {
        ateFamilia = new AteFamilia();
        listAteFamilia = null;
        loadAteFamilia();
    }

    @PreDestroy
    public void destroy() {
        clear();
        Sessions.remove("equipePesquisa");
    }

    public void clear() {
        Sessions.remove("ateFamiliaBean");
    }

    public void loadAteFamilia() {
        listAteFamilia = null;
        listAteFamilia = new AteFamiliaDao().findByCliente(SessaoCliente.get().getId());
    }

    /**
     *
     * @param tcase
     */
    public void clear(int tcase) {
        switch (tcase) {
        }
    }

    public void save() {
        try {
            if (ateFamilia.getEquipe() == null) {
                Messages.warn("Validação", "Pesquisar um membro da equipe!");
            }
            if (ateFamilia.getId() == null) {
                try {
                    ateFamilia.setOrdem(new AteFamiliaDao().maxOrdemByCliente(SessaoCliente.get().getId()).getOrdem());
                } catch (Exception e) {
                    ateFamilia.setOrdem(null);
                }
                if (ateFamilia.getOrdem() == null) {
                    ateFamilia.setOrdem(1);
                } else {
                    ateFamilia.setOrdem(ateFamilia.getOrdem() + 1);
                }
                if (!new Dao().save(ateFamilia, true)) {
                    Messages.info("Sistema", "Possível causa, membro da equipe já existe!");
                    Messages.warn("Erro", "Ao inserir registro!");
                    return;
                }
                loadAteFamilia();
                int y = 1;
                for (int i = 0; i < listAteFamilia.size(); i++) {
                    listAteFamilia.get(i).setOrdem(y);
                    new Dao().update(listAteFamilia.get(i), true);
                    y++;
                }
                Logger logger = new Logger();
                logger.delete(
                        "ID: " + ateFamilia.getId()
                        + " - Equipe: {" + ateFamilia.getEquipe().getId() + " - Nome " + ateFamilia.getEquipe().getPessoa().getNome() + "}"
                );
                Messages.info("Sucesso", "Registro excluído");
                ateFamilia = new AteFamilia();

            }
        } catch (Exception e) {
            Messages.warn("Sistema", "Possível causa, membro da equipe já existe!");
            Messages.warn("Sistema", e.getMessage());
        }

    }

    public String edit(Object o) {
        ateFamilia = new AteFamilia();
        Dao dao = new Dao();
        ateFamilia = (AteFamilia) dao.rebind(o);
        return null;
    }

    public void delete(Object o) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        ateFamilia = (AteFamilia) o;
        if (!dao.delete(ateFamilia, true)) {
            Messages.warn("Erro", "Ao remover registro!");
            return;
        }
        loadAteFamilia();
        logger.delete(
                "ID: " + ateFamilia.getId()
                + " - Equipe: {" + ateFamilia.getEquipe().getId() + " - Nome " + ateFamilia.getEquipe().getPessoa().getNome() + "}"
        );
        ateFamilia = new AteFamilia();
        reorder();
        Messages.info("Sucesso", "Registro excluído");

    }

    public void reorder() {
        int y = 1;
        for (int i = 0; i < listAteFamilia.size(); i++) {
            listAteFamilia.get(i).setOrdem(y);
            new Dao().update(listAteFamilia.get(i), true);
            y++;
        }
    }

    public AteFamilia getAteFamilia() {
        if (Sessions.exists("equipePesquisa")) {
            ateFamilia.setEquipe((Equipe) Sessions.getObject("equipePesquisa", true));
            Sessions.remove("equipeBean");
        }
        return ateFamilia;
    }

    public void setAteFamilia(AteFamilia ateFamilia) {
        this.ateFamilia = ateFamilia;
    }

    public List<AteFamilia> getListAteFamilia() {
        return listAteFamilia;
    }

    public void setListAteFamilia(List<AteFamilia> listAteFamilia) {
        this.listAteFamilia = listAteFamilia;
    }

    public void onSelect(SelectEvent event) {
        reorder();
    }

    public void onUnselect(UnselectEvent event) {
        reorder();
    }

    public void onReorder() {
        reorder();
    }

    public AteFamilia getSelectedAteFamilia() {
        return selectedAteFamilia;
    }

    public void setSelectedAteFamilia(AteFamilia selectedAteFamilia) {
        this.selectedAteFamilia = selectedAteFamilia;
    }
}
