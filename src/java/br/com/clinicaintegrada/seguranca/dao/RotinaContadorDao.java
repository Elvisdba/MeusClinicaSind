package br.com.clinicaintegrada.seguranca.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.RotinaContador;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;

@SuppressWarnings("unchecked")
public class RotinaContadorDao extends DB {

    private String order = "RC.contador DESC";
    private String orderNative = "RC.nr_contador DESC";

    public List findRotinasByRotinaTela(Integer rotina_tela_id) {
        return findRotinasByRotinaTela(rotina_tela_id, null);
    }

    public List findRotinasByRotinaTela(Integer rotina_tela_id, Integer usuario_id) {
        try {
            Query query;
            if (usuario_id == null) {
                query = getEntityManager().createQuery("SELECT RC.rotinaCombo FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.usuario IS NULL ORDER BY " + order);
            } else {
                query = getEntityManager().createQuery("SELECT RC.rotinaCombo FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.usuario.id = :usuario_id ORDER BY " + order);
                query.setParameter("usuario_id", usuario_id);
            }
            query.setParameter("rotina_tela_id", rotina_tela_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List findRotinaContadorByRotinaTela(Integer rotina_tela_id) {
        return findRotinaContadorByRotinaTela(rotina_tela_id, null);
    }

    public List findRotinaContadorByRotinaTela(Integer rotina_tela_id, Integer usuario_id) {
        try {
            Query query;
            if (usuario_id == null) {
                query = getEntityManager().createQuery("SELECT RC FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.usuario IS NULL ORDER BY " + order);
            } else {
                query = getEntityManager().createQuery("SELECT RC FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.usuario.id = :usuario_id ORDER BY " + order);
                query.setParameter("usuario_id", usuario_id);
            }
            query.setParameter("rotina_tela_id", rotina_tela_id);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Integer findMaxRotinaIdByRotinaTela(Integer rotina_tela_id) {
        return findMaxRotinaIdByRotinaTela(rotina_tela_id, null);
    }

    public Integer findMaxRotinaIdByRotinaTela(Integer rotina_tela_id, Integer usuario_id) {
        try {
            Query query;
            if (usuario_id == null) {
                query = getEntityManager().createNativeQuery("SELECT RC.id_rotina_combo, RC.nr_contador FROM seg_rotina_contador AS RC WHERE RC.id_rotina_tela = ? ORDER BY " + orderNative);
                query.setParameter("1", rotina_tela_id);
            } else {
                query = getEntityManager().createNativeQuery("SELECT RC.id_rotina_combo, RC.nr_contador FROM seg_rotina_contador AS RC WHERE RC.id_rotina_tela = ? AND RC.id_usuario = ? ORDER BY " + orderNative);
                query.setParameter("1", rotina_tela_id);
                query.setParameter("2", usuario_id);
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return (Integer) Integer.parseInt(((List) list.get(0)).get(0).toString());
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public RotinaContador findByRotinaTelaAndRotinaCombo(Integer rotina_tela_id, Integer rotina_combo_id) {
        return findByRotinaTelaAndRotinaCombo(rotina_tela_id, rotina_combo_id, null);
    }

    public RotinaContador findByRotinaTelaAndRotinaCombo(Integer rotina_tela_id, Integer rotina_combo_id, Integer usuario_id) {
        try {
            Query query;
            if (usuario_id == null) {
                query = getEntityManager().createQuery("SELECT RC FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.rotinaCombo.id = :rotina_combo_id AND RC.usuario IS NULL ");
            } else {
                query = getEntityManager().createQuery("SELECT RC FROM RotinaContador AS RC WHERE RC.rotinaTela.id = :rotina_tela_id AND RC.rotinaCombo.id = :rotina_combo_id AND RC.usuario.id = :usuario_id ");
                query.setParameter("usuario_id", usuario_id);
            }
            query.setParameter("rotina_tela_id", rotina_tela_id);
            query.setParameter("rotina_combo_id", rotina_combo_id);
            List list = query.getResultList();
            if (!list.isEmpty() && list.size() == 1) {
                return (RotinaContador) list.get(0);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public Boolean incrementar(Integer rotina_tela_id, Integer rotina_combo_id) {
        return incrementar(rotina_tela_id, rotina_combo_id, null);
    }

    public Boolean incrementar(Integer rotina_tela_id, Integer rotina_combo_id, Integer usuario_id) {
        try {
            RotinaContador rc = findByRotinaTelaAndRotinaCombo(rotina_tela_id, rotina_combo_id, usuario_id);
            if (rc == null) {
                rc = new RotinaContador(null, (Rotina) new Dao().find(new Rotina(), rotina_tela_id), (Rotina) new Dao().find(new Rotina(), rotina_combo_id), 1, (Usuario) new Dao().find(new Usuario(), usuario_id));
                rc.setData(DataHoje.dataHoje());
                if (new Dao().save(rc, true)) {
                    return true;
                }
            } else {
                rc.setContador(rc.getContador() + 1);
                rc.setData(DataHoje.dataHoje());
                if (new Dao().update(rc, true)) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderNative() {
        return orderNative;
    }

    public void setOrderNative(String orderNative) {
        this.orderNative = orderNative;
    }

    public void orderData() {
        order = "RC.data DESC";
    }

    public void orderContador() {
        order = "RC.contador DESC";
    }

    public void orderDataString() {
        orderNative = "RC.dt_data DESC";
    }

    public void orderContadorString() {
        orderNative = "RC.nr_contador DESC";
    }

}
