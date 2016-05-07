package kr.tangomike.artspectrum2016;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends Activity {

    private RelativeLayout rlMain;
    private DrawView dv;
    private int nowNote;

    private enum btn{BTN_RIGHT, BTN_LEFT,}

    private ArrayList<ImageView> circleArray;

    private static String LEEUM_IP = "192.168.0.84";

    private boolean isLaunched = false;


    private Handler mHandler;
    private static final int RESET_TIME = 60;
    private int tickTime = 0;
    private boolean isTicking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        rlMain = (RelativeLayout)findViewById(R.id.rl_main);
        rlMain.setBackgroundResource(R.drawable.bg01);

        nowNote = 0;

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
                    if(nowNote > 2) nowNote = 0;

                }else{

                    nowNote--;
                    if(nowNote < 0) nowNote = 2;

                }


                dv.setNote(nowNote);


                for(int i = 0; i < circleArray.size(); i++){

                    ImageView iv = circleArray.get(i);
                    if(i  == nowNote){
                        iv.setImageResource(R.drawable.sel_up);
                    }else{
                        iv.setImageResource(R.drawable.sel_down);
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
                iv.setImageResource(R.drawable.sel_up);
            }else{
                iv.setImageResource(R.drawable.sel_down);
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

                if(dv.getDirty()) {

                    sendImage(cropImage());
                }else{

                    Toast.makeText(getApplicationContext(), "빈 메모는 전송할 수 없습니다.", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnSend.setX(419);
        btnSend.setY(1056);

        rlMain.addView(btnSend);


        Button btnReset = new Button(this);
        btnReset.setX(191);
        btnReset.setY(1056);
        btnReset.setBackgroundResource(R.drawable.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dv.setNote(nowNote);

            }
        });

        rlMain.addView(btnReset);

        mHandler = new Handler(){

            public void handleMessage(Message msg){


                if(tickTime > RESET_TIME){

                    tickTime = 0;
                    Intent sendIntent = new Intent("shimaz.close");
                    sendBroadcast(sendIntent);
                    nowNote = 0;
                    dv.setNote(nowNote);
                    isTicking = false;

                }else{
                    tickTime++;
                    mHandler.sendEmptyMessageDelayed(0, 1000);
                    isTicking = true;


                }


            }

        };


    }

    private void sendImage(Bitmap bitmap) {

        String sdcard = Environment.getExternalStorageDirectory().toString();
        File dir = new File(sdcard + "/temp_image");
        dir.mkdirs();
        File tmpFile = new File(dir, "image_" + new SimpleDateFormat("HH-mm-ss").format(new Date()) + ".png");
        if(tmpFile.exists()) tmpFile.delete();

        try{
            FileOutputStream out = new FileOutputStream(tmpFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            sendFile(tmpFile.getAbsolutePath());
        }catch(UnknownHostException e){
            e.printStackTrace();;
        }catch(IOException e){
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }

//        requestHttp(tmpFile.getAbsolutePath());









    }



    private void sendFile(String fPath){


        Socket socket;
        try{

            android.util.Log.i("shimaz", fPath);
            socket = new Socket(LEEUM_IP, 11000 + nowNote);
            OutputStream os = socket.getOutputStream();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap bmp = BitmapFactory.decodeFile(fPath);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] buffer = stream.toByteArray();
            os.write(buffer);

            os.flush();
            os.close();

            socket.close();


            Intent intent = new Intent(MainActivity.this, SendCompleteActivity.class);

            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_short, R.anim.fade_out_short);

            isLaunched = true;

        }catch(UnknownHostException e){
            Toast.makeText(getApplicationContext(), "네트워크에 연결되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();;
        }catch(IOException e){
            Toast.makeText(getApplicationContext(), "파일 생성에 오류가 있습니다.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }



    }




    private Bitmap cropImage(){
        //TODO: Crop Image from 90, 332 by 600, 600

        Bitmap retVal;
        Bitmap tmp;
        View v1 = getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        tmp = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);
        retVal = Bitmap.createBitmap(tmp, 90, 332, 600, 600);

        return retVal;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        tickTime = 0;

        if(!isTicking) {
            mHandler.sendEmptyMessage(0);
            isTicking = true;
        }

        dv.onTouchEvent(event);

        return true;
    }


    @Override
    protected void onResume(){
        super.onResume();

        if(isLaunched) dv.setNote(nowNote);

    }

}
