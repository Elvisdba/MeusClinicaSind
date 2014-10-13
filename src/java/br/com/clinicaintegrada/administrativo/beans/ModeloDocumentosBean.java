package br.com.clinicaintegrada.administrativo.beans;

import br.com.clinicaintegrada.administrativo.ModeloContrato;
import br.com.clinicaintegrada.administrativo.ModeloDocumentos;
import br.com.clinicaintegrada.administrativo.dao.ModeloContratoDao;
import br.com.clinicaintegrada.administrativo.dao.ModeloDocumentosDao;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.utils.AnaliseString;
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
public class ModeloDocumentosBean implements Serializable {

    private ModeloDocumentos modeloDocumentos;
    private List<ModeloDocumentos> listModeloDocumentos;

    @PostConstruct
    public void init() {
        modeloDocumentos = new ModeloDocumentos();
        listModeloDocumentos = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("modeloDocumentosBean");
        Sessions.remove("modeloContratoPesquisa");
        Sessions.remove("modeloDocumentosPesquisa");
        Sessions.remove("rotinaPesquisa");
    }

    public void clear() {
        Sessions.remove("modeloDocumentosBean");
    }

    public void addModeloDocumentos() {
        if (modeloDocumentos.getDescricao().isEmpty()) {
            Messages.warn("Validação", "Informar descrição!");
            return;
        }
        if (modeloDocumentos.getRotina().getId() == -1) {
            Messages.warn("Validação", "Informar a rotina!");
            return;
        }
        if (modeloDocumentos.getModeloContrato().getId() == -1) {
            Messages.warn("Validação", "Informar o modelo do documento!");
            return;
        }
        ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
        if(!modeloContratoDao.pesquisaModeloContratoServico(modeloDocumentos.getModeloContrato().getId()).isEmpty()) {
            Messages.warn("Validação", "Este modelo de contrato já é utilizado como principal no módulo!");
            return;
        }
        for (int i = 0; i < listModeloDocumentos.size(); i++) {
            if (modeloDocumentos.getModeloContrato().getId() == listModeloDocumentos.get(i).getModeloContrato().getId()) {
                Messages.warn("Validação", "Modelo contrato já cadastrado para esta rotina!");
                return;
            }
        }
        Dao dao = new Dao();
        dao.openTransaction();
        if (dao.save(modeloDocumentos)) {
            dao.commit();
            Messages.info("Sucesso", "Registro adicionado");
            listModeloDocumentos.clear();
            Rotina r = modeloDocumentos.getRotina();
            modeloDocumentos = new ModeloDocumentos();
            modeloDocumentos.setRotina(r);
        } else {
            dao.rollback();
            Messages.warn("Erro", "Ao adicionar!");
        }

    }

    public void removeModeloDocumentos(ModeloDocumentos md) {
        if (md.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            if (dao.delete(md)) {
                dao.commit();
                Messages.info("Sucesso", "Modelo de documento removido");
                listModeloDocumentos.clear();
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao remover!");
            }
        }
    }

    public ModeloDocumentos getModeloDocumentos() {
        if (Sessions.exists("rotinaPesquisa")) {
            modeloDocumentos.setRotina((Rotina) Sessions.getObject("rotinaPesquisa", true));
            listModeloDocumentos.clear();
        }
        if (Sessions.exists("modeloContratoPesquisa")) {
            modeloDocumentos.setModeloContrato((ModeloContrato) Sessions.getObject("modeloContratoPesquisa", true));
        }
        return modeloDocumentos;
    }

    public void setModeloDocumentos(ModeloDocumentos modeloDocumentos) {
        this.modeloDocumentos = modeloDocumentos;
    }

    public List<ModeloDocumentos> getListModeloDocumentos() {
        if (listModeloDocumentos.isEmpty()) {
            ModeloDocumentosDao modeloDocumentosDao = new ModeloDocumentosDao();
            listModeloDocumentos = modeloDocumentosDao.pesquisaTodosPorRotina(modeloDocumentos.getRotina().getId());
        }
        return listModeloDocumentos;
    }

    public void setListModeloDocumentos(List<ModeloDocumentos> listModeloDocumentos) {
        this.listModeloDocumentos = listModeloDocumentos;
    }
}
