package oneBill.domain.entity.error;

/**
 * Created by 豪豪 on 2015/11/29.
 */
public class NullException extends Exception {
    public NullException(){
        super("账本名称输入为空");
    }
}
