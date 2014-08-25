package br.com.clinicaintegrada.endereco.beans;

import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.endereco.dao.CidadeDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Messages;
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
public class CidadeBean implements Serializable {

    private Cidade cidade;
    private String message;
    private String comoPesquisa;
    private List<Cidade> listCidade;
    private String descricaoCidadePesquisa;
    private String descricaoUFPesquisa;

    @PostConstruct
    public void init() {
        cidade = new Cidade();
        message = "";
        comoPesquisa = "";
        listCidade = new ArrayList<Cidade>();
        descricaoCidadePesquisa = "";
        descricaoUFPesquisa = "";
        PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
        PessoaEndereco pessoaEndereco = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(1, 3);
        if(pessoaEndereco != null) {
            cidade.setUf(pessoaEndereco.getEndereco().getCidade().getUf());
        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("cidadeBean");

    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getMessages() {
        return message;
    }

    public void setMessages(String message) {
        this.message = message;
    }

    public void save() {
        if (cidade.getCidade().isEmpty()) {
            message = "Digite uma Cidade por favor!";
            Messages.warn("Erro", message);
            return;
        }

        DaoInterface di = new Dao();
        Logger log = new Logger();

        di.openTransaction();
        if (cidade.getId() == -1) {
            if (di.save(cidade)) {
                message = "Cidade salva com Sucesso!";
                Messages.info("Salvo", message);
                listCidade.add(cidade);
                log.save("ID: " + cidade.getId() + " - Cidade: " + cidade.getCidade() + " - UF: " + cidade.getUf());
                clear();
            } else {
                message = "Erro ao salvar Cidade!";
                Messages.warn("Erro", message);
                di.rollback();
            }
        } else {
            Cidade c = (Cidade) di.find(cidade);
            String beforeUpdate = "ID: " + c.getId() + " - Cidade: " + c.getCidade() + " - UF: " + c.getUf();
            if (di.update(cidade)) {
                message = "Registro atualizado!";
                Messages.info("Atualizado", message);
                log.update(beforeUpdate, ""
                        + "ID: " + cidade.getId() + " - Cidade: " + cidade.getCidade() + " - UF: " + cidade.getUf()
                );
                clear();
            } else {
                message = "Erro ao atualizar!";
                Messages.warn("Erro", message);
                di.rollback();
            }
        }
        di.commit();
    }

    public void clear() {
        Sessions.remove("cidadeBean");
    }

    public void delete(Cidade ci) {
        Logger log = new Logger();
        DaoInterface di = new Dao();
        if (ci.getId() != -1) {
            di.openTransaction();
            if (di.delete(ci)) {
                message = "Cidade Excluida com Sucesso!";
                listCidade.clear();
                Messages.info("Sucesso", message);
                log.delete("ID: " + ci.getId() + " - Cidade: " + ci.getCidade() + " - UF: " + ci.getUf());
            } else {
                message = "Cidade não pode ser Excluida!";
                Messages.warn("Erro", message);
                di.rollback();
            }
        }
        cidade = new Cidade();
        di.commit();
    }

    public String editPagina(Cidade ci) {
        cidade = ci;
        return null;
    }

    public String edit(Cidade ci) {
        String urlRetorno = null;
        cidade = ci;
        Sessions.put("cidadePesquisa", cidade);
        Sessions.put("linkClicado", true);
        if (Sessions.exists("urlRetorno")) {
            if (!Sessions.getString("urlRetorno").equals("menuPrincipal")) {
                urlRetorno = Sessions.getString("urlRetorno");
            } else {
                PF.openDialog("dlg_save");
            }
        } else {
            PF.openDialog("dlg_save");
        }
        return urlRetorno;
    }

    public void setListCidade(List<Cidade> listCidade) {
        this.listCidade = listCidade;
    }

    public List<Cidade> getListCidade() {
        if (listCidade.isEmpty()) {
            CidadeDao cidadeDao = new CidadeDao();
            listCidade = cidadeDao.pesquisaCidadePorEstadoCidade(cidade.getUf(), cidade.getCidade(), getComoPesquisa());

        }
        return listCidade;
    }

    public void acaoPesquisaInicial() {
        listCidade.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listCidade.clear();
        comoPesquisa = "P";
    }

    public String getDescricaoCidadePesquisa() {
        return descricaoCidadePesquisa;
    }

    public void setDescricaoCidadePesquisa(String descricaoCidadePesquisa) {
        this.descricaoCidadePesquisa = descricaoCidadePesquisa;
    }

    public String getDescricaoUFPesquisa() {
        return descricaoUFPesquisa;
    }

    public void setDescricaoUFPesquisa(String descricaoUFPesquisa) {
        this.descricaoUFPesquisa = descricaoUFPesquisa;
    }
}
