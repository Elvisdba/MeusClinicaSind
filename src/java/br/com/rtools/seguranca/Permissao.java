package br.com.rtools.seguranca;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SEG_PERMISSAO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_CLIENTE", "ID_MODULO", "ID_ROTINA", "ID_EVENTO"})
)
public class Permissao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Cliente cliente;
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Evento evento;

    public Permissao() {
        this.id = -1;
        this.cliente = new Cliente();
        this.modulo = new Modulo();
        this.rotina = new Rotina();
        this.evento = new Evento();
    }

    public Permissao(int id, Cliente cliente, Modulo modulo, Rotina rotina, Evento evento) {
        this.id = id;
        this.cliente = cliente;
        this.modulo = modulo;
        this.rotina = rotina;
        this.evento = evento;
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

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
