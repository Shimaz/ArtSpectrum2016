package kr.tangomike.artspectrum2016;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by shimaz on 2016-05-02.
 */
public class SendCompleteActivity extends Activity {

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    private IntentFilter mFilter = new IntentFilter("shimaz.close");

    @Override
    protected void onCreate(Bundle sis){
        super.onCreate(sis);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        RelativeLayout rlMain = new RelativeLayout(this);
        rlMain.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(rlMain);


        rlMain.setBackgroundResource(R.drawable.bg_complete);



        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();
                overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short);

            }
        });


        registerReceiver(mReceiver, mFilter);

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
