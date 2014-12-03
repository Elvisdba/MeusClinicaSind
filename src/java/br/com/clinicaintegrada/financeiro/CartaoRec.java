package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_cartao_rec")
public class CartaoRec implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private FStatus status;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_liquidacao")
    private Date dtLiquidacao;

    public CartaoRec(int id, FStatus status, Date dtLiquidacao) {
        this.id = id;
        this.status = status;
        this.dtLiquidacao = dtLiquidacao;
    }

    public CartaoRec() {
        this.id = -1;
    }

    public CartaoRec(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }

    public Date getDtLiquidacao() {
        return dtLiquidacao;
    }

    public void setDtLiquidacao(Date dtLiquidacao) {
        this.dtLiquidacao = dtLiquidacao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final CartaoRec other = (CartaoRec) obj;
        return true;
    }

    @Override
    public String toString() {
        return "CartaoRec{" + "id=" + id + ", status=" + status + ", dtLiquidacao=" + dtLiquidacao + '}';
    }

}
