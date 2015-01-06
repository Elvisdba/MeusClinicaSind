package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import br.com.clinicaintegrada.pessoa.Profissao;
import br.com.clinicaintegrada.pessoa.TipoAtendimento;
import br.com.clinicaintegrada.pessoa.TipoDocumentoProfissao;
import br.com.clinicaintegrada.pessoa.dao.FuncaoEquipeDao;
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
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class FuncaoEquipeBean implements Serializable {

    private FuncaoEquipe funcaoEquipe;
    private List<FuncaoEquipe> listFuncaoEquipe;
    private List<SelectItem> listTipoAtendimento;
    private List<SelectItem> listTipoDocumentoProfissao;
    private List<SelectItem> listProfissao;
    private Integer idTipoAtendimento;
    private Integer idTipoFuncaEquipe;
    private Integer idTipoDocumentoProfissao;
    private Integer idProfissao;

    @PostConstruct
    public void init() {
        funcaoEquipe = new FuncaoEquipe();
        listFuncaoEquipe = new ArrayList<>();
        listTipoAtendimento = new ArrayList<>();
        listTipoDocumentoProfissao = new ArrayList<>();
        listProfissao = new ArrayList<>();
        idTipoAtendimento = -1;
        idTipoFuncaEquipe = 0;
        idTipoDocumentoProfissao = 0;
        idProfissao = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("funcaoEquipeBean");
        Sessions.remove("funcaoEquipePesquisa");
    }

    public void clear() {
        Sessions.remove("funcaoEquipeBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (listTipoDocumentoProfissao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipo de documento profissão!");
            return;
        }
        funcaoEquipe.setTipoDocumentoProfissao((TipoDocumentoProfissao) dao.find(new TipoDocumentoProfissao(), Integer.parseInt(listTipoDocumentoProfissao.get(idTipoDocumentoProfissao).getDescription())));
        if (listProfissao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipo de atendimento!");
            return;
        }
        funcaoEquipe.setProfissao((Profissao) dao.find(new Profissao(), Integer.parseInt(listProfissao.get(idProfissao).getDescription())));
        if (idTipoAtendimento == 0) {
            funcaoEquipe.setTipoAtendimento(null);
        } else {
            if (listTipoAtendimento.isEmpty()) {
                Messages.warn("Validação", "Cadastrar tipo de atendimento!");
                return;
            }
            funcaoEquipe.setTipoAtendimento((TipoAtendimento) dao.find(new TipoAtendimento(), Integer.parseInt(listTipoAtendimento.get(idTipoAtendimento).getDescription())));
        }
        Logger logger = new Logger();
        FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
        int idTa = 0;
        if (funcaoEquipe.getTipoAtendimento() != null && funcaoEquipe.getTipoAtendimento().getId() != -1) {
            idTa = funcaoEquipe.getTipoAtendimento().getId();
        }
        if (funcaoEquipeDao.existeFuncaoEquipePorProfissaoCliente(funcaoEquipe.getProfissao().getId(), SessaoCliente.get().getId())) {
            Messages.warn("Validação", "Função equipe já cadastrada!");
            return;
        }
        if (funcaoEquipe.getId() == null) {
            funcaoEquipe.setCliente(SessaoCliente.get());
            if (dao.save(funcaoEquipe, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao adicionar registro!");
            }
        } else {
            FuncaoEquipe fe = (FuncaoEquipe) dao.find(funcaoEquipe);
            String beforeUpdate = ""
                    + " ID: " + fe.getId()
                    + " - Profissão: [" + fe.getProfissao().getId() + "]" + fe.getProfissao().getProfissao()
                    + " - Tipo Documento: [" + fe.getTipoDocumentoProfissao().getId() + "]" + fe.getTipoDocumentoProfissao().getDescricao();
            if (dao.update(funcaoEquipe, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }
    }

    public void update(FuncaoEquipe fe) {
        if (fe.getId() != null) {
            Logger logger = new Logger();
            Dao dao = new Dao();
            FuncaoEquipe fe2 = (FuncaoEquipe) dao.find(fe);
            String beforeUpdate = ""
                    + " ID: " + fe2.getId()
                    + " - Profissão: [" + fe2.getProfissao().getId() + "]" + fe2.getProfissao().getProfissao()
                    + " - Tipo Documento: [" + fe2.getTipoDocumentoProfissao().getId() + "]" + fe2.getTipoDocumentoProfissao().getDescricao();
            if (dao.update(fe, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + fe.getId()
                        + " - Profissão: [" + fe.getProfissao().getId() + "]" + fe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + fe.getTipoDocumentoProfissao().getId() + "]" + fe.getTipoDocumentoProfissao().getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (funcaoEquipe.getId() != -1) {
            if (dao.delete(funcaoEquipe, true)) {
                logger.delete(
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                );
                clear();
                Messages.info("Sucesso", "Registro removido");
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public void delete(FuncaoEquipe fe) {
        funcaoEquipe = fe;
        delete();
    }

    public void edit(Object o) {
        Dao dao = new Dao();
        funcaoEquipe = (FuncaoEquipe) dao.rebind(o);
        for (int i = 0; i < listProfissao.size(); i++) {
            if (funcaoEquipe.getProfissao().getId() == Integer.parseInt(listProfissao.get(i).getDescription())) {
                idProfissao = i;
                break;
            }
        }
        for (int i = 0; i < listTipoDocumentoProfissao.size(); i++) {
            if (funcaoEquipe.getTipoDocumentoProfissao().getId() == Integer.parseInt(listTipoDocumentoProfissao.get(i).getDescription())) {
                idTipoDocumentoProfissao = i;
                break;
            }
        }
        if (funcaoEquipe.getTipoAtendimento() != null) {
            for (int i = 0; i < listTipoAtendimento.size(); i++) {
                if (funcaoEquipe.getTipoAtendimento().getId() == Integer.parseInt(listTipoAtendimento.get(i).getDescription())) {
                    idTipoAtendimento = i;
                    break;
                }
            }
        } else {
            idTipoAtendimento = 0;
        }
    }

    public FuncaoEquipe getFuncaoEquipe() {
        return funcaoEquipe;
    }

    public void setFuncaoEquipe(FuncaoEquipe funcaoEquipe) {
        this.funcaoEquipe = funcaoEquipe;
    }

    public List<FuncaoEquipe> getListFuncaoEquipe() {
        if (listFuncaoEquipe.isEmpty()) {
            if (!listProfissao.isEmpty()) {
                FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
                //listFuncaoEquipe = funcaoEquipeDao.pesquisaFuncaoEquipePorProfissaoCliente(Integer.parseInt(listProfissao.get(idProfissao).getDescription()), SessaoCliente.get().getId());
                listFuncaoEquipe = funcaoEquipeDao.pesquisaTodasFuncaoEquipePorCliente(SessaoCliente.get().getId());
            }
        }
        return listFuncaoEquipe;
    }

    public void setListFuncaoEquipe(List<FuncaoEquipe> listFuncaoEquipe) {
        this.listFuncaoEquipe = listFuncaoEquipe;
    }

    public List<SelectItem> getListTipoAtendimento() {
        if (listTipoAtendimento.isEmpty()) {
            Dao dao = new Dao();
            int i = 0;
            listTipoAtendimento.add(new SelectItem(i, "NENHUM", "" + -1));
            List<TipoAtendimento> list = (List<TipoAtendimento>) dao.list(new TipoAtendimento(), true);
            for (i = 0; i < list.size(); i++) {
                listTipoAtendimento.add(new SelectItem(i + 1, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoAtendimento;
    }

    public void setListTipoAtendimento(List<SelectItem> listTipoAtendimento) {
        this.listTipoAtendimento = listTipoAtendimento;
    }

    public List<SelectItem> getListTipoDocumentoProfissao() {
        if (listTipoDocumentoProfissao.isEmpty()) {
            Dao dao = new Dao();
            List<TipoDocumentoProfissao> list = (List<TipoDocumentoProfissao>) dao.list(new TipoDocumentoProfissao(), true);
            for (int i = 0; i < list.size(); i++) {
                listTipoDocumentoProfissao.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listTipoDocumentoProfissao;
    }

    public void setListTipoDocumentoProfissao(List<SelectItem> listTipoDocumentoProfissao) {
        this.listTipoDocumentoProfissao = listTipoDocumentoProfissao;
    }

    public List<SelectItem> getListProfissao() {
        if (listProfissao.isEmpty()) {
            Dao dao = new Dao();
            List<Profissao> list = (List<Profissao>) dao.list(new Profissao(), true);
            for (int i = 0; i < list.size(); i++) {
                listProfissao.add(new SelectItem(i, list.get(i).getProfissao(), "" + list.get(i).getId()));
            }
        }
        return listProfissao;
    }

    public void setListProfissao(List<SelectItem> listProfissao) {
        this.listProfissao = listProfissao;
    }

    public Integer getIdTipoAtendimento() {
        return idTipoAtendimento;
    }

    public void setIdTipoAtendimento(Integer idTipoAtendimento) {
        this.idTipoAtendimento = idTipoAtendimento;
    }

    public Integer getIdTipoFuncaEquipe() {
        return idTipoFuncaEquipe;
    }

    public void setIdTipoFuncaEquipe(Integer idTipoFuncaEquipe) {
        this.idTipoFuncaEquipe = idTipoFuncaEquipe;
    }

    public Integer getIdTipoDocumentoProfissao() {
        return idTipoDocumentoProfissao;
    }

    public void setIdTipoDocumentoProfissao(Integer idTipoDocumentoProfissao) {
        this.idTipoDocumentoProfissao = idTipoDocumentoProfissao;
    }

    public Integer getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(Integer idProfissao) {
        this.idProfissao = idProfissao;
    }

}
