package br.com.clinicaintegrada.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.FacesContext;

public class Download {

    private final String filename;
    private final String fileLocation;
    private final String mimeType;
    private final FacesContext facesContext;
    private HttpServletResponse response;
    private File file;

    public Download(
            String filename,
            String fileLocation,
            String mimeType,
            FacesContext facesContext) {
        this.filename = filename;
        this.fileLocation = fileLocation;
        this.mimeType = mimeType;
        this.facesContext = facesContext;
    }

    public synchronized void open() {
        ExternalContext context = facesContext.getExternalContext();
        file = new File(fileLocation + "/" + filename);
        response = (HttpServletResponse) context.getResponse();
        response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + "\""); // SETA O HEADER COM O QUE VAI APARECER NA HORA DO DOWNLOAD
        response.setContentLength((int) file.length()); // TAMANHO DO ARQUIVO
        response.setContentType(mimeType); // O TIPO DO ARQUIVO
        try {
            OutputStream out;
            try (FileInputStream in = new FileInputStream(file)) {
                out = response.getOutputStream();
                byte[] buf = new byte[(int) file.length()];
                int count;
                while ((count = in.read(buf)) >= 0) {
                    out.write(buf, 0, count);
                }
            }
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException ex) {
            System.out.println("Error in downloadFile: " + ex.getMessage());
        }
    }

    public void close() {
        if (file.exists()) {
            file.delete();
        }
    }
}
