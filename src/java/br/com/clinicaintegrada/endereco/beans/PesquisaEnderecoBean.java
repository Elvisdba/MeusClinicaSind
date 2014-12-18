package br.com.clinicaintegrada.endereco.beans;

import br.com.clinicaintegrada.fichamedica.Avaliacao;
import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.endereco.dao.EnderecoDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.PF;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class PesquisaEnderecoBean implements Serializable {

    private String cep;
    private String TCase;
    private List<Endereco> listEndereco;

    @PostConstruct
    public void init() {
        cep = "";
        TCase = "";
        listEndereco = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        Sessions.remove("pesquisaEnderecoBean");
    }

    public void find() {
        listEndereco.clear();
    }

    public void put(String t) {
        TCase = t;
    }

    /**
     * @param o Endereco
     * @return
     */
    public String put(Object o) {
        Dao dao = new Dao();
        o = (Endereco) dao.rebind(o);
        switch (TCase) {
            case "pessoaFisica":
                Sessions.put("enderecoPesquisa", o);
                PF.closeDialog("dlg_pesquisa_endereco");
                PF.update("form_pessoa_fisica:");
            case "pessoaJuridica":
                Sessions.put("enderecoPesquisa", o);
                PF.closeDialog("dlg_pesquisa_endereco");
                PF.update("formPessoaJuridica:");
            case "resgate":
                Sessions.put("enderecoPesquisa", o);
                PF.closeDialog("dlg_pesquisa_endereco");
                PF.update("form_r:");
        }
        return TCase;
    }

    public List<Endereco> getListEndereco() {
        if (!cep.isEmpty()) {
            if (listEndereco.isEmpty()) {
                EnderecoDao enderecoDao = new EnderecoDao();
                listEndereco = enderecoDao.pesquisaEnderecoPorCep(cep);
            }
        }
        return listEndereco;
    }

    public void setListEndereco(List<Endereco> listEndereco) {
        this.listEndereco = listEndereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTCase() {
        return TCase;
    }

    public void setTCase(String TCase) {
        this.TCase = TCase;
    }
}
