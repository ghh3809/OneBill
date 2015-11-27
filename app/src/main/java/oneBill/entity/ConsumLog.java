package oneBill.entity;

import java.util.Calendar;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class ConsumLog {
    private Type type;
    private Calendar time;
    private int id;
    private float thispaid, thispayable;

    ConsumLog(int id, Type type, Calendar time, float thispaid, float thispayable) {
        super();
        this.type = type;
        this.time = time;
        this.id = id;
        this.thispaid = thispaid;
        this.thispayable = thispayable;
    }

    /**
     * 返回消费类型.
     * @return 消费类型
     */

    public Type getType() {
        return type;
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
     * 获取此次消费个人实付.
     * @return 个人实付
     */

    public float getThispaid() {
        return thispaid;
    }

    /**
     * 获取此次消费个人应付.
     * @return 个人应付
     */

    public float getThispayable() {
        return thispayable;
    }

}
