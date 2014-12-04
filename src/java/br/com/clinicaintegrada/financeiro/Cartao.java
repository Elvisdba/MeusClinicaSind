package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Cliente;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fin_cartao")
public class Cartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao")
    private String descricao;
    @Column(name = "nr_dias")
    private int dias;
    @Column(name = "nr_taxa")
    private float taxa;
    @Column(name = "ds_debito_credito")
    private String debitoCredito;
    @JoinColumn(name = "id_plano5", referencedColumnName = "id")
    @ManyToOne
    private Plano5 plano5;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne
    private Cliente cliente;

    public Cartao() {
        this.id = -1;
        this.descricao = "";
        this.dias = 0;
        this.taxa = 0;
        this.debitoCredito = "";
        this.plano5 = new Plano5();
        this.cliente = new Cliente();
    }

    public Cartao(int id, String descricao, int dias, float taxa, String debitoCredito, Plano5 plano5, Cliente cliente) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
        this.taxa = taxa;
        this.debitoCredito = debitoCredito;
        this.plano5 = plano5;
        this.cliente = cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public float getTaxa() {
        return taxa;
    }

    public void setTaxa(float taxa) {
        this.taxa = taxa;
    }

    public String getDebitoCredito() {
        return debitoCredito;
    }

    public void setDebitoCredito(String debitoCredito) {
        this.debitoCredito = debitoCredito;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Cartao other = (Cartao) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Cartao{" + "id=" + id + ", descricao=" + descricao + ", dias=" + dias + ", taxa=" + taxa + ", debitoCredito=" + debitoCredito + ", plano5=" + plano5 + ", cliente=" + cliente + '}';
    }

}
