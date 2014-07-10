package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.seguranca.Cliente;
import br.com.rtools.utilitarios.Sessions;
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
