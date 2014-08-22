package br.com.clinicaintegrada.sistema.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.sistema.Combustivel;
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
public class CombustivelBean implements Serializable {

    private Combustivel combustivel;
    private List<Combustivel> listCombustivel;

    @PostConstruct
    public void init() {
        combustivel = new Combustivel();
        listCombustivel = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("combustivelBean");
        Sessions.remove("combustivelPesquisa");
    }

    public void clear() {
        Sessions.remove("combustivelBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (combustivel.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        if (combustivel.getValorLitro() <= 0) {
            Messages.warn("Validação", "Informar o valor do litro!");
            return;
        }
        Logger logger = new Logger();
        if (combustivel.getId() == -1) {
            for (int i = 0; i < getListCombustivel().size(); i++) {
                if (listCombustivel.get(i).getDescricao().toUpperCase().equals(combustivel.getDescricao().toUpperCase())) {
                    Messages.warn("Validação", "Tipo de combustível já cadastrado!");
                    return;
                }
            }
            if (dao.save(combustivel, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + combustivel.getId()
                        + " - Descrição: [" + combustivel.getDescricao()
                        + " - Valor do litro: " + combustivel.getValorLitroString()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        } else {
            Combustivel c = (Combustivel) dao.find(combustivel);
            String beforeUpdate
                    = " ID: " + c.getId()
                    + " - Descrição: [" + c.getDescricao()
                    + " - Valor do litro: " + c.getValorLitroString();
            if (dao.update(combustivel, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.update(beforeUpdate,
                        " ID: " + combustivel.getId()
                        + " - Descrição: [" + combustivel.getDescricao()
                        + " - Valor do litro: " + combustivel.getValorLitroString()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        }
    }

    public void delete(Combustivel tdp) {
        combustivel = tdp;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (combustivel.getId() != -1) {
            if (dao.delete(combustivel, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " ID: " + combustivel.getId()
                        + " - Descrição: [" + combustivel.getDescricao()
                        + " - Valor do litro: " + combustivel.getValorLitroString()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }
    }

    public void edit(Combustivel c) {
        combustivel = c;
    }

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public List<Combustivel> getListCombustivel() {
        if (listCombustivel.isEmpty()) {
            Dao dao = new Dao();
            listCombustivel = (List<Combustivel>) dao.list(new Combustivel(), true);
        }
        return listCombustivel;
    }

    public void setListCombustivel(List<Combustivel> listCombustivel) {
        this.listCombustivel = listCombustivel;
    }

}
