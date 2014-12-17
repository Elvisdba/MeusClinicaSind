package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class Jasper {

    public static String PATH = "downloads/relatorios";
    public static String PART_NAME = "relatorio";
    public static Boolean IS_DOWNLOAD = true;
    public static byte[] BYTES = null;

    public static void printReports(String jasperName, String fileName, Collection c) {
        byte[] bytesComparer = null;
        byte[] b;
        if (fileName.isEmpty() || jasperName.isEmpty() || c.isEmpty()) {
            Messages.info("Sistema", "Erro ao criar relatório!");
            return;
        }
        if (!Dirs.create("Arquivos/" + PATH + "/" + fileName)) {
            Messages.info("Sistema", "Erro ao criar diretório!");
            return;
        }
        try {
            JasperReport jasper;
            FacesContext faces = FacesContext.getCurrentInstance();
            if (new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/" + jasperName)).exists()) {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/ " + jasperName)));
            } else {
                jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) faces.getExternalContext().getContext()).getRealPath(jasperName)));
            }
            try {
                JRBeanCollectionDataSource dtSource;
                dtSource = new JRBeanCollectionDataSource(c);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                if (bytesComparer == BYTES) {
                    b = JasperExportManager.exportReportToPdf(print);
                } else {
                    b = BYTES;
                }
                String downloadName = PART_NAME + "_" + fileName + "_" + DataHoje.horaMinuto().replace(":", "") + ".pdf";
                String dirPath = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/" + PATH + "/" + fileName + "/");
                try {
                    File file = new File(dirPath);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write(b);
                        out.flush();
                    }
                } catch (IOException e) {
                    System.out.println(e);
                    return;
                }
                if (IS_DOWNLOAD) {
                    Download download = new Download(downloadName, dirPath, "application/pdf", FacesContext.getCurrentInstance());
                    download.open();
                    download.close();
                }
            } catch (JRException erro) {
                Messages.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
                System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            }
        } catch (JRException erro) {
            Messages.info("Sistema", "O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
            System.err.println("O arquivo não foi gerado corretamente! Erro: " + erro.getMessage());
        }
    }

}