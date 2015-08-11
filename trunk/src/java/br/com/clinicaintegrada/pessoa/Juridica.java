package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "pes_juridica")
public class Juridica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false, unique = true)
    @OneToOne
    private Pessoa pessoa;
    @Column(name = "ds_fantasia", length = 200, nullable = false)
    private String fantasia;
    @JoinColumn(name = "id_cnae", referencedColumnName = "id", nullable = true)
    @OneToOne(fetch = FetchType.EAGER)
    private Cnae cnae;
    @JoinColumn(name = "id_contabilidade", referencedColumnName = "id", nullable = true)
    @OneToOne
    private Juridica contabilidade;
    @Column(name = "ds_inscricao_estadual", length = 30, nullable = true)
    private String inscricaoEstadual;
    @Column(name = "ds_inscricao_municipal", length = 30, nullable = true)
    private String inscricaoMunicipal;
    @Column(name = "ds_contato", length = 50, nullable = true)
    private String contato;
    @Column(name = "ds_responsavel", length = 50, nullable = true)
    private String responsavel;
    @JoinColumn(name = "id_porte", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Porte porte;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_abertura")
    private Date dtAbertura;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fechamento")
    private Date dtFechamento;
    @Column(name = "is_email_escritorio", columnDefinition = "boolean default false")
    private Boolean emailEscritorio;
    @Column(name = "is_email_cobranca_escritorio", columnDefinition = "boolean default false")
    private Boolean cobrancaEscritorio;

    public Juridica() {
        this.id = null;
        this.pessoa = new Pessoa();
        this.fantasia = "";
        this.cnae = new Cnae();
        this.contabilidade = null;
        this.inscricaoEstadual = "";
        this.inscricaoMunicipal = "";
        this.contato = "";
        this.responsavel = "";
        this.porte = new Porte();
        setAbertura("");
        setFechamento("");
        this.emailEscritorio = false;
        this.cobrancaEscritorio = false;
    }

    public Juridica(Integer id, Pessoa pessoa, String fantasia, Cnae cnae, Juridica contabilidade, String inscricaoEstadual, String inscricaoMunicipal, String contato, String responsavel, Porte porte, String abertura, String fechamento, Boolean emailEscritorio, Boolean cobrancaEscritorio) {
        this.id = id;
        this.pessoa = pessoa;
        this.fantasia = fantasia;
        this.cnae = cnae;
        this.contabilidade = contabilidade;
        this.inscricaoEstadual = inscricaoEstadual;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.contato = contato;
        this.responsavel = responsavel;
        this.porte = porte;
        setAbertura(abertura);
        setFechamento(fechamento);
        this.emailEscritorio = emailEscritorio;
        this.cobrancaEscritorio = cobrancaEscritorio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Date getDtAbertura() {
        return dtAbertura;
    }

    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }

    public String getAbertura() {
        if (dtAbertura != null) {
            return DataHoje.converteData(dtAbertura);
        } else {
            return "";
        }
    }

    public void setAbertura(String abertura) {
        if (!(abertura.isEmpty())) {
            this.dtAbertura = DataHoje.converte(abertura);
        }
    }

    public String getFechamento() {
        if (dtAbertura != null) {
            return DataHoje.converteData(dtFechamento);
        } else {
            return "";
        }
    }

    public void setFechamento(String fechamento) {
        if (!(fechamento.isEmpty())) {
            this.dtFechamento = DataHoje.converte(fechamento);
        }
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Juridica getContabilidade() {
        return contabilidade;
    }

    public void setContabilidade(Juridica contabilidade) {
        this.contabilidade = contabilidade;
    }

    public Boolean getEmailEscritorio() {
        return emailEscritorio;
    }

    public void setEmailEscritorio(Boolean emailEscritorio) {
        this.emailEscritorio = emailEscritorio;
    }

    public Boolean getCobrancaEscritorio() {
        return cobrancaEscritorio;
    }

    public void setCobrancaEscritorio(Boolean cobrancaEscritorio) {
        this.cobrancaEscritorio = cobrancaEscritorio;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.id;
        hash = 79 * hash + Objects.hashCode(this.pessoa);
        hash = 79 * hash + Objects.hashCode(this.fantasia);
        hash = 79 * hash + Objects.hashCode(this.cnae);
        hash = 79 * hash + Objects.hashCode(this.inscricaoEstadual);
        hash = 79 * hash + Objects.hashCode(this.inscricaoMunicipal);
        hash = 79 * hash + Objects.hashCode(this.contato);
        hash = 79 * hash + Objects.hashCode(this.responsavel);
        hash = 79 * hash + Objects.hashCode(this.porte);
        hash = 79 * hash + Objects.hashCode(this.dtAbertura);
        hash = 79 * hash + Objects.hashCode(this.dtFechamento);
        hash = 79 * hash + (this.emailEscritorio ? 1 : 0);
        hash = 79 * hash + (this.cobrancaEscritorio ? 1 : 0);
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
        final Juridica other = (Juridica) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.pessoa, other.pessoa)) {
            return false;
        }
        if (!Objects.equals(this.fantasia, other.fantasia)) {
            return false;
        }
        if (!Objects.equals(this.cnae, other.cnae)) {
            return false;
        }
        if (!Objects.equals(this.inscricaoEstadual, other.inscricaoEstadual)) {
            return false;
        }
        if (!Objects.equals(this.inscricaoMunicipal, other.inscricaoMunicipal)) {
            return false;
        }
        if (!Objects.equals(this.contato, other.contato)) {
            return false;
        }
        if (!Objects.equals(this.responsavel, other.responsavel)) {
            return false;
        }
        if (!Objects.equals(this.porte, other.porte)) {
            return false;
        }
        if (!Objects.equals(this.dtAbertura, other.dtAbertura)) {
            return false;
        }
        if (!Objects.equals(this.dtFechamento, other.dtFechamento)) {
            return false;
        }
        if (this.emailEscritorio != other.emailEscritorio) {
            return false;
        }
        if (this.cobrancaEscritorio != other.cobrancaEscritorio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Juridica{" + "id=" + id + ", pessoa=" + pessoa + ", fantasia=" + fantasia + ", cnae=" + cnae + ", inscricaoEstadual=" + inscricaoEstadual + ", inscricaoMunicipal=" + inscricaoMunicipal + ", contato=" + contato + ", responsavel=" + responsavel + ", porte=" + porte + ", dtAbertura=" + dtAbertura + ", dtFechamento=" + dtFechamento + ", emailEscritorio=" + emailEscritorio + ", cobrancaEscritorio=" + cobrancaEscritorio + '}';
    }

    
}
