package tw.sspr.seoulsafepublicrestroom.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import tw.sspr.seoulsafepublicrestroom.R;

public class SampleActivity extends AppCompatActivity {
    private SliderLayout mSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        mSlider = findViewById(R.id.slider);

        HashMap<String,Integer> file_maps = new HashMap<String,Integer>();
        file_maps.put("s1",R.drawable.sample1);
        file_maps.put("s2",R.drawable.sample2);
        file_maps.put("s3",R.drawable.sample3);
        file_maps.put("s4",R.drawable.sample4);
        file_maps.put("s5",R.drawable.sample5);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mSlider.addSlider(textSliderView);
        }
        mSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSlider.stopAutoCycle();

    }
}
