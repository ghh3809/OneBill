package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.NullException;

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
    private Actioner actioner;
    String bookname;
    View.OnClickListener oklistener;
    Vector<String> addedperson=new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ibtnaddperson= (ImageButton) findViewById(R.id.imagebtnaddperson);
        ibtnback= (ImageButton) findViewById(R.id.imagebtnback);
        ibtnok= (ImageButton) findViewById(R.id.imagebtnok);
        etaddperson= (EditText) findViewById(R.id.etperson);
        etname= (EditText) findViewById(R.id.etname);
        actioner=new Actioner(this);
        etname.setText(actioner.GetDefaultName());
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
                Intent intent= new Intent(AddBook.this,MainActivity.class);
                startActivity(intent);
            }
        });
        oklistener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookname=etname.getText().toString();
                if (bookname!= "") {
                    try {
                        actioner.CreateBook(bookname);
                    } catch (NullException e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    } catch (DuplicationNameException e) {
                        System.out.println(e.toString());
                        e.printStackTrace();
                    }
                    for(int j=0;j<i;j++){
                        if(personname.get(j).getText().toString()!="")
                            try {
                                actioner.CreateMember(bookname, personname.get(j).getText().toString());
                            } catch (NullException e) {
                                e.printStackTrace();
                            } catch (DuplicationNameException e) {
                                e.printStackTrace();
                            }
                    }
                    if(etaddperson.getText().toString()!="")
                        try {
                            actioner.CreateMember(bookname, etaddperson.getText().toString());
                        } catch (NullException e) {
                            e.printStackTrace();
                        } catch (DuplicationNameException e) {
                            e.printStackTrace();
                        }
                    actioner.CloseDataBase();

                    Intent intent = new Intent(AddBook.this, Account.class);
                    intent.putExtra("name", bookname);

                    startActivity(intent);
                    AddBook.this.finish();


            }
        }
        };
        ibtnok.setOnClickListener(oklistener);
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        actioner.CloseDataBase();
    }
}
