package br.com.clinicaintegrada.agenda.beans;

import br.com.clinicaintegrada.agenda.AgendaContato;
import br.com.clinicaintegrada.agenda.dao.AgendaTelefoneDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class AgendaNotificaoBean implements Serializable {

    private String dataHoje;

    @PostConstruct
    public void init() {
        dataHoje = DataHoje.data();
    }

    public List<AgendaContato> getListAniversariantes() {
        AgendaTelefoneDao atdb = new AgendaTelefoneDao();
        return atdb.pesquisaAniversariantesPorPeriodo(SessaoCliente.get().getId());
    }

    public String getDataHoje() {
        return dataHoje;
    }

    public void setDataHoje(String dataHoje) {
        this.dataHoje = dataHoje;
    }

}
