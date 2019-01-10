package cn.icheny.cyswitch.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.icheny.view.CySwitch;
/**
 * <pre>
 *     @author : www.icheny.cn
 *     @e-mail : ausboyue@gmail.com
 *     @time   : 2019.01.08
 *     @desc   : CySwitch Sample
 *     @version: 1.0.0
 * </pre>
 */
public class MainActivity extends AppCompatActivity {

    private SeekBar pb_view_radius, pb_slider_radius, pb_border_width;
    private TextView tv_view_radius, tv_slider_radius, tv_border_width;
    private CySwitch cy_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb_view_radius = findViewById(R.id.pb_view_radius);
        pb_slider_radius = findViewById(R.id.pb_slider_radius);
        pb_border_width = findViewById(R.id.pb_border_width);
        tv_view_radius = findViewById(R.id.tv_view_radius);
        tv_slider_radius = findViewById(R.id.tv_slider_radius);
        tv_border_width = findViewById(R.id.tv_border_width);
        cy_switch = findViewById(R.id.cy_switch);

        pb_view_radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_view_radius.setText("View Radius " + progress + "dp");
                cy_switch.setViewRadius(progress);
            }
        });
        pb_slider_radius.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_slider_radius.setText("Slider Radius " + progress + "dp");
                cy_switch.setSliderRadius(progress);
            }
        });
        pb_border_width.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_border_width.setText("Border " + progress + "dp");
                cy_switch.setBorderWidth(progress);
            }
        });

        cy_switch.setOnCheckedChangeListener(new CySwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CySwitch switchView, boolean isChecked) {
                Toast.makeText(MainActivity.this, "Switch " + isChecked, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void checkableTrue(View view) {
        cy_switch.Switchable(true);
    }

    public void checkableFalse(View view) {
        cy_switch.Switchable(false);
    }
}
