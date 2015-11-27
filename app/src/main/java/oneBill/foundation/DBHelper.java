package oneBill.foundation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyBook.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS book" +
                "(BookName CHAR(32) PRIMARY KEY, CreateTime DATETIME, ChangeTime DATETIME, Sum DOUBLE)");
        db.execSQL("CREATE TABLE IF NOT EXISTS person" +
                "(Name CHAR(32), BookName CHAR(32), PRIMARY KEY(Name, BookName), FOREIGN KEY(BookName) REFERENCES book(BookName))");
        db.execSQL("CREATE TABLE IF NOT EXISTS log" +
                "(ID INT PRIMARY KEY, Time DATETIME, Type INT, BookName CHAR(32), Amount DOUBLE, FOREIGN KEY(BookName) REFERENCES book(BookName))");
        db.execSQL("CREATE TABLE IF NOT EXISTS detail" +
                "(ID INT, Name CHAR(32), BookName CHAR(32), Paid DOUBLE, Payable DOUBLE, FOREIGN KEY(id) REFERENCES log(id), FOREIGN KEY(Name) REFERENCES person(Name), FOREIGN KEY(BookName) REFERENCES person(BookName))");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE person ADD COLUMN other STRING");
    }
}
