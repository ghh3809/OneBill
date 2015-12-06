package oneBill.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

public class ManageMember extends AppCompatActivity {

    ListView listView;
    String [] names = {"张三","李四","王五"};
    String [] bills = {"应付 ￥50.00","应收 ￥20.00","应收 ￥30.00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);

        final String bookName="一叶账目示例";
        //Intent intent = getIntent();
        //final String bookName = intent.getStringExtra("bookName");
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bookName);

        listView=(ListView)this.findViewById(R.id.manageMemberList);
        listView.setAdapter(new ListViewAdapter(names, bills));

        Button addPerson = (Button) findViewById(R.id.button);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageMember.this, "Add a person", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton deletePerson = (ImageButton) findViewById(R.id.imageDelete);
        deletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageMember.this, DeleteMember.class);
                intent.putExtra("bookName",bookName);
                startActivity(intent);
            }
        });

    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;

        public ListViewAdapter(String[] itemNames, String[] itemBills) {
            itemViews = new View[itemNames.length];
            for (int i = 0; i < itemViews.length; i++) {
                itemViews[i] = makeItemView(itemNames[i], itemBills[i]);
            }
        }

        private View makeItemView(String strName, String strBill) {
            LayoutInflater inflater = (LayoutInflater) ManageMember.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.list_view_item, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView title = (TextView) itemView.findViewById(R.id.itemName);
            title.setText(strName);
            TextView text = (TextView) itemView.findViewById(R.id.itemBill);
            text.setText(strBill);
            return itemView;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                return itemViews[position];
            return convertView;
        }
        public int getCount()   {
            return itemViews.length;
        }

        public View getItem(int position)   {
            return itemViews[position];
        }

        public long getItemId(int position) {
            return position;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_member, menu);
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
