package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_cartao_pag")
public class CartaoPag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public CartaoPag() {
        this.id = -1;
    }

    public CartaoPag(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
