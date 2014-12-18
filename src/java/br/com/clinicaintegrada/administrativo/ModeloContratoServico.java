package br.com.clinicaintegrada.administrativo;

import br.com.clinicaintegrada.financeiro.Servicos;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ctr_servico",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contrato", "id_servico"})
)
public class ModeloContratoServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id")
    @ManyToOne
    private ModeloContrato contrato;
    @JoinColumn(name = "id_servico", referencedColumnName = "id")
    @ManyToOne
    private Servicos servicos;

    public ModeloContratoServico() {
        this.id = -1;
        this.contrato = new ModeloContrato();
        this.servicos = new Servicos();
    }

    public ModeloContratoServico(Integer id, ModeloContrato contrato, Servicos servicos) {
        this.id = id;
        this.contrato = contrato;
        this.servicos = servicos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ModeloContrato getContrato() {
        return contrato;
    }

    public void setContrato(ModeloContrato contrato) {
        this.contrato = contrato;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServico(Servicos servicos) {
        this.servicos = servicos;
    }
}
