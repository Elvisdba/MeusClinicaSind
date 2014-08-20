package br.com.rtools.seguranca;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_registro",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente"})
)
public class Registro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @Column(name = "sis_ds_email", length = 50)
    private String sisEmail;
    @Column(name = "sis_ds_senha", length = 20)
    private String sisSenha;
    @Column(name = "sis_ds_smtp", length = 50)
    private String sisSmtp;
    @Column(name = "sis_ds_url_path", length = 50)
    private String sisUrlPath;
    @Column(name = "sis_is_enviar_email_anexo")
    private boolean enviarEmailAnexo;
    @Column(name = "sis_email_resposta", length = 50)
    private String sisEmailResposta;
    @Column(name = "sis_email_porta")
    private int sisEmailPorta;
    @JoinColumn(name = "id_email_protocolo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SisEmailProtocolo sisEmailProtocolo;
    @Column(name = "sis_is_email_marketing", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sisEmailMarketing;
    @Column(name = "sis_is_email_autenticado", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sisEmailAutenticado;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica filial;

    public Registro() {
        this.id = -1;
        this.cliente = new Cliente();
        this.sisEmail = "";
        this.sisSenha = "";
        this.sisSmtp = "";
        this.sisUrlPath = "";
        this.enviarEmailAnexo = false;
        this.sisEmailResposta = "";
        this.sisEmailPorta = 0;
        this.sisEmailProtocolo = new SisEmailProtocolo();
        this.sisEmailMarketing = false;
        this.sisEmailAutenticado = false;
        this.filial = new Juridica();
    }

    public Registro(int id, Cliente cliente, String sisEmail, String sisSenha, String sisSmtp, String sisUrlPath, boolean enviarEmailAnexo, String sisEmailResposta, int sisEmailPorta, SisEmailProtocolo sisEmailProtocolo, boolean sisEmailMarketing, boolean sisEmailAutenticado, Juridica filial) {
        this.id = id;
        this.cliente = cliente;
        this.sisEmail = sisEmail;
        this.sisSenha = sisSenha;
        this.sisSmtp = sisSmtp;
        this.sisUrlPath = sisUrlPath;
        this.enviarEmailAnexo = enviarEmailAnexo;
        this.sisEmailResposta = sisEmailResposta;
        this.sisEmailPorta = sisEmailPorta;
        this.sisEmailProtocolo = sisEmailProtocolo;
        this.sisEmailMarketing = sisEmailMarketing;
        this.sisEmailAutenticado = sisEmailAutenticado;
        this.filial = filial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getSisEmail() {
        return sisEmail;
    }

    public void setSisEmail(String sisEmail) {
        this.sisEmail = sisEmail;
    }

    public String getSisSenha() {
        return sisSenha;
    }

    public void setSisSenha(String sisSenha) {
        this.sisSenha = sisSenha;
    }

    public String getSisSmtp() {
        return sisSmtp;
    }

    public void setSisSmtp(String sisSmtp) {
        this.sisSmtp = sisSmtp;
    }

    public String getSisUrlPath() {
        return sisUrlPath;
    }

    public void setSisUrlPath(String sisUrlPath) {
        this.sisUrlPath = sisUrlPath;
    }

    public boolean isEnviarEmailAnexo() {
        return enviarEmailAnexo;
    }

    public void setEnviarEmailAnexo(boolean enviarEmailAnexo) {
        this.enviarEmailAnexo = enviarEmailAnexo;
    }

    public String getSisEmailResposta() {
        return sisEmailResposta;
    }

    public void setSisEmailResposta(String sisEmailResposta) {
        this.sisEmailResposta = sisEmailResposta;
    }

    public int getSisEmailPorta() {
        return sisEmailPorta;
    }

    public void setSisEmailPorta(int sisEmailPorta) {
        this.sisEmailPorta = sisEmailPorta;
    }

    public SisEmailProtocolo getSisEmailProtocolo() {
        return sisEmailProtocolo;
    }

    public void setSisEmailProtocolo(SisEmailProtocolo sisEmailProtocolo) {
        this.sisEmailProtocolo = sisEmailProtocolo;
    }

    public boolean isSisEmailMarketing() {
        return sisEmailMarketing;
    }

    public void setSisEmailMarketing(boolean sisEmailMarketing) {
        this.sisEmailMarketing = sisEmailMarketing;
    }

    public boolean isSisEmailAutenticado() {
        return sisEmailAutenticado;
    }

    public void setSisEmailAutenticado(boolean sisEmailAutenticado) {
        this.sisEmailAutenticado = sisEmailAutenticado;
    }

    public Juridica getFilial() {
        return filial;
    }

    public void setFilial(Juridica filial) {
        this.filial = filial;
    }
}
