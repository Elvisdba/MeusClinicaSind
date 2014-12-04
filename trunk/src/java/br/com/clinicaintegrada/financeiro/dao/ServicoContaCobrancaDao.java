package br.com.clinicaintegrada.financeiro.dao;

import br.com.clinicaintegrada.financeiro.ServicoContaCobranca;
import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class ServicoContaCobrancaDao extends DB {

    // NOVO   
    public List listContaCobrancaAtivoAssociativo() {
        try {
            //Query qry = getEntityManager().createQuery("SELECT CC FROM ContaCobranca AS CC WHERE CC.ativo = true AND CC.associativo = true AND CC.arrecadacao = false");
            Query qry = getEntityManager().createQuery("SELECT CC FROM ContaCobranca AS CC WHERE CC.ativo = true");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    // DO SINDICAL
    public List pesquisaTodosTipoUm() {
        try {
            Query qry = getEntityManager().createQuery("SELECT s FROM ServicoContaCobranca s WHERE s.tipoServico.id = 1");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaTodosFiltrado() {
//        int idServicos = 0;
//        int idContaCobranca = 0;
        List<ServicoContaCobranca> listServ = new ArrayList();
        try {
            String textQuery = "     select id_servicos, id_conta_cobranca "
                    + "       from fin_servico_conta_cobranca s "
                    + "   group by id_servicos, id_conta_cobranca "
                    + "   order by id_servicos, id_conta_cobranca";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            if (!qry.getResultList().isEmpty()) {
                List vetor = qry.getResultList();
                for (int i = 0; i < vetor.size(); i++) {
//                    idServicos = (Integer) ((Vector) vetor.get(i)).get(0);
//                    idContaCobranca = (Integer) ((Vector) vetor.get(i)).get(1);
                    int idServicos = (Integer) ((List) vetor.get(i)).get(0);
                    int idContaCobranca = (Integer) ((List) vetor.get(i)).get(1);
                    String textQuery2 = "select id from  fin_servico_conta_cobranca where  id_servicos = " + idServicos + " and  id_conta_cobranca = " + idContaCobranca;
                    Query qry2 = getEntityManager().createNativeQuery(textQuery2).setMaxResults(1);
                    if (!qry2.getResultList().isEmpty()) {
                        List vetor2 = qry2.getResultList();
                        if (!vetor2.isEmpty()) {
                            for (int u = 0; u < vetor2.size(); u++) {
                                //listServ.add(pesquisaCodigo( (Integer) ((Vector) vetor2.get(u)).get(0) ));
                                //listServ.add(pesquisaCodigo((Integer) ((List) vetor2.get(u)).get(0)));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return listServ;
    }

    public List pesquisaTodosFiltradoAtivo() {
//        int idServicos = 0;
//        int idContaCobranca = 0;
//        List vetor;
//        List vetor2;
        List<ServicoContaCobranca> listServ = new ArrayList();
//        String textQuery = "";
//        String textQuery2 = "";
        try {
            String textQuery = "   select id_servicos, id_conta_cobranca "
                    + "     from fin_servico_conta_cobranca s "
                    + " group by id_servicos, id_conta_cobranca "
                    + " order by id_servicos, id_conta_cobranca";
            Query qry = getEntityManager().createNativeQuery(textQuery);
            if (!qry.getResultList().isEmpty()) {
                List vetor = qry.getResultList();
                for (int i = 0; i < vetor.size(); i++) {
//                    int idServicos = (Integer) ((Vector) vetor.get(i)).get(0);
//                    int idContaCobranca = (Integer) ((Vector) vetor.get(i)).get(1);
                    int idServicos = (Integer) ((List) vetor.get(i)).get(0);
                    int idContaCobranca = (Integer) ((List) vetor.get(i)).get(1);
                    String textQuery2 = " select id from fin_servico_conta_cobranca "
                            + "  where  id_servicos = " + idServicos + " "
                            + "    and id_conta_cobranca = " + idContaCobranca + " "
                            + "    and id_conta_cobranca in (select id from fin_conta_cobranca where is_ativo = true)";
                    Query qry2 = getEntityManager().createNativeQuery(textQuery2).setMaxResults(1);
                    if (!qry2.getResultList().isEmpty()) {
                        List vetor2 = qry2.getResultList();
                        for (int u = 0; u < vetor2.size(); u++) {
                            //listServ.add(pesquisaCodigo( (Integer) ((Vector) vetor2.get(u)).get(0) ));
                            //listServ.add(pesquisaCodigo((Integer) ((List) vetor2.get(u)).get(0)));
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return listServ;
    }

    public List pesquisaServPorIdServIdTipoServ(int servico, int tipoServico) {
        try {
            Query qry = getEntityManager().createQuery("select s from ServicoContaCobranca s "
                    + " where s.servicos.id = " + servico
                    + "   and s.tipoServico.id = " + tipoServico);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public ServicoContaCobranca pesquisaServPorIdServIdTipoServIdContCobranca(int servico, int tipoServico, int contaCobranca) {
        try {
            Query qry = getEntityManager().createQuery("select s from ServicoContaCobranca s "
                    + " where s.contaCobranca.id <> " + contaCobranca
                    + "   and s.servicos.id = " + servico
                    + "   and s.tipoServico.id = " + tipoServico);
            return (ServicoContaCobranca) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List pesquisaServPorIdServ(int servico) {
        try {
            Query qry = getEntityManager().createQuery("select s from ServicoContaCobranca s "
                    + " where s.servicos.id = " + servico);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    public List listaContaCobrancaAtivoArrecadacao() {
        try {
            Query qry = getEntityManager().createQuery("select co from ContaCobranca co where co.ativo = true and co.arrecadacao = true and co.associativo = false");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

}