package tw.sspr.seoulsafepublicrestroom.Activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.nio.charset.Charset;

import tw.sspr.seoulsafepublicrestroom.Object.RestroomItem;
import tw.sspr.seoulsafepublicrestroom.R;

public class NFCTaggingActivity extends AppCompatActivity {
    private ImageView tagView;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    private RestroomItem item;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctagging);
        mContext = this;
        initView();
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try{
            ndef.addDataType("sspr/nfc");
        }catch(IntentFilter.MalformedMimeTypeException e){
            e.printStackTrace();
        }
        mFilters = new IntentFilter[] {
                ndef,
        };
        // Setup a tech list for all NfcF tags
        mTechLists = new String[][] { new String[] { Ndef.class.getName() } };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) mAdapter.enableForegroundDispatch(this, mPendingIntent, mFilters,
                mTechLists);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) mAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String id = "";
        Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages != null){
            item = setReadTagData((NdefMessage)messages[0]);
        }
        Log.d("nfc","ID : " + item.getId());
        Log.d("nfc","NAME : " + item.getName());

        Intent i = new Intent(mContext,NFCReportActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item",item);
        i.putExtras(bundle);
        startActivity(i);

    }

    public RestroomItem setReadTagData(NdefMessage ndefMsg){
        item = new RestroomItem();
        String payloadStr = null;
        int i = 0;
        if(ndefMsg == null){
            return null;
        }
        NdefRecord[] records = ndefMsg.getRecords();
        for(NdefRecord rec : records){
            byte[] payload = rec.getPayload();
            String textEncoding = "UTF-8";
            if(payload.length > 0)
                textEncoding = (payload[0] & 0200) == 0 ? "UTF-8" : "UTF-16";

            payloadStr = new String(rec.getPayload(), Charset.forName(textEncoding));
            payloadStr = payloadStr.substring(3);
            if(i == 0){
                item.setId(payloadStr);
            }else if(i == 1){
                item.setName(payloadStr);
            }
            i++;
        }

        return item;
    }


    public void initView(){
        tagView = findViewById(R.id.tagView);
        Glide
                .with(this)
                .load(R.drawable.nfc_tagging)
                .into(tagView);
    }
}
