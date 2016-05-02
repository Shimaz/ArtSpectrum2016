package kr.tangomike.artspectrum2016;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
/**
 * Created by shimaz on 2016-05-02.
 */
public class SendCompleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle sis){
        super.onCreate(sis);

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


    }
}
