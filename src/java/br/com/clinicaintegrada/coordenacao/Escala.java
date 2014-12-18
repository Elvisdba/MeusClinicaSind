package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_escala",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"dt_escala", "id_paciente", "ds_hora_inicial"}),
            @UniqueConstraint(columnNames = {"dt_escala", "id_equipe", "ds_hora_inicial"})
        }
)
public class Escala implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento", nullable = false)
    private Date dataLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_escala")
    private Date dataEscala;
    @Column(name = "ds_hora_inicial", length = 5)
    private String horaInicial;
    @Column(name = "ds_hora_final", length = 5)
    private String horaFinal;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id")
    @OneToOne
    private Contrato paciente;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id")
    @OneToOne
    private Equipe equipe;
    @JoinColumn(name = "id_funcao_escala", referencedColumnName = "id", nullable = false)
    @OneToOne
    private FuncaoEscala funcaoEscala;
    @Column(name = "ds_obs", length = 200)
    private String observacao;

    public Escala() {
        this.id = -1;
        this.dataLancamento = new Date();
        this.dataEscala = null;
        this.horaInicial = "";
        this.horaFinal = "";
        this.paciente = null;
        this.equipe = null;
        this.funcaoEscala = null;
        this.observacao = "";
    }

    public Escala(Integer id, Date dataLancamento, Date dataEscala, String horaInicial, String horaFinal, Contrato paciente, Equipe equipe, FuncaoEscala funcaoEscala, String observacao) {
        this.id = id;
        this.dataLancamento = dataLancamento;
        this.dataEscala = dataEscala;
        this.horaInicial = horaInicial;
        this.horaFinal = horaFinal;
        this.paciente = paciente;
        this.equipe = equipe;
        this.funcaoEscala = funcaoEscala;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public void setDataLancamentoString(String dataLancamentoString) {
        this.dataLancamento = DataHoje.converte(dataLancamentoString);
    }

    public Date getDataEscala() {
        return dataEscala;
    }

    public void setDataEscala(Date dataEscala) {
        this.dataEscala = dataEscala;
    }

    public String getDataEscalaString() {
        return DataHoje.converteData(dataEscala);
    }

    public void setDataEscalaString(String dataEscalaString) {
        this.dataEscala = DataHoje.converte(dataEscalaString);
    }

    public String getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public Contrato getPaciente() {
        return paciente;
    }

    public void setPaciente(Contrato paciente) {
        this.paciente = paciente;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public FuncaoEscala getFuncaoEscala() {
        return funcaoEscala;
    }

    public void setFuncaoEscala(FuncaoEscala funcaoEscala) {
        this.funcaoEscala = funcaoEscala;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @Override
    public String toString() {
        return "Escala{" + "id=" + id + ", dataLancamento=" + dataLancamento + ", dataEscala=" + dataEscala + ", horaInicial=" + horaInicial + ", horaFinal=" + horaFinal + ", paciente=" + paciente + ", equipe=" + equipe + ", funcaoEscala=" + funcaoEscala + ", observacao=" + observacao + '}';
    }

    public void selectedDataLancamento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataLancamento = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataEscala(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataEscala = DataHoje.converte(format.format(event.getObject()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Escala other = (Escala) obj;
        return true;
    }

}
