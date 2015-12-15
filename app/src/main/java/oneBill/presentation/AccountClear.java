package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Solution;
import oneBill.domain.entity.error.UnableToClearException;

public class AccountClear extends AppCompatActivity {
    Actioner actioner;
    String name;
    String[] person;
    int numAccountClear = 0;
    ImageView ivToAccountFromClear;
    TextView tvNameInClear,tvAddConstraint;
    Button btnSolve;
    LinearLayout linearAccountClear;
    LinearLayout linearAccountClearConstraint;
    Vector<TextView> vTV = new Vector<TextView>();
    Vector<LinearLayout> vLL = new Vector<LinearLayout>();
    Vector<EditText> vET = new Vector<EditText>();
    Vector<Spinner> vSP = new Vector<Spinner>();
    int countll = 0, countsp = 0, countet = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_clear);

        actioner = new Actioner(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        person=new String[actioner.GetMember(name).size()];
        int index = 0;
        Iterator iterator=actioner.GetMember(name).iterator();
        while(iterator.hasNext()){
            person[index]= (String) iterator.next();
            index++;
        }

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

                vSP.add(countsp, new Spinner(AccountClear.this));
                vSP.get(countsp).setId(countsp);
                vSP.get(countsp).setMinimumWidth(280);
                vSP.get(countsp).setDropDownWidth(280);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AccountClear.this,android.R.layout.simple_list_item_multiple_choice,person);
                vSP.get(countsp).setAdapter(adapter);
                vLL.get(countll).addView(vSP.get(countsp++));

                TextView tv1 = new TextView(AccountClear.this);
                tv1.setText("给");
                vLL.get(countll).addView(tv1);

                vSP.add(countsp, new Spinner(AccountClear.this));
                vSP.get(countsp).setId(countsp);
                vSP.get(countsp).setMinimumWidth(280);
                vSP.get(countsp).setDropDownWidth(280);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(AccountClear.this,android.R.layout.simple_list_item_multiple_choice,person);
                vSP.get(countsp).setAdapter(adapter1);
                vLL.get(countll).addView(vSP.get(countsp++));

                TextView tv2 = new TextView(AccountClear.this);
                tv2.setText(":￥");
                vLL.get(countll).addView(tv2);

                vET.add(countet, new EditText(AccountClear.this));
                vET.get(countet).setId(countet);
                vET.get(countet).setWidth(280);
                vET.get(countet).setText("0");
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

                /*非浮点数的金额*/
                boolean wrongNumber = false;
                for(int k = 0; k < countet; ++ k){
                    String string = vET.get(k).getText().toString();
                    for(int t = 0; t < string.length(); ++ t) if(string.charAt(t)!='.') if(!Character.isDigit(string.charAt(t))){
                        wrongNumber = true;
                        break;
                    }
                }

                if(wrongNumber){
                    Toast.makeText(getApplicationContext(),"Wrong number found",Toast.LENGTH_SHORT).show();
                }
                else{
                    /*获得输入的约束条件*/
                    ArrayList<Solution> arraylist = new ArrayList<Solution>();
                    for(int k = 0; k < countet; ++ k){
                        Editable ed1, ed2, ed3;
                        ed1 = (Editable) vSP.get(2 * k).getSelectedItem();
                        ed2 = (Editable) vSP.get(2 * k + 1).getSelectedItem();
                        ed3 = vET.get(k).getText();

                        try {
                            Solution solution = new Solution(ed1.toString(), ed2.toString(), Double.parseDouble(ed3.toString()));
                            arraylist.add(solution);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),"Unable to generate a solution",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }

                    /*获取解决方案*/
                    linearAccountClear = (LinearLayout) findViewById(R.id.linearAccountClear);
                    try {
                        arraylist = actioner.SettleRecord(name);
                        numAccountClear = arraylist.size();
                    } catch (UnableToClearException e) {
                        Toast.makeText(getApplicationContext(),"Unable to settle",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    /*列示解决方案*/
                    for(int i = 0; i < numAccountClear; ++ i){
                        vTV.add(i, new TextView(AccountClear.this));

                        StringBuilder stringbuilder = new StringBuilder();
                        Solution solution = arraylist.get(i);
                        stringbuilder.append(solution.getGiver());
                        stringbuilder.append("    给    ");
                        stringbuilder.append(solution.getReceiver());
                        stringbuilder.append("    给    ");
                        stringbuilder.append(solution.getAmount());
                        vTV.get(i).setText(stringbuilder);

                        linearAccountClear.addView(vTV.get(i));
                    }

                    /*清空约束条件*/
                    for(int k = 0; k < countll; ++ k)
                        linearAccountClearConstraint.removeView(vLL.get(k));
                    countll = 0;
                    countet = 0;
                }
            }
        });
    }
}
