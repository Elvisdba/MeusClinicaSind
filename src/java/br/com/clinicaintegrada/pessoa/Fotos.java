package br.com.clinicaintegrada.pessoa;

import br.com.clinicaintegrada.coordenacao.Contrato;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_fotos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_contrato", "dt_data"})
)
public class Fotos implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Contrato contrato;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date data;

    public Fotos() {
        this.id = null;
        this.contrato = null;
        this.data = null;
    }

    public Fotos(Integer id, Contrato contrato, Date data) {
        this.id = id;
        this.contrato = contrato;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataString() {
        return DataHoje.converteData(data);
    }

    public void setDataString(String dataString) {
        this.data = DataHoje.converte(dataString);
    }

    public void selectedData(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.data = DataHoje.converte(format.format(event.getObject()));
    }

}
