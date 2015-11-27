package oneBill.foundation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import oneBill.entity.Person;
import oneBill.entity.Type;

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

    public void AddPerson(ArrayList<String> _persons, String _bookName) {
        db.beginTransaction();
        try {
            for (String person : _persons) {
                db.execSQL("INSERT INTO person VALUES(?, ?)", new String[]{person, _bookName});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void AddPerson(String _person, String _bookName) {
        db.execSQL("INSERT INTO person VALUES(?, ?)", new String[]{_person, _bookName});
    }

    public void AddLog(int _id, Type _type, String _bookName, double _amount) {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String nowTime = formatter.format(curDate);
        ContentValues cv = new ContentValues();
        cv.put("ID", _id);
        cv.put("Type", _type.toString());
        cv.put("BookName", _bookName);
        cv.put("Amount", _amount);
        db.insert("log", null, cv);
    }

    public void AddDetail(int _id, String _bookName, ArrayList<String> _persons, ArrayList<Double> _paid, ArrayList<Double> _payable) {
        db.beginTransaction();
        int n = _persons.size();
        try {
            for(int i = 0; i < n; i ++) {
                ContentValues cv = new ContentValues();
                cv.put("ID", _id);
                cv.put("Name", _persons.get(i));
                cv.put("BookName", _bookName);
                cv.put("Paid", _paid.get(i));
                cv.put("Payable", _payable.get(i));
                db.insert("detail", null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteBook(String _bookName) {

    }

    public void deletePerson(String _person) {

    }

//    /**
//     * update person's age
//     * @param person
//     */
//    public void updateAge(Person person) {
//        ContentValues cv = new ContentValues();
//        cv.put("age", person.age);
//        db.update("person", cv, "name = ?", new String[]{person.name});
//    }
//
//    /**
//     * delete old person
//     * @param person
//     */
//    public void deleteOldPerson(Person person) {
//        db.delete("person", "age >= ?", new String[]{String.valueOf(person.age)});
//    }
//
//    /**
//     * query all persons, return list
//     * @return List<Person>
//     */
//    public List<Person> query() {
//        ArrayList<Person> persons = new ArrayList<Person>();
//        Cursor c = queryTheCursor();
//        while (c.moveToNext()) {
//            Person person = new Person();
//            person._id = c.getInt(c.getColumnIndex("_id"));
//            person.name = c.getString(c.getColumnIndex("name"));
//            person.age = c.getInt(c.getColumnIndex("age"));
//            person.info = c.getString(c.getColumnIndex("info"));
//            persons.add(person);
//        }
//        c.close();
//        return persons;
//    }
//
//    /**
//     * query all persons, return cursor
//     * @return	Cursor
//     */
//    public Cursor queryTheCursor() {
//        Cursor c = db.rawQuery("SELECT * FROM person", null);
//        return c;
//    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
