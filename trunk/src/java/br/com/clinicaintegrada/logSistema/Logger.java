package br.com.clinicaintegrada.logSistema;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.SegEvento;
import br.com.clinicaintegrada.seguranca.Log;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.beans.SimplesBean;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Sessions;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class Logger extends salvaLogs {

    private boolean transaction = false;
    private boolean cadastroSimples = false;
    private final List<Log> listLogs = new ArrayList<>();

    // LOG ARQUIVOS    
    public String novo(String acao, String obs) {
        Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        String iusuario = ": ";
        if (user != null) {
            iusuario = iusuario + user.getPessoa().getId() + " " + user.getPessoa().getNome();
        }
        String ihorario = "[horario " + DataHoje.horaMinuto() + "]";
        String iacao = " >> " + acao;
        String iobs = "# " + obs;
        salvarLinha(ihorario + " " + iusuario + " " + iacao + " " + iobs);
        return null;
    }

    // LOG NO BANCO DE DADOS
    /**
     * <p>
     * <strong>startList</strong></p>
     * <p>
     * <strong>Example:</strong>startList();</p>
     * <p>
     * Abre uma transação e cria uma lista do processo atual para geração de
     * log, essa semana só será finalizada se houver o commit do processo.
     * Evitando assim gerar logs sem necessidade. Finish: commit() or
     * rollback()</p>
     *
     * @author Bruno
     *
     */
    public void startList() {
        transaction = true;
    }

    /**
     * <p>
     * <strong>saveList</strong></p>
     * <p>
     * Grava todos os logs da transação atual. Finish: saveList()</p>
     *
     * @author Bruno
     *
     */
    public void saveList() {
        for (Log l : listLogs) {
            execute(l);
        }
        listLogs.clear();
        transaction = false;
    }

    /**
     * <p>
     * <strong>cancelList</strong></p>
     * <p>
     * Apaga todos os logs da transação atual. Finish: cancelList()</p>
     *
     * @author Bruno
     *
     */
    public void cancelList() {
        listLogs.clear();
        transaction = false;
    }

    /**
     * <p>
     * <strong>Live</strong></p>
     * <p>
     * <strong>Example:</strong>live("User" + user.getLogin());</p>
     *
     * @author Bruno
     * @param infoLive Texto de informações livres para o log, não é gerado
     * nenhum Evento (seg_evento -> default null) para esta execução.
     *
     */
    public void live(String infoLive) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", null));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", null);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Save - String</strong></p>
     * <strong>Example:</strong><ul><li>save("User" + user.getLogin());</li><li>
     * Utilizar se for um novo registro. </li></ul>
     *
     * @author Bruno
     * @param infoLive Texto de informações livres para o log.
     *
     */
    public void save(String infoLive) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", getSegEvento(1)));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", getSegEvento(1));
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Save - Object</strong></p>
     * <p>
     * <strong>Example:</strong>save((User) user, true); Utilizar se for um novo
     * registro. </p>
     *
     * @author Bruno
     * @param object - Texto de informações livres para o log.
     * @param isObject - default = true
     *
     */
    public void save(Object object, boolean isObject) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), "", getSegEvento(1)));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), "", getSegEvento(1));
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Update - String</strong></p>
     * <p>
     * <strong>Example:</strong>update("Joao", "João"); Utilizar se for alterar
     * um registro existente. </p>
     *
     * @author Bruno
     * @param beforeUpdate - Informações antes da modificação.
     * @param afterUpdate - Informações depois da modificação.
     *
     */
    public void update(String beforeUpdate, String afterUpdate) {
        if (!beforeUpdate.equals(afterUpdate)) {
            if (transaction) {
                listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate, afterUpdate, getSegEvento(3)));
            } else {
                Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate, afterUpdate, getSegEvento(3));
                execute(log);
            }
        }
    }

    /**
     * <p>
     * <strong>Update - Object</strong></p>
     * <p>
     * <strong>Example:</strong>update((User) userBefore, (User) userAfter,
     * true); Utilizar se for alterar um registro existente. </p>
     *
     * @author Bruno
     * @param beforeUpdate - Informações antes da modificação.
     * @param afterUpdate - Informações depois da modificação.
     * @param isObject - default = true
     *
     */
    public void update(Object beforeUpdate, Object afterUpdate, boolean isObject) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate.toString(), afterUpdate.toString(), getSegEvento(3)));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate.toString(), afterUpdate.toString(), getSegEvento(3));
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Delete - String</strong></p>
     * <p>
     * <strong>Example:</strong>save("User" + user.getLogin()); Utilizar se for
     * remover um registro. </p>
     *
     * @author Bruno
     * @param infoLive - Texto de informações livres para o log.
     *
     */
    public void delete(String infoLive) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, null, getSegEvento(2)));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, null, getSegEvento(2));
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Delete - Object</strong></p>
     * <p>
     * <strong>Example:</strong>delete((User) user, true); Utilizar se for
     * remover um registro. </p>
     *
     * @author Bruno
     * @param object - Texto de informações livres para o log.
     * @param isObject - default = true
     *
     */
    public void delete(Object object, boolean isObject) {
        if (transaction) {
            listLogs.add(new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), null, getSegEvento(2)));
        } else {
            Log log = new Log(-1, getCliente(), new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), null, getSegEvento(2));
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Execute</strong></p>
     * <p>
     * <strong>Example:</strong>Executa o método.</p>
     *
     * @param log
     *
     */
    public void execute(Log log) {
        DaoInterface di = new Dao();
        di.save(log, true);
    }

    public Usuario getUsuario() {
        if (Sessions.exists("sessaoUsuario")) {
            return (Usuario) Sessions.getObject("sessaoUsuario");
        }
        return null;
    }

    public Cliente getCliente() {
        if (Sessions.exists("sessaoCliente")) {
            return (Cliente) Sessions.getObject("sessaoCliente");
        }
        return null;
    }

    public Rotina getRotina() {
        if (cadastroSimples) {
            String classe = ((SimplesBean) Sessions.getObject("simplesBean")).getSessoes()[0];
            RotinaDao rotinaDao = new RotinaDao();
            Rotina rotina = rotinaDao.pesquisaRotinaPorClasse(classe);
            if (rotina == null) {
                return null;
            }
            if (rotina.getId() != -1) {
                return rotina;
            }
        } else {
            HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String nomePagina = this.converteURL(paginaRequerida.getRequestURI());
            RotinaDao rotinaDao = new RotinaDao();
            Rotina rotina = rotinaDao.pesquisaRotinaPorPagina(nomePagina, true);
            try {
                if (rotina.getId() != -1) {
                    return rotina;
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    /**
     * <p>
     * <strong>Evento</strong></p>
     * <p>
     * <strong>Lista de eventos:</strong>
     * <ul>
     * <li>(1) Inclusão;</li>
     * <li>(2) Exclusão;</li>
     * <li>(3) Alteração;</li>
     * <li>(4) Consulta;</li>
     * </ul>
     *
     * @author Bruno
     * @param idEvento
     *
     * @return Evento
     */
    public SegEvento getSegEvento(Integer idEvento) {
        try {
            DaoInterface di = new Dao();
            return (SegEvento) di.find(new SegEvento(), idEvento);
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isCadastroSimples() {
        return cadastroSimples;
    }

    public void setCadastroSimples(boolean cadastroSimples) {
        this.cadastroSimples = cadastroSimples;
    }
}
