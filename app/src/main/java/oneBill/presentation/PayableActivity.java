package oneBill.presentation;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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
    Type type;
    String[] person;
    double[] paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payable);

        actioner=new Actioner(this);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        type= (Type) intent.getSerializableExtra("type");
        paid=intent.getDoubleArrayExtra("paid");
        person=intent.getStringArrayExtra("person");

        LinearLayout input_layout= (LinearLayout) findViewById(R.id.input_layout);

        for(int i=0;i<person.length;i++){
            LinearLayout panel=new LinearLayout(this);
            panel.setOrientation(LinearLayout.HORIZONTAL);
            input_layout.addView(panel, LinearLayout.LayoutParams.MATCH_PARENT, 160);

            TextView text=new TextView(this);
            text.setText(person[i]);
            text.setTextSize(18);
            text.setGravity(Gravity.CENTER);

            EditText editText=new EditText(this);
            editText.setId(i + 1);
            editText.setHint("￥");
            editText.setInputType(0x00002002);

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
                    if(findViewById(v.getId() - 100).isEnabled()){
                        findViewById(v.getId() - 100).setEnabled(false);
                        v.setBackgroundResource(R.mipmap.lock);
                    }
                    else {
                        findViewById(v.getId() - 100).setEnabled(true);
                        v.setBackgroundResource(R.mipmap.unlock);
                    }
                }
            });

            TextView blankText=new TextView(this);

            Button addButton=new Button(this);
            addButton.setBackgroundResource(R.mipmap.plus_empty);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setBackgroundResource(R.mipmap.plus_full);
                }
            });

            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT );
            lp.weight=2.0f;
            panel.addView(text,lp);
            lp.weight=1.0f;
            panel.addView(editText,lp);
            lp.weight=2.0f;
            panel.addView(subPanel,lp);
            subPanel.addView(lockButton,44,44);
            subPanel.addView(blankText,44,44);
            subPanel.addView(addButton,44,44);
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
                for (int i = 0; i < paid.length; i++) {
                    EditText edit = (EditText) findViewById(i + 1);
                    payable[i] = Double.valueOf(edit.getText().toString());
                }
                Intent intent = new Intent(PayableActivity.this, PreviewActivity.class);
                intent.putExtra("bookName",bookName);
                intent.putExtra("type", type);
                intent.putExtra("person",person);
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
