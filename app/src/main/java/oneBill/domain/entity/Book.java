package oneBill.domain.entity;

import java.util.Calendar;
import java.util.Vector;

import oneBill.domain.entity.error.AmountMismatchException;
import oneBill.domain.entity.error.MemberMismatchException;
import oneBill.domain.entity.error.PersonNotFoundException;
import oneBill.domain.entity.error.UnableToClearException;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class Book {

    private String name;
    private Vector<Person> member; //参与成员
    private Vector<ConsumRecord> consumption; //消费记录；
    private Vector<LoanRecord> loan; //借款记录；
    private float sum; //消费总额；
    private int order; //当前消费或借款记录序号；
    private static int bookNum = 1; //账单编号；

    /**
     * 默认构造函数，无参数。账单名默认为“账本X”。
     */

    public Book() {
        super();
        this.name = "账本" + bookNum;
        this.member = new Vector<Person>();
        this.consumption  = new Vector<ConsumRecord>();
        this.loan  = new Vector<LoanRecord>();
        this.sum = 0;
        this.order = 0;
        bookNum ++;
    }

    /**
     * 含参构造函数，以账本名字为起始参数。
     * @param _name 账本名字
     */

    public Book(String _name) {
        super();
        this.name = _name;
        this.member = new Vector<Person>();
        this.consumption  = new Vector<ConsumRecord>();
        this.loan  = new Vector<LoanRecord>();
        this.sum = 0;
        this.order = 0;
        bookNum ++;
    }

    /**
     * 获取账本名.
     * @return 账本名
     */

    public String getName() {
        return name;
    }

    /**
     * 设置账本名.
     * @param name 要设置的账本名
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取成员数量.
     * <p>得到该账本所有参与成员的数量（无论是否有过消费）。<br>
     * @return 成员总数
     */

    public int getNumber() {
        return this.member.size();
    }


    /**
     * 获取成员数组.
     * <p>得到该账本按顺序排列的所有参与成员名单（无论是否有过消费）。<br>
     * 注意：此方法得到的数组并非数据数组，对返回值得任何直接修改将不起作用。
     * @return 成员数组
     */

    public String[] getMember() {
        int n = this.member.size();
        String[] mem = new String[n];
        for(int i = 0; i < n; i ++) {
            mem[i] = this.member.get(i).getName();
        }
        return mem;
    }

    /**
     * 获取账本消费记录向量.
     * <p>只包含总金额，不包含个人具体开支情况。<br>
     * 注意：此方法属于对消费记录的一层浅复制，即返回值指针并非原数据指针，但向量中各成员的指针均为真实指针。
     * @return 账本消费记录向量
     */

    @SuppressWarnings("unchecked")
    public Vector<ConsumRecord> getConsumption() {
        return (Vector<ConsumRecord>)this.consumption.clone();
    }

    /**
     * 获取账本借款记录向量.
     * <p>包括该账本内所有借款记录。<br>
     * 注意：此方法属于对借款记录的一层浅复制，即返回值指针并非原数据指针，但向量中各成员的指针均为真实指针。
     * @return 账本借款记录向量
     */

    @SuppressWarnings("unchecked")
    public Vector<LoanRecord> getLoan() {
        return (Vector<LoanRecord>)this.loan.clone();
    }

    /**
     * 获取账本总消费金额.
     * <p>账本自建立以来所有的消费金额。<br>
     * @return 账本总消费金额
     */

    public float getSum() {
        return sum;
    }


    /**
     * 新添加一个成员.
     * <p>在账本类的member变量下增添一名新的成员。<br>
     * 该方法包含重名检测，若出现重名现象将返回false，添加失败。
     * @param _personName 新添加成员的姓名
     * @return 若成功返回true，有重名则返回false
     */

    public boolean addPerson(String _personName) {
        for(Person p : this.member) {
            if(p.getName().equals(_personName)) return false;
        }
        this.member.add(new Person(_personName));
        return true;
    }

    /**
     * 查找一名成员的编号.
     * <p>通过对姓名的查找返回成员在member向量中的位置。<br>
     * @param _personName 需要查找的成员姓名
     * @return 返回成员编号
     * @throws PersonNotFoundException 若未找到该名字，则抛出此错误，并给出提示信息
     */

    public int findPerson(String _personName) throws PersonNotFoundException {
        for(Person p : this.member) {
            if(p.getName().equals(_personName)) return member.indexOf(p);
        }
        throw new PersonNotFoundException(_personName);
    }

    /**
     * 增加消费记录.
     * <p>增加一笔新的消费记录，该方法将同时更新账本下的记录和与之相关的成员下的记录。<br>
     * 注意，该方法的输入参量为实付和应付数组，其分别对应成员的顺序。若该成员未发生记录，则以0记。<br>
     * 若给定数组的规模与成员规模不一致，则会抛出异常至调用函数。
     * @param _type 消费类型（5种枚举类型）
     * @param _paid 按照成员顺序给定的实付数组
     * @param _payable 按照成员顺序给定的应付数组
     * @return 该消费记录的ID，该ID在账本记录下和个人记录下具有相同的值
     * @throws MemberMismatchException 若给定实付或应付数组规模与成员规模不匹配，则抛出此错误，并给出提示信息
     * @throws AmountMismatchException 若实付总金额与应付总金额不符，则抛出此错误，并给出提示信息
     */

    public int addConsumRecord(Type _type, float[] _paid, float[] _payable) throws MemberMismatchException, AmountMismatchException {
        if((_paid.length != this.member.size()) || (_payable.length != this.member.size()))
            throw new MemberMismatchException(_paid.length, _payable.length, this.member.size());
        float amountpaid = 0.0f;
        float amountpayable = 0.0f;
        for(float a : _paid) {
            amountpaid += a;
        }
        for(float a : _payable) {
            amountpayable += a;
        }
        if(Math.abs(amountpaid - amountpayable) > 1E-4)
            throw new AmountMismatchException(amountpaid, amountpayable);
        consumption.add(new ConsumRecord(_type, Calendar.getInstance(), ++this.order, amountpaid));
        for(int i = 0; i < this.member.size(); i ++) {
            if((_paid[i] != 0.0f) || (_payable[i] != 0.0f)) {
                this.member.get(i).addConsumLog(this.order, _type, _paid[i], _payable[i]);
            }
        }
        this.sum += amountpaid;
        return this.order;
    }

    /**
     * 删除账本消费记录.
     * <p>以ID为关键字，删除对应的账本消费记录。该方法将同时更新账本下的记录和与之相关的成员下的记录。<br>
     * @param _id 要删除的消费记录的ID
     */

    public void delConsumRecord(int _id) {
        int head = 0, tail = this.consumption.size() - 1, find = tail;
        if(_id < this.consumption.get(tail).getId()) {
            while (head > tail){
                find = (head + tail) / 2;
                if(_id == this.consumption.get(find).getId()) {
                    this.sum -= this.consumption.get(find).getAmount();
                    this.consumption.remove(find);
                    break;
                }
                else if (_id > this.consumption.get(find).getId()) head = find + 1;
                else tail = find - 1;
            }
        }
        for(Person p : this.member) {
            p.delConsumLog(_id);
        }
    }

    /**
     * 增加账本借款记录.
     * <p>增加一笔新的借款记录。若借款金额为0，则不予处理；若借款金额为负，则会自动将借出人和借入人交换，保证借款金额始终大于0。<br>
     * 该方法将同时更新账本下的记录和与之相关的成员下的记录。当查无此人时，将抛出异常至调用函数。
     * @param _lender 借出人
     * @param _borrower 借入人
     * @param _amount 借款金额
     * @return 该借款记录的ID，该ID在账本记录下和个人记录下具有相同的值
     * @throws PersonNotFoundException 若未找到借出人或借入人，则抛出此错误，并给出提示信息
     */

    public int addLoanRecord(String _lender, String _borrower, float _amount) throws PersonNotFoundException {
        int len = this.findPerson(_lender);
        int bor = this.findPerson(_borrower);
        if((_amount != 0.0f) && (!_lender.equals(_borrower))){
            if(_amount < 0.0f) {
                this.addLoanRecord(_borrower, _lender, -_amount);
            } else {
                this.loan.add(new LoanRecord(++this.order, this.member.get(len), this.member.get(bor), _amount, Calendar.getInstance()));
                this.member.get(len).addLoanRecord(this.order, this.member.get(len), this.member.get(bor), _amount);
                this.member.get(bor).addLoanRecord(this.order, this.member.get(len), this.member.get(bor), _amount);
            }
        }
        return this.order;
    }

    /**
     * 删除账本借款记录.
     * <p>以ID为关键字，删除对应的账本借款记录。该方法将同时更新账本下的记录和与之相关的成员下的记录。<br>
     * @param _id 要删除的借款记录的ID
     */

    public void delLoadRecord(int _id) {
        int head = 0, tail = this.loan.size() - 1, find = tail;
        if(_id < this.loan.get(tail).getId()) {
            while (head > tail){
                find = (head + tail) / 2;
                if(_id == this.loan.get(find).getId()) break;
                else if (_id > this.loan.get(find).getId()) head = find + 1;
                else tail = find - 1;
            }
        }
        try {
            Person lender = this.member.get(this.findPerson(this.loan.get(find).getLender()));
            Person borrower = this.member.get(this.findPerson(this.loan.get(find).getBorrower()));
            this.loan.remove(find);
            lender.delLoadRecord(_id);
            borrower.delLoadRecord(_id);
        } catch (Exception e) { e.printStackTrace(); }

    }

    /**
     * 查询个人消费记录.
     * <p>查找某人的和该人有关的消费记录，该记录包括更详尽的实付、应付内容。<br>
     * 注意：此方法属于对个人记录的一层浅复制，即返回值指针并非原数据指针，但向量中各成员的指针均为真实指针。<br>
     * 当查无此人时，将抛出异常至调用函数。
     * @param _personName 要查询人的姓名
     * @return 个人消费记录
     * @throws PersonNotFoundException 未找到该人时，抛出此异常
     */

    @SuppressWarnings("unchecked")
    public Vector<ConsumLog> queryConsumLog(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        return (Vector<ConsumLog>)this.member.get(findp).getConsumLog().clone();
    }

    /**
     * 查询个人借款记录.
     * <p>查找某人的和该人有关的借款记录。当查无此人时，将抛出异常至调用函数。<br>
     * 注意：此方法属于对个人记录的一层浅复制，即返回值指针并非原数据指针，但向量中各成员的指针均为真实指针。<br>
     * @param _personName 要查询人的姓名
     * @return 个人消费记录
     * @throws PersonNotFoundException 未找到该人时，抛出此异常
     */

    @SuppressWarnings("unchecked")
    public Vector<LoanRecord> queryLoanRecord(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        return (Vector<LoanRecord>)this.member.get(findp).getLoanRecord().clone();
    }

    /**
     * 查询个人总净支出额，即“实付-应付”.
     * <p>注意：该函数将查询当前此成员的净支出，返回值为正代表应收取，为负代表应支出<br>
     * @param _personName 要查询人的姓名
     * @return 个人总实付额
     * @throws PersonNotFoundException 未找到该人时，抛出此异常
     */

    public float queryNetAmount(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        return (float)this.member.get(findp).getNetAmount();
    }

    /**
     * 查询个人总实付额.
     * @param _personName 要查询人的姓名
     * @return 个人总实付额
     * @throws PersonNotFoundException 未找到该人时，抛出此异常
     */

    public float queryPaid(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        return (float)this.member.get(findp).getPaid();
    }

    /**
     * 查询个人总应付额.
     * @param _personName 要查询人的姓名
     * @return 个人总应付额
     * @throws PersonNotFoundException 未找到该人时，抛出此异常
     */

    public float queryPayable(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        return (float)this.member.get(findp).getPayable();
    }

    /**
     * 个人清账并删除该成员.
     * <p>若该人当前无未清账款，可调用此方法。成员不存在时，抛出异常至调用函数。<br>
     * @param _personName 清账人姓名
     * @throws PersonNotFoundException 当该成员不存在时，抛出此异常
     */

    public void clearPerson(String _personName) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        this.member.remove(findp);
    }

    /**
     * 个人清账并删除该成员.
     * <p>若该人当前有需清账款项，则应调用此方法。成员不存在时，抛出异常至调用函数。<br>
     * 注意：此函数会自动生成一条借款记录。
     * @param _personName 清账人姓名
     * @param _trader 交易对象
     * @throws PersonNotFoundException 当该成员不存在时，抛出此异常
     */

    public void clearPerson(String _personName, String _trader) throws PersonNotFoundException {
        int findp = this.findPerson(_personName);
        Person p = member.get(findp);
        this.addLoanRecord(_trader, _personName, p.getNetAmount());
        this.clearPerson(_personName);
    }

    /**
     * 全员清账.
     * <p>在给定约束的条件下，对全体成员进行清账。此清账功能不会将成员及账本删除。<br>
     * @param _constrint 给定的约束条件，若无约束则输入size为0的向量
     * @return 清账结果，存储在Vector中
     * @throws UnableToClearException 无法清账时将抛出该异常至调用函数
     */

    public Vector<Solution> clearAll(Vector<Solution> _constrint) throws UnableToClearException {
        //其实我就在等着算法；
//		if(1 < 0) {
//			throw new UnableToClearException();
//		}
        return new Vector<Solution>();
    }
}
