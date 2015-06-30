package br.com.clinicaintegrada.configuracao;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "conf_coordenacao")
public class ConfiguracaoCoordenacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne
    private Cliente cliente;
    @Temporal(TemporalType.DATE)
    @Column(name = "agendamento_data_retroativa")
    private Date agendamentoDataRetroativo;
    @Column(name = "agendamento_max_meses_agenda", columnDefinition = "integer default 6")
    private Integer agendamentoMaxMesesAgenda;
    @Temporal(TemporalType.DATE)
    @Column(name = "escala_data_retroativa")
    private Date escalaDataRetroativo;
    @Column(name = "escala_max_meses_escala", columnDefinition = "integer default 2")
    private Integer escalaMaxMesesEscala;

    public ConfiguracaoCoordenacao() {
        this.id = -1;
        this.cliente = new Cliente();
        this.agendamentoDataRetroativo = null;
        this.agendamentoMaxMesesAgenda = 0;
        this.escalaDataRetroativo = null;
        this.escalaMaxMesesEscala = 0;
    }

    public ConfiguracaoCoordenacao(Integer id, Cliente cliente, Date agendamentoDataRetroativo, Integer agendamentoMaxMesesAgenda, Date escalaDataRetroativo, Integer escalaMaxMesesEscala) {
        this.id = id;
        this.cliente = cliente;
        this.agendamentoDataRetroativo = agendamentoDataRetroativo;
        this.agendamentoMaxMesesAgenda = agendamentoMaxMesesAgenda;
        this.escalaDataRetroativo = escalaDataRetroativo;
        this.escalaMaxMesesEscala = escalaMaxMesesEscala;
    }

    public void selectedAgendamentoDataRetroativo(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.agendamentoDataRetroativo = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedEscalaDataRetroativo(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.escalaDataRetroativo = DataHoje.converte(format.format(event.getObject()));
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

    public Date getAgendamentoDataRetroativo() {
        return agendamentoDataRetroativo;
    }

    public void setAgendamentoDataRetroativo(Date agendamentoDataRetroativo) {
        this.agendamentoDataRetroativo = agendamentoDataRetroativo;
    }

    public Integer getAgendamentoMaxMesesAgenda() {
        return agendamentoMaxMesesAgenda;
    }

    public void setAgendamentoMaxMesesAgenda(Integer agendamentoMaxMesesAgenda) {
        this.agendamentoMaxMesesAgenda = agendamentoMaxMesesAgenda;
    }

    public Date getEscalaDataRetroativo() {
        return escalaDataRetroativo;
    }

    public void setEscalaDataRetroativo(Date escalaDataRetroativo) {
        this.escalaDataRetroativo = escalaDataRetroativo;
    }

    public Integer getEscalaMaxMesesEscala() {
        return escalaMaxMesesEscala;
    }

    public void setEscalaMaxMesesEscala(Integer escalaMaxMesesEscala) {
        this.escalaMaxMesesEscala = escalaMaxMesesEscala;
    }

}
