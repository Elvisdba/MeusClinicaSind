package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.logSistema.Logger;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.sistema.ConfiguracaoUpload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@SuppressWarnings("unchecked")
public class Upload {

    public static boolean enviar(ConfiguracaoUpload cu) {
        return enviar(cu, false);
    }

    public static boolean enviar(ConfiguracaoUpload cu, boolean criarDiretorios) {
        return enviar(cu, criarDiretorios, false);
    }

    public static boolean enviar(ConfiguracaoUpload cu, boolean criarDiretorios, boolean mensagens) {
        if (cu.getEvent().getFile().getFileName() == null) {
            return false;
        }
        String cliente = "";
        if (Sessions.exists("sessaoCliente")) {
            cliente = ((Cliente) Sessions.getObject("sessaoCliente")).getIdentifica();
            if (cliente.equals("")) {
                return false;
            }
        }
        String diretorio = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + cliente + "/" + cu.getDiretorio());
        if (criarDiretorios) {
            if (diretorio.equals("")) {
                return false;
            }
            Dirs.create(cu.getDiretorio());
        }
        try {
            if (!cu.getTiposPermitidos().isEmpty()) {
                for (int i = 0; i < cu.getTiposPermitidos().size(); i++) {
                    if (cu.getTiposPermitidos().get(i) != cu.getEvent().getFile().getContentType()) {
                        if (mensagens) {
                            Messages.warn("Validação", "Tipo de arquivo não permitido! Tipos permitidos: " + cu.getTiposPermitidos().get(i).toString());
                        }
                        return false;
                    }
                }
            }
            if (cu.getTamanhoMaximo() > 0) {
                if (cu.getEvent().getFile().getSize() <= cu.getTamanhoMaximo()) {
                    if (mensagens) {
                        Messages.warn("Validação", "Tamanho excedido. Tamanho máximo " + cu.getEvent().getFile().getSize());
                    }
                }
                return false;
            }
            List listContentType = new ArrayList();
            listContentType.add("PNG");
            listContentType.add("JPG");
            listContentType.add("JPEG");
            listContentType.add("GIF");
            for (Object listContentType1 : listContentType) {
                if (cu.getEvent().getFile().getContentType().toUpperCase() == listContentType1) {
                    if (cu.getLarguraMaxima() > 0) {
                        if (mensagens) {
                            Messages.warn("Validação", "Largura excedida. Largura máxima: " + cu.getEvent().getFile().getSize());
                        }
                        return false;
                    }
                    if (cu.getAlturaMaxima() > 0) {
                        if (mensagens) {
                            Messages.warn("Validação", "Altura excedida. Altura máxima: " + cu.getEvent().getFile().getSize());
                        }
                        return false;
                    }
                }
                break;
            }
            File file = new File(diretorio + "/" + cu.getEvent().getFile().getFileName());
            if (cu.isSubstituir()) {
                if (!cu.getRenomear().equals("")) {
                    File novoNome = new File(diretorio + "/" + cu.getRenomear());
                    if (novoNome.exists()) {
                        novoNome.delete();
                    } else {
                        file.delete();
                    }
                } else {
                    file.delete();
                }
            } else {
                if (file.exists()) {
                    if (mensagens) {
                        Messages.warn("Validação", "Arquivo já existe no caminho específicado!");
                    }
                    return false;
                }
            }
            InputStream in = cu.getEvent().getFile().getInputstream();
            FileOutputStream out = new FileOutputStream(file.getPath());
            byte[] buf = new byte[(int) cu.getEvent().getFile().getSize()];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            in.close();
            out.flush();
            out.close();
            if (!cu.getRenomear().equals("")) {
                File novoNome = new File(diretorio + "/" + cu.getRenomear());
                file.renameTo(novoNome);
            }
            return true;
        } catch (IOException e) {
            Logger logger = new Logger();
            if (mensagens) {
                Messages.warn("Erro", e.getMessage());
            }
            logger.novo("Upload de arquivos", e.getMessage());
            System.out.println(e);
            return false;
        }
    }
}
