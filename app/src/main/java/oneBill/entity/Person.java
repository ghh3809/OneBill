package oneBill.entity;

import java.util.Calendar;
import java.util.Vector;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class Person {

    private String name; //姓名；
    private Vector<ConsumLog> consumLog; //消费记录；
    private Vector<LoanRecord> loanRecord; //借款记录；
    private float paid; //总实付金额；
    private float payable; //总应付金额；

    /**
     * 带参构造函数.
     * @param _name 姓名
     */
    Person(String _name) {
        super();
        this.name  = _name;
        this.consumLog  = new Vector<ConsumLog>();
        this.loanRecord  = new Vector<LoanRecord>();
        this.paid = 0;
        this.payable = 0;
    }

    //get方法；
    String getName() {
        return name;
    }

    Vector<ConsumLog> getConsumLog() {
        return consumLog;
    }

    Vector<LoanRecord> getLoanRecord() {
        return loanRecord;
    }

    float getPaid() {
        return paid;
    }

    float getPayable() {
        return payable;
    }

    /**
     * 增加个人消费记录.
     * <p>当发生一笔和该人有关的消费账目时，增加到其名下的消费记录中<br>
     * 应和Book.addConsumRecord()联合使用
     * @param _id 消费记录ID（和账目记录一致）
     * @param _type 消费类型
     * @param _thispaid 此次实付
     * @param _thispayable 此次应付
     */

    void addConsumLog(int _id, Type _type, float _thispaid, float _thispayable) {
        consumLog.addElement(new ConsumLog(_id, _type, Calendar.getInstance(), _thispaid, _thispayable));
        this.paid += _thispaid;
        this.payable += _thispayable;
    }

    /**
     * 删除个人消费记录.
     * <p>当删除一笔和该人有关的消费账目时，使用此函数<br>
     * 应和Book.delConsumRecord()联合使用
     * @param _id 要删除的消费记录ID
     */

    void delConsumLog(int _id) {
        int head = 0, tail = this.consumLog.size() - 1, find = tail;
        if(_id < this.consumLog.get(tail).getId()) {
            while (head > tail){
                find = (head + tail) / 2;
                if(_id == this.consumLog.get(find).getId()) {
                    this.paid -= this.consumLog.get(find).getThispaid();
                    this.payable -= this.consumLog.get(find).getThispayable();
                    this.consumLog.remove(find);
                    break;
                }
                else if (_id > this.consumLog.get(find).getId()) head = find + 1;
                else tail = find - 1;
            }
        }
    }

    /**
     * 增加个人借款记录.
     * <p>当发生一笔和该人有关的借款账目时，增加到其名下的借款记录中<br>
     * 应和Book.addLoanRecord()联合使用
     * @param _id 借款记录ID（和账目记录一致）
     * @param _lender 借出人姓名
     * @param _borrower 借入人姓名
     * @param _amount 借款金额
     */

    void addLoanRecord(int _id, Person _lender, Person _borrower, float _amount) {
        this.loanRecord.add(new LoanRecord(_id, _lender, _borrower, _amount, Calendar.getInstance()));
        if(_lender == this) this.paid += _amount;
        else this.payable += _amount;
    }

    /**
     * 删除个人借款记录.
     * <p>当删除一笔和该人有关的借款账目时，使用此函数<br>
     * 应和Book.delLoanRecord()联合使用
     * @param _id 借款记录ID
     * @return 成功则返回true，未找到则返回false
     */

    void delLoadRecord(int _id) {
        int head = 0, tail = this.loanRecord.size() - 1, find = tail;
        if(_id < this.loanRecord.get(tail).getId()) {
            while (head > tail){
                find = (head + tail) / 2;
                if(_id == this.loanRecord.get(find).getId()) {
                    if(this.loanRecord.get(find).getLender().equals(this.name)) this.paid -= this.loanRecord.get(find).getAmount();
                    else this.payable -= this.loanRecord.get(find).getAmount();
                    this.loanRecord.remove(find);
                    break;
                }
                else if (_id > this.loanRecord.get(find).getId()) head = find + 1;
                else tail = find - 1;
            }
        }
    }

    /**
     * 查询净支出（实出-应付）.
     * <p>查询当前此成员的净支出，返回值为正代表应收取，为负代表应支出<br>
     * @return 净支出，为正代表应收取，为负代表应支出
     */

    float getNetAmount() {
        return this.paid - this.payable;
    }

}
