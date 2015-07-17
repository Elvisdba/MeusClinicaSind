package br.com.clinicaintegrada.utils;

import java.util.List;

public class Lists {

    public String converte(List list, Integer i) {
        try {
            return list.get(i).toString();
        } catch (Exception e) {
            return null;
        }
    }

}
