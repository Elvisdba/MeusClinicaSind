package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.financeiro.ServicoContaCobranca;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.dao.ServicoContaCobrancaDao;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ServicoContaCobrancaBean implements Serializable {

    private ServicoContaCobranca servicoContaCobranca = new ServicoContaCobranca();
    private Servicos servico = new Servicos();
    private TipoServico tipoServico = new TipoServico();
    private ContaCobranca contaCobranca = new ContaCobranca();
    private int idServicos = 0;
    private int idContaCobranca = 0;
    private int idTipoServico = 0;
    private List<ServicoContaCobranca> listServicoCobranca = new ArrayList();

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdContaCobranca() {
        return idContaCobranca;
    }

    public void setIdContaCobranca(int idContaCobranca) {
        this.idContaCobranca = idContaCobranca;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public String add() {
        ServicoContaCobrancaDao servicoContaCobrancaDao = new ServicoContaCobrancaDao();
        Dao dao = new Dao();
        Logger logger = new Logger();
        if ((servicoContaCobranca != null) && (servicoContaCobranca.getId() == -1)) {
            servico = (Servicos) dao.find(new Servicos(), Integer.parseInt(getListServico().get(getIdServicos()).getDescription()));
            tipoServico = (TipoServico) dao.find(new TipoServico(), Integer.parseInt(getListTipoServico().get(getIdTipoServico()).getDescription()));
            contaCobranca = (ContaCobranca) dao.find(new ContaCobranca(), Integer.parseInt(getListContaCobranca().get(getIdContaCobranca()).getDescription()));
            List serv = servicoContaCobrancaDao.pesquisaServPorIdServIdTipoServ(servico.getId(), tipoServico.getId());
            if (serv.isEmpty()) {
                servicoContaCobranca.setServicos(servico);
                servicoContaCobranca.setTipoServico(tipoServico);
                servicoContaCobranca.setContaCobranca(contaCobranca);
                if (dao.save(servicoContaCobranca, true)) {
                    logger.save(
                            "ID: " + servicoContaCobranca.getId()
                            + " - Serviço: (" + servicoContaCobranca.getServicos().getId() + ") " + servicoContaCobranca.getServicos().getDescricao()
                            + " - Tipo Serviço: (" + servicoContaCobranca.getTipoServico().getId() + ") " + servicoContaCobranca.getTipoServico().getDescricao()
                            + " - Conta Cobrança: " + servicoContaCobranca.getContaCobranca().getId()
                    );
                    Messages.info("Sucesso", "Serviço adicionado!");
                } else {
                    Messages.warn("Erro", "Erro ao Salvar!");
                }
            } else {
                Messages.warn("Validação", "Serviço já existente!");
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
        listServicoCobranca.clear();
        return null;
    }

    public String delete(ServicoContaCobranca scc) {
        Dao dao = new Dao();
        Logger logger = new Logger();
        servicoContaCobranca = scc;
        if (servicoContaCobranca.getId() != -1) {
            if (dao.delete(servicoContaCobranca, true)) {
                logger.delete(
                        "ID: " + servicoContaCobranca.getId()
                        + " - Serviço: (" + servicoContaCobranca.getServicos().getId() + ") " + servicoContaCobranca.getServicos().getDescricao()
                        + " - Tipo Serviço: (" + servicoContaCobranca.getTipoServico().getId() + ") " + servicoContaCobranca.getTipoServico().getDescricao()
                        + " - Conta Cobrança: " + servicoContaCobranca.getContaCobranca().getId()
                );
                Messages.info("Sucesso", "Serviço excluido!");
            } else {
                Messages.warn("Erro", "Erro ao excluir serviço!");
            }
        }
        servicoContaCobranca = new ServicoContaCobranca();
        listServicoCobranca.clear();
        return null;
    }

    public String clear() {
        setIdContaCobranca(0);
        setIdServicos(0);
        setIdTipoServico(0);
        listServicoCobranca.clear();
        return "servicoContaCobranca";
    }

    public void refreshForm() {
    }

    public List<SelectItem> getListServico() {
        List<SelectItem> selectItems = new ArrayList<>();
        ServicosDao servicosDao = new ServicosDao();
        List list = servicosDao.findServicos(SessaoCliente.get().getId());
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i, (String) ((Servicos) list.get(i)).getDescricao(), Integer.toString(((Servicos) list.get(i)).getId())));
        }
        return selectItems;
    }

    public List<SelectItem> getListContaCobranca() {
        List<SelectItem> selectItems = new ArrayList<>();
        Dao dao = new Dao();
        List<ContaCobranca> list = (List<ContaCobranca>) dao.list(new ContaCobranca());
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i,
                    list.get(i).getCodCedente() + " - "
                    + list.get(i).getContaBanco().getBanco().getBanco(),
                    Integer.toString(list.get(i).getId())));

        }
        return selectItems;
    }

    public List<SelectItem> getListTipoServico() {
        List<SelectItem> selectItems = new ArrayList<>();
        Dao dao = new Dao();
        List<TipoServico> list = (List<TipoServico>) dao.list(new TipoServico());
        for (int i = 0; i < list.size(); i++) {
            selectItems.add(new SelectItem(i,
                    list.get(i).getDescricao(),
                    Integer.toString(list.get(i).getId())));
        }
        return selectItems;
    }

    public ServicoContaCobranca getServicoContaCobranca() {
        return servicoContaCobranca;
    }

    public void setServicoContaCobranca(ServicoContaCobranca servicoContaCobranca) {
        this.servicoContaCobranca = servicoContaCobranca;
    }

    public List<ServicoContaCobranca> getListServicoCobranca() {
        if (listServicoCobranca.isEmpty()) {
            ServicoContaCobrancaDao db = new ServicoContaCobrancaDao();
            listServicoCobranca = db.listAllByCliente(SessaoCliente.get().getId());
        }
        return listServicoCobranca;
    }

    public void setListServicoCobranca(List<ServicoContaCobranca> listServicoCobranca) {
        this.listServicoCobranca = listServicoCobranca;
    }
}
