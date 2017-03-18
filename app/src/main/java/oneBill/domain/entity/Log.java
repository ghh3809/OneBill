package oneBill.domain.entity;

import java.util.ArrayList;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

public class Log {

    private int ID;
    private String type;
    private String time;
    private double amount;

    public Log(int ID, String type, String time, double amount) {
        this.ID = ID;
        this.type = type;
        this.time = time;
        this.amount = amount;
    }

    public int getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public double getAmount() {
        return amount;
    }
}
