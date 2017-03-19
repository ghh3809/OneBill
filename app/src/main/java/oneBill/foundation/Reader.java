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

    /**
     * 从book表中按倒序获取所有账本
     * @return
     */
    public Cursor QueryBook() {
        Cursor c = db.rawQuery("SELECT * FROM book ORDER BY ChangeTime DESC", null);
        return c;
    }

    /**
     * person表中查找属于bookname的某人是否被删除
     * @param _bookName
     * @return
     */
    public Cursor QueryDeletedPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ? AND IsExist = 0", new String[]{_bookName});
        return c;
    }

    /**
     * person表中查询所有存在的人
     * @param _bookName
     * @return
     */
    public Cursor QueryPerson(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM person WHERE BookName = ? AND IsExist = 1", new String[]{_bookName});
        return c;
    }

    /**
     * log表中查询所有bookname账本记录,时间倒序
     * @param _bookName
     * @return
     */
    public Cursor QueryLog(String _bookName) {
        Cursor c = db.rawQuery("SELECT * FROM log WHERE BookName = ? ORDER BY id DESC", new String[]{_bookName});
        return c;
    }

    /**
     * log表中查询bookname账本中所有消费记录之和
     * @param _bookName
     * @return
     */
    public double QuerySum(String _bookName) {
        Cursor c = db.rawQuery("SELECT Amount FROM log WHERE BookName = ? AND Type <> ?", new String[]{_bookName, "借贷"});
        double sum = 0;
        while(c.moveToNext()) {
            sum += c.getDouble(c.getColumnIndex("Amount"));
        }
        return sum;
    }

    /**
     * detail表中查询某id对应详情
     * @param _ID
     * @return
     */
    public Cursor QueryLogDetail(int _ID) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE ID = ?", new String[]{String.valueOf(_ID)});
        return c;
    }

    /**
     * detail表中查询bookname账本中person消费详情
     * @param _bookName
     * @param _person
     * @return
     */
    public Cursor QueryDetail(String _bookName, String _person) {
        Cursor c = db.rawQuery("SELECT * FROM detail WHERE BookName = ? AND Name = ? ORDER BY id DESC", new String[]{_bookName, _person});
        return c;
    }

    /**
     * 查询账本总数
     * @return
     */
    public int QueryBookNum() {
        Cursor c = db.rawQuery("SELECT BookNum FROM const", null);
        int bookNum = 0;
        while(c.moveToNext()) {
            bookNum = c.getInt(c.getColumnIndex("BookNum"));
        }
        return bookNum;
    }

    /**
     * 查询log记录总数
     * @return
     */
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

