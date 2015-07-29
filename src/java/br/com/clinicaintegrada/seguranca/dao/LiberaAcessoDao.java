package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.LiberaAcesso;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;

public class LiberaAcessoDao extends DB {

    public LiberaAcesso findByMac(MacFilial mf) {
        try {
            Query query = getEntityManager().createQuery(" SELECT LA FROM LiberaAcesso AS LA WHERE LA.macFilial.id = :mac_filial_id AND LA.expira = :expira");
            query.setParameter("mac_filial_id", mf.getId());
            query.setParameter("solicitacao", DataHoje.dataHoje(), TemporalType.DATE);
            List list = query.getResultList();
            if (!query.getResultList().isEmpty() && list.size() == 1) {
                return (LiberaAcesso) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public LiberaAcesso findByMac(String mac) {
        try {
            Query query = getEntityManager().createQuery(" SELECT LA FROM LiberaAcesso AS LA WHERE LA.macFilial.mac = :mac AND LA.expira <= :expira");
            query.setParameter("mac", mac);
            query.setParameter("expira", DataHoje.dataHoje(), TemporalType.TIMESTAMP);
            List list = query.getResultList();
            if (!query.getResultList().isEmpty() && list.size() == 1) {
                return (LiberaAcesso) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Boolean clear() {
        try {
            getEntityManager().getTransaction().begin();
            Query query = getEntityManager().createNativeQuery(" DELETE FROM seg_libera_acesso WHERE dt_expira > CURRENT_DATE ");
            if (query.executeUpdate() == 0) {
                getEntityManager().getTransaction().rollback();
                return false;
            }
            getEntityManager().getTransaction().commit();
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
        return true;
    }
}
