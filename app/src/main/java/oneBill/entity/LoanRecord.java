package oneBill.entity;

import java.util.Calendar;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class LoanRecord {

    private Person lender;
    private Person borrower;
    private float amount;
    private int id;
    private Calendar time;

    LoanRecord(int _id, Person _lender, Person _borrower, float _amount, Calendar _time) {
        super();
        this.lender = _lender;
        this.borrower = _borrower;
        this.amount = _amount;
        this.id = _id;
        this.time = _time;
    }

    /**
     * 获取借出人姓名.
     * @return 借出人姓名
     */

    public String getLender() {
        return lender.getName();
    }

    /**
     * 获取借入人姓名.
     * @return 借入人姓名
     */

    public String getBorrower() {
        return borrower.getName();
    }

    /**
     * 获取借款金额.
     * @return 借款金额
     */

    public float getAmount() {
        return amount;
    }

    /**
     * 获取借款记录ID.
     * @return 借款记录ID
     */

    public int getId() {
        return id;
    }

    /**
     * 获取借款时间.
     * @return 借款时间
     */

    public Calendar getTime() {
        return time;
    }
}
