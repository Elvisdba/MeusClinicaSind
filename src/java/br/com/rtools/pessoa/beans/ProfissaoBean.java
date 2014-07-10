//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.logSistema.Logger;
//import br.com.rtools.pessoa.Profissao;
//import br.com.rtools.utilitarios.Dao;
//import br.com.rtools.utilitarios.DaoInterface;
//import br.com.rtools.utilitarios.Message;
//import br.com.rtools.utilitarios.Sessao;
//import br.com.rtools.utilitarios.Dao;
//import java.io.Serializable;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//@ManagedBean
//@SessionScoped
//public class ProfissaoBean extends PesquisarProfissaoBean implements Serializable {
//
//    private Profissao prof;
//    private String s_cbo;
//    private String s_profissao;
//
//    @PostConstruct
//    public void init() {
//        prof = new Profissao();
//        s_cbo = "";
//        s_profissao = "";
//    }
//
//    @PreDestroy
//    public void destroy() {
//        Sessao.remove("profissaoBean");
//    }
//
//    public void clearProfissao() {
//        prof = new Profissao();
//        super.profissao = new Profissao();
//    }
//
//    public void editProfissao(Profissao pr) {
//        //prof = (Profissao) super.getListaProfissao().get(super.getIdIndexProf());
//        prof = pr;//(Profissao) super.getListaProfissao().get(super.getIdIndexProf());
//        super.getListaProfissao().clear();
//    }
//
//    public void saveProfissao() {
//
//        if (prof.getProfissao().equals("")) {
//            Message.warn("Validação", "Digite a profissão!");
//            return;
//        }
//        Logger logger = new Logger();
//        Dao sv = new Dao();
//        DaoInterface di = new Dao();
//        di.openTransaction();
//        if (prof.getId() == -1) {
//            if (sv.descricaoExiste(prof.getProfissao(), "profissao", "Profissao")) {
//                Message.warn("Validação", "Profissão já cadastrada!");
//                return;
//            }
//            if (!prof.getCbo().equals("")) {
//                if (sv.descricaoExiste(prof.getCbo(), "cbo", "Profissao")) {
//                    Message.warn("Validação", "CBO já existe!");
//                    return;
//                }
//            }
//            if (di.save(prof)) {
//                Message.info("Sucesso!", "Profissão salva com sucesso");
//                logger.save(
//                        "ID: " + prof.getId()
//                        + " - Profissão: " + prof.getProfissao()
//                        + " - CBO: " + prof.getCbo()
//                );
//            } else {
//                Message.warn("Erro", "Erro ao salvar profissão!");
//                sv.desfazerTransacao();
//                return;
//            }
//        } else {
//            Profissao p = (Profissao) di.find(prof);
//            String beforeUpdate
//                    = "ID: " + p.getId()
//                    + " - Profissão: " + p.getProfissao()
//                    + " - CBO: " + p.getCbo();
//            if (di.update(prof)) {
//                logger.update(beforeUpdate,
//                        "ID: " + prof.getId()
//                        + " - Profissão: " + prof.getProfissao()
//                        + " - CBO: " + prof.getCbo()
//                );
//                Message.info("Sucesso!", "Profissão atualizada com sucesso");
//            } else {
//                Message.warn("Erro", "Erro ao atualizar profissão!");
//                sv.desfazerTransacao();
//                return;
//            }
//        }
//        di.commit();
//        super.getListaProfissao().clear();
//        prof = new Profissao();
//    }
//
//    public void deleteProfissao() {
//        if (prof.getId() == -1) {
//            Message.warn("Erro", "Selecione uma profissão para ser excluída!");
//            return;
//        }
//        DaoInterface di = new Dao();
//        di.openTransaction();
//        if (di.delete(prof)) {
//            Logger logger = new Logger();
//            logger.delete(
//                    "ID: " + prof.getId()
//                    + " - Profissão: " + prof.getProfissao()
//                    + " - CBO: " + prof.getCbo()
//            );
//            Message.info("Sucesso!", "Profissão deletada com sucesso!");
//            di.commit();
//        } else {
//            Message.warn("Erro", "Erro ao deletar profissão!");
//            di.rollback();
//        }
//        super.getListaProfissao().clear();
//        prof = new Profissao();
//    }
//
//    public String getS_cbo() {
//        s_cbo = super.profissao.getCbo();
//        return s_cbo;
//    }
//
//    public void setS_cbo(String s_cbo) {
//        this.s_cbo = s_cbo;
//    }
//
//    public String getS_profissao() {
//        s_profissao = super.profissao.getProfissao();
//        return s_profissao;
//    }
//
//    public void setS_profissao(String s_profissao) {
//        this.s_profissao = s_profissao;
//    }
//
//    public Profissao getProf() {
//        return prof;
//    }
//
//    public void setProf(Profissao prof) {
//        this.prof = prof;
//    }
//}
