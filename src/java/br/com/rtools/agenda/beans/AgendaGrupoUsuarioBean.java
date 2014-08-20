package br.com.rtools.agenda.beans;

import br.com.rtools.agenda.AgendaGrupoUsuario;
import br.com.rtools.agenda.GrupoAgenda;
import br.com.rtools.logSistema.Logger;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AgendaGrupoUsuarioBean implements Serializable {

    private Usuario usuario;
    private List<AgendaGrupoUsuario> agendaGrupoUsuarios;
    private List<GrupoAgenda> listGrupoAgenda;
    private GrupoAgenda grupoAgenda;

    @PostConstruct
    public void init() {
        usuario = new Usuario();
        agendaGrupoUsuarios = new ArrayList<>();
        listGrupoAgenda = new ArrayList<>();
        grupoAgenda = new GrupoAgenda();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("agendaGrupoUsuarioBean");
        Sessions.remove("usuarioPesquisa");
    }

    public void clear() {
        Sessions.remove("agendaGrupoUsuarioBean");
    }

    public void save() {
        if (usuario.getId() == -1) {
            Messages.warn("Validação", "Pesquisar usuário!");
            return;
        }
        AgendaGrupoUsuario agp = new AgendaGrupoUsuario();
        if (listGrupoAgenda.isEmpty()) {
            Messages.warn("Validação", "Cadastrar grupos!");
            return;
        }
        DaoInterface dao = new Dao();
        agp.setGrupoAgenda(grupoAgenda);
        agp.setUsuario(usuario);
        for (int i = 0; i < agendaGrupoUsuarios.size(); i++) {
            if (agp.getGrupoAgenda().getId() == agendaGrupoUsuarios.get(i).getGrupoAgenda().getId()
                    && agp.getUsuario().getId() == agendaGrupoUsuarios.get(i).getUsuario().getId()) {
                Messages.warn("Validação", "Grupo e usuário ja cadastrado!");
                return;
            }
        }
        Logger logger = new Logger();
        agp.setCliente(SessaoCliente.get());
        if (dao.save(agp, true)) {
            logger.save("ID: " + agp.getId() + " - Grupo Agenda: " + agp.getGrupoAgenda().getDescricao() + " - Usuário: (" + agp.getUsuario().getId() + ") " + agp.getUsuario().getLogin());
            Messages.info("Sucesso", "Registro adicionado");
            Sessions.remove("agendaTelefoneBean");
            agendaGrupoUsuarios.clear();
        } else {
            Messages.warn("Erro", "Ao adicionar!");
        }
    }

    public void remove(AgendaGrupoUsuario agp) {
        DaoInterface di = new Dao();
        if (di.delete(agp, true)) {
            Logger logger = new Logger();
            logger.delete("ID: " + agp.getId() + " - Grupo Agenda: " + agp.getGrupoAgenda().getDescricao() + " - Usuário: (" + agp.getUsuario().getId() + ") " + agp.getUsuario().getLogin());
            Messages.info("Sucesso", "Registro removido");
            Sessions.remove("agendaTelefoneBean");
            agendaGrupoUsuarios.clear();
        } else {
            Messages.warn("Erro", "Ao remover!");
        }
    }

    public List<AgendaGrupoUsuario> getAgendaGrupoUsuarios() {
        if (agendaGrupoUsuarios.isEmpty()) {
            if (!getListGrupoAgenda().isEmpty()) {
                DaoInterface di = new Dao();
                if (grupoAgenda != null) {
                    if (grupoAgenda.getId() != -1) {
                        agendaGrupoUsuarios = (List<AgendaGrupoUsuario>) di.listQuery("AgendaGrupoUsuario", "findGrupoAgenda", new Object[]{grupoAgenda.getId(), SessaoCliente.get().getId()});
                    }
                }
            }
        }
        return agendaGrupoUsuarios;
    }

    public void setAgendaGrupoUsuarios(List<AgendaGrupoUsuario> agendaGrupoUsuarios) {
        this.agendaGrupoUsuarios = agendaGrupoUsuarios;
    }

    public Usuario getUsuario() {
        if (Sessions.exists("usuarioPesquisa")) {
            usuario = (Usuario) Sessions.getObject("usuarioPesquisa", true);
        }
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<GrupoAgenda> getListGrupoAgenda() {
        if (listGrupoAgenda.isEmpty()) {
            Dao dao = new Dao();
            listGrupoAgenda = dao.listByCliente(new GrupoAgenda(), true);
            if (listGrupoAgenda.isEmpty()) {
                grupoAgenda = listGrupoAgenda.get(0);
            }
        }
        return listGrupoAgenda;
    }

    public void setListGrupoAgenda(List<GrupoAgenda> listGrupoAgenda) {
        this.listGrupoAgenda = listGrupoAgenda;
    }

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }
}
