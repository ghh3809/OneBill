package oneBill.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Vector;

import java.util.ArrayList;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.DuplicationNameException;
import oneBill.domain.entity.error.NullException;

public class MainActivity extends AppCompatActivity {

    public Actioner actioner;
    ImageButton ibtnAddBook;
    Vector<RelativeLayout> rlay=new Vector<RelativeLayout>();
    Vector<Button> vbtnmain=new Vector<Button>();
    Vector<ImageButton> vibtnmain=new Vector<ImageButton>();
    Vector<RelativeLayout.LayoutParams> rlaypa=new Vector<RelativeLayout.LayoutParams>();
    Vector<TextView>  tvcolor=new Vector<TextView>();
    LinearLayout llaymain;
    ScrollView mainsv;
    int booknum=3;
    int newestbook=1;
    int oldbook=1;
    boolean added=false;
    TextView tvamount;
    TextView tvtime;
    TextView tvblank;
    int [] randomcolor=new int [5];
    RelativeLayout.LayoutParams lp1;
    RelativeLayout.LayoutParams lp2 ;
    RelativeLayout.LayoutParams lp3;
    View.OnClickListener newconsumption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actioner=new Actioner(this);
        try {
            actioner.CreateBook("book1");
        } catch (NullException e) {
            e.printStackTrace();
        } catch (DuplicationNameException e) {
            e.printStackTrace();
        }
        try {
            actioner.CreateMember("book1","Lillard");
            actioner.CreateMember("book1","McColum");
            actioner.CreateMember("book1","Aminu");
            actioner.CreateMember("book1","Leonard");
            actioner.CreateMember("book1","Pulumle");
        } catch (DuplicationNameException e) {
            e.printStackTrace();
        }
        System.out.println(actioner.GetBook().get(0));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(MainActivity.this,AddRecordActivity.class);
                intent.putExtra("bookName","book1");
                startActivity(intent);
        ibtnAddBook= (ImageButton) findViewById(R.id.imagebtnAddBook);
        llaymain= (LinearLayout) findViewById(R.id.llayoutmain);
        mainsv= (ScrollView) findViewById(R.id.mainscrollView);
        lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                ,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                ,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                ,DensityUtil.dip2px(getApplicationContext(), 1));
        tvamount=new TextView(this);
        tvtime=new TextView(this);
        tvblank=new TextView(this);
        randomcolor[1]=getResources().getColor(R.color.darkGreen);
        randomcolor[0]=getResources().getColor(R.color.colorPrimaryDark);
        randomcolor[2]=getResources().getColor(R.color.lightGreen);
        randomcolor[3]=getResources().getColor(R.color.darkBrown);
        randomcolor[4]=getResources().getColor(R.color.lightBrown);
        newconsumption=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(MainActivity.this,);
            }
        };
        ibtnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
                if (newestbook < 2)
                    newestbook++;
                else
                    newestbook = 0;
            }
        });
        for(int i=0;i<booknum;i++) {
            rlay.add(i, new RelativeLayout(this));
            rlaypa.add(3 * i,  new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 40)));
            rlaypa.add(3 * i+1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            rlaypa.add(3 * i + 2, new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 24), DensityUtil.dip2px(getApplicationContext(), 24)));
            rlay.get(i).setId(4 * i);
            tvcolor.add(i, new TextView(this));
            vbtnmain.add(i, new Button(this));
            tvcolor.get(i).setId(4 * i + 1);
            tvcolor.get(i).setText(" ");
            tvcolor.get(i).setBackgroundColor(randomcolor[i%5]);
            vbtnmain.get(i).setId(4 * i + 2);
            vbtnmain.get(i).setPadding(0, 0, 0, 0);
            vbtnmain.get(i).setText("账本");
            vbtnmain.get(i).setTextColor(getResources().getColor(R.color.darkGreen));
            vbtnmain.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            vibtnmain.add(i, new ImageButton(this));
            vibtnmain.get(i).setId(4 * i + 3);
            vibtnmain.get(i).setImageDrawable(getResources().getDrawable(R.drawable.pen_leather));
            vibtnmain.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
            vibtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            vibtnmain.get(i).setOnClickListener(newconsumption);
            vbtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            rlaypa.get(3 * i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3*i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rlaypa.get(3*i).setMargins(0,DensityUtil.dip2px(getApplicationContext(), 4),0,0);
            rlaypa.get(3*i+1).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3*i+1).addRule(RelativeLayout.ALIGN_LEFT,tvcolor.get(i).getId());
            rlaypa.get(3*i+2).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3*i+2).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlaypa.get(3 * i + 2).setMargins(0, DensityUtil.dip2px(getApplicationContext(), 15), DensityUtil.dip2px(getApplicationContext(), 10), 0);
            rlay.get(i).addView(tvcolor.get(i), rlaypa.get(3 * i));
            rlay.get(i).addView(vbtnmain.get(i), rlaypa.get(3 * i+1));
            rlay.get(i).addView(vibtnmain.get(i), rlaypa.get(3 * i + 2));
            llaymain.addView(rlay.get(i));
        }
            tvblank.setText("");
            tvblank.setBackgroundColor(getResources().getColor(R.color.colorLine));
            tvamount.setId(newestbook + 1000);
            tvtime.setId(newestbook + 1001);
            tvamount.setTextColor(getResources().getColor(R.color.colorText));
            tvtime.setTextColor(getResources().getColor(R.color.colorText));
            tvamount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
            tvtime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvamount.setPadding(DensityUtil.dip2px(getApplicationContext(), 10), 0, 0, 0);
            tvtime.setPadding(DensityUtil.dip2px(getApplicationContext(), 10), 0, 0,0);
            tvamount.setText("$888");
            tvtime.setText("2015/12/7");
            lp1.addRule(RelativeLayout.BELOW, vbtnmain.get(newestbook).getId());
            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp2.addRule(RelativeLayout.BELOW, tvamount.getId());
            lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp3.addRule(RelativeLayout.BELOW, tvtime.getId());
            lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp3.setMargins(0,DensityUtil.dip2px(getApplicationContext(), 5),0,DensityUtil.dip2px(getApplicationContext(), 20));
            rlay.get(newestbook).addView(tvamount, lp1);
            rlay.get(newestbook).addView(tvtime,  lp2);
            rlay.get(newestbook).addView(tvblank, lp3);


        //正式测试内容（请将以下代码复制到activity里进行测试，且请只运行一次！！！否则请把数据库删了重来……）

/*        Actioner actioner = new Actioner(this);
        try{
            //创建账本My Book1
            actioner.CreateBook("My Book1");
            //添加成员
            actioner.CreateMember("My Book1", "Andy");
            actioner.CreateMember("My Book1", "Ketty");
            actioner.CreateMember("My Book1", "Jack");
            actioner.CreateMember("My Book1", "Judy");
            //删除成员
            actioner.DeleteMember("My Book1", "Andy");
            //重设账本名
            actioner.SetName("My Book1", "New Name");
            //添加消费记录1
            ArrayList<Double> paid1 = new ArrayList<Double>();
            ArrayList<Double> payable1 = new ArrayList<Double>();
            paid1.add(12.26);
            paid1.add(10.0);
            paid1.add(5.0);
            payable1.add(20.0);
            payable1.add(7.26);
            payable1.add(0.0);
            actioner.CreateConsumRecord("New Name", 1, paid1, payable1);
            //添加消费记录2
            ArrayList<Double> paid2 = new ArrayList<Double>();
            ArrayList<Double> payable2 = new ArrayList<Double>();
            paid2.add(1.0);
            paid2.add(2.0);
            paid2.add(3.0);
            payable2.add(2.0);
            payable2.add(2.0);
            payable2.add(2.0);
            actioner.CreateConsumRecord("New Name", 1, paid2, payable2);
            //添加消费记录3
            ArrayList<Double> paid3 = new ArrayList<Double>();
            ArrayList<Double> payable3 = new ArrayList<Double>();
            paid3.add(1.0);
            paid3.add(2.0);
            paid3.add(3.0);
            payable3.add(2.0);
            payable3.add(2.0);
            payable3.add(2.0);
            actioner.CreateConsumRecord("New Name", 1, paid3, payable3);
            //删除消费记录3
            actioner.DeleteRecord(3);
            //添加借款记录4
            actioner.CreateLoanRecord("New Name", "Ketty", "Jack", 18.0);
            //创建账本My Book2
            actioner.CreateBook("My Book2");
            //添加成员
            actioner.CreateMember("My Book2", "Andy");
            actioner.CreateMember("My Book2", "Ketty");
            //关闭数据库（大家一定不要忘了这一步哇不然运行会报错的！）
            actioner.CloseDataBase();
        }
        catch (Exception e){
            e.printStackTrace();
        }*/

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(newestbook!=oldbook){
            rlay.get(oldbook).removeView(tvamount);
            rlay.get(oldbook).removeView(tvtime);
            rlay.get(oldbook).removeView(tvblank);
            lp1.addRule(RelativeLayout.BELOW, vbtnmain.get(newestbook).getId());
            lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp2.addRule(RelativeLayout.BELOW, tvamount.getId());
            lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            lp3.addRule(RelativeLayout.BELOW, tvtime.getId());
            lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rlay.get(newestbook).addView(tvamount, lp1);
            rlay.get(newestbook).addView(tvtime,lp2);
            rlay.get(newestbook).addView(tvblank,lp3);
            oldbook=newestbook;
        }
    }
}
