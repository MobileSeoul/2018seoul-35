package tw.sspr.seoulsafepublicrestroom.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import tw.sspr.seoulsafepublicrestroom.R;

public class NFCAuthenticateActivity extends AppCompatActivity {
    private EditText codeInput;
    private Button submit;
    private Context mContext;

    final static private String SECRET_CODE = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcauthenticate);
        mContext = this;
        initView();
    }

    public void initView(){
        codeInput = findViewById(R.id.code_input);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = codeInput.getText().toString();
                if(input.equals(SECRET_CODE)){
                    Intent i = new Intent(mContext, NFCTaggingActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"잘못된 코드",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
