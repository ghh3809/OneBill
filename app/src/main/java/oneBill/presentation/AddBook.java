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
import android.widget.Toast;

import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.MemberReturnException;
import oneBill.domain.entity.error.NullException;
import oneBill.presentation.activity.AtyMain;

public class AddBook extends AppCompatActivity {
    ImageButton ibtnaddperson;
    ImageButton ibtnback;
    ImageButton ibtnok;
    EditText etaddperson;
    EditText etname;
    Vector<EditText> personname=new Vector<EditText>();
    int i = 0;//输入人员的个数
    String person;
    LinearLayout llayaddperson;
    private Actioner actioner;
    String bookname;
    View.OnClickListener oklistener;
    boolean nullname=false;
    boolean duplicatename=false;

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
                Intent intent= new Intent(AddBook.this, AtyMain.class);
                startActivity(intent);
            }
        });
        oklistener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        bookname=etname.getText().toString();
                        actioner.CreateBook(bookname);
                        for(int j=0;j<i;j++){
                                try {
                                    actioner.CreateMember(bookname, personname.get(j).getText().toString());
                                } catch (NullException e) {
                                    nullname=true;
                                    e.printStackTrace();
                                } catch (DuplicationNameException e) {
                                    duplicatename=true;
                                    e.printStackTrace();
                                } catch (MemberReturnException e) {}
                        }
                            try {
                                actioner.CreateMember(bookname, etaddperson.getText().toString());
                            } catch (NullException e) {
                                nullname=true;
                                e.printStackTrace();
                            } catch (DuplicationNameException e) {
                                duplicatename=true;
                                e.printStackTrace();
                            } catch (MemberReturnException e) {}
                        if(nullname)
                            Toast.makeText(getApplicationContext(),"参与人员中存在空的人名，已自动忽略",Toast.LENGTH_SHORT).show();
                        if(duplicatename)
                            Toast.makeText(getApplicationContext(), "参与人员出现重名，重名的已自动忽略", Toast.LENGTH_SHORT).show();
                        //TODO
                        Intent intent = new Intent(AddBook.this, Account.class);
                        intent.putExtra("name", bookname);
                        startActivity(intent);
                        AddBook.this.finish();
                    } catch (NullException e) {
                        Toast.makeText(getApplicationContext(),"账本名不能为空，请输入账本名",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (DuplicationNameException e) {
                        Toast.makeText(getApplicationContext(),"与现有账本重名，请重新命名",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
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
