package oneBill.presentation.customLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.presentation.activity.AtyAbout;
import oneBill.presentation.activity.AtySetting;
import oneBill.presentation.adapter.DrawerDataAdapter;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 侧边栏类，并添加点击事件
 * 方便在xml中直接使用
 */
public class MyDrawerLayout extends LinearLayout implements AdapterView.OnItemClickListener {

    private Context context;

    public MyDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.layout_drawer, this);
        TextView textviewClickToLogin = (TextView) findViewById(R.id.textviewClickToLogin);
        ListView listviewDrawer = (ListView) findViewById(R.id.listviewDrawer);
        ListView listviewBottom = (ListView) findViewById(R.id.listviewBottom);
        textviewClickToLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyDrawerLayout.this.context, "即将上线，敬请关注！", Toast.LENGTH_SHORT).show();
            }
        });
        listviewDrawer.setAdapter(initItems(R.id.listviewDrawer, context, R.layout.drawer_data));
        listviewBottom.setAdapter(initItems(R.id.listviewBottom, context, R.layout.drawer_data));
        listviewDrawer.setOnItemClickListener(this);
        listviewBottom.setOnItemClickListener(this);
    }

    /**
     * 侧滑菜单的初始化
     * @param listviewId listView的ID
     * @param context 上下文
     * @param textViewResourceId 要生成的layout样式
     * @return 一个adapter
     */
    private DrawerDataAdapter initItems(int listviewId, Context context, int textViewResourceId) {
        ArrayList<DrawerData> data = new ArrayList<>();
        switch(listviewId) {
            case R.id.listviewDrawer:
                data.add(new DrawerData("设置", R.mipmap.arror_head_right));
                data.add(new DrawerData("反馈", R.mipmap.arror_head_right));
                data.add(new DrawerData("检查更新", R.mipmap.arror_head_right));
                break;
            case R.id.listviewBottom:
                data.add(new DrawerData("关于一叶账目", R.mipmap.pen_leather));
                break;
        }

        DrawerDataAdapter adapter = new DrawerDataAdapter(context, textViewResourceId, data);
        return adapter;
    }

    /**
     * 列表点击事件的处理
     * @param parent 列表容器
     * @param view 子界面
     * @param position 位置
     * @param id ID
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()) {
            case R.id.listviewDrawer:
                if (position == 0) {
                    Intent intent = new Intent((Activity)context, AtySetting.class);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(MyDrawerLayout.this.context, "即将上线，敬请关注！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.listviewBottom:
                Intent intent = new Intent((Activity)context, AtyAbout.class);
                context.startActivity(intent);
                break;
        }
    }
}
