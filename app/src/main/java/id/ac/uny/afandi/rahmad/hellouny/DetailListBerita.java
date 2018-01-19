package id.ac.uny.afandi.rahmad.hellouny;

/**
 * Created by rmd on 1/18/2018.
 */

class DetailListBerita {

    private String judul;
    private String gambar;
    private String konten;

    public DetailListBerita(String judul, String gambar, String konten) {
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

