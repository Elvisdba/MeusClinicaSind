package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.Evento;
import br.com.clinicaintegrada.coordenacao.GrupoEvento;
import br.com.clinicaintegrada.coordenacao.dao.EventoDao;
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
public class EventoBean implements Serializable {

    private Evento evento;
    private List<Evento> listEvento;
    private List<SelectItem> listGrupoEvento;
    private int idGrupoEvento;

    @PostConstruct
    public void init() {
        evento = new Evento();
        listEvento = new ArrayList<>();
        listGrupoEvento = new ArrayList<>();
        idGrupoEvento = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("eventoBean");
        Sessions.remove("eventoPesquisa");
    }

    public void clear() {
        Sessions.remove("eventoBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (listGrupoEvento.isEmpty()) {
            Messages.warn("Validação", "Cadastrar funções para evento!");
            return;
        }
        evento.setGrupoEvento((GrupoEvento) dao.find(new GrupoEvento(), Integer.parseInt(listGrupoEvento.get(idGrupoEvento).getDescription())));
        Logger logger = new Logger();
        if (evento.getId() == -1) {
            EventoDao eventoDao = new EventoDao();
            if (eventoDao.exists(evento.getGrupoEvento().getId(), evento.getDescricao())) {
                Messages.warn("Validação", "Evento já cadastrada para esta função!");
                return;
            }
            if (dao.save(evento, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + evento.getId()
                        + " - Grupo Evento: [" + evento.getGrupoEvento().getId() + "]" + evento.getGrupoEvento().getDescricao()
                        + " - Descrição: " + evento.getDescricao()
                        + " - Sigla: " + evento.getSigla()
                        + " - Web: " + evento.isWeb()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao adicionar registro!");
            }
        } else {
            Evento e = (Evento) dao.find(evento);
            String beforeUpdate = ""
                    + " ID: " + e.getId()
                    + " - Grupo Evento: [" + e.getGrupoEvento().getId() + "]" + evento.getGrupoEvento().getDescricao()
                    + " - Descrição: " + e.getDescricao()
                    + " - Sigla: " + e.getSigla()
                    + " - Web: " + e.isWeb();
            if (dao.update(evento, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + evento.getId()
                        + " - Grupo Evento: [" + evento.getGrupoEvento().getId() + "]" + evento.getGrupoEvento().getDescricao()
                        + " - Descrição: " + evento.getDescricao()
                        + " - Sigla: " + evento.getSigla()
                        + " - Web: " + evento.isWeb()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }
    }

    public void delete(Evento e) {
        evento = e;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (evento.getId() != -1) {
            if (dao.delete(evento, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " ID: " + evento.getId()
                        + " - Grupo Evento: [" + evento.getGrupoEvento().getId() + "]" + evento.getGrupoEvento().getDescricao()
                        + " - Descrição: " + evento.getDescricao()
                        + " - Sigla: " + evento.getSigla()
                        + " - Web: " + evento.isWeb()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public String edit(Evento e) {
        evento = e;
        for (int i = 0; i < listGrupoEvento.size(); i++) {
            if (evento.getGrupoEvento().getId() == Integer.parseInt(listGrupoEvento.get(i).getDescription())) {
                idGrupoEvento = i;
                break;
            }
        }
        return null;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Evento> getListEvento() {
        if (listEvento.isEmpty()) {
            EventoDao eventoDao = new EventoDao();
            listEvento = eventoDao.findAll();
        }
        return listEvento;
    }

    public void setListEvento(List<Evento> listEvento) {
        this.listEvento = listEvento;
    }

    public List<SelectItem> getListGrupoEvento() {
        if (listGrupoEvento.isEmpty()) {
            Dao dao = new Dao();
            List<GrupoEvento> list = (List<GrupoEvento>) dao.list(new GrupoEvento(), true);
            for (int i = 0; i < list.size(); i++) {
                listGrupoEvento.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listGrupoEvento;
    }

    public void setListGrupoEvento(List<SelectItem> listGrupoEvento) {
        this.listGrupoEvento = listGrupoEvento;
    }

    public int getIdGrupoEvento() {
        return idGrupoEvento;
    }

    public void setIdGrupoEvento(int idGrupoEvento) {
        this.idGrupoEvento = idGrupoEvento;
    }

}
