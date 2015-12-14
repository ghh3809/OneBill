package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;

public class Account extends AppCompatActivity {
    Actioner actioner;
    TextView tvNameInAccount;
    TextView tvAccount;
    ImageView ivToMainFromAccount;
    ImageView ivAddLogFromAccount;
    ImageView ivToMemberFromAccount;
    ImageView ivToClearFromAccount;
    ImageView ivDeleteFromAccount;
    String name;
    ArrayList<ArrayList<String>> arraylist;
    ArrayList<String> arraylist1;
    int numAccount;
    LinearLayout linearAccount;
    Vector<TextView> vTV = new Vector<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        actioner = new Actioner(this);
        final Intent intent = getIntent();
        name = intent.getStringExtra("name");

        tvNameInAccount = (TextView) findViewById(R.id.tvNameInAccount);
        tvNameInAccount.setText(name);

        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvAccount.setText(Double.toString( actioner.GetSum(name) ));

        ivToMainFromAccount = (ImageView) findViewById(R.id.ivToMainFromAccount);
        ivToMainFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, MainActivity.class));
            }
        });

        ivAddLogFromAccount = (ImageView) findViewById(R.id.ivAddLogFromAccount);
        ivAddLogFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, AddRecordActivity.class);
                intent1.putExtra("bookName",intent.getStringExtra("name"));
                startActivity(intent1);
            }
        });

        ivToMemberFromAccount = (ImageView) findViewById(R.id.ivToMemberFromAccount);
        ivToMemberFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, ManageMember.class);
                intent1.putExtra("bookName",intent.getStringExtra("name"));
                startActivity(intent1);
            }
        });

        ivToClearFromAccount = (ImageView) findViewById(R.id.ivToClearFromAccount);
        ivToClearFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this,AccountClear.class));
            }
        });

        ivDeleteFromAccount = (ImageView) findViewById(R.id.ivDeleteFromAccount);
        ivDeleteFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        arraylist = actioner.GetRecord(name);   //获取记录
        numAccount = arraylist.size();
        linearAccount = (LinearLayout) findViewById(R.id.linearAccount);
        for(int i = 0;i < numAccount;++ i) {
            vTV.add(i, new TextView(Account.this));
            vTV.get(i).setId(i);

            arraylist1 = arraylist.get(i);  //获取第i条记录

            StringBuilder sb = new StringBuilder();
            sb.append(arraylist1.get(1));
            sb.append("    ");
            sb.append(arraylist1.get(3));
            sb.append("                          ");
            sb.append(arraylist1.get(2));
            vTV.get(i).setText(sb);

            vTV.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Account.this, AccountLog.class);
                    Bundle b = new Bundle();

                    b.putString("name", intent.getStringExtra("name"));
                    b.putInt("id", Integer.parseInt(arraylist1.get(0)));
                    i.putExtras(b);

                    startActivity(i);
                }
            });
            linearAccount.addView(vTV.get(i));
        }
    }
}
