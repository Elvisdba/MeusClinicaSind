package br.com.clinicaintegrada.utils;

import java.util.List;
import java.util.Vector;

public class ListArgumentos {

    private List lista = new Vector<List>();

    public ListArgumentos() {
        setLista(new Vector<List>());
    }

    public ListArgumentos(String campo, String tipo) {
        this.addObject(new Vector<List>(), campo, tipo);
    }

    public String addObject(List list, String campo, String tipo) {
        list.add(campo);
        list.add(tipo);
        lista.add(list);
        return null;
    }

    public List getLista() {
        return lista;
    }

    public void setLista(List lista) {
        this.lista = lista;
    }
}
