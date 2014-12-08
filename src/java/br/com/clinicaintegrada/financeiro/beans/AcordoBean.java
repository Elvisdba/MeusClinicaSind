package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.financeiro.Acordo;
import br.com.clinicaintegrada.financeiro.ContaCobranca;
import br.com.clinicaintegrada.financeiro.FTipoDocumento;
import br.com.clinicaintegrada.financeiro.Historico;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.financeiro.TipoServico;
import br.com.clinicaintegrada.financeiro.dao.ContaCobrancaDao;
import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
import br.com.clinicaintegrada.movimento.GerarMovimento;
import br.com.clinicaintegrada.movimento.ImprimirBoleto;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.FilialDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.seguranca.Registro;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.DataObject;
import br.com.clinicaintegrada.utils.Messages;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.Sessions;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AcordoBean {

    private List<DataObject> listVizualizado;
    private List<DataObject> listOperado;
    private Acordo acordo;
    private int idServicos;
    private int idVencimento;
    private int parcela;
    private float total;
    private String valorEntrada;
    private String vencimento;
    private int frequencia;
    private String totalOutras;
    private List<int[]> quantidade;
    public List<Boolean> listMarcados;
    private String ultimaData;
    private String mensagem;
    private boolean imprimeVerso;
    private Historico historico;
    private boolean imprimir;
    private boolean imprimir_pro;
    private List<Movimento> listMovs;
    private Pessoa pessoa;
    private Pessoa pessoaEnvio;
    private String emailPara;

    @PostConstruct
    public void init() {
        listVizualizado = new ArrayList<>();
        listOperado = new ArrayList<>();
        acordo = new Acordo();
        idServicos = 0;
        idVencimento = 0;
        parcela = 1;
        total = 0;
        valorEntrada = "0";
        vencimento = DataHoje.data();
        frequencia = 30;
        totalOutras = "0";
        quantidade = new ArrayList<>();
        listMarcados = new ArrayList<>();
        ultimaData = "";
        mensagem = "";
        imprimeVerso = false;
        historico = new Historico();
        imprimir = true;
        imprimir_pro = false;
        listMovs = new ArrayList();
        pessoa = new Pessoa();
        pessoaEnvio = new Pessoa();
        emailPara = "contabilidade";
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("acordoBean");
    }

    private String emailContato = "";

    public void verificaEmail() {
        JuridicaDao juridicaDao = new JuridicaDao();
        Juridica jur = juridicaDao.pesquisaJuridicaPorPessoa(pessoa.getId());
        if (pessoaEnvio.getEmail1().isEmpty()) {
            mensagem = "Digite um email válido!";
            pessoaEnvio = new Pessoa();
            return;
        }

        Dao dao = new Dao();
        if (emailPara.equals("contabilidade")) {
            if (jur.getContabilidade() == null) {
                mensagem = "Empresa sem contabilidade vinculada!";
                pessoaEnvio = new Pessoa();
                return;
            }

            if (!jur.getContabilidade().getPessoa().getEmail1().isEmpty() && !pessoaEnvio.getEmail1().isEmpty()) {
                if (jur.getContabilidade().getPessoa().getEmail1().equals(pessoaEnvio.getEmail1())) {
                    pessoaEnvio = jur.getContabilidade().getPessoa();
                } else {
                    jur.getContabilidade().getPessoa().setEmail1(pessoaEnvio.getEmail1());
                    dao.openTransaction();

                    if (dao.update(jur.getContabilidade().getPessoa())) {
                        dao.commit();
                    } else {
                        dao.rollback();
                    }

                    pessoaEnvio = jur.getContabilidade().getPessoa();
                }
            } else if (!jur.getContabilidade().getPessoa().getEmail1().isEmpty()) {
                pessoaEnvio = jur.getContabilidade().getPessoa();
            } else if (!pessoaEnvio.getEmail1().isEmpty()) {
                jur.getContabilidade().getPessoa().setEmail1(pessoaEnvio.getEmail1());
                dao.openTransaction();

                if (dao.update(jur.getContabilidade().getPessoa())) {
                    dao.commit();
                } else {
                    dao.rollback();
                }

                pessoaEnvio = jur.getContabilidade().getPessoa();
            } else {
                mensagem = "Digite um email válido!";
                pessoaEnvio = new Pessoa();
            }
        } else {
            if (!jur.getPessoa().getEmail1().isEmpty() && !pessoaEnvio.getEmail1().isEmpty()) {
                if (jur.getPessoa().getEmail1().equals(pessoaEnvio.getEmail1())) {
                    pessoaEnvio = jur.getPessoa();
                } else {
                    jur.getPessoa().setEmail1(pessoaEnvio.getEmail1());
                    dao.openTransaction();
                    if (dao.update(jur.getPessoa())) {
                        dao.commit();
                    } else {
                        dao.rollback();
                    }
                    pessoaEnvio = jur.getPessoa();
                }
            } else if (!jur.getPessoa().getEmail1().isEmpty()) {
                pessoaEnvio = jur.getPessoa();
            } else if (!pessoaEnvio.getEmail1().isEmpty()) {
                jur.getContabilidade().getPessoa().setEmail1(pessoaEnvio.getEmail1());
                dao.openTransaction();
                if (dao.update(jur.getPessoa())) {
                    dao.commit();
                } else {
                    dao.rollback();
                }
                pessoaEnvio = jur.getPessoa();
            } else {
                mensagem = "Digite um email válido!";
                pessoaEnvio = new Pessoa();
            }
        }
    }

    public String sendMail() {
        List<Movimento> listaImp = new ArrayList();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        Registro reg = new Registro();

        for (int i = 0; i < listOperado.size(); i++) {
            listaImp.add(((Movimento) listOperado.get(i).getArgumento2()));
            listaValores.add(((Movimento) listOperado.get(i).getArgumento2()).getValor());
            listaVencimentos.add(((Movimento) listOperado.get(i).getArgumento2()).getVencimentoString());
        }
        verificaEmail();
        if (!listaImp.isEmpty() && pessoaEnvio.getId() != -1) {
            for (int i = 0; i < listaImp.size(); i++) {
                ImprimirBoleto imp = new ImprimirBoleto();
                imp.imprimirBoleto(listaImp, listaValores, listaVencimentos, imprimeVerso);
                String patch = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos");
                if (!new File(patch + "/downloads").exists()) {
                    File file = new File(patch + "/downloads");
                    file.mkdir();
                }
                if (!new File(patch + "/downloads/boletos").exists()) {
                    File file = new File(patch + "/downloads/boletos");
                    file.mkdir();
                }
                String nome = imp.criarLink(listaImp.get(i).getPessoa(), reg.getSisUrlPath() + "/Sindical/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/boletos");

                reg = (Registro) new Dao().find(new Registro(), 1);
                List<Pessoa> p = new ArrayList();
                p.add(pessoaEnvio);

                String[] ret = new String[2];
                String nome_envio = "";
                if (listaImp.size() == 1) {
                    nome_envio = "Boleto " + listaImp.get(0).getServicos().getDescricao() + " N° " + listaImp.get(0).getDocumento();
                } else {
                    nome_envio = "Boleto";
                }
// VER
//                if (!reg.isEnviarEmailAnexo()) {
//                    ret = EnviarEmail.EnviarEmailPersonalizado(
//                            reg,
//                            p,
//                            " <h5>Visualize seu boleto clicando no link abaixo</5><br /><br />"
//                            + " <a href='" + reg.getSisUrlPath() + "/Sindical/acessoLinks.jsf?cliente=" + ControleUsuarioBean.getCliente() + "&amp;arquivo=" + nome + "' target='_blank'>Clique aqui para abrir boleto</a><br />",
//                            new ArrayList(),
//                            nome_envio
//                    );
//                } else {
//                    List<File> fls = new ArrayList<File>();
//                    fls.add(new File(imp.getPathPasta() + "/" + nome));
//
//                    ret = EnviarEmail.EnviarEmailPersonalizado(
//                            reg,
//                            p,
//                            " <h5>Baixe seu boleto anexado neste email</5><br /><br />",
//                            fls,
//                            nome_envio
//                    );
//                }
                if (!ret[1].isEmpty()) {
                    mensagem = ret[1];

                } else {
                    mensagem = ret[0];

                }
                listaImp.clear();
                p.clear();
            }
        }
        return null;
    }

    public List<SelectItem> getListVencimento() {
        List<SelectItem> vencto = new ArrayList<>();
        int i = 0;
        DataHoje data = new DataHoje();
        vencto.add(new SelectItem(
                i,
                vencimento)
        );
        i++;
        while (i < 31) {
            vencto.add(new SelectItem(
                    i,
                    data.incrementarDias(i, vencimento))
            );
            i++;
        }
        return vencto;
    }

    public void imprimirAcordo() {
//        if ((!opMovimentos.getListaAcordo().isEmpty()) && (mensagem.equals("Imprimir Boletos"))){
//            OperacaoMovimento opAcordo = new OperacaoMovimento(opMovimentos.getListaAcordo());
//            opAcordo.imprimirBoleto(imprimeVerso);
//        }
    }

    public List getListFolha() {
//        AcordoDao dbac = new AcordoDao();
//        List listaFolha = new ArrayList();
//        listaFolha = dbac.pesquisaTodasFolhas();
        return new ArrayList();
    }

    public List<DataObject> getListVizualizado() {
        if (listVizualizado.isEmpty() && !listMovs.isEmpty() && pessoa.getId() != -1) {
            historico.setHistorico("Acordo correspondente a: ");
            List<DataObject> aux = new ArrayList();
            aux.add(null);
            aux.add(null);
            aux.add(null);
            aux.add(null);
            float soma_sind = 0,
                    soma_assis = 0,
                    soma_conf = 0,
                    soma_neg = 0;

            String h_sind = "",
                    h_assis = "",
                    h_conf = "",
                    h_neg = "";

            for (int i = 0; i < listMovs.size(); i++) {
                soma_neg = Moeda.somaValores(soma_neg, listMovs.get(i).getValorBaixa());
                aux.set(3, new DataObject(listMovs.get(i).getServicos(), soma_neg, null, h_neg, null, null));
            }
            historico.setHistorico(historico.getHistorico() + h_sind + " " + h_assis + " " + h_conf + " " + h_neg);
            float soma_total = Moeda.somaValores(Moeda.somaValores(soma_assis, soma_conf), soma_neg);
            totalOutras = Moeda.converteR$Float(soma_total);
            total = Moeda.somaValores(soma_sind, soma_total);
            for (int i = 0; i < aux.size(); i++) {
                if (aux.get(i) != null) {
                    listVizualizado.add(new DataObject(aux.get(i).getArgumento0(), Moeda.converteR$Float((Float) aux.get(i).getArgumento1()), aux.get(i).getArgumento2(), (String) aux.get(i).getArgumento3(), null, null));
                }
            }
        }
        return listVizualizado;
    }

    public synchronized void efetuarAcordo() {
        if (listOperado.isEmpty()) {
            Messages.warn("Erro", "Acordo não foi gerado");
            return;
        }
        List<Movimento> listaAcordo = new ArrayList<Movimento>();
        List<String> listaHistorico = new ArrayList();

        for (int i = 0; i < listOperado.size(); i++) {
            listaAcordo.add((Movimento) listOperado.get(i).getArgumento2());
            listaHistorico.add((String) listOperado.get(i).getArgumento3());
        }

        try {
            // 07-11-2011 dep arrecad. secrp rogerio afirmou que o nr_ctr_boleto dos acordados tem que ser zerados,
            // para que nao haja conflito com os novos boletos gerados (* (nr_num_documento, nr_ctr_boleto, id_conta_cobranca) *)
            mensagem = GerarMovimento.salvarListaAcordo(acordo, listaAcordo, listMovs, listaHistorico);
            if (mensagem.isEmpty()) {
                Messages.info("Sucesso", "Acordo concluído com sucesso!");
            } else {
                Messages.warn("Erro", mensagem);
            }

            imprimir = false;

            String url = (String) Sessions.getString("urlRetorno");
            switch (url) {
                case "movimentosReceber":
                    ((MovimentosReceberBean) Sessions.getObject("movimentosReceberBean")).getListMovimento().clear();
                    break;
            }
        } catch (Exception e) {
            Messages.warn("Erro", "Acordo não foi gerado");
        }
    }

    public synchronized String subirData() {
        String vencimentoOut = getListVencimento().get(idVencimento).getLabel();
        if (listOperado.isEmpty()) {
            return null;
        }

        int i = 0;
        int j = 0;
        List listas = new ArrayList();
        List<Integer> subLista = new ArrayList<>();
        DataHoje data = new DataHoje();
        String dataPrincipal = "";
        String referencia = "";
        while (i < listOperado.size()) {
            if ((Boolean) listOperado.get(i).getArgumento0()) {
                subLista.add(i);
            } else {
                if (!(subLista.isEmpty())) {
                    listas.add(subLista);
                    subLista = new ArrayList<>();
                }
                while (i < listOperado.size()) {
                    if (listOperado.size() > (i + 1)) {
                        if ((Boolean) listOperado.get(i + 1).getArgumento0()) {
                            break;
                        }
                    }
                    i++;
                }
            }
            i++;
        }
        if (!(subLista.isEmpty())) {
            listas.add(subLista);
            subLista = new ArrayList<>();
        }
        i = 0;
        j = 0;
        String date = null;
        Servicos servico = null;
        while (i < listas.size()) {
            j = 0;
            Movimento movimento = (Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2();
            date = movimento.getVencimentoString();
            servico = movimento.getServicos();
            if (frequencia == 30) {
                if ((DataHoje.menorData(data.decrementarMeses(1, date), vencimentoOut))
                        && (!DataHoje.igualdadeData(data.decrementarMeses(1, date), vencimentoOut))) {
                    i++;
                    continue;
                }
                dataPrincipal = movimento.getVencimentoString();
                dataPrincipal = data.decrementarMeses(1, dataPrincipal);
                referencia = data.decrementarMeses(1, dataPrincipal);
            } else if (frequencia == 7) {
                if ((DataHoje.menorData(data.decrementarSemanas(1, date), vencimentoOut))
                        && (!DataHoje.igualdadeData(data.decrementarSemanas(1, date), vencimentoOut))) {
                    i++;
                    continue;
                }
                dataPrincipal = movimento.getVencimentoString();
                dataPrincipal = data.decrementarSemanas(1, dataPrincipal);
                referencia = data.decrementarSemanas(1, dataPrincipal);
            }

            while (j < ((List<Integer>) listas.get(i)).size()) {
                ((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setVencimentoString(dataPrincipal);
                if (movimento.getServicos().getId() != 1) {
                    ((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setReferencia(referencia.substring(3));
                }
                j++;
            }

            i++;
        }
        BubbleSort(listOperado);
        ordernarPorServico();
        while (i < listOperado.size()) {
            listOperado.get(i).setArgumento1(i + 1);
            i++;
        }
        return null;
    }

    public synchronized String descerData() {
        if (listOperado.isEmpty()) {
            return null;
        }
        int i = 0;
        int j = 0;
        List listas = new ArrayList();
        List<Integer> subLista = new ArrayList();
        DataHoje data = new DataHoje();
        String dataPrincipal = "";
        String referencia = "";
        while (i < listOperado.size()) {
            if ((Boolean) listOperado.get(i).getArgumento0()) {
                subLista.add(i);
            } else {
                if (!(subLista.isEmpty())) {
                    listas.add(subLista);
                    subLista = new ArrayList();
                }
                while (i < listOperado.size()) {
                    if (listOperado.size() > (i + 1)) {
                        if ((Boolean) listOperado.get(i + 1).getArgumento0()) {
                            break;
                        }
                    }
                    i++;
                }
            }
            i++;
        }
        if (!(subLista.isEmpty())) {
            listas.add(subLista);
            subLista = new ArrayList();
        }
        i = 0;
        j = 0;
        String date = null;
        Servicos servico = null;
        while (i < listas.size()) {
            j = 0;
            Movimento movimento = ((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2());
            date = movimento.getVencimentoString();
            servico = movimento.getServicos();
            if (frequencia == 30) {
                if (DataHoje.maiorData(data.incrementarMeses(1, date), ultimaData)) {
                    i++;
                    continue;
                }
                referencia = movimento.getVencimentoString();
                dataPrincipal = data.incrementarMeses(1, referencia);
            } else if (frequencia == 7) {
                if (DataHoje.maiorData(data.incrementarSemanas(1, date), ultimaData)) {
                    i++;
                    continue;
                }
                referencia = movimento.getVencimentoString();
                dataPrincipal = data.incrementarSemanas(1, referencia);
            }
            while (j < ((List<Integer>) listas.get(i)).size()) {
                ((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setVencimentoString(dataPrincipal);
                if (((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).getServicos().getId() != 1) {
                    ((Movimento) listOperado.get(((List<Integer>) listas.get(i)).get(j)).getArgumento2()).setReferencia(referencia.substring(3));
                }
                j++;
            }
            i++;
        }
        BubbleSort(listOperado);
        ordernarPorServico();
        i = 0;
        while (i < listOperado.size()) {
            listOperado.get(i).setArgumento1(i + 1);
            i++;
        }

        return null;
    }

    public synchronized void addParcela() {
        try {
            Dao dao = new Dao();
            FilialDao filialDao = new FilialDao();
            ContaCobrancaDao contaCobrancaDao = new ContaCobrancaDao();
            TipoServico tipoServico = (TipoServico) dao.find(new TipoServico(), 4);
            DataHoje data = new DataHoje();
            int j = 0, k = 0;
            Servicos servico = null;
            ContaCobranca contaCobranca = null;
            listOperado.clear();
            String ultimoVencimento = getListVencimento().get(idVencimento).getLabel();
            float valorTotalOutras = 0;
            float valorSwap = Moeda.substituiVirgulaFloat(valorEntrada);
            float valorTotal = Moeda.converteFloatR$Float(Moeda.substituiVirgulaFloat(totalOutras));
            float[] vetorEntrada = new float[listVizualizado.size()];
            float pdE = Moeda.divisaoValores(valorSwap, valorTotal);
            float valorParcela = 0;
            for (int i = 0; i < listVizualizado.size(); i++) {
                if (((Servicos) listVizualizado.get(i).getArgumento0()).getId() != 1) {
                    vetorEntrada[i] = Moeda.substituiVirgulaFloat((String) listVizualizado.get(i).getArgumento1());
                    if (listVizualizado.size() > 1) {
                        vetorEntrada[i] = Moeda.converteFloatR$Float(Moeda.multiplicarValores(vetorEntrada[i], pdE));
                    } else {
                        vetorEntrada[i] = valorSwap;
                    }
                } else {
                    vetorEntrada[i] = 0;
                }
            }
            String message = "";
            for (int i = 0; i < listVizualizado.size(); i++) {
                servico = (Servicos) listVizualizado.get(i).getArgumento0();
                contaCobranca = contaCobrancaDao.findContaCobrancaByServicoAndTipoServico(servico.getId(), tipoServico.getId());
                if (contaCobranca != null) {
                    ultimoVencimento = getListVencimento().get(idVencimento).getLabel();
                    j = 0;
                    if (parcela > 1) {
                        valorTotalOutras = Moeda.substituiVirgulaFloat((String) listVizualizado.get(i).getArgumento1());
                        valorTotalOutras = Moeda.subtracaoValores(valorTotalOutras, vetorEntrada[i]);
                        valorSwap = vetorEntrada[i];
                        valorParcela = Moeda.converteFloatR$Float(Moeda.divisaoValores(valorTotalOutras, parcela - 1));
                    } else {
                        valorSwap = Moeda.substituiVirgulaFloat((String) listVizualizado.get(i).getArgumento1());
                    }
                    while (j < parcela) {
                        if (j != 0) {
                            if ((Moeda.subtracaoValores(valorTotalOutras, valorParcela) != 0) && ((j + 1) == parcela)) {
                                valorParcela = valorTotalOutras;
                            } else {
                                valorTotalOutras = Moeda.subtracaoValores(valorTotalOutras, valorParcela);
                            }
                            valorSwap = valorParcela;
                        }
                        Movimento mov = new Movimento(
                                -1,
                                null,
                                pessoa,
                                servico,
                                null,
                                tipoServico,
                                (FTipoDocumento) dao.find(new FTipoDocumento(), 2),
                                valorSwap,
                                "", // ctr
                                referencia(ultimoVencimento),
                                DataHoje.converte(ultimoVencimento),
                                1,
                                true,
                                "E",
                                "",
                                null,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                null
                        );

                        listOperado.add(new DataObject(false, ++k, mov, (String) listVizualizado.get(i).getArgumento3(), null, null));

                        if (j == 0) {
                            ultimoVencimento = acordo.getDataString();
                        }

                        if (frequencia == 30) {
                            ultimoVencimento = data.incrementarMeses(1, ultimoVencimento);
                        } else if (frequencia == 7) {
                            ultimoVencimento = data.incrementarSemanas(1, ultimoVencimento);
                        }
                        j++;
                    }
                } else {
                    message += j + 1 + " - Serviço: " + servico.getDescricao() + " - Tipo Serviço: " + tipoServico.getDescricao() + "; ";
                }
            }
            if (!message.isEmpty()) {
                Messages.warn("Sistema", message + "Não existem no serviço conta cobrança.");
            }
        } catch (Exception e) {
            e.getMessage();
        }
        BubbleSort(listOperado);
        try {
            ultimaData = ((Movimento) listOperado.get(listOperado.size() - 1).getArgumento2()).getVencimentoString();
        } catch (Exception e) {
            ultimaData = "";
        }
    }

    public void imprimirBoletos() {
        ImprimirBoleto imp = new ImprimirBoleto();
        List<Float> listaValores = new ArrayList<Float>();
        List<String> listaVencimentos = new ArrayList<String>();
        List listaImp = new ArrayList();
        for (int i = 0; i < listOperado.size(); i++) {
            listaImp.add(((Movimento) listOperado.get(i).getArgumento2()));
            listaValores.add(((Movimento) listOperado.get(i).getArgumento2()).getValor());
            listaVencimentos.add(((Movimento) listOperado.get(i).getArgumento2()).getVencimentoString());

        }
        if (!listaImp.isEmpty()) {
            imp.imprimirBoleto(listaImp, listaValores, listaVencimentos, false);
            imp.visualizar(null);
        }
    }

    public void imprimirPlanilha() {
        ImprimirBoleto imp = new ImprimirBoleto();
        List listaImp = new ArrayList();
        MovimentoDao movimentoDao = new MovimentoDao();
        listaImp.addAll(movimentoDao.findAllAcordos(acordo.getId()));
        if (!listaImp.isEmpty()) {
            imp.imprimirAcordoPromissoria(listaImp, acordo, historico, emailContato, imprimir_pro);
            imp.visualizar(null);
        }

    }

    public String referencia(String data) {
        if (data.length() == 10) {
            String ref = data.substring(3);
            String mes = ref.substring(0, 2);
            if (!(mes.equals("01"))) {
                if ((Integer.parseInt(mes) - 1) < 10) {
                    ref = "0" + Integer.toString(Integer.parseInt(mes) - 1) + data.substring(5);
                } else {
                    ref = Integer.toString(Integer.parseInt(mes) - 1) + data.substring(5);
                }
            } else {
                ref = "12/" + Integer.toString(Integer.parseInt(data.substring(6)) - 1);
            }
            return ref;
        } else {
            return null;
        }
    }

    public List<DataObject> getListOperado() {
        return listOperado;
    }

    public void setListOperado(List<DataObject> listaOperado) {
        this.listOperado = listaOperado;
    }

    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public int getParcela() {
        return parcela;
    }

    public void setParcela(int parcela) {
        this.parcela = parcela;
    }

    public String getTotal() {
        return Moeda.converteR$Float(total);
    }

    public void setTotal(String total) {
        this.total = Moeda.substituiVirgulaFloat(total);
    }

    public void limparEntrada() {
        valorEntrada = "0";
    }

    public String getValorEntrada() {
        float valorTmp = Moeda.substituiVirgulaFloat(valorEntrada);
        float totalOutra = Moeda.substituiVirgulaFloat(totalOutras);
        if (valorEntrada.equals("0") || valorEntrada.equals("0,00")) {
            float valorTmp2 = Moeda.divisaoValores(totalOutra, parcela);
            if (parcela > 1) {
                valorEntrada = Moeda.converteR$Float(valorTmp2);
                return valorEntrada;
            }
        } else {
            if (valorTmp > (Moeda.multiplicarValores(totalOutra, (float) 0.05))
                    && valorTmp < (Moeda.multiplicarValores(totalOutra, (float) 0.8))) {
                return Moeda.converteR$(valorEntrada);
            } else {
                float valorTmp2 = Moeda.divisaoValores(totalOutra, parcela);
                if (parcela > 1) {
                    valorEntrada = Moeda.converteR$Float(valorTmp2);
//                    return Moeda.converteR$(valorEntrada);
                }
            }
        }
        return Moeda.converteR$(valorEntrada);
    }

    public void setValorEntrada(String valorEntrada) {
        this.valorEntrada = Moeda.substituiVirgula(valorEntrada);
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public int getIdVencimento() {
        return idVencimento;
    }

    public void setIdVencimento(int idVencimento) {
        this.idVencimento = idVencimento;
    }

    public String getTotalOutras() {
        return Moeda.converteR$(totalOutras);
    }

    public void setTotalOutras(String totalOutras) {
        this.totalOutras = Moeda.substituiVirgula(totalOutras);
    }

    public synchronized void ordernarPorServico() {
        int i = 0;
        int indI = 0, indF = 0;
        String data = ((Movimento) listOperado.get(i).getArgumento2()).getVencimentoString();
        while (i < listOperado.size()) {
            if (!data.equals(((Movimento) listOperado.get(i).getArgumento2()).getVencimento())) {
                BubbleSortServico(listOperado.subList(indI, indF));
                indI = indF;
                indF++;
                data = ((Movimento) listOperado.get(i).getArgumento2()).getVencimentoString();
            } else {
                indF++;
            }
            i++;
        }
    }

    public static void BubbleSort(List<DataObject> dados) {
        boolean trocou;
        int limite = dados.size() - 1;
        Object swap1 = null;
        Object swap2 = null;
        int i = 0;
        do {
            trocou = false;
            i = 0;
            while (i < limite) {
                if (((Movimento) dados.get(i).getArgumento2()).getVencimento().after(
                        ((Movimento) dados.get(i + 1).getArgumento2()).getVencimento())) {

                    swap1 = dados.get(i).getArgumento0();
                    swap2 = dados.get(i + 1).getArgumento0();
                    dados.get(i).setArgumento0(swap2);
                    dados.get(i + 1).setArgumento0(swap1);

                    swap1 = dados.get(i).getArgumento1();
                    swap2 = dados.get(i + 1).getArgumento1();
                    dados.get(i).setArgumento1(swap2);
                    dados.get(i + 1).setArgumento1(swap1);

                    swap1 = dados.get(i).getArgumento2();
                    swap2 = dados.get(i + 1).getArgumento2();
                    dados.get(i).setArgumento2(swap2);
                    dados.get(i + 1).setArgumento2(swap1);

                    swap1 = dados.get(i).getArgumento3();
                    swap2 = dados.get(i + 1).getArgumento3();
                    dados.get(i).setArgumento3(swap2);
                    dados.get(i + 1).setArgumento3(swap1);
                    trocou = true;
                }
                i++;
            }
            limite--;
        } while (trocou);
    }

    public static void BubbleSortServico(List<DataObject> dados) {
        boolean trocou;
        int limite = dados.size() - 1;
        Object swap1 = null;
        Object swap2 = null;
        int i = 0;
        int result = -1;
        do {
            trocou = false;
            i = 0;
            while (i < limite) {
                result = ((Movimento) dados.get(i).getArgumento2()).getServicos().getDescricao().compareTo(
                        ((Movimento) dados.get(i + 1).getArgumento2()).getServicos().getDescricao());
                if (result > 0) {
                    swap1 = dados.get(i).getArgumento0();
                    swap2 = dados.get(i + 1).getArgumento0();
                    dados.get(i).setArgumento0(swap2);
                    dados.get(i + 1).setArgumento0(swap1);

                    swap1 = dados.get(i).getArgumento1();
                    swap2 = dados.get(i + 1).getArgumento1();
                    dados.get(i).setArgumento1(swap2);
                    dados.get(i + 1).setArgumento1(swap1);

                    swap1 = dados.get(i).getArgumento2();
                    swap2 = dados.get(i + 1).getArgumento2();
                    dados.get(i).setArgumento2(swap2);
                    dados.get(i + 1).setArgumento2(swap1);
                    trocou = true;
                }
                i++;
            }
            limite--;
        } while (trocou);
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isImprimeVerso() {
        return imprimeVerso;
    }

    public void setImprimeVerso(boolean imprimeVerso) {
        this.imprimeVerso = imprimeVerso;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public boolean isImprimir() {
        return imprimir;
    }

    public void setImprimir(boolean imprimir) {
        this.imprimir = imprimir;
    }

    public List<Movimento> getListMovs() {
        return listMovs;
    }

    public void setListMovs(List<Movimento> listMovs) {
        this.listMovs = listMovs;
    }

    public Pessoa getPessoa() {
        if (Sessions.exists("listMovimentos")) {
            listMovs = Sessions.getList("listMovimentos", true);
            pessoa = listMovs.get(0).getPessoa();
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void setListVizualizado(List<DataObject> listVizualizado) {
        this.listVizualizado = listVizualizado;
    }

    public String getEmailPara() {
        return emailPara;
    }

    public void setEmailPara(String emailPara) {
        this.emailPara = emailPara;
    }

    public Pessoa getPessoaEnvio() {
        return pessoaEnvio;
    }

    public void setPessoaEnvio(Pessoa pessoaEnvio) {
        this.pessoaEnvio = pessoaEnvio;
    }

    public boolean isImprimir_pro() {
        return imprimir_pro;
    }

    public void setImprimir_pro(boolean imprimir_pro) {
        this.imprimir_pro = imprimir_pro;
    }

    public String getEmailContato() {
        return emailContato;
    }

    public void setEmailContato(String emailContato) {
        this.emailContato = emailContato;
    }
}
