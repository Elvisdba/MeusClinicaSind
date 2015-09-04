package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class QuestionarioBean {

    private Questionario questionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<Questionario> listQuestionarios;

    @PostConstruct
    public void init() {
        questionario = new Questionario();
        listRotinas = new ArrayList<>();
        idRotina = null;
        listQuestionarios = new ArrayList<>();
        loadRotinas();
        loadQuestionarios();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("questionarioBean");
    }

    /**
     * 1 - Carrega a lista de questionários
     *
     * @param tcase
     */
    public void listener(Integer tcase) {
        if (tcase == 1) {
            loadQuestionarios();
        }
    }

    public void save() {
        if (questionario.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        questionario.setRotina((Rotina) new Dao().find(new Rotina(), Integer.parseInt(idRotina)));
        if (new Dao().save(questionario, true)) {
            Messages.info("Sucesso", "Registro inserido");
            questionario = new Questionario();
            loadQuestionarios();
        } else {
            Messages.warn("Erro", "Ao inserir registro!");
        }
    }

    public void delete(Questionario q) {
        if (new Dao().delete(q, true)) {
            Messages.info("Sucesso", "Registro excluído");
            questionario = new Questionario();
            loadQuestionarios();
        } else {
            Messages.warn("Erro", "Ao excluir registro!");
        }

    }

    public Questionario getQuestionario() {
        return questionario;
    }

    public void setQuestionario(Questionario questionario) {
        this.questionario = questionario;
    }

    public final void loadRotinas() {
        idRotina = null;
        List<Rotina> list = new Dao().list(new Rotina(), true);
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
        if (idRotina != null) {
            listQuestionarios = new ArrayList<>();
            listQuestionarios = new QuestionarioDao().findByRotina(Integer.parseInt(idRotina));
        }
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

    public List<Questionario> getListQuestionarios() {
        return listQuestionarios;
    }

    public void setListQuestionarios(List<Questionario> listQuestionarios) {
        this.listQuestionarios = listQuestionarios;
    }

}
