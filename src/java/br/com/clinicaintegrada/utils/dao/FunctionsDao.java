package br.com.clinicaintegrada.utils.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import java.util.List;
import javax.persistence.Query;

public class FunctionsDao extends DB {

    /**
     * Gerar boletos e atualiza movimento automáticamente
     *
     * @return
     */
    public boolean gerarBoletos() {
        try {
            String queryString = " SELECT func_geraBoleto(" + SessaoCliente.get().getId() + ")";
            Query query = getEntityManager().createNativeQuery(queryString);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
