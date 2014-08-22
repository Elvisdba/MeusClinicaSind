package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Profissao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Dao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ProfissaoBean extends PesquisarProfissaoBean implements Serializable {

    private Profissao prof;
    private String s_cbo;
    private String s_profissao;

    @PostConstruct
    public void init() {
        prof = new Profissao();
        s_cbo = "";
        s_profissao = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("profissaoBean");
        Sessions.remove("pesquisarProfissaoBean");
    }

    public void clearProfissao() {
        Sessions.remove("profissaoBean");
        prof = new Profissao();
        super.profissao = new Profissao();
    }

    public void editarProfissao(Profissao pr) {
        prof = pr;
        super.getListProfissao().clear();
    }

    public void saveProfissao() {

        if (prof.getProfissao().equals("")) {
            Messages.warn("Validação", "Digite a profissão!");
            return;
        }
        Logger logger = new Logger();
        Dao dao = new Dao();
        if (prof.getId() == -1) {
            if (dao.existDescriptionInField(prof.getProfissao(), "profissao", "Profissao")) {
                Messages.warn("Validação", "Profissão já cadastrada!");
                return;
            }
            if (!prof.getCbo().equals("")) {
                if (dao.existDescriptionInField(prof.getCbo(), "cbo", "Profissao")) {
                    Messages.warn("Validação", "CBO já existe!");
                    return;
                }
            }
            dao.openTransaction();
            if (dao.save(prof)) {
                Messages.info("Sucesso!", "Profissão salva com sucesso");
                dao.commit();
                logger.save(
                        "ID: " + prof.getId()
                        + " - Profissão: " + prof.getProfissao()
                        + " - CBO: " + prof.getCbo()
                );
            } else {
                Messages.warn("Erro", "Erro ao salvar profissão!");
                dao.rollback();
                return;
            }
        } else {
            Profissao p = (Profissao) dao.find(prof);
            dao.openTransaction();
            String beforeUpdate
                    = "ID: " + p.getId()
                    + " - Profissão: " + p.getProfissao()
                    + " - CBO: " + p.getCbo();
            if (dao.update(prof)) {
                dao.commit();
                logger.update(beforeUpdate,
                        "ID: " + prof.getId()
                        + " - Profissão: " + prof.getProfissao()
                        + " - CBO: " + prof.getCbo()
                );
                Messages.info("Sucesso!", "Profissão atualizada com sucesso");
            } else {
                Messages.warn("Erro", "Erro ao atualizar profissão!");
                dao.rollback();
                return;
            }
        }
        super.getListProfissao().clear();
        prof = new Profissao();
    }

    public void deleteProfissao() {
        if (prof.getId() == -1) {
            Messages.warn("Erro", "Selecione uma profissão para ser excluída!");
            return;
        }
        DaoInterface di = new Dao();
        di.openTransaction();
        if (di.delete(prof)) {
            Logger logger = new Logger();
            logger.delete(
                    "ID: " + prof.getId()
                    + " - Profissão: " + prof.getProfissao()
                    + " - CBO: " + prof.getCbo()
            );
            Messages.info("Sucesso!", "Profissão deletada com sucesso!");
            di.commit();
        } else {
            Messages.warn("Erro", "Erro ao deletar profissão!");
            di.rollback();
        }
        super.getListProfissao().clear();
        prof = new Profissao();
    }

    public String getS_cbo() {
        s_cbo = super.profissao.getCbo();
        return s_cbo;
    }

    public void setS_cbo(String s_cbo) {
        this.s_cbo = s_cbo;
    }

    public String getS_profissao() {
        s_profissao = super.profissao.getProfissao();
        return s_profissao;
    }

    public void setS_profissao(String s_profissao) {
        this.s_profissao = s_profissao;
    }

    public Profissao getProf() {
        return prof;
    }

    public void setProf(Profissao prof) {
        this.prof = prof;
    }
}
