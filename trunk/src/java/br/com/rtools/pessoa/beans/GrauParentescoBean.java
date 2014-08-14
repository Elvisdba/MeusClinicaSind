//package br.com.rtools.pessoa.beans;
//
//import br.com.rtools.logSistema.Logger;
//import br.com.rtools.pessoa.GrauParentesco;
////import br.com.rtools.pessoa.dao.GrauParentescoDao;
//import br.com.rtools.seguranca.Cliente;
//import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
//import br.com.rtools.utilitarios.Dao;
//import br.com.rtools.utilitarios.DaoInterface;
//import br.com.rtools.utilitarios.Messages;
//import br.com.rtools.utilitarios.Sessions;
//import java.util.ArrayList;
//import java.util.List;
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//
//@ManagedBean
//@SessionScoped
//public class GrauParentescoBean {
//
//    private GrauParentesco grauParentesco = new GrauParentesco();
//    private String message = "";
//    private int idIndex = -1;
//    private List<GrauParentesco> listGrauParentesco = new ArrayList();
//    private ArrayList listaImagem = new ArrayList();
//    private boolean limpar = false;
//
//    @PostConstruct
//    public void init() {
//        grauParentesco = new GrauParentesco();
//        message = "";
//        idIndex = -1;
//        listGrauParentesco = new ArrayList();
//        listaImagem = new ArrayList();
//        limpar = false;
//
//    }
//
//    @PreDestroy
//    public void destroy() {
//        Sessions.remove("grauParentescoBean");
//    }
//
//    public void clear() {
//        Sessions.remove("grauParentescoBean");
//    }
//
//    public void save() {
//        Dao dao = new Dao();
//        Logger logger = new Logger();
//        Cliente cliente = SessaoCliente.get();
//        GrauParentescoDao grauParentescoDao = new GrauParentescoDao();
//        if (getGrauParentesco().getDescricao().equals("")) {
//            Messages.warn("Validação", "Digite um nome para o Parentesco!");
//            return;
//        }
//        if (grauParentescoDao.existeParentescoCliente(grauParentesco.getDescricao(), cliente.getId())) {
//            Messages.warn("Validação", "Digite um nome para o Parentesco!");
//            return;
//        }
//        if (getGrauParentesco().getId() == -1) {
//            grauParentesco.setCliente(cliente);
//            if (dao.save(grauParentesco, true)) {
//                setLimpar(false);
//                setMessage("Parentesco salvo com sucesso.");
//                Messages.info("Sucesso", message);
//                logger.save(
//                        "ID: " + grauParentesco.getId()
//                        + " - Cliente: " + grauParentesco.getCliente()
//                        + " - Descrição: " + grauParentesco.getDescricao()
//                );
//                grauParentesco = new GrauParentesco();
//                listGrauParentesco.clear();
//            } else {
//                setMessage("Erro ao salvar grauParentesco!");
//                Messages.warn("Erro", message);
//            }
//        } else {
//            DaoInterface di = new Dao();
//            GrauParentesco p = (GrauParentesco) di.find(grauParentesco);
//            String beforeUpdate
//                    = "ID: " + p.getId()
//                    + " - Cliente: " + p.getCliente()
//                    + " - Descrição: " + p.getDescricao();
//            if (dao.update(grauParentesco, true)) {
//                logger.update(beforeUpdate,
//                        "ID: " + grauParentesco.getId()
//                        + " - Cliente: " + grauParentesco.getCliente()
//                        + " - Descrição: " + grauParentesco.getDescricao()
//                );
//                setMessage("Parentesco atualizado com sucesso.");
//                Messages.info("Sucesso", message);
//                grauParentesco = new GrauParentesco();
//                listGrauParentesco.clear();
//            } else {
//                setMessage("Erro ao atualizar grauParentesco!");
//                Messages.warn("Erro", message);
//            }
//        }
//    }
//
//    public void delete() {
//        Dao dao = new Dao();
//        Logger logger = new Logger();
//        if (grauParentesco.getId() == -1) {
//            message = "Selecione um Parentesco para ser excluído!";
//            Messages.warn("Erro", message);
//            return;
//        }
//        if (dao.delete(grauParentesco, true)) {
//            logger.delete(
//                    "ID: " + grauParentesco.getId()
//                    + " - Cliente: " + grauParentesco.getCliente()
//                    + " - Descrição: " + grauParentesco.getDescricao()
//            );
//            setLimpar(true);
//            message = "Parentesco deletado com sucesso!";
//            Messages.info("Sucesso", message);
//        } else {
//            setMessage("Erro ao deletar grauParentesco!");
//            Messages.warn("Erro", message);
//        }
//        grauParentesco = new GrauParentesco();
//    }
//
//    public String edit(GrauParentesco p) {
//        grauParentesco = p;
//        return "grauParentesco";
//    }
//
//    public String novo() {
//        grauParentesco = new GrauParentesco();
//        listGrauParentesco.clear();
//        return "grauParentesco";
//    }
//
//    public String limpar() {
//        if (limpar == true) {
//            novo();
//        }
//        return "grauParentesco";
//    }
//
//    public GrauParentesco getGrauParentesco() {
//        return grauParentesco;
//    }
//
//    public void setGrupoCategoria(GrauParentesco grauParentesco) {
//        this.grauParentesco = grauParentesco;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public int getIdIndex() {
//        return idIndex;
//    }
//
//    public void setIdIndex(int idIndex) {
//        this.idIndex = idIndex;
//    }
//
//    public List<GrauParentesco> getListGrauParentesco() {
//        if (listGrauParentesco.isEmpty()) {
//            GrauParentescoDao grauParentescoDao = new GrauParentescoDao();
//            listGrauParentesco = grauParentescoDao.pesquisaTodosPorCliente(SessaoCliente.get().getId());
//        }
//        return listGrauParentesco;
//    }
//
//    public void setListGrauParentesco(List<GrauParentesco> listGrauParentesco) {
//        this.listGrauParentesco = listGrauParentesco;
//    }
//
//    public void setGrauParentesco(GrauParentesco grauParentesco) {
//        this.grauParentesco = grauParentesco;
//    }
//
//    public ArrayList getListaImagem() {
//        return listaImagem;
//    }
//
//    public void setListaImagem(ArrayList listaImagem) {
//        this.listaImagem = listaImagem;
//    }
//
//    public boolean isLimpar() {
//        return limpar;
//    }
//
//    public void setLimpar(boolean limpar) {
//        this.limpar = limpar;
//    }
//}
