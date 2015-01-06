package br.com.clinicaintegrada.fichamedica;

import br.com.clinicaintegrada.coordenacao.Agendamento;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ate_enfermaria")
public class Enfermaria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_equipe", referencedColumnName = "id", unique = true, nullable = false)
    @OneToOne
    private Equipe equipe;
    @JoinColumn(name = "id_agendamento", referencedColumnName = "id", unique = true, nullable = false)
    @OneToOne
    private Agendamento agendamento;
    @Column(name = "ds_hora", nullable = false, length = 5)
    private String hora;
    @Column(name = "nr_peso", columnDefinition = "double precision default 0", precision = 3)
    private Float peso;
    @Column(name = "nr_altura", columnDefinition = "double precision default 0", precision = 2)
    private Float altura;
    @Column(name = "nr_pressao_arterial_minima", columnDefinition = "double precision default 0", precision = 3)
    private Float pressaoArterialMinima;
    @Column(name = "nr_pressao_arterial_maxima", columnDefinition = "double precision default 0", precision = 3)
    private Float pressaoArterialMaxima;
    @Column(name = "nr_frequencia_cardiaca", columnDefinition = "integer default 0")
    private Integer frequenciaCardiaca;
    @Column(name = "nr_frequencia_respiratoria", columnDefinition = "integer default 0", precision = 2)
    private Integer frequenciaRespiratoria;
    @Column(name = "nr_temperatura", columnDefinition = "double precision default 0", precision = 2)
    private Float temperatura;
    @Column(name = "nr_satuaracao", columnDefinition = "double precision default 0", precision = 2)
    private Float saturacao;
    @Column(name = "nr_glicosimetria", columnDefinition = "double precision default 0", precision = 2)
    private Float glicosimetria;
    @Column(name = "ds_obs", length = 500)
    private String observacao;

    public Enfermaria() {
        this.id = null;
        this.equipe = null;
        this.agendamento = null;
        this.hora = "";
        this.peso = new Float(0);
        this.altura = new Float(0);
        this.pressaoArterialMinima = new Float(0);
        this.pressaoArterialMaxima = new Float(0);
        this.frequenciaCardiaca = 0;
        this.frequenciaRespiratoria = 0;
        this.temperatura = new Float(0);
        this.saturacao = new Float(0);
        this.glicosimetria = new Float(0);
        this.observacao = "";
    }

    public Enfermaria(Integer id, Equipe equipe, Agendamento agendamento, String hora, Float peso, Float altura, Float pressaoArterialMinima, Float pressaoArterialMaxima, Integer frequenciaCardiaca, Integer frequenciaRespiratoria, Float temperatura, Float saturacao, Float glicosimetria, String observacao) {
        this.id = id;
        this.equipe = equipe;
        this.agendamento = agendamento;
        this.hora = hora;
        this.peso = peso;
        this.altura = altura;
        this.pressaoArterialMinima = pressaoArterialMinima;
        this.pressaoArterialMaxima = pressaoArterialMaxima;
        this.frequenciaCardiaca = frequenciaCardiaca;
        this.frequenciaRespiratoria = frequenciaRespiratoria;
        this.temperatura = temperatura;
        this.saturacao = saturacao;
        this.glicosimetria = glicosimetria;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public String getPesoString() {
        return Moeda.converteR$Float(peso);
    }

    public void setPesoString(String pesoString) {
        this.peso = Moeda.converteUS$(pesoString);
    }

    public Float getAltura() {
        return altura;
    }

    public void setAltura(Float altura) {
        this.altura = altura;
    }

    public String getAlturaString() {
        return Moeda.converteR$Float(altura);
    }

    public void setAlturaString(String alturaString) {
        this.altura = Moeda.converteUS$(alturaString);
    }

    public Float getPressaoArterialMinima() {
        return pressaoArterialMinima;
    }

    public void setPressaoArterialMinima(Float pressaoArterialMinima) {
        this.pressaoArterialMinima = pressaoArterialMinima;
    }

    public String getPressaoArterialMinimaString() {
        return Moeda.substituiVirgula(Moeda.converteR$Float(pressaoArterialMinima));
    }

    public void setPressaoArterialMinimaString(String pressaoArterialMinimaString) {
        this.pressaoArterialMinima = Moeda.converteUS$(pressaoArterialMinimaString);
    }

    public Float getPressaoArterialMaxima() {
        return pressaoArterialMaxima;
    }

    public void setPressaoArterialMaxima(Float pressaoArterialMaxima) {
        this.pressaoArterialMaxima = pressaoArterialMaxima;
    }

    public String getPressaoArteriaMaximaString() {
        return Moeda.substituiVirgula(Moeda.converteR$Float(pressaoArterialMaxima));
    }

    public void setPressaoArterialMaximaString(String pressaoArterialMaximaString) {
        this.pressaoArterialMaxima = Moeda.converteUS$(pressaoArterialMaximaString);
    }

    public Integer getFrequenciaCardiaca() {
        return frequenciaCardiaca;
    }

    public void setFrequenciaCardiaca(Integer frequenciaCardiaca) {
        this.frequenciaCardiaca = frequenciaCardiaca;
    }

    public String getFrequenciaCardiacaString() {
        return Integer.toString(frequenciaCardiaca);
    }

    public void setFrequenciaCardiacaString(String frequenciaCardiacaString) {
        this.frequenciaCardiaca = Integer.parseInt(frequenciaCardiacaString);
    }

    public Integer getFrequenciaRespiratoria() {
        return frequenciaRespiratoria;
    }

    public void setFrequenciaRespiratoria(Integer frequenciaRespiratoria) {
        this.frequenciaRespiratoria = frequenciaRespiratoria;
    }

    public String getFrequenciaRespiratoriaString() {
        return Integer.toString(frequenciaRespiratoria);
    }

    public void setFrequenciaRespiratoriaString(String frequenciaRespiratoriaString) {
        this.frequenciaRespiratoria = Integer.parseInt(frequenciaRespiratoriaString);
    }

    public Float getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Float temperatura) {
        this.temperatura = temperatura;
    }

    public String getTemperaturaString() {
        return Moeda.substituiVirgula(Moeda.converteR$Float(temperatura));
    }

    public void setTemperaturaString(String temperaturaString) {
        this.temperatura = Moeda.converteUS$(temperaturaString);
    }

    public Float getSaturacao() {
        return saturacao;
    }

    public void setSaturacao(Float saturacao) {
        this.saturacao = saturacao;
    }

    public String getSaturacaoString() {
        return Moeda.substituiVirgula(Moeda.converteR$Float(saturacao));
    }

    public void setSaturacaoString(String saturacaoString) {
        this.saturacao = Moeda.converteUS$(saturacaoString);
        if(this.saturacao > 100) {
            this.saturacao = new Float(100);
        }
    }

    public Float getGlicosimetria() {
        return glicosimetria;
    }

    public void setGlicosimetria(Float glicosimetria) {
        this.glicosimetria = glicosimetria;
    }

    public String getGlicosimetriaString() {
        return Moeda.substituiVirgula(Moeda.converteR$Float(glicosimetria));
    }

    public void setGlicosimetriaString(String glicosimetriaString) {
        this.glicosimetria = Moeda.converteUS$(glicosimetriaString);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
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
        final Enfermaria other = (Enfermaria) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Enfermaria{" + "id=" + id + ", equipe=" + equipe + ", agendamento=" + agendamento + ", hora=" + hora + ", peso=" + peso + ", altura=" + altura + ", pressaoArterialMinima=" + pressaoArterialMinima + ", pressaoArterialMaxima=" + pressaoArterialMaxima + ", frequenciaCardiaca=" + frequenciaCardiaca + ", frequenciaRespiratoria=" + frequenciaRespiratoria + ", temperatura=" + temperatura + ", saturacao=" + saturacao + ", glicosimetria=" + glicosimetria + ", observacao=" + observacao + '}';
    }

}
