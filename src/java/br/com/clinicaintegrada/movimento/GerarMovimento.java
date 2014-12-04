package br.com.clinicaintegrada.movimento;

import br.com.clinicaintegrada.financeiro.Baixa;
import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.Caixa;
import br.com.clinicaintegrada.financeiro.ChequePag;
import br.com.clinicaintegrada.financeiro.ChequeRec;
import br.com.clinicaintegrada.financeiro.CondicaoPagamento;
import br.com.clinicaintegrada.financeiro.ContaBanco;
import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.financeiro.FStatus;
import br.com.clinicaintegrada.financeiro.FTipoDocumento;
import br.com.clinicaintegrada.financeiro.FormaPagamento;
import br.com.clinicaintegrada.financeiro.Lote;
import br.com.clinicaintegrada.financeiro.MensagemBoleto;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.MovimentoInativo;
import br.com.clinicaintegrada.financeiro.TipoPagamento;
import br.com.clinicaintegrada.financeiro.dao.BoletoDao;
import br.com.clinicaintegrada.financeiro.dao.ContaCobrancaDao;
import br.com.clinicaintegrada.financeiro.dao.FormaPagamentoDao;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.principal.DB;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Dao;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.persistence.Query;

public class GerarMovimento extends DB {

    public String gerarBoletos(String referencia, String vencimento, int id_grupo_cidade, int id_convencao, int id_servico, int id_tipo_servico, int id_rotina) {
        String textQry = "";
        getEntityManager().getTransaction().begin();
        Query qry = null;
        Logger log = new Logger();

        try {
            /* INSERÇÃO DE LOTE ***/
            textQry = "insert into fin_lote (dt_emissao, ds_pag_rec, nr_valor, dt_lancamento, id_filial, id_pessoa, id_tipo_documento, id_rotina, is_avencer_contabil) "
                    + "(select '" + DataHoje.data() + "' as dt_emissao, 'R' as ds_pag_rec, 0 as nr_valor, '" + DataHoje.data() + "' as dt_lancamento, 1 as id_filial, cv.id_pessoa, 2 as id_tipo_documento, 4 as id_rotina, false as is_avencer_contabil "
                    + "   from arr_contribuintes_vw cv "
                    + "   left join fin_bloqueia_servico_pessoa as sp on sp.id_pessoa = cv.id_pessoa and sp.id_servicos = 3 and '04/11/2014' >= sp.dt_inicio and '04/11/2014' <= sp.dt_fim   "
                    + "  where cv.dt_inativacao is null and cv.id_grupo_cidade = " + id_grupo_cidade + " and cv.id_convencao = " + id_convencao + " and cv.id_pessoa not in "
                    + "       (select id_pessoa from fin_movimento where ds_referencia='" + referencia + "' and id_servicos = " + id_servico + " and id_tipo_servico = " + id_tipo_servico + " and is_ativo = true) "
                    + " and (sp.is_geracao is true or sp.is_geracao is null));";
            qry = getEntityManager().createNativeQuery(textQry);
            if (qry.executeUpdate() <= 0) {
                getEntityManager().getTransaction().rollback();
                return "Erro ao gravar lote!";
            }
            log.novo("Geracao geral: FIN_LOTE", "Data: " + DataHoje.data() + " id_grupo_cidade: " + id_grupo_cidade + " id_convencao: " + id_convencao + " id_servico: " + id_servico + " referencia: " + referencia);
            /* ---------------- ***/
            /* ---------------- ***/

            /* INSERÇÃO DE MOVIMENTO */
            textQry = "insert into fin_movimento (ds_referencia, ds_es, ds_documento, nr_valor, dt_vencimento_original, dt_vencimento, nr_ctr_boleto, id_pessoa, id_tipo_documento, id_tipo_servico, id_titular, id_servicos, id_beneficiario, id_lote, is_ativo, is_obrigacao,nr_multa,nr_desconto,nr_taxa,nr_quantidade, "
                    + "nr_valor_baixa, nr_repasse_automatico, nr_correcao, nr_desconto_ate_vencimento, nr_juros, id_plano5)"
                    + "(select '" + referencia + "' as ds_referencia, 'E' as ds_es, null as ds_documento, 0 as nr_valor, '" + vencimento + "' as dt_vencimento_original, '" + vencimento + "' as dt_vencimento, null as nr_ctr_boleto, c.id_pessoa, 2 as id_tipo_documento, " + id_tipo_servico + " as id_tipo_servico, "
                    + "c.id_pessoa as id_titular, " + id_servico + " as id_servicos, c.id_pessoa as id_beneficiario, l.id as id_lote, true as is_ativo, true as is_obrigacao, 0 as nr_multa, 0 as nr_desconto, 0 as nr_taxa, 1 as nr_quantidade,"
                    + "0 as nr_valor_baixa, 0 as nr_repasse_automatico, 0 as nr_correcao, 0 as nr_desconto_ate_vencimento, 0 as nr_juros, se.id_plano5 as id_plano5 "
                    + "from arr_contribuintes_vw as c "
                    + "inner join fin_lote as l on l.id_pessoa = c.id_pessoa "
                    + "left join fin_movimento as m on m.id_lote = l.id "
                    + "left join fin_bloqueia_servico_pessoa as sp on sp.id_pessoa = c.id_pessoa and sp.id_servicos = " + id_servico + " and '" + vencimento + "' >= sp.dt_inicio and  '" + vencimento + "' <= sp.dt_fim  "
                    + "inner join fin_servicos as se on se.id = " + id_servico
                    + " where m.id_lote is null and c.id_grupo_cidade = " + id_grupo_cidade + " and c.id_convencao = " + id_convencao
                    + "  and (sp.is_geracao is true or sp.is_geracao is null)"
                    + "  and c.dt_inativacao is null);";
            qry = getEntityManager().createNativeQuery(textQry);
            if (qry.executeUpdate() <= 0) {
                getEntityManager().getTransaction().rollback();
                return "Erro ao gravar movimento!";
            }
            log.novo("Geracao geral: FIN_MOVIMENTO", "Data: " + DataHoje.data());
            /* ------------------------ ***/
            /* ------------------------ ***/

            /* INSERÇÃO DE BOLETO */
            textQry = "insert into fin_boleto (nr_ctr_boleto, is_ativo, id_conta_cobranca) "
                    + "(select m.id as nr_ctr_boleto, true as is_ativo, scc.id_conta_cobranca "
                    + "   from fin_movimento as m inner join fin_lote as l on l.id = m.id_lote inner join fin_servico_conta_cobranca as scc on scc.id_servicos = m.id_servicos and scc.id_tipo_servico = m.id_tipo_servico"
                    + "  where l.id_rotina = 4 and nr_ctr_boleto is null and m.id_servicos > 0 and m.id_servicos is not null"
                    + ");";
            qry = getEntityManager().createNativeQuery(textQry);
            if (qry.executeUpdate() <= 0) {
                getEntityManager().getTransaction().rollback();
                return "Erro ao gravar boleto!";
            }
            log.novo("Geracao geral: FIN_BOLETO", "Data: " + DataHoje.data());
            /* ---------------------- ***/
            /* ---------------------- ***/

            /* ATUALIZAÇÃO DE MOVIMENTO */
            textQry = "update fin_movimento set nr_ctr_boleto = text(fin_movimento.id), ds_documento = ds_boleto from fin_boleto "
                    + "where text(fin_movimento.id) = fin_boleto.nr_ctr_boleto and (fin_movimento.nr_ctr_boleto is null or length(fin_movimento.nr_ctr_boleto) = 0);";
            qry = getEntityManager().createNativeQuery(textQry);
            if (qry.executeUpdate() <= 0) {
                getEntityManager().getTransaction().rollback();
                return "Erro ao atualizar movimentos!";
            }
            log.novo("Geracao geral: atualiza FIN_MOVIMENTO", "Data: " + DataHoje.data());
            /* ---------------------- ***/
            /* ---------------------- ***/

            /* INSERÇÃO DE MENSAGEM COBRANÇA */
            textQry = "insert into fin_mensagem_cobranca (id_mensagem_convencao,id_movimento) "
                    + "(select mc.id,m.id from fin_movimento as m "
                    + "inner join arr_contribuintes_vw as c on c.id_pessoa=m.id_pessoa "
                    + "inner join arr_mensagem_convencao  as mc on mc.ds_referencia = m.ds_referencia "
                    + "and mc.id_servicos = m.id_servicos "
                    + "and mc.id_tipo_servico = m.id_tipo_servico "
                    + "and mc.id_convencao = c.id_convencao and mc.id_grupo_cidade = c.id_grupo_cidade "
                    + "left join fin_mensagem_cobranca as mco on m.id = mco.id_movimento "
                    + "where mco.id_movimento is null and m.is_ativo = true and m.id_baixa is null "
                    + ");";
            qry = getEntityManager().createNativeQuery(textQry);
            if (qry.executeUpdate() <= 0) {
                getEntityManager().getTransaction().rollback();
                return "Erro ao gravar mensagem cobrança!";
            }
            log.novo("Geracao geral: FIN_MENSAGEM_COBRANCA", "Data: " + DataHoje.data());
            /* ---------------------- ***/
        } catch (Exception e) {
            log.novo("Geracao geral: ERRO", "Data: " + DataHoje.data() + " " + e.getMessage());
            getEntityManager().getTransaction().rollback();
            return "Erro no processo de criação, verifique os logs!";
        }
        getEntityManager().getTransaction().commit();
        return "Gerado com sucesso!";
    }

    public static boolean salvarListaMovimento(List<Movimento> listaMovimento) {
        return false;
    }

//    public static synchronized String salvarListaAcordo(Acordo acordo, List<Movimento> listaMovimento, List<Movimento> listaAcordados, List<String> listaHistorico) {
//        Dao dao = new Dao();
//        ContaCobrancaDao dbc = new ContaCobrancaDao();
//        Logger log = new Logger();
//        Boleto boleto = new Boleto();
//        MensagemBoleto mb = new MensagemBoleto();
//        MensagemBoletoDao mensagemBoletoDao = new MensagemBoletoDao();
//
//        MovimentoDB db = new MovimentoDBToplink();
//        for (int i = 0; i < listaMovimento.size(); i++) {
//            if (listaMovimento.get(i).getPessoa().getId() != 0) {
//                Convencao convencao = dbco.pesquisarCnaeConvencaoPorPessoa(listaMovimento.get(i).getPessoa().getId());
//                if (convencao == null) {
//                    return "Convenção não encontrada!";
//                }
//
//                mc = dbm.verificaMensagem(convencao.getId(),
//                        listaMovimento.get(i).getServicos().getId(),
//                        listaMovimento.get(i).getTipoServico().getId(),
//                        dbgc.grupoCidadesPorPessoa(listaMovimento.get(i).getPessoa().getId(),
//                                convencao.getId()).getId(),
//                        "");
//                if (mc == null) {
//                    return "Mensagem de cobrança não encontrada";
//                }
//            }
//            ContaCobranca cc = dbc.pesquisaServicoCobranca(listaMovimento.get(i).getServicos().getId(), listaMovimento.get(i).getTipoServico().getId());
//            int id_boleto = db.inserirBoletoNativo(cc.getId());
//
//            if (id_boleto != -1) {
//                sv.abrirTransacao();
//                if (listaMovimento.get(i).getId() == -1) {
//                    // LOTE ---
//                    Lote lote = new Lote();
//                    lote.setDepartamento(null);
//                    lote.setStatus((FStatus) sv.pesquisaCodigo(1, "FStatus"));
//                    lote.setLancamento(DataHoje.data());
//                    lote.setAvencerContabil(false);
//                    lote.setEmissao(DataHoje.data());
//                    lote.setFTipoDocumento((FTipoDocumento) sv.pesquisaCodigo(2, "FTipoDocumento"));
//                    lote.setValor(listaMovimento.get(i).getValor());
//                    lote.setRotina((Rotina) sv.pesquisaCodigo(4, "Rotina"));
//                    lote.setPessoa(listaMovimento.get(i).getPessoa());
//                    lote.setCondicaoPagamento((CondicaoPagamento) sv.pesquisaCodigo(1, "CondicaoPagamento"));
//                    lote.setFilial((Filial) sv.pesquisaCodigo(1, "Filial"));
//                    lote.setPessoaSemCadastro(null);
//                    lote.setEvt(null);
//                    lote.setPlano5(null);
//                    lote.setDocumento("");
//
//                    if (cc == null) {
//                        sv.desfazerTransacao();
//                        return "Conta cobrança não encontrada!";
//                    }
//                    if (sv.inserirObjeto(lote)) {
//                        log.novo("Salvar Lote", "ID: " + lote.getId() + " Pessoa: " + lote.getPessoa().getNome() + " Data: " + lote.getEmissao());
//                    } else {
//                        sv.desfazerTransacao();
//                        return "Erro ao salvar Lote, verifique os logs!";
//                    }
//
//                    // ACORDO ----
//                    acordo.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
//                    if (sv.inserirObjeto(acordo)) {
//                        log.novo("Salvar Acordo", "ID: " + acordo.getId() + " Usuario: " + acordo.getUsuario().getPessoa().getNome());
//                    } else {
//                        sv.desfazerTransacao();
//                        return "Erro ao salvar acordo, verifique os logs!";
//                    }
//
//                    // MOVIMENTO ----
//                    listaMovimento.get(i).setLote(lote);
//                    if (sv.inserirObjeto(listaMovimento.get(i))) {
//                        // BOLETO ---
//                        boleto = (Boleto) sv.pesquisaCodigo(id_boleto, "Boleto");
//                        boleto.setContaCobranca(cc);
//
//                        // SE AGRUPA FOR TRUE** NR_CTR_BOLETO = ID_PESSOA + FATOR DE VENCIMENTO
//                        if (listaMovimento.get(i).getServicos().isAgrupaBoleto()) {
//                        } else {
//                            boleto.setNrCtrBoleto(String.valueOf(listaMovimento.get(i).getId()));
//
//                            listaMovimento.get(i).setDocumento(boleto.getBoletoComposto());
//                            listaMovimento.get(i).setNrCtrBoleto(boleto.getNrCtrBoleto());
//                            listaMovimento.get(i).setAcordo(acordo);
//                            sv.alterarObjeto(listaMovimento.get(i));
//                            sv.alterarObjeto(boleto);
//
//                            if (listaMovimento.get(i).getPessoa().getId() != 0) {
//                                if (!sv.inserirObjeto(new MensagemCobranca(-1, listaMovimento.get(i), mc))) {
//                                    sv.desfazerTransacao();
//                                    return "Erro ao salvar mensagem Cobrançam, verifique os logs!";
//                                }
//                            }
//                        }
//
//                        log.novo("Salvar Movimento", "ID: " + listaMovimento.get(i).getId() + " Pessoa: " + listaMovimento.get(i).getPessoa().getNome() + " Valor: " + listaMovimento.get(i).getValor());
//                    } else {
//                        sv.desfazerTransacao();
//                        return "Erro ao salvar movimento, verifique os logs!";
//                    }
//
//                    // MOVIMENTO ACORDADOS ----
//                    for (int wi = 0; wi < listaAcordados.size(); wi++) {
//                        listaAcordados.get(wi).setAcordo(acordo);
//                        listaAcordados.get(wi).setAtivo(false);
//                        listaAcordados.get(wi).setValorBaixa(0);
//                        if (!sv.alterarObjeto(listaAcordados.get(wi))) {
//                            sv.desfazerTransacao();
//                            return "Erro ao salvar boletos acordados!";
//                        }
//                    }
//
//                    // HISTORICO ----
//                    Historico his = new Historico();
//
//                    his.setMovimento(listaMovimento.get(i));
//                    his.setComplemento("");
//                    his.setHistorico(listaHistorico.get(i));
//                    if (sv.inserirObjeto(his)) {
//                        log.novo("Salvar Historico", "ID: " + his.getId() + " OBS: " + his.getHistorico() + " ID_MOVIMENTO: " + his.getMovimento().getId());
//                    } else {
//                        sv.desfazerTransacao();
//                        return "Erro ao salvar histórico, verifique os logs!";
//                    }
//                } else {
//                    sv.desfazerTransacao();
//                    return "Id do movimento deve ser -1";
//                }
//                sv.comitarTransacao();
//                listaAcordados.clear();
//            } else {
//                return "Erro ao salvar boleto, verifique os logs!";
//            }
//        }
//        return "";
//    }
    public static boolean salvarUmMovimentoBaixa(Lote lote, Movimento movimento) {
        Dao dao = new Dao();
        ContaCobrancaDao contaCobrancaDao = new ContaCobrancaDao();
        Logger log = new Logger();
        Boleto boleto = new Boleto();
        BoletoDao boletoDao = new BoletoDao();

        ContaCobranca cc = contaCobrancaDao.pesquisaServicoCobranca(movimento.getServicos().getId(), movimento.getTipoServico().getId());
        int id_boleto = boletoDao.saveBoletoNativeQuery(cc.getId());

        if (id_boleto != -1) {
            dao.openTransaction();
            if (movimento.getId() == -1) {
                // LOTE ---
                lote.setDepartamento(null);
                lote.setStatus((FStatus) dao.find(new FStatus(), 1));
                lote.setLancamento(DataHoje.dataHoje());
                lote.setEmissao(DataHoje.dataHoje());
                lote.setFtipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 2));
                lote.setValor(movimento.getValor());
                lote.setPessoa(movimento.getPessoa());
                lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 1));
                lote.setFilial((Filial) dao.find(new Filial(), 1));

                if (cc == null) {
                    dao.rollback();
                    return false;
                }
                if (dao.save(lote)) {
                    log.save("Salvar Lote - ID: " + lote.getId() + " Pessoa: " + lote.getPessoa().getNome() + " Data: " + lote.getEmissao());
                } else {
                    dao.rollback();
                    return false;
                }

                // MOVIMENTO ----
                movimento.setLote(lote);
                if (dao.save(movimento)) {
                    // BOLETO ---

                    boleto = (Boleto) dao.find(new Boleto(), id_boleto);
                    boleto.setContaCobranca(cc);

                    // SE AGRUPA FOR TRUE** NR_CTR_BOLETO = ID_PESSOA + FATOR DE VENCIMENTO
//                    if (movimento.getServicos().isAgrupaBoleto()) {
//                    } else {
//                        // SE AGRUPA FOR FALSE** NR_CTR_BOLETO = ID_MOVIMENTO
//                        boleto.setNrCtrBoleto(String.valueOf(movimento.getId()));
//                        movimento.setDocumento(boleto.getBoletoComposto());
//                        movimento.setNrCtrBoleto(boleto.getNrCtrBoleto());
//
//                        sv.alterarObjeto(movimento);
//                        sv.alterarObjeto(boleto);
//                    }
                    log.save("Salvar Movimento - ID: " + movimento.getId() + " Pessoa: " + movimento.getPessoa().getNome() + " Valor: " + movimento.getValor());
                } else {
                    dao.rollback();
                    return false;
                }
            } else {
                dao.rollback();
                return false;
            }
            dao.commit();
        } else {
            return false;
        }
        return true;
    }

    public static boolean salvarUmMovimento(Lote lote, Movimento movimento) {
        Dao dao = new Dao();
        ContaCobrancaDao contaCobrancaDao = new ContaCobrancaDao();
        Logger log = new Logger();
        MensagemBoleto mensagemBoleto = new MensagemBoleto();
        //MensagemBoletoDao mensagemBoletoDao = new MensagemConvencaoDBToplink();

        BoletoDao boletoDao = new BoletoDao();

        ContaCobranca cc = contaCobrancaDao.pesquisaServicoCobranca(movimento.getServicos().getId(), movimento.getTipoServico().getId());
//        if (movimento.getPessoa().getId() != 0) {
//            Convencao convencao = dbco.pesquisarCnaeConvencaoPorPessoa(movimento.getPessoa().getId());
//            if (convencao == null) {
//                return false;
//            }
//
//            if (movimento.getTipoServico().getId() != 4) {
//                mc = dbm.verificaMensagem(convencao.getId(),
//                        movimento.getServicos().getId(),
//                        movimento.getTipoServico().getId(),
//                        dbgc.grupoCidadesPorPessoa(movimento.getPessoa().getId(),
//                                convencao.getId()).getId(),
//                        movimento.getReferencia());
//                if (mc == null) {
//                    return false;
//                }
//            }
//        }
        int id_boleto = boletoDao.saveBoletoNativeQuery(cc.getId());

        if (id_boleto != -1) {
            dao.openTransaction();
            if (movimento.getId() == -1) {
                // LOTE ---
                lote.setDepartamento(null);
                lote.setStatus((FStatus) dao.find(new FStatus(), 1));
                lote.setLancamento(DataHoje.dataHoje());
                lote.setEmissao(DataHoje.dataHoje());
                lote.setFtipoDocumento((FTipoDocumento) dao.find(new FTipoDocumento(), 2));
                lote.setValor(movimento.getValor());
                lote.setPessoa(movimento.getPessoa());
                lote.setCondicaoPagamento((CondicaoPagamento) dao.find(new CondicaoPagamento(), 1));
                lote.setFilial((Filial) dao.find(new Filial(), 1));
                lote.setDocumento("");

                if (cc == null) {
                    dao.rollback();
                    return false;
                }
                if (dao.save(lote)) {
                    log.save("Salvar Lote - ID: " + lote.getId() + " Pessoa: " + lote.getPessoa().getNome() + " Data: " + lote.getEmissao());
                } else {
                    dao.rollback();
                    return false;
                }

                // MOVIMENTO ----
                movimento.setLote(lote);
                movimento.setVencimento(mensagemBoleto.getVencimento());

                if (dao.save(movimento)) {
                    // BOLETO ---

                    Boleto boleto = (Boleto) dao.find(new Boleto(), id_boleto);
                    boleto.setContaCobranca(cc);

                    // SE AGRUPA FOR TRUE** NR_CTR_BOLETO = ID_PESSOA + FATOR DE VENCIMENTO
//                    if (movimento.getServicos().isAgrupaBoleto()) {
//                    } else {
//                        // SE AGRUPA FOR FALSE** NR_CTR_BOLETO = ID_MOVIMENTO
//                        //boleto.setNrBoleto(boleto.getContaCobranca().getId());
//                        boleto.setNrCtrBoleto(String.valueOf(movimento.getId()));
//
//                        movimento.setDocumento(boleto.getBoletoComposto());
//                        movimento.setNrCtrBoleto(boleto.getNrCtrBoleto());
//                        dao.update(movimento);
//                        dao.update(boleto);
//
//                        if (movimento.getPessoa().getId() != 0 && movimento.getTipoServico().getId() != 4) {
//                            if (!dao.save(new MensagemCobranca(-1, movimento, mc))) {
//                                dao.rollback();
//                                return false;
//                            }
//                        }
//                    }
                    log.save("Salvar Movimento - ID: " + movimento.getId() + " Pessoa: " + movimento.getPessoa().getNome() + " Valor: " + movimento.getValor());
                } else {
                    dao.rollback();
                    return false;
                }
            } else {
                dao.rollback();
                return false;
            }
            dao.commit();
        } else {
            return false;
        }
        return true;
    }

    public static boolean alterarUmMovimento(Movimento movimento) {
        Dao dao = new Dao();
        dao.openTransaction();
        if (movimento.getId() != -1) {
            // LOTE ---
            Lote lote = (Lote) dao.find(movimento.getLote());
            lote.setValor(movimento.getValor());
//            
//            //ServicoContaCobranca scc = dbc.pesquisaServPorIdServIdTipoServ(movimento.getServicos().getId(), movimento.getTipoServico().getId());
            if (dao.update(lote)) {
                // log.update("", "Alterar Lote - ID: " + lote.getId() + " Pessoa: " + lote.getPessoa().getNome() + " Data: " + lote.getEmissao() + " Valor: " + lote.getValor());
            } else {
                dao.rollback();
                return false;
            }
//
//            // MOVIMENTO ----
//            movimento.setLote(lote);
            if (dao.update(movimento)) {
                // log.update("", "Alterar Movimento - ID: " + movimento.getId() + " Pessoa: " + movimento.getPessoa().getNome() + " Valor: " + movimento.getValor());
            } else {
                dao.rollback();
                return false;
            }
            dao.commit();
        } else {
            return false;
        }
        return true;
    }

//    public static boolean excluirUmMovimento(Movimento movimento) {
//        String mensagem = "Deletados com sucesso!";
//        MovimentoDB movDB = new MovimentoDBToplink();
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        Lote lote = null;
//        MensagemCobranca mensagemCobranca = null;
//        List<ImpressaoWeb> listaLogWeb = new ArrayList();
//        try {
//            if (movimento.isAtivo() && movimento.getBaixa() == null) {
//                mensagemCobranca = movDB.pesquisaMensagemCobranca(movimento.getId());
//                listaLogWeb = movDB.pesquisaLogWeb(movimento.getId());
//                sv.abrirTransacao();
//
//                // EXCLUI LISTA IMPRESSAO
//                for (ImpressaoWeb imp : listaLogWeb) {
//                    if (!sv.deletarObjeto(sv.pesquisaCodigo(imp.getId(), "ImpressaoWeb"))) {
//                        sv.desfazerTransacao();
//                        mensagem = "Erro na exclusão da lista de LogWeb!";
//                        return false;
//                    }
//                }
//
//                // EXCLUI MENSAGEM BOLETO
//                if (mensagemCobranca != null) {
//                    if (!sv.deletarObjeto(sv.pesquisaCodigo(mensagemCobranca.getId(), "MensagemCobranca"))) {
//                        sv.desfazerTransacao();
//                        mensagem = "Erro na exclusão da mensagem do boleto!";
//                        return false;
//                    }
//                }
//
//                // EXCLUI MOVIMENTO
//                if (!sv.deletarObjeto(sv.pesquisaCodigo(movimento.getId(), "Movimento"))) {
//                    sv.desfazerTransacao();
//                    mensagem = "Erro na exclusão do movimento!";
//                    return false;
//                }
//
//                lote = movimento.getLote();
//                // EXCLUI LOTE
//                if (!sv.deletarObjeto(sv.pesquisaCodigo(lote.getId(), "Lote"))) {
//                    sv.desfazerTransacao();
//                    mensagem = "Erro na exclusão do lote.";
//                    return false;
//                }
//
//                // EXCLUI BOLETO 
//                Object bols = sv.pesquisaCodigo(movDB.pesquisaBoletos(String.valueOf(movimento.getId())).getId(), "Boleto");
//                if (bols != null) {
//                    if (!sv.deletarObjeto(bols)) {
//                        sv.desfazerTransacao();
//                        mensagem = "Erro na exclusão do boleto!";
//                        return false;
//                    }
//                }
//                sv.comitarTransacao();
//                return true;
//            }
//        } catch (Exception e) {
//            mensagem = e.getMessage();
//        }
//        return false;
//    }
    public static String inativarUmMovimento(Movimento movimento, String historico) {
        String mensagem = "";
        BoletoDao boletoDao = new BoletoDao();
        Dao dao = new Dao();
        MovimentoInativo mi = new MovimentoInativo();

        try {
            if (movimento.isAtivo() && movimento.getBaixa() == null || movimento.getBaixa().getId() == -1) {
                dao.openTransaction();
                movimento.setAtivo(false);

                mi.setData(DataHoje.dataHoje());
                mi.setMovimento(movimento);
                mi.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                mi.setHistorico(historico);

                Boleto bol = boletoDao.findBoletosByNrCtrBoleto(movimento.getNrCtrBoleto());
                if (bol != null) {
                    bol.setAtivo(false);
                    if (!dao.update(bol)) {
                        dao.rollback();
                        return "Erro ao excluir Boleto, verifique os logs!";
                    }
                }

                if (!dao.update(movimento)) {
                    dao.rollback();
                    return "Erro ao excluir Movimento, verifique os logs!";
                }

                if (!dao.save(mi)) {
                    dao.rollback();
                    return "Erro ao salvar Motivo de Inativação, verifique os logs!";
                }
                dao.commit();
            }
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
        return mensagem;
    }

    public static boolean estornarMovimento(Movimento movimento) {
        FormaPagamentoDao formaPagamentoDao = new FormaPagamentoDao();
        Dao dao = new Dao();
        try {
            if (movimento == null || movimento.getBaixa() == null || (!movimento.isAtivo())) {
                return true;
            }

            MovimentoDao movimentoDao = new MovimentoDao();
            List<Movimento> lista = movimentoDao.listMovimentosByBaixaOrderByBaixa(movimento.getBaixa().getId());

            dao.openTransaction();
            if (lista.isEmpty()) {
                dao.rollback();
                return false;
            } else if (lista.size() > 1) {
                List<FormaPagamento> formaPagamento = formaPagamentoDao.findFormaPagamentoByBaixa(movimento.getBaixa().getId());

                for (int i = 0; i < formaPagamento.size(); i++) {
                    if (!dao.delete(formaPagamento.get(i))) {
                        dao.rollback();
                        return false;
                    }

                    if (formaPagamento.get(i).getChequeRec() != null) {
                        if (!dao.delete(formaPagamento.get(i).getChequeRec())) {
                            dao.rollback();
                            return false;
                        }
                    }
                }

                Baixa baixa = (Baixa) dao.find(movimento.getBaixa());

                for (int i = 0; i < lista.size(); i++) {
                    lista.get(i).setBaixa(null);
                    lista.get(i).setJuros(0);
                    lista.get(i).setMulta(0);
                    lista.get(i).setCorrecao(0);
                    lista.get(i).setTaxa(0);
                    lista.get(i).setDesconto(0);
                    lista.get(i).setValorBaixa(0);

                    if (!dao.update(lista.get(i))) {
                        dao.rollback();
                        return false;
                    }
                }

                if (!dao.delete(baixa)) {
                    dao.rollback();
                    return false;
                }
            } else {
                List<FormaPagamento> formaPagamento = formaPagamentoDao.findFormaPagamentoByBaixa(movimento.getBaixa().getId());
                for (int i = 0; i < formaPagamento.size(); i++) {
                    if (!dao.delete(formaPagamento.get(i))) {
                        dao.rollback();
                        return false;
                    }

                    if (formaPagamento.get(i).getChequeRec() != null) {
                        if (!dao.delete(formaPagamento.get(i).getChequeRec())) {
                            dao.rollback();
                            return false;
                        }
                    }
                }
                Baixa baixa = (Baixa) dao.find(movimento.getBaixa());
                movimento.setBaixa(null);
                movimento.setJuros(0);
                movimento.setMulta(0);
                movimento.setCorrecao(0);
                movimento.setTaxa(0);
                movimento.setDesconto(0);
                movimento.setValorBaixa(0);
                if (!dao.update(movimento)) {
                    dao.rollback();
                    return false;
                }

                if (!dao.delete(baixa)) {
                    dao.rollback();
                    return false;
                }
            }
            dao.commit();
            return true;
        } catch (Exception e) {
            dao.rollback();
        }
        return false;
    }

    public static boolean baixarMovimento(Movimento movimento, Usuario usuario, String pagamento, float valor_liquido, Date dataCredito, String numeroComposto, int nrSequencia) {
        Dao dao = new Dao();

        Baixa baixa = new Baixa();
        baixa.setUsuario(usuario);
        baixa.setFechamentoCaixa(null);
        baixa.setBaixa(pagamento);
        baixa.setImportacao(DataHoje.data());
        baixa.setSequenciaBaixa(nrSequencia);
        baixa.setDocumentoBaixa(numeroComposto);
        baixa.setCaixa(null);

        dao.openTransaction();
        if (!dao.save(baixa)) {
            dao.rollback();
            return false;
        }

        // CALCULO PARA PORCENTAGEM DO VALOR PAGO -- NESSE CASO DE ARRECADACAO É 100%
        //float calc = Moeda.multiplicarValores(Moeda.divisaoValores(fp.get(i).getValor(), valorTotal), 100);
        //calc = Moeda.converteFloatR$Float(calc);
        FormaPagamento fp = new FormaPagamento(-1,
                baixa,
                null,
                null,
                100,
                movimento.getValorBaixa(),
                movimento.getLote().getFilial(),
                null,
                null,
                null,
                (TipoPagamento) dao.find(new TipoPagamento(), 3),
                valor_liquido,
                dataCredito,
                0
        );

        if (!dao.save(fp)) {
            dao.rollback();
            return false;
        }

        movimento.setBaixa(baixa);

        //movimento.setValor(movimento.getValorBaixa());
        //movimento.setAtivo(false);
        if (!dao.update(movimento)) {
            dao.rollback();
            return false;
        }
//        
//        Movimento movBaixa = movimento;
//        movBaixa.setId(-1);
//        movBaixa.setAtivo(true);
//        movBaixa.setValor(movimento.getValorBaixa());
//        
//        if (!sv.inserirObjeto(movBaixa)){
//            sv.desfazerTransacao();
//            return false;
//        }

        //Movimento movi = new Movimento(-1, null, movimento.getPlano5(), movimento.getPessoa(), movimento.getServicos(), baixa, movimento.getTipoServico(), null, movimento.getValor(), movimento.getAcordado(), movimento.getReferencia(),movimento.getDtVencimento(), 1, true, movimento.getEs(), false, movimento.getTitular(), movimento.getBeneficiario(), "", movimento.getNrCtrBoleto(), movimento.getDtVencimentoOriginal(), 0, 0, 0, 0, 0, movimento.getTaxa(), movimento.getValorBaixa(), 0);
//                Movimento movi = movimento;
//                movi.setId(-1);
//                movi.setAtivo(true);
//                
//                if (salvarUmMovimento(null, movimento))
        dao.commit();
        return true;
    }

    public static boolean baixarMovimentoManual(List<Movimento> movimento, Usuario usuario, List<FormaPagamento> fp, float valorTotal, String pagamento, Caixa caixa) {
        // 15
        // 000003652580001
        // 8
        // 30042013
        // 10
        // 0000022912
        try {
            String numeroComposto = "";
            if (movimento.get(0).getServicos() != null) {

                if (movimento.get(0).getServicos().getId() == 1) {
                    //String documento = movimento.get(0).getPessoa().getDocumento().replace(".", "").replace("/", "").replace("-", "").substring(0, 12);
                    String documento = movimento.get(0).getDocumento();
                    documento = ("000000000000000").substring(0, 15 - documento.length()) + documento;
                    String d_pagamento = ("00000000").substring(0, 8 - pagamento.replace("/", "").length()) + pagamento.replace("/", "");
                    String v_pago = ("0000000000").substring(0, 10 - Moeda.converteR$Float(valorTotal).replace(".", "").replace(",", "").length()) + Moeda.converteR$Float(valorTotal).replace(".", "").replace(",", "");
                    numeroComposto = documento + d_pagamento + v_pago;
                }

            }
            Dao dao = new Dao();
            Baixa baixa = new Baixa();
            baixa.setUsuario(usuario);
            baixa.setFechamentoCaixa(null);
            baixa.setBaixa(pagamento);
            baixa.setDocumentoBaixa(numeroComposto);
            baixa.setCaixa(caixa);

            dao.openTransaction();
            if (!dao.save(baixa)) {
                dao.rollback();
                return false;
            }
            for (FormaPagamento fp1 : fp) {
                fp1.setBaixa(baixa);
                float calc = Moeda.multiplicarValores(Moeda.divisaoValores(fp1.getValor(), valorTotal), 100);
                calc = Moeda.converteFloatR$Float(calc);
                fp1.setValorP(calc);
                ChequeRec ch = new ChequeRec();
                if (fp1.getChequeRec() != null) {
                    ch.setAgencia(fp1.getChequeRec().getAgencia());
                    ch.setBanco(fp1.getChequeRec().getBanco());
                    ch.setCheque(fp1.getChequeRec().getCheque());
                    ch.setConta(fp1.getChequeRec().getConta());
                    ch.setEmissao(fp1.getChequeRec().getEmissao());
                    ch.setStatus(fp1.getChequeRec().getStatus());
                    ch.setVencimento(fp1.getChequeRec().getVencimento());
                    if (!dao.save(ch)) {
                        dao.rollback();
                        return false;
                    }
                    fp1.setChequeRec(ch);
                }
                ChequePag ch_p = new ChequePag();
                if (fp1.getChequePag() != null) {
                    ch_p.setCheque(fp1.getChequePag().getCheque());
                    ch_p.setPlano5(fp1.getChequePag().getPlano5());
                    ch_p.setStatus(fp1.getChequePag().getStatus());
                    ch_p.setVencimento(fp1.getChequePag().getVencimento());

                    if (!dao.save(ch_p)) {
                        dao.rollback();
                        return false;
                    }
                    fp1.setChequePag(ch_p);

                    ContaBanco cb = (ContaBanco) dao.find(ch_p.getPlano5().getContaBanco());
                    cb.setUCheque(cb.getUCheque() + 1);
                    if (!dao.update(cb)) {
                        dao.rollback();
                        return false;
                    }
                }

                if (!dao.save(fp1)) {
                    dao.rollback();
                    return false;
                }

            }

            for (int i = 0; i < movimento.size(); i++) {
                movimento.get(i).setBaixa(baixa);

                if (!dao.update(movimento.get(i))) {
                    dao.rollback();
                    return false;
                }
            }
            dao.commit();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Object[] baixarMovimentoSocial(List<Movimento> lista_movimento, Usuario usuario, String data_pagamento, float valor_baixa, float valor_taxa) {
        Dao dao = new Dao();
        Object[] lista_log = new Object[3];
        Baixa baixa = new Baixa();
        baixa.setUsuario(usuario);
        baixa.setFechamentoCaixa(null);
        baixa.setBaixa(data_pagamento);
        baixa.setImportacao(DataHoje.data());
        baixa.setCaixa(null);

        dao.openTransaction();
        if (!dao.save(baixa)) {
            dao.rollback();
            lista_log[0] = 0; // 0 - ERRO AO INSERIR BAIXA
            lista_log[1] = baixa;
            lista_log[2] = "Erro ao inserir Baixa";
            return lista_log;
        }

        FormaPagamento fp = new FormaPagamento(
                -1,
                baixa,
                null,
                null,
                100,
                valor_baixa,
                lista_movimento.get(0).getLote().getFilial(),
                null, // lista_movimento.get(0).getPlano5(),
                null,
                null,
                (TipoPagamento) dao.find(new TipoPagamento(), 3),
                valor_baixa,
                null,
                0
        );

        if (!dao.save(fp)) {
            dao.rollback();
            lista_log[0] = 1; // 1 - ERRO AO INSERIR FORMA DE PAGAMENTO
            lista_log[1] = fp;
            lista_log[2] = "Erro ao inserir Forma de Pagamento";
            return lista_log;
        }

        float soma = 0;
        for (Movimento movimento : lista_movimento) {
            soma = Moeda.somaValores(soma, movimento.getValor());

            movimento.setBaixa(baixa);
            if (!dao.update(movimento)) {
                dao.rollback();
                lista_log[0] = 2; // 2 - ERRO AO ALTERAR MOVIMENTO COM A BAIXA
                lista_log[1] = movimento;
                lista_log[2] = "Erro ao alterar Movimento";
                return lista_log;
            }
        }

        if (valor_baixa == soma) {
            // valor baixado corretamente
            // sv.comitarTransacao();
            for (Movimento movimento : lista_movimento) {
                movimento.setValorBaixa(movimento.getValor());
                if (!dao.update(movimento)) {
                    dao.rollback();
                    lista_log[0] = 9; // 9 - ERRO AO ALTERAR MOVIMENTO VALOR BAIXA CORRETO
                    lista_log[1] = movimento;
                    lista_log[2] = "Erro ao alterar Movimento com Desconto e Valor Baixa";
                }
            }
        } else if (valor_baixa < soma) {
            float acrescimo = Moeda.subtracaoValores(soma, valor_baixa);
            // valor da baixa é menor que os boletos ( O CLIENTE PAGOU MENOS )
            for (Movimento movimento : lista_movimento) {
                float valor = 0, percentual = 0;
                percentual = Moeda.multiplicarValores(Moeda.divisaoValores(movimento.getValor(), soma), 100);
                valor = Moeda.divisaoValores(Moeda.multiplicarValores(acrescimo, percentual), 100);

                movimento.setDesconto(valor);
                movimento.setValorBaixa(Moeda.subtracaoValores(movimento.getValor(), valor));

                if (!dao.update(movimento)) {
                    dao.rollback();
                    lista_log[0] = 3; // 3 - ERRO AO ALTERAR MOVIMENTO COM DESCONTO E VALOR BAIXA
                    lista_log[1] = movimento;
                    lista_log[2] = "Erro ao alterar Movimento com Desconto e Valor Baixa";
                    return lista_log;
                }
            }
            dao.commit();
            //sv.desfazerTransacao();
            lista_log[0] = 6; // 6 - VALOR DO ARQUIVO MENOR
            lista_log[1] = lista_movimento;
            lista_log[2] = "Valor do Boleto " + lista_movimento.get(0).getDocumento() + " - vencto. " + lista_movimento.get(0).getVencimento() + " - pag. " + data_pagamento + " MENOR com défit de " + Moeda.converteR$Float(acrescimo);
            return lista_log;
        } else if (valor_baixa > soma) {
            float acrescimo = Moeda.subtracaoValores(valor_baixa, soma);
            // valor da baixa é maior que os boletos ( O CLIENTE PAGOU MAIS ) 
            for (Movimento movimento : lista_movimento) {
                float valor = 0, percentual = 0;
                percentual = Moeda.multiplicarValores(Moeda.divisaoValores(movimento.getValor(), soma), 100);
                valor = Moeda.divisaoValores(Moeda.multiplicarValores(acrescimo, percentual), 100);

                movimento.setCorrecao(valor);
                movimento.setValorBaixa(Moeda.somaValores(valor, movimento.getValor()));

                if (!dao.update(movimento)) {
                    dao.rollback();
                    lista_log[0] = 4; // 4 - ERRO AO ALTERAR MOVIMENTO COM CORREÇÃO E VALOR BAIXA
                    lista_log[1] = movimento;
                    lista_log[2] = "Erro ao alterar Movimento com Correção e Valor Baixa";
                    return lista_log;
                }
            }
            dao.commit();
            //sv.desfazerTransacao();
            lista_log[0] = 7; // 7 - VALOR DO ARQUIVO MAIOR
            lista_log[1] = lista_movimento;
            lista_log[2] = "Valor do Boleto " + lista_movimento.get(0).getDocumento() + " - vencto. " + lista_movimento.get(0).getVencimento() + " - pag. " + data_pagamento + " MAIOR com acréscimo de " + Moeda.converteR$Float(acrescimo);
            return lista_log;
        }
        dao.commit();
        //sv.desfazerTransacao();
        lista_log[0] = 5; // 5 - BAIXA CONCLUÍDA COM SUCESSO
        lista_log[1] = lista_movimento;
        lista_log[2] = "Baixa concluída com Sucesso!";
        return lista_log;
    }
}
