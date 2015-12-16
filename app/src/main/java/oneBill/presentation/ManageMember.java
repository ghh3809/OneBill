package oneBill.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.NullException;

public class ManageMember extends AppCompatActivity {

    ArrayList<String> names = new ArrayList<String>();
    ArrayList<Double> Bills = new ArrayList<Double>();
    ArrayList<String> bills = new ArrayList<String>();

    String bookName;
    ListAdapter adapter = null;
    ListView listView01 = null;
    EditText editTextPersonName = null;
    Button addButton = null;
    Button addConfirmButton = null;
    Actioner actioner;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_member);
        actioner= new Actioner(this);
        //获取组件
        listView01 = (ListView) findViewById(R.id.listView01);
        editTextPersonName = (EditText) findViewById(R.id.editText01Edit);
        addButton = (Button) findViewById(R.id.addButton);
        addConfirmButton = (Button) findViewById(R.id.button01Edit);

        //获取账本名称
        Intent intent = getIntent();
        bookName = intent.getStringExtra("bookName");
        //bookName = "New Name";

        //显示账本名称
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(bookName);

        ImageButton goBack = (ImageButton) findViewById(R.id.imageButton);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageMember.this.finish();
            }
        });

        //显示ListView
        initListAllPersons();
        showByMyBaseAdapter();

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        actioner.CloseDataBase();
        ManageMember.this.finish();
    }
    public void initListAllPersons() {
        //从数据库获取names和bills

        names = actioner.GetMember(bookName);
        Bills = actioner.QueryNetAmount(bookName);

        /*
        names.add("张三");
        names.add("李四");
        names.add("王五");

        Bills.add(-50.0);
        Bills.add(20.0);
        Bills.add(30.0);
        */

        //处理bills的显示
        DecimalFormat df = new DecimalFormat("0.00");
        for (int i = 0; i < Bills.size(); i++) {
            double tempBill = Bills.get(i);
            if (tempBill >= 0)
                bills.add("应收￥" + df.format(tempBill));
            else
                bills.add("应付￥" + df.format(-tempBill));
        }

    }

    public void showByMyBaseAdapter() {
        adapter = new MyBaseAdapter(this, names, bills,Bills);
        listView01.setAdapter(adapter);
    }

    /**
     * 按钮button01Edit的onClick事件.
     *
     * @param view
     */
    public void editPersonAndRefreshListView01(View view) {
        //获取TextEdit数据
        String value = editTextPersonName.getText().toString();
        //设置addButton可见
        addButton.setVisibility(View.VISIBLE);
        //设置editText01Edit不可见(editTextPersonName)
        editTextPersonName.setText("");
        editTextPersonName.setVisibility(View.INVISIBLE);
        //设置button01Edit不可见(addConfirmButton)
        addConfirmButton.setVisibility(View.INVISIBLE);
        //更新ListView的内容
        //TODO 这句似乎没用...
        Actioner actioner = new Actioner(this);
        try{
            actioner.CreateMember(bookName, value);
        }
        catch (DuplicationNameException e){
            Toast.makeText(ManageMember.this,"添加成员重复，请重新添加",Toast.LENGTH_SHORT).show();
        }
        catch (NullException e){
            Toast.makeText(ManageMember.this,"人名不能为空，请重新添加",Toast.LENGTH_SHORT).show();
        }

        ArrayList<String> newNames = actioner.GetMember(bookName);
        if (names.size() != newNames.size()) {
            names.add(newNames.get(newNames.size() - 1));
        }

        if (names.size() == bills.size() + 1) {
            bills.add("应收￥0.00");
            Bills.add(0.0);
            //动态刷新
            ((BaseAdapter) adapter).notifyDataSetChanged();
        } else
            Toast.makeText(ManageMember.this, "添加成员错误，请重新添加", Toast.LENGTH_SHORT).show();
    }

    /**
     * 按钮addButton的onClick事件
     */
    public void addPersonAndShowEdit(View view) {
        //设置addButton不可见
        addButton.setVisibility(View.INVISIBLE);
        //设置editText01Edit可见(editTextPersonName)
        editTextPersonName.setVisibility(View.VISIBLE);
        //设置button01Edit可见(addConfirmButton)
        addConfirmButton.setVisibility(View.VISIBLE);
    }


    /**
     * 按钮itemDelete的onClick事件
     */
    public void showInfo(int pos){
        //Toast.makeText(ManageMember.this, "Delete a person"+pos, Toast.LENGTH_SHORT).show();
        String deleteName=names.get(pos);
        Double deleteBill=Bills.get(pos);
        Intent intent = new Intent(ManageMember.this, DeleteMember.class);
        intent.putExtra("bookName", bookName);
        intent.putExtra("deleteName", deleteName);
        intent.putExtra("deleteBill", deleteBill);
        startActivity(intent);
    }


    public class MyBaseAdapter extends BaseAdapter {

        private ArrayList<String> names = new ArrayList<String>();
        private ArrayList<Double> Bills = new ArrayList<Double>();
        private ArrayList<String> bills = new ArrayList<String>();

        Context context;


        public MyBaseAdapter(Context context,ArrayList<String> names,ArrayList<String> bills,ArrayList<Double> Bills){

            this.names = names;
            this.Bills=Bills;
            this.bills=bills;
            this.context = context;
        }

        @Override
        public int getCount() {
            //TODO adjust sizeCount
            return (names == null)?0:names.size();
        }

        @Override
        public Object getItem(int position) {
            //TODO adjust getItem
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class ViewHolder{
            TextView textViewItem01;
            TextView textViewItem02;
            ImageButton itemDelete;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String name = names.get(position);
            final String bill = bills.get(position);
            final double Bill = Bills.get(position);

            ViewHolder viewHolder = null;

            if(convertView==null){
                Log.d("MyBaseAdapter", "新建convertView,position=" + position);

                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_view_item, null);

                viewHolder = new ViewHolder();
                viewHolder.textViewItem01 = (TextView) convertView.findViewById(
                        R.id.listView01Item01);
                viewHolder.textViewItem02 = (TextView) convertView.findViewById(
                        R.id.listView01Item02);
                viewHolder.itemDelete = (ImageButton) convertView.findViewById(R.id.itemDelete);

                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
                Log.d("MyBaseAdapter", "旧的convertView,position=" + position);
            }

            viewHolder.textViewItem01.setText(name);
            viewHolder.textViewItem02.setText(bill);

            if (Bill >= 0)
                viewHolder.textViewItem02.setTextColor(getResources().getColor(R.color.colorGreen));
            else
                viewHolder.textViewItem02.setTextColor(getResources().getColor(R.color.colorRed));

            final int pos = position;
            viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //MainActivity mainActivity = new MainActivity();
                    showInfo(pos);
                }
            });

            return convertView;
        }

    }

}