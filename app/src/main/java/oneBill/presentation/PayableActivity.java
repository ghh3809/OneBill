package oneBill.presentation;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Type;

public class PayableActivity extends AppCompatActivity {

    Actioner actioner;
    String bookName;
    int type;
    String[] person;
    double[] paid;

    double addition;
    double[] additionArray;
    boolean[] isLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payable);

        actioner=new Actioner(this);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        type= intent.getIntExtra("type", 0);
        paid=intent.getDoubleArrayExtra("paid");
        person=intent.getStringArrayExtra("person");

        additionArray=new double[person.length];
        isLock=new boolean[person.length];
        for(int i=0;i<person.length;i++){
            additionArray[i]=0;
            isLock[i]=false;
        }
        double sum=0;
        for(int j=0;j<person.length;j++) sum=sum+paid[j];
        TextView sum_text= (TextView) findViewById(R.id.sum_text);
        sum_text.setText("总计￥"+String.valueOf(sum));
        final double final_sum=sum;

        LinearLayout input_layout= (LinearLayout) findViewById(R.id.input_layout);

        for(int i=0;i<person.length;i++){
            LinearLayout panel=new LinearLayout(this);
            panel.setOrientation(LinearLayout.HORIZONTAL);
            input_layout.addView(panel, LinearLayout.LayoutParams.MATCH_PARENT, 100);

            TextView text=new TextView(this);
            text.setText(person[i]);
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            EditText edit=new EditText(this);
            edit.setId(i + 1);
            edit.setText(String.valueOf(sum / person.length));
            edit.setInputType(0x00002002);
            edit.setBackgroundColor(Color.WHITE);
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    double[] payable = new double[person.length];
                    double currentSum = 0;
                    for (int i = 0; i < person.length; i++) {
                        EditText edit = (EditText) findViewById(i + 1);
                        if ("".equals(edit.getText().toString())) payable[i] = 0;
                        else payable[i] = Double.valueOf(edit.getText().toString());
                        currentSum = currentSum + payable[i];
                    }
                    TextView current_sum_text = (TextView) findViewById(R.id.current_sum_text);
                    current_sum_text.setText("总和数￥" + String.valueOf(currentSum));
                }
            });

            LinearLayout subPanel=new LinearLayout(this);
            subPanel.setOrientation(LinearLayout.HORIZONTAL);
            subPanel.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            subPanel.setVerticalGravity(Gravity.CENTER_VERTICAL);

            Button lockButton=new Button(this);
            lockButton.setBackgroundResource(R.mipmap.unlock);
            lockButton.setId(101 + i);
            lockButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLock[v.getId() - 101]) {
                        EditText edit = (EditText) findViewById(v.getId() - 100);
                        edit.setEnabled(true);
                        v.setBackgroundResource(R.mipmap.unlock);
                        isLock[v.getId() - 101] = false;
                    } else {
                        EditText edit = (EditText) findViewById(v.getId() - 100);
                        edit.setEnabled(false);
                        v.setBackgroundResource(R.mipmap.lock);
                        isLock[v.getId() - 101] = true;
                        double currentSum = final_sum;
                        int numOfNotLock = person.length;
                        for (int j = 0; j < person.length; j++) {
                            if (isLock[j]) {
                                currentSum -= Double.valueOf(((EditText) findViewById(j + 1)).getText().toString());
                                numOfNotLock--;
                            } else currentSum -= additionArray[j];
                        }
                        for (int k = 0; k < person.length; k++)
                            if (!isLock[k])
                                ((EditText) findViewById(k + 1)).setText(String.valueOf(additionArray[k] + currentSum / numOfNotLock));
                    }
                }
            });

            TextView blankText=new TextView(this);

            final Button addButton=new Button(this);
            addButton.setBackgroundResource(R.mipmap.plus_empty);
            addButton.setId(201 + i);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundResource(R.mipmap.plus_full);

                    AlertDialog.Builder builder= new AlertDialog.Builder(PayableActivity.this);
                    final View dialogView=LayoutInflater.from(PayableActivity.this).inflate(R.layout.dialog, null);
                    final AlertDialog dialog=builder.setView(dialogView).show();
                    dialogView.findViewById(R.id.comfirm_add_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText addition_edit = (EditText) dialogView.findViewById(R.id.addition_text);
                            if ("".equals(addition_edit.getText().toString())) addition = 0;
                            else addition = Double.valueOf(addition_edit.getText().toString());
                            dialog.dismiss();

                            addButton.setBackgroundResource(R.mipmap.plus_empty);
                            additionArray[addButton.getId()-201]+=addition;
                            double currentSum=final_sum;
                            int numOfNotLock=person.length;
                            for(int j=0;j<person.length;j++){
                                if(isLock[j]) {
                                    currentSum -= Double.valueOf(((EditText) findViewById(j + 1)).getText().toString());
                                    numOfNotLock--;
                                }
                                currentSum-=additionArray[j];
                            }
                            for(int k=0;k<person.length;k++)
                                if(!isLock[k])
                                    ((EditText) findViewById(k+1)).setText(String.valueOf(additionArray[k]+currentSum/numOfNotLock));
                        }
                    });

                }
            });

            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
            lp.weight=2.0f;
            panel.addView(text,lp);
            lp.weight=1.0f;
            panel.addView(edit,lp);
            lp.weight=2.0f;
            panel.addView(subPanel, lp);
            subPanel.addView(lockButton, 44, 44);
            subPanel.addView(blankText, 44, 44);
            subPanel.addView(addButton, 44, 44);
        }

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayableActivity.this, AddRecordActivity.class);
                startActivity(intent);
                PayableActivity.this.finish();
            }
        });
        findViewById(R.id.preview_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double[] payable = new double[paid.length];
                for (int i = 0; i < person.length; i++) {
                    EditText edit = (EditText) findViewById(i + 1);
                    if ("".equals(edit.getText().toString())) payable[i] = 0;
                    else payable[i] = Double.valueOf(edit.getText().toString());
                }
                Intent intent = new Intent(PayableActivity.this, PreviewActivity.class);
                intent.putExtra("bookName", bookName);
                intent.putExtra("type", type);
                intent.putExtra("person", person);
                intent.putExtra("paid", paid);
                intent.putExtra("payable", payable);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payable, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
