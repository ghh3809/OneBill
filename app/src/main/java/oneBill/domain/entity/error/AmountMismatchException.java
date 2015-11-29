package oneBill.domain.entity.error;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class AmountMismatchException extends MismatchException {
    public AmountMismatchException(float amountpaid, float amountpayable){
        super("应付、实付不匹配，总实付金额为" + amountpaid + "元，总应付金额为" + amountpayable + "元");
    }
}
