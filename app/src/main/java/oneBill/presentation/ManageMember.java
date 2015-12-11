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
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import oneBill.control.Actioner;
import cn.edu.tsinghua.cs.httpsoft.onebill.R;


public class ManageMember extends AppCompatActivity {

    ListView listView;
    EditText addEdit = null;
    ListViewAdapter adapter = null;

    final ArrayList<String> names = new ArrayList<String>();
    final ArrayList<Double> Bills = new ArrayList<Double>();
    final ArrayList<String> bills = new ArrayList<String>();
    final Actioner actioner = new Actioner(this);

    String bookName;
    String deleteName;
    Double deleteBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);



        bookName="一叶账目示例";

        //Intent intent = getIntent();
        //final String bookName = intent.getStringExtra("bookName");

        //ArrayList names = actioner.GetMember(bookName);
        //ArrayList Bills = actioner.QueryNetAmount(bookName);
        //names.get(index);

        //final ArrayList<String> names = new ArrayList<String>();
        names.add("张三");
        names.add("李四");
        names.add("王五");

        //final ArrayList<Double> Bills = new ArrayList<Double>();
        Bills.add(-50.0);
        Bills.add(20.0);
        Bills.add(30.0);

        // bills = paid - payable
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < Bills.size(); i++){
            double tempBill = Bills.get(i);
            if (tempBill>=0)
                bills.add("应收￥"+df.format(tempBill));
            else
                bills.add("应付￥"+df.format(-tempBill));
        }

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bookName);

        listView=(ListView)this.findViewById(R.id.manageMemberList);

        // TODO 检测adapter
        listView.setAdapter(new ListViewAdapter(names, bills));

        Button addPerson = (Button) findViewById(R.id.button);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ManageMember.this, "Add a person", Toast.LENGTH_SHORT).show();
                Button ConfirmAddButton = (Button) findViewById(R.id.confirmAddButton);
                addEdit = (EditText) findViewById(R.id.addEdit);
                ConfirmAddButton.setVisibility(View.VISIBLE);
                addEdit.setVisibility(View.VISIBLE);
                /*
                ConfirmAddButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ManageMember.this,"Confirm add a person",Toast.LENGTH_SHORT).show();
                        //addPersonAndRefreshListView(v);
                    }
                });
                */

                //ListViewAdapter.this.notifyDataSetChanged();
            }
        });

    }

    /**
     * 按钮ConfirmAddButton的onClick事件
     */
    public void addPersonAndRefreshListView(View view){
        //获取addEdit数据
        String value = addEdit.getText().toString();
        //创建新成员
        //actioner.CreateMember(bookName,value);
        //更新listView内容
        //ArrayList names = actioner.GetMember(bookName);
        //ArrayList Bills = actioner.QueryNetAmount(bookName);
        // bills = paid - payable
        /*
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < Bills.size(); i++){
            double tempBill = Bills.get(i);
            if (tempBill>=0)
                bills.add("应收￥"+df.format(tempBill));
            else
                bills.add("应付￥"+df.format(-tempBill));
        }
        */
        names.add("value");
        bills.add("应收￥0.00");
        //动态刷新
        //((BaseAdapter) adapter).notifyDataSetChanged();
    }

    public class ListViewAdapter extends BaseAdapter {
        View[] itemViews;
        //private final static String tag = "ListViewAdapter";

        public ListViewAdapter(ArrayList<String> itemNames, ArrayList<String> itemBills){
                //String[] itemNames, String[] itemBills) {
            //建立项目个数
            itemViews = new View[itemNames.size()];
            //循环，每条项目调用一次makeItemView
            for (int i = 0; i < itemViews.length; i++) {
                itemViews[i] = makeItemView(itemNames.get(i), itemBills.get(i), i);
            }
        }

        private View makeItemView(String strName, String strBill, final int pos) {
            LayoutInflater inflater = (LayoutInflater) ManageMember.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 使用View的对象itemView与R.layout.item关联
            View itemView = inflater.inflate(R.layout.list_view_item, null);

            // 通过findViewById()方法实例R.layout.item内各组件
            TextView name = (TextView) itemView.findViewById(R.id.itemName);
            name.setText(strName);
            TextView bill = (TextView) itemView.findViewById(R.id.itemBill);
            bill.setText(strBill);
            ImageButton itemDelete = (ImageButton) itemView.findViewById(R.id.itemDelete);
            itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInfo(pos);
                }
            });

            return itemView;
        }

        public void showInfo(int pos){
            //Toast.makeText(ManageMember.this, "Delete a person"+pos, Toast.LENGTH_SHORT).show();
            deleteName=names.get(pos);
            deleteBill=Bills.get(pos);
            Intent intent = new Intent(ManageMember.this, DeleteMember.class);
            intent.putExtra("bookName",bookName);
            intent.putExtra("deleteName", deleteName);
            intent.putExtra("deleteBill", deleteBill);
            startActivity(intent);
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
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
        /*
        public void updateSingleRow(ListView listView; long id){
            if (listView != null){
                int start = listView.getFirstVisiblePosition();
                for (int i = start, j = listView.getLastVisiblePosition(); i <= j; i++)
                    if (id == ((Messages) listView.getItemAtPosition(i)).getId()) {
                        View view = listView.getChildAt(i - start);
                        getView(i, view, listView);
                        break;
                    }
            }
        }
        */
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
