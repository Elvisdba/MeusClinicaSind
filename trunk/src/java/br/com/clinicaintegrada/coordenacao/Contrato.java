package br.com.clinicaintegrada.coordenacao;

import br.com.clinicaintegrada.administrativo.TipoDesligamento;
import br.com.clinicaintegrada.administrativo.TipoInternacao;
import br.com.clinicaintegrada.pessoa.Filial;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "rot_contrato",
        uniqueConstraints = @UniqueConstraint(columnNames = {})
)
public class Contrato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @OneToOne
    private Cliente cliente;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "id_filial_atual", referencedColumnName = "id")
    @OneToOne
    private Filial filialAtual;
    @Column(name = "ds_senha", length = 10)
    private String senha;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_cadastro")
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_internacao")
    private Date dataInternacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_rescisao")
    private Date dataRescisao;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id")
    @OneToOne
    private Pessoa responsavel;
    @JoinColumn(name = "id_paciente", referencedColumnName = "id")
    @OneToOne
    private Pessoa paciente;
    @JoinColumn(name = "id_tipo_internacao", referencedColumnName = "id")
    @OneToOne
    private TipoInternacao tipoInternacao;
    @Column(name = "nr_previsao_dias", nullable = true)
    private int previsaoDias;
    @JoinColumn(name = "id_tipo_desligamento", referencedColumnName = "id")
    @OneToOne
    private TipoDesligamento tipoDesligamento;
    @Column(name = "ds_obs", length = 255, nullable = true)
    private String observacao;
    @Column(name = "nr_valor_total", nullable = true)
    private float valorTotal;
    @Column(name = "nr_entrada", nullable = true)
    private float valorEntrada;
    @Column(name = "nr_qtde_parcelas", nullable = true)
    private int quantidadeParcelas;
    @Column(name = "ds_obs_rescisao", length = 255, nullable = true)
    private String observacaoRescisao;

    public Contrato() {
        this.id = -1;
        this.cliente = new Cliente();
        this.filial = new Filial();
        this.filialAtual = new Filial();
        this.senha = "";
        this.dataCadastro = new Date();
        this.dataInternacao = new Date();
        this.dataRescisao = null;
        this.responsavel = new Pessoa();
        this.paciente = new Pessoa();
        this.tipoInternacao = new TipoInternacao();
        this.previsaoDias = 30;
        this.tipoDesligamento = new TipoDesligamento();
        this.observacao = "";
        this.valorTotal = 0;
        this.valorEntrada = 0;
        this.quantidadeParcelas = 1;
        this.observacaoRescisao = "";
    }

    public Contrato(int id, Cliente cliente, Filial filial, Filial filialAtual, String senha, Date dataCadastro, Date dataInternacao, Date dataRescisao, Pessoa responsavel, Pessoa paciente, TipoInternacao tipoInternacao, int previsaoDias, TipoDesligamento tipoDesligamento, String observacao, float valorTotal, float valorEntrada, int quantidadeParcelas, String observacaoRescisao) {
        this.id = id;
        this.cliente = cliente;
        this.filial = filial;
        this.filialAtual = filialAtual;
        this.senha = senha;
        this.dataCadastro = dataCadastro;
        this.dataInternacao = dataInternacao;
        this.dataRescisao = dataRescisao;
        this.responsavel = responsavel;
        this.paciente = paciente;
        this.tipoInternacao = tipoInternacao;
        this.previsaoDias = previsaoDias;
        this.tipoDesligamento = tipoDesligamento;
        this.observacao = observacao;
        this.valorTotal = valorTotal;
        this.valorEntrada = valorEntrada;
        this.quantidadeParcelas = quantidadeParcelas;
        this.observacaoRescisao = observacaoRescisao;
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

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Filial getFilialAtual() {
        return filialAtual;
    }

    public void setFilialAtual(Filial filialAtual) {
        this.filialAtual = filialAtual;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getDataCadastroString() {
        return DataHoje.converteData(dataCadastro);
    }

    public void setDataCadastroString(String dataCadastroString) {
        this.dataCadastro = DataHoje.converte(dataCadastroString);
    }

    public Date getDataInternacao() {
        return dataInternacao;
    }

    public void setDataInternacao(Date dataInternacao) {
        this.dataInternacao = dataInternacao;
    }

    public String getDataInternacaoString() {
        return DataHoje.converteData(dataInternacao);
    }

    public void setDataInternacaoString(String dataInternacaoString) {
        this.dataInternacao = DataHoje.converte(dataInternacaoString);
    }

    public Date getDataRescisao() {
        return dataRescisao;
    }

    public void setDataRescisao(Date dataRescisao) {
        this.dataRescisao = dataRescisao;
    }

    public String getDataRescisaoString() {
        return DataHoje.converteData(dataRescisao);
    }

    public void setDataRescisaoString(String dataRescisaoString) {
        this.dataRescisao = DataHoje.converte(dataRescisaoString);
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Pessoa getPaciente() {
        return paciente;
    }

    public void setPaciente(Pessoa paciente) {
        this.paciente = paciente;
    }

    public TipoInternacao getTipoInternacao() {
        return tipoInternacao;
    }

    public void setTipoInternacao(TipoInternacao tipoInternacao) {
        this.tipoInternacao = tipoInternacao;
    }

    public int getPrevisaoDias() {
        return previsaoDias;
    }

    public void setPrevisaoDias(int previsaoDias) {
        this.previsaoDias = previsaoDias;
    }

    public String getPrevisaoDiasString() {
        return Integer.toString(previsaoDias);
    }

    public void setPrevisaoDiasString(String previsaoDiasString) {
        this.previsaoDias = Integer.parseInt(previsaoDiasString);
    }

    public TipoDesligamento getTipoDesligamento() {
        return tipoDesligamento;
    }

    public void setTipoDesligamento(TipoDesligamento tipoDesligamento) {
        this.tipoDesligamento = tipoDesligamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getValorTotalString() {
        return Moeda.converteR$Float(valorTotal);
    }

    public void setValorTotalString(String valorTotalString) {
        this.valorTotal = Moeda.converteUS$(valorTotalString);
    }

    public String getValorEntradaString() {
        return Moeda.converteR$Float(valorEntrada);
    }

    public void setValorEntradaString(String valorEntradaString) {
        this.valorEntrada = Moeda.converteUS$(valorEntradaString);
    }

    public int getQuantidadeParcelas() {
        return quantidadeParcelas;
    }

    public void setQuantidadeParcelas(int quantidadeParcelas) {
        this.quantidadeParcelas = quantidadeParcelas;
    }

    public String getQuantidadeParcelasString() {
        return Integer.toString(quantidadeParcelas);
    }

    public void setQuantidadeParcelas(String quantidadeParcelasString) {
        this.quantidadeParcelas = Integer.parseInt(quantidadeParcelasString);
    }

    public String getObservacaoRescisao() {
        return observacaoRescisao;
    }

    public void setObservacaoRescisao(String observacaoRescisao) {
        this.observacaoRescisao = observacaoRescisao;
    }

    public float getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(float valorEntrada) {
        this.valorEntrada = valorEntrada;
    }
    
    public void selectedDataCadastro(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataCadastro = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataInternacao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataInternacao = DataHoje.converte(format.format(event.getObject()));
    }

    public void selectedDataRecisao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataRescisao = DataHoje.converte(format.format(event.getObject()));
    }


}
