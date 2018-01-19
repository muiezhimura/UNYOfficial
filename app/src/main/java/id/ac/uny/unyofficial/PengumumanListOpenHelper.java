package id.ac.uny.unyofficial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by groovyle on 11/29/2017.
 */

public class PengumumanListOpenHelper extends SQLiteOpenHelper {
    // Tag for logging
    private static final String TAG = PengumumanListOpenHelper.class.getSimpleName();

    // Has to be 1 first time or app will crash
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "unyofficial_app"; // PLEASE STANDARDIZE
    private static final String PENGUMUMAN_LIST_TABLE = "pengumuman";

    public static final String CALENDAR_DATETIME_FORMAT = "yyyy-MM-dd kk:mm:ss";

    // Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_URL_ID = "url_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POST_DATE = "post_date";
    public static final String KEY_CONTENTS = "contents";
    public static final String KEY_PLAIN_CONTENTS = "plain_contents";

    // compacted column names
    public static final String[] COLUMNS = { KEY_ID, KEY_URL_ID, KEY_TITLE, KEY_POST_DATE, KEY_CONTENTS, KEY_PLAIN_CONTENTS };

    public PengumumanListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Queries
    private static final String PENGUMUMAN_LIST_TABLE_CREATE =
            "CREATE TABLE "+ PENGUMUMAN_LIST_TABLE +" ("+
            KEY_ID +" INTEGER PRIMARY KEY, "+
            KEY_URL_ID +" TEXT UNIQUE, "+
            KEY_TITLE +" TEXT, "+
            KEY_POST_DATE +" LONG, "+
            KEY_CONTENTS +" TEXT, "+
            KEY_PLAIN_CONTENTS +" TEXT "+
            ");";


    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PENGUMUMAN_LIST_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long count() {
        if(mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, PENGUMUMAN_LIST_TABLE);
    }

    // Please note that page begins from 0, not 1
    public ArrayList<PengumumanModel> getPengumumanByPage(int page) {
        return this.getPengumumanByPage(page, IndexPengumuman.DISPLAY_PER_PAGE);
    }

    public ArrayList<PengumumanModel> getPengumumanByPage(int page, int howMany) {
        int start = page * howMany;
        String query = "SELECT * FROM "+ PENGUMUMAN_LIST_TABLE
                +" ORDER BY "+ KEY_POST_DATE +" DESC LIMIT ? OFFSET ?;";
        Cursor cursor = null;
        ArrayList<PengumumanModel> list = new ArrayList<>();

        try {
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{ String.valueOf(howMany), String.valueOf(start) });
            if(cursor.moveToFirst()) {
                do {
                    Calendar postDate = Calendar.getInstance();
                    postDate.setTime(new Date(cursor.getLong(cursor.getColumnIndex(KEY_POST_DATE))));
                    PengumumanModel item = new PengumumanModel(
                            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                            postDate,
                            cursor.getString(cursor.getColumnIndex(KEY_CONTENTS)),
                            cursor.getString(cursor.getColumnIndex(KEY_PLAIN_CONTENTS)),
                            cursor.getString(cursor.getColumnIndex(KEY_URL_ID))
                    );
                    item.setDbId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
                    list.add(item);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("qwerty", "getPengumumanByPage: error "+ e.getMessage());
        } finally {
            if(cursor != null) cursor.close();
            return list;
        }
    }

    public PengumumanModel getPengumumanByUrlId(String urlId) {
        String query = "SELECT * FROM "+ PENGUMUMAN_LIST_TABLE
                +" WHERE "+ KEY_URL_ID +" = ? LIMIT 1;";
        Cursor cursor = null;
        PengumumanModel item = null;

        try {
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{ urlId });
            if(cursor.moveToFirst()) {
                Calendar postDate = Calendar.getInstance();
                Log.d("asdf", "getPengumumanByUrlId: date is "+ cursor.getLong(cursor.getColumnIndex(KEY_POST_DATE)));
                postDate.setTime(new Date(cursor.getLong(cursor.getColumnIndex(KEY_POST_DATE))));
                item = new PengumumanModel(
                        cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                        postDate,
                        cursor.getString(cursor.getColumnIndex(KEY_CONTENTS)),
                        cursor.getString(cursor.getColumnIndex(KEY_PLAIN_CONTENTS)),
                        cursor.getString(cursor.getColumnIndex(KEY_URL_ID))
                );
                item.setDbId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            }
        } catch (Exception e) {
            Log.d("qwerty", "getPengumumanByUrlId: error "+ e.getMessage());
        } finally {
            if(cursor != null) cursor.close();
            return item;
        }
    }

    public boolean checkExists(String urlIdentifier) {
        String query = "SELECT COUNT("+ KEY_ID +") AS `total` FROM "+ PENGUMUMAN_LIST_TABLE
                +" WHERE "+ KEY_URL_ID +" = ?;";
        Cursor cursor = null;
        boolean exists = false;

        try {
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, new String[]{ urlIdentifier });
            cursor.moveToFirst();
            int total = cursor.getInt(cursor.getColumnIndex("total"));
            exists = total > 0;
        } catch (Exception e) {
            Log.d("qwerty", "checkExists: error : "+ e.getMessage());
        } finally {
            if(cursor != null) cursor.close();
            return exists;
        }
    }

    public Calendar getMaxPostDate() {
        String query = "SELECT "+ KEY_POST_DATE +" FROM "+ PENGUMUMAN_LIST_TABLE +" ORDER BY "+ KEY_POST_DATE +" DESC LIMIT 1;";
        Cursor cursor = null;
        Calendar maxPostDate = null;

        try {
            if(mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            long max = cursor.getLong(cursor.getColumnIndex(KEY_POST_DATE));
            maxPostDate = Calendar.getInstance();
            maxPostDate.setTime(new Date(max));
        } catch (Exception e) {
            Log.d("qwerty", "getMaxPostDate: error query maxDate: "+ e.getMessage());
            return null;
        } finally {
            if(cursor != null) cursor.close();
            return maxPostDate;
        }
    }

    public long insert(String title, Calendar postDate, String contents, String plainContents, String urlIdentifier) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);

        // Convert post date first
        long postDateLong;
        if(postDate == null) {
            postDateLong = 0;
        } else {
            postDateLong = postDate.getTime().getTime(); // milliseconds to seconds
        }

        values.put(KEY_POST_DATE, postDateLong);
        values.put(KEY_CONTENTS, contents);
        values.put(KEY_PLAIN_CONTENTS, plainContents);
        values.put(KEY_URL_ID, urlIdentifier);

        try {
            if(mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }

            newId = mWritableDB.insert(PENGUMUMAN_LIST_TABLE, null, values);
        } catch(Exception e) {
            Log.d("qwerty", "insert: insert exception! "+ e.getMessage());
        }

        return newId;
    }

    public long insert(PengumumanModel pengumuman) {
        return this.insert(pengumuman.getTitle(),
                pengumuman.getPostDate(),
                pengumuman.getContents(),
                pengumuman.getPlainContents(),
                pengumuman.getUrlIdentifier()
        );
    }

    // Can update urlIdentifier too if you supply ID.
    // If ID is not supplied then urlIdentifier is used as key
    public int update(String title, String contents, String plainContents, String urlIdentifier, long dbId) {
        int affectedRows = -1;
        try {
            if(mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, title);

            // Post date can't be updated.
            // Besides, it's suuuper weird that we can't get the time
            // part from the specific Pengumuman's page. Only the date.
            // Thus the entry from index-pengumuman is more accurate.

            values.put(KEY_CONTENTS, contents);
            values.put(KEY_PLAIN_CONTENTS, plainContents);

            String whereClause;
            String[] whereParams;
            if(dbId < 1) {
                whereClause = KEY_ID +" = ?";
                whereParams = new String[] { String.valueOf(dbId) };
                values.put(KEY_URL_ID, urlIdentifier);
            } else {
                whereClause = KEY_URL_ID +" = ?";
                whereParams = new String[] { urlIdentifier };
            }

            affectedRows = mWritableDB.update(PENGUMUMAN_LIST_TABLE,
                    values,
                    whereClause,
                    whereParams
            );
        } catch (Exception e) {
            Log.d("qwerty", "update: update exception! "+ e.getMessage());
        }

        return affectedRows;
    }

    public int update(PengumumanModel pengumuman) {
        return this.update(pengumuman.getTitle(),
                pengumuman.getContents(),
                pengumuman.getPlainContents(),
                pengumuman.getUrlIdentifier(),
                pengumuman.getDbId()
        );
    }
}
