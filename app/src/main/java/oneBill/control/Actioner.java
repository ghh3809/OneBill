package oneBill.control;

import java.util.ArrayList;

import oneBill.entity.Solution;
import oneBill.entity.Type;

/**
 * Created by 豪豪 on 2015/11/26.
 */

public class Actioner {

    public void CreateBook(String _bookName) {

    }

    public ArrayList<String> GetBook() {
        return new ArrayList<String>();
    }

    public void SetName(String _bookName) {

    }

    public void DeleteBook(String _bookName) {

    }

    public void CreateMember(String _bookName, String _person) {

    }

    public void CreateMember(String _bookName, ArrayList<String> _persons) {

    }

    public ArrayList<String> GetMember(String _bookName) {
        return new ArrayList();
    }

    public boolean SetName(String _bookName, String _oldName, String _newName) {
        return true;
    }

    public void DeleteMember(String _bookName, String _person) {

    }

    public ArrayList GetRecord(String _bookName) {
        return new ArrayList();
    }

    public int CreateConsumRecord(String _bookName, Type _type, float[] _paid, float[] _payable) {
        return 0;
    }

    public int CreateLoanRecord(String _bookName, String _lender, String _borrower, float _amount) {
        return 0;
    }

    public void DeleteRecord(int _ID) {

    }

    public ArrayList GetLog(String _bookName, String _person) {
        return new ArrayList();
    }

    public double queryPaid(String _bookName, String _person) {
        return 0;
    }

    public double queryPayable(String _bookName, String _person) {
        return 0;
    }

    public double queryNetAmount(String _bookName, String _person) {
        return 0;
    }

    public void SettlePerson(String _bookName, String _person, String _trader) {

    }

    public ArrayList<Solution> SettleRecord(String _bookName) {
        return new ArrayList<Solution>();
    }

    public ArrayList<Solution> SettleRecord(String _bookName, ArrayList<Solution> constrint) {
        return new ArrayList<Solution>();
    }

}
