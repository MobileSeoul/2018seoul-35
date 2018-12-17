package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.R;
import tw.sspr.seoulsafepublicrestroom.RequestBody.DeleteReportDTO;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroCallback;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroClient;

public class DeleteReportAuthenticateActivity extends AppCompatActivity {

    private static final String LOG = "DeleteReportAuthenticat";

    private EditText codeInput;
    private Button submit;
    private Context mContext;

    private RetroClient retroClient;
    private String report_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_report_authenticate);
        mContext = this;

        Intent i = getIntent();
        report_id = i.getStringExtra("report_id");

        retroClient = RetroClient.getInstance(this).createBaseApi();

        initView();


    }

    public void initView(){
        codeInput = findViewById(R.id.code_input);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pwd = codeInput.getText().toString();
                DeleteReportDTO item = new DeleteReportDTO();
                item.setReport_id(report_id);
                item.setPwd(pwd);
                retroClient.deleteReport(item, new RetroCallback() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d(LOG,t.toString());
                    }

                    @Override
                    public void onSuccess(int code, Object receivedData) {
                        Log.d(LOG,"삭제성공");
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(int code) {
                        Log.d(LOG,"실패");
                    }
                });
            }
        });
    }
}
