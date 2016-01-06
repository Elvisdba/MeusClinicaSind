package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.questionario.Questao;
import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.QuestionarioSubgrupo;
import br.com.clinicaintegrada.questionario.dao.QuestaoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioGrupoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioSubgrupoDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
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
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@SessionScoped
public class QuestaoBean implements Serializable {

    private Questao questao;
    private List<SelectItem> listQuestionarios;
    private String idQuestionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<SelectItem> listQuestionarioGrupo;
    private String idQuestionarioGrupo;
    private List<SelectItem> listQuestionarioSubgrupo;
    private String idQuestionarioSubgrupo;
    private List<Questao> listQuestoes;
    private Boolean order;

    @PostConstruct
    public void init() {
        questao = new Questao();
        questao.setSubgrupo(new QuestionarioSubgrupo());
        listRotinas = new ArrayList<>();
        idRotina = null;
        idQuestionarioGrupo = null;
        listQuestionarios = new ArrayList<>();
        idQuestionario = null;
        listQuestionarioSubgrupo = new ArrayList<>();
        idQuestionarioSubgrupo = null;
        listQuestionarioGrupo = new ArrayList<>();
        listQuestoes = new ArrayList<>();
        loadRotinas();
        loadQuestionarios();
        loadQuestionarioGrupo();
        loadQuestionarioSubgrupo();
        loadQuestoes();
        order = false;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("questionarioGrupoBean");
    }

    /**
     * 1 - Carrega a lista de questionários
     *
     * @param tcase
     */
    public void listener(Integer tcase) {
        if (tcase == 1) {
            if (idRotina == null || idRotina.isEmpty()) {
                listQuestionarios.clear();
                listQuestionarioGrupo.clear();
                listQuestionarioSubgrupo.clear();
                listQuestoes.clear();
                idQuestionario = null;
                idQuestionarioGrupo = null;
                idQuestionarioSubgrupo = null;                
            } else {
                loadQuestionarios();
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupo();
                loadQuestoes();
            }
            order = false;
        }
        if (tcase == 2) {
            if (idQuestionario == null || idQuestionario.isEmpty()) {
                listQuestionarioGrupo.clear();
                listQuestionarioSubgrupo.clear();
                idQuestionarioGrupo = null;
                idQuestionarioSubgrupo = null;
                listQuestoes.clear();
            } else {
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupo();
                loadQuestoes();
            }
            order = false;
        }
        if (tcase == 3) {
            if (idQuestionarioGrupo == null || idQuestionarioGrupo.isEmpty()) {
                listQuestionarioSubgrupo.clear();
                idQuestionarioSubgrupo = null;
                listQuestoes.clear();
            } else {
                loadQuestionarioSubgrupo();
                loadQuestoes();
            }
            order = false;
        }
        if (tcase == 4) {
            if (idQuestionarioSubgrupo == null || idQuestionarioSubgrupo.isEmpty()) {
                listQuestoes.clear();
            } else {
                loadQuestoes();                                
            }
        }
        if (tcase == 5) {
            questao = new Questao();
        }
    }

    public void save() {
        if (questao.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        if (idRotina == null) {
            Messages.warn("Validação", "Informar rotina");
            return;
        }
        if (idQuestionario == null) {
            Messages.warn("Validação", "Informar questionário");
            return;
        }
        if (idQuestionarioGrupo == null) {
            Messages.warn("Validação", "Informar grupo");
            return;
        }
        if (idQuestionarioSubgrupo == null) {
            Messages.warn("Validação", "Informar subgrupo");
            return;
        }
        questao.setSubgrupo((QuestionarioSubgrupo) new Dao().find(new QuestionarioSubgrupo(), Integer.parseInt(idQuestionarioSubgrupo)));
        if (questao.getId() == null) {
            if (new Dao().save(questao, true)) {
                questao = new Questao();
                reorder();
                loadQuestoes();
                Messages.info("Sucesso", "Registro inserido");
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (new Dao().update(questao, true)) {
                questao = new Questao();
                reorder();
                loadQuestoes();
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void edit(Questao q) {
        idRotina = Integer.toString(q.getSubgrupo().getGrupo().getQuestionario().getRotina().getId());
        idQuestionario = Integer.toString(q.getSubgrupo().getGrupo().getQuestionario().getId());
        idQuestionarioGrupo = Integer.toString(q.getSubgrupo().getGrupo().getId());
        idQuestionarioSubgrupo = Integer.toString(q.getSubgrupo().getId());
        questao = q;
    }

    public void delete(Questao q) {
        if (new Dao().delete(q, true)) {
            Messages.info("Sucesso", "Registro excluído");
            questao = new Questao();
            reorder();
            loadQuestoes();
        } else {
            Messages.warn("Erro", "Ao excluir registro!");
        }
    }

    public final void loadRotinas() {
        idRotina = null;
        List<Rotina> list = new RotinaDao().listGroupByQuestionarios();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getAtivo()) {
                if (i == 0) {
                    idRotina = "" + list.get(i).getId();
                }
                listRotinas.add(new SelectItem("" + list.get(i).getId(), list.get(i).getRotina()));
            }
        }
    }

    public final void loadQuestionarios() {
        idQuestionario = null;
        listQuestionarios = new ArrayList<>();
        if (idRotina != null) {
            List<Questionario> list = new QuestionarioDao().findByRotina(Integer.parseInt(idRotina));
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idQuestionario = "" + list.get(i).getId();
                }
                listQuestionarios.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
            }
        } else {
            listQuestionarios.clear();
        }
    }

    public final void loadQuestionarioGrupo() {
        idQuestionarioGrupo = null;
        listQuestionarioGrupo = new ArrayList<>();
        if (idQuestionario != null) {
            List<QuestionarioGrupo> list = new QuestionarioGrupoDao().findByQuestionario(Integer.parseInt(idQuestionario));
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idQuestionarioGrupo = "" + list.get(i).getId();
                }
                listQuestionarioGrupo.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
            }
        } else {
            listQuestionarioGrupo.clear();
        }
    }

    public final void loadQuestionarioSubgrupo() {
        idQuestionarioSubgrupo = null;
        listQuestionarioSubgrupo = new ArrayList<>();
        if (idQuestionarioGrupo != null) {
            List<QuestionarioSubgrupo> list = new QuestionarioSubgrupoDao().findByGrupo(Integer.parseInt(idQuestionarioGrupo));
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idQuestionarioSubgrupo = "" + list.get(i).getId();
                }
                listQuestionarioSubgrupo.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
            }
        } else {
            listQuestionarioSubgrupo.clear();
        }
    }

    public final void loadQuestoes() {
        if (idQuestionarioSubgrupo != null) {
            listQuestoes = new ArrayList<>();
            QuestaoDao questaoDao = new QuestaoDao();
            questaoDao.setOrder_by("Q.ordem");
            listQuestoes = questaoDao.findBySubgrupo(Integer.parseInt(idQuestionarioSubgrupo));
        } else {
            listQuestoes.clear();

        }
    }

    public List<SelectItem> getListQuestionarios() {
        return listQuestionarios;
    }

    public void setListQuestionarios(List<SelectItem> listQuestionarios) {
        this.listQuestionarios = listQuestionarios;
    }

    public String getIdQuestionario() {
        return idQuestionario;
    }

    public void setIdQuestionario(String idQuestionario) {
        this.idQuestionario = idQuestionario;
    }

    public List<Questao> getListQuestoes() {
        return listQuestoes;
    }

    public void setListQuestoes(List<Questao> listQuestoes) {
        this.listQuestoes = listQuestoes;
    }

    public List<SelectItem> getListRotinas() {
        return listRotinas;
    }

    public void setListRotinas(List<SelectItem> listRotinas) {
        this.listRotinas = listRotinas;
    }

    public String getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(String idRotina) {
        this.idRotina = idRotina;
    }

    public List<SelectItem> getListQuestionarioGrupo() {
        return listQuestionarioGrupo;
    }

    public void setListQuestionarioGrupo(List<SelectItem> listQuestionarioGrupo) {
        this.listQuestionarioGrupo = listQuestionarioGrupo;
    }

    public String getIdQuestionarioGrupo() {
        return idQuestionarioGrupo;
    }

    public void setIdQuestionarioGrupo(String idQuestionarioGrupo) {
        this.idQuestionarioGrupo = idQuestionarioGrupo;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public List<SelectItem> getListQuestionarioSubgrupo() {
        return listQuestionarioSubgrupo;
    }

    public void setListQuestionarioSubgrupo(List<SelectItem> listQuestionarioSubgrupo) {
        this.listQuestionarioSubgrupo = listQuestionarioSubgrupo;
    }

    public String getIdQuestionarioSubgrupo() {
        return idQuestionarioSubgrupo;
    }

    public void setIdQuestionarioSubgrupo(String idQuestionarioSubgrupo) {
        this.idQuestionarioSubgrupo = idQuestionarioSubgrupo;
    }

    public Boolean getOrder() {
        return order;
    }

    public void setOrder(Boolean order) {
        this.order = order;
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

    public void reorder() {
        for (int i = 0; i < listQuestoes.size(); i++) {
            listQuestoes.get(i).setOrdem(i + 1);
            new Dao().update(listQuestoes.get(i), true);
        }
        Messages.info("Sucesso", "Lista reordenada com sucesso");
    }
}
