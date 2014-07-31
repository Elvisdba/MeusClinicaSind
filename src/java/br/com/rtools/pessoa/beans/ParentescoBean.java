package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.Parentesco;
import br.com.rtools.pessoa.dao.ParentescoDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ParentescoBean {

    private Parentesco parentesco = new Parentesco();
    private String message = "";
    private int idIndex = -1;
    private List<Parentesco> listParentesco = new ArrayList();
    private ArrayList listaImagem = new ArrayList();
    private boolean limpar = false;

    @PostConstruct
    public void init() {
        parentesco = new Parentesco();
        message = "";
        idIndex = -1;
        listParentesco = new ArrayList();
        listaImagem = new ArrayList();
        limpar = false;

    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("parentescoBean");
    }

    public void clear() {
        Sessions.remove("parentescoBean");
    }

    public void save() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (getParentesco().getParentesco().equals("") || getParentesco().getParentesco() == null) {
            setMessage("Digite um nome para o parentesco!");
            Messages.warn("Erro", message);
            return;
        }
        if (getParentesco().getId() == -1) {
            if (dao.save(parentesco, true)) {
                setLimpar(false);
                setMessage("Parentesco salvo com sucesso.");
                Messages.info("Sucesso", message);
                logger.save(
                        "ID: " + parentesco.getId()
                        + " - Parentesco: " + parentesco.getParentesco()
                        + " - Sexo: " + parentesco.getSexo()
                        + " - Com Validade?: " + parentesco.isValidade()
                        + " - Validade: " + parentesco.getNrValidade()
                        + " - Ativo: " + parentesco.isAtivo()
                );
                parentesco = new Parentesco();
            } else {
                setMessage("Erro ao salvar parentesco!");
                Messages.warn("Erro", message);
            }
        } else {
            DaoInterface di = new Dao();
            Parentesco p = (Parentesco) di.find(parentesco);
            String beforeUpdate
                    = "ID: " + p.getId()
                    + " - Parentesco: " + p.getParentesco()
                    + " - Sexo: " + p.getSexo()
                    + " - Com Validade?: " + p.isValidade()
                    + " - Validade: " + p.getNrValidade()
                    + " - Ativo: " + p.isAtivo();
            if (dao.update(parentesco, true)) {
                logger.update(beforeUpdate,
                        "ID: " + parentesco.getId()
                        + " - Parentesco: " + parentesco.getParentesco()
                        + " - Sexo: " + parentesco.getSexo()
                        + " - Com Validade?: " + parentesco.isValidade()
                        + " - Validade: " + parentesco.getNrValidade()
                        + " - Ativo: " + parentesco.isAtivo()
                );
                setMessage("Parentesco atualizado com sucesso.");
                Messages.info("Sucesso", message);
                parentesco = new Parentesco();
            } else {
                setMessage("Erro ao atualizar parentesco!");
                Messages.warn("Erro", message);
            }
        }
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (parentesco.getId() == -1) {
            message = "Selecione um parentesco para ser excluído!";
            Messages.warn("Erro", message);
            return;
        }
        if (dao.delete(parentesco, true)) {
            logger.delete(
                    "ID: " + parentesco.getId()
                    + " - Parentesco: " + parentesco.getParentesco()
                    + " - Sexo: " + parentesco.getSexo()
                    + " - Com Validade?: " + parentesco.isValidade()
                    + " - Validade: " + parentesco.getNrValidade()
                    + " - Ativo: " + parentesco.isAtivo()
            );
            setLimpar(true);
            message = "Parentesco deletado com sucesso!";
            Messages.info("Sucesso", message);
        } else {
            setMessage("Erro ao deletar parentesco!");
            Messages.warn("Erro", message);
        }
        parentesco = new Parentesco();
    }

    public String edit(Parentesco p) {
        parentesco = p;
        return "parentesco";
    }

    public String novo() {
        parentesco = new Parentesco();
        listParentesco.clear();
        return "parentesco";
    }

    public String limpar() {
        if (limpar == true) {
            novo();
        }
        return "parentesco";
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setGrupoCategoria(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List<Parentesco> getListParentesco() {
        ParentescoDao parentescoDao = new ParentescoDao();
        listParentesco = parentescoDao.pesquisaTodos();
        for (int i = 0; i < listParentesco.size(); i++) {
            if (listParentesco.get(i).isAtivo() == true) {
                getListaImagem().add("iconTrue.gif");
            } else {
                getListaImagem().add("iconFalse.gif");
            }
        }
        return listParentesco;
    }

    public void setListParentesco(List<Parentesco> listParentesco) {
        this.listParentesco = listParentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public ArrayList getListaImagem() {
        return listaImagem;
    }

    public void setListaImagem(ArrayList listaImagem) {
        this.listaImagem = listaImagem;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
