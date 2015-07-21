package br.com.clinicaintegrada.relatorios.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.relatorios.RelatorioOrdem;
import br.com.clinicaintegrada.relatorios.Relatorios;
import br.com.clinicaintegrada.relatorios.dao.RelatorioDao;
import br.com.clinicaintegrada.relatorios.dao.RelatorioOrdemDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.beans.RotinaBean;
import br.com.clinicaintegrada.seguranca.dao.RotinaContadorDao;
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
public class RelatorioBean implements Serializable {

    private Relatorios relatorio;
    private RelatorioOrdem relatorioOrdem;
    private List<SelectItem> listRotina;
    private List<Relatorios> listRelatorio;
    private List<RelatorioOrdem> listRelatorioOrdem;
    private Integer rotina_id;

    @PostConstruct
    public void init() {
        relatorio = new Relatorios();
        relatorioOrdem = new RelatorioOrdem();
        listRotina = new ArrayList<>();
        listRelatorio = new ArrayList<>();
        listRelatorioOrdem = new ArrayList<>();
        rotina_id = 0;
    }

    @PreDestroy
    public void destroy() {
        clear();
        Sessions.remove("rotinaBean");
    }

    public void addRelatorioOrdem() {
        if (relatorioOrdem.getNome().isEmpty() || relatorioOrdem.getQuery().isEmpty()) {
            Messages.warn("Validação", "Informar descrição e query!");
            return;
        }
        Dao dao = new Dao();
        Boolean sucess = false;
        String message;
        if (relatorioOrdem.getId() == null) {
            relatorioOrdem.setRelatorios(relatorio);
            if (dao.save(relatorioOrdem, true)) {
                sucess = true;
                message = "Registro inserido";
            } else {
                message = "Ao inserir registro!";
            }
        } else {
            if (dao.update(relatorioOrdem, true)) {
                sucess = true;
                message = "Registro atualizado";
            } else {
                message = "Ao atualizar registro!";
            }
        }
        if (!defaultRelatorioOrdem(relatorioOrdem)) {
            sucess = false;
            message = "Ao definir default!";
        }
        if (sucess) {
            Messages.info("Sucesso", message);
            relatorioOrdem = new RelatorioOrdem();
            listRelatorioOrdem.clear();
        } else {
            Messages.warn("Erro", message);

        }
    }

    public Boolean defaultRelatorioOrdem(RelatorioOrdem ro) {
        if (new RelatorioOrdemDao().defineDefault(ro)) {
            return true;
        }
        return false;
    }

    public void defaultOrdem(RelatorioOrdem ro) {
        if (defaultRelatorioOrdem(ro)) {
            listRelatorioOrdem.clear();
            getListRelatorioOrdem();
        }
    }

    public void deleteRelatorioOrdem(RelatorioOrdem ro) {
        Dao dao = new Dao();
        if (dao.delete(ro, true)) {
            Messages.info("Sucesso", "Registro excluído");
            relatorioOrdem = new RelatorioOrdem();
            listRelatorioOrdem.clear();
        } else {
            Messages.warn("Erro", "Ao excluir registro!");
        }
    }

    public void editRelatorioOrdem(RelatorioOrdem ro) {
        relatorioOrdem = ro;
    }

    public void save() {
        if (relatorio.getNome().isEmpty()) {
            Messages.warn("Validação", "Digite uma descrição!");
            return;
        }

        if (relatorio.getJasper().isEmpty()) {
            Messages.warn("Validação", "Digite um caminho para o Jasper!");
            return;
        }

        Dao dao = new Dao();
        Logger log = new Logger();

        relatorio.setRotina((Rotina) dao.find(new Rotina(), rotina_id));

        dao.openTransaction();
        if (relatorio.getId() == null) {
            if (dao.save(relatorio)) {
                Messages.info("Sucesso!", "Registro inserido");
                new RotinaContadorDao().incrementar(RotinaBean.getRotinaAtual().getId(), relatorio.getRotina().getId(), ((Usuario) Sessions.getObject("sessaoUsuario")).getId());
                log.save("Relatório inserido " + relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
                dao.rollback();
                return;
            }
        } else {
            Relatorios rel = (Relatorios) dao.find(new Relatorios(), relatorio.getId());
            String antes = "De: " + rel.getNome() + " / " + relatorio.getNome() + " -  " + rel.getJasper() + " / " + relatorio.getJasper();
            if (dao.update(relatorio)) {
                Messages.info("Sucesso!", "Registro atualizado");
                log.update(antes, relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
                new RotinaContadorDao().incrementar(RotinaBean.getRotinaAtual().getId(), relatorio.getRotina().getId(), ((Usuario) Sessions.getObject("sessaoUsuario")).getId());
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
                dao.rollback();
                return;
            }
        }
        listRelatorio.clear();
        dao.commit();
    }

    public void clear() {
        clear(0);
    }

    public void clear(Integer tcase) {
        if (tcase == 0) {
            Sessions.remove("relatorioBean");
        } else if (tcase == 1) {
            relatorio = new Relatorios();
            listRelatorioOrdem.clear();
            relatorioOrdem = new RelatorioOrdem();
            listRelatorio.clear();
            rotina_id = -1;
        }
    }

    public void delete() {
        if (relatorio.getId() == -1) {
            Messages.warn("Validação", "Pesquise um relatório para exclusão!");
            return;
        }

        Dao dao = new Dao();
        Logger log = new Logger();
        dao.openTransaction();

        if (dao.delete(relatorio)) {
            Messages.info("Sucesso!", "Registro excluído");
            log.delete(relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
        } else {
            Messages.warn("Erro", "Erro ao excluir registro!");
            dao.rollback();
        }

        dao.commit();
        relatorio = new Relatorios();
        listRelatorio.clear();
        rotina_id = 0;
    }

    public String edit(Relatorios r) {
        this.relatorio = r;
        Sessions.put("linkClicado", true);
        listRotina.clear();
        getListRotina();
        listRelatorioOrdem.clear();
        relatorioOrdem = new RelatorioOrdem();
        rotina_id = relatorio.getRotina().getId();
        return "relatorio";
    }

    public Relatorios getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorios relatorio) {
        this.relatorio = relatorio;
    }

    public List<SelectItem> getListRotina() {
        if (listRotina.isEmpty()) {
            List<Rotina> list = new Dao().list(new Rotina(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    rotina_id = list.get(i).getId();
                }
                listRotina.add(new SelectItem(list.get(i).getId(), list.get(i).getRotina()));
            }
        }
        return listRotina;
    }

    public void setListRotina(List<SelectItem> listRotina) {
        this.listRotina = listRotina;
    }

    public Integer getRotina_id() {
        return rotina_id;
    }

    public void setRotina_id(Integer rotina_id) {
        this.rotina_id = rotina_id;
    }

    public List<Relatorios> getListRelatorio() {
        if (listRelatorio.isEmpty()) {
            listRelatorio = new Dao().list(new Relatorios(), true);
        }
        return listRelatorio;
    }

    public void setListRelatorio(List<Relatorios> listRelatorio) {
        this.listRelatorio = listRelatorio;
    }

    public RelatorioOrdem getRelatorioOrdem() {
        return relatorioOrdem;
    }

    public void setRelatorioOrdem(RelatorioOrdem relatorioOrdem) {
        this.relatorioOrdem = relatorioOrdem;
    }

    public List<RelatorioOrdem> getListRelatorioOrdem() {
        if (relatorio.getId() != null) {
            if (listRelatorioOrdem == null) {
                RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
                listRelatorioOrdem = relatorioOrdemDao.findAllByRelatorio(relatorio.getId());
                for (int i = 0; i < listRelatorioOrdem.size(); i++) {
                    listRelatorioOrdem.set(i, (RelatorioOrdem) new Dao().rebind(listRelatorioOrdem.get(i)));
                }
            }
        }
        return listRelatorioOrdem;
    }

    public void setListRelatorioOrdem(List<RelatorioOrdem> listRelatorioOrdem) {
        this.listRelatorioOrdem = listRelatorioOrdem;
    }

    /**
     * 0 - Ultimas usadas 1 - Mais usadas
     *
     * @param tcase
     */
    public void loadRotinaCombo(Integer tcase) {
        List<Rotina> list = new ArrayList();
        RotinaContadorDao rcd = new RotinaContadorDao();
        if (tcase == 0) {
            rcd.orderData();
            list = rcd.findRotinasByRotinaTela(RotinaBean.getRotinaAtual().getId(), ((Usuario) Sessions.getObject("sessaoUsuario")).getId());
        } else if (tcase == 1) {
            rcd.orderContador();
            list = rcd.findRotinasByRotinaTela(RotinaBean.getRotinaAtual().getId(), ((Usuario) Sessions.getObject("sessaoUsuario")).getId());
        }
        if (!list.isEmpty()) {
            listRotina.clear();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    rotina_id = list.get(i).getId();
                }
                listRotina.add(new SelectItem(list.get(i).getId(), list.get(i).getRotina()));
            }
        }
    }

    /**
     * 1 - Update no relatório default para a rotina
     *
     * @param tcase
     */
    public void listener(Integer tcase) {
        // 1 - Update no relatório default para a rotina
        switch (tcase) {
            case 1:
                if (new RelatorioDao().defineDefault(relatorio)) {
                    Messages.info("Sucesso", "Definido como default desta rotina");
                    relatorio = (Relatorios) new Dao().rebind(relatorio);
                } else {
                    Messages.warn("Erro", "Ao definir como default!");
                }
                break;
        }
    }
}
