package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.endereco.Endereco;
import br.com.clinicaintegrada.pessoa.Equipe;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.sistema.Veiculo;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_resgate",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"id_paciente", "dt_saida"})}
)
public class Resgate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao", nullable = false)
    private Date dataEmissao;
    @JoinColumn(name = "id_solicitante", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa solicitante;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa paciente;
    @JoinColumn(name = "id_endereco", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Endereco endereco;
    @Column(name = "ds_numero", length = 20)
    private String numero;
    @Column(name = "ds_complemento", length = 50)
    private String complemento;
    @JoinColumn(name = "id_motorista", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe motorista;
    @JoinColumn(name = "id_tecnico", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Equipe tecnico;
    @JoinColumn(name = "id_apoio1", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Escala apoio1;
    @JoinColumn(name = "id_apoio2", referencedColumnName = "id")
    @OneToOne
    private Escala apoio2;
    @JoinColumn(name = "id_apoio3", referencedColumnName = "id")
    @OneToOne
    private Escala apoio3;
    @JoinColumn(name = "id_apoio4", referencedColumnName = "id")
    @OneToOne
    private Escala apoio4;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_saida")
    private Date dataSaida;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_retorno")
    private Date dataRetorno;
    @Column(name = "ds_hora_agenda", nullable = false, length = 5)
    private String horaAgenda;
    @Column(name = "ds_hora_saida", nullable = false, length = 5)
    private String horaSaida;
    @Column(name = "ds_hora_retorno", nullable = false, length = 5)
    private String horaRetorno;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Usuario usuario;
    @JoinColumn(name = "id_veiculo", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Veiculo veiculo;
    @Column(name = "nr_km_inicial")
    private Float kmInicial;
    @Column(name = "nr_km_final")
    private Float kmFinal;
    @Column(name = "is_resgatado", columnDefinition = "boolean default false")
    private Boolean resgatado;
    @Column(name = "ds_obs", length = 3000)
    private String observacao;

    public Resgate() {
        this.id = -1;
        this.dataEmissao = DataHoje.dataHoje();
        this.solicitante = null;
        this.paciente = null;
        this.endereco = null;
        this.numero = "";
        this.complemento = "";
        this.motorista = null;
        this.tecnico = null;
        this.apoio1 = null;
        this.apoio2 = null;
        this.apoio3 = null;
        this.apoio4 = null;
        this.dataSaida = null;
        this.dataRetorno = null;
        this.horaAgenda = "";
        this.horaSaida = "";
        this.horaRetorno = "";
        this.usuario = null;
        this.veiculo = null;
        this.kmInicial = new Float(0);
        this.kmFinal = new Float(0);
        this.resgatado = true;
        this.observacao = "";
    }

    public Resgate(Integer id, Date dataEmissao, Pessoa solicitante, Pessoa paciente, Endereco endereco, String numero, String complemento, Equipe motorista, Equipe tecnico, Escala apoio1, Escala apoio2, Escala apoio3, Escala apoio4, Date dataSaida, Date dataRetorno, String horaAgenda, String horaSaida, String horaRetorno, Usuario usuario, Veiculo veiculo, Float kmInicial, Float kmFinal, Boolean resgatado, String observacao) {
        this.id = id;
        this.dataEmissao = dataEmissao;
        this.solicitante = solicitante;
        this.paciente = paciente;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
        this.motorista = motorista;
        this.tecnico = tecnico;
        this.apoio1 = apoio1;
        this.apoio2 = apoio2;
        this.apoio3 = apoio3;
        this.apoio4 = apoio4;
        this.dataSaida = dataSaida;
        this.dataRetorno = dataRetorno;
        this.horaAgenda = horaAgenda;
        this.horaSaida = horaSaida;
        this.horaRetorno = horaRetorno;
        this.usuario = usuario;
        this.veiculo = veiculo;
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.resgatado = resgatado;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataEmissaoString() {
        return DataHoje.converteData(dataEmissao);
    }

    public void setDataEmissaoString(String dataEmissaoString) {
        this.dataEmissao = DataHoje.converte(dataEmissaoString);
    }

    public Pessoa getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Pessoa solicitante) {
        this.solicitante = solicitante;
    }

    public Pessoa getPaciente() {
        return paciente;
    }

    public void setPaciente(Pessoa paciente) {
        this.paciente = paciente;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public Equipe getMotorista() {
        return motorista;
    }

    public void setMotorista(Equipe motorista) {
        this.motorista = motorista;
    }

    public Equipe getTecnico() {
        return tecnico;
    }

    public void setTecnico(Equipe tecnico) {
        this.tecnico = tecnico;
    }

    public Escala getApoio1() {
        return apoio1;
    }

    public void setApoio1(Escala apoio1) {
        this.apoio1 = apoio1;
    }

    public Escala getApoio2() {
        return apoio2;
    }

    public void setApoio2(Escala apoio2) {
        this.apoio2 = apoio2;
    }

    public Escala getApoio3() {
        return apoio3;
    }

    public void setApoio3(Escala apoio3) {
        this.apoio3 = apoio3;
    }

    public Escala getApoio4() {
        return apoio4;
    }

    public void setApoio4(Escala apoio4) {
        this.apoio4 = apoio4;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public String getDataSaidaString() {
        return DataHoje.converteData(dataSaida);
    }

    public void setDataSaidaString(String dataSaidaString) {
        this.dataSaida = DataHoje.converte(dataSaidaString);
    }

    public Date getDataRetorno() {
        return dataRetorno;
    }

    public void setDataRetorno(Date dataRetorno) {
        this.dataRetorno = dataRetorno;
    }

    public String getDataRetornoString() {
        return DataHoje.converteData(dataRetorno);
    }

    public void setDataRetornoString(String dataRetornoString) {
        this.dataRetorno = DataHoje.converte(dataRetornoString);
    }

    public String getHoraAgenda() {
        return horaAgenda;
    }

    public void setHoraAgenda(String horaAgenda) {
        this.horaAgenda = horaAgenda;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getHoraRetorno() {
        return horaRetorno;
    }

    public void setHoraRetorno(String horaRetorno) {
        this.horaRetorno = horaRetorno;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Float getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(Float kmInicial) {
        this.kmInicial = kmInicial;
    }

    public String getKmInicialString() {
        return "" + kmInicial;
    }

    public void setKmInicialString(String kmInicialString) {
        try {
            this.kmInicial = Moeda.substituiVirgulaFloat(kmInicialString);
        } catch (Exception e) {
        }
    }

    public Float getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(Float kmFinal) {
        this.kmFinal = kmFinal;
    }

    public String getKmFinalString() {
        return "" + kmFinal;
    }

    public void setKmFinalString(String kmFinalString) {
        try {
            this.kmFinal = Moeda.substituiVirgulaFloat(kmFinalString);
            if (this.kmFinal < this.kmInicial) {
                this.kmFinal = this.kmInicial;
            }
        } catch (Exception e) {
        }
    }

    public Boolean getResgatado() {
        return resgatado;
    }

    public void setResgatado(Boolean resgatado) {
        this.resgatado = resgatado;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void selectedDataEmissao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataEmissao = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataSaida(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataSaida = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataRetorno(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataRetorno = DataHoje.converte(format.format(event.getObject()));
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
        final Resgate other = (Resgate) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Resgate{" + "id=" + id + ", dataEmissao=" + dataEmissao + ", solicitante=" + solicitante + ", paciente=" + paciente + ", endereco=" + endereco + ", numero=" + numero + ", complemento=" + complemento + ", motorista=" + motorista + ", tecnico=" + tecnico + ", apoio1=" + apoio1 + ", apoio2=" + apoio2 + ", apoio3=" + apoio3 + ", apoio4=" + apoio4 + ", dataSaida=" + dataSaida + ", dataRetorno=" + dataRetorno + ", horaAgenda=" + horaAgenda + ", horaSaida=" + horaSaida + ", horaRetorno=" + horaRetorno + ", usuario=" + usuario + ", veiculo=" + veiculo + ", kmInicial=" + kmInicial + ", kmFinal=" + kmFinal + ", resgatado=" + resgatado + ", observacao=" + observacao + '}';
    }

}
