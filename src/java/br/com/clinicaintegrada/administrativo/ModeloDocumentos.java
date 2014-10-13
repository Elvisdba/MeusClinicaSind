package br.com.clinicaintegrada.administrativo;

import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ctr_modelo_documentos")
public class ModeloDocumentos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 15000)
    private String descricao;
    @JoinColumn(name = "id_modelo_contrato", referencedColumnName = "id")
    @ManyToOne
    private ModeloContrato modeloContrato;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id")
    @ManyToOne
    private Rotina rotina;

    public ModeloDocumentos() {
        this.id = -1;
        this.descricao = "";
        this.modeloContrato = new ModeloContrato();
        this.rotina = new Rotina();
    }

    public ModeloDocumentos(int id, String descricao, ModeloContrato modeloContrato, Rotina rotina) {
        this.id = id;
        this.descricao = descricao;
        this.modeloContrato = modeloContrato;
        this.rotina = rotina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public ModeloContrato getModeloContrato() {
        return modeloContrato;
    }

    public void setModeloContrato(ModeloContrato modeloContrato) {
        this.modeloContrato = modeloContrato;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

}
