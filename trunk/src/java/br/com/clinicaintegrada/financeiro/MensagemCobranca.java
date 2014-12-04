package br.com.clinicaintegrada.financeiro;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "fin_mensagem_cobranca")
public class MensagemCobranca implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_mensagem_geral", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private MensagemBoleto mensagemBoleto;

    public MensagemCobranca() {
        this.id = -1;
        this.movimento = new Movimento();
        this.mensagemBoleto = new MensagemBoleto();
    }

    public MensagemCobranca(int id, Movimento movimento, MensagemBoleto mensagemBoleto) {
        this.id = id;
        this.movimento = movimento;
        this.mensagemBoleto = mensagemBoleto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public MensagemBoleto getMensagemBoleto() {
        return mensagemBoleto;
    }

    public void setMensagemBoleto(MensagemBoleto mensagemBoleto) {
        this.mensagemBoleto = mensagemBoleto;
    }
}
