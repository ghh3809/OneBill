package oneBill.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.TextView;

import java.util.Iterator;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Type;

public class AddRecordActivity extends AppCompatActivity {

    String bookName;
    Actioner actioner;
    Type type;
    String[] person;

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
                accom_button.setAlpha((float)0.5);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=Type.FOOD;
            }
        });
        accom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=Type.ACCOM;
            }
        });
        trans_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                play_button.setAlpha((float)0.5);
                other_button.setAlpha((float)0.5);
                type=Type.TRANS;
            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                trans_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                other_button.setAlpha((float)0.5);
                type=Type.PLAY;
            }
        });
        other_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_button.setAlpha((float)0.5);
                accom_button.setAlpha((float)0.5);
                trans_button.setAlpha((float)0.5);
                play_button.setAlpha((float)0.5);
                v.setAlpha((float) 1.0);
                type=Type.OTHER;
            }
        });

        findViewById(R.id.back_to_book_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                change the intent activity
                Intent intent=new Intent(AddRecordActivity.this,MainActivity.class);
                startActivity(intent);
                AddRecordActivity.this.finish();
            }
        });
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tabHost.getCurrentTab()==0){
                    double[] paid=new double[person.length];
                    for(int i=0;i<paid.length;i++){
                        EditText edit= (EditText) findViewById(i+1);
                        paid[i]=Double.valueOf(edit.getText().toString());
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
                    double amount=Double.valueOf(edit.getText().toString());
                    actioner.CreateLoanRecord(bookName,lender,borrower,amount);
                    Intent intent=new Intent(AddRecordActivity.this,MainActivity.class);
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
