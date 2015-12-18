package oneBill.domain.entity;

/**
 * Created by 豪豪 on 2015/12/7.
 */
public class Person {
    private String name;
    private double paid;

    public Person(String name, double paid){
        this.name=name;
        this.paid=paid;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }
    public double getPaid() {
        return paid;
    }
    public void setPaid(double paid) {
        this.paid = paid;
    }
}
