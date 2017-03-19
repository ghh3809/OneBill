package oneBill.presentation.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 关于界面
 */
public class AtyAbout extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_about);
        button = (Button) findViewById(R.id.buttonReturn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
