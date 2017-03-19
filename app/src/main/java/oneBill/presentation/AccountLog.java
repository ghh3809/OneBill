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
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Detail;
import oneBill.domain.entity.Person;
import oneBill.presentation.activity.AtyAddRecord;

public class AccountLog extends AppCompatActivity {
    Actioner actioner;
    TextView tvNameInLog;
    TextView tvAccountLog;
    ImageView ivToAccountFromLog,ivAddLogFromLog,ivToMemberFromLog,ivToClearFromLog,ivDeleteAccountFromLog;
    LinearLayout linearMember,linearPaid,linearPayable;
    String name;
    String detail;
    int id;
    int numAccountLog;
    ArrayList<Detail> arraylist;
    Vector<TextView> vTV = new Vector<TextView>();


    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_account_log);

        actioner = new Actioner(this);
        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        id = bundle.getInt("id");
        detail = bundle.getString("detail");

        linearMember = (LinearLayout) findViewById(R.id.linearMember);
        linearPaid = (LinearLayout) findViewById(R.id.linearPaid);
        linearPayable = (LinearLayout) findViewById(R.id.linearPayable);

        linearMember.setPadding(10,10,10,10);
        linearPaid.setPadding(10,10,10,10);
        linearPayable.setPadding(10,10,10,10);

        tvNameInLog = (TextView) findViewById(R.id.tvNameInLog);
        tvNameInLog.setText(name);

        tvAccountLog = (TextView) findViewById(R.id.tvAccountLog);
        tvAccountLog.setText(detail);

        arraylist = actioner.GetDetail(id);
        numAccountLog = arraylist.size();

        for(int i = 0; i < numAccountLog; ++ i){
            vTV.add(3 * i, new TextView(AccountLog.this));
            vTV.add(3 * i + 1, new TextView(AccountLog.this));
            vTV.add(3 * i + 2, new TextView(AccountLog.this));

            DecimalFormat df = new DecimalFormat("#0.00");

            Detail arraylist1 = arraylist.get(i);
            StringBuilder sb = new StringBuilder();
            vTV.get(3 * i).setText(arraylist1.getPerson());
            vTV.get(3 * i + 1).setText(df.format(arraylist1.getPaid()));
            vTV.get(3 * i + 2).setText(df.format(arraylist1.getPayable()));

            linearMember.addView(vTV.get(3 * i));
            linearPaid.addView(vTV.get(3 * i + 1));
            linearPayable.addView(vTV.get(3 * i + 2));
        }

        ivToAccountFromLog = (ImageView) findViewById(R.id.ivToAccountFromLog);
        ivToAccountFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this, Account.class);
                intent1.putExtra("name",name);
                startActivity(intent1);
                AccountLog.this.finish();
            }
        });

        ivAddLogFromLog = (ImageView) findViewById(R.id.ivAddLogFromLog);
        ivAddLogFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this, AtyAddRecord.class);
                intent1.putExtra("bookName", intent.getStringExtra("name"));
                startActivity(intent1);
                AccountLog.this.finish();
            }
        });

        ivToMemberFromLog = (ImageView) findViewById(R.id.ivToMemberFromLog);
        ivToMemberFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this, ManageMember.class);
                intent1.putExtra("bookName",intent.getStringExtra("name"));
                startActivity(intent1);
                AccountLog.this.finish();
            }
        });

        ivToClearFromLog = (ImageView) findViewById(R.id.ivToClearFromLog);
        ivToClearFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AccountLog.this,AccountClear.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
                AccountLog.this.finish();
            }
        });

        ivDeleteAccountFromLog = (ImageView) findViewById(R.id.ivDeleteAccountFromLog);
        ivDeleteAccountFromLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先判断记录中所有成员是不是都存在，如果不是不可删除（deletable = false）
                boolean deletable = true;
                ArrayList<Person> namelist = actioner.GetMembers(name);
                ArrayList<Detail> loglist = actioner.GetDetail(id);
                for(int i = 0; i < loglist.size(); ++ i){
                    String logperson = loglist.get(i).getPerson();
                    boolean exist = false;

                    for(int j = 0; j < namelist.size(); ++ j) if(logperson.equals(namelist.get(j).getName()))
                        exist = true;

                    if(!exist){
                        deletable = false;
                        break;
                    }
                }

                if(!deletable){
                    Toast.makeText(getApplicationContext(),"记录中有成员离开，无法删除",Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(AccountLog.this)
                            .setTitle("Alert!")
                            .setMessage("确认删除此记录?")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    actioner.DeleteRecord(id);
                                    Intent intent1 = new Intent(AccountLog.this, Account.class);
                                    intent1.putExtra("name", name);
                                    startActivity(intent1);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        actioner.CloseDataBase();
        super.onDestroy();
    }
}
