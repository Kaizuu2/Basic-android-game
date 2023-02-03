package sakkarose.androidgame.AiLaTrieuPhu;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import sakkarose.androidgame.AiLaTrieuPhu.Adapter.ActivityAnimation;
import sakkarose.androidgame.AiLaTrieuPhu.Handler.Database;
import sakkarose.androidgame.AiLaTrieuPhu.Model.Question;

public class AddQuestionActivity extends AppCompatActivity
{

    ImageButton ibback;
    EditText etquestion, etanswerA, etanswerB, etanswerC, etanswerD, ettruecase;
    Button btnsubmit;

    Database db;

    private void findViewsByIds()
    {
        //EditText
        etquestion = findViewById(R.id.et_content_question);
        etanswerA = findViewById(R.id.et_answer1);
        etanswerB = findViewById(R.id.et_answer2);
        etanswerC = findViewById(R.id.et_answer3);
        etanswerD = findViewById(R.id.et_answer4);
        ettruecase = findViewById(R.id.et_truecase);

        //ImageButton
        ibback = findViewById(R.id.ibtn_back);

        //Button
        btnsubmit = findViewById(R.id.btn_submit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //set layout full man hinh vi khac nhau dpi giua cac may
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        findViewsByIds();
        setContentView(R.layout.activity_add_question);

        try{
            db = new Database(this);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //OnClick của nút back
    public void Back2MainActivity(View view)
    {
        finish();
        ActivityAnimation a = new ActivityAnimation();
        a.fadeAnimation(AddQuestionActivity.this);
    }

    //OnClick của nút Add Question
    public void addquestion(View view)
    {
        String qs, ca, cb, cc, cd, temp;
        int tc = 0, id = 1;

        qs = etquestion.getText().toString();
        ca = etanswerA.getText().toString();
        cb = etanswerB.getText().toString();
        cc = etanswerC.getText().toString();
        cd = etanswerD.getText().toString();

        temp = ettruecase.getText().toString();
        
        switch (temp)
        {
            case "A":
                tc = 1;
                break;
            case "B":
                tc = 2;
                break;
            case "C":
                tc = 3;
                break;
            case "D":
                tc = 4;
                break;
        }

        db.addQuestion(new Question(qs, ca, cb, cc, cd, tc, id));
    }


}
