package sakkarose.androidgame.AiLaTrieuPhu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import sakkarose.androidgame.AiLaTrieuPhu.Adapter.ActivityAnimation;

public class MainActivity extends AppCompatActivity
{
    private ImageButton btnhuongdan;

    private Button btnbatdau, btnthemcauhoi, btnthoat;

    MediaPlayer mp;

    private void findViewsByIds()
    {
        //Button
        btnbatdau = findViewById(R.id.btn_batdau);
        btnthemcauhoi = findViewById(R.id.btn_themcauhoi);
        btnthoat = findViewById(R.id.btn_thoat);

        //ImageButton
        btnhuongdan = findViewById(R.id.btn_huongdan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //Set full màn hình máy ảo
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        findViewsByIds();
        playbgm(R.raw.main_theme);

        //Activity game
        btnbatdau.setOnClickListener(view -> {
            mp.stop();
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
            try
            {
                ActivityAnimation a = new ActivityAnimation();
                a.unzoomAnimation(MainActivity.this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Dialog luật game
        btnhuongdan.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(MainActivity.this, R.style.custom_dialog);

            dialog.setContentView(R.layout.dialog_intro);
            dialog.setTitle("Hướng dẫn");
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_box);
            TextView text = dialog.findViewById(R.id.tv_intro);
            text.setText(R.string.bat_dau);
            dialog.show();
        });

        //Activity thêm câu hỏi
        btnthemcauhoi.setOnClickListener(v -> {
            mp.stop();
            Intent intent = new Intent(MainActivity.this, AddQuestionActivity.class);
            startActivity(intent);

            try
            {
                ActivityAnimation a = new ActivityAnimation();
                a.unzoomAnimation(MainActivity.this);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Thoát game
        btnthoat.setOnClickListener(v -> {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        });

    }

    //Phát nhạc
    public void playbgm(int type)
    {
        mp = MediaPlayer.create(this,type);
        mp.setLooping(true);
        mp.start();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mp.stop();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        playbgm(R.raw.main_theme);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }
}