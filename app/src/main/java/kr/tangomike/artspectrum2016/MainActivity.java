package kr.tangomike.artspectrum2016;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private RelativeLayout rlMain;
    private DrawView dv;
    private int nowNote;

    private enum btn{BTN_RIGHT, BTN_LEFT,}

    private ArrayList<ImageView> circleArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        rlMain = (RelativeLayout)findViewById(R.id.rl_main);
        rlMain.setBackgroundResource(R.drawable.bg01);

        nowNote = 1;

        dv = new DrawView(this, nowNote);
        dv.setLayoutParams(new LayoutParams(600, 600));
        dv.setX(90);
        dv.setY(332);



//        dv.setNote(nowNote);
        rlMain.addView(dv);


        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn tag = (btn)v.getTag();
                if(tag == btn.BTN_RIGHT){

                    nowNote++;
                    if(nowNote > 3) nowNote = 1;

                }else{

                    nowNote--;
                    if(nowNote < 1) nowNote = 3;

                }


                dv.setNote(nowNote);


                for(int i = 0; i < circleArray.size(); i++){

                    ImageView iv = circleArray.get(i);
                    if(i + 1 == nowNote){
                        iv.setImageResource(R.drawable.sel_down);
                    }else{
                        iv.setImageResource(R.drawable.sel_up);
                    }


                }

            }
        };



        Button btnRight = new Button(this);
        btnRight.setBackgroundResource(R.drawable.btn_right);
        btnRight.setX(524);
        btnRight.setY(967);
        btnRight.setTag(btn.BTN_RIGHT);
        btnRight.setOnClickListener(ocl);
        rlMain.addView(btnRight);


        Button btnLeft = new Button(this);
        btnLeft.setBackgroundResource(R.drawable.btn_left);
        btnLeft.setX(210);
        btnLeft.setY(967);
        btnLeft.setTag(btn.BTN_LEFT);
        btnLeft.setOnClickListener(ocl);
        rlMain.addView(btnLeft);



        circleArray = new ArrayList();

        for(int i = 0; i < 3; i++){
            ImageView iv = new ImageView(this);
            if(i == 0){
                iv.setImageResource(R.drawable.sel_down);
            }else{
                iv.setImageResource(R.drawable.sel_up);
            }

            iv.setX(306 + (i * 64));
            iv.setY(967);
            rlMain.addView(iv);
            circleArray.add(iv);
        }



        Button btnSend = new Button(this);

        btnSend.setBackgroundResource(R.drawable.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendImage(cropImage());

            }
        });

        btnSend.setX(306);
        btnSend.setY(1056);

        rlMain.addView(btnSend);


    }

    private void sendImage(Bitmap bitmap){


    }


    private Bitmap cropImage(){
        //TODO: Crop Image from 90, 332 by 600, 600

        Bitmap retVal = null;


        return retVal;
    }


    @Override
    protected void onResume(){
        super.onResume();



    }

}
