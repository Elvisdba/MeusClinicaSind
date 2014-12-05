package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.dao.MovimentosReceberDao;
import br.com.clinicaintegrada.financeiro.Baixa;
import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.financeiro.FormaPagamento;
//import br.com.clinicaintegrada.financeiro.FormaPagamento;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.dao.FormaPagamentoDao;
//import br.com.clinicaintegrada.financeiro.dao.FormaPagamentoDao;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
import br.com.clinicaintegrada.financeiro.dao.ServicoContaCobrancaDao;
import br.com.clinicaintegrada.financeiro.list.ListMovimentoReceber;
import br.com.clinicaintegrada.impressao.ParametroRecibo;
import br.com.clinicaintegrada.movimento.GerarMovimento;
import br.com.clinicaintegrada.pessoa.Juridica;
//import br.com.clinicaintegrada.impressao.ParametroRecibo;
//import br.com.clinicaintegrada.movimento.GerarMovimento;
//import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.PessoaEndereco;
import br.com.clinicaintegrada.pessoa.dao.PessoaDao;
import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
//import br.com.clinicaintegrada.pessoa.PessoaEndereco;
//import br.com.clinicaintegrada.pessoa.dao.PessoaEnderecoDao;
import br.com.clinicaintegrada.seguranca.MacFilial;
import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
// import br.com.clinicaintegrada.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.utils.AnaliseString;
// import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.DataObject;
import br.com.clinicaintegrada.utils.Dirs;
// import br.com.clinicaintegrada.utils.Download;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
// import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.Collection;
import java.util.Date;
// import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
// import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
//import javax.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
//import net.sf.jasperreports.engine.util.JRLoader;
//import net.sf.jasperreports.engine.export.JRPdfExporter;
//import net.sf.jasperreports.export.SimpleExporterInput;
//import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
//import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@ManagedBean
@SessionScoped
public class MovimentosReceberBean {

    private String porPesquisa;
    private List<ListMovimentoReceber> listMovimento;
    private String titular;
    private String beneficiario;
    private String data;
    private String boleto;
    private String diasAtraso;
    private String multa, juros, correcao;
    private String caixa;
    private String documento;
    private String referencia;
    private String tipo;
    private String id_baixa;
    private String desconto;
    private boolean chkSeleciona;
    private boolean addMais;
    private Pessoa pessoa;
    private List<Pessoa> listPessoa;
    private String status;
    private String descPesquisaBoleto;
    private List<SelectItem> listContas;
    private int indexConta;

    @PostConstruct
    public void init() {
        porPesquisa = "abertos";
        listMovimento = new ArrayList();
        titular = "";
        beneficiario = "";
        data = "";
        boleto = "";
        diasAtraso = "";
        multa = "";
        juros = "";
        correcao = "";
        caixa = "";
        documento = "";
        referencia = "";
        tipo = "";
        id_baixa = "";
        desconto = "0";
        chkSeleciona = false;
        addMais = false;
        pessoa = new Pessoa();
        listPessoa = new ArrayList();
        status = "";
        descPesquisaBoleto = "";
        listContas = new ArrayList();
        indexConta = 0;
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("movimentosReceberBean");
        Sessions.remove("pessoaPesquisa");
        Sessions.remove("listMovimento");
    }

    public List<SelectItem> getListContas() {
        if (listContas.isEmpty()) {
            ServicoContaCobrancaDao servDB = new ServicoContaCobrancaDao();
            List<ContaCobranca> result = servDB.listContaCobrancaAtivoAssociativo();
            if (result.isEmpty()) {
                listContas.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
                return listContas;
            }
            int contador = 0;
            for (int i = 0; i < result.size(); i++) {
                // LAYOUT 2 = SINDICAL
                if (result.get(i).getLayout().getId() != 2) {
                    listContas.add(
                            new SelectItem(
                                    contador,
                                    result.get(i).getApelido() + " - " + result.get(i).getCodCedente(), // CODCEDENTE NO CASO DE OUTRAS
                                    Integer.toString(result.get(i).getId())
                            )
                    );
                    contador++;
                }
            }
//            if (!listaContas.isEmpty()) {
//                contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
//            }
        }
        return listContas;
    }

    public void pesquisaBoleto() {
        if (descPesquisaBoleto.isEmpty()) {
            if (pessoa.getId() != -1) {
                porPesquisa = "todos";
                listMovimento.clear();
                getListMovimento();
            }
            return;
        }
        try {
            PessoaDao pessoaDao = new PessoaDao();
            ContaCobranca contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listContas.get(indexConta)).getDescription()));
            Pessoa p = pessoaDao.findPessoaByBoletoAndContaCobranca(descPesquisaBoleto, contaCobranca.getId());
            listPessoa.clear();
            pessoa = new Pessoa();

            if (p != null) {
                pessoa = p;
                listPessoa.add(p);
            }
            porPesquisa = "todos";
            listMovimento.clear();
            getListMovimento();
        } catch (Exception e) {
            descPesquisaBoleto = "";
            Messages.fatal("Atenção", "Digite um número de Boleto válido!");
        }
    }

    public void salvarRecibo(byte[] arquivo, Baixa baixa) {
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

    public String recibo(String idMovimento) {
//
        MovimentoDao movimentoDao = new MovimentoDao();
        FormaPagamentoDao formaPagamentoDao = new FormaPagamentoDao();
        Movimento movimento = new Movimento();
        Dao dao = new Dao();
        movimento = (Movimento) dao.find(new Movimento(), Integer.parseInt(idMovimento));
        try {
            Collection collection = new ArrayList();
            Juridica cliente = (Juridica) (new Dao()).find(new Juridica(), 1);
            PessoaEnderecoDao dbp = new PessoaEnderecoDao();

            PessoaEndereco pe = dbp.pesquisaPessoaEnderecoPorPessoaTipo(1, 2);
            String formas[] = new String[10];

            // PESQUISA FORMA DE PAGAMENTO
            List<FormaPagamento> fp = formaPagamentoDao.findFormaPagamentoByBaixa(movimento.getBaixa().getId());

            for (int i = 0; i < fp.size(); i++) {
                // 4 - CHEQUE    
                if (fp.get(i).getTipoPagamento().getId() == 4) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque();
                    // 5 - CHEQUE PRÉ
                } else if (fp.get(i).getTipoPagamento().getId() == 5) {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor()) + " (B: " + fp.get(i).getChequeRec().getBanco() + " Ag: " + fp.get(i).getChequeRec().getAgencia() + " C: " + fp.get(i).getChequeRec().getConta() + " CH: " + fp.get(i).getChequeRec().getCheque() + " P: " + fp.get(i).getChequeRec().getVencimento() + ")";
                    // QUALQUER OUTRO    
                } else {
                    formas[i] = fp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(fp.get(i).getValor());
                }
            }

            List<Movimento> lista = movimentoDao.listMovimentosByBaixaOrderByBaixa(movimento.getBaixa().getId());
            for (int i = 0; i < lista.size(); i++) {

                collection.add(new ParametroRecibo(
                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                        cliente.getPessoa().getNome(),
                        pe.getEndereco().getDescricaoEndereco().getDescricao(),
                        pe.getEndereco().getLogradouro().getDescricao(),
                        pe.getNumero(),
                        pe.getComplemento(),
                        pe.getEndereco().getBairro().getDescricao(),
                        pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
                        pe.getEndereco().getCidade().getCidade(),
                        pe.getEndereco().getCidade().getUf(),
                        cliente.getPessoa().getTelefone1(),
                        cliente.getPessoa().getEmail1(),
                        cliente.getPessoa().getSite(),
                        cliente.getPessoa().getDocumento(),
                        lista.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
                        String.valueOf(lista.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
                        String.valueOf(lista.get(i).getBaixa().getId()), // ID_BAIXA
                        lista.get(i).getServicos().getDescricao(), // SERVICO
                        lista.get(i).getVencimentoString(), // VENCIMENTO
                        new BigDecimal(lista.get(i).getValorBaixa()), // VALOR BAIXA
                        lista.get(i).getBaixa().getUsuario().getLogin(),
                        lista.get(i).getBaixa().getBaixa(),
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
                        "" // (conveniada.isEmpty()) ? "" : "Empresa Conveniada: " + conveniada
                )
                );
            }

            File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));
            JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(collection);
            JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);

            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
            salvarRecibo(arquivo, lista.get(0).getBaixa());

            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();

            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException | IOException ex) {
            Logger.getLogger(MovimentosReceberBean.class.getName()).log(Level.SEVERE, null, ex);
        }
//
        return null;
    }

    public String removerPesquisa() {
        Sessions.remove("pessoaPesquisa");
        pessoa = new Pessoa();
        return "movimentosReceber";
    }

    public String removerPessoaLista(int index) {
        listPessoa.remove(index);
        listMovimento.clear();
        return "movimentosReceber";
    }

    public boolean baixado() {
        for (int i = 0; i < listMovimento.size(); i++) {
            if ((listMovimento.get(i).getSelected()) && Moeda.converteUS$(listMovimento.get(i).getBaixa_valor()) > 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean semValor() {
        for (int i = 0; i < listMovimento.size(); i++) {
            if (listMovimento.get(i).getSelected() && Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado()) <= 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean acordados() {
        for (int i = 0; i < listMovimento.size(); i++) {
            if (listMovimento.get(i).getSelected() && String.valueOf(listMovimento.get(i).getTipo_servico_descricao()).equals("Acordo")) {
                return true;
            }
        }
        return false;
    }

    public String estornarBaixa() {
        if (listMovimento.isEmpty()) {
            Messages.warn("Erro", "Não existem boletos para serem estornados!");
            return null;
        }

        MovimentoDao db = new MovimentoDao();
        int qnt = 0;
        Movimento mov = null;
        Dao dao = new Dao();
        for (int i = 0; i < listMovimento.size(); i++) {
            if (listMovimento.get(i).getSelected()) {
                qnt++;
                mov = (Movimento) dao.find(new Movimento(), Integer.parseInt(String.valueOf(listMovimento.get(i).getMovimento_id())));
            }
        }

        if (qnt == 0) {
            Messages.warn("Erro", "Nenhum Movimento selecionado!");
            return null;
        }

        if (qnt > 1) {
            Messages.warn("Erro", "Mais de um movimento foi selecionado!");
            return null;
        }

        if (!baixado()) {
            Messages.warn("Erro", "Existem boletos que não foram pagos para estornar!");
            return null;
        }

        boolean est = true;

        if (!mov.isAtivo()) {
            Messages.warn("Erro", "Boleto ID: " + mov.getId() + " esta inativo, não é possivel concluir estorno!");
            return null;
        }
        if (!GerarMovimento.estornarMovimento(mov)) {
            est = false;
        }
        if (!est) {
            Messages.warn("Erro", "Ocorreu erros ao estornar boletos, verifique o log!");
        } else {
            Messages.info("Sucesso", "Boletos estornados com sucesso!");
        }
        listMovimento.clear();
        chkSeleciona = true;
        return null;
    }

    public String telaBaixa() throws IOException {
        List lista = new ArrayList();
        MovimentoDao db = new MovimentoDao();
        Movimento movimento = new Movimento();
        MacFilial macFilial = (MacFilial) Sessions.getObject("acessoFilial");

        if (macFilial == null) {
            Messages.warn("Erro", "Não existe filial na sessão!");
            return null;
        }

        if (macFilial.getCaixa() == null) {
            Messages.warn("Erro", "Configurar Caixa nesta estação de trabalho!");
            return null;
        }

        if (baixado()) {
            Messages.warn("Erro", "Existem boletos baixados na lista!");
            return null;
        }

        if (semValor()) {
            Messages.warn("Erro", "Boletos sem valor não podem ser Baixados!");
            return null;
        }
        Dao dao = new Dao();
        if (!listMovimento.isEmpty()) {
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected()) {
                    movimento = (Movimento) dao.find(new Movimento(), Integer.parseInt(String.valueOf(listMovimento.get(i).getMovimento_id())));
                    movimento.setMulta(Moeda.converteUS$(listMovimento.get(i).getMovimento_multa()));
                    movimento.setJuros(Moeda.converteUS$(listMovimento.get(i).getMovimento_juros()));
                    movimento.setCorrecao(Moeda.converteUS$(listMovimento.get(i).getMovimento_correcao()));
                    movimento.setDesconto(Moeda.converteUS$(listMovimento.get(i).getMovimento_desconto()));
                    movimento.setValor(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor()));
                    movimento.setValorBaixa(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                Sessions.put("listMovimentos", lista);
                return ((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).pagina("baixaGeral");
            } else {
                Messages.warn("Erro", "Nenhum boleto foi selecionado");
            }
        } else {
            Messages.warn("Erro", "Lista vazia!");
        }
        return null;
    }

    public String telaMovimento(String idMovimento) throws IOException {
        List lista = new ArrayList();
        Dao dao = new Dao();
        Movimento movimento = new Movimento();
        movimento = (Movimento) dao.find(new Movimento(), Integer.parseInt(idMovimento));

//                    movimento.setMulta(Moeda.converteUS$(listMovimento.get(i).getArgumento19().toString()));
//                    movimento.setJuros(Moeda.converteUS$( listMovimento.get(i).getArgumento20().toString()));
//                    movimento.setCorrecao(Moeda.converteUS$( listMovimento.get(i).getArgumento21().toString()));
//
//                    movimento.setDesconto(Moeda.converteUS$(listMovimento.get(i).getArgumento8().toString()));
//
//                    movimento.setValor(Moeda.converteUS$(listMovimento.get(i).getArgumento6().toString()));
        // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//                    movimento.setValorBaixa(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado().toString()));
        lista.add(movimento);
        Sessions.put("listMovimento", lista);
        return ((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).pagina("alterarMovimento");
    }

    public String telaAcordo() throws IOException {
        List lista = new ArrayList();
        MovimentoDao db = new MovimentoDao();
        Movimento movimento = new Movimento();
        if (baixado()) {
            Messages.warn("Erro", "Existem boletos baixados na lista!");
            return null;
        }

        if (semValor()) {
            Messages.warn("Erro", "Boletos sem valor não podem ser Acordados!");
            return null;
        }

        if (acordados()) {
            Messages.warn("Erro", "Boletos do tipo Acordo não podem ser Reacordados!");
            return null;
        }
        Dao dao = new Dao();
        if (!listMovimento.isEmpty()) {
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected()) {
                    movimento = (Movimento) dao.find(new Movimento(), Integer.parseInt(String.valueOf(listMovimento.get(i).getMovimento_id())));
                    movimento.setMulta(Moeda.converteUS$(listMovimento.get(i).getMovimento_multa()));
                    movimento.setJuros(Moeda.converteUS$(listMovimento.get(i).getMovimento_juros()));
                    movimento.setCorrecao(Moeda.converteUS$(listMovimento.get(i).getMovimento_correcao()));
                    movimento.setDesconto(Moeda.converteUS$(listMovimento.get(i).getMovimento_desconto()));
                    movimento.setValor(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor()));
                    movimento.setValorBaixa(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                Sessions.put("listMovimento", lista);
                return ((ChamadaPaginaBean) Sessions.getObject("chamadaPaginaBean")).pagina("acordo");
            } else {
                Messages.warn("Erro", "Nenhum boleto foi selecionado");
            }
        } else {
            Messages.warn("Erro", "Lista vazia!");
        }
        return null;
    }

    public void calculoDesconto() {
        float descPorcento = 0;
        float desc = 0;
        float acre = 0;
        float calc = Moeda.substituiVirgulaFloat(getValorPraDesconto());
        if (Float.valueOf(desconto) > calc) {
            desconto = String.valueOf(calc);
        }
        if (Float.valueOf(desconto) > 0) {
            descPorcento = Moeda.multiplicarValores(Moeda.divisaoValores(Float.valueOf(desconto), calc), 100);
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected() && Moeda.converteUS$(listMovimento.get(i).getBaixa_valor()) == 0.0) {
                    acre = Moeda.converteUS$(listMovimento.get(i).getMovimento_acrescimo());

                    float valor_calc = Moeda.somaValores(Moeda.converteUS$(listMovimento.get(i).getMovimento_valor()), acre);
                    desc = Moeda.divisaoValores(Moeda.multiplicarValores(valor_calc, descPorcento), 100);

                    listMovimento.get(i).setMovimento_desconto(Moeda.converteR$(String.valueOf(desc)));
                    listMovimento.get(i).setMovimento_valor_calculado(Moeda.converteR$(String.valueOf(Moeda.subtracaoValores(valor_calc, desc))));
                } else {
                    listMovimento.get(i).setMovimento_desconto("0,00");
                    listMovimento.get(i).setMovimento_valor_calculado(Moeda.converteR$(listMovimento.get(i).getMovimento_valor()));
                }
            }
        }
    }

    public void atualizarStatus() {
        listMovimento.clear();
    }

    public String getTotal() {
        if (!listMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected() && Moeda.converteUS$(listMovimento.get(i).getBaixa_valor()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listMovimento.get(i).getMovimento_valor()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getAcrescimo() {
        if (!listMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listMovimento.get(i).getMovimento_acrescimo()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getValorPraDesconto() {
        if (!listMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected() && Moeda.converteUS$(listMovimento.get(i).getBaixa_valor()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getTotalCalculado() {
        if (!listMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listMovimento.size(); i++) {
                if (listMovimento.get(i).getSelected() && Moeda.converteUS$(listMovimento.get(i).getBaixa_valor()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public void complementoPessoa(ListMovimentoReceber lmr) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        //titular = (String) linha.getArgumento15(); // 13 - TITULAR
        tipo = (String) lmr.getTipo_servico_descricao(); // 1 - TIPO SERVIÇO
        referencia = (String) lmr.getMovimento_referencia(); // 2 - REFERENCIA
        id_baixa = lmr.getMovimento_baixa(); // 23 - ID_BAIXA

        // beneficiario = (String) lmr.getArgumento14(); // 12 - BENEFICIARIO
        data = lmr.getLote_lancamento(); // 16 - CRIACAO
        boleto = (String) lmr.getMovimento_documento(); // 17 - BOLETO
        diasAtraso = lmr.getMovimento_dias(); // 18 - DIAS EM ATRASO
        multa = "R$ " + Moeda.converteR$(lmr.getMovimento_multa()); // 19 - MULTA
        juros = "R$ " + Moeda.converteR$(lmr.getMovimento_juros()); // 20 - JUROS
        correcao = "R$ " + Moeda.converteR$(lmr.getMovimento_correcao()); // 21 - CORRECAO
        caixa = (lmr.getUsuario_caixa() == null) ? "Nenhum" : lmr.getUsuario_caixa(); // 22 - CAIXA
        documento = (lmr.getMovimento_documento() == null) ? "Sem Documento" : lmr.getLote_documento(); // 24 - DOCUMENTO
        int id_lote = Integer.valueOf(lmr.getLote_id());
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public void marcarTodos() {
        for (int i = 0; i < listMovimento.size(); i++) {
            listMovimento.get(i).setSelected(chkSeleciona);
        }
    }

    public List<ListMovimentoReceber> getListMovimento() {
        if (listMovimento.isEmpty() && !listPessoa.isEmpty()) {
            MovimentosReceberDao movimentosReceberDao = new MovimentosReceberDao();
            String ids = "";
            for (int i = 0; i < listPessoa.size(); i++) {
                if (ids.length() > 0 && i != listPessoa.size()) {
                    ids = ids + ",";
                }
                ids = ids + String.valueOf(listPessoa.get(i).getId());
            }
            List lista = movimentosReceberDao.listMovimentos(ids, porPesquisa);
            //float soma = 0;
            boolean chk = false, disabled = false;
            String dataBaixa = "";
            String vencimento = "";

            for (int i = 0; i < lista.size(); i++) {
                if (((List) lista.get(i)).get(8) != null) {
                    dataBaixa = DataHoje.converteData((Date) ((List) lista.get(i)).get(8));
                } else {
                    dataBaixa = "";
                }
                //soma = Moeda.somaValores(Moeda.converteR$(lista.get(i).get(5).toString()), Moeda.converteUS$(listMovimento.get(i).getMovimento_valor_calculado().toString()));
                // DATA DE HOJE MENOR OU IGUAL A DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date) ((List) lista.get(i)).get(3)))
                        <= DataHoje.converteDataParaInteger(DataHoje.data())
                        && dataBaixa.isEmpty()) {
                    chk = true;
                } else {
                    chk = false;
                }

                // DATA DE HOJE MENOR QUE DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date) ((List) lista.get(i)).get(3)))
                        < DataHoje.converteDataParaInteger(DataHoje.data()) && dataBaixa.isEmpty()) {
                    disabled = true;
                } else {
                    disabled = false;
                }
                if (((List) lista.get(i)).get(3) != null) {
                    vencimento = DataHoje.converteData((Date) ((List) lista.get(i)).get(3));
                } else {
                    vencimento = "";
                }
                listMovimento.add(
                        new ListMovimentoReceber(
                                /* selected     */chk,
                                /* serviço      */ AnaliseString.converteNullString(((List) lista.get(i)).get(0).toString()),
                                /* tipo serviço */ AnaliseString.converteNullString(((List) lista.get(i)).get(1).toString()),
                                /* referência   */ AnaliseString.converteNullString(((List) lista.get(i)).get(2).toString()),
                                /* vencto       */ vencimento,
                                /* valor        */ Moeda.converteR$(AnaliseString.converteNullString(((List) lista.get(i)).get(4))),
                                /* acrescimo    */ Moeda.converteR$(AnaliseString.converteNullString(((List) lista.get(i)).get(5))),
                                /* desconto     */ Moeda.converteR$(AnaliseString.converteNullString(((List) lista.get(i)).get(6))),
                                /* v. calculado */ Moeda.converteR$(AnaliseString.converteNullString(((List) lista.get(i)).get(7))),
                                /* baixa data   */ dataBaixa,
                                /* baixa valor  */ Moeda.converteR$(AnaliseString.converteNullString(((List) lista.get(i)).get(9))),
                                /* es           */ AnaliseString.converteNullString(((List) lista.get(i)).get(10)),
                                /* pessoa nome  */ AnaliseString.converteNullString(((List) lista.get(i)).get(11)),
                                /* pessoa id    */ AnaliseString.converteNullString(((List) lista.get(i)).get(12)),
                                /* movimento id */ AnaliseString.converteNullString(((List) lista.get(i)).get(13)),
                                /* lote id      */ AnaliseString.converteNullString(((List) lista.get(i)).get(14)),
                                /* l. lancamento*/ AnaliseString.converteNullString(((List) lista.get(i)).get(15)),
                                /* m. documento */ AnaliseString.converteNullString(((List) lista.get(i)).get(16)),
                                /* dias         */ AnaliseString.converteNullString(((List) lista.get(i)).get(17)),
                                /* multa        */ AnaliseString.converteNullString(((List) lista.get(i)).get(18)),
                                /* juros        */ AnaliseString.converteNullString(((List) lista.get(i)).get(19)),
                                /* correcao     */ AnaliseString.converteNullString(((List) lista.get(i)).get(20)),
                                /* usuário caixa*/ AnaliseString.converteNullString(((List) lista.get(i)).get(21)),
                                /* m. baixa     */ AnaliseString.converteNullString(((List) lista.get(i)).get(22)),
                                /* l. documento */ AnaliseString.converteNullString(((List) lista.get(i)).get(23)),
                                /* css          */ (!descPesquisaBoleto.isEmpty() && descPesquisaBoleto.equals(AnaliseString.converteNullString(((List) lista.get(i)).get(17)))) ? "tblListaBoleto" : "", // BOLETO PESQUISADO -- ARG 28
                                /* disabled     */ disabled
                        )
                );
//                listMovimento.add(
//                        new ListMovimentoReceber(
//                        chk, // ARG 0
//                        -- AnaliseString.converteNullString(lista.get(i).get(0)),                          // ARG 2 SERVICO
//                        -- AnaliseString.converteNullString(lista.get(i).get(1)),                          // ARG 3 TIPO_SERVICO
//                        -- AnaliseString.converteNullString(lista.get(i).get(2)),                          // ARG 4 REFERENCIA
//                        -- DataHoje.converteData((Date) lista.get(i).get(3)),                              // ARG 5 VENCIMENTO
//                        --  Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(4))),        // ARG 6 VALOR
//                        -- Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(5))),        // ARG 7 ACRESCIMO
//                        -- Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(6))),        // ARG 8 DESCONTO
//                        -- Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(7))),        // ARG 9 VALOR CALCULADO
//                        -- dataBaixa,                                                                      // ARG 10 DATA BAIXA
//                        -- Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(9))),        // ARG 11 VALOR_BAIXA
//                        -- AnaliseString.converteNullString(lista.get(i).get(10)),                         // ARG 12 ES
//                        -- AnaliseString.converteNullString(lista.get(i).get(11)),                         // ARG 13 RESPONSAVEL
//                        ?? AnaliseString.converteNullString(lista.get(i).get(12)),                         // ARG 14 BENEFICIARIO
//                        -- AnaliseString.converteNullString(lista.get(i).get(14)),                         // ARG 1 ID_MOVIMENTO
//                        AnaliseString.converteNullString(lista.get(i).get(13)),                         // ARG 15 TITULAR
//                        DataHoje.converteData((Date) lista.get(i).get(15)),                             // ARG 16 CRIACAO
//                        AnaliseString.converteNullString(lista.get(i).get(17)),                         // ARG 17 BOLETO
//                        AnaliseString.converteNullString(lista.get(i).get(18)),                         // ARG 18 DIAS DE ATRASO
//                        Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(19))),       // ARG 29 MULTA
//                        Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(20))),       // ARG 20 JUROS
//                        Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(21))),       // ARG 21 CORRECAO
//                        AnaliseString.converteNullString(lista.get(i).get(22)),                         // ARG 22 CAIXA
//                        AnaliseString.converteNullString(lista.get(i).get(23)), // ARG 23 DOCUMENTO
//                        Moeda.converteR$(AnaliseString.converteNullString(lista.get(i).get(7))), // ARG 24 VALOR CALCULADO ORIGINAL
//                        //lista.get(i).get(18), // ARG 25 ID_BAIXA
//                        AnaliseString.converteNullString(lista.get(i).get(22)), // ARG 26 ID_BAIXA
//                        AnaliseString.converteNullString(lista.get(i).get(14)), // ARG 27 ID_LOTE
//                        (!descPesquisaBoleto.isEmpty() && descPesquisaBoleto.equals(lista.get(i).get(17))) ? "tblListaBoleto" : "", // BOLETO PESQUISADO -- ARG 28
//                        disabled
//                )
//                );
            }
        }
        return listMovimento;
    }

    public void setListaMovimento(List<ListMovimentoReceber> listMovimento) {
        this.listMovimento = listMovimento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        this.desconto = Moeda.substituiVirgula(desconto);
    }

    public boolean isChkSeleciona() {
        return chkSeleciona;
    }

    public void setChkSeleciona(boolean chkSeleciona) {
        this.chkSeleciona = chkSeleciona;
    }

    public void adicionarPesquisa() {
        addMais = true;
        //return "movimentosReceberSocial";
    }

    public Pessoa getPessoa() {
        if (Sessions.exists("pessoaPesquisa")) {
            if (!addMais) {
                pessoa = new Pessoa();
                pessoa = (Pessoa) Sessions.getObject("pessoaPesquisa", true);

                listPessoa.clear();

                listPessoa.add(pessoa);
                listMovimento.clear();
            } else {
                listPessoa.add((Pessoa) Sessions.getObject("pessoaPesquisa", true));
                listMovimento.clear();
                addMais = false;
            }
            calculoDesconto();
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListPessoa() {
        return listPessoa;
    }

    public void setListPessoa(List<Pessoa> listPessoa) {
        this.listPessoa = listPessoa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId_baixa() {
        return id_baixa;
    }

    public void setId_baixa(String id_baixa) {
        this.id_baixa = id_baixa;
    }

    public String getDescPesquisaBoleto() {
        if (Sessions.exists("pessoaPesquisa")) {
            descPesquisaBoleto = "";
        }
        return descPesquisaBoleto;
    }

    public void setDescPesquisaBoleto(String descPesquisaBoleto) {
        this.descPesquisaBoleto = descPesquisaBoleto;
    }

    public void setListContas(List<SelectItem> listContas) {
        this.listContas = listContas;
    }

    public int getIndexConta() {
        return indexConta;
    }

    public void setIndexConta(int indexConta) {
        this.indexConta = indexConta;
    }
}
