package br.com.clinicaintegrada.pessoa.beans;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import br.com.clinicaintegrada.pessoa.TipoDocumentoProfissao;
import br.com.clinicaintegrada.pessoa.dao.EquipeDao;
import br.com.clinicaintegrada.pessoa.dao.FuncaoEquipeDao;
import br.com.clinicaintegrada.pessoa.dao.TipoDocumentoProfissaoDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Mask;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EquipeBean implements Serializable {

    private Equipe equipe;
    private List<Equipe> listEquipe;
    private List<SelectItem> listFuncaoEquipe;
    private Integer idTipoAtendimento;
    private Integer idFuncaoEquipe;
    private String documento;
    private String descricaoPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String tipoDocumentoProfissao;
    private String tipoAtendimento;
    private List<SelectItem> listTipoDocumentoProfissao;
    private Integer idTipoDocumentoProfissao;
    private String maskTipoAtendimento;

    @PostConstruct
    public void init() {
        equipe = new Equipe();
        equipe.setCadastro(new Date());
        listEquipe = new ArrayList<>();
        listFuncaoEquipe = new ArrayList<>();
        listTipoDocumentoProfissao = new ArrayList<>();
        idTipoAtendimento = 0;
        idFuncaoEquipe = 0;
        documento = "";
        descricaoPesquisa = "";
        porPesquisa = "ds_nome";
        comoPesquisa = "";
        tipoDocumentoProfissao = "";
        tipoAtendimento = "";
        idTipoDocumentoProfissao = 0;
        maskTipoAtendimento = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("equipeBean");
        Sessions.remove("equipePesquisa");
    }

    public void clear() {
        Sessions.remove("equipeBean");
    }

    public void save() {
        Dao dao = new Dao();
        if (listFuncaoEquipe.isEmpty()) {
            Messages.warn("Validação", "Cadastrar funções para equipe!");
            return;
        }
        equipe.setFuncaoEquipe((FuncaoEquipe) dao.find(new FuncaoEquipe(), Integer.parseInt(listFuncaoEquipe.get(idFuncaoEquipe).getDescription())));
        Logger logger = new Logger();
        if (equipe.getId() == -1) {
            EquipeDao equipeDao = new EquipeDao();
            if (equipeDao.existeEquipe(equipe.getFuncaoEquipe().getId(), equipe.getPessoa().getId(), SessaoCliente.get().getId())) {
                Messages.warn("Validação", "Pessoa equipe já cadastrada para esta função!");
                return;
            }
            equipe.setCliente(SessaoCliente.get());
            if (dao.save(equipe, true)) {
                Messages.info("Sucesso", "Registro adicionado");
                logger.save(
                        " ID: " + equipe.getId()
                        + " - Pessoa: [" + equipe.getPessoa().getId() + "]" + equipe.getPessoa().getNome()
                        + " - Função Equipe: [" + equipe.getFuncaoEquipe().getId() + "]" + equipe.getFuncaoEquipe().getProfissao().getProfissao()
                        + " - Documento: " + equipe.getDocumento()
                        + " - Ativo: " + equipe.getAtivo()
                );
                listEquipe.clear();
            } else {
                Messages.warn("Erro", "Erro ao adicionar registro!");
            }
        } else {
            Equipe e = (Equipe) dao.find(equipe);
            String beforeUpdate = ""
                    + " ID: " + e.getId()
                    + " - Pessoa: [" + e.getPessoa().getId() + "]" + equipe.getPessoa().getNome()
                    + " - Função Equipe: [" + e.getFuncaoEquipe().getId() + "]" + equipe.getFuncaoEquipe().getProfissao().getProfissao()
                    + " - Documento: " + e.getDocumento()
                    + " - Ativo: " + e.getAtivo();
            if (dao.update(equipe, true)) {
                Messages.info("Sucesso", "Registro atualizado");
                logger.update(beforeUpdate,
                        " ID: " + equipe.getId()
                        + " - Pessoa: [" + equipe.getPessoa().getId() + "]" + equipe.getPessoa().getNome()
                        + " - Função Equipe: [" + equipe.getFuncaoEquipe().getId() + "]" + equipe.getFuncaoEquipe().getProfissao().getProfissao()
                        + " - Documento: " + equipe.getDocumento()
                        + " - Ativo: " + equipe.getAtivo()
                );
                listEquipe.clear();
            } else {
                Messages.warn("Erro", "Erro ao atualizar registro!");
            }
        }
    }

    public void delete() {
        Dao dao = new Dao();
        Logger logger = new Logger();
        if (equipe.getId() != -1) {
            if (dao.delete(equipe, true)) {
                Messages.info("Sucesso", "Registro removido");
                logger.delete(
                        " ID: " + equipe.getId()
                        + " - Pessoa: [" + equipe.getPessoa().getId() + "]" + equipe.getPessoa().getNome()
                        + " - Função Equipe: [" + equipe.getFuncaoEquipe().getId() + "]" + equipe.getFuncaoEquipe().getProfissao().getProfissao()
                        + " - Documento: " + equipe.getDocumento()
                        + " - Ativo: " + equipe.getAtivo()
                );
                clear();
            } else {
                Messages.warn("Erro", "Erro ao remover registro!");
            }
        }

    }

    public String edit(Object o) {
        Dao dao = new Dao();
        equipe = (Equipe) dao.rebind(o);
        for (int i = 0; i < listFuncaoEquipe.size(); i++) {
            if (equipe.getFuncaoEquipe().getId() == Integer.parseInt(listFuncaoEquipe.get(i).getDescription())) {
                idFuncaoEquipe = i;
                break;
            }
        }
        Sessions.put("linkClicado", true);
        if (Sessions.exists("urlRetorno")) {
            if (!Sessions.getString("urlRetorno").equals("menuPrincipal")) {
                Sessions.put("equipePesquisa", equipe);
                return (String) Sessions.getString("urlRetorno");
            }
        } else {
            return "equipe";
        }
        return "equipe";
    }

    public Equipe getEquipe() {
        if (Sessions.exists("fisicaPesquisa")) {
            equipe.setPessoa(((Fisica) Sessions.getObject("fisicaPesquisa", true)).getPessoa());
        }
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public List<Equipe> getListEquipe() {
        if (listEquipe.isEmpty()) {
            EquipeDao equipeDao = new EquipeDao();
            listEquipe = equipeDao.pesquisaTodasEquipesPorCliente(descricaoPesquisa, porPesquisa, comoPesquisa, 0, SessaoCliente.get().getId());
        }
        return listEquipe;
    }

    public void setListEquipe(List<Equipe> listEquipe) {
        this.listEquipe = listEquipe;
    }

    public List<SelectItem> getListFuncaoEquipe() {
        if (listFuncaoEquipe.isEmpty()) {
            FuncaoEquipeDao funcaoEquipeDao = new FuncaoEquipeDao();
            List<FuncaoEquipe> list = (List<FuncaoEquipe>) funcaoEquipeDao.pesquisaTodasFuncaoEquipePorCliente(SessaoCliente.get().getId());
            for (int i = 0; i < list.size(); i++) {
                listFuncaoEquipe.add(new SelectItem(i, list.get(i).getProfissao().getProfissao(), "" + list.get(i).getId()));
            }
        }
        return listFuncaoEquipe;
    }

    public void setListFuncaoEquipe(List<SelectItem> listFuncaoEquipe) {
        this.listFuncaoEquipe = listFuncaoEquipe;
    }

    public Integer getIdFuncaoEquipe() {
        return idFuncaoEquipe;
    }

    public void setIdFuncaoEquipe(Integer idFuncaoEquipe) {
        this.idFuncaoEquipe = idFuncaoEquipe;
    }

    public String getMask() {
        if (!listFuncaoEquipe.isEmpty()) {
            Dao dao = new Dao();
            try {
                return ((FuncaoEquipe) dao.find(new FuncaoEquipe(), Integer.parseInt(listFuncaoEquipe.get(idFuncaoEquipe).getDescription()))).getTipoDocumentoProfissao().getMascara();
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    public Integer getIdTipoAtendimento() {
        return idTipoAtendimento;
    }

    public void setIdTipoAtendimento(Integer idTipoAtendimento) {
        this.idTipoAtendimento = idTipoAtendimento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void acaoPesquisaInicial() {
        setComoPesquisa("I");
        listEquipe.clear();
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("P");
        listEquipe.clear();
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getFindMask() {
        String mask = porPesquisa;
        if (porPesquisa.equals("ds_cpf")) {
            mask = "cpf";
        }
        return Mask.getMascaraPesquisa(mask, true);
    }

    public String getTipoDocumentoProfissao() {
        if (!listFuncaoEquipe.isEmpty()) {
            Dao dao = new Dao();
            FuncaoEquipe fe = (FuncaoEquipe) dao.find(new FuncaoEquipe(), Integer.parseInt(listFuncaoEquipe.get(idFuncaoEquipe).getDescription()));
            if (fe != null) {
                tipoDocumentoProfissao = fe.getTipoDocumentoProfissao().getDescricao();
            } else {
                tipoDocumentoProfissao = "";
            }
        }
        return tipoDocumentoProfissao;
    }

    public void setTipoDocumentoProfissao(String tipoDocumentoProfissao) {
        this.tipoDocumentoProfissao = tipoDocumentoProfissao;
    }

    public String getTipoAtendimento() {
        Dao dao = new Dao();
        FuncaoEquipe fe = (FuncaoEquipe) dao.find(new FuncaoEquipe(), Integer.parseInt(listFuncaoEquipe.get(idFuncaoEquipe).getDescription()));
        if (fe != null) {
            if (fe.getTipoAtendimento() != null) {
                tipoAtendimento = fe.getTipoAtendimento().getDescricao();
            } else {
                tipoAtendimento = "NENHUM";
            }
        } else {
            tipoAtendimento = "NENHUM";
        }
        return tipoAtendimento;
    }

    public void setTipoAtendimento(String tipoAtendimento) {
        this.tipoAtendimento = tipoAtendimento;
    }

    public List<SelectItem> getListTipoDocumentoProfissao() {
        if (listTipoDocumentoProfissao.isEmpty()) {
            listTipoDocumentoProfissao.clear();
            TipoDocumentoProfissaoDao tipoDocumentoProfissaoDao = new TipoDocumentoProfissaoDao();
            List list = (List) tipoDocumentoProfissaoDao.pesquisaTipoDocumentoProfissaoPorEquipeAgrupado();
            listTipoDocumentoProfissao.add(new SelectItem("Nome", "Nome", "ds_nome"));
            for (int i = 0; i < list.size(); i++) {
                listTipoDocumentoProfissao.add(new SelectItem(list.get(i).toString(), list.get(i).toString(), "" + list.get(i).toString()));
            }
        }
        return listTipoDocumentoProfissao;
    }

    public void setListTipoDocumentoProfissao(List<SelectItem> listTipoDocumentoProfissao) {
        this.listTipoDocumentoProfissao = listTipoDocumentoProfissao;
    }

    public Integer getIdTipoDocumentoProfissao() {
        return idTipoDocumentoProfissao;
    }

    public void setIdTipoDocumentoProfissao(Integer idTipoDocumentoProfissao) {
        this.idTipoDocumentoProfissao = idTipoDocumentoProfissao;
    }

    public String getMaskTipoAtendimento() {
        if (!listTipoDocumentoProfissao.isEmpty()) {
            if (!porPesquisa.equals("ds_nome")) {
                if (porPesquisa != null) {
                    TipoDocumentoProfissaoDao tipoDocumentoProfissaoDao = new TipoDocumentoProfissaoDao();
                    TipoDocumentoProfissao tdp1 = tipoDocumentoProfissaoDao.pesquisaTipoDocumentoProfissaoPorDescricao(porPesquisa);
                    if (tipoDocumentoProfissao != null) {
                        if (tdp1 != null) {
                            maskTipoAtendimento = tdp1.getMascara();
                        }
                        maskTipoAtendimento = "";
                    } else {
                        maskTipoAtendimento = "";
                    }
                } else {
                    maskTipoAtendimento = "";
                }
            } else {
                maskTipoAtendimento = "";
            }
        }
        return maskTipoAtendimento;
    }

    public void setMaskTipoAtendimento(String maskTipoAtendimento) {
        this.maskTipoAtendimento = maskTipoAtendimento;
    }

}
