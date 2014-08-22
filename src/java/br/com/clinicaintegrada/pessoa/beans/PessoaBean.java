package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.PessoaDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Mask;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PessoaBean implements Serializable {

    private Pessoa pessoa = new Pessoa();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String masc;
    private String maxl;
    private List<Pessoa> listaPessoa = new ArrayList();

    public PessoaBean() {
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getMasc() {
        return masc;
    }

    public void setMasc(String masc) {
        this.masc = masc;
    }

    public String salvar() {
        DaoInterface di = new Dao();
        if (pessoa.getId() == -1) {
            di.openTransaction();
            if (di.save(pessoa)) {
                di.commit();
            } else {
                di.rollback();
            }
        } else {
            di.openTransaction();
            if (di.update(pessoa)) {
                di.commit();
            } else {
                di.rollback();
            }
        }
        return null;
    }

    public String novo() {
        setPessoa(pessoa = new Pessoa());
        return "pessoa";
    }

    public String excluir() {
        DaoInterface di = new Dao();
        if (pessoa.getId() != -1) {
            di.openTransaction();
            if (di.delete(pessoa)) {
                di.commit();
            } else {
                di.rollback();
            }
        }
        setPessoa(pessoa = new Pessoa());
        return "pesquisaPessoa";
    }

    public void CarregarPessoa() {
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public String pesquisarPessoa() {
        Sessions.put("urlRetorno", "agenda");
        return "pesquisaPessoa";
    }

    public String editar(Pessoa p) {
        pessoa = p;
        Sessions.put("pessoaPesquisa", pessoa);
        Sessions.put("linkClicado", true);
        pessoa = new Pessoa();
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        if (Sessions.exists("urlRetorno")) {
            return Sessions.getString("urlRetorno");
        } else {
            return null;
        }
    }

    public String getColocarMascara() {
        if (porPesquisa.equals("telefone1")) {
            masc = "telefone";
        } else {
            masc = "";
        }
        return masc;
    }

    public String getColocarMaxlenght() {
        if (porPesquisa.equals("telefone1")) {
            maxl = "14";
        } else {
            maxl = "50";
        }
        return maxl;
    }

    public List<Pessoa> getListaPessoa() {
        PessoaDao pessoaDao = new PessoaDao();
        if (descPesquisa.equals("")) {
            listaPessoa.clear();
            return listaPessoa;
        } else {
            listaPessoa = pessoaDao.pesquisarPessoa(descPesquisa, porPesquisa, comoPesquisa, SessaoCliente.get().getId());
            return listaPessoa;
        }
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }

    public String getMascaraPesquisa() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }
}
