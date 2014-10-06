package br.com.clinicaintegrada.administrativo;

import br.com.clinicaintegrada.financeiro.Servicos;
import br.com.clinicaintegrada.seguranca.Cliente;
import br.com.clinicaintegrada.utils.Moeda;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "ctr_taxas",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_servicos", "id_cliente"})
)
@NamedQuery(name = "Taxas.findAll", query = "SELECT T FROM Taxas AS T ORDER BY T.servicos.descricao ASC ")
public class Taxas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Cliente cliente;
    @Column(name = "nr_valor", length = 10, nullable = false)
    private float valor;

    public Taxas() {
        this.id = -1;
        this.servicos = new Servicos();
        this.cliente = new Cliente();
        this.valor = 0;
    }

    public Taxas(int id, Servicos servicos, Cliente cliente, float valor) {
        this.id = id;
        this.servicos = servicos;
        this.cliente = cliente;
        this.valor = valor;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getValorString() {
        return Moeda.converteR$Float(valor);
    }

    public void setValorString(String valorString) {
        this.valor = Moeda.converteUS$(valorString);
    }
}
