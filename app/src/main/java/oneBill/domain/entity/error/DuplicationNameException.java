package oneBill.domain.entity.error;

/**
 * Created by 豪豪 on 2015/11/29.
 */
public class DuplicationNameException extends Exception {
    public DuplicationNameException() {
        super("出现重名");
    }
}
