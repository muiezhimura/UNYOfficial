package com.infinite.rzzkan.uny.Model;

/**
 * Created by Rzzkan on 22/01/2018.
 */

public class DetailBeritaModel {

    private String judul;
    private String gambar;
    private String konten;

    public DetailBeritaModel(String judul, String gambar, String konten) {
        this.judul = judul;
        this.gambar = gambar;
        this.konten = konten;
    }

    public String getJudul() {
        return judul;
    }

    public String getGambar() {
        return gambar;
    }

    public String getKonten() {
        return konten;
    }
}

