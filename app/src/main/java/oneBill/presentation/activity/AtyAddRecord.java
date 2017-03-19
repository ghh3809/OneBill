package oneBill.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Person;
import oneBill.presentation.Account;
import oneBill.presentation.PayableActivity;
import oneBill.presentation.customLayout.CalculatorLayout;

/**
 * Created by 豪豪 on 2017/3/19 0019.
 */

public class AtyAddRecord extends AppCompatActivity {

    private final float alphaValue = 0.3f;
    private Actioner actioner;
    private CalculatorLayout calculatorLayout;
    private TextView textviewSum, textviewTitle;
    private Spinner spinnerLender, spinnerBorrower;
    private TabHost tabHost;
    private LinearLayout layoutEat, layoutTrans, layoutPlay, layoutAccom, layoutOther, linearLayoutPaid, layoutBack, layoutContinue;
    private String bookName;
    private ArrayList<Person> persons;
    private ArrayList<EditText> editTexts;
    private EditText edittext, edittextLoanMoney;
    private String type = "吃喝";
    private String[] names;
    private double[] amounts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_add_record);
        actioner = new Actioner(this);
        bookName = getIntent().getStringExtra("bookName");
        calculatorLayout = (CalculatorLayout) findViewById(R.id.calculatorLayout);
        textviewSum = (TextView) findViewById(R.id.textviewSum);
        textviewTitle = (TextView) findViewById(R.id.textviewTitle);
        textviewTitle.setText(bookName);
        spinnerLender = (Spinner) findViewById(R.id.spinnerLender);
        spinnerBorrower = (Spinner) findViewById(R.id.spinnerBorrower);
        layoutEat = (LinearLayout) findViewById(R.id.layoutEat);
        layoutTrans = (LinearLayout) findViewById(R.id.layoutTrans);
        layoutPlay = (LinearLayout) findViewById(R.id.layoutPlay);
        layoutAccom = (LinearLayout) findViewById(R.id.layoutAccom);
        layoutOther = (LinearLayout) findViewById(R.id.layoutOther);
        layoutBack = (LinearLayout) findViewById(R.id.layoutBack);
        layoutContinue = (LinearLayout) findViewById(R.id.layoutContinue);
        edittextLoanMoney = (EditText) findViewById(R.id.edittextLoanMoney);
        edittextLoanMoney.setInputType(InputType.TYPE_NULL);
        setClickListener();
        linearLayoutPaid = (LinearLayout) findViewById(R.id.linearlayoutPaid);
        tabHost= (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1=tabHost.newTabSpec("tab1").setIndicator("消费").setContent(R.id.tab_consume);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2=tabHost.newTabSpec("tab2").setIndicator("借款").setContent(R.id.tab_loan);
        tabHost.addTab(tab2);
    }

    private void setClickListener() {

        layoutEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEat.setAlpha(1);
                layoutTrans.setAlpha(alphaValue);
                layoutPlay.setAlpha(alphaValue);
                layoutAccom.setAlpha(alphaValue);
                layoutOther.setAlpha(alphaValue);
                type = "吃喝";
            }
        });
        layoutTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEat.setAlpha(alphaValue);
                layoutTrans.setAlpha(1);
                layoutPlay.setAlpha(alphaValue);
                layoutAccom.setAlpha(alphaValue);
                layoutOther.setAlpha(alphaValue);
                type = "交通";
            }
        });
        layoutPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEat.setAlpha(alphaValue);
                layoutTrans.setAlpha(alphaValue);
                layoutPlay.setAlpha(1);
                layoutAccom.setAlpha(alphaValue);
                layoutOther.setAlpha(alphaValue);
                type = "娱乐";
            }
        });
        layoutAccom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEat.setAlpha(alphaValue);
                layoutTrans.setAlpha(alphaValue);
                layoutPlay.setAlpha(alphaValue);
                layoutAccom.setAlpha(1);
                layoutOther.setAlpha(alphaValue);
                type = "住宿";
            }
        });
        layoutOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutEat.setAlpha(alphaValue);
                layoutTrans.setAlpha(alphaValue);
                layoutPlay.setAlpha(alphaValue);
                layoutAccom.setAlpha(alphaValue);
                layoutOther.setAlpha(1);
                type = "其它";
            }
        });
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtyAddRecord.this.finish();
            }
        });
        layoutContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tabHost.getCurrentTab() == 0) {
                    amounts = new double[persons.size()];
                    for (int i = 0; i < persons.size(); i ++) {
                        amounts[i] = Double.parseDouble(editTexts.get(i).getText().toString());
                    }
                    Intent intent = new Intent(AtyAddRecord.this, PayableActivity.class);
                    intent.putExtra("bookName", bookName);
                    intent.putExtra("type", type);
                    intent.putExtra("person", names);
                    intent.putExtra("paid", amounts);
                    AtyAddRecord.this.startActivity(intent);
                } else {
                    String lender = spinnerLender.getSelectedItem().toString();
                    String borrower = spinnerBorrower.getSelectedItem().toString();
                    double amount = Double.parseDouble(edittextLoanMoney.getText().toString());
                    actioner.CreateLoanRecord(bookName, lender, borrower, amount);
                    Intent intent = new Intent(AtyAddRecord.this, Account.class);
                    intent.putExtra("bookName", bookName);
                    AtyAddRecord.this.startActivity(intent);
                }
            }
        });
        edittextLoanMoney.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    calculatorLayout.setEditText(edittextLoanMoney);
                    calculatorLayout.init();
                } else {
                    calculatorLayout.setEditText(new EditText(AtyAddRecord.this));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTexts = new ArrayList<>();
        persons = actioner.GetMembers(bookName);
        names = new String[persons.size()];
        for (int i = 0; i < persons.size(); i ++) {
            names[i] = persons.get(i).getName();
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, names);
        spinnerLender.setAdapter(adapter);
        spinnerBorrower.setAdapter(adapter2);
        Log.d("size", String.valueOf(persons.size()));
        linearLayoutPaid.removeAllViews();
        for (int i = 0; i < persons.size(); i ++) {
            View view = View.inflate(this, R.layout.person_paid_data, null);
            TextView textviewPersonName = (TextView) view.findViewById(R.id.textviewPersonName);
            edittext = (EditText) view.findViewById(R.id.edittextPaid);
            textviewPersonName.setText(persons.get(i).getName());
            edittext.setInputType(InputType.TYPE_NULL);
            edittext.setText("0");
            edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        calculatorLayout.setEditText((EditText) v);
                        calculatorLayout.init();
                    } else {
                        calculatorLayout.setEditText(new EditText(AtyAddRecord.this));
                    }
                }
            });
            edittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    double sum = 0;
                    for (EditText e : editTexts) {
                        if (!e.getText().toString().equals("")) {
                            sum += Double.parseDouble(e.getText().toString());
                        }
                    }
                    textviewSum.setText(new DecimalFormat("#.##").format(sum));
                }
            });
            editTexts.add(edittext);
            linearLayoutPaid.addView(view);
        }
    }

    @Override
    protected void onDestroy() {
        actioner.CloseDataBase();
        super.onDestroy();
    }
}
