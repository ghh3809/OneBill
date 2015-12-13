package oneBill.control;

import android.content.Context;

import java.util.ArrayList;

import oneBill.domain.entity.Solution;
import oneBill.domain.entity.error.AmountMismatchException;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.NullException;
import oneBill.domain.entity.error.UnableToClearException;
import oneBill.domain.mediator.Broker;

/**
 * Created by 豪豪 on 2015/11/26.
 */

public class Actioner {

    private Broker br;

    public Actioner(Context context) {
        br = new Broker(context);
    }

    /**
     *创建新账本.
     * <p>使用账本名称创建一个新的账本。当输入为空时，创建账本名为“账本*”，编号延递。<br>
     *     创建账本时，其创建时间与修改时间均为当前系统时间，且不可修改。
     * @param _bookName 要创建账本的名字
     * @throws NullException 当输入为空时，返回此异常
     * @throws  DuplicationNameException 当出现重名时，返回此异常
     */
    public void CreateBook(String _bookName) throws NullException, DuplicationNameException {
        if (_bookName.trim().equals("")) throw new NullException();
        br.CreateBook(_bookName);
    }

    /**
     * 获取账本列表.
     * <p>获取已经创建的账本名称的ArrayList列表。顺序按最近修改时间降序排列。<br>
     * @return 返回按修改时间降序排列的ArrayList列表
     */
    public ArrayList<String> GetBook() {
        return br.GetBook();
    }

    /**
     * 获取新建账本时默认账本名.
     * <p>当用户创建新的账本时，可在账本名称输入框内默认为此函数生成的默认账本名。<br>
     * @return 返回默认账本名
     */
    public String GetDefaultName() {
        return br.GetDefaultName();
    }

    /**
     * 删除账本.
     * <p>删除与该账本有关的所有内容，包括成员、记录等。此操作将不可恢复。<br>
     * @param _bookName 要删除的账本的名字
     */
    public void DeleteBook(String _bookName) {
        br.DeleteBook(_bookName);
    }

    /**
     * 创建成员.
     * <p>创建一名新的成员。<br>
     * @param _bookName 要创建成员的账本名
     * @param _person 要创建成员的姓名
     * @throws DuplicationNameException 当与已有成员出现重名时，抛出此异常
     */
    public void CreateMember(String _bookName, String _person) throws NullException, DuplicationNameException {
        if (_person.trim().equals("")) throw new NullException();
        br.CreateMember(_bookName, _person);
    }

    /**
     * 获取成员列表.
     * <p>按创建顺序获取成员列表。<br>
     * @param _bookName 要获取成员的账本名
     * @return 返回成员列表
     */
    public ArrayList<String> GetMember(String _bookName) {
        return br.GetMember(_bookName);
    }

    /**
     * 账本更名.
     * <p>此函数可以将账本名改变。如果有重名现象，则抛出异常。<br>
     * @param _oldName 旧账本名
     * @param _newName 新账本名
     * @throws DuplicationNameException 当出现重名现象时，抛出此异常
     */
    public void SetName(String _oldName, String _newName) throws DuplicationNameException {
        br.SetName(_oldName, _newName);
    }

    /**
     * 删除成员.
     * <p>从该账本中删除对应的成员。注意此处的删除并非真正删除，而是成员列表中不再有此人。但是过去的记录中依然可以看到此人的记录，但无法增加新的记录。新加成员时，也不可与其重名。<br>
     * @param _bookName 要删除成员的账本名称
     * @param _person 要删除的成员姓名
     */
    public void DeleteMember(String _bookName, String _person) {
        br.DeleteMember(_bookName, _person);
    }

    /**
     * 获取账本总消费金额.
     * <p>从记录中搜索所有的消费记录（不包括借款记录），返回消费总金额。<br>
     * @param _bookName 要查找的账本名称
     * @return 返回消费总金额
     */
    public double GetSum(String _bookName) {
        return br.GetSum(_bookName);
    }

    /**
     * 获取账本记录.
     * <p>获取该账本的简要记录，包括时间、金额、类型。记录方式为二维ArrayList，每个ArrayList为一行记录。<br>
     * @param _bookName 要获取记录的账本
     * @return 返回账本记录
     */
    public ArrayList<ArrayList<String>> GetRecord(String _bookName) {
        return br.GetRecord(_bookName);
    }

    /**
     * 获取最新账本记录.
     * <p>获取该账本的最新一笔账的简要记录，包括时间、金额、类型。记录方式为二维ArrayList，每个ArrayList为一行记录。<br>
     * @param _bookName 要获取记录的账本
     * @return 返回账本记录
     */
    public ArrayList<String> GetLatestRecord(String _bookName) {
        return br.GetConsumRecord(_bookName).get(0);
    }

    /**
     * 获取记录明细.
     * <p>获取记录的明细，包括参与人员、实付、应付。记录方式为二维ArrayList，每个ArrayList为一行记录。<br>
     * @param _ID 要获取明细的记录ID号
     * @return 返回记录明细
     */
    public ArrayList<ArrayList<String>> GetDetail(int _ID) {
        return br.GetDetail(_ID);
    }

    /**
     * 新增账本消费记录.
     * <p>新增一条消费记录，需要传递实付与应付两个ArrayList，其排列顺序应与GetMember方法中给出的顺序一致。<br>
     * @param _bookName 要新增记录的账本名
     * @param _type 新增记录的类型
     * @param _paid 实付数组
     * @param _payable 应付数组
     * @throws AmountMismatchException 当实付与应付总额出现矛盾时，抛出此异常。
     */
    public void CreateConsumRecord(String _bookName, int _type, ArrayList<Double> _paid, ArrayList<Double> _payable) throws AmountMismatchException {
        br.CreateConsumRecord(_bookName, _type, _paid, _payable);
    }

    /**
     * 新增账本借款记录.
     * <p>新增一条借款记录，需要传递借出人、借入人及金额三个参数。<br>
     * @param _bookName 要增加记录的账本名
     * @param _lender 借出人
     * @param _borrower 借入人
     * @param _amount 借款金额
     */
    public void CreateLoanRecord(String _bookName, String _lender, String _borrower, double _amount) {
        if (_amount > 0) br.CreateLoanRecord(_bookName, _lender, _borrower, _amount);
        else if (_amount < 0) br.CreateLoanRecord(_bookName, _borrower, _lender, -_amount);
    }

    /**
     * 删除某条账本记录.
     * <p>按id号删除一条记录，可能为消费记录或借款记录。<br>
     * @param _ID 要删除记录的ID号
     */
    public void DeleteRecord(int _ID) {
        br.DeleteRecord(_ID);
    }

    /**
     * 查询净支出额.
     * <p>查询列表中成员的净支出额（总支出-总应付），顺序按照人员列表顺序排列。<br>
     * @param _bookName 要查询的账本名
     * @return 净支出额列表
     */
    public ArrayList<Double> QueryNetAmount(String _bookName) {
        ArrayList<String> persons = br.GetMember(_bookName);
        ArrayList<Double> net = new ArrayList<Double>();
        for(int i = 0; i < persons.size(); i ++) {
            net.add(QueryNetAmount(_bookName, persons.get(i)));
        }
        return net;
    }

    /**
     * 查询个人净支出额.
     * <p>查询账本中某成员的净支出额（总支出-总应付）。<br>
     * @param _bookName 要查询的账本名
     * @param _person 要查询的人员姓名
     * @return 净支出额
     */
    public double QueryNetAmount(String _bookName, String _person) {
        return br.QueryNetAmount(_bookName, _person);
    }

    /**
     * 个人清账并删除成员.
     * <p>个人清账并删除成员，其中最后一个参数为交易者（删除成员要求其净支出额必须为0）<br>
     * @param _bookName 要清账个人所在的账本
     * @param _person 清账人姓名
     * @param _trader 交易者姓名
     */
    public void SettlePerson(String _bookName, String _person, String _trader) {
        br.SettlePerson(_bookName, _person, _trader);
    }

    /**
     * 全员清账.
     * <p>提供全员清账函数，包括两种传参方法。此方法用于不含任何约束条件时的清账。<br>
     * @param _bookName 要清账的账本名
     * @return 清账方案，以Solution类型存于ArrayList中
     * @throws UnableToClearException 无法清账时，抛出此错误
     */
    public ArrayList<Solution> SettleRecord(String _bookName) throws UnableToClearException {
        return br.SettleRecord(_bookName, new ArrayList<Solution>());
    }

    /**
     * 全员清账.
     * <p>提供全员清账函数，包括两种传参方法。此方法用于含有约束条件时的清账。<br>
     * @param _bookName 要清账的账本名
     * @param constrint 包含的约束条件，以Solution类型存于ArrayList中
     * @return 清账方案，以Solution类型存于ArrayList中
     * @throws UnableToClearException UnableToClearException 无法清账时，抛出此错误
     */
    public ArrayList<Solution> SettleRecord(String _bookName, ArrayList<Solution> constrint) throws UnableToClearException {
        return br.SettleRecord(_bookName, new ArrayList<Solution>());
    }

    /**
     * 关闭数据库.
     * <p>关闭程序调用的数据库。在Activity被关闭或退出时，请务必调用此方法！<br>
     */
    public void CloseDataBase() {
        br.CloseDataBase();
    }

    /**
     * 返回消费/借贷类型.
     * 返回消费或者借贷的类型列表，包括FOOD, TRANS, PLAY, ACCOM, OTHER, LOAN几种。
     * @return 类型列表
     */
    public ArrayList<String> GetType() {
        return br.GetType();
    }

}
