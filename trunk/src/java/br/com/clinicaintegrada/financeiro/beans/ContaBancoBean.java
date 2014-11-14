package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.financeiro.Banco;
import br.com.clinicaintegrada.financeiro.ContaBanco;
import br.com.clinicaintegrada.financeiro.Plano5;
import br.com.clinicaintegrada.financeiro.dao.ContaBancoDao;
import br.com.clinicaintegrada.financeiro.dao.PlanoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.ListArgumentos;
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
public class ContaBancoBean implements Serializable {

    private ContaBanco contaBanco;
    private Banco banco;
    private String indice;
    private int idBanco;
    private int idPlanoContas;
    private int idFilial;
    private Cidade cidade;
    private Banco Sbanco;
    private Plano5 plano5;
    private boolean salvar;
    private List<ContaBanco> listContaBanco;
    private List<SelectItem> listPlano5Conta;
    private List<SelectItem> listFilial;
    private List<SelectItem> listBancoCompleta;

    @PostConstruct
    public void init() {
        contaBanco = new ContaBanco();
        banco = new Banco();
        indice = "";
        idBanco = 0;
        idPlanoContas = 0;
        idFilial = 0;
        cidade = new Cidade();
        Sbanco = null;
        plano5 = new Plano5();
        salvar = false;
        listContaBanco = new ArrayList<>();
        listPlano5Conta = new ArrayList<>();
        listFilial = new ArrayList<>();
        listBancoCompleta = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("contaBancoBean");
        Sessions.remove("contaBancoPesquisa");
        Sessions.remove("cidadePesquisa");
    }

    public void removeCidade() {
        cidade = new Cidade();
    }

    public void save() {
        Dao dao = new Dao();
        Filial filial = (Filial) dao.find(new Filial(), Integer.parseInt(getListFilial().get(idFilial).getDescription()));
        contaBanco.setFilial(filial);
        if (cidade == null) {
            Messages.warn("Validação", "Pesquise uma Cidade!");
            return;
        }

        if (Integer.parseInt(getListPlano5Conta().get(idPlanoContas).getDescription()) == 0) {
            Messages.warn("Validação", "Registro não pode ser salvo sem Plano de Contas!");
            return;
        }

        Sbanco = (Banco) dao.find(new Banco(), Integer.parseInt(getListBancoCompleta().get(idBanco).getDescription()));
        contaBanco.setCidade(cidade);
        contaBanco.setBanco(Sbanco);

        Logger logger = new Logger();
        dao.openTransaction();
        if (contaBanco.getId() == -1) {
            salvar = true;
            if (!dao.save(contaBanco)) {
                Messages.warn("Erro", "Ao inserir registro!");
                dao.rollback();
                return;
            }
            logger.save(
                    "ID: " + contaBanco.getId()
                    + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                    + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                    + " - Agência: " + contaBanco.getAgencia().trim()
                    + " - C. Conta: " + contaBanco.getConta().trim()
            );
            Messages.info("Sucesso", "Conta salva com Sucesso");
        } else {
            ContaBanco c = (ContaBanco) dao.find(contaBanco);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Filial: (" + c.getFilial().getId() + ") " + c.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + c.getCidade().getId() + ") " + c.getCidade().getCidade() + " - UF: " + c.getCidade().getUf()
                    + " - Banco: (" + c.getBanco().getId() + ") " + c.getBanco().getBanco().trim()
                    + " - Agência: " + c.getAgencia().trim()
                    + " - C. Conta: " + c.getConta().trim();
            if (!dao.update(contaBanco)) {
                Messages.warn("Erro", "Ao atualizar conta banco");
                dao.rollback();
                return;
            }
            logger.update(beforeUpdate,
                    "ID: " + contaBanco.getId()
                    + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                    + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                    + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                    + " - Agência: " + contaBanco.getAgencia().trim()
                    + " - C. Conta: " + contaBanco.getConta().trim()
            );
            Messages.info("Sucesso", "Conta atualizada com Sucesso");
        }
        if (!updatePlano5Conta(dao)) {
            Messages.warn("Erro", "Erro ao atualizar Plano 5!");
            dao.rollback();
            return;
        }
        dao.commit();
        clear();
    }

    public boolean updatePlano5Conta(Dao dao) {
        plano5 = (Plano5) dao.find(new Plano5(), Integer.parseInt(getListPlano5Conta().get(idPlanoContas).getDescription()));
        if (plano5.getId() != -1) {
            plano5.setConta(contaBanco.getBanco().getBanco() + " - " + contaBanco.getConta());
            plano5.setContaBanco(contaBanco);
            if (!dao.update(plano5)) {
                return false;
            }
        }
        return true;
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (contaBanco.getId() != -1) {
            dao.openTransaction();
            if (plano5 != null) {
                plano5.setContaBanco(null);
                plano5.setConta("??????????????????");
                if (!dao.update(plano5)) {
                    Messages.warn("Erro", "Erro ao atualizar Plano 5!");
                    dao.rollback();
                    return;
                }
            }
            if (!dao.delete(contaBanco)) {
                Messages.warn("Erro", "Conta não pode ser Excluida!");
                dao.rollback();
                return;
            } else {
                logger.delete(
                        "ID: " + contaBanco.getId()
                        + " - Filial: (" + contaBanco.getFilial().getId() + ") " + contaBanco.getFilial().getFilial().getPessoa().getNome()
                        + " - Cidade: (" + contaBanco.getCidade().getId() + ") " + contaBanco.getCidade().getCidade() + " - UF: " + contaBanco.getCidade().getUf()
                        + " - Banco: (" + contaBanco.getBanco().getId() + ") " + contaBanco.getBanco().getBanco().trim()
                        + " - Agência: " + contaBanco.getAgencia().trim()
                        + " - C. Conta: " + contaBanco.getConta().trim()
                );
                Messages.info("Sucesso", "Conta Excluida com Sucesso!");
                dao.commit();

            }
            clear();
        }
    }

    public List getListaBanco() {
//       Pesquisa pesquisa = new Pesquisa();
        ListArgumentos por = new ListArgumentos();
        por.addObject(new ArrayList<List>(), "numero", "S");
        por.addObject(new ArrayList<List>(), "banco", "S");
        List result = null;
        return result;
    }

    public List<ContaBanco> getListContaBanco() {
        if (listContaBanco.isEmpty()) {
            ContaBancoDao contaBancoDao = new ContaBancoDao();
            Dao dao = new Dao();
            listContaBanco = contaBancoDao.findAllByCliente(SessaoCliente.get().getId());
        }
        return listContaBanco;
    }

    public void setListContaBanco(List<ContaBanco> listContaBanco) {
        this.listContaBanco = listContaBanco;
    }

    public String edit(ContaBanco cb) {
        contaBanco = cb;
        PlanoDao planoDao = new PlanoDao();
        plano5 = planoDao.findPlano5ByContaBanco(contaBanco.getId(), SessaoCliente.get().getId());
        if (plano5 != null && plano5.getId() != -1) {
            int qnt = getListPlano5Conta().size();
            for (int i = 0; i < qnt; i++) {
                if (Integer.parseInt(getListPlano5Conta().get(i).getDescription()) == plano5.getId()) {
                    idPlanoContas = i;
                    break;
                }
            }
        }
        if (contaBanco != null && contaBanco.getBanco().getId() != -1) {
            int qnt = getListBancoCompleta().size();
            for (int i = 0; i < qnt; i++) {
                if (Integer.valueOf(getListBancoCompleta().get(i).getDescription()) == contaBanco.getBanco().getId()) {
                    idBanco = i;
                    break;
                }
            }
        }
        Sessions.put("contaBancoPesquisa", contaBanco);
        Sessions.put("linkClicado", true);
        if ((Sessions.getString("urlRetorno")).equals("menuFinanceiro")) {
            return "contaBanco";
        }
        return Sessions.getString("urlRetorno");

    }

    public List<SelectItem> getListBancoCompleta() {
        if (!listBancoCompleta.isEmpty()) {
            Dao dao = new Dao();
            List<Banco> list = (List<Banco>) dao.list(new Banco());
            listBancoCompleta.add(new SelectItem(0, "Nenhum", "0"));
            for (int i = 1; i < listBancoCompleta.size(); i++) {
                listBancoCompleta.add(new SelectItem(i + 1, list.get(i).getNumero() + " - " + list.get(i).getBanco(), Integer.toString(list.get(i).getId())));
            }
        }
        return listBancoCompleta;
    }

    public void setListBancoCompleta(List<SelectItem> listBancoCompleta) {
        this.listBancoCompleta = listBancoCompleta;
    }

    public List<SelectItem> getListFilial() {
        if (listFilial.isEmpty()) {
            Dao dao = new Dao();
            List<Filial> listaFilial = (List<Filial>) dao.list(new Filial(), true);
            for (int i = 0; i < listaFilial.size(); i++) {
                listFilial.add(new SelectItem(i, listaFilial.get(i).getFilial().getPessoa().getNome(), Integer.toString(listaFilial.get(i).getId())));
            }
            if (contaBanco != null) {
                if (contaBanco.getFilial().getId() != -1) {
                    for (int i = 0; i < listFilial.size(); i++) {
                        if (Integer.parseInt(listFilial.get(i).getDescription()) == contaBanco.getFilial().getId()) {
                            setIdFilial(i);
                            break;
                        }
                    }
                }
            }
        }
        return listFilial;
    }

    public List<SelectItem> getListPlano5Conta() {
        if (listPlano5Conta.isEmpty()) {
            PlanoDao planoDao = new PlanoDao();
            List planoContas;
            if ((contaBanco != null) && (contaBanco.getId() != -1) && salvar == false) {
                planoContas = planoDao.findPlano5ContaComID(contaBanco.getId());
            } else {
                planoContas = planoDao.findPlano5InContaRotina(SessaoCliente.get().getId());
            }
            listPlano5Conta.add(new SelectItem(0, "Nenhum", "0"));
            for (int i = 1; i < planoContas.size(); i++) {
                listPlano5Conta.add(new SelectItem(i + 1, ((Plano5) planoContas.get(i)).getConta(), Integer.toString(((Plano5) planoContas.get(i)).getId())));
            }
        }
        return listPlano5Conta;
    }

    public void setListPlano5Conta(List<SelectItem> listPlano5Conta) {
        this.listPlano5Conta = listPlano5Conta;
    }

    public void clear() {
        contaBanco = new ContaBanco();
        idBanco = 0;
        idPlanoContas = 0;
        salvar = false;
        cidade = new Cidade();
        Sessions.remove("contaBancoPesquisa");
        listContaBanco.clear();
        listPlano5Conta.clear();
    }

    public String novoGeral() {
        contaBanco = new ContaBanco();
        cidade = new Cidade();
        idBanco = 0;
        idPlanoContas = 0;
        salvar = false;
        Sessions.remove("contaBancoPesquisa");
        listContaBanco.clear();
        return "contaBanco";
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public ContaBanco getContaBanco() {
        return contaBanco;
    }

    public void setContaBanco(ContaBanco contaBanco) {
        this.contaBanco = contaBanco;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public int getIdPlanoContas() {
        return idPlanoContas;
    }

    public void setIdPlanoContas(int idPlanoContas) {
        this.idPlanoContas = idPlanoContas;
    }

    public Cidade getCidade() {
        if (Sessions.exists("cidadePesquisa")) {
            cidade = (Cidade) Sessions.getObject("cidadePesquisa", true);
        } else if (contaBanco.getId() != -1) {
            cidade = contaBanco.getCidade();
        }
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public void setListFilial(List<SelectItem> listFilial) {
        this.listFilial = listFilial;
    }
}