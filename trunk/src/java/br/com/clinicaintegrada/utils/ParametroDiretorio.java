package br.com.clinicaintegrada.utils;

import java.io.File;

public class ParametroDiretorio {

    private File file;
    private String fileName;
    private int index;

    public ParametroDiretorio(File file, String fileName, int index) {
        this.file = file;
        this.fileName = fileName;
        this.index = index;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
