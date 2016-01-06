package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioGrupoDao;
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
public class QuestionarioGrupoBean implements Serializable {

    private QuestionarioGrupo questionarioGrupo;
    private List<SelectItem> listQuestionarios;
    private String idQuestionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<QuestionarioGrupo> listQuestionarioGrupos;
    private Boolean order;

    @PostConstruct
    public void init() {
        questionarioGrupo = new QuestionarioGrupo();
        questionarioGrupo.setQuestionario(new Questionario());
        listRotinas = new ArrayList<>();
        idRotina = null;
        listQuestionarios = new ArrayList<>();
        idQuestionario = null;
        listQuestionarioGrupos = new ArrayList<>();
        loadRotinas();
        loadQuestionarios();
        loadQuestionarioGrupos();
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
                listQuestionarioGrupos.clear();
                idRotina = null;
                idQuestionario = null;
            } else {
                loadQuestionarios();
                loadQuestionarioGrupos();
            }
            order = false;
        }
        if (tcase == 2) {
            if (idQuestionario == null || idQuestionario.isEmpty()) {
                listQuestionarioGrupos.clear();
                idQuestionario = null;
            } else {
                loadQuestionarioGrupos();
            }
            order = false;
        }
        if (tcase == 3) {
            questionarioGrupo = new QuestionarioGrupo();
        }
    }

    public void save() {
        if (questionarioGrupo.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        questionarioGrupo.setQuestionario((Questionario) new Dao().find(new Questionario(), Integer.parseInt(idQuestionario)));
        if (questionarioGrupo.getId() == null) {
            if (new Dao().save(questionarioGrupo, true)) {
                Messages.info("Sucesso", "Registro inserido");
                questionarioGrupo = new QuestionarioGrupo();
                reorder();
                loadQuestionarioGrupos();
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (new Dao().update(questionarioGrupo, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                questionarioGrupo = new QuestionarioGrupo();
                reorder();
                loadQuestionarioGrupos();
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void edit(QuestionarioGrupo q) {
        idQuestionario = Integer.toString(q.getQuestionario().getId());
        idRotina = Integer.toString(q.getQuestionario().getRotina().getId());
        questionarioGrupo = q;
    }

    public void delete(QuestionarioGrupo q) {
        if (new Dao().delete(q, true)) {
            Messages.info("Sucesso", "Registro excluído");
            questionarioGrupo = new QuestionarioGrupo();
            reorder();
            loadQuestionarioGrupos();
        } else {
            Messages.warn("Erro", "Ao excluir registro!");
        }

    }

    public QuestionarioGrupo getQuestionarioGrupo() {
        return questionarioGrupo;
    }

    public void setQuestionarioGrupo(QuestionarioGrupo questionarioGrupo) {
        this.questionarioGrupo = questionarioGrupo;
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

    public final void loadQuestionarioGrupos() {
        if (idQuestionario != null) {
            listQuestionarioGrupos = new ArrayList<>();
            QuestionarioGrupoDao questionarioGrupoDao = new QuestionarioGrupoDao();
            questionarioGrupoDao.setOrder_by("QG.ordem");
            listQuestionarioGrupos = questionarioGrupoDao.findByQuestionario(Integer.parseInt(idQuestionario));
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

    public List<QuestionarioGrupo> getListQuestionarioGrupos() {
        return listQuestionarioGrupos;
    }

    public void setListQuestionarioGrupos(List<QuestionarioGrupo> listQuestionarioGrupos) {
        this.listQuestionarioGrupos = listQuestionarioGrupos;
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
        for (int i = 0; i < listQuestionarioGrupos.size(); i++) {
            listQuestionarioGrupos.get(i).setOrdem(i + 1);
            new Dao().update(listQuestionarioGrupos.get(i), true);
        }
        Messages.info("Sucesso", "Lista reordenada com sucesso");
    }
}
