package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_REGISTRO")
public class Registro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Cliente cliente;
    @Column(name = "SIS_DS_EMAIL", length = 50)
    private String sisEmail;
    @Column(name = "SIS_DS_SENHA", length = 20)
    private String sisSenha;
    @Column(name = "SIS_DS_SMTP", length = 50)
    private String sisSmtp;
    @Column(name = "SIS_DS_URL_PATH", length = 50)
    private String sisUrlPath;
    @Column(name = "SIS_IS_ENVIAR_EMAIL_ANEXO")
    private boolean enviarEmailAnexo;
    @Column(name = "SIS_EMAIL_RESPOSTA", length = 50)
    private String sisEmailResposta;
    @Column(name = "SIS_EMAIL_PORTA")
    private int sisEmailPorta;
    @JoinColumn(name = "ID_EMAIL_PROTOCOLO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private SisEmailProtocolo sisEmailProtocolo;
    @Column(name = "SIS_IS_EMAIL_MARKETING", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sisEmailMarketing;

    public Registro() {
        this.id = 1;
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
    }

    public Registro(int id, Cliente cliente, String sisEmail, String sisSenha, String sisSmtp, String sisUrlPath, boolean enviarEmailAnexo, String sisEmailResposta, int sisEmailPorta, SisEmailProtocolo sisEmailProtocolo, boolean sisEmailMarketing) {
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
}
