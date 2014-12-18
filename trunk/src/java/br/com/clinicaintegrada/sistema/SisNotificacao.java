package br.com.clinicaintegrada.sistema;

import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "sis_notificacao")
public class SisNotificacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Cliente cliente;
    @JoinColumn(name = "id_tipo_notificacao", referencedColumnName = "id", nullable = true)
    @OneToOne
    private TipoSisNotificacao tipoSisNotificacao;
    @JoinColumn(name = "id_remetente", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Pessoa remetente;
    @JoinColumn(name = "id_destinatario", referencedColumnName = "id")
    @OneToOne
    private Pessoa destinatario;
    @Column(name = "ds_descricao", length = 255, nullable = true)
    private String descricao;
    @Column(name = "ds_link", length = 255, nullable = true)
    private String link;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date lancamento;
    @Column(name = "ds_hora_lancamento")
    private String horaLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_visualizado")
    private Date visualizado;
    @Column(name = "ds_hora_visualizado")
    private String horaVisualizado;

    public SisNotificacao() {
        this.id = null;
        this.cliente = null;
        this.tipoSisNotificacao = null;
        this.remetente = null;
        this.destinatario = null;
        this.descricao = null;
        this.link = "";
        this.lancamento = DataHoje.dataHoje();
        this.horaLancamento = DataHoje.livre(new Date(), "HH:mm");
        this.visualizado = null;
        this.horaVisualizado = null;
    }

    public SisNotificacao(Integer id, Cliente cliente, TipoSisNotificacao tipoSisNotificacao, Pessoa remetente, Pessoa destinatario, String descricao, String link, Date lancamento, String horaLancamento, Date visualizado, String horaVisualizado) {
        this.id = id;
        this.cliente = cliente;
        this.tipoSisNotificacao = tipoSisNotificacao;
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.descricao = descricao;
        this.link = link;
        this.lancamento = lancamento;
        this.horaLancamento = horaLancamento;
        this.visualizado = visualizado;
        this.horaVisualizado = horaVisualizado;
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

    public TipoSisNotificacao getTipoSisNotificacao() {
        return tipoSisNotificacao;
    }

    public void setTipoSisNotificacao(TipoSisNotificacao tipoSisNotificacao) {
        this.tipoSisNotificacao = tipoSisNotificacao;
    }

    public Pessoa getRemetente() {
        return remetente;
    }

    public void setRemetente(Pessoa remetente) {
        this.remetente = remetente;
    }

    public Pessoa getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Pessoa destinatario) {
        this.destinatario = destinatario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getLancamento() {
        return lancamento;
    }

    public void setLancamento(Date lancamento) {
        this.lancamento = lancamento;
    }

    public String getHoraLancamento() {
        return horaLancamento;
    }

    public void setHoraLancamento(String horaLancamento) {
        this.horaLancamento = horaLancamento;
    }

    public Date getVisualizado() {
        return visualizado;
    }

    public void setVisualizado(Date visualizado) {
        this.visualizado = visualizado;
    }

    public String getHoraVisualizado() {
        return horaVisualizado;
    }

    public void setHoraVisualizado(String horaVisualizado) {
        this.horaVisualizado = horaVisualizado;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SisNotificacao other = (SisNotificacao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "SisNotificacao{" + "id=" + id + ", cliente=" + cliente + ", tipoSisNotificacao=" + tipoSisNotificacao + ", remetente=" + remetente + ", destinatario=" + destinatario + ", descricao=" + descricao + ", link=" + link + ", lancamento=" + lancamento + ", horaLancamento=" + horaLancamento + ", visualizado=" + visualizado + ", horaVisualizado=" + horaVisualizado + '}';
    }

}
