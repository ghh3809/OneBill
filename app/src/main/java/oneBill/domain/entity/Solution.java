package oneBill.domain.entity;

/**
 * Created by 豪豪 on 2015/11/26.
 */
public class Solution {

    private String giver;
    private String receiver;
    private float amount;

    public Solution(String giver, String receiver, float amount) {
        super();
        this.giver = giver;
        this.receiver = receiver;
        this.amount = amount;
    }

    /**
     * 获取给出者.
     * @return 给出者姓名
     */

    public String getGiver() {
        return giver;
    }

    /**
     * 获取接受者.
     * @return 接受者姓名
     */

    public String getReceiver() {
        return receiver;
    }

    /**
     * 获取金额.
     * @return 金额
     */

    public float getAmount() {
        return amount;
    }
}
