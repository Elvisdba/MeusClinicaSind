package br.com.clinicaintegrada.converter;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

@ManagedBean
public class ThisNumber implements Converter {

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            return (String) value; // Or (value != null) ? value.toString().toUpperCase() : null;            
        } catch (Exception e) {
            return "0";
        }
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            return (value != null) ? value.replaceAll("[^0-9]", "") : null;
        } catch (Exception e) {
            return "0";
        }
    }
}
