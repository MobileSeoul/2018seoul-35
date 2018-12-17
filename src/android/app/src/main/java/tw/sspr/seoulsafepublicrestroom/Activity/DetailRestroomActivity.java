package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import tw.sspr.seoulsafepublicrestroom.Adapter.ReportAdapter;
import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.Object.RestroomItem;
import tw.sspr.seoulsafepublicrestroom.R;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.RestroomGET;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroCallback;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroClient;

public class DetailRestroomActivity extends AppCompatActivity {

    private static final String LOG = "DetailRestroomActivity";

    private Context mContext;

    @BindView(R.id.nameView)
    TextView nameView;

    @BindView(R.id.stateView)
    TextView stateView;

    @BindView(R.id.dateView)
    TextView dateView;

    @BindView(R.id.msgView)
    TextView msgView;

    //Report
    @BindView(R.id.report_size)
    TextView reportSizeView;

    @BindView(R.id.report_msg)
    TextView reportMsgView;

    @BindView(R.id.report_writer)
    TextView reportWriterView;

    @BindView(R.id.report_updatedate)
    TextView reportUpdatedateView;

    @BindView(R.id.report_img)
    ImageView reportImgView;

    private RestroomItem item;

    private RetroClient retroClient;

    private ReportItem reportItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restroom);
        mContext = this;
        ButterKnife.bind(this);
        retroClient = RetroClient.getInstance(this).createBaseApi();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        item = (RestroomItem) bundle.getSerializable("item");

        initView();

        LinearLayout goDetail = findViewById(R.id.goDetail);
        goDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", item);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    public void initView() {
        nameView.setText(item.getName());
        Log.d(LOG, item.getId());
        retroClient.readRestroom(item.getId(), new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d(LOG, t.toString());

            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                Log.d(LOG, "성공");

                RestroomGET res = (RestroomGET) receivedData;
                stateView.setText(res.getState());

                Date date = new Date(Long.parseLong(res.getUpdatedate().trim()));
                //Java7 YYYY format error
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                dateView.setText(sdf.format(date));

                final String msg = res.getMsg();
                msgView.setText(msg);

                msgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext,DetailDialogActivity.class);
                        i.putExtra("msg",msg);
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onFailure(int code) {
                Log.d(LOG, "실패");
            }
        });

        getReportsFromServer();


    }

    public List<ReportGET> getReportsFromServer() {
        String restroom_id = item.getId();
        retroClient.readReports(restroom_id, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("kokz", t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ArrayList<ReportGET> receive = (ArrayList<ReportGET>) receivedData;
                Log.d("kozz", receive.size() + "");
                int size = receive.size();
                if(size == 0){
                    reportSizeView.setText("0건의 몰카 의심 제보가 있어요\n\n작성하기");
                    return;
                }
                //최신
                ReportGET receiveItem = receive.get(0);

                reportItem = new ReportItem(receiveItem);
//                bindingView
                reportSizeView.setText(size + "개의 몰카 의심 제보가 있어요");
                reportMsgView.setText(reportItem.getMsg());
                reportWriterView.setText("by " + reportItem.getWriter());
                reportUpdatedateView.setText(reportItem.getUpdatedate());
                Glide
                        .with(mContext)
                        .load(reportItem.getImg())
                        .into(reportImgView);
            }

            @Override
            public void onFailure(int code) {
                Log.d("kokz", "실패");
            }
        });
        return null;
    }
}
