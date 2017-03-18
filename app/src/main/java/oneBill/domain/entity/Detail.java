package oneBill.domain.entity;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

public class Detail {

    private String person;
    private double paid;
    private double payable;

    public Detail(String person, double paid, double payable) {
        this.person = person;
        this.paid = paid;
        this.payable = payable;
    }

    public String getPerson() {
        return person;
    }

    public double getPaid() {
        return paid;
    }

    public double getPayable() {
        return payable;
    }
}
