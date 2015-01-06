package br.com.clinicaintegrada.fichamedica.beans;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.coordenacao.Status;
import br.com.clinicaintegrada.fichamedica.Atendimento;
import br.com.clinicaintegrada.fichamedica.AtendimentoAvaliacao;
import br.com.clinicaintegrada.fichamedica.Avaliacao;
import br.com.clinicaintegrada.fichamedica.AvaliacaoEquipe;
import br.com.clinicaintegrada.fichamedica.Enfermaria;
import br.com.clinicaintegrada.fichamedica.dao.AtendimentoAvaliacaoDao;
import br.com.clinicaintegrada.fichamedica.dao.AtendimentoDao;
import br.com.clinicaintegrada.fichamedica.dao.AvaliacaoEquipeDao;
import br.com.clinicaintegrada.fichamedica.dao.EnfermariaDao;
import br.com.clinicaintegrada.pessoa.dao.EquipeDao;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class AvaliacaoTecnicaBean implements Serializable {

    private Atendimento atendimento;
    private AtendimentoAvaliacao atendimentoAvaliacao;
    private Agendamento agendamento;
    private Enfermaria enfermaria;
    private Integer[] selectedAtendimentoAvaliacao;
    private List<AtendimentoAvaliacao> listAtendimentoAvaliacao;
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;
    private String[] historico;
    private Boolean[] isHistorico;

    @PostConstruct
    public void init() {
        atendimento = new Atendimento();
        atendimentoAvaliacao = new AtendimentoAvaliacao();
        agendamento = new Agendamento();
        enfermaria = new Enfermaria();
        index = new Integer[2];
        index[0] = null;
        index[1] = null;
        listAtendimentoAvaliacao = new ArrayList<>();
        listSelectItem = new ArrayList[200];
        selectedAtendimentoAvaliacao = null;
        selectedAtendimentoAvaliacao = new Integer[200];
        historico = new String[200];
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("avaliacaoTecnicaBean");
    }

    public void clear() {
        Sessions.remove("avaliacaoTecnicaBean");
    }

    public void clear(int tCase) {
        // LIMPAR DATAS
        if (tCase == 0) {
            Sessions.remove("avaliacaoTecnicaBean");
        }
    }

    // VOLTA PARA O MENU EQUIPE TÉCNICA
    public String back() {
        Sessions.remove("avaliacaoTecnicaBean");
        Sessions.remove("menuEquipeTecnicaBean");
        ChamadaPaginaBean.link();
        return "menuEquipeTecnica";
    }

    // STATUS 1 - DE ATENDIMENTO PARA AGENDADO
    public String exit() {
        Dao dao = new Dao();
        if (agendamento.getStatus().getId() == 3) {
            Messages.warn("Sistema", "Não é possível sair de um agendamento já concluído!");
            return null;
        }
        if (atendimento.getId() != -1) {
            atendimento.setAgendamento(agendamento);
            agendamento.setStatus((Status) dao.find(new Status(), 1));
            agendamento.setEquipe(null);
            agendamento.setDataAtendimento(null);
            agendamento.setHoraAtendimento("");
            AtendimentoAvaliacaoDao atendimentoAvaliacaoDao = new AtendimentoAvaliacaoDao();
            dao.update(agendamento, true);
            atendimento.setAgendamento(agendamento);
            List<AtendimentoAvaliacao> list = atendimentoAvaliacaoDao.listByAtendimento(atendimento.getId());
            for (AtendimentoAvaliacao list1 : list) {
                dao.delete(list1, true);
            }
        } else {
            agendamento.setStatus((Status) dao.find(new Status(), 1));
            agendamento.setEquipe(null);
            agendamento.setDataAtendimento(null);
            agendamento.setHoraAtendimento("");
            dao.update(agendamento, true);
        }
        Sessions.remove("avaliacaoTecnicaBean");
        Sessions.remove("menuEquipeTecnicaBean");
        ChamadaPaginaBean.link();
        return "menuEquipeTecnica";
    }

    // STATUS 3 - CONCLUÍDO
    public void save() {
        Dao dao = new Dao();
        if (atendimento.getId() == null) {
            atendimento.setAgendamento(agendamento);
            dao.save(atendimento, true);
        } else {
            dao.update(atendimento, true);
        }
        if (listSelectItem[0] != null && !listSelectItem[0].isEmpty()) {
            AtendimentoAvaliacaoDao atendimentoAvaliacaoDao = new AtendimentoAvaliacaoDao();
            List<AtendimentoAvaliacao> list = atendimentoAvaliacaoDao.listByAtendimento(atendimento.getId());
            for (AtendimentoAvaliacao list1 : list) {
                dao.delete(list1, true);
            }
            dao.openTransaction();
            for (int i = 0; i < listSelectItem.length; i++) {
                Avaliacao avaliacao = (Avaliacao) dao.find(new Avaliacao(), selectedAtendimentoAvaliacao[i]);
                AtendimentoAvaliacao aa = atendimentoAvaliacaoDao.findByAtendimentoAndAvaliacao(atendimento.getId(), avaliacao.getId());
                if (avaliacao.getHistorico()) {
                    if (historico[i].isEmpty()) {
                        Messages.warn("Validação", "Informar histórico para a avaliação do grupo " + avaliacao.getGrupoAvaliacao().getDescricao() + " e tipo " + avaliacao.getTiposAvaliacao().getDescricao() + ".");
                        dao.rollback();
                        return;
                    } else {
                        if (historico[i].length() < 5) {
                            Messages.warn("Validação", "Grupo " + avaliacao.getGrupoAvaliacao().getDescricao() + " - Tipo " + avaliacao.getTiposAvaliacao().getDescricao() + ". O histórico deve conter no mínimo 5 caracteres.");
                            dao.rollback();
                            return;
                        }
                    }
                }
                if (aa == null) {
                    aa = new AtendimentoAvaliacao();
                    aa.setAvaliacao(avaliacao);
                    aa.setAtendimento(atendimento);
                    aa.setHistorico(historico[i]);
                    if (!dao.save(aa)) {
                        dao.rollback();
                        Messages.warn("Erro", "Ao salvar avaliações!");
                        return;
                    }
                }
            }
            dao.commit();
            agendamento.setStatus((Status) dao.find(new Status(), 3));
            agendamento.setDataAtendimento(DataHoje.dataHoje());
            agendamento.setHoraAtendimento(DataHoje.horaMinuto());
            dao.update(agendamento, true);
            atendimento.setAgendamento(agendamento);
        }
        listAtendimentoAvaliacao.clear();
        Messages.info("Sucesso", "Avaliação gerada com sucesso");
    }

    // STATUS 6 - ENFERMARIA
    public void saveEnfermaria() {
        Dao dao = new Dao();
        if (enfermaria.getId() == null) {
            enfermaria.setAgendamento(agendamento);
            enfermaria.setHora(DataHoje.horaMinuto());
            if (dao.save(enfermaria, true)) {
                agendamento.setStatus((Status) dao.find(new Status(), 6));
                dao.update(agendamento, true);
                Messages.info("Sucesso", "Pré atendimento salvo");
            } else {
                Messages.warn("Erro", "Ao salvar pré atendimento!");
            }
        } else {
            if(enfermaria.getAgendamento().getStatus().getId() == 2 && agendamento.getHoraAtendimento().isEmpty()) {
                agendamento.setStatus((Status) dao.find(new Status(), 6));                
            }
            dao.update(agendamento, true);
            if (dao.update(enfermaria, true)) {
                enfermaria.setAgendamento(agendamento);
                Messages.info("Sucesso", "Pré atendimento atualizado");
            } else {
                Messages.warn("Erro", "Ao atualizar pré atendimento!");
            }
        }
        listAtendimentoAvaliacao.clear();
    }

    // STATUS 4 - CANCELADO
    public void cancel() {
        Dao dao = new Dao();
        agendamento.setStatus((Status) dao.find(new Status(), 4));
        if (atendimento.getId() != null) {
            AtendimentoAvaliacaoDao atendimentoAvaliacaoDao = new AtendimentoAvaliacaoDao();
            List<AtendimentoAvaliacao> list = atendimentoAvaliacaoDao.listByAtendimento(atendimento.getId());
            for (AtendimentoAvaliacao list1 : list) {
                dao.delete(list1, true);
            }
        }
        dao.update(agendamento, true);
        atendimento.setAgendamento(agendamento);
        Messages.info("Sucesso", "Avaliação cancelada.");
    }

    // STATUS 5 - FALTOU
    public void missed() {
        Dao dao = new Dao();
        if (atendimento.getId() != null) {
            AtendimentoAvaliacaoDao atendimentoAvaliacaoDao = new AtendimentoAvaliacaoDao();
            agendamento.setDataAtendimento(null);
            List<AtendimentoAvaliacao> list = atendimentoAvaliacaoDao.listByAtendimento(atendimento.getId());
            for (AtendimentoAvaliacao list1 : list) {
                dao.delete(list1, true);
            }
        }
        agendamento.setStatus((Status) dao.find(new Status(), 5));
        dao.update(agendamento, true);
        atendimento.setAgendamento(agendamento);
        Messages.info("Sucesso", "Avaliação cancelada.");
    }

    // STATUS - 2 ATENDIMENT (SOMENTE SE ESTIVER COM STATUS AGENDADO)
    public String select(Object o) throws IOException {
        return select(o, false);
    }

    public String select(Object o, Boolean isEnfermaria) throws IOException {
        Dao dao = new Dao();
        AtendimentoDao atendimentoDao = new AtendimentoDao();
        EnfermariaDao enfermariaDao = new EnfermariaDao();
        agendamento = (Agendamento) dao.rebind(o);
        atendimento = atendimentoDao.findByAgendamento(agendamento.getId());
        enfermaria = enfermariaDao.findByAgendamento(agendamento.getId());
        agendamento.setDataAtendimento(DataHoje.dataHoje());
        listAtendimentoAvaliacao.clear();
        if (atendimento == null) {
            atendimento = new Atendimento();
            if (agendamento.getStatus().getId() == 1) {
                agendamento.setStatus((Status) dao.find(new Status(), 2));
            }
            EquipeDao equipeDao = new EquipeDao();
            agendamento.setEquipe(equipeDao.findByPessoaAndFuncaoEquipe(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId(), agendamento.getFuncaoEquipe().getId()));
            dao.update(agendamento, true);
            if (isEnfermaria) {
                if(enfermaria == null) {
                    enfermaria = new Enfermaria();
                    enfermaria.setAgendamento(agendamento);
                    enfermaria.setEquipe(equipeDao.findByPessoaAndProfissao(((Usuario) Sessions.getObject("sessaoUsuario")).getPessoa().getId(), 251));
                    enfermaria.setHora(DataHoje.horaMinuto());                    
                    dao.save(enfermaria, true);
                }
            } else {
                enfermaria = new Enfermaria();
                agendamento.setHoraAtendimento(DataHoje.horaMinuto());
                dao.update(agendamento, true);                
            }
        } else {
            agendamento = atendimento.getAgendamento();
            if(agendamento.getHoraAtendimento().isEmpty()) {
                agendamento.setHoraAtendimento(DataHoje.horaMinuto());
            }
            if(enfermaria == null) {
                enfermaria = new Enfermaria();                
            }
        }
        return null;
    }

    public String close(Object o) {
        return null;
    }

    public List<SelectItem>[] getListSelectItem() {
        return listSelectItem;
    }

    public void setListSelectItem(List<SelectItem>[] listSelectItem) {
        this.listSelectItem = listSelectItem;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public Atendimento getAtendimento() {
        return atendimento;
    }

    public void setAtendimento(Atendimento atendimento) {
        this.atendimento = atendimento;
    }

    public AtendimentoAvaliacao getAtendimentoAvaliacao() {
        return atendimentoAvaliacao;
    }

    public void setAtendimentoAvaliacao(AtendimentoAvaliacao atendimentoAvaliacao) {
        this.atendimentoAvaliacao = atendimentoAvaliacao;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public List<AtendimentoAvaliacao> getListAtendimentoAvaliacao() {
        if (listAtendimentoAvaliacao.isEmpty()) {
            int j = 0;
            int x = 0;
            if (agendamento.getId() != -1) {
                AvaliacaoEquipeDao avaliacaoEquipeDao = new AvaliacaoEquipeDao();
                Integer id = -1;
                List<AvaliacaoEquipe> list = avaliacaoEquipeDao.findAllByFuncaoEquipe(agendamento.getFuncaoEquipe().getId());
                for (int i = 0; i < list.size(); i++) {
                    if (!Objects.equals(list.get(i).getAvaliacao().getGrupoAvaliacao().getId(), id)) {
                        id = list.get(i).getAvaliacao().getGrupoAvaliacao().getId();
                        x++;
                    }
                }
                listSelectItem = new ArrayList[x];
                selectedAtendimentoAvaliacao = new Integer[x];
                historico = new String[x];
                isHistorico = new Boolean[x];
                for (int i = 0; i < x; i++) {
                    listSelectItem[i] = new ArrayList();
                    selectedAtendimentoAvaliacao[i] = null;
                    historico[i] = "";
                    isHistorico[i] = false;
                }
                for (int i = 0; i < list.size(); i++) {
                    if (!Objects.equals(list.get(i).getAvaliacao().getGrupoAvaliacao().getId(), id)) {
                        id = list.get(i).getAvaliacao().getGrupoAvaliacao().getId();
                        listAtendimentoAvaliacao.add(new AtendimentoAvaliacao(i, null, list.get(i).getAvaliacao(), ""));
                        getSubListAtendimentoAvaliacoes(list.get(i).getAvaliacao().getGrupoAvaliacao().getId(), j);
                        j++;
                    }
                }
            }
        }
        return listAtendimentoAvaliacao;
    }

    public void setListAtendimentoAvaliacao(List<AtendimentoAvaliacao> listAtendimentoAvaliacao) {
        this.listAtendimentoAvaliacao = listAtendimentoAvaliacao;
    }

    public List<AtendimentoAvaliacao> getSubListAtendimentoAvaliacao(Integer idAvaliacao) {
        List<AtendimentoAvaliacao> sublist = new ArrayList<>();
        AvaliacaoEquipeDao avaliacaoEquipeDao = new AvaliacaoEquipeDao();
        Integer id = -1;
        List<AvaliacaoEquipe> list = avaliacaoEquipeDao.findAllByGrupoAvaliacao(idAvaliacao);
        for (int i = 0; i < list.size(); i++) {
            if (!Objects.equals(list.get(i).getAvaliacao().getTiposAvaliacao().getId(), id)) {
                id = list.get(i).getAvaliacao().getTiposAvaliacao().getId();
                sublist.add(new AtendimentoAvaliacao(i, null, list.get(i).getAvaliacao(), ""));
            }
        }
        return sublist;
    }

    public List<SelectItem> getSubListAtendimentoAvaliacoes(Integer idAvaliacao, Integer j) {
        if (listSelectItem[j].isEmpty()) {
            AvaliacaoEquipeDao avaliacaoEquipeDao = new AvaliacaoEquipeDao();
            List<AvaliacaoEquipe> list = avaliacaoEquipeDao.findAllByGrupoAvaliacao(idAvaliacao);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    selectedAtendimentoAvaliacao[j] = list.get(i).getAvaliacao().getId();
                    defineHistorico(j, true);
                }
                listSelectItem[j].add(new SelectItem(list.get(i).getAvaliacao().getId(), list.get(i).getAvaliacao().getTiposAvaliacao().getDescricao()));
            }
        }
        return listSelectItem[j];
    }

    public Integer[] getSelectedAtendimentoAvaliacao() {
        return selectedAtendimentoAvaliacao;
    }

    public void setSelectedAtendimentoAvaliacao(Integer[] selectedAtendimentoAvaliacao) {
        this.selectedAtendimentoAvaliacao = selectedAtendimentoAvaliacao;
    }

    public Integer getSelected(Integer i) {
        return selectedAtendimentoAvaliacao[i];
    }

    public void selected(SelectEvent selectEvent) {
        if (selectEvent != null) {
            this.selectedAtendimentoAvaliacao = (Integer[]) selectEvent.getObject();
        }
    }

    public void mySelectionMethodListener(ValueChangeEvent event) {
        Integer[] x = (Integer[]) event.getOldValue();
    }

    public String[] getHistorico() {
        return historico;
    }

    public void setHistorico(String[] historico) {
        this.historico = historico;
    }

    public Boolean[] getIsHistorico() {
        return isHistorico;
    }

    public void setIsHistorico(Boolean[] isHistorico) {
        this.isHistorico = isHistorico;
    }

    public void defineHistorico(Integer i, Boolean edit) {
        try {
            if (edit && atendimento.getId() != -1) {
                if (selectedAtendimentoAvaliacao[i] != null) {
                    Dao dao = new Dao();
                    Avaliacao a = (Avaliacao) dao.find(new Avaliacao(), selectedAtendimentoAvaliacao[i]);
                    AtendimentoAvaliacaoDao atendimentoAvaliacaoDao = new AtendimentoAvaliacaoDao();
                    List<AtendimentoAvaliacao> list = atendimentoAvaliacaoDao.listByAtendimento(atendimento.getId());
                    if (a != null) {
                        isHistorico[i] = a.getHistorico();
                        try {
                            selectedAtendimentoAvaliacao[i] = list.get(i).getAvaliacao().getId();
                        } catch (Exception e) {
                        }
                        AtendimentoAvaliacao aa = atendimentoAvaliacaoDao.findByAtendimentoAndAvaliacao(atendimento.getId(), selectedAtendimentoAvaliacao[i]);
                        if (aa != null) {
                            selectedAtendimentoAvaliacao[i] = aa.getAvaliacao().getId();
                            if (isHistorico[i]) {
                                historico[i] = aa.getHistorico();
                            } else {
                                historico[i] = "";
                            }
                        }
                    }
                }
            } else {
                if (selectedAtendimentoAvaliacao[i] != null) {
                    Dao dao = new Dao();
                    Avaliacao a = (Avaliacao) dao.find(new Avaliacao(), selectedAtendimentoAvaliacao[i]);
                    if (a != null) {
                        isHistorico[i] = a.getHistorico();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public Enfermaria getEnfermaria() {
        return enfermaria;
    }

    public void setEnfermaria(Enfermaria enfermaria) {
        this.enfermaria = enfermaria;
    }
    
    public String getImc() {
        if(enfermaria != null) {
            if(enfermaria.getId() != null) {
                if(enfermaria.getPeso() > 0 && enfermaria.getAltura() > 0) {
                   Float h = enfermaria.getAltura() * enfermaria.getAltura();
                   Float t = Moeda.converteFloatR$Float(enfermaria.getPeso() / h);
                   return "" + t;
                }
            }
        }
        return "0";
    }

}
