package br.com.clinicaintegrada.converter;

import br.com.clinicaintegrada.utils.Moeda;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean
public class ToPercent implements Converter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return (String) value; // Or (value != null) ? value.toString().toUpperCase() : null;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null) {
            if (value.isEmpty()) {
                value = "0";
            }
            String  somenteLetras = value.replaceAll("[^a-zA-Z]", "");
            if (somenteLetras.length() > 0) {
                return null;
            }
            value = value.replace("-", "");
            if (value.isEmpty()) {
                return null;
            }
            try {
                return Moeda.substituiVirgula(Moeda.converteR$(value));
            } catch (Exception e) {
                return "0";
            }
        }
        return null;
    }

}
