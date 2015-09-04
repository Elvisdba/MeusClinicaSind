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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class RespostaFixaBean {
    
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
            loadQuestionarios();
            loadQuestionarioGrupo();
            loadQuestionarioSubgrupo();
            loadQuestoes();
            loadRespostaFixa();
        }
        if (tcase == 2) {
            loadQuestionarioGrupo();
            loadQuestionarioSubgrupo();
            loadQuestoes();
            loadRespostaFixa();
        }
        if (tcase == 3) {
            loadQuestionarioSubgrupo();
            loadQuestoes();
            loadRespostaFixa();
        }
        if (tcase == 4) {
            loadQuestoes();
            loadRespostaFixa();
        }
        if (tcase == 5) {
            loadRespostaFixa();
        }
        if (tcase == 6) {
            respostaFixa.setDescricao(opcao);
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
            loadRespostaFixa();
        } else {
            Messages.warn("Erro", "Ao inserir registro!");
        }
    }
    
    public void delete(RespostaFixa rf) {
        if (new Dao().delete(rf, true)) {
            Messages.info("Sucesso", "Registro excluído");
            respostaFixa = new RespostaFixa();
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
            listRespostaFixa = new RespostaFixaDao().findByQuestao(Integer.parseInt(idQuestao));
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
    
}
