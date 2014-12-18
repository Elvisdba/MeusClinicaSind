package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Profissao;
import br.com.clinicaintegrada.pessoa.TipoDocumentoProfissao;
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
public class TipoDocumentoProfissaoBean implements Serializable {

    private TipoDocumentoProfissao tipoDocumentoProfissao;
    private List<TipoDocumentoProfissao> listTipoDocumentoProfissao;
    private List<SelectItem> listProfissao;
    private int idProfissao;

    @PostConstruct
    public void init() {
        tipoDocumentoProfissao = new TipoDocumentoProfissao();
        listTipoDocumentoProfissao = new ArrayList<>();
        listProfissao = new ArrayList<>();
        idProfissao = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("tipoDocumentoProfissaoBean");
        Sessions.remove("tipoDocumentoProfissaoPesquisa");
    }

    public void clear() {
        Sessions.remove("tipoDocumentoProfissaoBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (listProfissao.isEmpty()) {
            Messages.warn("Validação", "Informar descrição da profissão!");
            return;
        }
        if (listProfissao.isEmpty()) {
            Messages.warn("Validação", "Cadastrar profissões!");
            return;
        }
        tipoDocumentoProfissao.setProfissao((Profissao) dao.find(new Profissao(), Integer.parseInt(listProfissao.get(idProfissao).getDescription())));
        Logger logger = new Logger();
        if (tipoDocumentoProfissao.getId() == -1) {
            for (int i = 0; i < getListTipoDocumentoProfissao().size(); i++) {
                if (listTipoDocumentoProfissao.get(i).getDescricao().toUpperCase().equals(tipoDocumentoProfissao.getDescricao().toUpperCase()) && listTipoDocumentoProfissao.get(i).getProfissao().getProfissao().toUpperCase().equals(tipoDocumentoProfissao.getProfissao().getProfissao().toUpperCase())) {
                    Messages.warn("Validação", "Tipo de documento e profissão já cadastrado!");
                    return;
                }
            }
            if (dao.save(tipoDocumentoProfissao, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + tipoDocumentoProfissao.getId()
                        + " - Profissão: [" + tipoDocumentoProfissao.getProfissao().getId() + "]" + tipoDocumentoProfissao.getProfissao().getProfissao()
                        + " - Descrição: " + tipoDocumentoProfissao.getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        } else {
            TipoDocumentoProfissao tdp = (TipoDocumentoProfissao) dao.find(tipoDocumentoProfissao);
            String beforeUpdate
                    = " ID: " + tdp.getId()
                    + " - Profissão: [" + tdp.getProfissao().getId() + "]" + tdp.getProfissao().getProfissao()
                    + " - Descrição: " + tdp.getDescricao();
            if (dao.update(tipoDocumentoProfissao, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + tipoDocumentoProfissao.getId()
                        + " - Profissão: [" + tipoDocumentoProfissao.getProfissao().getId() + "]" + tipoDocumentoProfissao.getProfissao().getProfissao()
                        + " - Descrição: " + tipoDocumentoProfissao.getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao inserir registro!");
            }
        }
    }

    public void delete(TipoDocumentoProfissao tdp) {
        tipoDocumentoProfissao = tdp;
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (tipoDocumentoProfissao.getId() != -1) {
            if (dao.delete(tipoDocumentoProfissao, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " ID: " + tipoDocumentoProfissao.getId()
                        + " - Profissão: [" + tipoDocumentoProfissao.getProfissao().getId() + "]" + tipoDocumentoProfissao.getProfissao().getProfissao()
                        + " - Descrição: " + tipoDocumentoProfissao.getDescricao()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }
    }

    public void edit(Object o) {
        Dao dao = new Dao();
        tipoDocumentoProfissao = (TipoDocumentoProfissao) dao.rebind(o);
        for (int i = 0; i < listProfissao.size(); i++) {
            if (tipoDocumentoProfissao.getProfissao().getId() == Integer.parseInt(getListProfissao().get(i).getDescription())) {
                idProfissao = i;
                break;
            }
        }
    }

    public TipoDocumentoProfissao getTipoDocumentoProfissao() {
        return tipoDocumentoProfissao;
    }

    public void setTipoDocumentoProfissao(TipoDocumentoProfissao tipoDocumentoProfissao) {
        this.tipoDocumentoProfissao = tipoDocumentoProfissao;
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

    public int getIdProfissao() {
        return idProfissao;
    }

    public void setIdProfissao(int idProfissao) {
        this.idProfissao = idProfissao;
    }

    public List<TipoDocumentoProfissao> getListTipoDocumentoProfissao() {
        if (listTipoDocumentoProfissao.isEmpty()) {
            Dao dao = new Dao();
            listTipoDocumentoProfissao = (List<TipoDocumentoProfissao>) dao.list(new TipoDocumentoProfissao(), true);
        }
        return listTipoDocumentoProfissao;
    }

    public void setListTipoDocumentoProfissao(List<TipoDocumentoProfissao> listTipoDocumentoProfissao) {
        this.listTipoDocumentoProfissao = listTipoDocumentoProfissao;
    }

}
