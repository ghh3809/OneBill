package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

public class AddBook extends AppCompatActivity {
    ImageButton ibtnaddperson;
    ImageButton ibtnback;
    ImageButton ibtnok;
    EditText etaddperson;
    EditText etname;
    Vector<EditText> personname=new Vector<EditText>();
    int i=0;//输入人员的个数
    String person;
    RelativeLayout.LayoutParams etlaypa;
    RelativeLayout.LayoutParams ibtnlaypa;
    LinearLayout llayaddperson;
    RelativeLayout rlayibtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ibtnaddperson= (ImageButton) findViewById(R.id.imagebtnaddperson);
        ibtnback= (ImageButton) findViewById(R.id.imagebtnback);
        ibtnok= (ImageButton) findViewById(R.id.imagebtnok);
        etaddperson= (EditText) findViewById(R.id.etperson);
        etname= (EditText) findViewById(R.id.etname);
        llayaddperson= (LinearLayout) findViewById(R.id.llaoutperson);
        ibtnaddperson.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               person=etaddperson.getText().toString();
               personname.add(i, new EditText(AddBook.this));
               personname.get(i).setText(person);
               personname.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
               personname.get(i).setBackground(getResources().getDrawable(R.drawable.edit_shape));
               personname.get(i).setMinWidth(DensityUtil.dip2px(getApplicationContext(), 200));
               personname.get(i).setHeight(DensityUtil.dip2px(getApplicationContext(), 30));
               LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                       ,RelativeLayout.LayoutParams.WRAP_CONTENT);
               lp1.setMargins(DensityUtil.dip2px(getApplicationContext(), 40),DensityUtil.dip2px(getApplicationContext(), 20),0,0);
               llayaddperson.addView(personname.get(i),lp1);
               etaddperson.setText("");
               i++;
           }
       });
        ibtnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBook.this.finish();

            }
        });
        ibtnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBook.this, Account.class);
                Editable editable = etname.getText();
                intent.putExtra("name",editable.toString());
                startActivity(intent);
            }
        });
    }
}
