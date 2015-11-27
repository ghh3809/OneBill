package oneBill.foundation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class Reader {

    private DBHelper helper;
    private SQLiteDatabase db;

    public Reader(Context context) {
        helper = new DBHelper(context);
        db = helper.getReadableDatabase();
    }

    public Cursor queryPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ?", new String[]{_bookName});
        return c;
    }

    public Cursor queryConsumRecord(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM log WHERE BookName = ? ORDER BY id", new String[]{_bookName});
        return c;
    }

    public Cursor queryConsumLog(String _bookName, String _name) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE BookName = ? AND WHERE Name = ? ORDER BY id", new String[]{_bookName, _name});
        return c;
    }

    public void closeDB() {
        db.close();
    }

}

