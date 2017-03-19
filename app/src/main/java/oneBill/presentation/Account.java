package oneBill.presentation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.Log;
import oneBill.presentation.activity.AtyAddRecord;
import oneBill.presentation.activity.AtyMain;

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
    ArrayList<Log> arraylist;
    Log arraylist1;
    int numAccount;
    LinearLayout linearTime,linearType,linearCons;
    Vector<TextView> vTV = new Vector<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        name = intent.getStringExtra("bookName");
        if (intent.getBooleanExtra("FromHome", false)) {
            Intent newIntent = new Intent(Account.this, AtyAddRecord.class);
            newIntent.putExtra("bookName", name);
            startActivity(newIntent);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
        setContentView(R.layout.activity_account);

        actioner = new Actioner(this);
        final Intent intent = getIntent();
        tvNameInAccount = (TextView) findViewById(R.id.tvNameInAccount);
        tvNameInAccount.setText(name);

        DecimalFormat df = new DecimalFormat("#0.00");
        Double amt = actioner.GetSum(name);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvAccount.setText(df.format(amt));

        ivToMainFromAccount = (ImageView) findViewById(R.id.ivToMainFromAccount);
        ivToMainFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, AtyMain.class));
            }
        });

        ivAddLogFromAccount = (ImageView) findViewById(R.id.ivAddLogFromAccount);
        ivAddLogFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, AtyAddRecord.class);
                intent1.putExtra("bookName", name);
                startActivity(intent1);
            }
        });

        ivToMemberFromAccount = (ImageView) findViewById(R.id.ivToMemberFromAccount);
        ivToMemberFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, ManageMember.class);
                intent1.putExtra("bookName",name);
                startActivity(intent1);
            }
        });

        ivToClearFromAccount = (ImageView) findViewById(R.id.ivToClearFromAccount);
        ivToClearFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, AccountClear.class);
                intent1.putExtra("bookName", name);
                startActivity(intent1);
            }
        });

        ivDeleteFromAccount = (ImageView) findViewById(R.id.ivDeleteFromAccount);
        ivDeleteFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Account.this)
                        .setTitle("警告")
                        .setMessage("确认删除此帐本？删除后，将无法恢复！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actioner.DeleteBook(name);
                                startActivity(new Intent(Account.this, AtyMain.class));
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });

        arraylist = actioner.GetRecord(name);   //获取记录
        numAccount = arraylist.size();
        linearTime = (LinearLayout) findViewById(R.id.linearTime);
        linearType = (LinearLayout) findViewById(R.id.linearType);
        linearCons = (LinearLayout) findViewById(R.id.linearCons);

        linearTime.setPadding(10,10,10,10);
        linearType.setPadding(10,10,10,10);
        linearCons.setPadding(10,10,10,10);

        for(int i = 0;i < numAccount;++ i) {
            final int num = i;  //传给ClickListener的final参数
            arraylist1 = arraylist.get(i);  //获取第i条记录
            String type = arraylist1.getType();

            vTV.add(3 * i, new TextView(Account.this));   //三个TextView分别记录时间、类型、消费金额
            vTV.get(3*i).setId(3*i);
            vTV.add(3 * i + 1, new TextView(Account.this));
            vTV.get(3*i+1).setId(3*i+1);
            vTV.add(3*i+2, new TextView(Account.this));
            vTV.get(3*i+2).setId(3*i+2);

            Double amt1 = arraylist1.getAmount();
            DecimalFormat df1 = new DecimalFormat("#0.00");

            vTV.get(3*i).setText(arraylist1.getTime());
            vTV.get(3*i+1).setText(type);
            vTV.get(3*i+2).setText(df1.format(amt1));

            linearTime.addView(vTV.get(3 * i));
            linearType.addView(vTV.get(3 * i + 1));
            linearCons.addView(vTV.get(3 * i + 2));
        }

        for(int i = 0; i < 3 * numAccount; ++ i){
            final int num = i / 3;

            vTV.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(num);
                    Log arrayList = arraylist.get(num);
                    String type = arrayList.getType();

                    Intent i = new Intent(Account.this, AccountLog.class);
                    Bundle b = new Bundle();

                    b.putString("name", name);
                    b.putInt("id", arrayList.getID());

                    Double amt = arrayList.getAmount();
                    DecimalFormat df = new DecimalFormat("#0.00");

                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(arrayList.getTime());
                    sb1.append("   ￥");
                    sb1.append(df.format(amt));
                    sb1.append("    ");
                    sb1.append(type);
                    b.putString("detail", String.valueOf(sb1));
                    i.putExtras(b);

                    startActivity(i);
                }
            });
        }
    }
    @Override
    protected void onDestroy(){
        actioner.CloseDataBase();
        super.onDestroy();
    }
}
