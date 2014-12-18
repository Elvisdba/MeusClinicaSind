package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.endereco.Cidade;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.FilialCidade;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.dao.FilialCidadeDao;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.DataObject;
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
public class FilialBean implements Serializable {

    private Filial filial;
    private List<Filial> listaFilial;
    private String renderAdicionar;
    private Integer idFilial;
    private List<DataObject> listaCidade;
    private boolean adicionarLista;
    private List<SelectItem> result;

    @PostConstruct
    @SuppressWarnings("empty-statement")
    public void init() {
        filial = new Filial();
        listaFilial = new ArrayList();;
        renderAdicionar = "false";
        idFilial = 0;
        listaCidade = new ArrayList();
        adicionarLista = false;
        result = new ArrayList<>();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("filialBean");
        Sessions.remove("juridicaPesquisa");
    }

    public void clear() {
        Sessions.remove("filialBean");
    }

    public Filial getFilial() {
        if (Sessions.exists("juridicaPesquisa")) {
            filial.setFilial((Juridica) Sessions.getObject("juridicaPesquisa", true));
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public void btnAddFilial() {

        FilialDao db = new FilialDao();

        if (!db.pesquisaFilialExiste(filial.getFilial().getId()).isEmpty()) {
            Messages.warn("Erro", "Filial já existe!");
            return;
        }

        DaoInterface di = new Dao();
        Logger Logger = new Logger();
        di.openTransaction();

        if (filial.getId() == -1) {
            filial.setMatriz((Juridica) di.find(new Juridica(), SessaoCliente.get().getIdJuridica()));
            if (di.save(filial)) {
                Logger.save(
                        "ID: " + filial.getId()
                        + " - Filial: " + filial.getFilial().getPessoa().getNome()
                );
                Messages.info("Sucesso", "Registro adicionado com sucesso");
                di.commit();
            } else {
                Messages.warn("Erro", "Falha ao adicionar a filial!");
                di.rollback();
            }
            filial = new Filial();
            adicionarLista = true;
        } else {
            Filial f = (Filial) di.find(filial);
            String beforeUpdate
                    = "ID: " + f.getId()
                    + " - Filial: " + f.getFilial().getPessoa().getNome();
            if (di.update(filial)) {
                Logger.update(beforeUpdate,
                        "ID: " + filial.getId()
                        + " - Filial: " + filial.getFilial().getPessoa().getNome()
                );
                Messages.info("Sucesso", "Registro atualizado com sucesso");
                di.commit();
            } else {
                Messages.warn("Erro", "Falha ao atualizar a filial!");
                di.rollback();
            }
        }
        listaFilial.clear();
        filial = new Filial();
        renderAdicionar = "false";
    }

    public void saveCidadeFilial(Cidade cid, int index) {
        FilialCidadeDao db = new FilialCidadeDao();
        FilialCidade filialCidade;
        Logger Logger = new Logger();
        DaoInterface di = new Dao();
        int iCidade = cid.getId();
        int iFilial = Integer.parseInt(result.get(index).getDescription());
        if (iFilial != -1) {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                filialCidade.setFilial((Filial) di.find(new Filial(), iFilial));
                Messages.info("Sucesso", "Cidade atualizada com Sucesso!");
                if (di.update(filialCidade, true)) {
                    Logger.update("", "Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
            } else {
                filialCidade = new FilialCidade();
                filialCidade.setCidade((Cidade) di.find(new Cidade(), iCidade));
                filialCidade.setFilial((Filial) di.find(new Filial(), iFilial));
                Messages.info("Sucesso", "Cidade atualizada com Sucesso!");
                if (di.save(filialCidade, true)) {
                    Logger.save("Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
            }
        } else {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                if (di.delete(filialCidade, true)) {
                    Logger.save("Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
                Messages.info("Sucesso", "Cidade atualizada com Sucesso!");
            }
        }
        result = new ArrayList();

    }

    public String novo() {
        filial = new Filial();
        return "filial";
    }

    public void delete(Filial fi) {
        if (fi.getId() != -1) {
            Logger Logger = new Logger();
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(fi)) {
                Logger.delete(
                        "ID: " + fi.getId()
                        + " - Filial: " + fi.getFilial().getPessoa().getNome()
                );
                Messages.info("Sucesso", "Filial excluída com sucesso");
                listaFilial.clear();
                result.clear();
                filial = new Filial();
                getListaFilialSemMatriz().clear();
                di.commit();
            } else {
                Messages.warn("Erro", "Não foi possível excluir essa filial. Verifique se há vínculos!");
                listaFilial.clear();
                filial = new Filial();
                di.rollback();
            }
        }
    }

    public List<Filial> getListaFilial() {
        FilialDao db = new FilialDao();
        listaFilial = db.pesquisaTodasCliente();
        return listaFilial;
    }

    public List<Filial> getListaFilialSemMatriz() {
        if (listaFilial.isEmpty()) {
            FilialDao db = new FilialDao();
            listaFilial = db.pesquisaTodasCliente();
        }
        return listaFilial;
    }

    public String getRenderAdicionar() {
        if (filial.getFilial().getId() != -1) {
            renderAdicionar = "true";
        } else {
            renderAdicionar = "false";
        }
        return renderAdicionar;
    }

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public List<DataObject> getListaCidade() {
        if (listaCidade.isEmpty()) {
//            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
//            //List<GrupoCidades> lis = dbCids.pesquisaTodos();
//            List<Cidade> lis = dbCids.pesquisaCidadesBase();
//
//            DaoInterface di = new Dao();
//            List<FilialCidade> fc = (List<FilialCidade>) di.list(new FilialCidade());
//
//            if (!lis.isEmpty()) {
//                boolean tem;
//                for (int i = 0; i < lis.size(); i++) {
//                    tem = false;
//                    for (int w = 0; w < fc.size(); w++) {
//                        if (lis.get(i).getId() == fc.get(w).getCidade().getId()) {
//                            for (int u = 0; u < getResult().size(); u++) {
//                                if (fc.get(w).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())) {
//                                    listaCidade.add(new DataObject((Cidade) di.find(new Cidade(), lis.get(i).getId()), u));
//                                    tem = true;
//                                }
//                                if (tem) {
//                                    break;
//                                }
//                            }
//                            if (tem) {
//                                break;
//                            }
//                        }
//                        if (tem) {
//                            break;
//                        }
//                    }
//                    if (!tem) {
//                        listaCidade.add(new DataObject((Cidade) di.find(new Cidade(), lis.get(i).getId()), 0));
//                    }
//                }
//            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<DataObject> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public List<SelectItem> getResult() {
        if ((result.isEmpty()) || (this.adicionarLista)) {
            result.clear();
            FilialDao db = new FilialDao();
            List<Filial> fi = db.pesquisaTodasCliente();
            result.add(new SelectItem(0,
                    " -- NENHUM -- ",
                    "-1"));
            for (int i = 0; i < fi.size(); i++) {
                result.add(new SelectItem(i + 1,
                        fi.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(fi.get(i).getId()))
                );
            }
            this.adicionarLista = false;
//            atualizarIndexFilial();
        }
        return result;
    }

//    public void atualizarIndexFilial(){
//        FilialCidadeDao db = new FilialCidadeDao();
//        FilialDao dbF = new FilialDao();
//        List<FilialCidade> fc = db.pesquisaTodos();
//        //List<Filial> fili = dbF.pesquisaTodos();
//        boolean tem;
//        for(int i = 0; i < fc.size();i++){
//            tem = false;
//            for(int w = 0; w < listaCidade.size(); w++){
//                if (!tem){
//                    if(fc.get(i).getCidade().getId() == listaCidade.get(w).getCidade().getId()){
//                        for (int u = 0; u < result.size();u++){
//                            if (fc.get(i).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())){
//                                listaCidade.get(w).setIndiceFilial(u);
//                                tem = true;
//                                break;
//                            }
//                        }
//                    }
//                }else break;
//            }
//        }
//    }
    public void setResult(List<SelectItem> result) {
        this.result = result;
    }

}
