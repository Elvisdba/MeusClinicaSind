package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.ContaBanco;
import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.financeiro.Layout;
import br.com.clinicaintegrada.financeiro.dao.ContaCobrancaDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
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
public class ContaCobrancaBean {

    private ContaCobranca contaCobranca;
    private List<ContaCobranca> listaContaCobranca;
    private List<SelectItem> listLayout;
    private int idLayout;
    private String repasse;
    private String codigoCedente;
    private boolean limpar;

    @PostConstruct
    public void init() {
        contaCobranca = new ContaCobranca();
        listaContaCobranca = new ArrayList<>();
        listLayout = new ArrayList<>();
        idLayout = 0;
        repasse = "0.0";
        codigoCedente = "";
        limpar = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("contaCobrancaBean");
        Sessions.remove("contaCobrancaPesquisa");
    }

    public void save() {
        if (getListLayout().isEmpty()) {
            Messages.warn("Validação", "Nenhum layout cadastrado!");
            return;
        }
        if (contaCobranca.getContaBanco().getBanco().getBanco().isEmpty()) {
            Messages.warn("Validação", "Atenção, é preciso pesquisar um banco!");
            return;
        }
        contaCobranca.setCodigoSindical(codigoCedente);
        if ((contaCobranca.getCodCedente().isEmpty()) || (contaCobranca.getCodCedente().equals("0"))) {
            Messages.warn("Validação", "Digite um código cedente!");
            return;
        }

        if (contaCobranca.getCedente().isEmpty()) {
            Messages.warn("Validação", "Digite um cedente!");
            return;
        }

        if (contaCobranca.getLocalPagamento().isEmpty()) {
            Messages.warn("Validação", "Local de pagamento não pode ser nulo!");
            return;
        }

        if ((contaCobranca.getBoletoInicial().equals("0")) || (contaCobranca.getBoletoInicial().isEmpty())) {
            Messages.warn("Validação", "Boleto Inicial está em branco!");
            return;
        }

        if (contaCobranca.getMoeda().isEmpty()) {
            Messages.warn("Validação", "O campo moeda está em branco!");
            return;
        }

        if (contaCobranca.getEspecieMoeda().isEmpty()) {
            Messages.warn("Validação", "O espécie campo moeda está em branco!");
            return;
        }

        if (contaCobranca.getEspecieDoc().isEmpty()) {
            Messages.warn("Validação", "Digite uma espécie de documento!");
            return;
        }

        if (contaCobranca.getAceite().isEmpty()) {
            Messages.warn("Validação", "Digite um aceite!");
            return;
        }
        Dao dao = new Dao();
        Layout layout = (Layout) dao.find(new Layout(), Integer.valueOf(getListLayout().get(idLayout).getDescription()));
        contaCobranca.setLayout(layout);
        contaCobranca.setSicasSindical("");
        ContaCobrancaDao contaCobrancaDao = new ContaCobrancaDao();
        Logger logger = new Logger();
        if (contaCobranca.getId() == -1) {
            contaCobranca.setCliente(SessaoCliente.get());
            if (contaCobrancaDao.existContaCobranca(contaCobranca)) {
                Messages.warn("Validação", "Conta Cobrança já cadastrada!");
                return;
            } else {
                if (dao.save(contaCobranca, true)) {
                    logger.novo("Novo Registro", " ID: " + contaCobranca.getId() + " Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                    Messages.info("Sucesso", "Registro inserido");
                } else {
                    Messages.warn("Erro", "Ao inserir registro!");
                }
            }
        } else {
            ContaCobranca conta = new ContaCobranca();
            ContaCobranca cc = (ContaCobranca) dao.find(contaCobranca);
            String antes = conta.getId() + " Banco: " + cc.getContaBanco().getBanco().getBanco() + " - Agência: " + cc.getContaBanco().getAgencia() + " - Conta: " + cc.getContaBanco().getConta() + " - Cedente: " + cc.getCedente() + " - Código Cedente: " + cc.getCodCedente();
            if (dao.update(contaCobranca, true)) {
                logger.save(antes + " - para ID: " + contaCobranca.getId() + " Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
        limpar = false;
    }

    public String clear() {
        contaCobranca = new ContaCobranca();
        listaContaCobranca.clear();
        idLayout = 0;
        repasse = "0.0";
        codigoCedente = "";
        limpar = false;
        return "contaCobranca";
    }

    public void limpar() {
        if (limpar == true) {
            clear();
        }
    }

    public void delete() {
        Dao dao = new Dao();
        if (contaCobranca.getId() != -1) {
            Logger logger = new Logger();
            if (dao.delete(contaCobranca, true)) {
                logger.delete(" ID: " + contaCobranca.getId() + " - Banco: " + contaCobranca.getContaBanco().getBanco().getBanco() + " - Agência: " + contaCobranca.getContaBanco().getAgencia() + " - Conta: " + contaCobranca.getContaBanco().getConta() + " - Cedente: " + contaCobranca.getCedente() + " - Código Cedente: " + contaCobranca.getCodCedente());
                Messages.info("Sucesso", "Registro removido");
                limpar = true;
            } else {
                Messages.warn("Erro", "Não foi possível excluir esse cadastro. Verifique se há vínculos externos!");
            }
        }
    }

    public List<ContaCobranca> getListaContaCobranca() {
        if (listaContaCobranca.isEmpty()) {
            ContaCobrancaDao contaCobrancaDao = new ContaCobrancaDao();
            listaContaCobranca = contaCobrancaDao.findAllByCliente(SessaoCliente.get().getId());
        }
        return listaContaCobranca;
    }

    public void setListaContaCobranca(List<ContaCobranca> listaContaCobranca) {
        this.listaContaCobranca = listaContaCobranca;
    }

    public List<SelectItem> getListLayout() {
        if (listLayout.isEmpty()) {
            Dao dao = new Dao();
            List<Layout> list = (List<Layout>) dao.list(new Layout());
            for (int i = 0; i < list.size(); i++) {
                listLayout.add(new SelectItem(i, ((Layout) list.get(i)).getDescricao(), Integer.toString(((Layout) list.get(i)).getId())));
            }
        }
        return listLayout;
    }

    public void setListLayout(List<SelectItem> listLayout) {
        this.listLayout = listLayout;
    }

    public String edit(ContaCobranca c) {
        contaCobranca = c;
        getListLayout();
        for (int i = 0; i < listLayout.size(); i++) {
            if (Integer.parseInt(listLayout.get(i).getDescription()) == contaCobranca.getLayout().getId()) {
                idLayout = i;
                break;
            }
        }
        setCodigoCedente(contaCobranca.getCodigoSindical());
        Sessions.put("contaCobrancaPesquisa", contaCobranca);
        Sessions.put("linkClicado", true);
        if (!Sessions.exists("urlRetorno")) {
            return "contaCobranca";
        } else {
            return (String) Sessions.getObject("urlRetorno");
        }
    }

    public String getRepasse() {
        return repasse;
    }

    public void setRepasse(String repasse) {
        this.repasse = Moeda.substituiVirgula(repasse);
    }

    public ContaCobranca getContaCobranca() {
        if (Sessions.exists("contaCobrancaPesquisa")) {
            contaCobranca = (ContaCobranca) Sessions.getObject("contaCobrancaPesquisa", true);
        }
        if (Sessions.exists("contaBancoPesquisa")) {
            contaCobranca.setContaBanco((ContaBanco) Sessions.getObject("contaBancoPesquisa", true));
        }
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public int getIdLayout() {
        return idLayout;
    }

    public void setIdLayout(int idLayout) {
        this.idLayout = idLayout;
    }

    public String getCodigoCedente() {
        return codigoCedente;
    }

    public void setCodigoCedente(String codigoCedente) {
        this.codigoCedente = codigoCedente;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
