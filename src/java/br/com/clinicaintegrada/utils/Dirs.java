package br.com.clinicaintegrada.utils;

import br.com.clinicaintegrada.seguranca.controleUsuario.SessaoCliente;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

@SuppressWarnings("unchecked")
public class Dirs {

    private final static String cliente = "";

    public static boolean create(String diretorio) {
        return create(diretorio, true);
    }

    public static boolean create(String diretorio, boolean ignore) {
        if (!ignore) {
            diretorio = "/Cliente/" + getCliente() + "/" + diretorio;
        } else {
            diretorio = "/resources/cliente/" + getCliente().toLowerCase() + "/" + diretorio.toLowerCase();
        }
        try {
            String s[] = diretorio.split("/");
            boolean err = false;
            String pathContac = "";
            int b = 0;
            String path;
            for (String item : s) {
                if (!item.equals("")) {
                    if (b == 0) {
                        pathContac = item;
                        path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/" + pathContac);
                    } else {
                        pathContac = "/" + pathContac + "/" + item;
                        path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(pathContac);
                    }
                    if (!new File(path).exists()) {
                        File file = new File(path);
                        if (!file.mkdir()) {
                            err = false;
                            break;
                        }
                    }
                    b++;
                }
            }
            return !err;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean remove(String dir) {
        if (new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + dir)).exists()) {
            File file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + dir));
            if (file.delete()) {
                return true;
            }
        }
        return false;
    }

    public static List listFiles(String dir) {
        List listaArquivos = new ArrayList();
        String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + dir);
        try {
            File files = new File(path);
            if (!files.exists()) {
                return new ArrayList();
            }
            File listFile[] = files.listFiles();
            int numArq = listFile.length;
            int i = 0;
            while (i < numArq) {
                listaArquivos.add(new ParametroDiretorio(listFile[i], listFile[i].getName(), i));
                i++;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return listaArquivos;
    }

    public static String files(String dir, String file) {
        if (file.isEmpty()) {
            return null;
        }
        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/" + dir + "/" + file);
        try {
            File files = new File(caminho);
            if (!files.exists()) {
                return null;
            }
            return caminho;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getCliente() {
        if (cliente.equals("")) {
            if (Sessions.exists("sessaoCliente")) {
                return SessaoCliente.get().getIdentifica();
            }
        }
        return cliente;
    }

}
