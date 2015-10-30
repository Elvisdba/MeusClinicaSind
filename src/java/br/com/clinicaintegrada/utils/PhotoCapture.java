package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Juridica;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.JuridicaDao;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.event.CaptureEvent;

@ManagedBean
@SessionScoped
public class PhotoCapture implements Serializable {

    private static Boolean renderedPhotoCapture = false;
    private static String update = "";
    private static String savePath = "";
    private static String nameFile = "";
    private static Pessoa pessoa = null;

    public static void open(String aSavePath, String aUpdate) {
        savePath = aSavePath;
        update = aUpdate;
        renderedPhotoCapture = true;

        PF.openDialog("dlg_photo_capture");
        PF.update("form_photo_capture");
    }

    public static void openAndSave(Pessoa aPessoa, String aUpdate) {
        pessoa = aPessoa;
        update = aUpdate;
        renderedPhotoCapture = true;

        PF.openDialog("dlg_photo_capture");
        PF.update("form_photo_capture");
    }

    public static String getSavePath() {
        return savePath;
    }

    public static void setSavePath(String aSavePath) {
        savePath = aSavePath;
    }

    public static String getNameFile() {
        return nameFile;
    }

    public static void setNameFile(String aNameFile) {
        nameFile = aNameFile;
    }

    public static Pessoa getPessoa() {
        return pessoa;
    }

    public static void setPessoa(Pessoa aPessoa) {
        pessoa = aPessoa;
    }

    public void close() {
        renderedPhotoCapture = false;
        PF.closeDialog("dlg_photo_capture");
        PF.update("form_photo_capture");
    }

//    public static void load(String aUpdate, Pessoa aPessoa) {
//        update = aUpdate;
//        renderedPhotoCapture = true;
//
//        PF.openDialog("dlg_photo_capture");
//        PF.update("form_photo_capture");
//    }
    public static void unload() {
        savePath = "";
        update = "";
        nameFile = "";
        pessoa = null;
    }

    public static Boolean getRenderedPhotoCapture() {
        return renderedPhotoCapture;
    }

    public static void setRenderedPhotoCapture(Boolean aRenderedPhotoCapture) {
        renderedPhotoCapture = aRenderedPhotoCapture;
    }

    public static String getUpdate() {
        return update;
    }

    public static void setUpdate(String aUpdate) {
        update = aUpdate;
    }

    public Boolean getRenderedPhotoCaptureView() {
        return renderedPhotoCapture;
    }

    public void setRenderedPhotoCaptureView(Boolean aRenderedPhotoCaptureView) {
        renderedPhotoCapture = aRenderedPhotoCaptureView;
    }

    public String getUpdateView() {
        return update;
    }

    public void setUpdateView(String aUpdateView) {
        update = aUpdateView;
    }

    public String getNameFileView() {
        return nameFile;
    }

    public void setNameFileView(String aNameFileView) {
        nameFile = aNameFileView;
    }

    public void capturar(CaptureEvent captureEvent) throws FileNotFoundException {
//        if (!Diretorio.criar("imagens/temp/" + Usuario.getUsuario().getId(), true)) { // PASTA resources/cliente/sindical/imagens
//            return;
//        }

        UUID uuidX = UUID.randomUUID();
        String nameTemp = uuidX.toString().replace("-", "_");

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        byte[] data = captureEvent.getData();

        nameFile = nameTemp;

        if (pessoa == null) {
            String path = servletContext.getRealPath("")+"resources/cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/" + savePath + "/" + nameTemp + ".png";
            File file = new File(path);
            try {
                FileUtils.writeByteArrayToFile(file, data);
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            if (!Dirs.create("imagens/pessoa", true)) { // PASTA ex. resources/cliente/sindical/imagens/pessoa
                return;
            }
            String path = servletContext.getRealPath("")+"resources/cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + pessoa.getId() + "/" + nameTemp + ".png";
            File file = new File(path);
            try {
                FileUtils.writeByteArrayToFile(file, data);
            } catch (IOException e) {
                e.getMessage();
            }

            if (file.exists()) {
                FisicaDao fisicaDao = new FisicaDao();
                Fisica fisica = fisicaDao.pesquisaFisicaPorPessoa(pessoa.getId());

                if (fisica != null) {
                    // CASO QUEIRA REMOVER A FOTO ANTERIOR
//                    File fotoAntiga = new File(servletContext.getRealPath("")+"resources/cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + pessoa.getId() + "/" + fisica.getFoto() + ".png");
//                    if (fotoAntiga.exists()) {
//                        FileUtils.deleteQuietly(fotoAntiga);
//                    }

                    fisica.setFoto(nameTemp);
                    new Dao().update(fisica, true);
                } else {
                    JuridicaDao juridicaDao = new JuridicaDao();
                    Juridica juridica = juridicaDao.pesquisaJuridicaPorPessoa(pessoa.getId());

                    // CASO QUEIRA REMOVER A FOTO ANTERIOR
//                    File fotoAntiga = new File(servletContext.getRealPath("")+"resources/cliente/" + ControleUsuarioBean.getCliente().toLowerCase() + "/imagens/pessoa/" + pessoa.getId() + "/" + juridica.getFoto() + ".png");
//                    if (fotoAntiga.exists()) {
//                        FileUtils.deleteQuietly(fotoAntiga);
//                    }

                    juridica.setFoto(nameTemp);
                    new Dao().update(juridica, true);
                }
            }
        }
        
        PhotoUpload.unload();
        PhotoCropper.unload();
    }
}
