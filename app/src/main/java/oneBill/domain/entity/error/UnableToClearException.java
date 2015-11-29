package oneBill.domain.entity.error;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class UnableToClearException extends Exception {
    public UnableToClearException(){
        super("约束太多，无法清账");
    }
}
