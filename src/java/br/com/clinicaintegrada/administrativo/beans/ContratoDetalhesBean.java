package br.com.clinicaintegrada.administrativo.beans;

import javax.faces.bean.ManagedBean;
import br.com.clinicaintegrada.coordenacao.Contrato;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class ContratoDetalhesBean {

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
