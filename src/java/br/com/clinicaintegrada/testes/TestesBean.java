package br.com.clinicaintegrada.testes;

import br.com.clinicaintegrada.pessoa.beans.*;
import br.com.clinicaintegrada.utils.Sessions;
import br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean;
import static br.com.clinicaintegrada.seguranca.controleUsuario.ControleUsuarioBean.getCliente;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;

@ManagedBean
@SessionScoped
public class TestesBean extends PesquisarProfissaoBean implements Serializable {

    private String imagem;

    @PostConstruct
    public void init() {

    }

    @PreDestroy
    public void destroy() {
        Sessions.remove("fisicaBean");
        Sessions.remove("fisicaPesquisa");
        Sessions.remove("juridicaPesquisa");
        Sessions.remove("enderecoPesquisa");
        Sessions.remove("remove_ids");
        Sessions.remove("validacao_tipo");
        Sessions.remove("uploadBean");
        Sessions.remove("photocamBean");

    }

    public void clear() {
        Sessions.remove("fisicaBean");
        clear(0);

    }

    public void clear(Integer tCase) {
        if (tCase == 0) {
            try {
                Sessions.remove("cropperBean");
                Sessions.remove("uploadBean");
                Sessions.remove("photoCamBean");
                FileUtils.deleteDirectory(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + "/Cliente/" + getCliente() + "/temp/" + "foto/testes/"));
                File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/testes/" + -1 + ".png"));
                if (f.exists()) {
                    f.delete();
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(TestesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void apagarImagem() {
        boolean sucesso = false;
        String extension = "";
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                extension = "png";
            }
            if (i == 1) {
                extension = "jpg";
            }
            File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + getCliente() + "/imagens/testes/" + "teste" + "." + extension));
            sucesso = f.delete();
            if (sucesso) {
                imagem = "";
                RequestContext.getCurrentInstance().update(":form_testes");
                break;
            }
        }
    }

    /**
     * Retorna foto
     *
     * @param filename
     * @return
     */
    public String getImagem(String filename) {
        return getImagem(filename, 0);
    }

    /**
     * Retorna foto
     *
     * @param filename
     * @param waiting
     * @return
     */
    public String getImagem(String filename, Integer waiting) {
        if (waiting > 0) {
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException ex) {
            }
        }
        String foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/testes/" + filename + ".png";
        File f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
        if (!f.exists()) {
            foto = "/Cliente/" + ControleUsuarioBean.getCliente() + "/imagens/testes/" + filename + ".jpg";
            f = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath(foto));
            if (!f.exists()) {
                foto = "/imagens/user_male.png";
            }
        }
        return foto;
    }

    public String getPath() {
        return "imagens/testes";
    }

}
