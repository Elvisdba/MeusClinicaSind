package br.com.clinicaintegrada.relatorios.beans;

import br.com.clinicaintegrada.principal.DBExternal;
import br.com.clinicaintegrada.questionario.RespostaLote;
import br.com.clinicaintegrada.relatorios.dao.RelatorioQuestionarioDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Jasper;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class RelatorioQuestionatioBean implements Serializable {

    public void print(Integer resposta_lote_id) throws FileNotFoundException {
        RelatorioQuestionarioDao rqd = new RelatorioQuestionarioDao();
        rqd.setIS_QUERY(true);
        List list = rqd.find(resposta_lote_id);
        DBExternal dBExternal = new DBExternal();
        dBExternal.setDatabase(SessaoCliente.get().getPersistence());
        // dBExternal.setDatabase("ClinicaIntegradaProducao");
        dBExternal.setPassword("989899");
        dBExternal.setUrl("192.168.1.60");
        Jasper.IS_REPORT_CONNECTION = true;
        Jasper.dbe = dBExternal;
        Jasper.IS_QUERY_STRING = true;
        Jasper.QUERY_STRING = rqd.getQUERY();
        RespostaLote respostaLote = (RespostaLote) new Dao().find(new RespostaLote(), resposta_lote_id);
        Jasper.IS_HEADER = true;
        Jasper.TYPE = "default";
        Jasper.printReports("FICHA_QUESTIONARIO.jasper", respostaLote.getQuestionario().getDescricao(), new ArrayList());
    }
}
