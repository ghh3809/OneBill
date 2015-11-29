package oneBill.domain.entity;

import java.util.Calendar;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class ConsumRecord {

    private Type type;
    private Calendar time;
    private int id;
    private float amount;

    ConsumRecord(Type _type, Calendar _time, int _id, float _amount) {
        super();
        this.type = _type;
        this.time = _time;
        this.id = _id;
        this.amount = _amount;
    }

    /**
     * 返回消费类型.
     * @return 消费类型
     */

    public String getType() {
        return type.toString();
    }

    /**
     * 获取消费时间.
     * @return 消费时间
     */

    public Calendar getTime() {
        return time;
    }

    /**
     * 获取消费记录ID.
     * @return 消费记录ID
     */

    public int getId() {
        return id;
    }

    /**
     * 获取消费总金额.
     * @return 消费总金额
     */

    public float getAmount() {
        return amount;
    }
}
