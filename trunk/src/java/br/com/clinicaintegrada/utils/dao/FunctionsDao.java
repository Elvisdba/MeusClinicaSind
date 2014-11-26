package br.com.clinicaintegrada.utils.dao;

import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import javax.persistence.Query;

public class FunctionsDao extends DB {

    /**
     * Gerar boletos e atualiza movimento automáticamente
     *
     * @return
     */
    public boolean gerarBoletos() {
        int idResponsavel = -1;
        try {
            Query query = getEntityManager().createNativeQuery(" SELECT func_geraBoleto(" + SessaoCliente.get().getId() + "); ");
            int bool = query.executeUpdate();
            if (bool == 1) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
