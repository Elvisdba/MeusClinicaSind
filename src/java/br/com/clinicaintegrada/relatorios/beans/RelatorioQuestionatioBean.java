package br.com.clinicaintegrada.relatorios.beans;

import br.com.clinicaintegrada.questionario.RespostaLote;
import br.com.clinicaintegrada.relatorios.dao.RelatorioQuestionarioDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Jasper;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class RelatorioQuestionatioBean implements Serializable {

    public void print(Integer resposta_lote_id) throws FileNotFoundException {
        RelatorioQuestionarioDao rqd = new RelatorioQuestionarioDao();
        rqd.setIS_QUERY(true);
        RespostaLote respostaLote = (RespostaLote) new Dao().find(new RespostaLote(), resposta_lote_id);
        List list = rqd.find(respostaLote);
        // dBExternal.setDatabase(SessaoCliente.get().getIdentifica());
        // dBExternal.setDatabase("ClinicaIntegradaProducao");
        // dBExternal.setPassword("989899");
        // dBExternal.setUrl("192.168.1.160");
        Jasper.IS_REPORT_CONNECTION = true;
        Jasper.IS_QUERY_STRING = true;
        Jasper.QUERY_STRING = rqd.getQUERY();
        Jasper.IS_HEADER = true;
        Jasper.TYPE = "default";
        Map map = new HashMap();
        map.put("equipe_nome", respostaLote.getEquipe().getPessoa().getNome());
        map.put("lancamento", respostaLote.getLancamento());
        map.put("lote_id", respostaLote.getId());
        Jasper.printReports("FICHA_QUESTIONARIO.jasper", respostaLote.getQuestionario().getDescricao(), new ArrayList<>(), map);
    }
}
