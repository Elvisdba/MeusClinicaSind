package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.FTipoDocumento;
import br.com.clinicaintegrada.utils.Dao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@ViewScoped
@RequestScoped
public class FTipoDocumentoBean implements Serializable {

    private FTipoDocumento fTipoDocumento;
    private List<SelectItem> listFTipoDocumento;
    private String inIds;

    @PostConstruct
    public void init() {
        fTipoDocumento = new FTipoDocumento();
        listFTipoDocumento = new ArrayList<>();
        inIds = null;
    }

    @PreDestroy
    public void destroy() {
    }

    public void load() {
    }

    public FTipoDocumento getfTipoDocumento() {
        return fTipoDocumento;
    }

    public void setfTipoDocumento(FTipoDocumento fTipoDocumento) {
        this.fTipoDocumento = fTipoDocumento;
    }

    //  <f:attribute name="inIds" value="#{}" />    
    public List<SelectItem> getListFTipoDocumento() {
        if (listFTipoDocumento.isEmpty()) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
            String action = params.get("inIds");
            Dao dao = new Dao();
            List<FTipoDocumento> list;
            if (inIds == null || inIds.isEmpty()) {
                list = (List<FTipoDocumento>) dao.list(new FTipoDocumento());
            } else {
                list = (List<FTipoDocumento>) dao.find(new FTipoDocumento(), inIds);
            }
            for (int i = 0; i < list.size(); i++) {
                listFTipoDocumento.add(new SelectItem(list.get(i).getId(), list.get(i).getDescricao()));
            }
        }
        return listFTipoDocumento;
    }

    public void setListFTipoDocumento(List<SelectItem> listFTipoDocumento) {
        this.listFTipoDocumento = listFTipoDocumento;
    }

    public String getInIds() {
        return inIds;
    }

    public void setInIds(String inIds) {
        this.inIds = inIds;
    }
    
    public void addInIds(Object inIds) {
        this.inIds = inIds.toString();
    }
}
