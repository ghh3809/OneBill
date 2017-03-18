package oneBill.presentation.customLayout;

import java.text.DecimalFormat;

/**
 * Created by 豪豪 on 2017/2/26 0026.
 */

/**
 * Class Item: each input can be seen as an item, and each item has a value and a type.
 * If the item is a number or a result, the value is the number or result; otherwise the value is -1.
 */
public class Item {

    public double value = 0;
    public int type = 0;

    public Item(double value, int type) {
        this.value = value;
        this.type = type;
    }

    /**
     * override the toString method.
     * @return number or operator to String
     */
    @Override
    public String toString() {
        String result = "";
        switch (type) {
            case Types.NUM:
            case Types.RES:
                result = new DecimalFormat("#.##").format(value);
                break;
            case Types.ADD:
                result = "+";
                break;
            case Types.SUB:
                result = "-";
                break;
            case Types.DOT:
                result = ".";
                break;
        }
        return result;
    }
}
