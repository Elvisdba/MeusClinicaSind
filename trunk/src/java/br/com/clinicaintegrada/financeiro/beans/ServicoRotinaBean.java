package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.ServicoRotina;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.dao.ServicoRotinaDao;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
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
public class ServicoRotinaBean implements Serializable {

    private ServicoRotina servicoRotina;
    private List<ServicoRotina> listServicoRotina;
    /**
     * <ul>
     * <li>[0] Serviços</li>
     * <li>[1] Rotina</li>
     * </ul>
     */
    private Integer[] index;
    private List[] listSelectItem;

    @PostConstruct
    public void init() {
        index = new Integer[]{0, 0};
        listSelectItem = new ArrayList[]{
            new ArrayList<>(),
            new ArrayList<>()
        };
        servicoRotina = new ServicoRotina();
        listServicoRotina = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("servicoRotinaBean");
    }

    public List<SelectItem> getListServicos() {
        if (listSelectItem[0].isEmpty()) {
            ServicosDao db = new ServicosDao();
            List<Servicos> list = (List<Servicos>) db.findServicos(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListRotinas() {
        if (listSelectItem[1].isEmpty()) {
            if (listSelectItem[0].isEmpty()) {
                return listSelectItem[0];
            }
            RotinaDao rotinaDao = new RotinaDao();
            List<Rotina> list = (List<Rotina>) rotinaDao.findRotinasSemServicoRotina(Integer.parseInt(getListServicos().get(index[0]).getDescription()), SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[1].add(new SelectItem(i, list.get(i).getRotina(), Integer.toString((list.get(i).getId()))));
            }
            if (listSelectItem[1].isEmpty()) {
                listSelectItem[1] = new ArrayList<>();
            }
        }
        return listSelectItem[1];
    }

    public void clear(int type) {
        if (type == 1) {
            getListRotinas().clear();
            listServicoRotina.clear();
            index[1] = 0;
        } else if (type == 2) {
            listServicoRotina.clear();
        }
    }

    public void add() {
        DaoInterface di = new Dao();
        ServicoRotinaDao servicoRotinaDB = new ServicoRotinaDao();
        if (getListServicos().isEmpty()) {
            Messages.warn("Validação", "Serviços não existe!");
            return;
        }
        if (getListRotinas().isEmpty()) {
            Messages.warn("Sistema", "Não existem rotinas!");
            return;
        }
        servicoRotina.setServicos((Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[0]).getDescription())));
        servicoRotina.setRotina((Rotina) di.find(new Rotina(), Integer.parseInt(getListRotinas().get(index[1]).getDescription())));
        if (servicoRotinaDB.exists(servicoRotina.getServicos().getId(), servicoRotina.getRotina().getId())) {
            Messages.warn("Validação", "Serviço Rotina já existe!");
            return;
        }
        servicoRotina.setCliente(SessaoCliente.get());
        if (di.save(servicoRotina, true)) {
            Logger log = new Logger();
            log.save(
                    "ID: " + servicoRotina.getId()
                    + " - Serviços: (" + servicoRotina.getServicos().getId() + ") " + servicoRotina.getServicos().getDescricao()
                    + " - Rotina: (" + servicoRotina.getRotina().getId() + ") " + servicoRotina.getRotina().getRotina()
            );
            Messages.info("Sucesso", "Registro adicionado");
            listServicoRotina.clear();
            getListRotinas().clear();
        } else {
            Messages.warn("Erro", "Erro ao Salvar!");
        }
        servicoRotina = new ServicoRotina();
    }

    public void delete(ServicoRotina sr) {
        if (sr.getId() != -1) {
            servicoRotina = sr;
        }
        if (sr.getId() == -1) {
            return;
        }
        DaoInterface di = new Dao();
        if (di.delete(servicoRotina, true)) {
            Logger log = new Logger();
            log.delete(
                    "ID: " + servicoRotina.getId()
                    + " - Serviços: (" + servicoRotina.getServicos().getId() + ") " + servicoRotina.getServicos().getDescricao()
                    + " - Rotina: (" + servicoRotina.getRotina().getId() + ") " + servicoRotina.getRotina().getRotina()
            );
            Messages.info("Sucesso", "Registro excluído");
            listServicoRotina.clear();
            getListRotinas().clear();
        } else {
            Messages.warn("Erro", "Erro ao Excluir!");
        }
        servicoRotina = new ServicoRotina();
    }

    public List<ServicoRotina> getListServicoRotina() {
        if (listServicoRotina.isEmpty()) {
            if (!getListServicos().isEmpty()) {
                ServicoRotinaDao servicoRotinaDao = new ServicoRotinaDao();
                listServicoRotina = servicoRotinaDao.findByServico(Integer.parseInt(getListServicos().get(index[0]).getDescription()));
            }
        }
        return listServicoRotina;
    }

    public void setListServicoRotina(List<ServicoRotina> listServicoRotina) {
        this.listServicoRotina = listServicoRotina;
    }

    public ServicoRotina getServicoRotina() {
        return servicoRotina;
    }

    public void setServicoRotina(ServicoRotina servicoRotina) {
        this.servicoRotina = servicoRotina;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }
}
