package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.QuestionarioSubgrupo;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioGrupoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioSubgrupoDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
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
public class QuestionarioSubgrupoBean {

    private QuestionarioSubgrupo questionarioSubgrupo;
    private List<SelectItem> listQuestionarios;
    private String idQuestionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<SelectItem> listQuestionarioGrupo;
    private String idQuestionarioGrupo;
    private List<QuestionarioSubgrupo> listQuestionarioSubgrupos;

    @PostConstruct
    public void init() {
        questionarioSubgrupo = new QuestionarioSubgrupo();
        questionarioSubgrupo.setGrupo(new QuestionarioGrupo());
        listRotinas = new ArrayList<>();
        idRotina = null;
        idQuestionarioGrupo = null;
        listQuestionarios = new ArrayList<>();
        idQuestionario = null;
        listQuestionarioGrupo = new ArrayList<>();
        listQuestionarioSubgrupos = new ArrayList<>();
        loadRotinas();
        loadQuestionarios();
        loadQuestionarioGrupo();
        loadQuestionarioSubgrupos();
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
            loadQuestionarios();
            loadQuestionarioGrupo();
            loadQuestionarioSubgrupos();
        }
        if (tcase == 2) {
            loadQuestionarioGrupo();
            loadQuestionarioSubgrupos();
        }
        if (tcase == 3) {
            loadQuestionarioSubgrupos();
        }
    }

    public void save() {
        if (questionarioSubgrupo.getDescricao().isEmpty()) {
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
        questionarioSubgrupo.setGrupo((QuestionarioGrupo) new Dao().find(new QuestionarioGrupo(), Integer.parseInt(idQuestionarioGrupo)));
        if (new Dao().save(questionarioSubgrupo, true)) {
            questionarioSubgrupo = new QuestionarioSubgrupo();
            loadQuestionarioSubgrupos();
        } else {
            Messages.warn("Erro", "Ao inserir registro!");
        }
    }

    public void delete(QuestionarioGrupo q) {
        if (new Dao().delete(q, true)) {
            Messages.info("Sucesso", "Registro excluído");
            questionarioSubgrupo = new QuestionarioSubgrupo();
            loadQuestionarioSubgrupos();
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

    public final void loadQuestionarioSubgrupos() {
        if (idQuestionarioGrupo != null) {
            listQuestionarioSubgrupos = new ArrayList<>();
            listQuestionarioSubgrupos = new QuestionarioSubgrupoDao().findByGrupo(Integer.parseInt(idQuestionarioGrupo));
        } else {
            listQuestionarioSubgrupos.clear();

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

    public List<QuestionarioSubgrupo> getListQuestionarioSubgrupos() {
        return listQuestionarioSubgrupos;
    }

    public void setListQuestionarioSubgrupos(List<QuestionarioSubgrupo> listQuestionarioSubgrupos) {
        this.listQuestionarioSubgrupos = listQuestionarioSubgrupos;
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

    public QuestionarioSubgrupo getQuestionarioSubgrupo() {
        return questionarioSubgrupo;
    }

    public void setQuestionarioSubgrupo(QuestionarioSubgrupo questionarioSubgrupo) {
        this.questionarioSubgrupo = questionarioSubgrupo;
    }

}
