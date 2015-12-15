package oneBill.presentation;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.Iterator;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Type;

public class AddRecordActivity extends AppCompatActivity {

    String bookName;
    Actioner actioner;
    int type;
    String[] person;
    static AddRecordActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        actioner=new Actioner(this);
        Intent intent=getIntent();
        bookName=intent.getStringExtra("bookName");
        person=new String[actioner.GetMember(bookName).size()];
        int index=0;
        Iterator iterator=actioner.GetMember(bookName).iterator();
        while(iterator.hasNext()){
            person[index]= (String) iterator.next();
            index++;
        }
        instance=this;

        final TabHost tabHost= (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tab1=tabHost.newTabSpec("tab1").setIndicator("消费").setContent(R.id.tab_consume);
        tabHost.addTab(tab1);
        TabHost.TabSpec tab2=tabHost.newTabSpec("tab2").setIndicator("借款").setContent(R.id.tab_loan);
        tabHost.addTab(tab2);

        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,person);
        spinner.setAdapter(adapter);
        Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,person);
        spinner2.setAdapter(adapter2);

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
            edit.setHint("￥");
            edit.setInputType(0x00002002);
            edit.setGravity(Gravity.FILL_HORIZONTAL);
            edit.setBackground(getResources().getDrawable(R.drawable.edit_shape));
            edit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    double sum = 0;
                    for (int i = 0; i < person.length; i++) {
                        double[] paid = new double[person.length];
                        EditText edit = (EditText) findViewById(i + 1);
                        if ("".equals(edit.getText().toString())||".".equals(edit.getText().toString())) paid[i] = 0;
                        else paid[i] = Double.valueOf(edit.getText().toString());
                        sum = sum + paid[i];
                    }
                    TextView sum_text = (TextView) findViewById(R.id.sum_text);
                    sum_text.setText("总计￥" + String.valueOf(sum));
                }
            });

            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight=1.0f;
            panel.addView(text,lp);
            lp.weight=1.0f;
            panel.addView(edit,lp);
        }

        final ImageButton food_button= (ImageButton) findViewById(R.id.food_button);
        final ImageButton accom_button= (ImageButton) findViewById(R.id.accom_button);
        final ImageButton trans_button= (ImageButton) findViewById(R.id.trans_button);
        final ImageButton play_button= (ImageButton) findViewById(R.id.play_button);
        final ImageButton other_button= (ImageButton) findViewById(R.id.other_button);
        food_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAlpha((float) 1.0);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=1;
            }
        });
        trans_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                play_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=2;
            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                trans_button.setAlpha((float) 1.0);
                v.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=3;
            }
        });
        accom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                other_button.setAlpha((float)0.5);
                type=4;
            }
        });
        other_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                type=5;
            }
        });

        findViewById(R.id.back_to_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                change the intent activity
                Intent intent=new Intent(AddRecordActivity.this,Account.class);
                intent.putExtra("name",bookName);
                startActivity(intent);
                AddRecordActivity.this.finish();
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabHost.getCurrentTab()==0){
                    double[] paid=new double[person.length];
                    for(int i=0;i<person.length;i++){
                        EditText edit= (EditText) findViewById(i+1);
                        if("".equals(edit.getText().toString())||".".equals(edit.getText().toString())) paid[i]=0;
                        else paid[i]=Double.valueOf(edit.getText().toString());
                    }
                    Intent intent=new Intent(AddRecordActivity.this,PayableActivity.class);
                    intent.putExtra("bookName",bookName);
                    intent.putExtra("type",type);
                    intent.putExtra("person",person);
                    intent.putExtra("paid",paid);
                    startActivity(intent);
                }
                else{
                    Spinner borrower_spinner=(Spinner)findViewById(R.id.spinner);
                    String borrower=borrower_spinner.getSelectedItem().toString();
                    Spinner lender_spinner= (Spinner) findViewById(R.id.spinner2);
                    String lender=lender_spinner.getSelectedItem().toString();
                    EditText edit= (EditText) findViewById(R.id.editText);
                    double amount;
                    if ("".equals(edit.getText().toString())||".".equals(edit.getText().toString())) amount = 0;
                    else amount=Double.valueOf(edit.getText().toString());
                    actioner.CreateLoanRecord(bookName,lender,borrower,amount);
                    Intent intent=new Intent(AddRecordActivity.this,Account.class);
                    intent.putExtra("name",bookName);
                    startActivity(intent);
                    AddRecordActivity.this.finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_record, menu);
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
