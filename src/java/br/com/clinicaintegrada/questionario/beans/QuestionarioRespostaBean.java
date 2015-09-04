package br.com.clinicaintegrada.questionario.beans;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.questionario.Questao;
import br.com.clinicaintegrada.questionario.Questionario;
import br.com.clinicaintegrada.questionario.QuestionarioGrupo;
import br.com.clinicaintegrada.questionario.QuestionarioSubgrupo;
import br.com.clinicaintegrada.questionario.Resposta;
import br.com.clinicaintegrada.questionario.RespostaFixa;
import br.com.clinicaintegrada.questionario.dao.QuestaoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioGrupoDao;
import br.com.clinicaintegrada.questionario.dao.QuestionarioSubgrupoDao;
import br.com.clinicaintegrada.questionario.dao.RespostaDao;
import br.com.clinicaintegrada.questionario.dao.RespostaFixaDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.PF;
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
public class QuestionarioRespostaBean {
// 121

    private Integer rotina_id;
    private Resposta resposta;
    private String tituloRotina;
    private String tituloQuestionario;
    private List<QuestionarioGrupo> listQuestionarioGrupo;
    private List<RespostaFixa> listRespostaFixa;
    private Pessoa pessoa;
    private List<SelectItem> listOpcoes;
    private String quantidade;
    private String valor;
    private String opcao;
    private String texto;
    private String indexString;
    private Integer index;
    private String activeIndexGrupo;
    private String activeIndexSubgrupo;
    private Questao questao;

    @PostConstruct
    public void init() {
        questao = null;
        rotina_id = null;
        resposta = new Resposta();
        tituloRotina = "";
        tituloQuestionario = "";
        listRespostaFixa = new ArrayList();
        listOpcoes = new ArrayList();
        listQuestionarioGrupo = new ArrayList();
        pessoa = new Pessoa();
        quantidade = "0";
        valor = null;
        opcao = null;
        indexString = null;
        index = null;
        activeIndexSubgrupo = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
        activeIndexGrupo = "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20";
        texto = "";
        getTituloRotina();
    }

    public void load(Integer rotina_id) {
        Dao dao = new Dao();
        tituloRotina = ((Rotina) dao.find(new Rotina(), rotina_id)).getRotina();
        Questionario q = (Questionario) new QuestionarioDao().findByRotina(rotina_id).get(0);
        tituloQuestionario = q.getDescricao();
        QuestionarioGrupoDao qgd = new QuestionarioGrupoDao();
        qgd.setOrder_by("QG.id");
        listQuestionarioGrupo = qgd.findByQuestionario(q.getId());

    }

    @PreDestroy
    public void destroy() {
        Sessions.put("rotina_id", rotina_id);
        clear();
    }

    public void clear() {
        Sessions.remove("questionarioRespostaBean");
    }

    public void setRotina(Integer rotina_id) {
        Sessions.put("rotina_id", rotina_id);
    }

    public void click(Questao q) {
        questao = q;
        quantidade = "0";
        valor = null;
        opcao = null;
        indexString = null;
        index = null;
        texto = null;
        List<RespostaFixa> list = new RespostaFixaDao().findByQuestao(q.getId());
        if (list.isEmpty()) {
            Messages.warn("Sistema", "Nenhuma pergunta encontrada!");
            return;
        }
        if (list.size() == 1) {
            RespostaFixa respostaFixa = list.get(0);
            switch (respostaFixa.getDescricao()) {
                case "#int":
                    PF.openDialog("dlg_int");
                    break;
                case "#money":
                    PF.openDialog("dlg_money");
                    break;
                case "#boolean":
                    PF.openDialog("dlg_boolean");
                    break;
                case "#text":
                    PF.openDialog("dlg_text");
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
            PF.openDialog("dlg_option");
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
        if (Sessions.exists("contratoPesquisa")) {
            pessoa = ((Contrato) Sessions.getObject("contratoPesquisa", true)).getPaciente();
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

    public List<QuestionarioGrupo> getListQuestionarioGrupo() {
        return listQuestionarioGrupo;
    }

    public void setListQuestionarioGrupo(List<QuestionarioGrupo> listQuestionarioGrupo) {
        this.listQuestionarioGrupo = listQuestionarioGrupo;
    }

    public List<QuestionarioSubgrupo> getListQuestionarioSubgrupo(Integer grupo_id) {
        try {
            QuestionarioSubgrupoDao qsgd = new QuestionarioSubgrupoDao();
            qsgd.setOrder_by("QSG.id");
            List list = qsgd.findByGrupo(grupo_id);
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public List<QuestionarioSubgrupo> getListQuestoes(Integer subgrupo_id) {
        try {
            QuestaoDao qd = new QuestaoDao();
            qd.setOrder_by("Q.id");
            List list = qd.findBySubgrupo(subgrupo_id);
            return list;
        } catch (Exception e) {
            return new ArrayList();
        }
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
        Dao dao = new Dao();
        resposta.setPessoa(pessoa);
        RespostaDao respostaDao = new RespostaDao();
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao.getId());
        if (r.getId() == null) {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            r.setDescricao(quantidade);
            r.setLancamento(DataHoje.dataHoje());
            if (dao.save(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (quantidade != null && !quantidade.equals("#int")) {
                r.setDescricao(quantidade);
            }
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                Messages.info("Sucesso", "Questão atualizada");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveOpcao() {
        Dao dao = new Dao();
        resposta.setPessoa(pessoa);
        RespostaDao respostaDao = new RespostaDao();
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao.getId());
        if (r.getId() == null) {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            r.setDescricao(opcao);
            r.setLancamento(DataHoje.dataHoje());
            if (dao.save(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (opcao != null && !opcao.equals("#boolean")) {
                r.setDescricao(opcao);
            }
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveValor() {
        Dao dao = new Dao();
        resposta.setPessoa(pessoa);
        RespostaDao respostaDao = new RespostaDao();
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao.getId());
        if (r.getId() == null) {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            r.setDescricao(valor);
            r.setLancamento(DataHoje.dataHoje());
            if (dao.save(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (valor != null && !valor.equals("#money")) {
                r.setDescricao(valor);
            }
            r.setDescricao(valor);
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                Messages.info("Sucesso", "Questão atualziada");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveTexto() {
        Dao dao = new Dao();
        resposta.setPessoa(pessoa);
        RespostaDao respostaDao = new RespostaDao();
        RespostaFixa rf = new RespostaFixaDao().findByQuestao(questao.getId(), true);
        if (rf == null) {
            Messages.warn("Sistema", "Resposta não existe!");
            return;
        }
        Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao.getId());
        if (r.getId() == null) {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            r.setDescricao(texto);
            r.setLancamento(DataHoje.dataHoje());
            if (dao.save(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            if (texto != null && !texto.equals("#text")) {
                r.setDescricao(texto);
            }
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public void saveIndexString() {
        Dao dao = new Dao();
        resposta.setPessoa(pessoa);
        RespostaDao respostaDao = new RespostaDao();
        RespostaFixa rf = (RespostaFixa) dao.find(new RespostaFixa(), Integer.parseInt(indexString));
        Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao.getId());
        if (r.getId() == null) {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            r.setLancamento(DataHoje.dataHoje());
            if (dao.save(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao salvar registro!");
            }
        } else {
            r.setPessoa(pessoa);
            r.setRespostaFixa(rf);
            if (dao.update(r, true)) {
                Messages.info("Sucesso", "Questão respondida");
            } else {
                Messages.warn("Erro", "Ao atualizar questão!");
            }
        }
    }

    public String getResultado(Integer questao_id) {
        String respostaString = "";
        if (pessoa.getId() != -1) {
            RespostaDao respostaDao = new RespostaDao();
            Resposta r = respostaDao.findByQuestao(pessoa.getId(), questao_id);
            if (r.getId() != null) {
                if (r.getRespostaFixa() != null) {
                    respostaString = r.getRespostaFixa().getDescricao();
                    if (r.getDescricao() != null && !r.getDescricao().isEmpty()) {
                        respostaString = r.getDescricao();
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
}
