package br.com.clinicaintegrada.seguranca.utilitarios;

import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SegurancaUtilitariosBean implements Serializable {

    public boolean getExisteMacFilial() {
        return MacFilial.getAcessoFilial().getId() != -1;
    }

    public Usuario getSessaoUsuario() {
        return (Usuario) Sessions.getObject("sessaoUsuario");
    }

    public boolean getExisteMacFilialComCaixa() {
        MacFilial macFilial = MacFilial.getAcessoFilial();
        if (macFilial.getId() != -1 && macFilial.getCaixa() != null) {
            return true;
        }
        return false;
    }

}
