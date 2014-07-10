package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.dao.ProfissaoDao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PesquisarProfissaoBean implements Serializable {

    protected Profissao profissao = new Profissao();
    private String por;
    private String combo;
    private List<Profissao> listProfissao;
    private String descricaoProfissao = "";

    public PesquisarProfissaoBean() {
        profissao = new Profissao();
        por = "";
        combo = "profissao";
        listProfissao = new ArrayList();
    }

    public List<Profissao> getListProfissao() {
        if (!descricaoProfissao.equals("")) {
            profissao.setProfissao(descricaoProfissao);
        }
        if (listProfissao.isEmpty() && !por.isEmpty()) {
            ProfissaoDao profissaoDao = new ProfissaoDao();
            listProfissao = (List<Profissao>) profissaoDao.pesquisaProfissao(por, combo, profissao.getProfissao());
        }
        descricaoProfissao = "";
        return listProfissao;
    }

    public void inicial() {
        listProfissao.clear();
        por = "I";
    }

    public void parcial() {
        listProfissao.clear();
        por = "P";
    }

    public void editProfissao(Profissao p) {
        profissao = new Profissao();
        profissao = p;
    }

    public Profissao getProfissao() {
        if (profissao.getId() == -1) {
            profissao = new Profissao();
        }
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public void setListProfissao(List<Profissao> listProfissao) {
        this.listProfissao = listProfissao;
    }

    public String getDescricaoProfissao() {
        return descricaoProfissao;
    }

    public void setDescricaoProfissao(String descricaoProfissao) {
        this.descricaoProfissao = descricaoProfissao;
    }
}
