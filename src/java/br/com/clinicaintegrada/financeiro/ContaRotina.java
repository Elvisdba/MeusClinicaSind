package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "fin_conta_rotina",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_rotina", "id_plano4", "id_cliente"})
)
public class ContaRotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_plano4", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Plano4 plano4;
    @Column(name = "ds_pag_rec", length = 1, nullable = true)
    private String pagRec;
    @Column(name = "nr_partida", length = 1, nullable = false)
    private int partida;   // 1 Partida 0 Contra Partida
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Cliente cliente;

    public ContaRotina() {
        this.id = -1;
        this.rotina = new Rotina();
        this.plano4 = new Plano4();
        this.pagRec = "";
        this.partida = 0;
        this.cliente = new Cliente();
    }

    public ContaRotina(int id, Rotina rotina, Plano4 plano4, String pagRec, int partida, Cliente cliente) {
        this.id = id;
        this.rotina = rotina;
        this.plano4 = plano4;
        this.pagRec = pagRec;
        this.partida = partida;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Plano4 getPlano4() {
        return plano4;
    }

    public void setPlano4(Plano4 plano4) {
        this.plano4 = plano4;
    }

    public String getPagRec() {
        return pagRec;
    }

    public void setPagRec(String pagRec) {
        this.pagRec = pagRec;
    }

    public int getPartida() {
        return partida;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
