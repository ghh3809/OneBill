package oneBill.entity.error;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class MemberMismatchException extends MismatchException {
    public MemberMismatchException(int _lenPaid, int _lenPayable, int _lenMember){
        super("人数不匹配，实付人数为" + _lenPaid + "人，应付人数为" + _lenPayable + "人，成员总数为" + _lenMember + "人");
    }
}
