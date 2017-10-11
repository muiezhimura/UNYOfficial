package id.ac.uny.unyofficial;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by groovyle on 10/10/2017.
 */

public class PengumumanModel {
    // Refer to http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html
    public static String defaultDateFormat = "dd MMM yyyy, kk:mm";
    public static String defaultDateFormatShort = "dd-MM-yyyy kk:mm";

    protected String title;
    protected Calendar postDate;
    protected String contents;
    protected String urlIdentifier;

    public PengumumanModel(String title, Calendar postDate, String contents, String urlIdentifier) {
        this.title = title;
        this.postDate = postDate;
        this.contents = contents;
        this.urlIdentifier = urlIdentifier;
    }

    public PengumumanModel(String title, String postDate, String contents, String urlIdentifier) {
        this.title = title;
        this.contents = contents;
        this.urlIdentifier = urlIdentifier;

        // Parse the postDate string here
    }

    public String getTitle() {
        return title;
    }

    public Calendar getPostDate() {
        return postDate;
    }

    // Format is format string used in SimpleDateFormat
    public String getFormattedPostDate(String formatString) {
        if(postDate == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(postDate.getTime());
    }

    // An overload for a default format
    public String getFormattedPostDate() {
        return this.getFormattedPostDate(defaultDateFormat);
    }

    public String getContents() {
        return contents;
    }

    public String getUrlIdentifier() {
        return urlIdentifier;
    }

}
