package br.com.clinicaintegrada.seguranca;

import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "seg_log")
public class Log implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cliente cliente;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_hora", length = 50, nullable = false)
    private String hora;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Usuario usuario;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "ds_conteudo_original", length = 1024, nullable = true)
    private String conteudoOriginal;
    @Column(name = "ds_conteudo_alterado", length = 1024, nullable = true)
    private String conteudoAlterado;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private SegEvento segEvento;

    public Log() {
        this.id = -1;
        this.cliente = new Cliente();
        this.dtData = new Date();
        this.hora = DataHoje.livre(new Date(), "HH:mm");
        this.usuario = new Usuario();
        this.rotina = new Rotina();
        this.conteudoOriginal = "";
        this.conteudoAlterado = "";
        this.segEvento = new SegEvento();
    }

    public Log(Integer id, Cliente cliente, Date dtData, String hora, Usuario usuario, Rotina rotina, String conteudoOriginal, String conteudoAlterado, SegEvento segEvento) {
        this.id = id;
        this.dtData = dtData;
        this.cliente = cliente;
        this.hora = hora;
        this.usuario = usuario;
        this.rotina = rotina;
        this.conteudoOriginal = conteudoOriginal;
        this.conteudoAlterado = conteudoAlterado;
        this.segEvento = segEvento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getData() {
        if (dtData != null) {
            return DataHoje.converteData(dtData);
        } else {
            return "";
        }
    }

    public void setData(String data) {
        if (!(data.isEmpty())) {
            this.dtData = DataHoje.converte(data);
        }
    }

    public String getConteudoOriginal() {
        return conteudoOriginal;
    }

    public void setConteudoOriginal(String conteudoOriginal) {
        this.conteudoOriginal = conteudoOriginal;
    }

    public String getConteudoAlterado() {
        return conteudoAlterado;
    }

    public void setConteudoAlterado(String conteudoAlterado) {
        this.conteudoAlterado = conteudoAlterado;
    }

    public SegEvento getSegEvento() {
        return segEvento;
    }

    public void setSegEvento(SegEvento segEvento) {
        this.segEvento = segEvento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
