package id.ac.uny.afandi.rahmad.hellouny;

/**
 * Created by rmd on 1/18/2018.
 */

class ListPengumuman {

    private String judul;
    private String link;
    private String tanggal;

    public ListPengumuman(String judul, String link, String tanggal) {
        this.judul = judul;
        this.link = link;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}

