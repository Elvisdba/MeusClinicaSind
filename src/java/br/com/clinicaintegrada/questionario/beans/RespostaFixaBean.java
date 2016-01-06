package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.questionario.Questao;
import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.QuestionarioSubgrupo;
import br.com.clinicaintegrada.questionario.RespostaFixa;
import br.com.clinicaintegrada.questionario.dao.QuestaoDao;
import br.com.clinicaintegrada.questionario.dao.RespostaFixaDao;
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
public class RespostaFixaBean implements Serializable {

    private RespostaFixa respostaFixa;
    private List<SelectItem> listQuestionarios;
    private String idQuestionario;
    private List<SelectItem> listRotinas;
    private String idRotina;
    private List<SelectItem> listQuestionarioGrupo;
    private String idQuestionarioGrupo;
    private List<SelectItem> listQuestionarioSubgrupo;
    private String idQuestionarioSubgrupo;
    private List<SelectItem> listQuestao;
    private String idQuestao;
    private List<RespostaFixa> listRespostaFixa;
    private String opcao;
    private Boolean order;

    @PostConstruct
    public void init() {
        respostaFixa = new RespostaFixa();
        respostaFixa.setQuestao(new Questao());
        listRotinas = new ArrayList<>();
        idRotina = null;
        idQuestionarioGrupo = null;
        listQuestionarios = new ArrayList<>();
        listQuestao = new ArrayList<>();
        idQuestao = null;
        idQuestionario = null;
        listQuestionarioSubgrupo = new ArrayList<>();
        idQuestionarioSubgrupo = null;
        listQuestionarioGrupo = new ArrayList<>();
        listRespostaFixa = new ArrayList<>();
        loadRotinas();
        loadQuestionarios();
        loadQuestionarioGrupo();
        loadQuestionarioSubgrupo();
        loadQuestoes();
        loadRespostaFixa();
        opcao = "";
        order = false;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("respostaFixaBean");
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
                listQuestao.clear();
                idQuestionario = null;
                idQuestionarioGrupo = null;
                idQuestionarioSubgrupo = null;
                idQuestao = null;
            } else {
                loadQuestionarios();
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupo();
                loadQuestoes();
                loadRespostaFixa();
            }
            order = false;
        }
        if (tcase == 2) {
            if (idQuestionario == null || idQuestionario.isEmpty()) {
                listQuestionarioGrupo.clear();
                listQuestionarioSubgrupo.clear();
                listQuestao.clear();
                idQuestionarioGrupo = null;
                idQuestionarioSubgrupo = null;
                idQuestao = null;
            } else {
                loadQuestionarioGrupo();
                loadQuestionarioSubgrupo();
                loadQuestoes();
                loadRespostaFixa();
            }
            order = false;
        }
        if (tcase == 3) {
            if (idQuestionarioGrupo == null || idQuestionarioGrupo.isEmpty()) {
                listQuestionarioSubgrupo.clear();
                listQuestao.clear();
                idQuestionarioSubgrupo = null;
                idQuestao = null;
            } else {
                loadQuestionarioSubgrupo();
                loadQuestoes();
                loadRespostaFixa();
            }
            order = false;
        }
        if (tcase == 4) {
            if (idQuestionarioSubgrupo == null || idQuestionarioSubgrupo.isEmpty()) {
                listQuestao.clear();
                listRespostaFixa.clear();
                idQuestao = null;
            } else {
                loadQuestoes();
                loadRespostaFixa();
            }
            order = false;
        }
        if (tcase == 5) {
            if (idQuestao == null || idQuestao.isEmpty()) {
                listRespostaFixa.clear();
            } else {
                loadRespostaFixa();
            }
        }
        if (tcase == 6) {
            respostaFixa.setDescricao(opcao);
        }
        if (tcase == 7) {
            respostaFixa = new RespostaFixa();
            opcao = "";
        }
    }

    public void save() {
        opcao = "";
        if (respostaFixa.getDescricao().isEmpty()) {
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
        if (idQuestao == null) {
            Messages.warn("Validação", "Informar questão");
            return;
        }
        if (respostaFixa.getId() == null) {
            respostaFixa.setQuestao((Questao) new Dao().find(new Questao(), Integer.parseInt(idQuestao)));
            List<RespostaFixa> list = new RespostaFixaDao().findByQuestao(Integer.parseInt(idQuestao));
            if (!list.isEmpty()) {
                switch (list.get(0).getDescricao()) {
                    case "#money":
                    case "#int":
                    case "#boolean":
                    case "#text":
                        Messages.warn("Validação", "Tipo já pré definido para esta questão (Só pode haver um tipo)!");
                        return;
                }
            }
            if (new Dao().save(respostaFixa, true)) {
                respostaFixa = new RespostaFixa();
                reorder();
                loadRespostaFixa();
                Messages.info("Sucesso", "Registro inserido");
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            if (new Dao().update(respostaFixa, true)) {
                respostaFixa = new RespostaFixa();
                reorder();
                loadRespostaFixa();
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                Messages.warn("Erro", "Ao inserir registro!");
            }
        }
    }

    public void edit(RespostaFixa rf) {
        idRotina = Integer.toString(rf.getQuestao().getSubgrupo().getGrupo().getQuestionario().getRotina().getId());
        idQuestionario = Integer.toString(rf.getQuestao().getSubgrupo().getGrupo().getQuestionario().getId());
        idQuestionarioGrupo = Integer.toString(rf.getQuestao().getSubgrupo().getGrupo().getId());
        idQuestionarioSubgrupo = Integer.toString(rf.getQuestao().getSubgrupo().getId());
        idQuestao = Integer.toString(rf.getQuestao().getId());
        opcao = rf.getDescricao();
        respostaFixa = rf;
    }

    public void delete(RespostaFixa rf) {
        if (new Dao().delete(rf, true)) {
            Messages.info("Sucesso", "Registro excluído");
            respostaFixa = new RespostaFixa();
            reorder();
            loadRespostaFixa();
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
        idQuestao = null;
        listQuestao = new ArrayList<>();
        if (idQuestionarioSubgrupo != null) {
            List<Questao> list = new QuestaoDao().findBySubgrupo(Integer.parseInt(idQuestionarioSubgrupo));
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idQuestao = "" + list.get(i).getId();
                }
                listQuestao.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
            }
        } else {
            listQuestao.clear();
        }
    }

    public final void loadRespostaFixa() {
        if (idQuestao != null) {
            listRespostaFixa = new ArrayList<>();
            RespostaFixaDao respostaFixaDao = new RespostaFixaDao();
            respostaFixaDao.setOrder_by("RF.ordem");
            listRespostaFixa = respostaFixaDao.findByQuestao(Integer.parseInt(idQuestao));
        } else {
            listRespostaFixa.clear();

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

    public List<RespostaFixa> getListRespostaFixa() {
        if (listRespostaFixa.isEmpty()) {
            order = false;
        }
        return listRespostaFixa;
    }

    public void setListRespostaFixa(List<RespostaFixa> listRespostaFixa) {
        this.listRespostaFixa = listRespostaFixa;
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

    public RespostaFixa getRespostaFixa() {
        return respostaFixa;
    }

    public void setRespostaFixa(RespostaFixa respostaFixa) {
        this.respostaFixa = respostaFixa;
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

    public List<SelectItem> getListQuestao() {
        return listQuestao;
    }

    public void setListQuestao(List<SelectItem> listQuestao) {
        this.listQuestao = listQuestao;
    }

    public String getIdQuestao() {
        return idQuestao;
    }

    public void setIdQuestao(String idQuestao) {
        this.idQuestao = idQuestao;
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
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
        if (listRespostaFixa.size() == 1) {
            return;
        }
        for (int i = 0; i < listRespostaFixa.size(); i++) {
            listRespostaFixa.get(i).setOrdem(i + 1);
            new Dao().update(listRespostaFixa.get(i), true);
        }
        Messages.info("Sucesso", "Lista reordenada com sucesso");
    }

}
