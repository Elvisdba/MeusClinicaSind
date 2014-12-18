package br.com.clinicaintegrada.fichamedica.dao;

import br.com.clinicaintegrada.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SisNotificacaoDao extends DB {

    /**
     *
     * @param idPessoa
     * @return
     */
    public List findAllByPessoaNaoVisualizado(Integer idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT SN FROM SisNotificacao AS SN WHERE SN.destinatario.id = :idPessoa AND SN.visualizado IS NULL ORDER BY SN.lancamento ASC, SN.horaLancamento ASC");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    /**
     *
     * @param idPessoa
     * @return
     */
    public List findAllByPessoaVisualizado(Integer idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT SN FROM SisNotificacao AS SN WHERE SN.destinatario.id = :idPessoa ORDER BY SN.lancamento ASC, SN.horaLancamento ASC");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
    
    /**
     *
     * @param idPessoa
     * @return
     */
    public List findAllByPessoaOcultados(Integer idPessoa) {
        try {
            Query query = getEntityManager().createQuery("SELECT SN FROM SisNotificacao AS SN WHERE SN.destinatario.id = :idPessoa ORDER BY SN.lancamento ASC, SN.horaLancamento ASC");
            query.setParameter("pessoa", idPessoa);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
