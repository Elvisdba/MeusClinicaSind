package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.coordenacao.Evento;
import br.com.clinicaintegrada.pessoa.Pessoa;
import br.com.clinicaintegrada.utils.DataHoje;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_movimento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_lote", "id_pessoa", "id_servicos", "vencimento", "ds_documento"})
)
@NamedQuery(name = "Movimento.findAll", query = "SELECT M FROM Movimento AS M ORDER BY M.vencimento ASC ")
public class Movimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_lote", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Lote lote;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id")
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_evento", referencedColumnName = "id")
    @ManyToOne
    private Evento evento;
    @JoinColumn(name = "id_tipo_servico", referencedColumnName = "id")
    @ManyToOne
    private TipoServico tipoServico;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id")
    @ManyToOne
    private FTipoDocumento tipoDocumento;
    @Column(name = "nr_valor", length = 10, nullable = false)
    private float valor;
    @Column(name = "nr_ctr_boleto", length = 30)
    private String nrCtrBoleto;
    @Column(name = "ds_referencia", length = 7)
    private String referencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vencimento")
    private Date vencimento;
    @Column(name = "nr_quantidade")
    private int quantidade;
    @Column(name = "is_ativo", columnDefinition = "boolean default true")
    private boolean ativo;
    @Column(name = "ds_es", length = 1)
    private String es;
    @Column(name = "ds_documento", length = 100)
    private String documento;
    @JoinColumn(name = "id_baixa", referencedColumnName = "id")
    @ManyToOne
    private Baixa baixa;
    @Column(name = "nr_correcao")
    private float correcao;
    @Column(name = "nr_juros")
    private float juros;
    @Column(name = "nr_multa")
    private float multa;
    @Column(name = "nr_desconto")
    private float desconto;
    @Column(name = "nr_taxa")
    private float taxa;
    @Column(name = "nr_valor_baixa")
    private float valorBaixa;
    @Column(name = "nr_desconto_ate_vencimento")
    private float descontoAteVencimento;

    public Movimento() {
        this.id = -1;
        this.lote = new Lote();
        this.pessoa = new Pessoa();
        this.servicos = new Servicos();
        this.evento = new Evento();
        this.tipoServico = new TipoServico();
        this.tipoDocumento = new FTipoDocumento();
        this.valor = 0;
        this.nrCtrBoleto = "";
        this.referencia = "";
        this.vencimento = null;
        this.quantidade = 0;
        this.ativo = false;
        this.es = "";
        this.documento = "";
        this.baixa = new Baixa();
        this.correcao = 0;
        this.juros = 0;
        this.multa = 0;
        this.desconto = 0;
        this.taxa = 0;
        this.valorBaixa = 0;
        descontoAteVencimento = 0;
    }

    public Movimento(int id, Lote lote, Pessoa pessoa, Servicos servicos, Evento evento, TipoServico tipoServico, FTipoDocumento tipoDocumento, float valor, String nrCtrBoleto, String referencia, Date vencimento, int quantidade, boolean ativo, String es, String documento, Baixa baixa, float correcao, float juros, float multa, float desconto, float taxa, float valorBaixa, float descontoAteVencimento) {
        this.id = id;
        this.lote = lote;
        this.pessoa = pessoa;
        this.servicos = servicos;
        this.evento = evento;
        this.tipoServico = tipoServico;
        this.tipoDocumento = tipoDocumento;
        this.valor = valor;
        this.nrCtrBoleto = nrCtrBoleto;
        this.referencia = referencia;
        this.vencimento = vencimento;
        this.quantidade = quantidade;
        this.ativo = ativo;
        this.es = es;
        this.documento = documento;
        this.baixa = baixa;
        this.correcao = correcao;
        this.juros = juros;
        this.multa = multa;
        this.desconto = desconto;
        this.taxa = taxa;
        this.valorBaixa = valorBaixa;
        this.descontoAteVencimento = descontoAteVencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public FTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(FTipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valor) {
        this.valor = Moeda.converteUS$(valor);
    }

    public String getNrCtrBoleto() {
        return nrCtrBoleto;
    }

    public void setNrCtrBoleto(String nrCtrBoleto) {
        this.nrCtrBoleto = nrCtrBoleto;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public String getVencimentoString() {
        return DataHoje.converteData(vencimento);
    }

    public void setVencimentoString(String vencimentoString) {
        this.vencimento = DataHoje.converte(vencimentoString);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Baixa getBaixa() {
        return baixa;
    }

    public void setBaixa(Baixa baixa) {
        this.baixa = baixa;
    }

    public float getCorrecao() {
        return correcao;
    }

    public void setCorrecao(float correcao) {
        this.correcao = correcao;
    }

    public float getJuros() {
        return juros;
    }

    public void setJuros(float juros) {
        this.juros = juros;
    }

    public float getMulta() {
        return multa;
    }

    public void setMulta(float multa) {
        this.multa = multa;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }

    public float getValorBaixa() {
        return valorBaixa;
    }

    public void setValorBaixa(float valorBaixa) {
        this.valorBaixa = valorBaixa;
    }

    public float getDescontoAteVencimento() {
        return descontoAteVencimento;
    }

    public void setDescontoAteVencimento(float descontoAteVencimento) {
        this.descontoAteVencimento = descontoAteVencimento;
    }

}
