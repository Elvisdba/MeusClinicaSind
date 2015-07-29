package br.com.clinicaintegrada.seguranca;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "seg_libera_acesso")
public class LiberaAcesso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_mac_filial", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private MacFilial macFilial;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_expira")
    private Date expira;

    public LiberaAcesso() {
        this.id = null;
        this.macFilial = null;
        this.expira = null;
    }

    public LiberaAcesso(Integer id, MacFilial macFilial, Date expira) {
        this.id = id;
        this.macFilial = macFilial;
        this.expira = expira;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Date getExpira() {
        return expira;
    }

    public void setExpira(Date expira) {
        this.expira = expira;
    }

}
