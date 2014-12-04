package br.com.clinicaintegrada.financeiro;

import br.com.clinicaintegrada.seguranca.Usuario;
import br.com.clinicaintegrada.utils.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "fin_movimento_inativo")
public class MovimentoInativo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date data;
    @Column(name = "ds_historico", length = 8000)
    private String historico;

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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDataString() {
        return DataHoje.converteData(data);
    }

    public void setDataString(String data) {
        this.data = DataHoje.converte(data);
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final MovimentoInativo other = (MovimentoInativo) obj;
        return true;
    }

    @Override
    public String toString() {
        return "MovimentoInativo{" + "id=" + id + ", movimento=" + movimento + ", usuario=" + usuario + ", data=" + data + ", historico=" + historico + '}';
    }

}
