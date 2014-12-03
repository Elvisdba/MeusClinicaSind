package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Baixa;
import br.com.clinicaintegrada.financeiro.CondicaoPagamento;
import br.com.clinicaintegrada.financeiro.Lote;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
//import br.com.clinicaintegrada.movimento.GerarMovimento;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.dao.UsuarioDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public final class AlterarMovimentoBean implements Serializable {

    private List<Movimento> listMovimento;
    private Movimento movimento;
    private Lote lote;
    private Baixa baixa;
    private int idServicos;
    private int idTipoServicos;
    private int idCondicaoPagamento;
    private List<SelectItem> listServico;
    private List<Servicos> selectServico;
    private List<SelectItem> listTipoServico;
    private List<TipoServico> selectTipoServico;
    private List<CondicaoPagamento> selectCondicao;
    private List<SelectItem> listCondicao;
    private String historico;

    @PostConstruct
    public void init() {
        listMovimento = new ArrayList<>();
        movimento = new Movimento();
        lote = new Lote();
        baixa = null;
        idServicos = 0;
        idTipoServicos = 0;
        idCondicaoPagamento = 0;
        listServico = new ArrayList<>();
        selectServico = new ArrayList();
        listTipoServico = new ArrayList<>();
        selectTipoServico = new ArrayList();
        selectCondicao = new ArrayList();
        listCondicao = new ArrayList<>();
        historico = "";
        this.getMovimento();
        this.getListServico();
        this.getListTipoServico();

        if (movimento.getId() != -1) {
            for (int i = 0; i < selectServico.size(); i++) {
                if (selectServico.get(i).getId() == movimento.getServicos().getId()) {
                    idServicos = i;
                    break;
                }
            }
            for (int i = 0; i < selectTipoServico.size(); i++) {
                if (selectTipoServico.get(i).getId() == movimento.getTipoServico().getId()) {
                    idTipoServicos = i;
                    break;
                }
            }
            for (int i = 0; i < selectCondicao.size(); i++) {
                if (selectCondicao.get(i).getId() == lote.getCondicaoPagamento().getId()) {
                    idCondicaoPagamento = i;
                    break;
                }
            }

        }
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("alterarMovimentoBean");
        Sessions.remove("usuarioPesquisa");
    }

    public List<SelectItem> getListCondicao() {
        if (listCondicao.isEmpty()) {
            Dao dao = new Dao();
            selectCondicao = dao.list(new CondicaoPagamento());
            for (int i = 0; i < selectCondicao.size(); i++) {
                listCondicao.add(new SelectItem(i, selectCondicao.get(i).getDescricao(), Integer.toString(selectCondicao.get(i).getId())));
            }
        }
        return listCondicao;
    }

    public List<SelectItem> getListServico() {
        if (listServico.isEmpty()) {
            selectServico = new Dao().list(new Servicos());
            for (int i = 0; i < selectServico.size(); i++) {
                listServico.add(new SelectItem(i, selectServico.get(i).getDescricao(), Integer.toString(selectServico.get(i).getId())));
            }
        }
        return listServico;
    }

    public List<SelectItem> getListTipoServico() {
        if (listTipoServico.isEmpty()) {
            selectTipoServico = new Dao().list(new TipoServico());
            for (int i = 0; i < selectTipoServico.size(); i++) {
                listTipoServico.add(new SelectItem(i, selectTipoServico.get(i).getDescricao(), Integer.toString(selectTipoServico.get(i).getId())));
            }
        }
        return listTipoServico;
    }

    // LISTA DE MOVIMENTOS PARA SER ALTERADO ---------------------------
    public List<Movimento> getListMovimento() {
        if (Sessions.exists("listMovimento")) {
            listMovimento = (List) Sessions.getList("listMovimento", true);
            Sessions.remove("listMovimento");
        }
        return listMovimento;
    }

    public void setListMovimento(List<Movimento> listMovimento) {
        this.listMovimento = listMovimento;
    }
    // ------------------------------------------------------
    // ------------------------------------------------------

    public String save() {
        if (movimento.getId() == -1) {
            Messages.warn("Sistema", "Nenhum movimento encontrado para ser alterado!");
            return null;
        }

        Dao dao = new Dao();
        dao.openTransaction();
        MovimentoDao movimentoDao = new MovimentoDao();

        movimento.setServicos(selectServico.get(idServicos));
        movimento.setTipoServico(selectTipoServico.get(idTipoServicos));

        List<Movimento> lista = movimentoDao.findMovimentosByLote(lote.getId());
        if (lista.size() > 1) {
            for (int i = 0; i < lista.size(); i++) {
                if (movimento.getId() != lista.get(i).getId()) {
                    Movimento lm = (Movimento) dao.find(new Movimento(), lista.get(i).getId());
                    lm.setServicos(selectServico.get(idServicos));
                    dao.update(lm);
                }
            }
            lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 2));
        } else {
            if (DataHoje.converteDataParaInteger(movimento.getVencimentoString()) > DataHoje.converteDataParaInteger(lote.getEmissaoString())) {
                lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 2));
            } else {
                lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 1));
            }
        }

        dao.update(lote);
        movimento.setLote(lote);

        if (baixa != null) {
            dao.update(baixa);
            movimento.setBaixa(baixa);
        }

        if (dao.update(movimento)) {
            Messages.info("Sucesso", "Movimento atualizado!");
            dao.commit();
        } else {
            Messages.warn("Erro", "Ao atualizar Movimento!");
            dao.rollback();
        }

        String url = (String) Sessions.getString("urlRetorno");
        if (url.equals("movimentosReceber")) {
            ((MovimentosReceberBean) Sessions.getObject("movimentosReceberBean")).getListMovimento().clear();
        }
        return null;
    }

    public String inativarBoleto() {
        Dao dao = new Dao();

        if (movimento.getBaixa() != null) {
            Messages.warn("Sistema", "Boletos quitados não podem ser Excluídos!");
            return null;
        }
//        if (movimento.getAcordo() != null) {
        Messages.warn("Sistema", "Boletos do tipo acordo não podem ser Excluídos!");
//            return null;
//        }
        if (historico.isEmpty()) {
            Messages.warn("Validação", "Digite um motivo para exclusão!");
            return null;
        } else if (historico.length() < 6) {
            Messages.warn("Validação", "Motivo de exclusão inválido!");
            return null;
        }
//        if (!GerarMovimento.inativarUmMovimento(dao.find(movimento), historico).isEmpty()) {
//            Messages.warn("Erro", "Ocorreu um erro em uma das exclusões, verifique o log!");
//        } else {
//            Messages.info("Sucesso", "Boleto excluído com sucesso");
//        }
        String url = (String) Sessions.getString("urlRetorno");
        if (url.equals("movimentosReceber")) {
            ((MovimentosReceberBean) Sessions.getObject("movimentosReceberBean")).getListMovimento().clear();
        }
        movimento = new Movimento();
        lote = new Lote();
        baixa = new Baixa();
        return null;
    }

    public Movimento getMovimento() {
        if (Sessions.exists("listMovimento")) {
            movimento = new Movimento();
            Dao dao = new Dao();
            movimento = (Movimento) Sessions.getList("listMovimento").get(0);
            lote = (Lote) dao.find(movimento.getLote());
            if (movimento.getBaixa() != null) {
                baixa = (Baixa) dao.find(movimento.getBaixa());
            }
            Sessions.remove("listMovimento");
        }

        if (Sessions.exists("pessoaPesquisa")) {
            if (baixa != null) {
                Pessoa p = (Pessoa) Sessions.getObject("pessoaPesquisa", true);
                UsuarioDao usuarioDao = new UsuarioDao();
                baixa.setUsuario(usuarioDao.pesquisaUsuarioPorPessoa(p.getId()));
            }
        }
        return movimento;
    }

    public String back() {
        Sessions.put("linkClicado", true);
        return (String) Sessions.getString("urlRetorno");
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getIdTipoServicos() {
        return idTipoServicos;
    }

    public void setIdTipoServicos(int idTipoServicos) {
        this.idTipoServicos = idTipoServicos;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Baixa getBaixa() {
        return baixa;
    }

    public void setBaixa(Baixa baixa) {
        this.baixa = baixa;
    }

    public int getIdCondicaoPagamento() {
        return idCondicaoPagamento;
    }

    public void setIdCondicaoPagamento(int idCondicaoPagamento) {
        this.idCondicaoPagamento = idCondicaoPagamento;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}
