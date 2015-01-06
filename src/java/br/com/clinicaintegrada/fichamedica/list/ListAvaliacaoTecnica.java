package br.com.clinicaintegrada.fichamedica.list;

import br.com.clinicaintegrada.fichamedica.AtendimentoAvaliacao;

public class ListAvaliacaoTecnica {

    private Boolean selected;
    private Integer index;
    private AtendimentoAvaliacao atendimentoAvaliacao;

    public ListAvaliacaoTecnica() {
        this.selected = false;
        this.index = null;
        this.atendimentoAvaliacao = null;
    }

    public ListAvaliacaoTecnica(Boolean selected, Integer index, AtendimentoAvaliacao atendimentoAvaliacao) {
        this.selected = selected;
        this.index = index;
        this.atendimentoAvaliacao = atendimentoAvaliacao;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public AtendimentoAvaliacao getAtendimentoAvaliacao() {
        return atendimentoAvaliacao;
    }

    public void setAtendimentoAvaliacao(AtendimentoAvaliacao atendimentoAvaliacao) {
        this.atendimentoAvaliacao = atendimentoAvaliacao;
    }

}
