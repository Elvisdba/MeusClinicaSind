package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fin_lote_boleto")
public class LoteBoleto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_processamento")
    private Date dtProcessamento;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;

    public LoteBoleto() {
        this.id = -1;
        this.dtProcessamento = DataHoje.dataHoje();
        this.cliente = new Cliente();
    }

    public LoteBoleto(int id, Date dtProcessamento, Cliente cliente) {
        this.id = id;
        this.dtProcessamento = dtProcessamento;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtProcessamento() {
        return dtProcessamento;
    }

    public void setDtProcessamento(Date dtProcessamento) {
        this.dtProcessamento = dtProcessamento;
    }

    public String getProcessamento() {
        return DataHoje.converteData(dtProcessamento);
    }

    public void setProcessamento(String processamento) {
        this.dtProcessamento = DataHoje.converte(processamento);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
        final LoteBoleto other = (LoteBoleto) obj;
        return true;
    }

    @Override
    public String toString() {
        return "LoteBoleto{" + "id=" + id + ", dtProcessamento=" + dtProcessamento + ", cliente=" + cliente + '}';
    }
    
    
}
