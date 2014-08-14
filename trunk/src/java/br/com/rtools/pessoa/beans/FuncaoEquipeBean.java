package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.Logger;
import br.com.rtools.pessoa.FuncaoEquipe;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.TipoAtendimento;
import br.com.rtools.pessoa.TipoDocumentoProfissao;
import br.com.rtools.pessoa.dao.FuncaoEquipeDao;
import br.com.rtools.pessoa.dao.TipoAtendimentoDao;
import br.com.rtools.seguranca.controleUsuario.SessaoCliente;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.Messages;
import br.com.rtools.utilitarios.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class FuncaoEquipeBean {

    private FuncaoEquipe funcaoEquipe;
    private List<FuncaoEquipe> listFuncaoEquipe;
    private List<SelectItem> listTipoAtendimento;
    private List<SelectItem> listTipoDocumentoProfissao;
    private List<SelectItem> listProfissao;
    private int idTipoAtendimento;
    private int idTipoFuncaEquipe;
    private int idTipoDocumentoProfissao;
    private int idProfissao;

    @PostConstruct
    public void init() {
        funcaoEquipe = new FuncaoEquipe();
        listFuncaoEquipe = new ArrayList<>();
        listTipoAtendimento = new ArrayList<>();
        listTipoDocumentoProfissao = new ArrayList<>();
        listProfissao = new ArrayList<>();
        idTipoAtendimento = 0;
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
        if (listTipoAtendimento.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipo de atendimento!");
            return;
        }
        funcaoEquipe.setTipoAtendimento((TipoAtendimento) dao.find(new TipoAtendimento(), Integer.parseInt(listTipoAtendimento.get(idTipoAtendimento).getDescription())));
        if (listProfissao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar tipo de atendimento!");
            return;
        }
        funcaoEquipe.setProfissao((Profissao) dao.find(new Profissao(), Integer.parseInt(listProfissao.get(idProfissao).getDescription())));
        Logger logger = new Logger();
        FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
        if (funcaoEquipeDao.existeFuncaoEquipe(funcaoEquipe.getTipoAtendimento().getId(), funcaoEquipe.getTipoDocumentoProfissao().getId(), funcaoEquipe.getProfissao().getId(), SessaoCliente.get().getId())) {
            Messages.warn("Validação", "Função equipe já cadastrada!");
            return;
        }
        if (funcaoEquipe.getId() == -1) {
            funcaoEquipe.setCliente(SessaoCliente.get());
            if (dao.save(funcaoEquipe, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                        + " - Tipo Atendimento: [" + funcaoEquipe.getTipoAtendimento().getId() + "]" + funcaoEquipe.getTipoAtendimento().getDescricao()
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
                    + " - Tipo Documento: [" + fe.getTipoDocumentoProfissao().getId() + "]" + fe.getTipoDocumentoProfissao().getDescricao()
                    + " - Tipo Atendimento: [" + fe.getTipoAtendimento().getId() + "]" + fe.getTipoAtendimento().getDescricao();
            if (dao.update(funcaoEquipe, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                        + " - Tipo Atendimento: [" + funcaoEquipe.getTipoAtendimento().getId() + "]" + funcaoEquipe.getTipoAtendimento().getDescricao()
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
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " ID: " + funcaoEquipe.getId()
                        + " - Profissão: [" + funcaoEquipe.getProfissao().getId() + "]" + funcaoEquipe.getProfissao().getProfissao()
                        + " - Tipo Documento: [" + funcaoEquipe.getTipoDocumentoProfissao().getId() + "]" + funcaoEquipe.getTipoDocumentoProfissao().getDescricao()
                        + " - Tipo Atendimento: [" + funcaoEquipe.getTipoAtendimento().getId() + "]" + funcaoEquipe.getTipoAtendimento().getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public void edit(FuncaoEquipe fe) {
        funcaoEquipe = fe;
        for (int i = 0; i < listTipoDocumentoProfissao.size(); i++) {
            if (funcaoEquipe.getTipoDocumentoProfissao().getId() == Integer.parseInt(listTipoDocumentoProfissao.get(i).getDescription())) {
                idTipoDocumentoProfissao = i;
                break;
            }
        }
        for (int i = 0; i < listTipoAtendimento.size(); i++) {
            if (funcaoEquipe.getTipoAtendimento().getId() == Integer.parseInt(listTipoAtendimento.get(i).getDescription())) {
                idTipoAtendimento = i;
                break;
            }
        }
        for (int i = 0; i < listProfissao.size(); i++) {
            if (funcaoEquipe.getProfissao().getId() == Integer.parseInt(listProfissao.get(i).getDescription())) {
                idProfissao = i;
                break;
            }
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
            FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
            listFuncaoEquipe = funcaoEquipeDao.pesquisaTodasFuncaoEquipePorCliente(SessaoCliente.get().getId());
        }
        return listFuncaoEquipe;
    }

    public void setListFuncaoEquipe(List<FuncaoEquipe> listFuncaoEquipe) {
        this.listFuncaoEquipe = listFuncaoEquipe;
    }

    public List<SelectItem> getListTipoAtendimento() {
        if (listTipoAtendimento.isEmpty()) {
            TipoAtendimentoDao tipoAtendimentoDao = new TipoAtendimentoDao();
            List<TipoAtendimento> list = (List<TipoAtendimento>) tipoAtendimentoDao.pesquisaTodasTipoAtendimentoPorCliente(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listTipoAtendimento.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
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

    public int getIdTipoAtendimento() {
        return idTipoAtendimento;
    }

    public void setIdTipoAtendimento(int idTipoAtendimento) {
        this.idTipoAtendimento = idTipoAtendimento;
    }

    public int getIdTipoFuncaEquipe() {
        return idTipoFuncaEquipe;
    }

    public void setIdTipoFuncaEquipe(int idTipoFuncaEquipe) {
        this.idTipoFuncaEquipe = idTipoFuncaEquipe;
    }

    public int getIdTipoDocumentoProfissao() {
        return idTipoDocumentoProfissao;
    }

    public void setIdTipoDocumentoProfissao(int idTipoDocumentoProfissao) {
        this.idTipoDocumentoProfissao = idTipoDocumentoProfissao;
    }

    public int getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(int idProfissao) {
        this.idProfissao = idProfissao;
    }

}
