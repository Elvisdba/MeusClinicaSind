package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
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

@ManagedBean
@SessionScoped
public class ServicosBean implements Serializable {

    private Servicos servicos;
    private List<Servicos> listServicos;
    private List<SelectItem> listSubGrupo;
    private List<SelectItem> listGrupo;
    private List<SelectItem> listPeriodo;
    private String porPesquisa;
    private String comoPesquisa;
    private String descPesquisa;
    private final String messageClass = "Serviço";

    @PostConstruct
    public void init() {
        servicos = new Servicos();
        porPesquisa = "descricao";
        comoPesquisa = "P";
        descPesquisa = "";
        listServicos = new ArrayList<>();
        listGrupo = new ArrayList<>();
        listSubGrupo = new ArrayList<>();
        listPeriodo = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("servicosBean");
        Sessions.remove("pesquisaPlano");
        Sessions.remove("pesquisaServicos");
        Sessions.remove("contaCobrancaPesquisa");
    }

    public void clear() {
        Sessions.remove("servicosBean");
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public Servicos getServicos() {
        if (Sessions.exists("pesquisaServicos")) {
            servicos = (Servicos) Sessions.getObject("pesquisaServicos", true);
        }
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public void acaoInicial() {
        comoPesquisa = "I";
        listServicos.clear();
    }

    public void acaoParcial() {
        comoPesquisa = "P";
        listServicos.clear();
    }

    public void save() {
        if (servicos.getDescricao().equals("")) {
            Messages.warn("Validação", "Informe a " + servicos.getDescricao().getClass().getSimpleName());
            return;
        }
        ServicosDao servicosDao = new ServicosDao();
        Dao dao = new Dao();
        Logger logger = new Logger();
        dao.openTransaction();
        if (servicos.getId() == -1) {
            servicos.setCliente(SessaoCliente.get());
            if (servicosDao.existsServicos(servicos)) {
                Messages.warn("Validação", messageClass + " já cadastrado!");
                return;
            }
            if (dao.save(servicos)) {
                logger.save(
                        "ID: " + servicos.getId()
                        + " - Descrição: " + servicos.getDescricao()
                );
                Messages.info("Sucesso", messageClass + " inserido");
                dao.commit();
            } else {
                Messages.warn("Erro", "Ao inserir " + messageClass);
                dao.rollback();
            }
        } else {
            Servicos s = (Servicos) dao.find(servicos);
            String beforeUpdate
                    = "ID: " + s.getId()
                    + " - Descrição: " + s.getDescricao();
            if (dao.update(servicos)) {
                logger.update(beforeUpdate,
                        "ID: " + servicos.getId()
                        + " - Descrição: " + servicos.getDescricao()
                );
                dao.commit();
                Messages.info("Sucesso", messageClass + " atualizado");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao atualizar " + messageClass);
            }
        }
    }

    public String edit(Servicos s) {
        servicos = s;
        Sessions.put("pesquisaServicos", servicos);
        Sessions.put("linkClicado", true);
        if (Sessions.exists("urlRetorno")) {
            return "servicos";
        } else {
            return Sessions.getString("urlRetorno");
        }
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        dao.openTransaction();
        if (dao.delete(servicos)) {
            logger.delete(
                    "ID: " + servicos.getId()
                    + " - Descrição: " + servicos.getDescricao()
            );
            dao.commit();
            Messages.info("Sucesso", messageClass + " excluído");
        } else {
            dao.rollback();
            Messages.warn("Erro", "Ao remover " + messageClass);
        }
        clear();
    }

    public String pesquisaContaCobranca() {
        Sessions.put("urlRetorno", "servicos");
        return "pesquisaContaCobranca";
    }

    public String pesquisarServicos() {
        Sessions.put("urlRetorno", "servicos");
        descPesquisa = "";
        return "pesquisaServicos";
    }

    public List<Servicos> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDao servicosDao = new ServicosDao();
            if (porPesquisa.equals("I")) {
                servicosDao.setType(1);
            } else {
                servicosDao.setType(2);
            }
            listServicos = (List<Servicos>) servicosDao.findServicosByDescricao(SessaoCliente.get().getId(), descPesquisa);
        }
        return listServicos;
    }
}
