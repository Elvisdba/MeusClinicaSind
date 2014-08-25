package br.com.clinicaintegrada.utils;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class Request {

    public static String getParam(String parameter) {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String param = hsr.getParameter(parameter);
        return param;
    }

}
