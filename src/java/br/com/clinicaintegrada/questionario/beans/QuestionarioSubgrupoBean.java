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
public class QuestionarioSubgrupoBean implements Serializable {

    private QuestionarioSubgrupo questionarioSubgrupo;
    private List<SelectItem> listQuestionarios;
    private String idQuestionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<SelectItem> listQuestionarioGrupo;
    private String idQuestionarioGrupo;
    private List<QuestionarioSubgrupo> listQuestionarioSubgrupos;
    private Boolean order;

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
                listQuestionarioSubgrupos.clear();
                idQuestionario = null;
                idQuestionarioGrupo = null;
            } else {
                loadQuestionarios();
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupos();
            }
            order = false;
        }
        if (tcase == 2) {
            if (idQuestionario == null || idQuestionario.isEmpty()) {
                listQuestionarioGrupo.clear();
                listQuestionarioSubgrupos.clear();
                idQuestionarioGrupo = null;
            } else {
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupos();
            }
            order = false;
        }
        if (tcase == 3) {
            if(idQuestionarioGrupo == null || idQuestionarioGrupo.isEmpty()) {
                listQuestionarioSubgrupos.clear();
            } else {
                loadQuestionarioSubgrupos();                
            }
            order = false;
        }
        if (tcase == 4) {
            questionarioSubgrupo = new QuestionarioSubgrupo();
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
        if (questionarioSubgrupo.getId() == null) {
            if (new Dao().save(questionarioSubgrupo, true)) {
                questionarioSubgrupo = new QuestionarioSubgrupo();
                reorder();
                loadQuestionarioSubgrupos();
                Messages.info("Sucesso", "Registro inserido");
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (new Dao().update(questionarioSubgrupo, true)) {
                questionarioSubgrupo = new QuestionarioSubgrupo();
                reorder();
                loadQuestionarioSubgrupos();
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void edit(QuestionarioSubgrupo qs) {
        idRotina = Integer.toString(qs.getGrupo().getQuestionario().getRotina().getId());
        idQuestionario = Integer.toString(qs.getGrupo().getQuestionario().getId());
        idQuestionarioGrupo = Integer.toString(qs.getGrupo().getId());
        questionarioSubgrupo = qs;
    }

    public void delete(QuestionarioSubgrupo qs) {
        if (new Dao().delete(qs, true)) {
            Messages.info("Sucesso", "Registro excluído");
            questionarioSubgrupo = new QuestionarioSubgrupo();
            reorder();
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
            QuestionarioSubgrupoDao questionarioSubgrupoDao = new QuestionarioSubgrupoDao();
            questionarioSubgrupoDao.setOrder_by("QSG.ordem");
            listQuestionarioSubgrupos = questionarioSubgrupoDao.findByGrupo(Integer.parseInt(idQuestionarioGrupo));
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
        for (int i = 0; i < listQuestionarioSubgrupos.size(); i++) {
            listQuestionarioSubgrupos.get(i).setOrdem(i + 1);
            new Dao().update(listQuestionarioSubgrupos.get(i), true);
        }
        Messages.info("Sucesso", "Lista reordenada com sucesso");
    }
}
