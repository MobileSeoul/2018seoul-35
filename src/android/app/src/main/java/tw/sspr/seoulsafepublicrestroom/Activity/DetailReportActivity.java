package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import tw.sspr.seoulsafepublicrestroom.Adapter.ReportAdapter;
import tw.sspr.seoulsafepublicrestroom.Object.ReportItem;
import tw.sspr.seoulsafepublicrestroom.Object.RestroomItem;
import tw.sspr.seoulsafepublicrestroom.R;
import tw.sspr.seoulsafepublicrestroom.ResponseBody.ReportGET;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroCallback;
import tw.sspr.seoulsafepublicrestroom.Retrofit.RetroClient;

public class DetailReportActivity extends AppCompatActivity {

    private static final String LOG = "DetailReportActivity";
    private static final int GOZEBO = 1;

    private RecyclerView list;
    private Context mContext;
    private RestroomItem item;

    private ArrayList<ReportItem> reportstList;

    private RetroClient retroClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_report);
        list = findViewById(R.id.list);
        mContext = this;
        retroClient = RetroClient.getInstance(this).createBaseApi();

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        item = (RestroomItem)bundle.getSerializable("item");

        LinearLayout gozebo = findViewById(R.id.gozebo);
        //리스트 로드
        getReportsFromServer();

        //의심제보
        gozebo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(mContext,WriteReportActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item",item);
                i.putExtras(bundle);
                startActivityForResult(i,GOZEBO);
            }
        });

        //사례
        LinearLayout gosample = findViewById(R.id.gosample);

        gosample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, SampleActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GOZEBO){
            if(resultCode == RESULT_OK){
                getReportsFromServer();
            }
        }
    }

    public List<ReportGET> getReportsFromServer(){
        String restroom_id = item.getId();
        retroClient.readReports(restroom_id, new RetroCallback() {
            @Override
            public void onError(Throwable t) {
                Log.d("kokz",t.toString());
            }

            @Override
            public void onSuccess(int code, Object receivedData) {
                ArrayList<ReportGET> receive = (ArrayList<ReportGET>)receivedData;
                Log.d("kozz",receive.size()+"");
                reportstList = new ArrayList<ReportItem>();
                for(int i = 0 ; i < receive.size(); i++){
                    ReportGET receiveItem = receive.get(i);
                    Log.d("kozz",receiveItem.getWriter());
                    ReportItem reportItem =  new ReportItem(receiveItem);

                    reportstList.add(reportItem);
                }
                Log.d("kokz","성공" + reportstList.size());
                list.setLayoutManager(new LinearLayoutManager(mContext));
                list.setAdapter(new ReportAdapter(mContext,reportstList));
                list.invalidate();
            }

            @Override
            public void onFailure(int code) {
                Log.d("kokz","실패");
            }
        });
        return null;
    }
}
