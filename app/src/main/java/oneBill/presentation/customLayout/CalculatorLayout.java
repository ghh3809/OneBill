package oneBill.presentation.customLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

/**
 * Created by 豪豪 on 2017/3/19 0019.
 */

public class CalculatorLayout extends LinearLayout implements View.OnClickListener {

    private Context context;
    private TextView textViewFormula;
    private EditText view;
    private ArrayList<Item> items = new ArrayList<>();
    private double finalResult = 0;
    private static final double NUM_MAX = 1E8;
    private static final int MAX_LENGTH = 18;
    private static final int MAX_SIZE = 50;

    public CalculatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_calculator, this);
        textViewFormula = (TextView) findViewById(R.id.textViewFormula);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
        findViewById(R.id.button0).setOnClickListener(this);
        findViewById(R.id.buttonAdd).setOnClickListener(this);
        findViewById(R.id.buttonSub).setOnClickListener(this);
        findViewById(R.id.buttonBack).setOnClickListener(this);
        findViewById(R.id.buttonDot).setOnClickListener(this);
        findViewById(R.id.buttonClear).setOnClickListener(this);
        findViewById(R.id.buttonEqual).setOnClickListener(this);
        //initialize
        items.add(new Item(0, Types.RES));
    }

    public void setEditText(EditText editText) {
        this.view = editText;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                recordNum(1);
                break;
            case R.id.button2:
                recordNum(2);
                break;
            case R.id.button3:
                recordNum(3);
                break;
            case R.id.button4:
                recordNum(4);
                break;
            case R.id.button5:
                recordNum(5);
                break;
            case R.id.button6:
                recordNum(6);
                break;
            case R.id.button7:
                recordNum(7);
                break;
            case R.id.button8:
                recordNum(8);
                break;
            case R.id.button9:
                recordNum(9);
                break;
            case R.id.button0:
                recordNum(0);
                break;
            case R.id.buttonAdd:
                recordOpt(Types.ADD);
                break;
            case R.id.buttonSub:
                recordOpt(Types.SUB);
                break;
            case R.id.buttonBack:
                //Judge the length of the data. If the length is equal to 1, delete and regenerate 0.
                if (items.size() > 1) {
                    items.remove(items.size() - 1);
                    showFormula();
                    showResult();
                } else {
                    items.clear();
                    items.add(new Item(0, Types.RES));
                    textViewFormula.setText("");
                    showResult();
                }
                break;
            case R.id.buttonDot:
                recordDot();
                break;
            case R.id.buttonClear:
                items.clear();
                items.add(new Item(0, Types.RES));
                textViewFormula.setText("");
                showResult();
                break;
            case R.id.buttonEqual:
                //Show "=", clear and save the data in the same time. If there is "=" in the formula, do not append it.
                String text = textViewFormula.getText().toString();
                if ((text.length() != 0) && (text.charAt(text.length() - 1) != '=')) {
                    textViewFormula.append("=");
                }
                items.clear();
                items.add(new Item(finalResult, Types.RES));
                break;
        }
    }
    //Clear all and initialize.
    public void init() {

        if (!view.getText().toString().equals("")) {
            double result = Double.parseDouble(view.getText().toString());
            items.clear();
            items.add(new Item(result, Types.RES));
            showFormula();
        } else {
            items.clear();
            items.add(new Item(0, Types.RES));
            textViewFormula.setText("");
        }
    }

    /**
     * Judge and record the input number.
     * If the input is too long, make a toast.
     * @param i the input number
     */
    private void recordNum(int i) {
        if (items.size() <= MAX_SIZE) {
            items.add(new Item(i, Types.NUM));
            FormatFormula(items);
            showResult();
            showFormula();
        } else {
            Toast.makeText(context, "输入长度过长！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Judge and record the input operator.
     * If the input is too long, make a toast.
     * @param type the type of the operator
     */
    private void recordOpt(int type) {
        if (items.size() <= MAX_SIZE) {
            items.add(new Item(-1, type));
            FormatFormula(items);
            showResult();
            showFormula();
        } else {
            Toast.makeText(context, "输入长度过长！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Judge and record the input dot.
     * If the input is too long, make a toast.
     */
    private void recordDot() {
        if (items.size() <= MAX_SIZE) {
            items.add(new Item(-1, Types.DOT));
            FormatFormula(items);
            showResult();
            showFormula();
        } else {
            Toast.makeText(context, "输入长度过长！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Format the formula to legal the input formula.
     * @param items input formula.
     */
    private void FormatFormula(ArrayList<Item> items) {
        switch (getType(items.size() - 1)) {
            case Types.NUM:
                /*when the last item is a number, we must judge these things:
                1. whether the former item is a result? If so, clear the result;
                2. whether the former items are an operator and a 0? If so, clear the former 0;
                3. whether the former item is only a 0? If so, clear the former 0;
                4. whether the decimal digits is more than 2? If so, clear the last input;
                 */
                if (isResult(items.size() - 2)) {
                    items.remove(0);
                } else if (((items.size() >= 3) && (isOperator(items.size() - 3)) && (items.get(items.size() - 2).value == 0))
                        || ((items.size() == 2) && (items.get(0).value == 0))) {
                    items.remove(items.size() - 2);
                } else if ((items.size() >= 4) && (isNumber(items.size() - 2)) && (isNumber(items.size() - 3)) && (isDot(items.size() - 4))) {
                    items.remove(items.size() - 1);
                }
                break;
            case Types.ADD:
            case Types.SUB:
                /*when the last item is an operator, we must judge this thing:
                1. whether the former item is an operator or a dot? If so, clear the former operator or dot;
                 */
                if (isOperator(items.size() - 2) || (isDot(items.size() - 2))) {
                    items.remove(items.size() - 2);
                }
                break;
            case Types.DOT:
                /*when the last item is a dot, we must judge these things:
                1. whether the dot exists? If so, clear the last dot;
                2. whether the former item is a result? If so, clear the result;
                 */
                boolean dotFlag = false;
                if (isResult(items.size() - 2)) {
                    items.remove(0);
                }
                for (int i = items.size() - 2; i >= 0; i --) {
                    if (isDot(i)) {
                        dotFlag = true;
                        break;
                    } else if (isOperator(i)) {
                        break;
                    }
                }
                if (dotFlag) items.remove(items.size() - 1);
                break;
        }
    }

    /**
     * calculate and show result, using data in items.
     * if the result is not in range, clear the last item.
     */
    private void showResult() {
        int indexStart = 0, index = 0;
        double result = 0;
        int optType = Types.ADD;
        double optNum;
        boolean optFlag = false;
        if (isResult(0)) {
            if (items.size() < 3) {
                finalResult = items.get(0).value;
                view.setText(new DecimalFormat("#.##").format(finalResult));
                return;
            } else {
                indexStart = 2;
                index = 2;
                result = items.get(0).value;
                optType = items.get(1).type;
            }
        }
        while (index < items.size()) {
            for (; index < items.size(); index ++) {
                if (isOperator(index)) {
                    optFlag = true;
                    break;
                }
            }
            optNum = getNumber(indexStart, index);
            switch (optType) {
                case Types.ADD:
                    result += optNum;
                    break;
                case Types.SUB:
                    result -= optNum;
                    break;
            }
            if (optFlag) {
                optType = items.get(index).type;
                optFlag = false;
            }
            index ++;
            indexStart = index;
        }
        if ((result < NUM_MAX) && (result > -NUM_MAX)) {
            finalResult = result;
            view.setText(new DecimalFormat("#.##").format(finalResult));
        } else {
            items.remove(items.size() - 1);
            showResult();
        }
    }

    /**
     * show formula, using data in items.
     * if the formula is longer than MAX_LENGTH, show some part of the formula.
     */
    private void showFormula() {
        String formula = "";
        for (Item item : items) {
            formula += item.toString();
        }
        if (formula.length() > MAX_LENGTH) {
            formula = "..." + formula.substring(formula.length() - MAX_LENGTH + 1, formula.length());
        }
        textViewFormula.setText(formula);
    }

    /**
     * get the equivalent number from indexStart to indexEnd.
     * @param indexStart the start position of the index
     * @param indexEnd the end position of the index
     * @return the equivalent number
     */
    private double getNumber(int indexStart, int indexEnd) {
        String result = "";
        for (int i = indexStart; i < indexEnd; i ++) {
            result += items.get(i).toString();
        }
        return Double.parseDouble(result);
    }

    /**
     * get the type of the index position of the items.
     * @param index position
     * @return item type
     */
    private int getType(int index) {
        return items.get(index).type;
    }

    private boolean isResult(int index) {
        return getType(index) == Types.RES;
    }

    private boolean isNumber(int index) {
        return getType(index) == Types.NUM;
    }

    private boolean isDot(int index) {
        return getType(index) == Types.DOT;
    }

    private boolean isOperator(int index) {
        return (getType(index) == Types.ADD) || (getType(index) == Types.SUB);
    }
}
