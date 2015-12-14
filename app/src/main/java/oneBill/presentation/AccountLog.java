package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;

public class AccountLog extends AppCompatActivity {
    Actioner actioner;
    TextView tvNameInLog;
    TextView tvAccountLog;
    String name;
    int id;
    ArrayList<ArrayList<String>> arraylist;
    ArrayList<String> arraylist1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_log);

        actioner = new Actioner(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getString("name");
        id = bundle.getInt("id");

        tvNameInLog = (TextView) findViewById(R.id.tvNameInLog);
        tvNameInLog.setText(name);

        tvAccountLog = (TextView) findViewById(R.id.tvAccountLog);
        arraylist = actioner.GetRecord(name);
        /*
        arraylist1 = arraylist.get(id);
        StringBuilder sb = new StringBuilder();
        sb.append();
        tvAccountLog.setText(sb);

        */
    }
}
