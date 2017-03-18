package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.AmountMismatchException;

public class PreviewActivity extends AppCompatActivity {

    Actioner actioner;
    String bookName;
    String type;
    String[] person;
    double[] paid;
    double[] payable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        actioner=new Actioner(this);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        type= intent.getStringExtra("type");
        paid=intent.getDoubleArrayExtra("paid");
        payable=intent.getDoubleArrayExtra("payable");
        person=intent.getStringArrayExtra("person");

        DecimalFormat df=new DecimalFormat("#0.00");
        double sum=0;
        for(int j=0;j<person.length;j++) sum=sum+paid[j];
        TextView amount_text=(TextView)findViewById(R.id.amount_text);
        amount_text.setText("￥"+String.valueOf(df.format(sum)));

        LinearLayout display_layout= (LinearLayout) findViewById(R.id.display_layout);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewActivity.this, PayableActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.confirm_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList paid_list=new ArrayList();
                ArrayList payable_list=new ArrayList();
                for(int i=0;i<person.length;i++){
                    paid_list.add(paid[i]);
                    payable_list.add(payable[i]);
                }
                try {
                    actioner.CreateConsumeRecord(bookName,type,paid_list,payable_list);
                    actioner.CloseDataBase();
                } catch (AmountMismatchException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(PreviewActivity.this, Account.class);
                intent.putExtra("name",bookName);
                startActivity(intent);
                PreviewActivity.this.finish();
                PayableActivity.instance.finish();
                AddRecordActivity.instance.finish();
            }
        });

        for(int i=0;i<person.length;i++){
            LinearLayout panel=new LinearLayout(this);
            panel.setOrientation(LinearLayout.HORIZONTAL);
            display_layout.addView(panel, LinearLayout.LayoutParams.MATCH_PARENT, 100);

            TextView[] text=new TextView[3];
            for(int j=0;j<3;j++){
                text[j]=new TextView(this);
                text[j].setTextSize(18);
                text[j].setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
                lp.weight=1;
                panel.addView(text[j],lp);
            }
            text[0].setText(person[i]);
            text[1].setText(String.valueOf(paid[i]));
            text[2].setText(String.valueOf(df.format(payable[i])));
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        actioner.CloseDataBase();
    }
}
