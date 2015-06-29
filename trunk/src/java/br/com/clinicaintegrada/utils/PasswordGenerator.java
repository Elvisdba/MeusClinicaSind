package br.com.clinicaintegrada.utils;

import java.util.UUID;

public class PasswordGenerator {

    public static String create() {
        UUID uuidX = UUID.randomUUID();
        String uuid = uuidX.toString().toUpperCase().substring(0, 4);
        return uuid;
    }

}
