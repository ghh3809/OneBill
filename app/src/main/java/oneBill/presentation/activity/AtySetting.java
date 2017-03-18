package oneBill.presentation.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.edu.tsinghua.cs.httpsoft.onebill.R;

/**
 * Created by 豪豪 on 2017/3/18 0018.
 */

/**
 * 设置界面
 */
public class AtySetting extends AppCompatActivity {

    SeekBar seekbarNumber;
    TextView textviewNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_setting);
        seekbarNumber = (SeekBar) findViewById(R.id.seekbarNumber);
        textviewNumber = (TextView) findViewById(R.id.textviewNumber);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("setting", MODE_PRIVATE);
        int progress = pref.getInt("MaxDetail", 1);
        seekbarNumber.setProgress(progress);
        textviewNumber.setText(String.valueOf(progress));
        seekbarNumber.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textviewNumber.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = getSharedPreferences("setting", MODE_PRIVATE).edit();
                editor.putInt("MaxDetail", seekBar.getProgress());
                editor.apply();
            }
        });
    }
}
