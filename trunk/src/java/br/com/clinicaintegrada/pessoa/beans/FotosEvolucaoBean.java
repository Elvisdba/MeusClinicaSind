package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.beans.AgendamentoBean;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Fotos;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.FotosDao;
import br.com.clinicaintegrada.seguranca.Cliente;
import static br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean.getCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Sessions;
import static com.google.common.io.Files.copy;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@ManagedBean
@ViewScoped
public class FotosEvolucaoBean implements Serializable {

    private List<Fotos> listFotos;
    private String update;
    private String rotinaNome;
    private Integer contrato_id;
    private Boolean disable_delete_photo;

    @PostConstruct
    public void init() {
        this.listFotos = new ArrayList<>();
        update = ":form_fotos_evolucao:i_fotos_evolucao";
        contrato_id = 0;
        disable_delete_photo = false;
        rotinaNome = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("fotosEvolucaoBean");
    }

    public void load(Integer contrato_id) {
        this.contrato_id = contrato_id;
        this.listFotos.clear();
        this.listFotos = new FotosDao().findFotosByContrato(contrato_id);
    }

    public List<Fotos> getListFotos() {
        return listFotos;
    }

    public void setListFotos(List<Fotos> listFotos) {
        this.listFotos = listFotos;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setUpdateString(String update) {
        this.update = update;
    }

    public void delete(Fotos fotos) {
        File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica() + "/imagens/evolucao/" + fotos.getId() + ".png"));
        try {
            if (file.exists()) {
                if (file.delete()) {
                    if (new Dao().delete(fotos, true)) {
                        this.listFotos.clear();
                        this.listFotos = new FotosDao().findFotosByContrato(contrato_id);
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Adiciona a foto a evolução caso a mesma não exista.
     *
     * @param c
     */
    public void execute(Contrato c) {
        try {
            Dao dao = new Dao();
            String PATH_FILE = getId() + ".png";
            try {
                if (c == null) {
                    c = ((AgendamentoBean) Sessions.getObject("agendamentoBean")).getContrato();
                }
            } catch (Exception e) {
                return;
            }
            Integer id = c.getPaciente().getId();
            Fotos fotos = new Fotos();
            fotos.setId(null);
            fotos.setContrato(c);
            fotos.setDataString(DataHoje.data());
            FotosDao fotosDao = new FotosDao();
            if (fotosDao.findFotosByContrato(fotos.getContrato().getId()).isEmpty()) {
                dao.openTransaction();
                if (dao.save(fotos)) {
                    Dirs.create("imagens/evolucao");
                    File file_destino = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/evolucao/" + fotos.getId() + ".png"));
                    File fileFisica = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/fotos/" + c.getPaciente().getId() + ".png"));
                    if (!fileFisica.exists()) {
                        fileFisica = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/fotos/" + c.getPaciente().getId() + ".jpg"));
                    }
                    if (fileFisica.exists()) {
                        try {
                            copy(fileFisica, file_destino);
                        } catch (Exception e) {
                            dao.rollback();
                            return;
                        }
                        if (fileFisica.exists()) {
                            Fisica fisica = new FisicaDao().pesquisaFisicaPorPessoa(c.getPaciente().getId());
                            if (fisica.getDtFoto() == null) {
                                fisica.setDtFoto(DataHoje.dataHoje());
                                if (dao.update(fisica)) {
                                    dao.commit();
                                } else {
                                    dao.rollback();
                                }
                            } else {
                                dao.commit();
                            }
                        } else {
                            dao.commit();
                        }
                    } else {
                        dao.commit();
                    }
                } else {
                    // FILE_PERMANENT = "";
                    dao.rollback();
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String getId() {
        Integer id;
        id = new FotosDao().currval();
        if (id == null) {
            id = 1;
        }
        return "" + id;
    }

    public Boolean getDisable_delete_photo() {
        disable_delete_photo = false;
        if (rotinaNome.equals("contrato")) {
            disable_delete_photo = true;
        }
        return disable_delete_photo;
    }

    public void setDisable_delete_photo(Boolean disable_delete_photo) {
        this.disable_delete_photo = disable_delete_photo;
    }

    public String getRotinaNome() {
        return rotinaNome;
    }

    public void setRotinaNome(String rotinaNome) {
        this.rotinaNome = rotinaNome;
    }

}
