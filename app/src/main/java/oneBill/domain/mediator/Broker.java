package oneBill.domain.mediator;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.foundation.Reader;
import oneBill.foundation.Writer;

/**
 * Created by 豪豪 on 2015/11/29.
 */
public class Broker {

    private Writer writer;
    private Reader reader;

    public Broker(Context context) {
        writer = new Writer(context);
        reader = new Reader(context);
    }

    public void CreateBook(String _bookName) throws DuplicationNameException {
        ArrayList<String> books = GetBook();
        for(int i = 0; i < books.size(); i ++) {
            if(books.get(i).equals(_bookName)) throw new DuplicationNameException();
        }
        writer.AddBook(_bookName);
        writer.UpdateBookNumber();
    }

    public ArrayList<String> GetBook() {
        ArrayList<String> books = new ArrayList<String>();
        Cursor c = reader.QueryBook();
        while(c.moveToNext()) {
            books.add(c.getString(c.getColumnIndex("BookName")));
        }
        return books;
    }

    public String GetDefaultName() {
        return "账本" + (reader.QueryBookNum() + 1);
    }

    public void DeleteBook(String _bookName) {
        writer.DeleteBook(_bookName);
    }

    public void CreateMember(String _bookName, String _person) throws DuplicationNameException {
        Cursor c = reader.QueryAllPerson(_bookName);
        ArrayList<String> persons = new ArrayList<String>();
        while(c.moveToNext()) {
            persons.add(c.getString(c.getColumnIndex("Name")));
        }
        for(int i = 0; i < persons.size(); i ++) {
            if(persons.get(i).equals(_person)) throw new DuplicationNameException();
        }
        writer.AddPerson(_bookName, _person);
    }

    public ArrayList<String> GetMember(String _bookName) {
        Cursor c = reader.QueryPerson(_bookName);
        ArrayList<String> persons = new ArrayList<String>();
        while(c.moveToNext()) {
            persons.add(c.getString(c.getColumnIndex("Name")));
        }
        return persons;
    }

    public void SetName(String _oldName, String _newName) throws DuplicationNameException {
        ArrayList<String> books = GetBook();
        for(int i = 0; i < books.size(); i ++) {
            if(books.get(i).equals(_newName)) throw new DuplicationNameException();
        }
        writer.UpdateBookName(_oldName, _newName);
    }

    public void DeleteMember(String _bookName, String _person) {
        writer.DeletePerson(_bookName, _person);
    }

    public ArrayList<ArrayList<String>> GetRecord(String _bookName) {
        Cursor c = reader.QueryLog(_bookName);
        ArrayList<ArrayList<String>> logs = new ArrayList<ArrayList<String>>();
        while(c.moveToNext()) {
            ArrayList<String> log = new ArrayList<String>();
            log.add(0, c.getString(c.getColumnIndex("Time")));
            log.add(1, String.valueOf(c.getDouble(c.getColumnIndex("Amount"))));
            log.add(2, c.getString(c.getColumnIndex("Type")));
            logs.add(log);
        }
        return logs;
    }

}
