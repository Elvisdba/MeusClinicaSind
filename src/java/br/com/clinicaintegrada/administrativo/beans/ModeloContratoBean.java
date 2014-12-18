package br.com.clinicaintegrada.administrativo.beans;

import br.com.clinicaintegrada.administrativo.ModeloContrato;
import br.com.clinicaintegrada.administrativo.ModeloContratoCampos;
import br.com.clinicaintegrada.administrativo.ModeloContratoServico;
import br.com.clinicaintegrada.administrativo.ModeloDocumentos;
import br.com.clinicaintegrada.administrativo.dao.ModeloContratoDao;
import br.com.clinicaintegrada.administrativo.dao.ModeloDocumentosDao;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.sistema.ConfiguracaoUpload;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.DataObject;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Upload;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ModeloContratoBean implements Serializable {

    private ModeloContrato modeloContrato;
    private ModeloContratoCampos modeloContratoCampos;
    private ModeloContratoServico modeloContratoServico;
    private List<ModeloContrato> modeloContratos;
    private List<ModeloContratoServico> listaModeloContratoServico;
    private List<ModeloContratoCampos> listaModeloContratoCampos;
    private List<SelectItem> listServicos;
    private List<SelectItem> listModulos;
    private List<SelectItem> listModulos2;
    private List listaArquivos;
    private Integer idIndexServicos;
    private Integer idIndex;
    private Modulo modulo;
    private Integer idModulo;
    private Integer idModulo2;
    private Integer idServicos;
    private Integer quantidadeAnexo;
    private Servicos servicos;
    private String descricaoPesquisa;
    private String msgServico;
    private boolean desabilitaObservacao;
    private List<ModeloDocumentos> listModeloDocumentos;

    @PostConstruct
    public void init() {
        modeloContrato = new ModeloContrato();
        modeloContratoCampos = new ModeloContratoCampos();
        modeloContratoServico = new ModeloContratoServico();
        modeloContratos = new ArrayList<>();
        listaModeloContratoServico = new ArrayList<>();
        listaModeloContratoCampos = new ArrayList<>();
        listServicos = new ArrayList<>();
        listModulos = new ArrayList<>();
        listModulos2 = new ArrayList<>();
        listaArquivos = new ArrayList();
        idIndexServicos = -1;
        idIndex = -1;
        modulo = new Modulo();
        idModulo = 0;
        idModulo2 = 0;
        idServicos = 0;
        quantidadeAnexo = 0;
        servicos = new Servicos();
        descricaoPesquisa = "";
        msgServico = "";
        desabilitaObservacao = false;
        listModeloDocumentos = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("modeloContratoBean");
        Sessions.remove("modeloContratoPesquisa");

    }

    public boolean isDesabilitaObservacao() {
        desabilitaObservacao = ((Usuario) (Sessions.getObject("sessaoUsuario"))).getId() == 1;
        return desabilitaObservacao;
    }

    public void setDesabilitaObservacao(boolean desabilitaObservacao) {
        this.desabilitaObservacao = desabilitaObservacao;
    }

    // MATRICULA CONTRATO
    public void clear() {
        Sessions.remove("modeloContratoBean");
    }

    public void save() {
        if (modeloContrato.getTitulo().equals("")) {
            Messages.warn("Validação", "Informar o titulo!");
            return;
        }
        if (modeloContrato.getDescricao().equals("")) {
            Messages.warn("Validação", "Informar o descrição!");
            return;
        }
        Dao dao = new Dao();
        Logger novoLog = new Logger();
        if (modeloContrato.getId() == -1) {
            if (Sessions.exists("idModulo")) {
                int idMod = Sessions.getInteger("idModulo");
                if (idMod != 0) {
                    modulo = (Modulo) dao.find(new Modulo(), idMod);
                }
            }
            modeloContrato.setModulo(modulo);
            modeloContrato.setCliente(SessaoCliente.get());
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            if (modeloContratoDao.existeModeloContrato(modeloContrato)) {
                Messages.warn("Validação", "Contrato já existe!");
                return;
            }
            dao.openTransaction();
            if (dao.save(modeloContrato)) {
                dao.commit();
                novoLog.save("ID: " + modeloContrato.getId() + " - Título: " + modeloContrato.getTitulo() + " - Módulo: (" + modeloContrato.getModulo().getId() + ") " + modeloContrato.getModulo().getDescricao());
                modeloContratos.clear();
                Messages.info("Sucesso", "Registro inserido");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao inserir registro!");
            }
        } else {
            ModeloContrato mc = (ModeloContrato) dao.find(modeloContrato);
            String beforeUpdate = "ID: " + mc.getId() + " - Título: " + mc.getTitulo() + " - Módulo: (" + mc.getModulo().getId() + ") " + mc.getModulo().getDescricao();
            modeloContrato.setDataAtualizado(DataHoje.data());
            dao.openTransaction();
            if (dao.update(modeloContrato)) {
                novoLog.update(beforeUpdate, "ID: " + modeloContrato.getId() + " - Título: " + modeloContrato.getTitulo() + " - Módulo: (" + modeloContrato.getModulo().getId() + ") " + modeloContrato.getModulo().getDescricao());
                dao.commit();
                modeloContratos.clear();
                Messages.info("Sucesso", "Registro atualizado");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao atualizar registro!");
            }
        }
    }

    public void delete() {
        if (modeloContrato.getId() != -1) {
            Logger novoLog = new Logger();
            Dao dao = new Dao();
            dao.openTransaction();
            for (int i = 0; i < listaModeloContratoServico.size(); i++) {
                if (!dao.delete(listaModeloContratoServico.get(i))) {
                    dao.rollback();
                    Messages.warn("Erro", "Ao excluir registro!");
                    return;
                }
            }
            if (dao.delete(modeloContrato)) {
                dao.commit();
                novoLog.delete("ID: " + modeloContrato.getId() + " - Título: " + modeloContrato.getTitulo() + " - Módulo: (" + modeloContrato.getModulo().getId() + ") " + modeloContrato.getModulo().getDescricao());
                modeloContratos.clear();
                clear();
                Messages.info("Sucesso", "Registro excluído");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public String link(ModeloContrato mc) {
        Dao dao = new Dao();
        setModeloContrato((ModeloContrato) dao.find(new ModeloContrato(), mc.getId()));
        Sessions.put("modeloContratoPesquisa", modeloContrato);
        Sessions.put("linkClicado", true);
        listaModeloContratoServico.clear();
        if (Sessions.exists("urlRetorno")) {
            return (String) Sessions.getString("urlRetorno");
        } else {
            return "modeloContrato";
        }
    }

    // MATRICULA CONTRATO CAMPOS
    public void limparListaMCCampos() {
        // idModulo = 0;
        // listaModulos.clear();
        listaModeloContratoCampos.clear();
        // getListaModeloContratoCampos("");
    }

    public void novoModeloContratoCampos() {
        idModulo = 0;
        listaModeloContratoServico.clear();
        modeloContratoCampos = new ModeloContratoCampos();
    }

    public synchronized void addCamposModuloContrato() {
        if (modeloContratoCampos.getCampo().equals("")) {
            Messages.info("Sistema", "Informar o campo!");
            return;
        }
        if (modeloContratoCampos.getVariavel().equals("")) {
            Messages.warn("Sistema", "Informar a variável!");
            return;
        }
        Dao dao = new Dao();
        ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
        if (modeloContratoCampos.getId() == -1) {
            if (modeloContratoDao.existeModeloContratoCampo(modeloContratoCampos, "campo")) {
                Messages.warn("Sistema", "Variável já existe!");
                return;
            }
            if (modeloContratoDao.existeModeloContratoCampo(modeloContratoCampos, "variavel")) {
                Messages.warn("Sistema", "Campo já cadastrado!");
                return;
            }
            if (modeloContratoDao.existeModeloContratoCampo(modeloContratoCampos, "tudo")) {
                Messages.warn("Sistema", "Campo já cadastrado!");
                return;
            }
            modeloContratoCampos.setModulo((Modulo) dao.find(new Modulo(), Integer.parseInt(listModulos2.get(idModulo2).getDescription())));
            dao.openTransaction();
            if (dao.save(modeloContratoCampos)) {
                dao.commit();
                Messages.info("Sucesso", "Registro inserido com sucesso.");
                listaModeloContratoCampos.clear();
                listModulos.clear();
                idModulo = 0;
            } else {
                dao.rollback();
                Messages.info("Erro", "Falha ao inserir o registro!");
            }
        } else {
            dao.openTransaction();
            if (dao.update(modeloContratoCampos)) {
                dao.commit();
                Messages.info("Sucesso", "Registro atualizado com sucesso.");
                listaModeloContratoCampos.clear();
            } else {
                dao.rollback();
                Messages.info("Erro", "Falha ao atualizar o registro!");
            }
        }
        modeloContratoCampos.setModulo(modulo);
    }

    public String removeCamposModuloContrato(ModeloContratoCampos mcc) {
        Dao dao = new Dao();
        if (mcc.getId() != -1) {
            dao.openTransaction();
            if (dao.delete(mcc)) {
                dao.commit();
                Messages.info("Sucesso", "Registro excluído com sucesso");
                listaModeloContratoCampos.clear();
                listModulos.clear();
                idModulo = 0;
                modeloContratoCampos = new ModeloContratoCampos();
            } else {
                dao.rollback();
                Messages.warn("Erro", "Falha ao excluir o registro!");
            }
        }
        return "modeloContratoCampos";
    }

    public ModeloContrato getModeloContrato() {
        if (Sessions.exists("modeloContratoPesquisa")) {
            modeloContrato = (ModeloContrato) Sessions.getObject("modeloContratoPesquisa", true);
        }
        return modeloContrato;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            List<Servicos> list = (List<Servicos>) modeloContratoDao.listaServicosDispiniveis();
            for (int i = 0; i < list.size(); i++) {
                listServicos.add(new SelectItem(i, (String) (list.get(i)).getDescricao(), Integer.toString((list.get(i)).getId())));
            }
        }
        return listServicos;
    }

    public void addServicos() {
        msgServico = "";
        if (modeloContrato.getId() != -1) {
            int idServico = Integer.parseInt(getListServicos().get(idServicos).getDescription());
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            if (modeloContratoDao.validaModeloContratoServico(modeloContrato.getId(), idServico)) {
                Messages.warn("Validação", "Contrato já possui esse serviço!");
                return;
            }
            if (modeloContratoDao.existeServicoModeloContrato(idServico)) {
                Messages.warn("Validação", "Serviço já cadastrado para contrato (s)!");
                return;
            }
            Dao dao = new Dao();
            modeloContratoServico.setServico((Servicos) (dao.find(new Servicos(), idServico)));
            modeloContratoServico.setContrato(modeloContrato);
            dao.openTransaction();
            if (dao.save(modeloContratoServico)) {
                dao.commit();
                Messages.info("Sucesso", "Serviço adicionado");
                modeloContratoServico = new ModeloContratoServico();
                listaModeloContratoServico.clear();
            } else {
                Messages.warn("Erro", "Ao adicionar este serviço!");
                dao.rollback();
            }
        }
    }

    public void removeServicos(ModeloContratoServico mcs) {
        msgServico = "";
        if (mcs.getId() != -1) {
            modeloContratoServico = mcs;
        }
        if (modeloContratoServico.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            if (dao.delete(modeloContratoServico)) {
                dao.commit();
                Messages.info("Sucesso", "Serviço removido");
                listaModeloContratoServico.clear();
                modeloContratoServico = new ModeloContratoServico();
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao remover!");
            }
        }
    }

    public void setModeloContrato(ModeloContrato modeloContrato) {
        this.modeloContrato = modeloContrato;
    }

    public Integer getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(Integer idIndex) {
        this.idIndex = idIndex;
    }

    public List<ModeloContrato> getModeloContratos() {
        modeloContratos.clear();
        if (modeloContratos.isEmpty()) {
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            if (getModulo().getId() != -1) {
                modeloContratos = modeloContratoDao.pesquisaTodosPorModulo(modulo.getId(), SessaoCliente.get().getId());
            }
        }
        return modeloContratos;
    }

    public void setModeloContratos(List<ModeloContrato> modeloContratos) {
        this.modeloContratos = modeloContratos;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public List<ModeloContratoServico> getListaModeloContratoServico() {
        if (modeloContrato.getId() != -1) {
            if (listaModeloContratoServico.isEmpty()) {
                ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
                listaModeloContratoServico = modeloContratoDao.pesquisaModeloContratoServico(modeloContrato.getId());
            }
        }
        return listaModeloContratoServico;
    }

    public void setListaModeloContratoServico(List<ModeloContratoServico> listaModeloContratoServico) {
        this.listaModeloContratoServico = listaModeloContratoServico;
    }

    public int getIdIndexServicos() {
        return idIndexServicos;
    }

    public void setIdIndexServicos(int idIndexServicos) {
        this.idIndexServicos = idIndexServicos;
    }

    public ModeloContratoServico getModeloContratoServico() {
        return modeloContratoServico;
    }

    public void setModeloContratoServico(ModeloContratoServico modeloContratoServico) {
        this.modeloContratoServico = modeloContratoServico;
    }

    public Modulo getModulo() {
        if (Sessions.exists("idModulo")) {
            int idMod = Sessions.getInteger("idModulo");
            if (idMod != 0) {
                Dao dao = new Dao();
                modulo = (Modulo) dao.find(new Modulo(), idMod);
            }
        }
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getMsgServico() {
        return msgServico;
    }

    public void setMsgServico(String msgServico) {
        this.msgServico = msgServico;
    }

    public ModeloContratoCampos getModeloContratoCampos() {
        return modeloContratoCampos;
    }

    public void setModeloContratoCampos(ModeloContratoCampos modeloContratoCampos) {
        this.modeloContratoCampos = modeloContratoCampos;
    }

    public List<ModeloContratoCampos> getListaModeloContratoCampos(String tipoLista) {
        if (listaModeloContratoCampos.isEmpty()) {
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            if (tipoLista.equals("this")) {
                if (Sessions.exists("idModulo")) {
                    int idMod = Sessions.getInteger("idModulo");
                    if (idMod != 0) {
                        if (descricaoPesquisa.equals("")) {
                            listaModeloContratoCampos = (List<ModeloContratoCampos>) modeloContratoDao.listaModeloContratoCampo(idMod);
                        } else {
                            listaModeloContratoCampos = (List<ModeloContratoCampos>) modeloContratoDao.listaModeloContratoCampo(idMod, descricaoPesquisa);
                        }
                    }
                }
            } else {
                listaModeloContratoCampos = (List<ModeloContratoCampos>) modeloContratoDao.listaModeloContratoCampo(Integer.parseInt(listModulos.get(idModulo).getDescription()));
            }
        }
        return listaModeloContratoCampos;
    }

    public void setListaModeloContratoCampos(List<ModeloContratoCampos> listaModeloContratoCampos) {
        this.listaModeloContratoCampos = listaModeloContratoCampos;
    }

    public List<SelectItem> getListModulos() {
        if (listModulos.isEmpty()) {
            ModeloContratoDao modeloContratoDao = new ModeloContratoDao();
            List<Modulo> lista = (List<Modulo>) modeloContratoDao.listaModulosModeloContratoCampos();
            for (int i = 0; i < lista.size(); i++) {
                listModulos.add(new SelectItem(i, lista.get(i).getDescricao(), Integer.toString(lista.get(i).getId())));
            }
        }
        return listModulos;
    }

    public void setListModulos(List<SelectItem> listModulos) {
        this.listModulos = listModulos;
    }

    public Integer getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Integer idModulo) {
        this.idModulo = idModulo;
    }

    public Integer getIdModulo2() {
        return idModulo2;
    }

    public void setIdModulo2(Integer idModulo2) {
        this.idModulo2 = idModulo2;
    }

    public List<SelectItem> getListModulos2() {
        if (listModulos2.isEmpty()) {
            Dao dao = new Dao();
            List<Modulo> list = (List<Modulo>) dao.list(new Modulo());
            for (int i = 0; i < list.size(); i++) {
                listModulos2.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listModulos2;
    }

    public void setListModulos2(List<SelectItem> listModulos2) {
        this.listModulos2 = listModulos2;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public void upload(FileUploadEvent event) {
        if (modeloContrato.getId() != -1) {
            ConfiguracaoUpload cu = new ConfiguracaoUpload();
            cu.setArquivo(event.getFile().getFileName());
            cu.setDiretorio("Arquivos/contrato/" + modeloContrato.getId());
            cu.setEvent(event);
            if (Upload.enviar(cu, true)) {
                listaArquivos.clear();
            }
        }
    }

    public void excluirArquivo(Integer index) {
        if (Dirs.remove("Arquivos/contrato/" + modeloContrato.getId() + "/" + (String) ((DataObject) listaArquivos.get(index)).getArgumento1())) {
            listaArquivos.remove(index);
            listaArquivos.clear();
            getListaArquivos();
        }
    }

    public List getListaArquivos() {
        if (modeloContrato.getId() != -1) {
            if (listaArquivos.isEmpty()) {
                listaArquivos = Dirs.listFiles("Arquivos/contrato/" + modeloContrato.getId());
                if (listaArquivos.size() > 0) {
                    setQuantidadeAnexo(listaArquivos.size());
                } else {
                    setQuantidadeAnexo(0);
                }
            }
        }
        return listaArquivos;
    }

    public void setListaArquivos(List listaArquivos) {
        this.listaArquivos = listaArquivos;
    }

    public Integer getQuantidadeAnexo() {
        return quantidadeAnexo;
    }

    public void setQuantidadeAnexo(Integer quantidadeAnexo) {
        this.quantidadeAnexo = quantidadeAnexo;
    }

    public List<ModeloDocumentos> getListModeloDocumentos() {
        if (listModeloDocumentos.isEmpty()) {
            ModeloDocumentosDao modeloDocumentosDao = new ModeloDocumentosDao();
            listModeloDocumentos = modeloDocumentosDao.pesquisaTodosPorRotina(73);
        }
        return listModeloDocumentos;
    }

    public void setListModeloDocumentos(List<ModeloDocumentos> listModeloDocumentos) {
        this.listModeloDocumentos = listModeloDocumentos;
    }

    public void addModeloDocumento() {

    }

    public void removeModeloDocumento(ModeloDocumentos md) {
        if (md.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            if (dao.delete(md)) {
                dao.commit();
                Messages.info("Sucesso", "Model de documento removido");
                listModeloDocumentos.clear();
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao remover!");
            }
        }
    }
}
