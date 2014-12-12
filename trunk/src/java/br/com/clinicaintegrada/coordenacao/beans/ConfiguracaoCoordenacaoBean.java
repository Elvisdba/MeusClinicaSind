package br.com.clinicaintegrada.coordenacao.beans;

import br.com.clinicaintegrada.coordenacao.ConfiguracaoCoordenacao;
import br.com.clinicaintegrada.coordenacao.dao.ConfiguracaoCoordenacaoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SlideEndEvent;

@ManagedBean
@SessionScoped
public class ConfiguracaoCoordenacaoBean implements Serializable {

    private ConfiguracaoCoordenacao configuracaoCoordenacao;

    @PostConstruct
    public void init() {
        configuracaoCoordenacao = new ConfiguracaoCoordenacao();
        configuracaoCoordenacao = new ConfiguracaoCoordenacaoDao().findByCliente(SessaoCliente.get().getId());
        if (configuracaoCoordenacao == null) {
            configuracaoCoordenacao = new ConfiguracaoCoordenacao();
        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("configuracaoCoordenacaoBean");
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (configuracaoCoordenacao.getId() == -1) {
            configuracaoCoordenacao.setCliente(SessaoCliente.get());
            if (dao.save(configuracaoCoordenacao, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        "ID: " + configuracaoCoordenacao.getId() + "]"
                );
            } else {
                Messages.warn("Erro", "Ao adicionar registro!");
            }
        } else {
            ConfiguracaoCoordenacao cc = (ConfiguracaoCoordenacao) dao.find(configuracaoCoordenacao);
            String beforeUpdate = "ID: " + cc.getId() + "]";
            if (dao.update(configuracaoCoordenacao, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        "ID: " + configuracaoCoordenacao.getId() + "]"
                );
            } else {
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public ConfiguracaoCoordenacao getConfiguracaoCoordenacao() {
        return configuracaoCoordenacao;
    }

    public void setConfiguracaoCoordenacao(ConfiguracaoCoordenacao configuracaoCoordenacao) {
        this.configuracaoCoordenacao = configuracaoCoordenacao;
    }

    public void onSlideEndAgendamento(SlideEndEvent event) {
        int value = event.getValue();
        configuracaoCoordenacao.setAgendamentoMaxMesesAgenda(value);
        save();
    }

    public void onSlideEndEscala(SlideEndEvent event) {
        int value = event.getValue();
        configuracaoCoordenacao.setEscalaMaxMesesEscala(value);
        save();
    }
}
