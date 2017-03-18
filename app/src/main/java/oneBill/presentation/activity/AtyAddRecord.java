package oneBill.presentation.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.presentation.customLayout.CalculatorLayout;

/**
 * Created by 豪豪 on 2017/3/19 0019.
 */

public class AtyAddRecord extends AppCompatActivity {

    private EditText editText, editText2;
    private CalculatorLayout calculatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_add_record);
        editText = (EditText) findViewById(R.id.edittextSum);
        editText2 = (EditText) findViewById(R.id.edittextSum2);
        editText.setInputType(InputType.TYPE_NULL);
        editText2.setInputType(InputType.TYPE_NULL);
        calculatorLayout = (CalculatorLayout) findViewById(R.id.calculatorLayout);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calculatorLayout.setEditText(editText);
                    calculatorLayout.init();
                } else {
                    calculatorLayout.setEditText(new EditText(AtyAddRecord.this));
                }
            }
        });
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calculatorLayout.setEditText(editText2);
                    calculatorLayout.init();
                } else {
                    calculatorLayout.setEditText(new EditText(AtyAddRecord.this));
                }
            }
        });
//        View view = getCurrentFocus();
//        if (view instanceof EditText) {
//            calculatorLayout.setEditText((EditText) view);
//        } else {
//            calculatorLayout.setEditText(new EditText(this));
//        }
    }
}
