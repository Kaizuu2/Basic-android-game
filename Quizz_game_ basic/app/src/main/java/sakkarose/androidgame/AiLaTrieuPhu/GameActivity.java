package sakkarose.androidgame.AiLaTrieuPhu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

import sakkarose.androidgame.AiLaTrieuPhu.Adapter.ActivityAnimation;
import sakkarose.androidgame.AiLaTrieuPhu.Handler.Database;
import sakkarose.androidgame.AiLaTrieuPhu.Model.Question;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvtime, tvcontentquestion, tvquestion, tvanswerA, tvanswerB, tvanswerC, tvanswerD;
    MediaPlayer mp;

    private ImageView ivstop;

    private static CDRunnable CDRun;
    private static Handler CDHandler;
    boolean isRunning = false;

    Database appDatabase;

    int socauhientai = 0, time = 30, trueCase, idTC;


    ArrayList<Question> questionList;


    private void findViewsByIds()
    {
        //TextView
        tvcontentquestion = findViewById(R.id.tv_content_question);
        tvquestion = findViewById(R.id.tv_question);
        tvanswerA = findViewById(R.id.tv_answer1);
        tvanswerB = findViewById(R.id.tv_answer2);
        tvanswerC = findViewById(R.id.tv_answer3);
        tvanswerD = findViewById(R.id.tv_answer4);
        tvtime = findViewById(R.id.tv_time);

        //ImageView
        ivstop = findViewById(R.id.iv_stop);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set layout full man hinh vi khac nhau dpi giua cac may
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        findViewsByIds();

        questionList = new ArrayList<>();
        try {
            appDatabase = new Database(this);
            questionList = appDatabase.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvanswerA.setOnClickListener(this);
        tvanswerB.setOnClickListener(this);
        tvanswerC.setOnClickListener(this);
        tvanswerD.setOnClickListener(this);
        ivstop.setOnClickListener(this);

        CDHandler = new Handler();
        CDRun = new CDRunnable();

        playbgm(R.raw.ready);

        final Dialog dg = new Dialog(GameActivity.this, R.style.custom_dialog);
        dg.setTitle("Chú ý !");
        dg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
        dg.setContentView(R.layout.dialog_ready);

        TextView tv = dg.findViewById(R.id.textDialog);
        tv.setText(R.string.ban_san_sang);
        dg.show();

        final Button btnDongY = dg.findViewById(R.id.btn_dongy);
        final Button btnHuy = dg.findViewById(R.id.btn_huy);

        btnDongY.setOnClickListener(v -> {
            try {
                dg.dismiss();
                socauhientai++;
                playbgm(R.raw.start);
                Thread.sleep(2000);

                tvquestion.setText(String.valueOf(socauhientai));
                tvcontentquestion.setText(questionList.get(socauhientai - 1).Question);
                tvanswerA.setText(questionList.get(socauhientai - 1).CaseA);
                tvanswerB.setText(questionList.get(socauhientai - 1).CaseB);
                tvanswerC.setText(questionList.get(socauhientai - 1).CaseC);
                tvanswerD.setText(questionList.get(socauhientai - 1).CaseD);
                tvtime.setText(time + "");
                time();
                playbgm_loop(R.raw.first_five_questions);
                trueCase();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        btnHuy.setOnClickListener(v -> goMainAct());
    }

    @Override
    public void onClick(final View v)
    {
        if(v.getId() == R.id.tv_answer1 || v.getId() == R.id.tv_answer2 || v.getId() == R.id.tv_answer3 || v.getId() == R.id.tv_answer4)
        {
            CDHandler.removeCallbacks(CDRun);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //Đã chọn đáp án
            new Handler().postDelayed(() -> {
                if(v.getId() == trueCase)
                {
                    switch (trueCase)
                    {
                        case R.id.tv_answer1:
                            tvanswerA.setBackgroundResource(R.drawable.bg_green_corner_30);
                            playbgm(R.raw.true_answer_a);
                            break;
                        case R.id.tv_answer2:
                            tvanswerB.setBackgroundResource(R.drawable.bg_green_corner_30);
                            playbgm(R.raw.true_answer_b);
                            break;
                        case R.id.tv_answer3:
                            tvanswerC.setBackgroundResource(R.drawable.bg_green_corner_30);
                            playbgm(R.raw.true_answer_c);
                            break;
                        case R.id.tv_answer4:
                            tvanswerD.setBackgroundResource(R.drawable.bg_green_corner_30);
                            playbgm(R.raw.true_answer_d);
                            break;
                    }
                    socauhientai++;
                    if(socauhientai < 16)
                    {
                        showQuestion();
                    }

                    if(socauhientai == 16)
                    {
                        mp.stop();
                        try{
                            Thread.sleep(2000);

                            final Dialog dg = new Dialog(GameActivity.this, R.style.custom_dialog);
                            dg.setContentView(R.layout.dialog_finish);
                            dg.setTitle("!!! Chúc mừng !!!");
                            dg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

                            TextView tv = dg.findViewById(R.id.textDialog);
                            TextView tv_scd = dg.findViewById(R.id.textDialog_socaudung);
                            tv.setText(R.string.thang_cuoc);
                            tv_scd.setText(String.valueOf(16));
                            dg.setCancelable(false);

                            dg.show();

                            resetTV();

                            Button btn_ok = dg.findViewById(R.id.btn_ok_finish);
                            btn_ok.setOnClickListener(v1 -> {
                                dg.dismiss();
                                goMainAct();
                            });
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                //Chọn sai đáp án
                else {
                    mp.stop();
                    switch (trueCase)
                    {
                        case R.id.tv_answer1:
                            playbgm(R.raw.lose_answer_a);
                            wrongCaseAnim(tvanswerA);
                            break;
                        case R.id.tv_answer2:
                            playbgm(R.raw.lose_answer_b);
                            wrongCaseAnim(tvanswerB);
                            break;
                        case R.id.tv_answer3:
                            playbgm(R.raw.lose_answer_c);
                            wrongCaseAnim(tvanswerC);
                            break;
                        case R.id.tv_answer4:
                            playbgm(R.raw.lose_answer_d);
                            wrongCaseAnim(tvanswerD);
                            break;
                    }
                    try{
                        Thread.sleep(2000);
                        final Dialog dg = new Dialog(GameActivity.this, R.style.custom_dialog);
                        dg.setContentView(R.layout.dialog_finish);
                        dg.setTitle(R.string.that_tiec);
                        dg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

                        TextView tv = dg.findViewById(R.id.textDialog);
                        TextView tv_scd = dg.findViewById(R.id.textDialog_socaudung);
                        tv.setText(R.string.ban_da_thua2);
                        tv_scd.setText(String.valueOf(socauhientai - 1));
                        dg.setCancelable(false);

                        dg.show();

                        Button btn_ok = dg.findViewById(R.id.btn_ok_finish);
                        btn_ok.setOnClickListener(v12 -> {
                            dg.dismiss();
                            goMainAct();
                        });

                        //resetTV();
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }, 4000);
        }

        //Xin dừng cuộc chơi khi click ảnh stop
        if (v.getId() == R.id.iv_stop)
        {
            stop();
        }
    }

    public void resetTV()
    {
        tvquestion.setText("");
        tvanswerA.setText("");
        tvanswerB.setText("");
        tvanswerC.setText("");
        tvanswerD.setText("");
        tvanswerA.setBackgroundResource(R.drawable.bg_blue_corner_30);
        tvanswerB.setBackgroundResource(R.drawable.bg_blue_corner_30);
        tvanswerC.setBackgroundResource(R.drawable.bg_blue_corner_30);
        tvanswerD.setBackgroundResource(R.drawable.bg_blue_corner_30);
    }

    public void resetTV1()
    {
        tvquestion.setText("");
        tvanswerA.setText("");
        tvanswerB.setText("");
        tvanswerC.setText("");
        tvanswerD.setText("");
    }

    @SuppressLint("SetTextI18n")
    public void showQuestion()
    {
        resetTV();
        trueCase();
        //playbgm(BGM_Question[socauhientai - 1]);
        tvquestion.setText(String.valueOf(socauhientai));
        tvcontentquestion.setText(questionList.get(socauhientai - 1).Question);

        tvanswerA.setText(questionList.get(socauhientai - 1).CaseA);
        tvanswerB.setText(questionList.get(socauhientai - 1).CaseB);
        tvanswerC.setText(questionList.get(socauhientai - 1).CaseC);
        tvanswerD.setText(questionList.get(socauhientai - 1).CaseD);

        time = 30;
        tvtime.setText(time + "");
        time();
    }

    public void trueCase()
    {
        idTC = questionList.get(socauhientai - 1).TrueCase;
        switch (idTC)
        {
            case 1:
                trueCase = R.id.tv_answer1;
                break;
            case 2:
                trueCase = R.id.tv_answer2;
                break;
            case 3:
                trueCase = R.id.tv_answer3;
                break;
            case 4:
                trueCase = R.id.tv_answer4;
                break;
        }
    }

    private void showCD()
    {
        time = 30;
        CDHandler.removeCallbacks(CDRun);
        CDHandler.postDelayed(CDRun, 1000);
    }

    private class CDRunnable implements Runnable
    {
        @Override
        public void run()
        {
            handlerCD();
        }
    }

    @SuppressLint("SetTextI18n")
    private void handlerCD()
    {
        time--;
        if(time == 0)
        {
            isRunning = false;
            mp.stop();
            //playbgm(R.raw.out_of_time);

            switch (trueCase)
            {
                case R.id.tv_answer1:
                    playbgm(R.raw.lose_answer_a);
                    wrongCaseAnim(tvanswerA);
                    break;

                case R.id.tv_answer2:
                    playbgm(R.raw.lose_answer_b);
                    wrongCaseAnim(tvanswerB);
                    break;

                case R.id.tv_answer3:
                    playbgm(R.raw.lose_answer_c);
                    wrongCaseAnim(tvanswerC);
                    break;

                case R.id.tv_answer4:
                    playbgm(R.raw.lose_answer_d);
                    wrongCaseAnim(tvanswerD);
                    break;
            }
            try{
                Thread.sleep(4000);
                final Dialog dg = new Dialog(GameActivity.this);
                dg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dg.setContentView(R.layout.dialog_finish);
                dg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

                TextView tv = dg.findViewById(R.id.textDialog);
                TextView tv_caudung = dg.findViewById(R.id.textDialog_socaudung);
                tv.setText(R.string.ban_da_thua);
                tv_caudung.setText(String.valueOf(socauhientai - 1));
                dg.setCancelable(true);
                dg.show();

                Button btn_ok = dg.findViewById(R.id.btn_ok_finish);
                btn_ok.setOnClickListener(v -> {
                    dg.dismiss();
                    goMainAct();
                });
                resetTV1();
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            tvtime.setText(time + "");
            CDHandler.removeCallbacks(CDRun);
        }

        else{
            isRunning = true;
            tvtime.setText(time + "");
            CDHandler.postDelayed(CDRun, 1000);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        isRunning = false;
        CDHandler.removeCallbacks(CDRun);
        mp.stop();
    }

    public void time()
    {
        showCD();
    }

    public void stop() {
        mp.stop();
        try {
            Thread.sleep(2000);

            final Dialog dg = new Dialog(GameActivity.this, R.style.custom_dialog);
            dg.setContentView(R.layout.dialog_finish);
            dg.setTitle("Thông báo !");
            dg.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);

            TextView tv = dg.findViewById(R.id.textDialog);
            TextView tv_socaudung = dg.findViewById(R.id.textDialog_socaudung);

            tv.setText(R.string.dung_cuoc_choi);
            tv_socaudung.setText(String.valueOf(socauhientai - 1));
            dg.setCancelable(false);
            dg.show();

            Button btn_ok = dg.findViewById(R.id.btn_ok_finish);
            btn_ok.setOnClickListener(v -> {
                dg.dismiss();
                mp.stop();
                goMainAct();
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void playbgm(int type) {
        mp = MediaPlayer.create(this, type);
        mp.start();
    }

    public void playbgm_loop(int type) {
        mp = MediaPlayer.create(this, type);
        mp.setLooping(true);
        mp.start();
    }

    public void wrongCaseAnim(TextView tv)
    {
        tv.setBackgroundResource(R.drawable.bg_red_corner_30);
    }

    public void goMainAct()
    {
        mp.stop();
        finish();
        try {
            ActivityAnimation anim = new ActivityAnimation();
            anim.unzoomAnimation(GameActivity.this);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed()
    {
        goMainAct();
        super.onBackPressed();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    protected void onResume()
    {
        if(isRunning) {
            CDRun.run();
        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        CDHandler.removeCallbacks(CDRun);
        super.onPause();
    }

    @Override
    public void onStop()
    {
        mp.stop();
        super.onStop();
    }

}


