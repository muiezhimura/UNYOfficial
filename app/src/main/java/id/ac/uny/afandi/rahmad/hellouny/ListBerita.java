package id.ac.uny.afandi.rahmad.hellouny;

/**
 * Created by rmd on 1/18/2018.
 */

class ListBerita {

    private String judul;
    private String link;
    private String gambar;
    private String tanggal;

    public ListBerita(String judul, String link, String gambar, String tanggal) {
        this.judul = judul;
        this.link = link;
        this.gambar = gambar;
        this.tanggal = tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}

