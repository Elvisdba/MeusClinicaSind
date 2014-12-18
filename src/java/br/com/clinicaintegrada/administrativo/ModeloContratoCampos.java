package br.com.clinicaintegrada.administrativo;

import br.com.clinicaintegrada.seguranca.Modulo;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ctr_campos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_modulo", "ds_variavel"})
)
@NamedQueries({
    @NamedQuery(name = "ModeloContratoCampos.findAll", query = "SELECT MCC FROM ModeloContratoCampos AS MCC ORDER BY MCC.modulo.descricao ASC, MCC.campo ASC, MCC.variavel ASC")
})
public class ModeloContratoCampos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id")
    @ManyToOne
    private Modulo modulo;
    @Column(name = "ds_campo", length = 100)
    private String campo;
    @Column(name = "ds_variavel")
    private String variavel;
    @Column(name = "ds_observacao", length = 1500)
    private String observacao;

    public ModeloContratoCampos() {
        this.id = -1;
        this.modulo = new Modulo();
        this.campo = "";
        this.variavel = "";
        this.observacao = "";
    }

    public ModeloContratoCampos(Integer id, Modulo modulo, String campo, String variavel, String observacao) {
        this.id = id;
        this.modulo = modulo;
        this.campo = campo;
        this.variavel = variavel;
        this.observacao = observacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getVariavel() {
        return variavel;
    }

    public void setVariavel(String variavel) {
        this.variavel = variavel;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
