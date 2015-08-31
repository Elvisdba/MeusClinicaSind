package br.com.clinicaintegrada.administrativo.beans;

import javax.faces.bean.ManagedBean;
import br.com.clinicaintegrada.coordenacao.Contrato;
import java.io.Serializable;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ContratoDetalhesBean implements Serializable {

    private Contrato contrato;

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato c) {
        if (c != null) {
            contrato = c;
        }
    }
}
