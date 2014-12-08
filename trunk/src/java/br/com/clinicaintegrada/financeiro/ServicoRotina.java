package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.seguranca.Rotina;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_servico_rotina",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_servicos", "id_rotina", "id_cliente"})
)
public class ServicoRotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;

    public ServicoRotina() {
        this.id = -1;
        this.servicos = new Servicos();
        this.rotina = new Rotina();
        this.cliente = new Cliente();
    }

    public ServicoRotina(int id, Servicos servicos, Rotina rotina, Cliente cliente) {
        this.id = id;
        this.servicos = servicos;
        this.rotina = rotina;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
