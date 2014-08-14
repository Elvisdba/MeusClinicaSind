package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_pessoa_empresa",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_fisica", "id_juridica", "id_funcao", "dt_admissao", "dt_demissao"})
)
public class PessoaEmpresa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_fisica", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Fisica fisica;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica juridica;
    @JoinColumn(name = "id_funcao", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Profissao funcao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_admissao")
    private Date dtAdmissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_demissao")
    private Date dtDemissao;
    @Column(name = "ds_setor", length = 30, nullable = false)
    private String setor;
    @Column(name = "aviso_trabalhado", nullable = true)
    private boolean avisoTrabalhado;

    public PessoaEmpresa() {
        this.id = -1;
        this.fisica = new Fisica();
        this.juridica = new Juridica();
        this.funcao = new Profissao();
        setAdmissao("");
        setDemissao("");
        this.setor = "";
        this.avisoTrabalhado = true;
    }

    public PessoaEmpresa(int id, Fisica fisica, Juridica juridica, Profissao funcao, String admissao, String demissao, String setor, boolean avisoTrabalhado) {
        this.id = id;
        this.fisica = fisica;
        this.juridica = juridica;
        this.funcao = funcao;
        setAdmissao(admissao);
        setDemissao(demissao);
        this.setor = setor;
        this.avisoTrabalhado = avisoTrabalhado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Profissao getFuncao() {
        return funcao;
    }

    public void setFuncao(Profissao funcao) {
        this.funcao = funcao;
    }

    public Date getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(Date dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public String getAdmissao() {
        if (dtAdmissao != null) {
            return DataHoje.converteData(dtAdmissao);
        } else {
            return "";
        }
    }

    public void setAdmissao(String admissao) {
        if (!(admissao.isEmpty())) {
            this.dtAdmissao = DataHoje.converte(admissao);
        }
    }

    public Date getDtDemissao() {
        return dtDemissao;
    }

    public void setDtDemissao(Date dtDemissao) {
        this.dtDemissao = dtDemissao;
    }

    public String getDemissao() {
        if (dtDemissao != null) {
            return DataHoje.converteData(dtDemissao);
        } else {
            return "";
        }
    }

    public void setDemissao(String demissao) {
        if (!(demissao.isEmpty())) {
            this.dtDemissao = DataHoje.converte(demissao);
        }
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public boolean isAvisoTrabalhado() {
        return avisoTrabalhado;
    }

    public void setAvisoTrabalhado(boolean avisoTrabalhado) {
        this.avisoTrabalhado = avisoTrabalhado;
    }

    public void selecionaDataAdmissao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtAdmissao = DataHoje.converte(format.format(event.getObject()));
    }

    public void selecionaDataDemissao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtDemissao = DataHoje.converte(format.format(event.getObject()));
    }

}
