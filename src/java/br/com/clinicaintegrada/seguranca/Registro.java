package br.com.clinicaintegrada.seguranca;

import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Juridica;
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
    private Integer id;
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
    private Boolean enviarEmailAnexo;
    @Column(name = "sis_email_resposta", length = 50)
    private String sisEmailResposta;
    @Column(name = "sis_email_porta")
    private int sisEmailPorta;
    @JoinColumn(name = "id_email_protocolo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SisEmailProtocolo sisEmailProtocolo;
    @Column(name = "sis_is_email_marketing", columnDefinition = "boolean default false")
    private Boolean sisEmailMarketing;
    @Column(name = "sis_is_email_autenticado", columnDefinition = "boolean default false")
    private Boolean sisEmailAutenticado;
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

    public Registro(Integer id, Cliente cliente, String sisEmail, String sisSenha, String sisSmtp, String sisUrlPath, Boolean enviarEmailAnexo, String sisEmailResposta, int sisEmailPorta, SisEmailProtocolo sisEmailProtocolo, Boolean sisEmailMarketing, Boolean sisEmailAutenticado, Juridica filial) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getEnviarEmailAnexo() {
        return enviarEmailAnexo;
    }

    public void setEnviarEmailAnexo(Boolean enviarEmailAnexo) {
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

    public Boolean getSisEmailMarketing() {
        return sisEmailMarketing;
    }

    public void setSisEmailMarketing(Boolean sisEmailMarketing) {
        this.sisEmailMarketing = sisEmailMarketing;
    }

    public Boolean getSisEmailAutenticado() {
        return sisEmailAutenticado;
    }

    public void setSisEmailAutenticado(Boolean sisEmailAutenticado) {
        this.sisEmailAutenticado = sisEmailAutenticado;
    }

    public Juridica getFilial() {
        return filial;
    }

    public void setFilial(Juridica filial) {
        this.filial = filial;
    }
}
