package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.FuncaoEquipe;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_agendamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contrato", "dt_agenda", "ds_hora_agenda"})
)
public class Agendamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @JoinColumn(name = "id_agendador", referencedColumnName = "id")
    @OneToOne
    private Usuario agendador;
    @JoinColumn(name = "id_funcao_equipe", referencedColumnName = "id")
    @OneToOne
    private FuncaoEquipe funcaoEquipe;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Evento evento;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id")
    @OneToOne
    private Equipe equipe;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @OneToOne
    private Status status;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_agenda", nullable = false)
    private Date dataAgenda;
    @Column(name = "ds_hora_agenda", nullable = false, length = 5)
    private String horaAgenda;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atendimento")
    private Date dataAtendimento;
    @Column(name = "ds_hora_atendimento", length = 5)
    private String horaAtendimento;
    @Column(name = "ds_obs", length = 200)
    private String observacao;

    public Agendamento() {
        this.id = -1;
        this.contrato = new Contrato();
        this.agendador = null;
        this.funcaoEquipe = null;
        this.evento = new Evento();
        this.equipe = null;
        this.status = null;
        this.dataLancamento = new Date();
        this.dataAgenda = new Date();
        this.horaAgenda = "";
        this.observacao = "";
        this.dataAtendimento = null;
        this.horaAtendimento = "";
    }

    public Agendamento(Integer id, Contrato contrato, Usuario agendador, FuncaoEquipe funcaoEquipe, Evento evento, Equipe equipe, Status status, Date lancamento, Date agenda, String horaAgenda, String observacao, Date atendimento, String horaAtendimento) {
        this.id = id;
        this.contrato = contrato;
        this.agendador = agendador;
        this.funcaoEquipe = funcaoEquipe;
        this.evento = evento;
        this.equipe = equipe;
        this.status = status;
        this.dataLancamento = lancamento;
        this.dataAgenda = agenda;
        this.horaAgenda = horaAgenda;
        this.observacao = observacao;
        this.dataAtendimento = atendimento;
        this.horaAtendimento = horaAtendimento;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Usuario getAgendador() {
        return agendador;
    }

    public void setAgendador(Usuario agendador) {
        this.agendador = agendador;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getDataLancamentoString() {
        return DataHoje.converteData(dataLancamento);
    }

    public void setDataLancamentoString(String lancamentoString) {
        this.dataLancamento = DataHoje.converte(lancamentoString);
    }

    public Date getDataAgenda() {
        return dataAgenda;
    }

    public void setDataAgenda(Date dataAgenda) {
        this.dataAgenda = dataAgenda;
    }

    public String getDataAgendaString() {
        return DataHoje.converteData(dataAgenda);
    }

    public void setDataAgendaString(String agendaString) {
        this.dataAgenda = DataHoje.converte(agendaString);
    }

    public String getHoraAgenda() {
        return horaAgenda;
    }

    public void setHoraAgenda(String horaAgenda) {
        this.horaAgenda = horaAgenda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(Date dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getHoraAtendimento() {
        return horaAtendimento;
    }

    public void setHoraAtendimento(String horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
    }

    public String getDataAtendimentoString() {
        return DataHoje.converteData(dataAtendimento);
    }

    public void setDataAtendimentoString(String dataAtendimentoString) {
        this.dataAtendimento = DataHoje.converte(dataAtendimentoString);
    }

    public FuncaoEquipe getFuncaoEquipe() {
        return funcaoEquipe;
    }

    public void setFuncaoEquipe(FuncaoEquipe funcaoEquipe) {
        this.funcaoEquipe = funcaoEquipe;
    }

    public void selectedDataLancamento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataLancamento = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataAgenda(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataAgenda = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataAtendimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataAtendimento = DataHoje.converte(format.format(event.getObject()));
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Agendamento other = (Agendamento) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Agendamento{" + "id=" + id + ", contrato=" + contrato + ", agendador=" + agendador + ", funcaoEquipe=" + funcaoEquipe + ", evento=" + evento + ", dataLancamento=" + dataLancamento + ", dataAgenda=" + dataAgenda + ", horaAgenda=" + horaAgenda + ", dataAtendimento=" + dataAtendimento + ", horaAtendimento=" + horaAtendimento + ", observacao=" + observacao + '}';
    }

}
