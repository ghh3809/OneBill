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

import java.text.DecimalFormat;
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
    LinearLayout linearTime,linearType,linearCons;
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

        DecimalFormat df = new DecimalFormat("#.00");
        Double amt = actioner.GetSum(name);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvAccount.setText(df.format(amt));

        ivToMainFromAccount = (ImageView) findViewById(R.id.ivToMainFromAccount);
        ivToMainFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Account.this, MainActivity.class));
                Account.this.finish();
            }
        });

        ivAddLogFromAccount = (ImageView) findViewById(R.id.ivAddLogFromAccount);
        ivAddLogFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Account.this, AddRecordActivity.class);
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
                Intent intent1 = new Intent(Account.this,AccountClear.class);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });

        ivDeleteFromAccount = (ImageView) findViewById(R.id.ivDeleteFromAccount);
        ivDeleteFromAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Account.this)
                        .setTitle("Alert!")
                        .setMessage("Are you sure to DELETE the book?")
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                actioner.DeleteBook(name);
                                startActivity(new Intent(Account.this, MainActivity.class));
                                Account.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
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

        for(int i = 0;i < numAccount;++ i) {
            final int num = i;  //传给ClickListener的final参数
            arraylist1 = arraylist.get(i);  //获取第i条记录

            vTV.add(3*i, new TextView(Account.this));   //三个TextView分别记录时间、类型、消费金额
            vTV.get(3*i).setId(3*i);
            vTV.add(3*i+1, new TextView(Account.this));
            vTV.get(3*i+1).setId(3*i+1);
            vTV.add(3*i+2, new TextView(Account.this));
            vTV.get(3*i+2).setId(3*i+2);

            Double amt1 = Double.parseDouble(arraylist1.get(2));
            DecimalFormat df1 = new DecimalFormat("#.00");

            vTV.get(3*i).setText(arraylist1.get(1));
            vTV.get(3*i+1).setText(arraylist1.get(3));
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
                    ArrayList<String> arrayList = arraylist.get(num);

                    Intent i = new Intent(Account.this, AccountLog.class);
                    Bundle b = new Bundle();

                    b.putString("name", name);
                    b.putInt("id", Integer.parseInt(arrayList.get(0)));

                    Double amt = Double.parseDouble(arrayList.get(2));
                    DecimalFormat df = new DecimalFormat("#.00");

                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(arrayList.get(1));
                    sb1.append("   ￥");
                    sb1.append(df.format(amt));
                    sb1.append("    ");
                    sb1.append(arrayList.get(3));
                    b.putString("detail", String.valueOf(sb1));
                    i.putExtras(b);

                    startActivity(i);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actioner.CloseDataBase();
    }
}
