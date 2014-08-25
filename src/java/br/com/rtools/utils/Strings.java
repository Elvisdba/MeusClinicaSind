package br.com.clinicaintegrada.utils;

public class Strings {

    public static String converterNullToString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }
}
