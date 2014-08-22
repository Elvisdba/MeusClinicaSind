package br.com.clinicaintegrada.seguranca.controleUsuario;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.Sessions;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SessaoCliente {

    public static Cliente get() {
        if (Sessions.exists("sessaoCliente")) {
            return ((Cliente) Sessions.getObject("sessaoCliente"));
        }
        return null;
    }

}
