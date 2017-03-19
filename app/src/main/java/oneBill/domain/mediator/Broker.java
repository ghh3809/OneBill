package oneBill.domain.mediator;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;

import oneBill.domain.entity.Book;
import oneBill.domain.entity.Detail;
import oneBill.domain.entity.Log;
import oneBill.domain.entity.Person;
import oneBill.domain.entity.SettleModify;
import oneBill.domain.entity.Solution;
import oneBill.domain.entity.Type;
import oneBill.domain.entity.error.AmountMismatchException;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.MemberReturnException;
import oneBill.domain.entity.error.UnableToClearException;
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

    /**
     * 新增账本
     * @param _bookName
     * @throws DuplicationNameException
     */
    public void CreateBook(String _bookName) throws DuplicationNameException {
        ArrayList<Book> books = GetBooks();
        for(int i = 0; i < books.size(); i ++) {
            if(books.get(i).getName().equals(_bookName)) throw new DuplicationNameException();
        }
        writer.AddBook(_bookName);
        writer.UpdateBookNumber();
    }

    /**
     * 获取所有账本
     * @return 所有book
     */
    public ArrayList<Book> GetBooks() {
        ArrayList<Book> books = new ArrayList<>();
        Cursor c = reader.QueryBook();
        while(c.moveToNext()) {
            String bookName = c.getString(c.getColumnIndex("BookName"));
            String changeTime = c.getString(c.getColumnIndex("ChangeTime"));
            double sum = c.getDouble(c.getColumnIndex("Sum"));
            books.add(new Book(bookName, changeTime, sum));
        }
        return books;
    }

    /**
     * 获取默认账本名
     * @return
     */
    public String GetDefaultName() {
        return "账本" + (reader.QueryBookNum() + 1);
    }

    /**
     * 删除账本
     * @param _bookName
     */
    public void DeleteBook(String _bookName) {
        writer.DeleteBook(_bookName);
    }

    /**
     * 新增成员
     * @param _bookName
     * @param _person
     * @throws DuplicationNameException
     * @throws MemberReturnException
     */
    public void CreateMember(String _bookName, String _person) throws DuplicationNameException, MemberReturnException {
        Cursor c = reader.QueryDeletedPerson(_bookName);
        ArrayList<String> persons = new ArrayList<>();
        while(c.moveToNext()) {
            persons.add(c.getString(c.getColumnIndex("Name")));
        }
        for(int i = 0; i < persons.size(); i ++) {
            if(persons.get(i).equals(_person)) {
                writer.UpdatePerson(_bookName, _person);
                throw new MemberReturnException();
            }
        }
        c = reader.QueryPerson(_bookName);
        persons = new ArrayList<>();
        while(c.moveToNext()) {
            persons.add(c.getString(c.getColumnIndex("Name")));
        }
        for(int i = 0; i < persons.size(); i ++) {
            if(persons.get(i).equals(_person)) throw new DuplicationNameException();
        }
        writer.AddPerson(_bookName, _person);
        writer.UpdateBookTime(_bookName);

    }

    /**
     * 获取所有成员
     * @param _bookName
     * @return
     */
    public ArrayList<Person> GetMembers(String _bookName) {
        Cursor c = reader.QueryPerson(_bookName);
        ArrayList<Person> persons = new ArrayList<>();
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("Name"));
            double net = this.QueryNetAmount(_bookName, name);
            persons.add(new Person(name, net));
        }
        return persons;
    }

    /**
     * 获取所有成员名字
     * @param _bookName
     * @return
     */
    public ArrayList<String> GetMemberNames(String _bookName) {
        Cursor c = reader.QueryPerson(_bookName);
        ArrayList<String> persons = new ArrayList<>();
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("Name"));
            persons.add(name);
        }
        return persons;
    }

    /**
     * 账本改名
     * @param _oldName
     * @param _newName
     * @throws DuplicationNameException
     */
    public void SetName(String _oldName, String _newName) throws DuplicationNameException {
        ArrayList<Book> books = GetBooks();
        for(int i = 0; i < books.size(); i ++) {
            if(books.get(i).equals(_newName)) throw new DuplicationNameException();
        }
        writer.UpdateBookName(_oldName, _newName);
    }

    /**
     * 删除成员
     * @param _bookName
     * @param _person
     */
    public void DeleteMember(String _bookName, String _person) {
        writer.DeletePerson(_bookName, _person);
        writer.UpdateBookTime(_bookName);
    }

    /**
     * 获取账本总金额
     * @param _bookName
     * @return
     */
    public double GetSum(String _bookName) {
        return reader.QuerySum(_bookName);
    }

    /**
     * 获取所有记录
     * @param _bookName
     * @return
     */
    public ArrayList<Log> GetRecord(String _bookName) {
        Cursor c = reader.QueryLog(_bookName);
        ArrayList<Log> logs = new ArrayList<>();
        while(c.moveToNext()) {
            int ID = c.getInt(c.getColumnIndex("ID"));
            String time = c.getString(c.getColumnIndex("Time")).substring(0, 16);
            String type = c.getString(c.getColumnIndex("Type"));
            double amount = c.getDouble(c.getColumnIndex("Amount"));
            logs.add(new Log(ID, type, time, amount));
        }
        return logs;
    }

    /**
     * 获取某ID记录的详情
     * @param _ID
     * @return
     */
    public ArrayList<Detail> GetDetail(int _ID) {
        ArrayList<Detail> details = new ArrayList<>();
        Cursor c = reader.QueryLogDetail(_ID);
        while(c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("Name"));
            double paid = c.getDouble(c.getColumnIndex("Paid"));
            double payable = c.getDouble(c.getColumnIndex("Payable"));
            details.add(new Detail(name, paid, payable));
        }
        return details;
    }

    /**
     * 新增消费记录
     * @param _bookName
     * @param _type
     * @param _paid
     * @param _payable
     * @throws AmountMismatchException
     */
    public void CreateConsumeRecord(String _bookName, String _type, ArrayList<Double> _paid, ArrayList<Double> _payable) throws AmountMismatchException {
        double sumPaid = 0;
        double sumPayable = 0;
        for(int i = 0; i < _paid.size(); i ++) {
            sumPaid += _paid.get(i);
            sumPayable += _payable.get(i);
        }
        if(Math.abs(sumPaid - sumPayable) > 1E-5) throw new AmountMismatchException(sumPaid, sumPayable);
        if (Math.abs(sumPaid) < 1E-5) return;
        int id = reader.QueryIDNum() + 1;
        writer.AddLog(id, _type, _bookName, sumPaid);
        writer.AddDetails(id, _bookName, GetMemberNames(_bookName), _paid, _payable);
        writer.AddSum(_bookName, sumPaid);
        writer.UpdateBookTime(_bookName);
        writer.UpdateIDNumber();
    }

    /**
     * 新增借款记录
     * @param _bookName
     * @param _lender
     * @param _borrower
     * @param _amount
     */
    public void CreateLoanRecord(String _bookName, String _lender, String _borrower, double _amount) {
        int id = reader.QueryIDNum() + 1;
        writer.AddLog(id, "借贷", _bookName, _amount);
        writer.AddDetail(id, _bookName, _lender, _amount, 0);
        writer.AddDetail(id, _bookName, _borrower, 0, _amount);
        writer.UpdateBookTime(_bookName);
        writer.UpdateIDNumber();
    }

    /**
     * 删除记录
     * @param _ID
     */
    public void DeleteRecord(int _ID) {
        writer.DeleteLog(_ID);
    }

    /**
     * 查询净支付额
     * @param _bookName
     * @param _person
     * @return
     */
    public double QueryNetAmount(String _bookName, String _person) {
        Cursor c = reader.QueryDetail(_bookName, _person);
        double paid = 0, payable = 0;
        while(c.moveToNext()) {
            paid += c.getDouble(c.getColumnIndex("Paid"));
            payable += c.getDouble(c.getColumnIndex("Payable"));
        }
        return paid - payable;
    }

    /**
     * 单人清账
     * @param _bookName
     * @param _person
     * @param _trader
     */
    public void SettlePerson(String _bookName, String _person, String _trader) {
        double net = QueryNetAmount(_bookName, _person);
        if(net > 0) CreateLoanRecord(_bookName, _trader, _person, net);
        else if(net < 0) CreateLoanRecord(_bookName, _person, _trader, -net);
        DeleteMember(_bookName, _person);
    }

    /**
     * 全员清账
     * @param _bookName
     * @param constrint
     * @return
     * @throws UnableToClearException
     */
    public ArrayList<Solution> SettleRecord(String _bookName, ArrayList<Solution> constrint) throws UnableToClearException {
        SettleModify settlemodify = new SettleModify();
        ArrayList<Person> persons = GetMembers(_bookName);
        return settlemodify.SettleTest(persons, constrint);
    }

    public void CloseDataBase() {
        reader.CloseDB();
        writer.CloseDB();
    }

}
