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

import java.util.ArrayList;
import java.util.Vector;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;
import oneBill.control.Actioner;
import oneBill.domain.entity.error.NullException;

public class MainActivity extends AppCompatActivity {
    ImageButton ibtnAddBook;
    Vector<RelativeLayout> rlay = new Vector<RelativeLayout>();
    Vector<Button> vbtnmain = new Vector<Button>();
    Vector<ImageButton> vibtnmain = new Vector<ImageButton>();
    Vector<RelativeLayout.LayoutParams> rlaypa = new Vector<RelativeLayout.LayoutParams>();
    Vector<TextView> tvcolor = new Vector<TextView>();
    LinearLayout llaymain;
    ScrollView mainsv;
    int booknum = 0;
    int newestbook = 0;
    int oldbook = 0;
    TextView tvamount;
    TextView tvtime;
    TextView tvblank;
    int[] randomcolor = new int[4];
    RelativeLayout.LayoutParams lp1;
    RelativeLayout.LayoutParams lp2;
    RelativeLayout.LayoutParams lp3;
    View.OnClickListener newconsumption;
    View.OnClickListener bookmain;
    private Actioner actioner;
    ArrayList<String> existedbook = new ArrayList<String>();
    int i;//bookindex
    String type;//消费类型
    ArrayList<String> latestconsum = new ArrayList<String>();
    java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
    boolean created = false;
    int maxbooknum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actioner = new Actioner(this);
        existedbook = actioner.GetBook();
        booknum = existedbook.size();
        maxbooknum = booknum;
        ibtnAddBook = (ImageButton) findViewById(R.id.imagebtnAddBook);
        llaymain = (LinearLayout) findViewById(R.id.llayoutmain);
        mainsv = (ScrollView) findViewById(R.id.mainscrollView);
        lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT
                , RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                , DensityUtil.dip2px(getApplicationContext(), 1));
        tvamount = new TextView(this);
        tvtime = new TextView(this);
        tvblank = new TextView(this);
        randomcolor[0] = getResources().getColor(R.color.darkGreen);
        randomcolor[1] = getResources().getColor(R.color.lightGreen);
        randomcolor[2] = getResources().getColor(R.color.darkBrown);
        randomcolor[3] = getResources().getColor(R.color.lightBrown);
        newconsumption = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("bookName", existedbook.get(v.getId() / 4));
                startActivity(intent);
            }
        };
        bookmain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Account.class);
                intent.putExtra("name", existedbook.get(v.getId() / 4));
                startActivity(intent);
            }
        };
        ibtnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddBook.class);
                startActivity(intent);
            }
        });

        for (i = 0; i < booknum; i++) {
            rlay.add(i, new RelativeLayout(this));
            rlay.get(i).setId(4 * i);
            tvcolor.add(i, new TextView(this));
            vbtnmain.add(i, new Button(this));
            tvcolor.get(i).setId(4 * i + 1);
            tvcolor.get(i).setText(" ");
            tvcolor.get(i).setBackgroundColor(randomcolor[i % 4]);
            vbtnmain.get(i).setId(4 * i + 2);
            vbtnmain.get(i).setPadding(0, 0, 0, 0);
            vbtnmain.get(i).setTextColor(getResources().getColor(R.color.darkGreen));
            vbtnmain.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            vbtnmain.get(i).setOnClickListener(bookmain);
            vibtnmain.add(i, new ImageButton(this));
            vibtnmain.get(i).setId(4 * i + 3);
            vibtnmain.get(i).setImageDrawable(getResources().getDrawable(R.drawable.ic_pen));
            vibtnmain.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
            vibtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            vibtnmain.get(i).setOnClickListener(newconsumption);
            vbtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            rlaypa.add(3 * i, new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 40)));
            rlaypa.add(3 * i + 1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            rlaypa.add(3 * i + 2, new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 24), DensityUtil.dip2px(getApplicationContext(), 36)));
            rlaypa.get(3 * i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3 * i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            rlaypa.get(3 * i).setMargins(0, DensityUtil.dip2px(getApplicationContext(), 4), 0, 0);
            rlaypa.get(3 * i + 1).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3 * i + 1).addRule(RelativeLayout.ALIGN_LEFT, tvcolor.get(i).getId());
            rlaypa.get(3 * i + 1).setMargins(DensityUtil.dip2px(getApplicationContext(), 10), 0, 0, 0);
            rlaypa.get(3 * i + 2).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            rlaypa.get(3 * i + 2).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rlaypa.get(3 * i + 2).setMargins(0, DensityUtil.dip2px(getApplicationContext(), 15), DensityUtil.dip2px(getApplicationContext(), 10), 0);
            rlay.get(i).addView(tvcolor.get(i), rlaypa.get(3 * i));
            rlay.get(i).addView(vbtnmain.get(i), rlaypa.get(3 * i + 1));
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
        tvtime.setPadding(DensityUtil.dip2px(getApplicationContext(), 10), 0, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        existedbook = actioner.GetBook();
        if (existedbook.size() != booknum) {
            i = existedbook.size() - 1;
            if (existedbook.size() > maxbooknum) {
                i = maxbooknum;
                maxbooknum++;
                rlay.add(i, new RelativeLayout(this));
                rlaypa.add(3 * i, new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 8), DensityUtil.dip2px(getApplicationContext(), 40)));
                rlaypa.add(3 * i + 1, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                rlaypa.add(3 * i + 2, new RelativeLayout.LayoutParams(DensityUtil.dip2px(getApplicationContext(), 24), DensityUtil.dip2px(getApplicationContext(), 24)));
                rlay.get(i).setId(4 * i);
                tvcolor.add(i, new TextView(this));
                vbtnmain.add(i, new Button(this));
                tvcolor.get(i).setId(4 * i + 1);
                tvcolor.get(i).setText(" ");
                tvcolor.get(i).setBackgroundColor(randomcolor[i % 4]);
                vbtnmain.get(i).setId(4 * i + 2);
                vbtnmain.get(i).setPadding(0, 0, 0, 0);
                vbtnmain.get(i).setTextColor(getResources().getColor(R.color.darkGreen));
                vbtnmain.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                vbtnmain.get(i).setText(existedbook.get(i));
                vbtnmain.get(i).setOnClickListener(bookmain);
                vibtnmain.add(i, new ImageButton(this));
                vibtnmain.get(i).setId(4 * i + 3);
                vibtnmain.get(i).setImageDrawable(getResources().getDrawable(R.drawable.pen_leather));
                vibtnmain.get(i).setScaleType(ImageView.ScaleType.FIT_XY);
                vibtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                vibtnmain.get(i).setOnClickListener(newconsumption);
                vbtnmain.get(i).setBackgroundColor(getResources().getColor(R.color.colorTransparent));
                rlaypa.get(3 * i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rlaypa.get(3 * i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                rlaypa.get(3 * i).setMargins(0, DensityUtil.dip2px(getApplicationContext(), 4), 0, 0);
                rlaypa.get(3 * i + 1).addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rlaypa.get(3 * i + 1).addRule(RelativeLayout.ALIGN_LEFT, tvcolor.get(i).getId());
                rlaypa.get(3 * i + 1).setMargins(DensityUtil.dip2px(getApplicationContext(), 10), 0, 0, 0);
                rlaypa.get(3 * i + 2).addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rlaypa.get(3 * i + 2).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                rlaypa.get(3 * i + 2).setMargins(0, DensityUtil.dip2px(getApplicationContext(), 15), DensityUtil.dip2px(getApplicationContext(), 10), 0);
                rlay.get(i).addView(tvcolor.get(i), rlaypa.get(3 * i));
                rlay.get(i).addView(vbtnmain.get(i), rlaypa.get(3 * i + 1));
                rlay.get(i).addView(vibtnmain.get(i), rlaypa.get(3 * i + 2));
                llaymain.addView(rlay.get(i));
            } else if (existedbook.size() < booknum) {
                llaymain.removeView(rlay.get(booknum - 1));
            } else {
                llaymain.addView(rlay.get(booknum));
            }
            booknum = existedbook.size();
        }
        if (booknum > 0) {
            for (int j = 0; j < booknum; j++) {
                vbtnmain.get(j).setText(existedbook.get(j));
            }
            try {
                latestconsum = actioner.GetLatestRecord(existedbook.get(0));
                switch (latestconsum.get(2)) {
                    case "FOOD":
                        type = "吃喝";
                        break;
                    case "TRANS":
                        type = "交通";
                        break;
                    case "PLAY":
                        type = "娱乐";
                        break;
                    case "ACCOM":
                        type = "住宿";
                        break;
                    case "OTHER":
                        type = "其他";
                        break;
                    default:
                        type = "其他";
                        break;
                }
            } catch (NullException e) {
                latestconsum.add(0, "####");
                latestconsum.add(1, "0.0");
                latestconsum.add(2, "暂无任何消费");
                type = "暂无任何消费";
            }
            if (!created) {
                lp1.addRule(RelativeLayout.BELOW, vbtnmain.get(0).getId());
                lp1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp2.addRule(RelativeLayout.BELOW, tvamount.getId());
                lp2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp3.addRule(RelativeLayout.BELOW, tvtime.getId());
                lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                lp3.setMargins(0, DensityUtil.dip2px(getApplicationContext(), 5), 0, DensityUtil.dip2px(getApplicationContext(), 20));
                rlay.get(0).addView(tvamount, lp1);
                rlay.get(0).addView(tvtime, lp2);
                rlay.get(0).addView(tvblank, lp3);
                created = true;
            }
            tvtime.setText("最近消费:   " + latestconsum.get(0) + "   ¥" +
                    df.format(Double.parseDouble(latestconsum.get(1))) +
                    "  " + type);
            tvamount.setText("¥" + String.valueOf(df.format(actioner.GetSum(existedbook.get(0)))));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        actioner.CloseDataBase();
    }
}

