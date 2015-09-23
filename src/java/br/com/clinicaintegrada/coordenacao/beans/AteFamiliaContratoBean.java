package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.AteFamilia;
import br.com.clinicaintegrada.coordenacao.AteFamiliaContrato;
import br.com.clinicaintegrada.coordenacao.dao.AteFamiliaContratoDao;
import br.com.clinicaintegrada.coordenacao.dao.AteFamiliaDao;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AteFamiliaContratoBean implements Serializable {

    private List<AteFamiliaContrato> listAteFamiliaContrato;
    private AteFamilia ateFamilia;

    @PostConstruct
    public void init() {
        ateFamilia = null;
        listAteFamiliaContrato = null;
        loadAteFamiliaContrato();
        loadAteFamilia();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("ateFamiliaContratoBean");
    }

    public void loadAteFamiliaContrato() {
        listAteFamiliaContrato = null;
        listAteFamiliaContrato = new AteFamiliaContratoDao().findByCliente(SessaoCliente.get().getId());
    }

    public List<AteFamiliaContrato> getListAteFamiliaContrato() {
        return listAteFamiliaContrato;
    }

    public void setListAteFamiliaContrato(List<AteFamiliaContrato> listAteFamiliaContrato) {
        this.listAteFamiliaContrato = listAteFamiliaContrato;
    }

    /**
     * Próximo da lista
     *
     * @return
     */
    public AteFamilia getAteFamilia() {
        return ateFamilia;
    }

    public void loadAteFamilia() {
        try {
            if (!listAteFamiliaContrato.isEmpty()) {
                AteFamiliaDao ateFamiliaDao = new AteFamiliaDao();
                Equipe e = new Equipe();
                List<AteFamilia> listAteFamilia = ateFamiliaDao.findByCliente(SessaoCliente.get().getId());
                for (int i = 0; i < listAteFamilia.size(); i++) {
                    if (listAteFamiliaContrato.get(0).getEquipe().getId().equals(listAteFamilia.get(i).getEquipe().getId())) {
                        AteFamilia atual = listAteFamilia.get(i);
                        ateFamilia = ateFamiliaDao.nextOrdemByCliente(atual.getOrdem(), SessaoCliente.get().getId());
                        break;
                    }
                }
                // return AteFamiliaDao.nextOrdemByCliente(listAteFamiliaContrato.get(0).Ordem(), SessaoCliente.get().getId());
            }
        } catch (Exception e) {
            ateFamilia = null;
        }
    }

    public void relad() {
        loadAteFamiliaContrato();
        loadAteFamilia();
    }
}
