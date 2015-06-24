package br.com.clinicaintegrada.financeiro.beans;

import br.com.clinicaintegrada.cobranca.BancoDoBrasil;
import br.com.clinicaintegrada.cobranca.Bradesco;
import br.com.clinicaintegrada.cobranca.CaixaFederalSicob;
import br.com.clinicaintegrada.cobranca.CaixaFederalSigCB;
import br.com.clinicaintegrada.cobranca.Cobranca;
import br.com.clinicaintegrada.cobranca.Itau;
import br.com.clinicaintegrada.cobranca.Real;
import br.com.clinicaintegrada.cobranca.Santander;
import br.com.clinicaintegrada.cobranca.Sicoob;
import br.com.clinicaintegrada.financeiro.Boleto;
import br.com.clinicaintegrada.financeiro.BoletosVw;
import br.com.clinicaintegrada.financeiro.Movimento;
import br.com.clinicaintegrada.financeiro.dao.BoletoDao;
import br.com.clinicaintegrada.impressao.ParametroBoleto;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.clinicaintegrada.utils.Dao;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Dirs;
import br.com.clinicaintegrada.utils.Download;
import br.com.clinicaintegrada.utils.Moeda;
import br.com.clinicaintegrada.utils.SalvaArquivos;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@ViewScoped
public class ImpressaoBoleto {

    public static boolean IMPRIME_VERSO;
    public static boolean IMPRIME_VERSO_FIM;

    public static void printByNrCtrBoleto(String nrCtrBoleto) {
        if (nrCtrBoleto.isEmpty()) {
            return;
        }
        List<BoletosVw> listBoletosVw = new BoletoDao().listBoletosVwByNrCtrBoleto(nrCtrBoleto);
        load(listBoletosVw);
    }

    public static void printByLote(Integer idLote) {
        if (idLote == null || idLote == 0) {
            return;
        }
        List<BoletosVw> listBoletosVw = new BoletoDao().finBoletosByLote(idLote);
        load(listBoletosVw);

    }

    public static void load(List<BoletosVw> listBoletosVw) {
        if (!listBoletosVw.isEmpty()) {
            List list = new ArrayList();
            Dao dao = new Dao();
            Filial filial = (Filial) dao.find(new Filial(), 1);
            Map<String, Object> map = new LinkedHashMap<>();
            float valor = 0;
            double valor_total = 0;

            try {
                File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO.jasper"));
                JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

                File file_jasper_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_VERSO.jasper"));
                JasperReport jasperReportVerso = (JasperReport) JRLoader.loadObject(file_jasper_verso);
                List<JasperPrint> jasperPrintList = new ArrayList<>();
                //File file_promo = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/BannerPromoBoleto.png"));

////                if (!file_promo.exists()) {
////                    file_promo = null;
////                }
                // String LAYOUT = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_HEADER.jasper");
                Cobranca cobranca = null;
                String cedente_logo = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png");
                // int qntItens = 0;
                for (int w = 0; w < listBoletosVw.size(); w++) {
                    Boleto boletox = new BoletoDao().findBoletosByNrCtrBoleto(listBoletosVw.get(w).getNrCtrBoleto()); // NR_CTR_BOLETO
                    String logo_banco = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(boletox.getContaCobranca().getContaBanco().getBanco().getLogo().trim());
                    Movimento mov = (Movimento) dao.find(new Movimento(), (Integer) Integer.parseInt(Long.toString(listBoletosVw.get(w).getMovimento())));
                    if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
                            && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SICOB)) {
                        cobranca = new CaixaFederalSicob(mov, boletox);
                    } else if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
                            && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SIGCB)) {
                        cobranca = new CaixaFederalSigCB(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.itau)) {
                        cobranca = new Itau(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bancoDoBrasil)) {
                        cobranca = new BancoDoBrasil(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.real)) {
                        cobranca = new Real(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bradesco)) {
                        cobranca = new Bradesco(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.santander)) {
                        cobranca = new Santander(mov, boletox);
                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.sicoob)) {
                        cobranca = new Sicoob(mov, boletox);
                    }

                    valor = Moeda.converteUS$(listBoletosVw.get(w).getValorString());
                    valor_total = Moeda.somaValores(valor_total, listBoletosVw.get(w).getValor());

                    list.add(new ParametroBoleto(
                            // CEDENTE
                            listBoletosVw.get(w).getNomeFilial(), // CEDENTE - NOME
                            listBoletosVw.get(w).getCnpjFilial(), // CEDENTE - DOCUMENTO
                            boletox.getContaCobranca().getCodCedente(), // CEDENTE - CÓDIGO
                            listBoletosVw.get(w).getEnderecoFilial(), // CEDENTE - ENDEREÇO
                            "", // CEDENTE - NÚMERO
                            "", // CEDENTE - COMPLEMENTO
                            listBoletosVw.get(w).getBairroFilial(), // CEDENTE - BAIRRO
                            listBoletosVw.get(w).getCidadeFilial(), // CEDENTE - CIDADE
                            listBoletosVw.get(w).getUfFilial(), // CEDENTE - UF
                            listBoletosVw.get(w).getCepFilial(), // CEDENTE - CEP
                            cedente_logo, // CEDENTE - LOGO
                            listBoletosVw.get(w).getSilteFilial(), // CEDENTE - SITE
                            listBoletosVw.get(w).getEmail(), // CEDENTE - EMAIL
                            listBoletosVw.get(w).getTelefoneFilial(), // CEDENTE - EMAIL
                            // SACADO
                            mov.getPessoa().getNome(), // SACADO - NOME
                            mov.getPessoa().getDocumento(), // SACADO - DOCUMENTO
                            listBoletosVw.get(w).getEnderecoResponsavel(), // SACADO - ENDEREÇO
                            "", // SACADO - NÚMERO
                            "", // SACADO - COMPLEMENTO
                            "", // SACADO - BAIRRO
                            listBoletosVw.get(w).getCidadeResponsavel(), // SACADO - CIDADE
                            listBoletosVw.get(w).getUfResponsavel(), // SACADO - ESTADO
                            listBoletosVw.get(w).getCepResponsavel(), // SACADO - CEP

                            // BANCO
                            boletox.getContaCobranca().getContaBanco().getBanco().getNumero(), // BANCO - CÓDIGO
                            logo_banco, // BANCO - LOGO
                            "", // BANCO - USO
                            cobranca.getNossoNumeroFormatado(), // BANCO - NOSSO NÚMERO
                            cobranca.getAgenciaFormatada(), // BANCO - AGÊNCIA

                            // HEADER
                            listBoletosVw.get(w).getServico(), // SERVIÇO
                            "", // TIPO

                            // BOLETO - LAYOUT
                            mov.getReferencia(), // REFERÊNCIA 
                            new BigDecimal(valor), // VALOR
                            cobranca.representacao(), // REPRESETANÇÃO NÚMÉRICA
                            DataHoje.converteData(boletox.getDtVencimento()), // VENCIMENTO
                            mov.getLote().getEmissaoString(), // DATA DOCUMENTO
                            boletox.getContaCobranca().getMoeda(), // MOEDA
                            boletox.getContaCobranca().getEspecieMoeda(), // ESPÉCIE
                            boletox.getContaCobranca().getEspecieDoc(), // ESPÉCIE DOCUMENTO
                            boletox.getContaCobranca().getAceite(), // ACEITE
                            boletox.getContaCobranca().getCarteira(), // CARTEIRA
                            mov.getReferencia().substring(3), // EXERCICÍO
                            mov.getDocumento(), // BOLETO
                            "", //  MENSAGEM
                            boletox.getMensagem(), // MENSAGEM BOLETO
                            getSerrilha(), // SERRILHA
                            "", // TEXTO
                            "", // CAMINHO VERSO
                            boletox.getContaCobranca().getLocalPagamento(), // LOCAL DE PAGAMENTO
                            listBoletosVw.get(w).getServico(), // DESCRIÇÃO SERVIÇO
                            false, // IMPRIME VERSO
                            "", // LAYOUT
                            cobranca.codigoBarras(), // CÓDIGO BARRAS
                            mov.getLote().getContrato().getId() // CONTRATO NUMERO
                    ));
//                        list.add(new ParametroBoleto(
//                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO CLÍNICA
//                                filial.getFilial().getPessoa().getNome(), // CLIENTE NOME
//                                "" + listBoletosVw.get(w).getCodigo(), // CODIGO
//                                listBoletosVw.get(w).getResponsavel(), // RESPONSAVEL
//                                listBoletosVw.get(w).getVencimentoString(), // VENCIMENTO
//                                listBoletosVw.get(w).getServico(), // SERVICO
//                                Moeda.converteR$Float(valor), // VALOR
//                                Moeda.converteR$Float(valor_total), // VALOR TOTAL
//                                Moeda.converteR$("" + listBoletosVw.get(w).getMensalidadesCorrigidas()), // VALOR ATRASADAS
//                                Moeda.converteR$Float(valor_total), // VALOR ATÉ  VALOR VENCIMENTO
//                                file_promo == null ? null : file_promo.getAbsolutePath(), // LOGO PROMOÇÃO
//                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(boletox.getContaCobranca().getContaBanco().getBanco().getLogo().trim()), // LOGO BANCO
//                                listBoletosVw.get(w).getMensagemBoleto(), // MENSAGEM
//                                listBoletosVw.get(w).getAgencia(), // AGENCIA
//                                cobranca.representacao(), // REPRESENTACAO
//                                listBoletosVw.get(w).getCedente(), // CODIGO CEDENTE
//                                listBoletosVw.get(w).getNrCtrBoleto(), // NOSSO NUMENTO
//                                listBoletosVw.get(w).getProcessamentoString(), // PROCESSAMENTO
//                                cobranca.codigoBarras(),
//                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"), // SERRILHA
//                                listBoletosVw.get(w).getLogradouroResponsavel() + " " + listBoletosVw.get(w).getEnderecoResponsavel(), // ENDERECO RESPONSAVEL
//                                listBoletosVw.get(w).getBairroFilial() + " " + listBoletosVw.get(w).getEnderecoFilial(), // ENDERECO FILIAL
//                                listBoletosVw.get(w).getCidadeResponsavel() + " " + listBoletosVw.get(w).getUfResponsavel() + " " + listBoletosVw.get(w).getCepResponsavel(), // COMPLEMENTO RESPONSAVEL
//                                listBoletosVw.get(w).getCidadeFilial() + " " + listBoletosVw.get(w).getUfFilial() + " " + listBoletosVw.get(w).getCepFilial(), // COMPLEMENTO FILIAL
//                                listBoletosVw.get(w).getCnpjFilial(), // CNPJ FILIAL
//                                listBoletosVw.get(w).getTelefoneFilial(), // TELEFONE FILIAL
//                                listBoletosVw.get(w).getEmail(), // EMAIL FILIAL
//                                listBoletosVw.get(w).getSilteFilial(), // SITE FILIAL
//                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO BOLETO - VERSO
//                                listBoletosVw.get(w).getLocalPagamento(), // LOCAL DE PAGAMENTO
//                                listBoletosVw.get(w).getInformativo()
//                        ));
                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(list);
                    jasperPrintList.add(JasperFillManager.fillReport(jasperReport, null, dtSource));
                    dtSource = new JRBeanCollectionDataSource(list);
                    jasperPrintList.add(JasperFillManager.fillReport(jasperReportVerso, null, dtSource));
//                    if (IMPRIME_VERSO) {
//                        if (IMPRIME_VERSO_FIM) {
//                            if ((w + 1) == listBoletosVw.size()) {
//                            }
//                        } else {
//                            dtSource = new JRBeanCollectionDataSource(list);
//                            jasperPrintList.add(JasperFillManager.fillReport(jasperReportVerso, null, dtSource));
//                        }
//
//                    }
                    valor = 0;
                    valor_total = 0;
                    list.clear();
                }

                JRPdfExporter exporter = new JRPdfExporter();
                ByteArrayOutputStream retorno = new ByteArrayOutputStream();

                exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
                exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
                exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
                exporter.exportReport();

                byte[] arquivo = retorno.toByteArray();
                UUID uuidX = UUID.randomUUID();
                String uuid = uuidX.toString().replace("-", "_");
                String nomeDownload = "boleto_bancario_" + uuid + ".pdf";
                Dirs.create("arquivos/downloads/boletos");
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/arquivos/downloads/boletos");
                SalvaArquivos sa = new SalvaArquivos(arquivo, nomeDownload, false);
                sa.salvaNaPasta(pathPasta);
                Download download = new Download(nomeDownload, pathPasta, "application/pdf", FacesContext.getCurrentInstance());
                download.open();
                download.close();
            } catch (JRException e) {
                e.getMessage();
            } catch (NumberFormatException e) {
                e.getMessage();
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    public static String getCaminho_verso() {
        String caminho_verso = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_VERSO.jasper");
        if (new File(caminho_verso).exists()) {
            return caminho_verso;
        }
        return "";
    }

    public static String getSerrilha() {
        String caminho_verso = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF");
        if (new File(caminho_verso).exists()) {
            return caminho_verso;
        }
        return "";
    }
}

//
//import br.com.clinicaintegrada.cobranca.BancoDoBrasil;
//import br.com.clinicaintegrada.cobranca.Bradesco;
//import br.com.clinicaintegrada.cobranca.CaixaFederalSicob;
//import br.com.clinicaintegrada.cobranca.CaixaFederalSigCB;
//import br.com.clinicaintegrada.cobranca.Cobranca;
//import br.com.clinicaintegrada.cobranca.Itau;
//import br.com.clinicaintegrada.cobranca.Real;
//import br.com.clinicaintegrada.cobranca.Santander;
//import br.com.clinicaintegrada.cobranca.Sicoob;
//import br.com.clinicaintegrada.financeiro.Boleto;
//import br.com.clinicaintegrada.financeiro.BoletosVw;
//import br.com.clinicaintegrada.financeiro.Movimento;
//import br.com.clinicaintegrada.financeiro.dao.BoletoDao;
//import br.com.clinicaintegrada.financeiro.dao.FinanceiroDao;
//import br.com.clinicaintegrada.financeiro.dao.MovimentoDao;
//import br.com.clinicaintegrada.pessoa.Filial;
//import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
//import br.com.clinicaintegrada.utils.Moeda;
//import br.com.clinicaintegrada.utils.Dao;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Vector;
//// import java.util.Vector;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperPrint;
//import net.sf.jasperreports.engine.JasperReport;
//import net.sf.jasperreports.engine.export.JRPdfExporter;
//import net.sf.jasperreports.engine.util.JRLoader;
//
//@ManagedBean
//@SessionScoped
//public class ImpressaoBoletoBean {
//
//    private List listBoletos;
//    private List[] selectedBoleto;
//    private int de;
//    private int ate;
//    private boolean imprimeVerso;
//    private String strResponsavel;
//    private String strLote;
//    private String processamentoString;
//
//    public ImpressaoBoletoBean() {
//        listBoletos = new ArrayList<>();
//        selectedBoleto = null;
//        de = 0;
//        ate = 0;
//        imprimeVerso = true;
//        strResponsavel = "";
//        strLote = "";
//        processamentoString = "";
//    }
//
//    public void filter() {
//        listBoletos.clear();
//        if (!processamentoString.isEmpty()) {
//            BoletoDao boletoDao = new BoletoDao();
//            listBoletos = boletoDao.listaBoletosGroup(strResponsavel, strLote, processamentoString);
//        }
//    }
//
//    public void select() {
//        for (int i = 0; i < listBoletos.size(); i++) {
////            if ((i + 1) >= de && ate == 0) {
////                listBoletos.get(i).setArgumento1(true);
////            } else if ((i + 1) >= de && (i + 1) <= ate) {
////                listBoletos.get(i).setArgumento1(true);
////            } else if (de == 0 && (i + 1) <= ate) {
////                listBoletos.get(i).setArgumento1(true);
////            } else {
////                listBoletos.get(i).setArgumento1(false);
////            }
//        }
//    }
//
//    public void print() {
//        List lista = new ArrayList();
//        Dao dao = new Dao();
//        Filial filial = (Filial) dao.find(new Filial(), 1);
//        FinanceiroDao financeiroDao = new FinanceiroDao();
//        Map<String, Object> map = new LinkedHashMap<>();
//        float valor = 0;
//        float valor_total = 0;
//
//        try {
//            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO.jasper"));
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
//
//            File file_jasper_verso = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/BOLETO_VERSO.jasper"));
//            JasperReport jasperReportVerso = (JasperReport) JRLoader.loadObject(file_jasper_verso);
//
//            List<JasperPrint> jasperPrintList = new ArrayList<>();
//            File file_promo = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/BannerPromoBoleto.png"));
//
//            if (!file_promo.exists()) {
//                file_promo = null;
//            }
//
//            MovimentoDao movimentoDao = new MovimentoDao();
//            BoletoDao boletoDao = new BoletoDao();
//            Cobranca cobranca = null;
//
//            for (int i = 0; i < selectedBoleto.length; i++) {
//                List lista_socio = boletoDao.listaBoletos("0000000963790020001400"); // NR_CTR_BOLETO
//                for (int w = 0; w < lista_socio.size(); w++) {
//                    Boleto boletox = boletoDao.findBoletosByNrCtrBoleto("0000000963790020001400"); // NR_CTR_BOLETO
//                    Movimento mov = (Movimento) dao.find(new Movimento(), 1);
//                    if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
//                            && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SICOB)) {
//                        cobranca = new CaixaFederalSicob(mov, boletox);
//                    } else if ((boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.caixaFederal))
//                            && (boletox.getContaCobranca().getLayout().getId() == Cobranca.SIGCB)) {
//                        cobranca = new CaixaFederalSigCB(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.itau)) {
//                        cobranca = new Itau(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bancoDoBrasil)) {
//                        cobranca = new BancoDoBrasil(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.real)) {
//                        cobranca = new Real(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.bradesco)) {
//                        cobranca = new Bradesco(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.santander)) {
//                        cobranca = new Santander(mov, boletox);
//                    } else if (boletox.getContaCobranca().getContaBanco().getBanco().getNumero().equals(Cobranca.sicoob)) {
//                        cobranca = new Sicoob(mov, boletox);
//                    }
//
//                    valor = Moeda.converteUS$(null);
//                    valor_total = Moeda.somaValores(valor_total, Moeda.converteUS$(null));
//
////                    lista.add(new ParametroBoleto(
////                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"), // LOGO CLÍNICA
////                            filial.getFilial().getPessoa().getNome(),
////                            lista_socio.get(w).get(9).toString(), // CODIGO
////                            lista_socio.get(w).get(10).toString(), // RESPONSAVEL
////                            DataHoje.converteData((Date) lista_socio.get(w).get(11)), // VENCIMENTO
////                            (lista_socio.get(w).get(12) == null) ? "" : lista_socio.get(w).get(12).toString(), // MATRICULA
////                            (lista_socio.get(w).get(14) == null) ? "" : lista_socio.get(w).get(14).toString(), // CATEGORIA
////                            (lista_socio.get(w).get(13) == null) ? "" : lista_socio.get(w).get(13).toString(), // GRUPO
////                            lista_socio.get(w).get(16).toString(), // CODIGO BENEFICIARIO
////                            lista_socio.get(w).get(17).toString(), // BENEFICIARIO
////                            lista_socio.get(w).get(15).toString(), // SERVICO
////                            Moeda.converteR$Float(valor), // VALOR
////                            Moeda.converteR$Float(valor_total), // VALOR TOTAL
////                            Moeda.converteR$(lista_socio.get(w).get(19).toString()), // VALOR ATRASADAS
////                            Moeda.converteR$Float(valor_total), // VALOR ATÉ  VALORVENCIMENTO
////                            file_promo == null ? null : file_promo.getAbsolutePath(), // LOGO PROMOÇÃO
////                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(boletox.getContaCobranca().getContaBanco().getBanco().getLogo().trim()), // LOGO BANCO
////                            lista_socio.get(w).get(20).toString(), // MENSAGEM
////                            lista_socio.get(w).get(22).toString(), // AGENCIA
////                            cobranca.representacao(), // REPRESENTACAO
////                            lista_socio.get(w).get(23).toString(), // CODIGO CEDENTE
////                            lista_socio.get(w).get(24).toString(), // NOSSO NUMENTO
////                            DataHoje.converteData((Date) lista_socio.get(w).get(4)), // PROCESSAMENTO
////                            cobranca.codigoBarras(), // CODIGO DE BARRAS
////                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/serrilha.GIF"), // SERRILHA
////                            lista_socio.get(w).get(35).toString() + " " + lista_socio.get(w).get(36).toString(), // ENDERECO RESPONSAVEL
////                            lista_socio.get(w).get(30).toString() + " " + lista_socio.get(w).get(31).toString(), // ENDERECO FILIAL
////                            lista_socio.get(w).get(39).toString() + " " + lista_socio.get(w).get(38).toString() + " " + lista_socio.get(w).get(37).toString(), // COMPLEMENTO RESPONSAVEL
////                            lista_socio.get(w).get(32).toString() + " - " + lista_socio.get(w).get(33).toString() + " " + lista_socio.get(w).get(34).toString(), // COMPLEMENTO FILIAL
////                            lista_socio.get(w).get(28).toString(), // CNPJ FILIAL
////                            lista_socio.get(w).get(29).toString(), // TELEFONE FILIAL
////                            lista_socio.get(w).get(25).toString(), // EMAIL FILIAL
////                            lista_socio.get(w).get(27).toString(), // SITE FILIAL
////                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoBoletoVersoSocial.png"), // LOGO BOLETO VERSO SOCIAL
////                            lista_socio.get(w).get(41).toString(), // LOCAL DE PAGAMENTO
////                            lista_socio.get(w).get(40).toString() // INFORMATIVO
////                    ));
////                }
////                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
////                jasperPrintList.add(JasperFillManager.fillReport(jasperReport, null, dtSource));
////                if (imprimeVerso) {
////                    dtSource = new JRBeanCollectionDataSource(lista);
////                    jasperPrintList.add(JasperFillManager.fillReport(jasperReportVerso, null, dtSource));
////                }
////                lista.clear();
////                valor = 0;
////                valor_total = 0;
//                }
//            }
//
//            JRPdfExporter exporter = new JRPdfExporter();
//            ByteArrayOutputStream retorno = new ByteArrayOutputStream();
//
////        exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
////        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, retorno);
////        exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
////        exporter.exportReport();
//            byte[] arquivo = retorno.toByteArray();
//
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Boleto Social.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();
//        } catch (IOException e) {
//            Logger.getLogger(GerarBoletoBean.class.getName()).log(Level.SEVERE, null, e);
//        } catch (JRException ex) {
//            Logger.getLogger(ImpressaoBoletoBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
////    public List<DataObject> getListaGrid() {
////        if (listBoletos.isEmpty() && !strData.isEmpty()) {
////            FinanceiroDao financeiroDao = new FinanceiroDao();
////            List<BoletosVw> list = financeiroDao.listaBoletosGroup(strResponsavel, strLote, strData);
////            for (int i = 0; i < list.size(); i++) {
////                //listBoletos.add(new DataObject(i + 1, true, list.get(i), Moeda.converteR$(list.get(i).get(6).toString())));
////                listBoletos.add(new DataObject(i + 1, true, list.get(i), Moeda.converteR$(list.get(i).getValor().toString())));
////            }
////        }
////        return listBoletos;
////    }
//    public List getListaBoletos() {
//        return listBoletos;
//    }
//
//    public void setListaBoletos(List listBoletos) {
//        this.listBoletos = listBoletos;
//    }
//
//    public int getDe() {
//        return de;
//    }
//
//    public void setDe(int de) {
//        this.de = de;
//    }
//
//    public int getAte() {
//        return ate;
//    }
//
//    public void setAte(int ate) {
//        this.ate = ate;
//    }
//
//    public boolean isImprimeVerso() {
//        return imprimeVerso;
//    }
//
//    public void setImprimeVerso(boolean imprimeVerso) {
//        this.imprimeVerso = imprimeVerso;
//    }
//
//    public String getStrResponsavel() {
//        return strResponsavel;
//    }
//
//    public void setStrResponsavel(String strResponsavel) {
//        this.strResponsavel = strResponsavel;
//    }
//
//    public String getStrLote() {
//        return strLote;
//    }
//
//    public void setStrLote(String strLote) {
//        this.strLote = strLote;
//    }
//
//    public String getProcessamentoString() {
//        return processamentoString;
//    }
//
//    public void setProcessamentoString(String processamentoString) {
//        this.processamentoString = processamentoString;
//    }
//
//    public List[] getSelectedBoleto() {
//        return selectedBoleto;
//    }
//
//    public void setSelectedBoleto(List[] selectedBoleto) {
//        this.selectedBoleto = selectedBoleto;
//    }
//
//}
