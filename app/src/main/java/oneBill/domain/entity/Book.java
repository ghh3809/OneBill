package oneBill.domain.entity;

import java.util.ArrayList;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

public class Book {

    private String name;
    private String changeTime;
    private double amount;

    public Book(String name, String changeTime, double amount) {
        this.name = name;
        this.amount = amount;
        this.changeTime = changeTime;
    }

    public String getName() {
        return name;
    }

    public String getChangeTime() {
        return changeTime;
    }

    public double getAmount() {
        return amount;
    }
}
