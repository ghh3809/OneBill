package oneBill.presentation.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.domain.entity.Person;
import oneBill.presentation.customLayout.CalculatorLayout;

/**
 * Created by 豪豪 on 2017/3/19 0019.
 */

public class PaidAdapter extends ArrayAdapter<Person> {

    CalculatorLayout calculatorLayout;
    double[] paid;
    EditText edittextPaid;
    int thePosition;
    TextView textviewSum;
    int resourceId;

    public PaidAdapter(Context context, int textViewResourceId, List<Person> personList) {
        super(context, textViewResourceId, personList);
        paid = new double[personList.size()];
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.thePosition = position;
        Person person = getItem(position);
        Holder mHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            mHolder = new Holder();
            mHolder.textviewPersonName = (TextView) convertView.findViewById(R.id.textviewPersonName);
            mHolder.edittextPaid = (EditText) convertView.findViewById(R.id.edittextPaid);
            convertView.setTag(mHolder);
        } else {
            mHolder = (Holder) convertView.getTag();
        }
        mHolder.textviewPersonName.setText(person.getName());
        mHolder.edittextPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("touch", "click.........");
            }
        });
        mHolder.edittextPaid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("touch", "touch!!!!!!!!!");
                ((ViewGroup) v.getParent()).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                return false;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("touch", "click,,,,,,,,,,,,");
            }
        });
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("touch", "touch??????????????");
                ((ViewGroup) v).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
                return false;
            }
        });

//        edittextPaid.setInputType(InputType.TYPE_NULL);
//        edittextPaid.requestFocus();

//        edittextPaid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    Log.d("person", "listened" + edittextPaid.hashCode());
//                    calculatorLayout.setEditText(edittextPaid);
//                    calculatorLayout.init();
//                } else {
//                    Log.d("person", "unlistened" + edittextPaid.hashCode());
//                    calculatorLayout.setEditText(new EditText(getContext()));
//                }
//            }
//        });
//        edittextPaid.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                paid[thePosition] = Double.parseDouble(s.toString());
//                double sum = 0;
//                for (double p : paid) {
//                    sum += p;
//                }
//                textviewSum.setText(String.valueOf(sum));
//            }
//        });
        return convertView;
    }

    public void setCalculatorLayout(CalculatorLayout calculatorLayout) {
        this.calculatorLayout = calculatorLayout;
    }

    public void setTextviewSum(TextView textviewSum) {
        this.textviewSum = textviewSum;
    }

    private static final class Holder {
        TextView textviewPersonName;
        EditText edittextPaid;
    }
}
