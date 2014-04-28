package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by benoc on 28/04/2014.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "table.db";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        Table.onCreate(database);
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Table.onUpgrade(database, oldVersion, newVersion);
    }
}
