package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Baixa;
import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.Caixa;
import br.com.clinicaintegrada.financeiro.Cartao;
import br.com.clinicaintegrada.financeiro.ChequePag;
import br.com.clinicaintegrada.financeiro.ChequeRec;
import br.com.clinicaintegrada.financeiro.ContaBanco;
import br.com.clinicaintegrada.financeiro.FStatus;
import br.com.clinicaintegrada.financeiro.FormaPagamento;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.Plano5;
import br.com.clinicaintegrada.financeiro.TipoPagamento;
import br.com.clinicaintegrada.financeiro.dao.BoletoDao;
import br.com.clinicaintegrada.financeiro.dao.ChequePagDao;
import br.com.clinicaintegrada.financeiro.dao.ContaBancoDao;
import br.com.clinicaintegrada.financeiro.dao.ContaRotinaDao;
import br.com.clinicaintegrada.financeiro.dao.PlanoDao;
import br.com.clinicaintegrada.financeiro.list.ListMovimentoBaixaGeral;
import br.com.clinicaintegrada.impressao.ParametroRecibo;
import br.com.clinicaintegrada.movimento.GerarMovimento;
import br.com.clinicaintegrada.movimento.ImprimirBoleto;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.Departamento;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.Modulo;
import br.com.clinicaintegrada.seguranca.Rotina;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import br.com.clinicaintegrada.seguranca.dao.RotinaDao;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DaoInterface;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public class BaixaGeralBean {

    private String quitacao;
    private String vencimento;
    private String valor;
    private String numero;
    private String numeroChequePag;
    private String total;
    private List<ListMovimentoBaixaGeral> list;
    private List<Movimento> listMovimentos;
    private List<SelectItem> listCartao;
    private List<SelectItem> listBanco;
    private List<SelectItem> listBancoSaida;
    private int idConta;
    private int idTipoPagamento;
    private int idCartao;
    private int idBanco;
    private int idBancoSaida;
    private Rotina rotina;
    private Modulo modulo;
    private boolean desHabilitaConta;
    private boolean desHabilitaQuitacao;
    private boolean desHabilitaNumero;
    private boolean desHabilitadoVencimento;
    private boolean retorna;
    private String mensagem;
    private Plano5 plano5;
    private int index;
    private String tipo;
    private String banco;
    private String taxa;
    private String es;
    private ChequeRec chequeRec;
    private List<FormaPagamento> lfp;

    @PostConstruct
    public void init() {
        quitacao = DataHoje.data();
        vencimento = DataHoje.data();
        valor = "0";
        numero = "";
        numeroChequePag = "";
        total = "0.00";
        list = new ArrayList<>();
        listMovimentos = new ArrayList();
        listCartao = new ArrayList();
        listBanco = new ArrayList();
        listBancoSaida = new ArrayList();
        idConta = 0;
        idTipoPagamento = 0;
        idCartao = 0;
        idBanco = 0;
        idBancoSaida = 0;
        rotina = null;
        modulo = new Modulo();
        desHabilitaConta = false;
        desHabilitaQuitacao = false;
        desHabilitaNumero = false;
        desHabilitadoVencimento = false;
        retorna = false;
        mensagem = "";
        plano5 = new Plano5();
        index = 0;
        tipo = "";
        banco = "";
        taxa = "0";
        es = "";
        chequeRec = new ChequeRec();
        lfp = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("baixaGeralBean");
    }

    public void updateTipoPagamento() {
        TipoPagamento tipoPagamento = (TipoPagamento) new Dao().find(new TipoPagamento(), Integer.parseInt(((SelectItem) getListTipoPagamento().get(idTipoPagamento)).getDescription()));
        if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
            listCartao.clear();
            idCartao = 0;
        }
    }

    public void updateNumeroChequeConta() {
        PlanoDao planoDao = new PlanoDao();
        Plano5 p5 = planoDao.findPlano5ByContaBanco(Integer.valueOf(listBancoSaida.get(idBancoSaida).getDescription()), SessaoCliente.get().getId());
        ChequePag ch = new ChequePagDao().findChequePagByNumeroAndPlano5(numeroChequePag, p5.getId());
        Dao dao = new Dao();
        ContaBanco cb = (ContaBanco) dao.find(new ContaBanco(), Integer.valueOf(listBancoSaida.get(idBancoSaida).getDescription()));
        if (ch != null) {
            Messages.warn("Erro", "O cheque " + numeroChequePag + " já existe");
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
        }

        if (Integer.valueOf(numeroChequePag) == cb.getUCheque()) {
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
            return;
        }

        if (Integer.valueOf(numeroChequePag) == cb.getUCheque() + 1) {
            return;
        }

        if ((Integer.valueOf(numeroChequePag) + 1) > (cb.getUCheque() + 1)) {
            Messages.warn("Erro", "Número " + (Integer.valueOf(numeroChequePag)) + " maior que permitido!");
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
        }
    }

    public void saveRecibo(byte[] arquivo, Baixa baixa) {
        if (baixa.getCaixa() == null) {
            return;
        }

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
        Dirs.create("Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));

        String path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + ".pdf";
        File file_arquivo = new File(path_arquivo);

        if (file_arquivo.exists()) {
            path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + "_(2).pdf";
        }

        try {
            File fl = new File(path_arquivo);
            FileOutputStream out = new FileOutputStream(fl);
            out.write(arquivo);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public String printRecibo() throws IOException {
        if (!listMovimentos.isEmpty()) {
            try {
                Collection collection = new ArrayList();
                Juridica sindicato = (Juridica) new Dao().find(new Juridica(), 1);
                PessoaEnderecoDao pessoaEnderecoDao = new PessoaEnderecoDao();
                PessoaEndereco pe = pessoaEnderecoDao.pesquisaPessoaEnderecoPorPessoaTipo(1, 2);
                String formas[] = new String[10];
                for (int i = 0; i < lfp.size(); i++) {
                    // 4 - CHEQUE    
                    if (lfp.get(i).getTipoPagamento().getId() == 4) {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque();
                        // 5 - CHEQUE PRÉ
                    } else if (lfp.get(i).getTipoPagamento().getId() == 5) {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque() + " P: " + lfp.get(i).getChequeRec().getVencimento() + ")";
                        // QUALQUER OUTRO    
                    } else {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor());
                    }
                }
                for (int i = 0; i < listMovimentos.size(); i++) {
                    if (listMovimentos.get(i).getBaixa() == null) {
                        mensagem = "Baixa não foi concluída, não existe recibo";
                        return null;
                    }
                    collection.add(new ParametroRecibo(
                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                            sindicato.getPessoa().getNome(),
                            pe.getEndereco().getDescricaoEndereco().getDescricao(),
                            pe.getEndereco().getLogradouro().getDescricao(),
                            pe.getNumero(),
                            pe.getComplemento(),
                            pe.getEndereco().getBairro().getDescricao(),
                            pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                            pe.getEndereco().getCidade().getCidade(),
                            pe.getEndereco().getCidade().getUf(),
                            sindicato.getPessoa().getTelefone1(),
                            sindicato.getPessoa().getEmail1(),
                            sindicato.getPessoa().getSite(),
                            sindicato.getPessoa().getDocumento(),
                            listMovimentos.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
                            String.valueOf(listMovimentos.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
                            String.valueOf(listMovimentos.get(i).getBaixa().getId()), // ID_BAIXA
                            listMovimentos.get(i).getServicos() != null ? listMovimentos.get(i).getServicos().getDescricao() : "", // SERVICO
                            listMovimentos.get(i).getVencimentoString(), // VENCIMENTO
                            new BigDecimal(listMovimentos.get(i).getValorBaixa()), // VALOR BAIXA
                            listMovimentos.get(i).getBaixa().getUsuario().getLogin(),
                            listMovimentos.get(i).getBaixa().getBaixa(),
                            DataHoje.horaMinuto(),
                            formas[0],
                            formas[1],
                            formas[2],
                            formas[3],
                            formas[4],
                            formas[5],
                            formas[6],
                            formas[7],
                            formas[8],
                            formas[9],
                            ""
                    )
                    );

                }

                File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));
                JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(collection);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

                byte[] arquivo = JasperExportManager.exportReportToPdf(print);

                saveRecibo(arquivo, listMovimentos.get(0).getBaixa());

                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                res.setContentType("application/pdf");
                res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
                res.getOutputStream().write(arquivo);
                res.getCharacterEncoding();

                FacesContext.getCurrentInstance().responseComplete();
            } catch (JRException | IOException ex) {
                Logger.getLogger(MovimentosReceberBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return retorno();
    }

    public String retorno() throws IOException {
        if (retorna) {
            String url = Sessions.getString("urlRetorno");
            switch (url) {
                case "baixaBoleto":
                    Sessions.put("linkClicado", true);
//                ((BaixaBoletoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).getListaBoletos().clear();
                    //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaBoleto();
                    return "baixaBoleto";
                case "movimentosReceber":
                    ((MovimentosReceberBean) Sessions.getObject("movimentosReceberBean")).getListMovimento().clear();
                    Sessions.put("linkClicado", true);
                    return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).pagina("movimentosReceber");
                case "lancamentoFinanceiro":
                    Sessions.put("linkClicado", true);
                    return "lancamentoFinanceiro";
                //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).lancamentoFinanceiro();
                case "emissaoGuias":
                    Sessions.put("linkClicado", true);
                    return "emissaoGuias";
                default:
                    return null;
            }

        } else {
            return null;
        }
    }

    private float somaValoresGrid() {
        float soma = 0;
        for (int i = 0; i < list.size(); i++) {
            soma = Moeda.somaValores(soma,
                    Float.parseFloat(
                            Moeda.substituiVirgula(
                                    String.valueOf(list.get(i).getValor()))));
        }
        return soma;
    }

    public void add() {
        float soma = somaValoresGrid();
        float valorF = Float.parseFloat(Moeda.substituiVirgula(valor));
        float totalF = Float.parseFloat(Moeda.substituiVirgula(total));
        if ((Moeda.substituiVirgulaFloat(valor) != 0) && (Moeda.somaValores(soma, valorF) <= totalF)) {
            Dao dao = new Dao();
            if ((Moeda.somaValores(soma, valorF) < totalF) || (soma == 0)) {
                valorF = Moeda.subtracaoValores(totalF, Moeda.somaValores(soma, valorF));
            } else {
                valorF = 0;
            }
            TipoPagamento tipoPagamento = (TipoPagamento) dao.find(new TipoPagamento(), Integer.parseInt(((SelectItem) getListTipoPagamento().get(idTipoPagamento)).getDescription()));
            // CHEQUE
            PlanoDao planoDao = new PlanoDao();
            if (tipoPagamento.getId() == 4 || tipoPagamento.getId() == 5) {
                if (!getEs().isEmpty() && getEs().equals("S")) {
                    Plano5 p5 = planoDao.findPlano5ByContaBanco(Integer.valueOf(listBancoSaida.get(idBancoSaida).getDescription()), SessaoCliente.get().getId());

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getChequePag() != null) {
                            if (((ChequePag) list.get(i).getChequePag()).getPlano5().getId() == p5.getId()) {
                                Messages.error("Erro", "Esta CONTA já foi adicionada!");
                                return;
                            }
                        }
                        listBancoSaida.get(i).setValue(i);
                    }

                    ChequePag ch_p = new ChequePag();
                    ch_p.setCheque(numeroChequePag);
                    ch_p.setPlano5(p5);
                    ch_p.setVencimento(vencimento);

                    if (tipo.equals("caixa")) {
                        ch_p.setStatus((FStatus) (new Dao()).find(new FStatus(), 7));
                    } else {
                        ch_p.setStatus((FStatus) (new Dao()).find(new FStatus(), 8));
                    }

                    list.add(new ListMovimentoBaixaGeral(vencimento, valor, numeroChequePag, tipoPagamento, ch_p, p5));

                } else {
                    ChequeRec ch = new ChequeRec();
                    ch.setAgencia(chequeRec.getAgencia());
                    ch.setBanco(chequeRec.getBanco());
                    ch.setCheque(numero);
                    ch.setConta(chequeRec.getConta());
                    ch.setEmissao(quitacao);
                    if (tipo.equals("caixa")) {
                        ch.setStatus((FStatus) (new Dao()).find(new FStatus(), 7));
                    } else {
                        ch.setStatus((FStatus) (new Dao()).find(new FStatus(), 8));
                    }

                    ch.setVencimento(vencimento);
                    list.add(new ListMovimentoBaixaGeral(vencimento, valor, numero, tipoPagamento, ch));
                }
            } else if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
                // CARTAO
                list.add(new ListMovimentoBaixaGeral(vencimento, valor, numero, tipoPagamento, null, (Cartao) (new Dao()).find(new Cartao(), Integer.valueOf(listCartao.get(idCartao).getDescription()))));
            } else if (tipoPagamento.getId() == 8 || tipoPagamento.getId() == 9 || tipoPagamento.getId() == 10) {
                Plano5 p5 = planoDao.findPlano5ByContaBanco(Integer.valueOf(listBanco.get(idBanco).getDescription()), SessaoCliente.get().getId());
                list.add(new ListMovimentoBaixaGeral(vencimento, valor, numero, tipoPagamento, null, p5));
            } else {
                list.add(new ListMovimentoBaixaGeral(vencimento, valor, numero, tipoPagamento));
            }
            setValor(Moeda.converteR$Float(valorF));
            desHabilitaConta = true;
            desHabilitaQuitacao = true;
        }
    }

    public String remove(int index) {
        list.remove(index);
        float soma = somaValoresGrid();
        float valorF = Float.parseFloat(Moeda.substituiVirgula(valor));
        float totalF = Float.parseFloat(Moeda.substituiVirgula(total));
        if ((Moeda.somaValores(soma, valorF) < totalF) || (soma == 0)) {
            valorF = Moeda.subtracaoValores(totalF, soma);
        } else {
            valorF = 0;
        }
        setValor(Moeda.converteR$Float(valorF));

        return null;
    }

    public List<SelectItem> getListaConta() {
        List<SelectItem> conta = new ArrayList<>();
        ContaRotinaDao contaRotinaDao = new ContaRotinaDao();
        List select;
        if (verificaBaixaBoleto()) {
            select = contaRotinaDao.pesquisaContasPorRotina(1);
        } else {
            select = contaRotinaDao.pesquisaContasPorRotina();
        }
        for (int i = 0; i < select.size(); i++) {
            conta.add(new SelectItem(
                    i,
                    (String) ((Plano5) select.get(i)).getConta(),
                    Integer.toString(((Plano5) select.get(i)).getId())));
        }
        return conta;
    }

    public List<SelectItem> getListTipoPagamento() {
        Dao dao = new Dao();
        List<SelectItem> selectItem = new ArrayList<>();
        List<TipoPagamento> listTipoPagamento;
        if (!getEs().isEmpty() && getEs().equals("S")) {
            listTipoPagamento = dao.find("TipoPagamento", new int[]{3, 4, 5, 8, 9, 10});
        } else {
            listTipoPagamento = dao.find("TipoPagamento", new int[]{3, 4, 5, 6, 7, 8, 9, 10, 11});
        }
        for (int i = 0; i < listTipoPagamento.size(); i++) {
            selectItem.add(
                    new SelectItem(
                            i, listTipoPagamento.get(i).getDescricao(),
                            Integer.toString((listTipoPagamento.get(i).getId())
                            )
                    )
            );
        }
        return selectItem;
    }

    private Rotina getRotina() {
        if (rotina == null) {
            HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String urlDestino = paginaRequerida.getRequestURI();
            RotinaDao rotinaDao = new RotinaDao();
            rotina = rotinaDao.pesquisaRotinaPermissao(urlDestino);
        }
        return rotina;
    }

    public String baixar() {
        if (list.isEmpty()) {
            return mensagem = "Lista esta vazia!";
        }
        MacFilial macFilial = (MacFilial) Sessions.getObject("acessoFilial");
        Caixa caixa = null;
        if (macFilial == null) {
            return mensagem = "Não existe filial na sessão!";
        }

        if (tipo.equals("caixa")) {
            if (macFilial.getCaixa() == null) {
                return mensagem = "Não é possivel salvar baixa sem um caixa definido!";
            }

            caixa = macFilial.getCaixa();
        }

        Filial filial;
        Departamento departamento = new Departamento();

        try {
            filial = macFilial.getFilial();
            departamento = macFilial.getDepartamento();
        } catch (Exception e) {
            return mensagem = "Não é foi possível encontrar a filial no sistema!";
        }

        if (Moeda.converteUS$(valor) > 0) {
            return mensagem = "Complete as parcelas para que o Valor seja zerado!";
        } else if (Moeda.converteUS$(valor) < 0) {
            return mensagem = "Erro com o campo valor!";

        }

        PlanoDao planoDao = new PlanoDao();
        Usuario usuario = (Usuario) Sessions.getObject("sessaoUsuario");

        if (verificaBaixaBoleto()) {
            Dao dao = new Dao();
            plano5 = (Plano5) dao.find(new Plano5(), Integer.parseInt(((SelectItem) getListaConta().get(getIdConta())).getDescription()));
        } else {
            // PEGAR PLANO 5 VINCULADO AO FIN_BOLETO > FIN_CONTA_COBRANCA
            // CASO NÃO TENHA FIN_BOLETO return mensagem = "Não existe conta banco para baixar este boleto!";
            BoletoDao boletoDao = new BoletoDao();
            Boleto bol = boletoDao.findBoletosByNrCtrBoleto(listMovimentos.get(0).getNrCtrBoleto());
            if (bol == null) {
                return mensagem = "Não existe conta banco para baixar este boleto!";
            }
            plano5 = planoDao.findPlano5ByContaBanco(bol.getContaCobranca().getContaBanco().getId(), SessaoCliente.get().getId());
        }

        if (DataHoje.converte(quitacao) == null) {
            quitacao = DataHoje.data();
        }

        for (int i = 0; i < list.size(); i++) {
            // CHEQUE
            if (((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 4 || ((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 5) {
                if (!getEs().isEmpty() && getEs().equals("S")) {
                    lfp.add(new FormaPagamento(-1, null, null, (ChequePag) list.get(i).getChequePag(), 0, list.get(i).getValor(), filial, (Plano5) list.get(i).getPlano5(), null, null, (TipoPagamento) list.get(i).getTipoPagamento(), 0, null, 0));
                } else {
                    lfp.add(new FormaPagamento(-1, null, (ChequeRec) list.get(i).getChequeRec(), null, 0, list.get(i).getValor(), filial, plano5, null, null, (TipoPagamento) list.get(i).getTipoPagamento(), 0, null, 0));
                }
            } else if (((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 6 || ((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 7) {
                // CARTAO    
                Cartao cartao = ((Cartao) list.get(i).getCartao());
                DataHoje dh = new DataHoje();
                lfp.add(new FormaPagamento(-1, null, null, null, 0, list.get(i).getValor(), filial, cartao.getPlano5(), null, null, (TipoPagamento) list.get(i).getTipoPagamento(), 0, dh.converte(dh.incrementarDias(cartao.getDias(), quitacao)), Moeda.divisaoValores(Moeda.multiplicarValores(list.get(i).getValor(), cartao.getTaxa()), 100)));
            } else if (((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 8 || ((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 9 || ((TipoPagamento) list.get(i).getTipoPagamento()).getId() == 10) {
                // DOC BANCARIO    
                lfp.add(new FormaPagamento(-1, null, null, null, 0, list.get(i).getValor(), filial, (Plano5) list.get(i).getPlano5(), null, null, (TipoPagamento) list.get(i).getTipoPagamento(), 0, DataHoje.dataHoje(), 0));
            } else {
                // DINHEIRO E OUTROS
                lfp.add(new FormaPagamento(-1, null, null, null, 0, list.get(i).getValor(), filial, plano5, null, null, (TipoPagamento) list.get(i).getTipoPagamento(), 0, null, 0));
            }
        }

        for (int i = 0; i < listMovimentos.size(); i++) {
            listMovimentos.get(i).setTaxa(Moeda.converteUS$(taxa));
        }
//
//        if (!false) {
//            mensagem = "Erro ao atualizar boleto!";
//            return null;
        if (!GerarMovimento.baixarMovimentoManual(listMovimentos, usuario, lfp, Moeda.substituiVirgulaFloat(total), quitacao, caixa)) {
            mensagem = "Erro ao atualizar boleto!";
            return null;
        } else {
            list.clear();
            total = "0.0";
            String url = Sessions.getString("urlRetorno");
            switch (url) {
                case "baixaBoleto":
                    break;
                case "movimentosReceber":
                    ((MovimentosReceberBean) Sessions.getObject("movimentosReceberBean")).getListMovimento().clear();
                    break;
                case "emissaoGuias":
                case "menuPrincipal":
                    break;
                case "lancamentoFinanceiro":
                    break;
            }
            retorna = true;
            mensagem = "Baixa realizada com sucesso!";
        }
        return null;
    }

    public String getQuitacao() {
        return quitacao;
    }

    public void setQuitacao(String quitacao) {
        this.quitacao = quitacao;
    }

    public String getValor() {
        getListMovimentos();
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public List<ListMovimentoBaixaGeral> getList() {
        return list;
    }

    public void setList(List<ListMovimentoBaixaGeral> list) {
        this.list = list;
    }

    public int getIdTipoPagamento() {
        return idTipoPagamento;
    }

    public void setIdTipoPagamento(int idTipoPagamento) {
        this.idTipoPagamento = idTipoPagamento;
    }

    private void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Modulo getModulo() {
        if (modulo.getId() == -1) {
            modulo = (Modulo) new Dao().find(new Modulo(), 3);
        }
        return modulo;
    }

    private void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public boolean isDesHabilitaConta() {
        desHabilitaConta = (!list.isEmpty()) || (!verificaBaixaBoleto());
        return desHabilitaConta;
    }

    public boolean verificaBaixaBoleto() {
        String urlRetorno = Sessions.getString("urlRetorno");
        return !((urlRetorno.equals("baixaBoleto")) && (tipo.equals("banco")));

    }

    public void setDesHabilitaConta(boolean desHabilitaConta) {
        this.desHabilitaConta = desHabilitaConta;
    }

    public boolean isDesHabilitaQuitacao() {
        desHabilitaQuitacao = !tipo.equals("banco");
        return desHabilitaQuitacao;
    }

    public void setDesHabilitaQuitacao(boolean desHabilitaQuitacao) {
        this.desHabilitaQuitacao = desHabilitaQuitacao;
    }

    public boolean isDesHabilitaNumero() {
        if (!getListTipoPagamento().isEmpty()) {
            TipoPagamento tipoPagamento = (TipoPagamento) new Dao().find(new TipoPagamento(), Integer.parseInt(((SelectItem) getListTipoPagamento().get(idTipoPagamento)).getDescription()));
            if (tipoPagamento.getId() == 3 || tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7 || (!getEs().isEmpty() && getEs().equals("S"))) {
                desHabilitaNumero = true;
                numero = "";
            } else {
                desHabilitaNumero = false;
            }

        }
        return desHabilitaNumero;
    }

    public void setDesHabilitaNumero(boolean desHabilitaNumero) {
        this.desHabilitaNumero = desHabilitaNumero;
    }

    public boolean isDesHabilitadoVencimento() {
        if (!getListTipoPagamento().isEmpty()) {
            TipoPagamento tipoPagamento = (TipoPagamento) new Dao().find(new TipoPagamento(), Integer.parseInt(((SelectItem) getListTipoPagamento().get(idTipoPagamento)).getDescription()));
            if (tipoPagamento.getId() == 5) {
                desHabilitadoVencimento = false;
            } else {
                vencimento = quitacao;
                desHabilitadoVencimento = true;
            }
        }
        return desHabilitadoVencimento;
    }

    public void setDesHabilitadoVencimento(boolean desHabilitadoVencimento) {
        this.desHabilitadoVencimento = desHabilitadoVencimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public List<Movimento> getListMovimentos() {
        if (listMovimentos.isEmpty()) {
            if (Sessions.exists("listMovimentos")) {
                listMovimentos = (List) Sessions.getList("listMovimentos", true);
                float valorTotal = 0;

                if (total.equals("0.00")) {
                    for (int i = 0; i < listMovimentos.size(); i++) {
                        valorTotal = Moeda.somaValores(valorTotal, listMovimentos.get(i).getValorBaixa());
                    }
                    total = Moeda.converteR$Float(valorTotal);
                    valor = total;
                }
                if (!verificaBaixaBoleto()) {
                    // plano5 = listaMovimentos.get(0).getPlano5();
                }
            }
        }
        return listMovimentos;
    }

    public void setListMovimentos(List<Movimento> listMovimentos) {
        this.listMovimentos = listMovimentos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTipo() {
        if (Sessions.exists("caixa_banco")) {
            tipo = Sessions.getString("caixa_banco", true);
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBanco() {
        if (banco.isEmpty() && getListMovimentos().size() == 1) {
            BoletoDao boletoDao = new BoletoDao();
            ImprimirBoleto imp = new ImprimirBoleto();
            Boleto bol = boletoDao.findBoletosByNrCtrBoleto(listMovimentos.get(0).getNrCtrBoleto());

            if (bol == null) {
                listMovimentos = imp.atualizaContaCobrancaMovimento(listMovimentos);
            }

            bol = boletoDao.findBoletosByNrCtrBoleto(listMovimentos.get(0).getNrCtrBoleto());
            banco = bol.getContaCobranca().getContaBanco().getConta() + " / " + bol.getContaCobranca().getContaBanco().getBanco().getBanco();
        }
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public ChequeRec getChequeRec() {
        return chequeRec;
    }

    public void setChequeRec(ChequeRec chequeRec) {
        this.chequeRec = chequeRec;
    }

    public String getTaxa() {
        return Moeda.converteR$(taxa);
    }

    public void setTaxa(String taxa) {
        this.taxa = Moeda.substituiVirgula(taxa);
    }

    public boolean isRetorna() {
        return retorna;
    }

    public void setRetorna(boolean retorna) {
        this.retorna = retorna;
    }

    public void alteraVencimento() {
        int quitacaoInteiro = DataHoje.converteDataParaRefInteger(quitacao);
        int vencimentoInteiro = DataHoje.converteDataParaRefInteger(vencimento);
        if (quitacaoInteiro != vencimentoInteiro) {
            vencimento = quitacao;
        }
    }

    public List<SelectItem> getListCartao() {
        if (listCartao.isEmpty()) {
            if (!getListTipoPagamento().isEmpty()) {
                Dao dao = new Dao();
                List<Cartao> listCartoes = dao.list(new Cartao());
                TipoPagamento tipoPagamento = (TipoPagamento) (new Dao()).find(new TipoPagamento(), Integer.parseInt(((SelectItem) getListTipoPagamento().get(idTipoPagamento)).getDescription()));
                int conta = 0;
                for (Cartao c : listCartoes) {
                    String tipoString = c.getDebitoCredito().equals("D") ? "Débito" : "Crédito";
                    if (tipoPagamento.getId() == 6 && c.getDebitoCredito().equals("C")) {
                        listCartao.add(new SelectItem(conta, c.getDescricao() + " - " + tipoString, Integer.toString(c.getId())));
                        conta++;
                    } else if (tipoPagamento.getId() == 7 && c.getDebitoCredito().equals("D")) {
                        listCartao.add(new SelectItem(conta, c.getDescricao() + " - " + tipoString, Integer.toString(c.getId())));
                        conta++;
                    }
                }
            }
        }
        return listCartao;
    }

    public void setListCartao(List<SelectItem> listCartao) {
        this.listCartao = listCartao;
    }

    public int getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(int idCartao) {
        this.idCartao = idCartao;
    }

    public List<SelectItem> getListBanco() {
        if (listBanco.isEmpty()) {
            List<ContaBanco> listContaBanco = new ContaBancoDao().findAllByCliente(SessaoCliente.get().getId());
            for (int i = 0; i < listContaBanco.size(); i++) {
                listBanco.add(new SelectItem(i, listContaBanco.get(i).getAgencia() + " " + listContaBanco.get(i).getConta() + " - " + listContaBanco.get(i).getBanco().getBanco(), Integer.toString(listContaBanco.get(i).getId())));
            }
        }
        return listBanco;
    }

    public void setListBanco(List<SelectItem> listBanco) {
        this.listBanco = listBanco;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getEs() {
        if (Sessions.exists("esMovimento")) {
            es = String.valueOf(Sessions.getString("esMovimento", true));
        }
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getNumeroChequePag() {
        return numeroChequePag;
    }

    public void setNumeroChequePag(String numeroChequePag) {
        this.numeroChequePag = numeroChequePag;
    }

    public List<SelectItem> getListBancoSaida() {
        if (listBancoSaida.isEmpty()) {
            List<ContaBanco> listContasbanco = new ContaBancoDao().findAllByCliente(SessaoCliente.get().getId());
            for (int i = 0; i < listContasbanco.size(); i++) {
                listBancoSaida.add(new SelectItem(i, listContasbanco.get(i).getAgencia() + " " + listContasbanco.get(i).getConta() + " - " + listContasbanco.get(i).getBanco().getBanco(), Integer.toString(listContasbanco.get(i).getId())));
            }
        }
        if (!getEs().isEmpty() && getEs().equals("S")) {
            Dao dao = new Dao();
            ContaBanco cb = (ContaBanco) dao.find(new ContaBanco(), Integer.valueOf(listBancoSaida.get(idBancoSaida).getDescription()));
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
        }
        return listBancoSaida;
    }

    public void setListBancoSaida(List<SelectItem> listBancoSaida) {
        this.listBancoSaida = listBancoSaida;
    }

    public int getIdBancoSaida() {
        return idBancoSaida;
    }

    public void setIdBancoSaida(int idBancoSaida) {
        this.idBancoSaida = idBancoSaida;
    }
}
