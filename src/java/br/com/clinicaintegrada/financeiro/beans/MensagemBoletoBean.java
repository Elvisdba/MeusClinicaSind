package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.MensagemBoleto;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.dao.MensagemBoletoDao;
import br.com.clinicaintegrada.financeiro.dao.ServicosDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class MensagemBoletoBean {

    private MensagemBoleto mensagemBoleto;
    private int idServico;
    private int idTipoServico;
    private int idReplica;
    private int processarGrupos;
    private int idIndex;
    private List listMensagens;
    private boolean disAcordo;
    private boolean processarTipoServicos;
    private boolean gerarAno;
    private String vencimento;
    private String replicaPara;

    @PostConstruct
    public void init() {
        mensagemBoleto = new MensagemBoleto();
        idServico = 0;
        idTipoServico = 0;
        idReplica = 1;
        processarGrupos = 4;
        idIndex = -1;
        listMensagens = new ArrayList<>();
        disAcordo = false;
        processarTipoServicos = false;
        gerarAno = false;
        vencimento = DataHoje.data();
        replicaPara = "";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("mensagemBoletoBean");
        Sessions.remove("mensagemPesquisa");
    }

    public MensagemBoletoBean() {
        mensagemBoleto.setReferencia(DataHoje.data().substring(3));
    }

    public String replicar() {
        MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();

        List<MensagemBoleto> list = mensagemBoletoDao.listAllByAno(this.getListRefReplica().get(idReplica).getLabel());

        Dao dao = new Dao();
        if (!list.isEmpty()) {
            dao.openTransaction();
        }
        DataHoje dh = new DataHoje();
        boolean comita = false;
        for (int i = 0; i < list.size(); i++) {
            MensagemBoleto mc = mensagemBoletoDao.verificaMensagem(
                    list.get(i).getServicos().getId(), list.get(i).getTipoServico().getId(),
                    list.get(i).getReferencia().substring(0, 3) + replicaPara);
            if (mc != null && mc.getId() != -1) {
                continue;
            }

            MensagemBoleto men = new MensagemBoleto(-1,
                    list.get(i).getServicos(),
                    list.get(i).getTipoServico(),
                    list.get(i).getMensagem(),
                    list.get(i).getMensagemCompensacao(),
                    list.get(i).getReferencia().substring(0, 3) + replicaPara, DataHoje.converte(dh.incrementarAnos(1, list.get(i).getVencimentoString())));

            if (dao.save(men)) {
                comita = true;
            } else {
            }

        }
        if (comita) {
            dao.commit();
            Messages.info("Sucesso", "Registro replicado");
        } else {
            dao.rollback();
            Messages.warn("Erro", "Nenhuma mensagem para Replicar!");
        }
        return "";
    }

    public List<SelectItem> getListRefReplica() {
        List<SelectItem> lista = new ArrayList<>();
        List select = new ArrayList();
        select.add(Integer.valueOf(DataHoje.data().substring(6)) - 1);
        select.add(DataHoje.data().substring(6));
        for (int i = 0; i < select.size(); i++) {
            lista.add(new SelectItem(i, select.get(i).toString(), Integer.toString(i)));
        }
        return lista;
    }

    public MensagemBoleto getMensagemBoleto() {
        if (mensagemBoleto.getId() == -1) {
            if (Sessions.exists("mensagemPesquisa")) {
                mensagemBoleto = (MensagemBoleto) Sessions.getObject("mensagemPesquisa");
            }
        }
        return mensagemBoleto;
    }

    public void setMensagemBoleto(MensagemBoleto mensagemBoleto) {
        this.mensagemBoleto = mensagemBoleto;
    }

    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public int getIdTipoServico() {
        return idTipoServico;
    }

    public void setIdTipoServico(int idTipoServico) {
        this.idTipoServico = idTipoServico;
    }

    public synchronized String save() {
        String mensagem = "";
        MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();
        DataHoje dataHoje = new DataHoje();
        mensagemBoleto.setVencimentoString(vencimento);

        if (!mensagemBoleto.getVencimentoString().equals(vencimento)) {
            Messages.warn("Erro", "Este vencimento esta incorreto!");
            return null;
        }

        if ((mensagemBoleto.getReferencia().length() != 7)
                && (Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription()) != 4)) {
            Messages.warn("Validação", "Referência esta incorreta");
            return null;
        }

        if (DataHoje.converteData(mensagemBoleto.getVencimento()) == null) {
            Messages.warn("Validação", "Informe o vencimento");
            return null;
        }

        try {
            if (mensagemBoleto.getId() == -1) {
                // SE ACORDO FOR FALSO ----------------------------------------------------
                if (mensagemBoleto.getReferencia().length() != 7 && !disAcordo) {
                    Messages.warn("Validação", "Digite uma referencia!");
                    return null;
                }

                int ano = 0;
                String referencia = "",
                        vencto = "",
                        diaOriginal = "";
                int iservicos = Integer.parseInt(this.getListServico().get(idServico).getDescription()),
                        itiposervico = Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription());
                if (gerarAno && !disAcordo) {
                    ano = 12;
                    referencia = "01/01/" + mensagemBoleto.getReferencia().substring(3);
                    diaOriginal = mensagemBoleto.getVencimentoString().substring(0, 2);
                    vencto = diaOriginal + "/01/" + mensagemBoleto.getVencimentoString().substring(6, 10);
                    if (iservicos == 1) {
                        vencto = dataHoje.incrementarMesesUltimoDia(1, vencto);
                    } else {
                        vencto = dataHoje.incrementarMeses(1, vencto);
                    }
                } else {
                    ano = 1;
                    referencia = mensagemBoleto.getReferencia();
                    diaOriginal = mensagemBoleto.getVencimentoString().substring(0, 2);
                    vencto = mensagemBoleto.getVencimentoString();
                }

                switch (processarGrupos) {
                    // NENHUMA DESTAS OPÇÕES
                    case 4: {
                        for (int l = 0; l < ano; l++) {
                            if (gerarAno && !disAcordo) {
                                mensagem = this.saveMensagem(iservicos, itiposervico, referencia.substring(3), vencto);
                            } else {
                                mensagem = this.saveMensagem(iservicos, itiposervico, referencia, vencto);
                            }

                            referencia = dataHoje.incrementarMeses(1, referencia);
                            if (iservicos == 1) {
                                vencto = dataHoje.incrementarMesesUltimoDia(1, vencto);
                            } else {
                                vencto = diaOriginal + vencto.substring(2, 10);
                                vencto = dataHoje.incrementarMeses(1, vencto);
                            }
                        }
                        break;
                    }
                }
            } else {
                Dao dao = new Dao();
                MensagemBoleto men = null;
                Logger novoLog = new Logger();
                if (processarTipoServicos) {
                    List<MensagemBoleto> lista = mensagemBoletoDao.mesmoTipoServico(
                            Integer.parseInt(this.getListServico().get(idServico).getDescription()),
                            Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription()),
                            mensagemBoleto.getReferencia().substring(3));
                    for (int i = 0; i < lista.size(); i++) {
                        lista.get(i).setMensagemCompensacao(mensagemBoleto.getMensagemCompensacao());
                        lista.get(i).setMensagem(mensagemBoleto.getMensagem());
                        lista.get(i).setVencimentoString(vencimento);
                        men = mensagemBoletoDao.verificaMensagem(
                                lista.get(i).getServicos().getId(),
                                lista.get(i).getTipoServico().getId(),
                                lista.get(i).getReferencia());
                        if ((men == null) || (men.getId() != -1)) {
                            MensagemBoleto mcBefore = (MensagemBoleto) dao.find(men);
                            String beforeUpdate = "Referência: " + mcBefore.getReferencia()
                                    + " - Vencimento: " + mcBefore.getVencimento()
                                    + " - Serviço: (" + mcBefore.getServicos().getId() + ") "
                                    + " - Tipo Serviço: (" + mcBefore.getTipoServico().getId() + ") " + mcBefore.getTipoServico().getDescricao()
                                    + " - Mensagem Compensação: " + mcBefore.getMensagemCompensacao();
                            if (dao.update(lista.get(i))) {
                                mensagem = "Mensagem atualizado!";
                                novoLog.update(beforeUpdate,
                                        "Referência: " + men.getReferencia()
                                        + " - Vencimento: " + men.getVencimento()
                                        + " - Serviço: (" + men.getServicos().getId() + ") "
                                        + " - Tipo Serviço: (" + men.getTipoServico().getId() + ") " + men.getTipoServico().getDescricao()
                                        + " - Mensagem Compensação: " + men.getMensagemCompensacao()
                                        + " - Mensagem Contribuinte: " + men.getMensagem()
                                );
                                Messages.info("Sucesso", mensagem);
                            } else {
                                Messages.warn("Erro", mensagem);
                            }
                        }
                    }
                } else {
                    men = mensagemBoletoDao.verificaMensagem(
                            mensagemBoleto.getServicos().getId(),
                            mensagemBoleto.getTipoServico().getId(),
                            mensagemBoleto.getReferencia());
                    MensagemBoleto mcBefore = (MensagemBoleto) dao.find(mensagemBoleto);
                    String beforeUpdate = ""
                            + "Referência: " + mcBefore.getReferencia()
                            + " - Vencimento: " + mcBefore.getVencimento()
                            + " - Serviço: (" + mcBefore.getServicos().getId() + ") "
                            + " - Tipo Serviço: (" + mcBefore.getTipoServico().getId() + ") " + mcBefore.getTipoServico().getDescricao()
                            + " - Mensagem Compensação: " + mcBefore.getMensagemCompensacao();
                    if (men == null || (men.getId() == mensagemBoleto.getId())) {
                        if (dao.update(mensagemBoleto)) {
                            novoLog.update(beforeUpdate,
                                    "Referência: " + mensagemBoleto.getReferencia()
                                    + " - Vencimento: " + mensagemBoleto.getVencimento()
                                    + " - Serviço: (" + mensagemBoleto.getServicos().getId() + ") "
                                    + " - Tipo Serviço: (" + mensagemBoleto.getTipoServico().getId() + ") " + mensagemBoleto.getTipoServico().getDescricao() + " - Mensagem Compensação: " + mensagemBoleto.getMensagemCompensacao()
                                    + " - Mensagem: " + mensagemBoleto.getMensagem()
                            );
                            Messages.info("Sucesso", "Mensagem atualizado");
                        } else {
                            Messages.warn("Erro", "Ao atualizar");
                        }
                    } else {
                        Messages.warn("Validação", "Mensagem já existe!");
                    }
                }
            }
        } catch (Exception e) {
            Messages.warn("Erro", e.getMessage());
        }
        return null;
    }

    private synchronized String saveMensagem(int idServ, int idTipo, String referencia, String vencimento) {
        Dao dao = new Dao();
        MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();
        String result = "";
        mensagemBoleto.setServicos((Servicos) dao.find(new Servicos(), idServ));
        mensagemBoleto.setTipoServico((TipoServico) dao.find(new TipoServico(), idTipo));
        mensagemBoleto.setReferencia(referencia);
        mensagemBoleto.setVencimentoString(vencimento);
        Logger novoLog = new Logger();
        MensagemBoleto menConvencao = mensagemBoletoDao.verificaMensagem(idServ, idTipo, referencia);
        try {
            if (menConvencao == null) {
                dao.openTransaction();
                if (dao.save(mensagemBoleto)) {
                    novoLog.save(
                            " - Referência: " + mensagemBoleto.getReferencia()
                            + " - Vencimento: " + mensagemBoleto.getVencimento()
                            + " - Serviço: (" + mensagemBoleto.getServicos().getId() + ") "
                            + " - Tipo Serviço: (" + mensagemBoleto.getTipoServico().getId() + ") " + mensagemBoleto.getTipoServico().getDescricao()
                            + " - Mensagem Compensação: " + mensagemBoleto.getMensagemCompensacao()
                            + " - Mensagem: " + mensagemBoleto.getMensagem()
                    );
                    dao.commit();
                    mensagemBoleto.setId(-1);
                    result = "Mensagem salva com Sucesso!";
                } else {
                    result = "Erro ao salvar mensagem!";
                    dao.rollback();
                }
            } else if (menConvencao.getId() == -1) {
                result = "Mensagem ja existe!";
            } else {
                result = "Mensagem ja existe!";
            }
        } catch (Exception e) {
        }
        return result;
    }

    public String clear() {
        Sessions.remove("mensagemPesquisa");
        Sessions.remove("mensagemBoletoBean");
        mensagemBoleto.setReferencia(DataHoje.data().substring(3));
        return "mensagem";
    }

    public String delete() {
        if (mensagemBoleto.getId() != -1) {
            Logger novoLog = new Logger();
            Dao dao = new Dao();
            mensagemBoleto = (MensagemBoleto) dao.find(mensagemBoleto);
            dao.openTransaction();
            if (dao.delete(mensagemBoleto)) {
                novoLog.delete(
                        " - Referência: " + mensagemBoleto.getReferencia()
                        + " - Vencimento: " + mensagemBoleto.getVencimento()
                        + " - Serviço: (" + mensagemBoleto.getServicos().getId() + ") "
                        + " - Tipo Serviço: (" + mensagemBoleto.getTipoServico().getId() + ") " + mensagemBoleto.getTipoServico().getDescricao()
                        + " - Mensagem Compensação: " + mensagemBoleto.getMensagemCompensacao()
                        + " - Mensagem: " + mensagemBoleto.getMensagem()
                );
                dao.commit();
                Messages.info("Sucesso", "Mensagem Excluida");
            } else {
                dao.rollback();
                Messages.warn("Erro", "Ao excluir");
            }
        } else {
            Messages.warn("Erro", "Pesquise uma mensagem para ser Excluída!");
        }
        return null;
    }

    public List<MensagemBoleto> getListMensagens() {
        if ((!this.getListServico().isEmpty()) && (!this.getListTipoServico().isEmpty())) {
            MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();
            int param[] = new int[2];
            listMensagens = mensagemBoletoDao.listAllOrdering(
                    mensagemBoleto.getReferencia(),
                    Integer.parseInt(this.getListServico().get(idServico).getDescription()),
                    Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription())
            );

            if (listMensagens == null) {
                listMensagens = new ArrayList();
            }
        }

        return listMensagens;
    }

    public boolean getAtualizar() {
        return mensagemBoleto.getId() != -1;
    }

    public boolean getNovox() {
        return mensagemBoleto.getId() == -1;
    }

    public String edit(MensagemBoleto me) {
        mensagemBoleto = me;
        vencimento = mensagemBoleto.getVencimentoString();

        if (mensagemBoleto.getTipoServico().getId() != -1) {
            List<SelectItem> tipoServico = getListTipoServico();
            for (SelectItem tipoServico1 : tipoServico) {
                if (Integer.parseInt(tipoServico1.getDescription()) == mensagemBoleto.getTipoServico().getId()) {
                    idTipoServico = (Integer) tipoServico1.getValue();
                    break;
                }
            }
        }

        if (mensagemBoleto.getServicos().getId() != -1) {
            List<SelectItem> servicos = getListServico();
            for (SelectItem servico : servicos) {
                if (Integer.parseInt(servico.getDescription()) == mensagemBoleto.getServicos().getId()) {
                    idServico = (Integer) servico.getValue();
                    break;
                }
            }
        }
        return "mensagem";
    }

    public List<SelectItem> getListTipoServico() {
        List<SelectItem> tipoServico = new ArrayList<SelectItem>();
        int i = 0;
//        TipoServicoDB db = new TipoServicoDBToplink();
//        List select = db.pesquisaTodosPeloContaCobranca();
        List select = null;
        while (i < select.size()) {
            tipoServico.add(new SelectItem(
                    i,
                    (String) ((TipoServico) select.get(i)).getDescricao(),
                    Integer.toString(((TipoServico) select.get(i)).getId())));
            i++;
        }
        return tipoServico;
    }

    public List<SelectItem> getListServico() {
        List<SelectItem> servicos = new ArrayList<SelectItem>();
        int i = 0;
        ServicosDao servicosDao = new ServicosDao();
        //List select = servicosDao.fpesquisaTodosPeloContaCobranca(4);
        List select = null;
        while (i < select.size()) {
            servicos.add(new SelectItem(
                    i,
                    (String) ((Servicos) select.get(i)).getDescricao(),
                    Integer.toString(((Servicos) select.get(i)).getId())));
            i++;
        }
        return servicos;
    }

    public void capturarUltimaMensagem() {
        MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();
        this.mensagemBoleto.getTipoServico().setId(-1);
        this.mensagemBoleto.getServicos().setId(-1);
        this.mensagemBoleto.setVencimentoString("");
        MensagemBoleto msgConvencao = mensagemBoletoDao.findLastMensagem(
                Integer.parseInt(this.getListServico().get(idServico).getDescription()),
                Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription())
        );
        this.mensagemBoleto.setTipoServico(msgConvencao.getTipoServico());
        this.mensagemBoleto.setServicos(msgConvencao.getServicos());
        this.mensagemBoleto.setMensagemCompensacao(msgConvencao.getMensagemCompensacao());
        this.mensagemBoleto.setMensagem(msgConvencao.getMensagem());
        this.mensagemBoleto.setVencimento(msgConvencao.getVencimento());
    }

    public void setListMensagens(List listMensagens) {
        this.listMensagens = listMensagens;
    }

    public String getIdentificador() {
        if (mensagemBoleto.getId() == -1) {
            return "";
        } else {
            return Integer.toString(mensagemBoleto.getId());
        }
    }

    public String getHabilitar() {
        if (mensagemBoleto.getId() != -1) {
            return "true";
        } else {
            return "false";
        }
    }

    public boolean isDisAcordo() {
        if (Integer.parseInt(this.getListTipoServico().get(idTipoServico).getDescription()) == 4) {
            disAcordo = true;
            mensagemBoleto.setReferencia("");
            mensagemBoleto.setMensagem("");
            mensagemBoleto.setVencimentoString("");
        } else {
            disAcordo = false;
        }
        return disAcordo;
    }

    public void setDisAcordo(boolean disAcordo) {
        this.disAcordo = disAcordo;
    }

    public boolean isProcessarTipoServicos() {
        return processarTipoServicos;
    }

    public void setProcessarTipoServicos(boolean processarTipoServicos) {
        this.processarTipoServicos = processarTipoServicos;
    }

    public boolean isGerarAno() {
        return gerarAno;
    }

    public void setGerarAno(boolean gerarAno) {
        this.gerarAno = gerarAno;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public int getProcessarGrupos() {
        return processarGrupos;
    }

    public void setProcessarGrupos(int processarGrupos) {
        this.processarGrupos = processarGrupos;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdReplica() {
        return idReplica;
    }

    public void setIdReplica(int idReplica) {
        this.idReplica = idReplica;
    }

    public String getReplicaPara() {
        replicaPara = Integer.toString(Integer.valueOf(this.getListRefReplica().get(idReplica).getLabel()) + 1);
        return replicaPara;
    }

    public void setReplicaPara(String replicaPara) {
        this.replicaPara = replicaPara;
    }
}
