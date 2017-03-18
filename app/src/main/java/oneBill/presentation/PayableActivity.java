package oneBill.presentation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;

public class PayableActivity extends AppCompatActivity {

    Actioner actioner;
    String bookName;
    String type;
    String[] person;
    double[] paid;
    double[] payable;
    static PayableActivity instance;

    double addition;
    double[] additionArray;
    boolean[] isLock;
    double final_sum;
    double remainSum;
    boolean isOperating;
    boolean hasNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payable);

        actioner=new Actioner(this);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        type = intent.getStringExtra("type");
        paid=intent.getDoubleArrayExtra("paid");
        person=intent.getStringArrayExtra("person");
        payable=new double[person.length];
        instance=this;

        additionArray=new double[person.length];
        isLock=new boolean[person.length];
        for(int i=0;i<person.length;i++){
            additionArray[i]=0;
            isLock[i]=false;
        }
        double sum=0;
        for(int j=0;j<person.length;j++) sum=sum+paid[j];
        final DecimalFormat df=new DecimalFormat("#0.00");
        TextView sum_text= (TextView) findViewById(R.id.sum_text);
        sum_text.setText("总计￥"+String.valueOf(df.format(sum)));
        TextView current_sum_text = (TextView) findViewById(R.id.current_sum_text);
        current_sum_text.setText("总和数￥" + String.valueOf(df.format(sum)));
        final_sum=sum;
        remainSum=sum;
        isOperating=false;
        hasNegative=false;

        LinearLayout input_layout= (LinearLayout) findViewById(R.id.input_layout);

        for(int i=0;i<person.length;i++){
            LinearLayout panel=new LinearLayout(this);
            panel.setOrientation(LinearLayout.HORIZONTAL);
            input_layout.addView(panel, LinearLayout.LayoutParams.MATCH_PARENT, 100);

            TextView text=new TextView(this);
            text.setText(person[i]);
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            final EditText edit=new EditText(this);
            edit.setId(i + 1);
            edit.setInputType(0x00002002);
            edit.setBackground(getResources().getDrawable(R.drawable.edit_shape));
            payable[i]=final_sum / person.length;
            edit.setText(String.valueOf(df.format(payable[i])));
            edit.addTextChangedListener(new TextWatcher() {
                String after;
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    if(isOperating){
                        remainSum=final_sum;
                        TextView current_sum_text = (TextView) findViewById(R.id.current_sum_text);
                        current_sum_text.setText("总和数￥" + String.valueOf(df.format(remainSum)));
                    }
                    else{
                        remainSum=0;
                        if("".equals(s.toString())||".".equals(s.toString())) after="0";
                        else after=s.toString();
                        for(int j=0;j<person.length;j++){
                            if(j==edit.getId()-1) remainSum+=Double.valueOf(after);
                            else remainSum+=payable[j];
                        }
                        payable[edit.getId()-1]=Double.valueOf(after);
                        TextView current_sum_text = (TextView) findViewById(R.id.current_sum_text);
                        current_sum_text.setText("总和数￥" + String.valueOf(df.format(remainSum)));
                    }
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
                    isOperating=true;
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
                        if("".equals(edit.getText().toString())||".".equals(edit.getText().toString())) payable[v.getId() - 101]=0;
                        else payable[v.getId() - 101]=Double.valueOf((edit.getText().toString()));
                        double currentSum = final_sum;
                        int numOfNotLock = person.length;
                        for (int j = 0; j < person.length; j++) {
                            if (isLock[j]) {
                                currentSum -= payable[j];
                                numOfNotLock--;
                            }
                            else currentSum -= additionArray[j];
                        }
                        for (int k = 0; k < person.length; k++)
                            if (!isLock[k]){
                                payable[k]=additionArray[k] + currentSum / numOfNotLock;
                                if(payable[k]<0) hasNegative=true;
                                ((EditText) findViewById(k + 1)).setText(String.valueOf(df.format(payable[k])));
                            }
                        remainSum=final_sum;
                    }
                    if(hasNegative){
                        PayableActivity.this.initialize();
                        PayableActivity.this.warning();
                    }
                    isOperating=false;
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
                            isOperating=true;
                            EditText addition_edit = (EditText) dialogView.findViewById(R.id.addition_text);
                            if ("".equals(addition_edit.getText().toString())||".".equals(addition_edit.getText().toString())) addition = 0;
                            else addition = Double.valueOf(addition_edit.getText().toString());
                            dialog.dismiss();

                            addButton.setBackgroundResource(R.mipmap.plus_empty);
                            additionArray[addButton.getId()-201]+=addition;
                            double currentSum=final_sum;
                            int numOfNotLock=person.length;
                            for(int j=0;j<person.length;j++){
                                if(isLock[j]) {
                                    currentSum -= payable[j];
                                    numOfNotLock--;
                                }
                                else currentSum-=additionArray[j];
                            }
                            for(int k=0;k<person.length;k++)
                                if (!isLock[k]){
                                    payable[k]=additionArray[k] + currentSum / numOfNotLock;
                                    if(payable[k]<0) hasNegative=true;
                                    ((EditText) findViewById(k + 1)).setText(String.valueOf(df.format(payable[k])));
                                }
                            if(hasNegative){
                                PayableActivity.this.initialize();
                                PayableActivity.this.warning();
                            }
                            remainSum=final_sum;
                            isOperating=false;
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
                intent.putExtra("bookName",bookName);
                startActivity(intent);
                PayableActivity.this.finish();
            }
        });
        findViewById(R.id.preview_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PayableActivity.this, PreviewActivity.class);
                intent.putExtra("bookName", bookName);
                intent.putExtra("type", type);
                intent.putExtra("person", person);
                intent.putExtra("paid", paid);
                intent.putExtra("payable", payable);
                if(remainSum!=final_sum) PayableActivity.this.warning();
                else startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        actioner.CloseDataBase();
    }

    protected void initialize(){
        DecimalFormat df=new DecimalFormat("#0.00");
        for(int i=0;i<person.length;i++){
            additionArray[i]=0;
            isLock[i]=false;
            payable[i]=final_sum/person.length;
            EditText edit= (EditText) findViewById(i+1);
            edit.setText(String.valueOf(df.format(payable[i])));
            edit.setEnabled(true);
            findViewById(101 + i).setBackgroundResource(R.mipmap.unlock);
        }
        hasNegative=false;
    }

    protected void warning(){
        Toast warning_toast=new Toast(PayableActivity.this);
        TextView warning_text=new TextView(PayableActivity.this);
        warning_text.setText("应付输入有误");
        warning_text.setTextSize(22);
        warning_text.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        warning_toast.setView(warning_text);
        warning_toast.setDuration(Toast.LENGTH_SHORT);
        warning_toast.setGravity(Gravity.CENTER,0,0);
        warning_toast.show();
    }
}
