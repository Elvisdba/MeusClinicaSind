package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_permissao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_cliente", "id_modulo", "id_rotina", "id_evento"})
)
public class Permissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SegEvento segEvento;

    public Permissao() {
        this.id = -1;
        this.cliente = new Cliente();
        this.modulo = new Modulo();
        this.rotina = new Rotina();
        this.segEvento = new SegEvento();
    }

    public Permissao(int id, Cliente cliente, Modulo modulo, Rotina rotina, SegEvento segEvento) {
        this.id = id;
        this.cliente = cliente;
        this.modulo = modulo;
        this.rotina = rotina;
        this.segEvento = segEvento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public SegEvento getSegEvento() {
        return segEvento;
    }

    public void setSegEvento(SegEvento segEvento) {
        this.segEvento = segEvento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
