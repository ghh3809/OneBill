package oneBill.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 欢迎界面，持续3秒钟
 */
public class AtyWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_welcome);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(AtyWelcome.this, AtyMain.class);
                startActivity(intent);
                AtyWelcome.this.finish();
            }
        }, 1000);
    }
}
