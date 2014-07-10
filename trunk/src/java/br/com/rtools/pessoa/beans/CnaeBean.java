package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.dao.CnaeDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CnaeBean {

    private Cnae cnae;
    private List<Cnae> listCnaes;
    private String descricaoPesquisa;
    private String comoPesquisa;
    private String porPesquisa;

    @PostConstruct
    public void init() {
        cnae = new Cnae();
        listCnaes = new ArrayList<>();
        descricaoPesquisa = "";
        comoPesquisa = "";
        porPesquisa = "numero";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("cnaeBean");
        Sessions.remove("cnaePesquisa");
    }

    public void save() {
        Dao dao = new Dao();
        if (cnae.getId() == -1) {
            if (dao.save(cnae, true)) {
                Messages.info("Sucesso", "Registro inserido");
            } else {
                Messages.warn("Erro", "Ao inserir registro");
            }
        } else {
            if (dao.update(cnae, true)) {
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar registro");
            }
        }
    }

    public void delete() {
        Dao dao = new Dao();
        if (cnae.getId() == -1) {
            if (dao.delete(cnae, true)) {
                Messages.info("Sucesso", "Registro removido");
            } else {
                Messages.warn("Erro", "Ao remover registro");
            }
        }
    }

    public void clear() {
        Sessions.remove("cnaeBean");
    }

    public String edit(Cnae c) {
        cnae = c;
        String url = (String) Sessions.getString("urlRetorno");
        Sessions.put("linkClicado", true);
        Sessions.put("cnaePesquisa", cnae);
        return url;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public List<Cnae> getListCnaes() {
        if (listCnaes.isEmpty()) {
            CnaeDao cnaeDao = new CnaeDao();
            listCnaes = cnaeDao.pesquisaCnae(descricaoPesquisa, porPesquisa, comoPesquisa);
        }
        return listCnaes;
    }

    public void setListCnaes(List<Cnae> listCnaes) {
        this.listCnaes = listCnaes;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        descricaoPesquisa = descricaoPesquisa.replace("-", "").replace("/", "").replace(".", "");
        listCnaes.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        descricaoPesquisa = descricaoPesquisa.replace("-", "").replace("/", "").replace(".", "");
        listCnaes.clear();
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

}
