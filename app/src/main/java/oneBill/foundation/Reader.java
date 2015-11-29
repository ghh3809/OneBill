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

    public Cursor QueryBook() {
        Cursor c = db.rawQuery("SELECT * FROM book ORDER BY ChangeTime", null);
        return c;
    }

    public Cursor QueryAllPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ?", new String[]{_bookName});
        return c;
    }

    public Cursor QueryPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ? AND IsExist = TRUE", new String[]{_bookName});
        return c;
    }

    public Cursor QueryLog(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM log WHERE BookName = ? ORDER BY id DESC", new String[]{_bookName});
        return c;
    }

    public Cursor QueryDetail(String _bookName, String _person) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE BookName = ? AND WHERE Name = ? ORDER BY id", new String[]{_bookName, _person});
        return c;
    }

    public int QueryBookNum() {
        Cursor c = db.rawQuery("SELECT BookNum FROM const", null);
        return c.getInt(c.getColumnIndex("BookNum"));
    }

    public int QueryIDNum() {
        Cursor c = db.rawQuery("SELECT IDNum FROM const", null);
        return c.getInt(c.getColumnIndex("IDNum"));
    }

    public void CloseDB() {
        db.close();
    }

}

