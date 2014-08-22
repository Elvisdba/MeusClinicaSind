package br.com.clinicaintegrada.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;

public class ArquiveCreator {

    private static Formatter output;

    public static void openFile(String arquivo) throws FileNotFoundException {
        output = new Formatter(arquivo);
    }

    public static void execFile(String arquivo, String conteudo) {
        try {

            //openFile("html/" + arquivo);
            openFile(arquivo);
            writeFile(conteudo);
            closeFile();
            if (new File(arquivo).exists()) {
                Desktop.getDesktop().open(new File(arquivo));
            }
            //Desktop.getDesktop().open(new File("html/" + arquivo));

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void writeFile(String conteudo) {
        output.format("%s", conteudo);
    }

    public static void closeFile() {
        if (output != null) {
            output.close();
        }
    }
}