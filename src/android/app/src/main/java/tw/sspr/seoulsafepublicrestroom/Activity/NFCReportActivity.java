package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import tw.sspr.seoulsafepublicrestroom.RequestBody.NFCReportDTO;
import tw.sspr.seoulsafepublicrestroom.Object.RestroomItem;
import tw.sspr.seoulsafepublicrestroom.R;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroCallback;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroClient;

public class NFCReportActivity extends AppCompatActivity {

    private static final String LOG = "NFCReportActivity";

    private TextView nameView;
    private MaterialSpinner spinner;
    private EditText reportMsg;

    private Context mContext;

    private RestroomItem item;

    private String state = "안전"; //의심,위험

    RetroClient retroClient;

    @BindView(R.id.submit)
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreport);
        mContext = this;
        ButterKnife.bind(this);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        item = (RestroomItem) bundle.getSerializable("item");

        initView();

        retroClient = RetroClient.getInstance(this).createBaseApi();
    }

    @OnClick(R.id.submit)
    void submit() {
        NFCReportDTO dto = new NFCReportDTO();
        dto.setId(item.getId());
        dto.setState(state);
        dto.setMsg(reportMsg.getText().toString());

        Log.d("submit", dto.getId() + "|" + dto.getState() + "|" + dto.getMsg() + "|");

        retroClient.updateRestroom(dto, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(LOG, t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.e(LOG, "성공");
                Toast.makeText(getApplicationContext(),"제출 완료",Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code) {
                Log.e(LOG, "실패");
            }
        });

    }

    public void initView() {
        nameView = findViewById(R.id.name);
        spinner = findViewById(R.id.spinner);
        reportMsg = findViewById(R.id.report_msg);

        spinner.setItems("안전", "의심", "위험");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                state = item.toString();
            }
        });

        nameView.setText(item.getName());
    }
}
