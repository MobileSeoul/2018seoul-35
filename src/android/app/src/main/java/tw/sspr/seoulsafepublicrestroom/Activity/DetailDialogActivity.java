package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import tw.sspr.seoulsafepublicrestroom.R;

public class DetailDialogActivity extends AppCompatActivity {
    private TextView msgView;
    private String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dialog);
        initView();

        Intent i = getIntent();
        msg = i.getStringExtra("msg");

        msgView.setText(msg);

    }

    public void initView(){
        msgView = findViewById(R.id.msg);
    }
}
