package oneBill.domain.entity.error;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class PersonNotFoundException extends Exception {
    public PersonNotFoundException(String _personName){
        super("成员中无" + _personName);
    }
}

