package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.EquipeDao;
import br.com.clinicaintegrada.questionario.Questao;
import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.QuestionarioSubgrupo;
import br.com.clinicaintegrada.questionario.Resposta;
import br.com.clinicaintegrada.questionario.RespostaFixa;
import br.com.clinicaintegrada.questionario.RespostaLote;
import br.com.clinicaintegrada.questionario.dao.QuestaoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioGrupoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioSubgrupoDao;
import br.com.clinicaintegrada.questionario.dao.RespostaDao;
import br.com.clinicaintegrada.questionario.dao.RespostaFixaDao;
import br.com.clinicaintegrada.questionario.dao.RespostaLoteDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.PF;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class QuestionarioRespostaBean implements Serializable {
// 121

    private Integer rotina_id;
    private Resposta resposta;
    private String tituloRotina;
    private String tituloQuestionario;
    private List<QuestionarioGrupo> listQuestionarioGrupo;
    private List<QuestionarioSubgrupo>[] listQuestionarioSubgrupo;
    private List<Questao> listQuestoes;
    private List<RespostaFixa> listRespostaFixa;
    private Pessoa pessoa;
    private List<SelectItem> listOpcoes;
    private List<SelectItem> listQuestionario;
    private String quantidade;
    private String valor;
    private String opcao;
    private String texto;
    private String indexString;
    private Integer index;
    private String activeIndexGrupo;
    private String activeIndexSubgrupo;
    private Questao questao;
    private RespostaLote respostaLote;
    private String questionario_id;
    private List<RespostaLote> listRespostaLote;
    private String[] descricaoPesquisa;
    private Integer comoPesquisa;
    private String porPesquisa;
    private Boolean disabledPesquisaFisica;
    private Questao questaoMemoria;

    @PostConstruct
    public void init() {
        descricaoPesquisa = new String[2];
        descricaoPesquisa[0] = "";
        descricaoPesquisa[1] = "";
        comoPesquisa = null;
        porPesquisa = "nome";
        questao = null;
        rotina_id = null;
        resposta = new Resposta();
        tituloRotina = "";
        tituloQuestionario = "";
        listRespostaFixa = new ArrayList();
        listQuestionario = new ArrayList();
        listQuestionarioSubgrupo = null;
        listOpcoes = new ArrayList();
        listQuestionarioGrupo = new ArrayList();
        listQuestoes = new ArrayList();
        listRespostaLote = new ArrayList();
        pessoa = new Pessoa();
        quantidade = "0";
        valor = null;
        opcao = null;
        indexString = null;
        index = null;
        activeIndexSubgrupo = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
        activeIndexGrupo = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
        texto = "";
        questionario_id = "";
        respostaLote = null;
        disabledPesquisaFisica = false;
        questaoMemoria = null;
    }

    public String edit(RespostaLote rl) {
        respostaLote = rl;
        pessoa = respostaLote.getPessoa();
        ChamadaPaginaBean.link();
        return "questionarioCoordenacao";
    }

    public void load(Integer rotina_id) {
        Dao dao = new Dao();
        tituloRotina = ((Rotina) dao.find(new Rotina(), rotina_id)).getRotina();
        List<Questionario> list = new QuestionarioDao().findByRotina(rotina_id);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                questionario_id = "" + list.get(i).getId();
            }
            listQuestionario.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
        }
        loadQuestionarioGrupo();
        loadQuestionarioSubgrupo();
        loadQuestao();
        listOpcoes.clear();

    }

    public void loadQuestionarioGrupo() {
        QuestionarioGrupoDao qgd = new QuestionarioGrupoDao();
        qgd.setOrder_by("QG.ordem");
        listQuestionarioGrupo = new ArrayList();
        listQuestionarioGrupo = qgd.findByQuestionario(Integer.parseInt(questionario_id));

    }

    public void loadQuestionarioSubgrupo() {
        if (!listQuestionarioGrupo.isEmpty()) {
            listQuestionarioSubgrupo = new ArrayList[listQuestionarioGrupo.size()];
            QuestionarioSubgrupoDao qsgd = new QuestionarioSubgrupoDao();
            qsgd.setOrder_by("QSG.ordem");
            for (int i = 0; i < listQuestionarioGrupo.size(); i++) {
                listQuestionarioSubgrupo[i] = new ArrayList();
                listQuestionarioSubgrupo[i].addAll(qsgd.findByGrupo(listQuestionarioGrupo.get(i).getId()));
            }
        }

    }

    public void loadQuestao() {
        if (listQuestionarioSubgrupo != null && listQuestionarioSubgrupo.length > 0) {
            listQuestoes = new ArrayList();
            QuestaoDao qd = new QuestaoDao();
            qd.setOrder_by("Q.ordem");
            for (int i = 0; i < listQuestionarioSubgrupo.length; i++) {
                for (int y = 0; y < listQuestionarioSubgrupo[i].size(); y++) {
                    listQuestoes.addAll(qd.findBySubgrupo(listQuestionarioSubgrupo[i].get(y).getId()));
                }
            }
        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("questionarioRespostaBean");
    }

    public void clear() {
        Sessions.put("rotina_id", rotina_id);
        Sessions.remove("questionarioRespostaBean");
    }

    public void setRotina(Integer rotina_id) {
        setRotina(null, rotina_id);
    }

    public void setRotina(Fisica fisica, Integer rotina_id) {
        Sessions.put("rotina_id", rotina_id);
        if (fisica != null) {
            // ChamadaPaginaBean.link();
            Sessions.put("fisicaPesquisa", fisica);
            Sessions.put("disabledPesquisaFisica", true);
        }
    }

    public void listener(Integer tcase) {
        if (tcase == 1) {
            // listQuestionarioGrupo = qgd.findByQuestionario(Integer.parseInt(questionario_id));
            listOpcoes.clear();
        } else if (tcase == 2) {
            listQuestionarioGrupo.size();
            loadQuestionarioGrupo();
            loadQuestionarioSubgrupo();
            loadQuestao();
        }
    }

    public void click(Questao q) {
        loadRespostaLote();
        questao = q;
        quantidade = "0";
        valor = null;
        opcao = null;
        indexString = null;
        index = null;
        texto = null;
        RespostaFixaDao respostaFixaDao = new RespostaFixaDao();
        respostaFixaDao.setOrder_by("RF.ordem");
        List<RespostaFixa> list = respostaFixaDao.findByQuestao(q.getId());
        if (list.isEmpty()) {
            Messages.warn("Sistema", "Nenhuma pergunta encontrada! Cadastrar respostas fixas.");
            PF.update("form_resposta");
            return;
        }
        Resposta resposta = null;
        RespostaDao respostaDao = new RespostaDao();
        if (list.size() == 1) {
            RespostaFixa respostaFixa = list.get(0);
            if (respostaLote.getId() != null) {
                resposta = respostaDao.findByPessoaAndRespostaFixa(respostaLote.getId(), respostaFixa.getId());
            }
            switch (respostaFixa.getDescricao()) {
                case "#int":
                    if (resposta != null && resposta.getId() != null) {
                        quantidade = resposta.getDescricao();
                    }
                    PF.openDialog("dlg_int");
                    PF.update("i_pg_int");
                    break;
                case "#money":
                    if (resposta != null && resposta.getId() != null) {
                        valor = resposta.getDescricao();
                    }
                    PF.openDialog("dlg_money");
                    PF.update("i_pg_money");
                    break;
                case "#boolean":
                    if (resposta != null && resposta.getId() != null) {
                        opcao = resposta.getDescricao();
                    }
                    PF.openDialog("dlg_boolean");
                    PF.update("i_pg_boolean");
                    break;
                case "#text":
                    if (resposta != null && resposta.getId() != null) {
                        texto = resposta.getDescricao();
                    }
                    PF.openDialog("dlg_text");
                    PF.update("i_pg_text");
                    break;
                default:
                    Messages.warn("Sistema", "Nenhum tipo foi encontrado");
                    break;
            }
        } else {
            listOpcoes.clear();
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    indexString = "" + list.get(i).getId();
                }
                listOpcoes.add(new SelectItem("" + list.get(i).getId(), list.get(i).getDescricao()));
            }
            for (int i = 0; i < list.size(); i++) {
                if (respostaLote.getId() != null) {
                    resposta = respostaDao.findByPessoaAndRespostaFixa(respostaLote.getId(), list.get(i).getId());
                    if (resposta != null && resposta.getId() != null) {
                        indexString = "" + list.get(i).getId();
                    }
                }
            }
            PF.openDialog("dlg_option");
            PF.update("i_pg_option");
        }
    }

    public Integer getRotina_id() {
        return rotina_id;
    }

    public void setRotina_id(Integer rotina_id) {
        this.rotina_id = rotina_id;
    }

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    public List<RespostaFixa> getListRespostaFixa() {
        return listRespostaFixa;
    }

    public void setListRespostaFixa(List<RespostaFixa> listRespostaFixa) {
        this.listRespostaFixa = listRespostaFixa;
    }

    public Pessoa getPessoa() {
        if (Sessions.exists("fisicaPesquisa")) {
            pessoa = ((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa();
            respostaLote = null;
            respostaLote = new RespostaLoteDao().findBy(Integer.parseInt(questionario_id), pessoa.getId(), DataHoje.dataHoje());
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTituloRotina() {
        if (Sessions.exists("rotina_id")) {
            rotina_id = Sessions.getInteger("rotina_id", true);
            load(rotina_id);
        }
        return tituloRotina;
    }

    public void setTituloRotina(String tituloRotina) {
        this.tituloRotina = tituloRotina;
    }

    public String getTituloQuestionario() {
        return tituloQuestionario;
    }

    public void setTituloQuestionario(String tituloQuestionario) {
        this.tituloQuestionario = tituloQuestionario;
    }

    public String getActiveIndex(Object collection) {
        try {
            List list = (List) collection;
            if (list != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    sb.append(i);
                    sb.append(",");
                }
                return sb.toString();
            }
        } catch (Exception e) {

        }
        return "";
    }

    public List<SelectItem> getListOpcoes() {
        return listOpcoes;
    }

    public void setListOpcoes(List<SelectItem> listOpcoes) {
        this.listOpcoes = listOpcoes;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        try {
            if (Integer.parseInt(quantidade) < 1) {
                quantidade = "0";
            }
            this.quantidade = quantidade;
        } catch (Exception e) {
            this.quantidade = "0";
        }
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        try {
            if (Moeda.converteUS$(valor) < 0) {
                valor = "0,00";
            }
            this.valor = valor;
        } catch (Exception e) {
            valor = "0,00";
        }
    }

    public String getOpcao() {
        return opcao;
    }

    public void setOpcao(String opcao) {
        this.opcao = opcao;
    }

    public String getIndexString() {
        return indexString;
    }

    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }

    public String getRespostaFinal(Integer questao_id) {
        return "";
    }

    public void saveQuantidade() {
        listenerQuantidade(true);
    }

    public void deleteQuantidade() {
        listenerQuantidade(false);
    }

    public void listenerQuantidade(Boolean save) {
        Dao dao = new Dao();
        RespostaDao respostaDao = new RespostaDao();
        Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao.getId());
        if (!save) {
            if (r != null && r.getId() != null) {
                if (dao.delete(r, true)) {
                    Messages.info("Sucesso", "Resposta removida!");
                    return;
                } else {
                    Messages.warn("Validação", "Ao remover resposta!");
                    return;
                }
            } else {
                Messages.info("Sucesso", "Resposta removida!");
                return;
            }
        }
        try {
            if (quantidade == null || Integer.parseInt(quantidade) <= 0 || quantidade.equals("#int")) {
                Messages.warn("Validação", "Informar uma quantidade válida!");
                return;
            }
        } catch (Exception e) {
            Messages.warn("Validação", "Informar uma quantidade válida!");
            return;
        }
        RespostaLote rl = getRespostaLote();
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        updateRespostaLote();
        questaoMemoria = null;
        if (r.getId() == null) {
            r.setRespostaLote(getRespostaLote());
            r.setRespostaFixa(rf);
            r.setDescricao(quantidade);
            if (dao.save(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (quantidade != null && !quantidade.equals("#int")) {
                r.setDescricao(quantidade);
            }
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão atualizada");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveOpcao() {
        listenerOpcao(true);
    }

    public void deleteOpcao() {
        listenerOpcao(false);
    }

    public void listenerOpcao(Boolean save) {
        Dao dao = new Dao();
        RespostaDao respostaDao = new RespostaDao();
        Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao.getId());
        if (!save) {
            if (r != null && r.getId() != null) {
                if (dao.delete(r, true)) {
                    Messages.info("Sucesso", "Resposta removida!");
                    return;
                } else {
                    Messages.warn("Validação", "Ao remover resposta!");
                    return;
                }
            } else {
                Messages.info("Sucesso", "Resposta removida!");
                return;
            }
        }
        if (opcao == null || opcao.isEmpty() || opcao.equals("#boolean")) {
            Messages.warn("Validação", "Selecionar uma opção!");
            return;
        }
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        updateRespostaLote();
        questaoMemoria = null;
        if (r.getId() == null) {
            r.setRespostaLote(getRespostaLote());
            r.setRespostaFixa(rf);
            r.setDescricao(opcao);
            if (dao.save(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (opcao != null && !opcao.equals("#boolean")) {
                r.setDescricao(opcao);
            }
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveValor() {
        listenerValor(true);
    }

    public void deleteValor() {
        listenerValor(false);
    }

    public void listenerValor(Boolean save) {
        Dao dao = new Dao();
        RespostaDao respostaDao = new RespostaDao();
        Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao.getId());
        if (!save) {
            if (r != null && r.getId() != null) {
                if (dao.delete(r, true)) {
                    Messages.info("Sucesso", "Resposta removida!");
                    return;
                } else {
                    Messages.warn("Validação", "Ao remover resposta!");
                    return;
                }
            } else {
                Messages.info("Sucesso", "Resposta removida!");
                return;
            }
        }
        if (valor == null || valor.isEmpty() || valor.equals("#money")) {
            Messages.warn("Validação", "Digitar um valor!");
            return;
        }
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        if (valor == null || valor.isEmpty()) {
            Messages.warn("Sistema", "Nenhum valor espefíciado!");
            return;

        }
        if (Moeda.converteUS$(valor) < 0) {
            Messages.warn("Sistema", "Valor deve ser maior ou igual à 0!");
            return;

        }
        updateRespostaLote();
        questaoMemoria = null;
        if (r.getId() == null) {
            r.setRespostaLote(getRespostaLote());
            r.setRespostaFixa(rf);
            r.setDescricao(valor);
            if (dao.save(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (valor != null && !valor.equals("#money")) {
                r.setDescricao(valor);
            }
            r.setDescricao(valor);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão atualizada");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveTexto() {
        listenerValor(true);
    }

    public void deleteTexto() {
        listenerValor(false);
    }

    public void listenerTexto(Boolean save) {
        Dao dao = new Dao();
        RespostaDao respostaDao = new RespostaDao();
        Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao.getId());
        if (!save) {
            if (r != null && r.getId() != null) {
                if (dao.delete(r, true)) {
                    Messages.info("Sucesso", "Resposta removida!");
                    return;
                } else {
                    Messages.warn("Validação", "Ao remover resposta!");
                    return;
                }
            } else {
                Messages.info("Sucesso", "Resposta removida!");
                return;
            }
        }
        if (texto == null || texto.isEmpty() || texto.equals("#text")) {
            Messages.warn("Validação", "Digitar um valor!");
            return;
        }
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        updateRespostaLote();
        questaoMemoria = null;
        if (r.getId() == null) {
            r.setRespostaLote(getRespostaLote());
            r.setRespostaFixa(rf);
            r.setDescricao(texto);
            if (dao.save(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (texto != null && !texto.equals("#text")) {
                r.setDescricao(texto);
            }
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveIndexString() {
        listenerIndexString(true);
    }

    public void deleteIndexString() {
        listenerIndexString(false);
    }

    public void listenerIndexString(Boolean save) {
        Dao dao = new Dao();
        RespostaDao respostaDao = new RespostaDao();
        Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao.getId());
        if (!save) {
            if (r != null && r.getId() != null) {
                if (dao.delete(r, true)) {
                    Messages.info("Sucesso", "Resposta removida!");
                    return;
                } else {
                    Messages.warn("Validação", "Ao remover resposta!");
                    return;
                }
            } else {
                Messages.info("Sucesso", "Resposta removida!");
                return;
            }
        }
        if (indexString == null || indexString.isEmpty()) {
            Messages.warn("Validação", "Selecionar uma opção!");
            return;
        }
        RespostaFixa rf = (RespostaFixa) dao.find(new RespostaFixa(), Integer.parseInt(indexString));
        updateRespostaLote();
        questaoMemoria = null;
        if (r.getId() == null) {
            r.setRespostaLote(getRespostaLote());
            r.setRespostaFixa(rf);
            if (dao.save(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                questaoMemoria = r.getRespostaFixa().getQuestao();
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void loadRespostaLote() {
        if (respostaLote == null) {
            Dao dao = new Dao();
            respostaLote = new RespostaLoteDao().findBy(Integer.parseInt(questionario_id), pessoa.getId(), DataHoje.dataHoje());
            if (respostaLote == null) {
                respostaLote = new RespostaLote();
                respostaLote.setPessoa(pessoa);
                Equipe e = new EquipeDao().findByPessoaAndFuncaoEquipe(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId());
                if (e == null) {
                    Messages.warn("Sistema", "Equipe não existe!");
                    return;
                }
                respostaLote.setEquipe(e);
                respostaLote.setLancamento(DataHoje.dataHoje());
                respostaLote.setHora(DataHoje.hora());
                respostaLote.setQuestionario((Questionario) dao.find(new Questionario(), Integer.parseInt(questionario_id)));
                if (!dao.save(respostaLote, true)) {
                    respostaLote = null;
                } else {
                    Logger logger = new Logger();
                    logger.save("ID: " + respostaLote.getId()
                            + " - Lançamento: " + respostaLote.getLancamentoString()
                            + " - Equipe: (" + respostaLote.getEquipe().getId() + ") " + respostaLote.getEquipe().getPessoa().getNome()
                            + " - Pessoa: (" + respostaLote.getPessoa().getId() + ") " + respostaLote.getPessoa().getNome()
                            + " - Quationário: (" + respostaLote.getQuestionario().getId() + ") " + respostaLote.getQuestionario().getDescricao()
                    );
                }
            }
        }
    }

    public void updateRespostaLote() {
        RespostaLote rl = getRespostaLote();
        if (rl.getId() == null) {
            return;
        }
        Integer compare = DataHoje.dataHoje().compareTo(rl.getLancamento());
        if (compare > 0) {
            rl.setAtualizacao(new Date());
            if (new Dao().update(rl, true)) {
                Logger logger = new Logger();
                logger.update("", "ID: " + rl.getId()
                        + " - Lançamento: " + rl.getLancamentoString()
                        + " - Atualização: " + rl.getAtualizacaoString()
                        + " - Pessoa: (" + rl.getPessoa().getId() + ") " + rl.getPessoa().getNome()
                        + " - Quationário: (" + rl.getQuestionario().getId() + ") " + rl.getQuestionario().getDescricao()
                );
            }
        }
    }

    public RespostaLote getRespostaLote() {
        return respostaLote;
    }

    public void setRespostaLote(RespostaLote respostaLote) {
        this.respostaLote = respostaLote;
    }

    public String getResultado(Integer questao_id) {
        String respostaString = "";
        if (pessoa.getId() != -1) {
            if (respostaLote != null && respostaLote.getId() != null) {
                RespostaDao respostaDao = new RespostaDao();
                Resposta r = respostaDao.findByQuestao(getRespostaLote().getId(), questao_id);
                if (r.getId() != null) {
                    if (r.getRespostaFixa() != null) {
                        respostaString = r.getRespostaFixa().getDescricao();
                        if (r.getDescricao() != null && !r.getDescricao().isEmpty()) {
                            respostaString = r.getDescricao();
                        }
                    }
                }
            }
        }
        return respostaString;
    }

    public String getActiveIndexSubgrupo() {
        return activeIndexSubgrupo;
    }

    public void setActiveIndexSubgrupo(String activeIndexSubgrupo) {
        this.activeIndexSubgrupo = activeIndexSubgrupo;
    }

    public String getActiveIndexGrupo() {
        return activeIndexGrupo;
    }

    public void setActiveIndexGrupo(String activeIndexGrupo) {
        this.activeIndexGrupo = activeIndexGrupo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<SelectItem> getListQuestionario() {
        return listQuestionario;
    }

    public void setListQuestionario(List<SelectItem> listQuestionario) {
        this.listQuestionario = listQuestionario;
    }

    public String getQuestionario_id() {
        return questionario_id;
    }

    public void setQuestionario_id(String questionario_id) {
        this.questionario_id = questionario_id;
    }

    public List<RespostaLote> getListRespostaLote() {
        return listRespostaLote;
    }

    public void setListRespostaLote(List<RespostaLote> listRespostaLote) {
        this.listRespostaLote = listRespostaLote;
    }

    public void loadListRespostaLote() {
        listRespostaLote.clear();
        if (disabledPesquisaFisica) {
            descricaoPesquisa[0] = "" + pessoa.getId();
            listRespostaLote = (List<RespostaLote>) new RespostaLoteDao().findBy("id", 0, descricaoPesquisa);
        } else {
            listRespostaLote = (List<RespostaLote>) new RespostaLoteDao().findBy(porPesquisa, comoPesquisa, descricaoPesquisa);
        }
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa(1);
        loadListRespostaLote();
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa(2);
        loadListRespostaLote();
    }

    public String[] getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String[] descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public Integer getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(Integer comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    /**
     * Questionário grupo
     *
     * @return
     */
    public List<QuestionarioGrupo> getListQGrupo() {
        try {
            return listQuestionarioGrupo;
        } catch (Exception e) {
            return new ArrayList<>();

        }
    }

    /**
     * Questionário subgrupo
     *
     * @param index
     * @return
     */
    public List<QuestionarioSubgrupo> getListQSubgrupo(Integer index) {
        try {
            return listQuestionarioSubgrupo[index];
        } catch (Exception e) {
            return new ArrayList<>();

        }
    }

    public Integer getSequence(Integer index) {
        try {
            return index;
        } catch (Exception e) {
            return null;

        }
    }

    /**
     * .Retornar questões do Subgrupo
     *
     * @param questionario_subgrupo_id
     * @return
     */
    public List<Questao> getListQuestoes(Integer questionario_subgrupo_id) {
        try {
            List<Questao> list = new ArrayList();
            for (int i = 0; i < listQuestoes.size(); i++) {
                if (Objects.equals(listQuestoes.get(i).getSubgrupo().getId(), questionario_subgrupo_id)) {
                    list.add(listQuestoes.get(i));
                }
            }
            return list;
        } catch (Exception e) {
            return new ArrayList<>();

        }
    }

    public Boolean getDisabledPesquisaFisica() {
        if (Sessions.exists("disabledPesquisaFisica")) {
            loadListRespostaLote();
            disabledPesquisaFisica = Sessions.getBoolean("disabledPesquisaFisica");
        }
        return disabledPesquisaFisica;
    }

    public void setDisabledPesquisaFisica(Boolean disabledPesquisaFisica) {
        this.disabledPesquisaFisica = disabledPesquisaFisica;
    }

    public String getStyleClassQuestao(Questao questao) {
        try {
            if (questaoMemoria != null && questao != null) {
                if (Objects.equals(questaoMemoria.getId(), questao.getId())) {
                    return "border: 1px solid red;";
                }
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public String getAnchor() {
        if (questaoMemoria != null) {
            String anchor = "";
            anchor += questaoMemoria.getSubgrupo().getGrupo().getQuestionario().getId();
            anchor += questaoMemoria.getSubgrupo().getGrupo().getId();
            anchor += questaoMemoria.getSubgrupo().getId();
            anchor += questaoMemoria.getId();
            return anchor;
        }
        return "";
    }

    public Questao getQuestaoMemoria() {
        return questaoMemoria;
    }

    public void setQuestaoMemoria(Questao questaoMemoria) {
        this.questaoMemoria = questaoMemoria;
    }
}
