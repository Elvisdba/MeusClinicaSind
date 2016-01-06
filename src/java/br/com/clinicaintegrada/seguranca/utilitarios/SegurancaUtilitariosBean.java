package br.com.clinicaintegrada.seguranca.utilitarios;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.dao.EquipeDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SegurancaUtilitariosBean implements Serializable {

    public boolean getExisteMacFilial() {
        return MacFilial.getAcessoFilial().getId() != null;
    }

    public Usuario getSessaoUsuario() {
        return (Usuario) Sessions.getObject("sessaoUsuario");
    }

    public boolean getExisteMacFilialComCaixa() {
        MacFilial macFilial = MacFilial.getAcessoFilial();
        if (macFilial.getId() != null && macFilial.getCaixa() != null) {
            return true;
        }
        return false;
    }

    public Boolean getExisteEquipe() {
            Equipe e = new EquipeDao().findByPessoaAndFuncaoEquipe(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId());
        return e != null;
    }

    public Boolean getExisteEquipe(Integer funcao_equipe_id) {
        Equipe e = new EquipeDao().findByPessoaAndFuncaoEquipe(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId(), funcao_equipe_id);
        return e != null;
    }

}
