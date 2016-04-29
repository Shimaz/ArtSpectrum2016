package kr.tangomike.artspectrum2016;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout rlMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlMain = (RelativeLayout)findViewById(R.id.rl_main);

        DrawView dv = new DrawView(this);
        dv.setLayoutParams(new LayoutParams(300, 300));




    }
}
