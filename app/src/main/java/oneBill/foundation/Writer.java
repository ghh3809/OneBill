package oneBill.foundation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oneBill.domain.entity.Type;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class Writer {

    private DBHelper helper;
    private SQLiteDatabase db;

    public Writer(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void AddBook(String _bookName) {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime = formatter.format(curDate);
        ContentValues cv = new ContentValues();
        cv.put("BookName", _bookName);
        cv.put("CreateTime", nowTime);
        cv.put("ChangeTime", nowTime);
        cv.put("Sum", 0);
        db.insert("Book", null, cv);
    }

    public void AddPerson(String _bookName ,ArrayList<String> _persons) {
        db.beginTransaction();
        try {
            for (String person : _persons) {
                db.execSQL("INSERT INTO person VALUES(?, ?, ?)", new String[]{person, _bookName, "1"});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void AddPerson(String _bookName, String _person) {
        db.execSQL("INSERT INTO person VALUES(?, ?, ?)", new String[]{_person, _bookName, "1"});
    }

    public void AddLog(int _id, String _type, String _bookName, double _amount) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime = formatter.format(curDate);
        ContentValues cv = new ContentValues();
        cv.put("ID", _id);
        cv.put("Time", nowTime);
        cv.put("Type", _type);
        cv.put("BookName", _bookName);
        cv.put("Amount", _amount);
        db.insert("log", null, cv);
    }

    public void AddDetail(int _id, String _bookName, String _person, double _paid, double _payable) {
        ContentValues cv = new ContentValues();
        cv.put("ID", _id);
        cv.put("Name", _person);
        cv.put("BookName", _bookName);
        cv.put("Paid", _paid);
        cv.put("Payable", _payable);
        if ((_paid > 1E-5) || (_payable > 1E-5)) db.insert("detail", null, cv);
    }

    public void AddDetails(int _id, String _bookName, ArrayList<String> _persons, ArrayList<Double> _paid, ArrayList<Double> _payable) {
        db.beginTransaction();
        int n = _persons.size();
        try {
            for(int i = 0; i < n; i ++) {
                AddDetail(_id, _bookName, _persons.get(i), _paid.get(i), _payable.get(i));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void AddSum(String _bookName, double _amount) {
        Cursor c = db.rawQuery("SELECT Sum FROM book WHERE BookName = ?", new String[]{_bookName});
        double sum = 0;
        while(c.moveToNext()) {
            sum = c.getDouble(c.getColumnIndex("Sum"));
        }
        sum += _amount;
        ContentValues cv = new ContentValues();
        cv.put("Sum", sum);
        db.update("book", cv, "BookName = ?", new String[]{_bookName});
    }

    public void DeleteBook(String _bookName) {
        db.delete("book", "BookName = ?", new String[]{_bookName});
        db.delete("person", "BookName = ?", new String[]{_bookName});
        db.delete("log", "BookName = ?", new String[]{_bookName});
        db.delete("detail", "BookName = ?", new String[]{_bookName});
    }

    public void DeletePerson(String _bookName, String _person) {
        ContentValues cv = new ContentValues();
        cv.put("IsExist", 0);
        db.update("person", cv, "BookName = ? AND Name = ?", new String[]{_bookName, _person});
    }

    public void DeleteLog(int _id) {
        Cursor c = db.rawQuery("SELECT Amount,BookName FROM log WHERE ID = ?", new String[]{String.valueOf(_id)});
        double amount = 0;
        String bookName = "";
        while(c.moveToNext()) {
            amount = c.getDouble(c.getColumnIndex("Amount"));
            bookName = c.getString(c.getColumnIndex("BookName"));
        }
        AddSum(bookName, -amount);
        UpdateBookTime(bookName);
        db.delete("log", "ID = ?", new String[]{String.valueOf(_id)});
        db.delete("detail", "ID = ?", new String[]{String.valueOf(_id)});
    }

    public void UpdateBookTime(String _bookName) {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime = formatter.format(curDate);
        ContentValues cv = new ContentValues();
        cv.put("ChangeTime", nowTime);
        db.update("book", cv, "BookName = ?", new String[]{_bookName});
    }

    public void UpdateBookName(String _oldName, String _newName) {
        ContentValues cv = new ContentValues();
        cv.put("BookName", _newName);
        db.update("book", cv, "BookName = ?", new String[]{_oldName});
        db.update("person", cv, "BookName = ?", new String[]{_oldName});
        db.update("log", cv, "BookName = ?", new String[]{_oldName});
        db.update("detail", cv, "BookName = ?", new String[]{_oldName});
    }

    public void UpdateBookNumber() {
        Cursor c = db.rawQuery("SELECT BookNum FROM const", null);
        int BookNum = 0;
        while(c.moveToNext()) {
            BookNum = c.getInt(c.getColumnIndex("BookNum"));
        }
        BookNum ++;
        ContentValues cv = new ContentValues();
        cv.put("BookNum", BookNum);
        db.update("const", cv, null, null);
    }

    public void UpdateIDNumber() {
        Cursor c = db.rawQuery("SELECT IDNum FROM const", null);
        int IDNum = 0;
        while(c.moveToNext()) {
            IDNum = c.getInt(c.getColumnIndex("IDNum"));
        }
        IDNum ++;
        ContentValues cv = new ContentValues();
        cv.put("IDNum", IDNum);
        db.update("const", cv, null, null);
    }

    public void CloseDB() {
        db.close();
    }
}
