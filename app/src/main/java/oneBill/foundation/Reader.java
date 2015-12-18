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
        Cursor c = db.rawQuery("SELECT * FROM book ORDER BY ChangeTime DESC", null);
        return c;
    }

    public Cursor QueryDeletedPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ? AND IsExist = 0", new String[]{_bookName});
        return c;
    }

    public Cursor QueryPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ? AND IsExist = 1", new String[]{_bookName});
        return c;
    }

    public Cursor QueryLog(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM log WHERE BookName = ? ORDER BY id DESC", new String[]{_bookName});
        return c;
    }

    public Cursor QueryConsumLog(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM log WHERE BookName = ? AND Type <> ? ORDER BY id DESC", new String[]{_bookName, "LOAN"});
        return c;
    }

    public double QuerySum(String _bookName) {
        Cursor c = db.rawQuery("SELECT Amount FROM log WHERE BookName = ? AND Type <> ?", new String[]{_bookName, "LOAN"});
        double sum = 0;
        while(c.moveToNext()) {
            sum += c.getDouble(c.getColumnIndex("Amount"));
        }
        return sum;
    }

    public Cursor QueryLogDetail(int _ID) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE ID = ?", new String[]{String.valueOf(_ID)});
        return c;
    }

    public Cursor QueryDetail(String _bookName, String _person) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE BookName = ? AND Name = ? ORDER BY id DESC", new String[]{_bookName, _person});
        return c;
    }

    public int QueryBookNum() {
        Cursor c = db.rawQuery("SELECT BookNum FROM const", null);
        int bookNum = 0;
        while(c.moveToNext()) {
            bookNum = c.getInt(c.getColumnIndex("BookNum"));
        }
        return bookNum;
    }

    public int QueryIDNum() {
        Cursor c = db.rawQuery("SELECT IDNum FROM const", null);
        int IDNum = 0;
        while(c.moveToNext()) {
            IDNum = c.getInt(c.getColumnIndex("IDNum"));;
        }
        return IDNum;
    }

    public void CloseDB() {
        db.close();
    }

}

