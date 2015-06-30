package br.com.clinicaintegrada.configuracao.beans;

import br.com.clinicaintegrada.configuracao.ConfiguracaoFinanceiro;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.configuracao.dao.ConfiguracaoFinanceiroDao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ConfiguracaoFinanceiroBean implements Serializable {

    private ConfiguracaoFinanceiro configuracaoFinanceiro;

    @PostConstruct
    public void init() {
        configuracaoFinanceiro = new ConfiguracaoFinanceiro();
        configuracaoFinanceiro = new ConfiguracaoFinanceiroDao().findByCliente(SessaoCliente.get().getId());
        if (configuracaoFinanceiro == null) {
            configuracaoFinanceiro = new ConfiguracaoFinanceiro();
        } else {
            Dao dao = new Dao();
            configuracaoFinanceiro = (ConfiguracaoFinanceiro) dao.rebind(configuracaoFinanceiro);
        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("configuracaoFinanceiroBean");
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (configuracaoFinanceiro.getId() == null) {
            configuracaoFinanceiro.setCliente(SessaoCliente.get());
            if (dao.save(configuracaoFinanceiro, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: " + configuracaoFinanceiro.getId() + "]"
                );
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            ConfiguracaoFinanceiro cf = (ConfiguracaoFinanceiro) dao.find(configuracaoFinanceiro);
            String beforeUpdate = "ID: " + cf.getId() + "]";
            if (dao.update(configuracaoFinanceiro, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: " + configuracaoFinanceiro.getId() + "]"
                );
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public ConfiguracaoFinanceiro getConfiguracaoFinanceiro() {
        return configuracaoFinanceiro;
    }

    public void setConfiguracaoFinanceiro(ConfiguracaoFinanceiro configuracaoFinanceiro) {
        this.configuracaoFinanceiro = configuracaoFinanceiro;
    }
}
