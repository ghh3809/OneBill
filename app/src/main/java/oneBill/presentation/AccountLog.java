package oneBill.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AccountLog extends AppCompatActivity {
    Actioner actioner;
    TextView tvNameInLog;
    TextView tvAccountLog;
    ImageView ivToAccountFromLog,ivAddLogFromLog,ivToMemberFromLog,ivToClearFromLog,ivDeleteAccountFromLog;
    LinearLayout linearAccountLog;
    String name;
    String detail;
    int id;
    int numAccountLog;
    ArrayList<ArrayList<String>> arraylist;
    Vector<TextView> vTV = new Vector<TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log);

        actioner = new Actioner(this);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        id = bundle.getInt("id");
        detail = bundle.getString("detail");

        linearAccountLog = (LinearLayout) findViewById(R.id.linearAccountLog);
        tvNameInLog = (TextView) findViewById(R.id.tvNameInLog);
        tvNameInLog.setText(name);

        tvAccountLog = (TextView) findViewById(R.id.tvAccountLog);
        tvAccountLog.setText(detail);

        arraylist = actioner.GetDetail(id);
        numAccountLog = arraylist.size();

        for(int i = 0; i < numAccountLog; ++ i){
            vTV.add(i, new TextView(AccountLog.this));

            ArrayList<String> arraylist1 = arraylist.get(i);
            StringBuilder sb = new StringBuilder();
            sb.append(arraylist1.get(0));
            sb.append("          ");
            sb.append(arraylist1.get(1));
            sb.append("               ");
            sb.append(arraylist1.get(2));

            vTV.get(i).setText(sb);
            linearAccountLog.addView(vTV.get(i));
        }

        ivToAccountFromLog = (ImageView) findViewById(R.id.ivToAccountFromLog);
        ivToAccountFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountLog.this, Account.class));
            }
        });

        ivAddLogFromLog = (ImageView) findViewById(R.id.ivAddLogFromLog);
        ivAddLogFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this, AddRecordActivity.class);
                intent1.putExtra("bookName",intent.getStringExtra("name"));
                startActivity(intent1);
            }
        });

        ivToMemberFromLog = (ImageView) findViewById(R.id.ivToMemberFromLog);
        ivToMemberFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this, ManageMember.class);
                intent1.putExtra("bookName",intent.getStringExtra("name"));
                startActivity(intent1);
            }
        });

        ivToClearFromLog = (ImageView) findViewById(R.id.ivToClearFromLog);
        ivToClearFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this,AccountClear.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });

        ivDeleteAccountFromLog = (ImageView) findViewById(R.id.ivDeleteAccountFromLog);
        ivDeleteAccountFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AccountLog.this)
                    .setTitle("Alert!")
                    .setMessage("Are you sure to DELETE the book?")
                    .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            actioner.DeleteBook(name);
                            startActivity(new Intent(AccountLog.this,MainActivity.class));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        actioner.CloseDataBase();
    }
}
