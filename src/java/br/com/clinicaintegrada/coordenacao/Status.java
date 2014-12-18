package br.com.clinicaintegrada.coordenacao;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "rot_agendamento_status")
@NamedQueries({
    @NamedQuery(name = "Status.findAll", query = "SELECT S FROM Status AS S ORDER BY S.descricao ASC "),
    @NamedQuery(name = "Status.findName", query = "SELECT S FROM Status S WHERE UPPER(S.descricao) LIKE :pdescricao ORDER BY S.descricao ASC")
})
public class Status implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 30, nullable = true)
    private String descricao;

    public Status() {
        this.id = -1;
        this.descricao = "";
    }

    public Status(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        final Status other = (Status) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Status{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
