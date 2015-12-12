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

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

public class MainActivity extends AppCompatActivity {
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
