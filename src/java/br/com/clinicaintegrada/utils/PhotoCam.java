package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.coordenacao.beans.AgendamentoBean;
import br.com.clinicaintegrada.pessoa.Fisica;
import br.com.clinicaintegrada.pessoa.Fotos;
import br.com.clinicaintegrada.pessoa.dao.FisicaDao;
import br.com.clinicaintegrada.pessoa.dao.FotosDao;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Usuario;
import static com.google.common.io.Files.copy;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.ServletContext;
import org.primefaces.event.CaptureEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "photoCamBean")
@SessionScoped
public class PhotoCam implements Serializable {

    private static String PATH;
    private static String PATH_FILE;
    private static String FILE_TEMP;
    private static String FILE_TEMP_NAME;
    private static String FILE_PERMANENT;
    private static String FILE_MEMORY;
    private static Boolean REPLACE_FILES;
    private static Boolean AUTO_SAVE;
    private static List UPDATES;
    private static String UPDATE;
    private static Boolean SHOW_MESSAGE;
    private static Boolean SUCCESS;
    private static String DEFAULT_EXTENSION;

    private Boolean load;
    private String rotinaNome;
    private Boolean visible;
    private Integer stop;
    private StreamedContent streamedContent;

    @PostConstruct
    public void init() {
        streamedContent = null;
        load = false;
        PATH = "temp/foto/" + getUsuario().getId();
        PATH_FILE = "perfil";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "/imagens/user_undefined.png";
        REPLACE_FILES = false;
        UPDATES = new ArrayList();
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        SUCCESS = false;
        rotinaNome = "";
        UPDATE = "";
        visible = false;
        stop = 0;
        FILE_MEMORY = "";
        DEFAULT_EXTENSION = "";
//        deleteMemoryFile();
    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("photoCamBean");
        Sessions.remove("cropperBean");
    }

    public void load() {

    }

    /**
     *
     * @param path_temp
     * @param filename_temp
     */
    public void load(String path_temp, String filename_temp) {
        PATH = path_temp + "/" + getUsuario().getId() + "/";
        PATH_FILE = filename_temp;
        FILE_TEMP_NAME = filename_temp;
    }

    /**
     *
     * @param replace_file
     */
    public void listenerTemp(boolean replace_file) {
        REPLACE_FILES = replace_file;
    }

    public void listener(String aPATH, String aFILENAME, Boolean replace_file, Boolean autosave) throws FileNotFoundException {
        listener(aPATH, aFILENAME, replace_file, autosave, "");

    }

    /**
     * @param aPATH
     * @param aFILENAME
     * @param replace_file
     * @param autosave
     * @param update
     */
    public void listener(String aPATH, String aFILENAME, Boolean replace_file, Boolean autosave, String update) throws FileNotFoundException {
        String[] split = null;
        if (!update.isEmpty()) {
            split = update.split(",");
            for (String split1 : split) {
                UPDATES.add(split1);
            }
        }
        FILE_PERMANENT = "/imagens/user_undefined.png";
        UPDATE = update.trim();
        AUTO_SAVE = autosave;
        PATH = aPATH;
        aFILENAME = aFILENAME + ".png";
        REPLACE_FILES = replace_file;
        PATH_FILE = aFILENAME;
        SHOW_MESSAGE = true;
        visible = true;
        stop = 4;
        InputStream stream = new FileInputStream(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/imagens/user_undefined.png")));
        streamedContent = new DefaultStreamedContent(stream, "image/png");
        // FILE_MEMORY = "";
        // deleteMemoryFile();

    }

    public static void oncapture(CaptureEvent captureEvent, String photo) {
        oncapture(captureEvent, photo, "");
    }

    public static synchronized void oncapture(CaptureEvent captureEvent, String photo, String caminhoTemporario) {
        oncapture(captureEvent, photo, "", false);
    }

    public static synchronized boolean oncapture(CaptureEvent captureEvent, String photo, String aPATH, Boolean create_dirs) {
        if (photo.equals("")) {
            Date date = new Date();
            photo = date.toGMTString();
        }
        String caminho;
        if (!PATH.isEmpty()) {
            caminho = PATH;
        } else {
            caminho = aPATH;
        }
        if (create_dirs) {
            Dirs.create(caminho);
        }
        byte[] data = captureEvent.getData();
        if (photo.isEmpty()) {
            photo = PATH_FILE;
        }
        String file_path_local;
        if (photo.contains("png")) {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo;
        } else {
            file_path_local = "/Cliente/" + getCliente() + "/" + caminho + "/" + photo + ".png";
        }
//        if (FILE_MEMORY.isEmpty()) {
//            if (f.exists()) {
//                Diretorio.criar("/temp/foto/" + getUsuarioId() + "/memory/");
//                FILE_MEMORY = "/Cliente/" + getCliente() + "/temp/foto/" + getUsuarioId() + "/memory/" + photo;
//                File fileMemory = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_MEMORY));
//                if (fileMemory.exists()) {
//                    success = fileMemory.delete();
//                } else {
//                    success = true;
//                }
//                if (success) {
//                    File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(file_path_local));
//                    try {
//                        success = src.renameTo(fileMemory);
//                    } catch (Exception e) {
//                        e.getMessage();
//                    }
//                }
//            }
//        }
        String newFileName = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(file_path_local);
        File f = new File(newFileName);
        boolean success = false;
        FileImageOutputStream imageOutput;
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            imageOutput = new FileImageOutputStream(f);
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
            FILE_TEMP = newFileName;
            FILE_TEMP_NAME = file_path_local;
            SUCCESS = true;
            return true;
        } catch (IOException e) {
            //throw new FacesException("Erro ao capturar imagem!");
            return false;
        }
    }

    public synchronized String capture(CaptureEvent captureEvent) throws InterruptedException, FileNotFoundException {
        if (PhotoCam.oncapture(captureEvent, PATH_FILE, "", true)) {
            load = true;
            complete();
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + PATH + "/" + PATH_FILE));
            int i = 0;
            FILE_PERMANENT = "/Cliente/" + getCliente() + "/" + PATH + "/" + PATH_FILE;
            InputStream stream = new FileInputStream(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_PERMANENT)));
            streamedContent = new DefaultStreamedContent(stream, "image/png");
            if (!f.exists()) {
                while (!f.exists()) {
                    Thread.sleep(1000);
                    if (i == 10) {
                        stream = new FileInputStream(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("imagens/user_undefined.png")));
                        streamedContent = new DefaultStreamedContent(stream, "image/png");
                        FILE_PERMANENT = "/imagens/user_undefined.png";
                        break;
                    }
                    i++;
                }
            }
        }
        return null;
//        RequestContext.getCurrentInstance().update(":form_pessoa_fisica");
//        RequestContext.getCurrentInstance().execute("dgl_captura.hide();");
    }

    public Usuario getUsuario() {
        if (Sessions.exists("sessaoUsuario")) {
            return (Usuario) Sessions.getObject("sessaoUsuario");
        }
        return new Usuario();
    }

    public static Integer getUsuarioId() {
        if (Sessions.exists("sessaoUsuario")) {
            return ((Usuario) Sessions.getObject("sessaoUsuario")).getId();
        }
        return -1;
    }

    public static String getCliente() {
        if (Sessions.exists("sessaoCliente")) {
            return ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
        }
        return "";
    }

    public String getFILE_TEMP() {
        return FILE_TEMP;
    }

    public void setFILE_TEMP(String aFILE_TEMP) {
        FILE_TEMP = aFILE_TEMP;
    }

    public String getFILE_PERMANENT() {
        if (load != null && load) {
            try {
                // ERA 5000
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
            load = false;
        }
        return FILE_PERMANENT;
    }

    public void setFILE_PERMANENT(String aFILE_PERMANENT) {
        FILE_PERMANENT = aFILE_PERMANENT;
    }

    public Boolean getSUCCESS() {
        return SUCCESS;
    }

    public void setSUCCESS(Boolean aSUCCESS) {
        SUCCESS = aSUCCESS;
    }

    public String getRotinaNome() {
        return rotinaNome;
    }

    public void setRotinaNome(String rotinaNome) {
        this.rotinaNome = rotinaNome;
    }

    public String getPATH() {
        return PATH;
    }

    public void setPATH(String aPATH) {
        PATH = aPATH;
    }

    public void complete() throws FileNotFoundException {
        if (SUCCESS) {
            Fisica f;
            Dao dao = new Dao();
            switch (rotinaNome) {
                case "pessoaFisica":
                    try {
                        Integer id = Integer.parseInt(PATH_FILE.replace(".png", ""));
                        if (id != -1) {
                            FisicaDao fisicaDao = new FisicaDao();
                            f = fisicaDao.pesquisaFisicaPorPessoa(id);
                            f.setDtFoto(DataHoje.dataHoje());
                            if (!dao.update(f, true)) {
                                SUCCESS = false;
                            }
                        }
                    } catch (Exception e) {

                    }
                    break;
                case "agendamento":
                    try {
                        Integer id = Integer.parseInt(PATH_FILE.replace(".png", ""));
                        Fotos fotos = new Fotos();
                        fotos.setId(null);
                        Contrato c = ((AgendamentoBean) Sessions.getObject("agendamentoBean")).getContrato();
                        fotos.setContrato(c);
                        fotos.setDataString(DataHoje.data());
                        FotosDao fotosDao = new FotosDao();
                        if (fotosDao.findFotosByContratoData(fotos.getContrato().getId(), DataHoje.data()).isEmpty()) {
                            if (dao.save(fotos, true)) {
                                streamedContent = null;
                                File file_origem = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + PATH + "/" + PATH_FILE));
                                File file_destino = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + PATH + "/" + fotos.getId() + ".png"));
                                PATH_FILE = fotos.getId() + ".png";
                                file_origem.renameTo(file_destino);
                                InputStream stream = new FileInputStream(file_destino);
                                streamedContent = new DefaultStreamedContent(stream, "image/png");
                                File fileFisica = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/fotos/" + c.getPaciente().getId() + ".png"));
                                if (!fileFisica.exists()) {
                                    copy(file_destino, fileFisica);
                                    if (fileFisica.exists()) {
                                        Fisica fisica = new FisicaDao().pesquisaFisicaPorPessoa(c.getPaciente().getId());
                                        if (fisica.getDtFoto() == null) {
                                            fisica.setDtFoto(DataHoje.dataHoje());
                                            dao.update(fisica, true);
                                        }
                                    }
                                }
                            } else {
                                // FILE_PERMANENT = "";
                            }
                        }
                    } catch (NumberFormatException | IOException e) {

                    }
                    break;
            }
        }
        SUCCESS = false;
    }

    public String getUPDATE() {
        return UPDATE;
    }

    public void setUPDATE(String aUPDATE) {
        UPDATE = aUPDATE;
    }

    public void waiting(Integer sleep) {
        try {
            Thread.sleep(sleep);
            PF.closeDialog("dlg_loading_photo");
        } catch (InterruptedException ex) {

        }
    }

    public void waiting() {
        waiting(5000);
    }

    public void close() {
        close(0);
    }

    public synchronized void close(Integer tCase) {
        streamedContent = null;
        Sessions.remove("cropperBean");
        SUCCESS = false;
        PATH = "temp/foto/" + getUsuario().getId();
        PATH_FILE = "perfil";
        FILE_TEMP = "";
        FILE_TEMP_NAME = "";
        FILE_PERMANENT = "/imagens/user_undefined.png";
        REPLACE_FILES = false;
        SHOW_MESSAGE = false;
        AUTO_SAVE = false;
        SUCCESS = false;
        UPDATES = new ArrayList();
        visible = false;
        stop = 0;
        if (tCase == 0) {
            deleteMemoryFile();
        } else if (tCase == 1) {
            moveMemoryFile();
        }
    }

    public void rollback() {
        moveMemoryFile();
    }

    public String closeRefresh(Integer tCase) {
        close(tCase);
        return rotinaNome;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void listenerStop() {
        if (stop > 0) {
            stop--;
        }
    }

    public Integer getStop() {
        return stop;
    }

    public void setStop(Integer stop) {
        this.stop = stop;
    }

    public String getFILE_MEMORY() {
        return FILE_MEMORY;
    }

    public void setFILE_MEMORY(String aFILE_MEMORY) {
        FILE_MEMORY = aFILE_MEMORY;
    }

    public void deleteMemoryFile() {
//        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_MEMORY));
//        if (f.exists()) {
//            f.delete();
//        }
//        FILE_MEMORY = "";
    }

    public void moveMemoryFile() {
//        File des = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_PERMANENT));
//        if (des.exists()) {
//            des.delete();
//        }
//        boolean rename = false;
//        if (!FILE_PERMANENT.equals("/Imagens/user_undefined.png")) {
//            File src = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_MEMORY));
//            try {
//                rename = src.renameTo(des);
//                FILE_MEMORY = "";
//            } catch (Exception e) {
//                rename = false;
//            }
//        }
    }

    public Boolean getLoad() {
        return load;
    }

    public void setLoad(Boolean load) {
        this.load = load;
    }

    public StreamedContent getStreamedContent() {
        try {
            InputStream stream = new FileInputStream(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(FILE_PERMANENT)));
            streamedContent = new DefaultStreamedContent(stream, "image/png");
        } catch (Exception ex) {
            Logger.getLogger(PhotoCam.class.getName()).log(Level.SEVERE, null, ex);
        }
        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public static String getDEFAULT_EXTENSION() {
        return DEFAULT_EXTENSION;
    }

    public static void setDEFAULT_EXTENSION(String aDEFAULT_EXTENSION) {
        DEFAULT_EXTENSION = aDEFAULT_EXTENSION;
    }

}
