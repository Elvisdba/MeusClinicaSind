package br.com.clinicaintegrada.sistema.beans;

import br.com.clinicaintegrada.fichamedica.dao.SisNotificacaoDao;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.sistema.SisNotificacao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SisNotificacaoBean implements Serializable {

    private SisNotificacao sisNotificacao;
    private List<SisNotificacao> listSisNotificacaoPendentes;
    private List<SisNotificacao> listSisNotificacao;
    private Boolean ocultado;

    @PostConstruct
    public void init() {
        sisNotificacao = new SisNotificacao();
        listSisNotificacaoPendentes = new ArrayList<>();
        listSisNotificacao = new ArrayList<>();
        ocultado = false;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("sisNotificacaoBean");
    }

    public void clear() {
        Sessions.remove("sisNotificacaoBean");
    }

    public SisNotificacao getSisNotificacao() {
        return sisNotificacao;
    }

    public void setSisNotificacao(SisNotificacao sisNotificacao) {
        this.sisNotificacao = sisNotificacao;
    }

    public List<SisNotificacao> getListSisNotificacao() {
        if (listSisNotificacao.isEmpty()) {
            SisNotificacaoDao sisNotificacaoDao = new SisNotificacaoDao();
            if (ocultado) {
                listSisNotificacao = sisNotificacaoDao.findAllByPessoaOcultados(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId());
            } else {
                listSisNotificacao = sisNotificacaoDao.findAllByPessoaVisualizado(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId());
            }
        }
        return listSisNotificacao;
    }

    public void setListSisNotificacao(List<SisNotificacao> listSisNotificacao) {
        this.listSisNotificacao = listSisNotificacao;
    }

    public void viewed() {
        if (!listSisNotificacaoPendentes.isEmpty()) {
            Dao dao = new Dao();
            for (int i = 0; i < listSisNotificacaoPendentes.size(); i++) {
                if (listSisNotificacaoPendentes.get(i).getVisualizado() == null) {
                    listSisNotificacaoPendentes.get(i).setVisualizado(new Date());
                    listSisNotificacaoPendentes.get(i).setHoraVisualizado(DataHoje.hora());
                    dao.update(listSisNotificacaoPendentes.get(i), true);
                }
            }
        }
        listSisNotificacaoPendentes.clear();
    }

    public void delete(SisNotificacao o) {
        Dao dao = new Dao();
        if (o.getId() != -1) {
            if (dao.delete(o, true)) {
                listSisNotificacao.clear();
            }
        }
    }

    public List<SisNotificacao> getListSisNotificacaoPendentes() {
        if (listSisNotificacaoPendentes.isEmpty()) {
            SisNotificacaoDao sisNotificacaoDao = new SisNotificacaoDao();
            listSisNotificacaoPendentes = sisNotificacaoDao.findAllByPessoaNaoVisualizado(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId());
        }
        return listSisNotificacaoPendentes;
    }

    public void setListSisNotificacaoPendentes(List<SisNotificacao> listSisNotificacaoPendentes) {
        this.listSisNotificacaoPendentes = listSisNotificacaoPendentes;
    }

    public Boolean getOcultado() {
        return ocultado;
    }

    public void setOcultado(Boolean ocultado) {
        this.ocultado = ocultado;
    }

}
