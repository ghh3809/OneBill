package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Solution;

public class AccountClear extends AppCompatActivity {
    Actioner actioner;
    String name;
    int numAccountClear = 0;
    ImageView ivToAccountFromClear;
    TextView tvNameInClear,tvAddConstraint;
    Button btnSolve;
    LinearLayout linearAccountClear;
    LinearLayout linearAccountClearConstraint;
    Vector<TextView> vTV = new Vector<TextView>();
    Vector<EditText> vET = new Vector<EditText>();
    Vector<LinearLayout> vLL = new Vector<LinearLayout>();
    int countll = 0, countet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_clear);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        ivToAccountFromClear = (ImageView) findViewById(R.id.ivToAccountFromClear);
        ivToAccountFromClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountClear.this, Account.class));
            }
        });

        tvNameInClear = (TextView) findViewById(R.id.tvNameInClear);
        tvNameInClear.setText(name);

        linearAccountClearConstraint = (LinearLayout) findViewById(R.id.linearAccountClearConstraint);
        tvAddConstraint = (TextView) findViewById(R.id.tvAddConstraint);
        tvAddConstraint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vLL.add(countll, new LinearLayout(AccountClear.this));
                vLL.get(countll).setOrientation(LinearLayout.HORIZONTAL);

                vET.add(countet, new EditText(AccountClear.this));
                vET.get(countet).setId(countet);
                vET.get(countet).setWidth(280);
                vLL.get(countll).addView(vET.get(countet++));

                TextView tv1 = new TextView(AccountClear.this);
                tv1.setText("给");
                vLL.get(countll).addView(tv1);

                vET.add(countet, new EditText(AccountClear.this));
                vET.get(countet).setId(countet);
                vET.get(countet).setWidth(280);
                vLL.get(countll).addView(vET.get(countet++));

                TextView tv2 = new TextView(AccountClear.this);
                tv2.setText(":￥");
                vLL.get(countll).addView(tv2);

                vET.add(countet, new EditText(AccountClear.this));
                vET.get(countet).setId(countet);
                vET.get(countet).setWidth(280);
                vLL.get(countll).addView(vET.get(countet++));

                linearAccountClearConstraint.addView(vLL.get(countll++));
            }
        });

        btnSolve = (Button) findViewById(R.id.btnSolve);
        btnSolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {;
                /*先把已有的解决方案清空*/
                for(int k = 0; k < numAccountClear; ++ k)
                    linearAccountClear.removeView(vTV.get(k));
                numAccountClear = 0;

                /*获得约束条件*/
                ArrayList<Solution> arraylist = new ArrayList<Solution>();
                for(int k = 0; k < countet/3; ++ k){
                    Editable ed1, ed2, ed3;
                    ed1 = vET.get(3 * k).getText();
                    ed2 = vET.get(3 * k + 1).getText();
                    ed3 = vET.get(3 * k + 2).getText();

                    Solution solution = new Solution(ed1.toString(),ed2.toString(),Double.parseDouble(ed3.toString()));
                    arraylist.add(solution);
                }

                linearAccountClear = (LinearLayout) findViewById(R.id.linearAccountClear);
                /*try {
                    arraylist = actioner.SettleRecord(name);
                } catch (UnableToClearException e) {
                    System.out.println("unable to settle");
                    e.printStackTrace();
                }
                numAccountClear = arraylist.size();
                */

                for(int i = 0; i < numAccountClear; ++ i){
                    vTV.add(i, new TextView(AccountClear.this));

                    StringBuilder stringbuilder = new StringBuilder();
                    /*Solution solution = arraylist.get(i);
                    stringbuilder.append(solution.getGiver());
                    stringbuilder.append("    给    ");
                    stringbuilder.append(solution.getReceiver());
                    stringbuilder.append("    给    ");
                    stringbuilder.append(solution.getAmount());
                    vTV.get(i).setText(stringbuilder);
                    */

                    linearAccountClear.addView(vTV.get(i));
                }

                /*清空约束条件*/
                for(int k = 0; k < countll; ++ k)
                    linearAccountClearConstraint.removeView(vLL.get(k));
                countll = 0;
                countet = 0;
            }
        });
    }
}
