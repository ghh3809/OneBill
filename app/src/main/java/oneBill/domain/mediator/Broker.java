package oneBill.domain.mediator;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Iterator;

import oneBill.domain.entity.Person;
import oneBill.domain.entity.Solution;
import oneBill.domain.entity.Type;
import oneBill.domain.entity.error.AmountMismatchException;
import oneBill.domain.entity.error.DuplicationNameException;
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
        writer.UpdateBookTime(_bookName);
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
        writer.UpdateBookTime(_bookName);
    }

    public double GetSum(String _bookName) {
        return reader.QuerySum(_bookName);
    }

    public ArrayList<ArrayList<String>> GetRecord(String _bookName) {
        Cursor c = reader.QueryLog(_bookName);
        ArrayList<ArrayList<String>> logs = new ArrayList<ArrayList<String>>();
        while(c.moveToNext()) {
            ArrayList<String> log = new ArrayList<String>();
            log.add(0, c.getString(c.getColumnIndex("ID")));
            log.add(1, c.getString(c.getColumnIndex("Time")).substring(0, 16));
            log.add(2, String.valueOf(c.getDouble(c.getColumnIndex("Amount"))));
            log.add(3, c.getString(c.getColumnIndex("Type")));
            logs.add(log);
        }
        return logs;
    }

    public ArrayList<ArrayList<String>> GetConsumRecord(String _bookName) {
        Cursor c = reader.QueryConsumLog(_bookName);
        ArrayList<ArrayList<String>> logs = new ArrayList<ArrayList<String>>();
        while(c.moveToNext()) {
            ArrayList<String> log = new ArrayList<String>();
            log.add(0, c.getString(c.getColumnIndex("Time")).substring(0, 16));
            log.add(1, String.valueOf(c.getDouble(c.getColumnIndex("Amount"))));
            log.add(2, c.getString(c.getColumnIndex("Type")));
            logs.add(log);
        }
        return logs;
    }

    public ArrayList<ArrayList<String>> GetDetail(int _ID) {
        ArrayList<ArrayList<String>> logdetail = new ArrayList<ArrayList<String>>();
        Cursor c = reader.QueryLogDetail(_ID);
        while(c.moveToNext()) {
            ArrayList<String> detail = new ArrayList<String>();
            detail.add(0, c.getString(c.getColumnIndex("Name")));
            detail.add(1, String.valueOf(c.getDouble(c.getColumnIndex("Paid"))));
            detail.add(2, String.valueOf(c.getDouble(c.getColumnIndex("Payable"))));
            logdetail.add(detail);
        }
        return logdetail;
    }

    public void CreateConsumRecord(String _bookName, int _type, ArrayList<Double> _paid, ArrayList<Double> _payable) throws AmountMismatchException {
        double sumPaid = 0;
        double sumPayable = 0;
        Type type = Type.FOOD;
        switch(_type) {
            case 1: type = Type.FOOD; break;
            case 2: type = Type.TRANS; break;
            case 3: type = Type.PLAY; break;
            case 4: type = Type.ACCOM; break;
            case 5: type = Type.OTHER; break;
        }
        for(int i = 0; i < _paid.size(); i ++) {
            sumPaid += _paid.get(i);
            sumPayable += _payable.get(i);
        }

        if(Math.abs(sumPaid - sumPayable) > 1E-5) throw new AmountMismatchException(sumPaid, sumPayable);
        int id = reader.QueryIDNum() + 1;
        writer.AddLog(id, type.toString(), _bookName, sumPaid);
        writer.AddDetails(id, _bookName, GetMember(_bookName), _paid, _payable);
        writer.AddSum(_bookName, sumPaid);
        writer.UpdateBookTime(_bookName);
        writer.UpdateIDNumber();
    }

    public void CreateLoanRecord(String _bookName, String _lender, String _borrower, double _amount) {
        Type type = Type.LOAN;
        int id = reader.QueryIDNum() + 1;
        writer.AddLog(id, type.toString(), _bookName, _amount);
        writer.AddDetail(id, _bookName, _lender, _amount, 0);
        writer.AddDetail(id, _bookName, _borrower, 0, _amount);
        writer.UpdateBookTime(_bookName);
        writer.UpdateIDNumber();
    }

    public void DeleteRecord(int _ID) {
        writer.DeleteLog(_ID);
    }

    public double QueryNetAmount(String _bookName, String _person) {
        Cursor c = reader.QueryDetail(_bookName, _person);
        double paid = 0, payable = 0;
        while(c.moveToNext()) {
            paid += c.getDouble(c.getColumnIndex("Paid"));
            payable += c.getDouble(c.getColumnIndex("Payable"));
        }
        return paid - payable;
    }

    public void SettlePerson(String _bookName, String _person, String _trader) {
        double net = QueryNetAmount(_bookName, _person);
        if(net > 0) CreateLoanRecord(_bookName, _trader, _person, net);
        else if(net < 0) CreateLoanRecord(_bookName, _person, _trader, net);
        DeleteMember(_bookName, _person);
    }

    public static int findMaxPayer(ArrayList pVecDym){
        double maxPaid=0;
        int maxIndex=-1;
        Iterator<Person> pVecDymIterator=pVecDym.iterator();
        int i=0;
        while(pVecDymIterator.hasNext()){
            Person thePerson=pVecDymIterator.next();
            if(thePerson.getPaid()>maxPaid){
                maxPaid=thePerson.getPaid();
                maxIndex=i;
            }
            i++;
        }
        return maxIndex;
    }
    public static int findMinPayer(ArrayList pVecDym){
        double minPaid=0;
        int minIndex=-1;
        Iterator<Person> pVecDymIterator=pVecDym.iterator();
        int i=0;
        while(pVecDymIterator.hasNext()){
            Person thePerson=pVecDymIterator.next();
            if(thePerson.getPaid()<minPaid){
                minPaid=thePerson.getPaid();
                minIndex=i;
            }
            i++;
        }
        return minIndex;
    }
    public static boolean hasLimit(Person giver,Person receiver,String[][] limit){
        for(int j=0;j<limit[0].length;j++){
            if(giver.getName()==limit[0][j]&&receiver.getName()==limit[1][j]){
                return true;
            }
        }
        return false;
    }

    public static String[] findSolution(int label,ArrayList<Person> thePVecDym,Person Payer,String[][] limit){
        if(findMaxPayer(thePVecDym)==-1||findMinPayer(thePVecDym)==-1) return null;
        if(label==0){
            Person maxPayer=thePVecDym.get(findMaxPayer(thePVecDym));
            Person minPayer=thePVecDym.get(findMinPayer(thePVecDym));
            if(hasLimit(maxPayer,minPayer,limit)){
                if(maxPayer.getPaid()>-minPayer.getPaid()){
                    thePVecDym.remove(maxPayer);
                    return findSolution(1, thePVecDym, minPayer, limit);
                }
                else{
                    thePVecDym.remove(minPayer);
                    return findSolution(2, thePVecDym, maxPayer, limit);
                }
            }
            else{
                String[] pair={minPayer.getName(),maxPayer.getName()};
                return pair;
            }
        }
        else if(label==1){
            Person maxPayer=thePVecDym.get(findMaxPayer(thePVecDym));
            Person minPayer=Payer;
            if(hasLimit(maxPayer,minPayer,limit)){
                thePVecDym.remove(maxPayer);
                return findSolution(label, thePVecDym, minPayer, limit);
            }
            else{
                String[] pair={minPayer.getName(),maxPayer.getName()};
                return pair;
            }
        }
        else{
            Person maxPayer=Payer;
            Person minPayer=thePVecDym.get(findMinPayer(thePVecDym));
            if(hasLimit(maxPayer, minPayer, limit)) {
                thePVecDym.remove(minPayer);
                return findSolution(label, thePVecDym, maxPayer, limit);
            }
            else{
                String[] pair={minPayer.getName(),maxPayer.getName()};
                return pair;
            }
        }
    }

    public ArrayList<Solution> SettleRecord(String _bookName, ArrayList<Solution> _constrint) throws UnableToClearException {
        ArrayList<Person> person = new ArrayList<Person>();
        ArrayList<String> personName = GetMember(_bookName);
        for(int i = 0; i < personName.size(); i ++) {
            double net = QueryNetAmount(_bookName, personName.get(i));
            person.add(new Person(personName.get(i), net));
        }

        String[][] limit=new String[2][_constrint.size()];

        Iterator<Solution> _constrintIterator1=_constrint.iterator();
        int i = 0;
        while(_constrintIterator1.hasNext()) {
            Solution solu = _constrintIterator1.next();
            String giverName = solu.getGiver();
            String receiverName = solu.getReceiver();
            limit[0][i] = giverName;
            limit[1][i] = receiverName;
            Person giver = person.get(personName.indexOf(giverName));
            giver.setPaid(giver.getPaid() + solu.getAmount());
            if(giver.getPaid() == 0) {
                personName.remove(giver.getName());
                person.remove(giver);
            }
            Person receiver = person.get(personName.indexOf(receiverName));
            receiver.setPaid(receiver.getPaid() - solu.getAmount());
            if(receiver.getPaid() == 0) {
                personName.remove(receiver.getName());
                person.remove(receiver);
            }
            i ++;
        }

        boolean hasCompleteSolution=false;
        while(true){
            if (person.size()==0){
                hasCompleteSolution=true;
                break;
            }
            ArrayList<Person> theperson = (ArrayList<Person>) person.clone();
            String[] pair = findSolution(0, theperson, new Person(), limit);
            if (pair==null) break;
            Person maxPayer=person.get(personName.indexOf(pair[1]));
            Person minPayer=person.get(personName.indexOf(pair[0]));
            if (maxPayer.getPaid() > -minPayer.getPaid()) {
                maxPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                Person newGiver = person.get(personName.indexOf(minPayer.getName()));
                Person newReceiver = person.get(personName.indexOf(maxPayer.getName()));
                Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(), -minPayer.getPaid());
                _constrint.add(newSolu);
                personName.remove(minPayer.getName());
                person.remove(minPayer);
            } else if (maxPayer.getPaid() < -minPayer.getPaid()) {
                minPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                Person newGiver = person.get(personName.indexOf(minPayer.getName()));
                Person newReceiver = person.get(personName.indexOf(maxPayer.getName()));
                Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(), maxPayer.getPaid());
                _constrint.add(newSolu);
                personName.remove(maxPayer.getName());
                person.remove(maxPayer);
            } else {
                Person newGiver = person.get(personName.indexOf(minPayer.getName()));
                Person newReceiver = person.get(personName.indexOf(maxPayer.getName()));
                Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(), maxPayer.getPaid());
                _constrint.add(newSolu);
                personName.remove(maxPayer.getName());
                personName.remove(minPayer.getName());
                person.remove(maxPayer);
                person.remove(minPayer);
            }
        }
        if (!hasCompleteSolution) throw new UnableToClearException();

        return _constrint;
    }

    public void CloseDataBase() {
        reader.CloseDB();
        writer.CloseDB();
    }

    public ArrayList<String> GetType() {
        ArrayList<String> type = new ArrayList<String>();
        type.add(Type.FOOD.toString());
        type.add(Type.TRANS.toString());
        type.add(Type.PLAY.toString());
        type.add(Type.ACCOM.toString());
        type.add(Type.OTHER.toString());
        return type;
    }

}
