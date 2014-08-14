package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.Equipe;
import br.com.rtools.pessoa.FuncaoEquipe;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.dao.EquipeDao;
import br.com.rtools.principal.DB;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import javax.persistence.Query;

@ManagedBean
@SessionScoped
public class EquipeBean {

    private Equipe equipe;
    private List<Equipe> listEquipe;
    private List<SelectItem> listFuncaoEquipe;
    private int idFuncaoEquipe;

    @PostConstruct
    public void init() {
        equipe = new Equipe();
        listEquipe = new ArrayList<>();
        listFuncaoEquipe = new ArrayList<>();
        idFuncaoEquipe = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("equipeBean");
        Sessions.remove("equipePesquisa");
    }

    public void clear() {
        Sessions.remove("equipeBean");
    }

    public void save() {
//        Dao dao = new Dao();
//        if (listTipoDocumentoProfissao.isEmpty()) {
//            Messages.warn("Validação", "Cadastrar tipo de documento profissão!");
//            return;
//        }
//        equipe.setTipoDocumentoProfissao((TipoDocumentoProfissao) dao.find(new TipoDocumentoProfissao(), Integer.parseInt(listTipoDocumentoProfissao.get(idTipoDocumentoProfissao).getDescription())));
//        if (listTipoAtendimento.isEmpty()) {
//            Messages.warn("Validação", "Cadastrar tipo de atendimento!");
//            return;
//        }
//        equipe.setTipoAtendimento((TipoAtendimento) dao.find(new TipoAtendimento(), Integer.parseInt(listTipoAtendimento.get(idTipoAtendimento).getDescription())));
//        if (listProfissao.isEmpty()) {
//            Messages.warn("Validação", "Cadastrar tipo de atendimento!");
//            return;
//        }
//        equipe.setProfissao((Profissao) dao.find(new Profissao(), Integer.parseInt(listProfissao.get(idProfissao).getDescription())));
//        Logger logger = new Logger();
//        EquipeDao equipeDao = new EquipeDao();
//        if (equipeDao.existeEquipe(equipe.getTipoAtendimento().getId(), equipe.getTipoDocumentoProfissao().getId(), equipe.getProfissao().getId(), SessaoCliente.get().getId())) {
//            Messages.warn("Validação", "Função equipe já cadastrada!");
//            return;
//        }
//        if (equipe.getId() == -1) {
//            equipe.setCliente(SessaoCliente.get());
//            if (dao.save(equipe, true)) {
//                Messages.info("Sucesso", "Registro adicionado");
//                logger.save(
//                        " ID: " + equipe.getId()
//                        + " - Profissão: [" + equipe.getProfissao().getId() + "]" + equipe.getProfissao().getProfissao()
//                        + " - Tipo Documento: [" + equipe.getTipoDocumentoProfissao().getId() + "]" + equipe.getTipoDocumentoProfissao().getDescricao()
//                        + " - Tipo Atendimento: [" + equipe.getTipoAtendimento().getId() + "]" + equipe.getTipoAtendimento().getDescricao()
//                );
//                clear();
//            } else {
//                Messages.warn("Erro", "Erro ao adicionar registro!");
//            }
//        } else {
//            Equipe fe = (Equipe) dao.find(equipe);
//            String beforeUpdate = ""
//                    + " ID: " + fe.getId()
//                    + " - Profissão: [" + fe.getProfissao().getId() + "]" + fe.getProfissao().getProfissao()
//                    + " - Tipo Documento: [" + fe.getTipoDocumentoProfissao().getId() + "]" + fe.getTipoDocumentoProfissao().getDescricao()
//                    + " - Tipo Atendimento: [" + fe.getTipoAtendimento().getId() + "]" + fe.getTipoAtendimento().getDescricao();
//            if (dao.update(equipe, true)) {
//                Messages.info("Sucesso", "Registro atualizado");
//                logger.update(beforeUpdate,
//                        " ID: " + equipe.getId()
//                        + " - Profissão: [" + equipe.getProfissao().getId() + "]" + equipe.getProfissao().getProfissao()
//                        + " - Tipo Documento: [" + equipe.getTipoDocumentoProfissao().getId() + "]" + equipe.getTipoDocumentoProfissao().getDescricao()
//                        + " - Tipo Atendimento: [" + equipe.getTipoAtendimento().getId() + "]" + equipe.getTipoAtendimento().getDescricao()
//                );
//                clear();
//            } else {
//                Messages.warn("Erro", "Erro ao atualizar registro!");
//            }
//        }
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
//        if (equipe.getId() != -1) {
//            if (dao.delete(equipe, true)) {
//                Messages.info("Sucesso", "Registro removido");
//                logger.delete(
//                        " ID: " + equipe.getId()
//                        + " - Profissão: [" + equipe.getProfissao().getId() + "]" + equipe.getProfissao().getProfissao()
//                        + " - Tipo Documento: [" + equipe.getTipoDocumentoProfissao().getId() + "]" + equipe.getTipoDocumentoProfissao().getDescricao()
//                        + " - Tipo Atendimento: [" + equipe.getTipoAtendimento().getId() + "]" + equipe.getTipoAtendimento().getDescricao()
//                );
//                clear();
//            } else {
//                Messages.warn("Erro", "Erro ao remover registro!");
//            }
//        }

    }

    public void edit(Equipe e) {
        equipe = e;
        for (int i = 0; i < listFuncaoEquipe.size(); i++) {
            if (equipe.getFuncaoEquipe().getId() == Integer.parseInt(listFuncaoEquipe.get(i).getDescription())) {
                idFuncaoEquipe = i;
                break;
            }
        }
    }

    public Equipe getEquipe() {
        DB db = new DB();
        String sql = "SELECT p.* FROM pes_pessoa as p WHERE p.id = 1 ";
        Query query = db.getEntityManager().createNativeQuery(sql, Pessoa.class);
        //query.setParameter(1, 1);
        Pessoa pessoa = (Pessoa) query.getSingleResult();
//ReadAllQuery query = new ReadAllQuery();
// Usuario usuario = new Usuario();
// usuario.setLogin("a");
// query.setExampleObject(usuario);
// QueryByExamplePolicy policy = new QueryByExamplePolicy();
// policy.addSpecialOperation(String.class, "like");
// policy.addSpecialOperation(Integer.class, "greaterThan");
// policy.alwaysIncludeAttribute(Employee.class, "salary");
// query.setQueryByExamplePolicy(policy);
// Vector results = (Vector) session.executeQuery(query);        
        if (Sessions.exists("fisicaPesquisa")) {
            equipe.setPessoa((Pessoa) Sessions.getObject("fisicaPesquisa", true));
        }
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public List<Equipe> getListEquipe() {
        if (listEquipe.isEmpty()) {
            EquipeDao equipeDao = new EquipeDao();
            //listEquipe = equipeDao.pesquisaTodasEquipesPorCliente(String descricaoPesquisa, SessaoCliente.get().getId());
        }
        return listEquipe;
    }

    public void setListEquipe(List<Equipe> listEquipe) {
        this.listEquipe = listEquipe;
    }

    public List<SelectItem> getListFuncaoEquipe() {
        if (listFuncaoEquipe.isEmpty()) {
            Dao dao = new Dao();
            List<FuncaoEquipe> list = (List<FuncaoEquipe>) dao.list(new FuncaoEquipe(), true);
            for (int i = 0; i < list.size(); i++) {
                listFuncaoEquipe.add(new SelectItem(i, list.get(i).getTipoAtendimento().getDescricao() + " - " + list.get(i).getTipoDocumentoProfissao().getDescricao() + " - " + list.get(i).getProfissao(), "" + list.get(i).getId()));
            }
        }
        return listFuncaoEquipe;
    }

    public void setListFuncaoEquipe(List<SelectItem> listFuncaoEquipe) {
        this.listFuncaoEquipe = listFuncaoEquipe;
    }

    public int getIdFuncaoEquipe() {
        return idFuncaoEquipe;
    }

    public void setIdFuncaoEquipe(int idFuncaoEquipe) {
        this.idFuncaoEquipe = idFuncaoEquipe;
    }

}
