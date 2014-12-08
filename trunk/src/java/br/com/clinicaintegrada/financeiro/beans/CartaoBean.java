package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Cartao;
import br.com.clinicaintegrada.financeiro.Plano5;
import br.com.clinicaintegrada.financeiro.dao.CartaoDao;
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

@ManagedBean
@SessionScoped
public class CartaoBean implements Serializable {

    private Cartao cartao;
    private List<Cartao> listCartao;

    @PostConstruct
    public void init() {
        cartao = new Cartao();
        listCartao = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("cartaoBean");
        Sessions.remove("pesquisaPlano");
    }

    public void save() {
        Dao dao = new Dao();
        if (cartao.getDescricao().isEmpty()) {
            Messages.warn("Erro", "O campo descrição não pode estar vazio!");
            return;
        }
        Logger novoLog = new Logger();
        if (cartao.getId() == -1) {
            cartao.setCliente(SessaoCliente.get());
            CartaoDao cartaoDao = new CartaoDao();
            if (cartaoDao.exist(cartao.getDescricao(), cartao.getPlano5().getId())) {
                Messages.warn("Validação", "Cartão já existe!");
                return;
            }
            if (dao.save(cartao, true)) {
                novoLog.save(
                        "ID: " + cartao.getId()
                        + " - Plano 5: (" + cartao.getPlano5().getId() + ") " + cartao.getPlano5().getConta()
                        + " - Descrição: " + cartao.getDescricao()
                        + " - Dias: " + cartao.getDias()
                        + " - Taxa: " + cartao.getTaxa()
                        + " - Débito/Crédito: " + cartao.getDebitoCredito()
                );
                Messages.info("Sucesso", "Registro inserido");
                listCartao.clear();
                cartao = new Cartao();
            } else {
                Messages.warn("Erro", "Não foi possível salvar este registro!");
            }
        } else {
            Cartao c = (Cartao) dao.find(cartao);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Plano 5: (" + c.getPlano5().getId() + ") " + c.getPlano5().getConta()
                    + " - Descrição: " + c.getDescricao()
                    + " - Dias: " + c.getDias()
                    + " - Taxa: " + c.getTaxa()
                    + " - Débito/Crédito: " + c.getDebitoCredito();
            if (dao.update(cartao, true)) {
                novoLog.update(beforeUpdate,
                        "ID: " + cartao.getId()
                        + " - Plano 5: (" + cartao.getPlano5().getId() + ") " + cartao.getPlano5().getConta()
                        + " - Descrição: " + cartao.getDescricao()
                        + " - Dias: " + cartao.getDias()
                        + " - Taxa: " + cartao.getTaxa()
                        + " - Débito/Crédito: " + cartao.getDebitoCredito()
                );
                Messages.info("Sucesso", "Registro atualizado");
                listCartao.clear();
                cartao = new Cartao();
            } else {
                Messages.warn("Erro", "Não foi possível alterar este registro!");
            }
        }
    }

    public void delete(Cartao c) {
        Dao dao = new Dao();
        Logger novoLog = new Logger();
        if (dao.delete(c, true)) {
            novoLog.delete(
                    "ID: " + c.getId()
                    + " - Plano 5: (" + c.getPlano5().getId() + ") " + c.getPlano5().getConta()
                    + " - Descrição: " + c.getDescricao()
                    + " - Dias: " + c.getDias()
                    + " - Taxa: " + c.getTaxa()
                    + " - Débito/Crédito: " + c.getDebitoCredito()
            );
            Messages.info("Sucesso", "Registro excluído");
            listCartao.clear();
            cartao = new Cartao();
        } else {
            Messages.warn("Erro", "Este registro não pode ser excluido!");
        }
    }

    public void edit(Cartao c) {
        cartao = c;
    }

    public void removePlano() {
        cartao.setPlano5(new Plano5());
    }

    public Cartao getCartao() {
        if (Sessions.exists("pesquisaPlano")) {
            cartao.setPlano5((Plano5) Sessions.getObject("pesquisaPlano", true));
        }
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public List<Cartao> getListCartao() {
        if (listCartao.isEmpty()) {
            CartaoDao cartaoDao = new CartaoDao();
            listCartao = cartaoDao.findAllByCliente(SessaoCliente.get().getId());
        }
        return listCartao;
    }

    public void setListCartao(List<Cartao> listCartao) {
        this.listCartao = listCartao;
    }

}
