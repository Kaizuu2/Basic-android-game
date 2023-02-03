package sakkarose.androidgame.AiLaTrieuPhu.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sakkarose.androidgame.AiLaTrieuPhu.Model.Question;

public class Database extends SQLiteOpenHelper
{

    private Context AppContext;

    public static final String TABLE_NAME = "Question";
    public static final String TABLE_ID = "_id";
    public static final String TABLE_QUESTION = "Question";
    public static final String TABLE_CASE_A = "CaseA";
    public static final String TABLE_CASE_B = "CaseB";
    public static final String TABLE_CASE_C = "CaseC";
    public static final String TABLE_CASE_D = "CaseD";
    public static final String TABLE_TRUE_CASE = "TrueCase";

    private static final String DB_NAME = "Question.sqlite";
    public SQLiteDatabase appDatabase;
    private final String DB_PATH = "data/data/sakkarose.androidgame.AiLaTrieuPhu/databases/";



    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) throws IOException {
        super(context, name, factory, version, errorHandler);
    }

    public void addQuestion(Question q)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String nullColumnHack = null;
        ContentValues values = new ContentValues();
        if(q.getID() != -1)
        {
            values.put(TABLE_ID, q.getID());
            values.put(TABLE_QUESTION, q.getQuestion());
            values.put(TABLE_CASE_A, q.getCaseA());
            values.put(TABLE_CASE_B, q.getCaseB());
            values.put(TABLE_CASE_C, q.getCaseC());
            values.put(TABLE_CASE_D, q.getCaseD());
            values.put(TABLE_TRUE_CASE, q.getTrueCase());

            db.insert(TABLE_NAME,nullColumnHack,values);

        }
    }

    public Database(Context context) throws IOException
    {
        super(context, DB_NAME, null, 1);
        this.AppContext=context;

        boolean dbexist = checkdatabase();
        if (dbexist)
        {
            opendatabase();
        } else
        {
            System.out.println("Database doesn't exist !");
            createdatabase();
        }

    }


    public void createdatabase() throws IOException
    {
        boolean dbexist = checkdatabase();
        appDatabase = null;
        if(dbexist)
        {
            System.out.println("Database exists.");
        } else
            {
            appDatabase = this.getReadableDatabase();
            appDatabase.close();
            try
            {
                copydatabase();
            } catch(IOException e)
            {
                throw new Error("Error copying database: " + e);
            }
        }
    }

    private boolean checkdatabase()
    {
        //SQLiteDatabase checkdb = null;
        boolean checkdb = false;
        try
        {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e)
        {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException
    {
        InputStream myinput = AppContext.getAssets().open(DB_NAME);

        String outfilename = DB_PATH + DB_NAME;

        OutputStream myoutput = new FileOutputStream("/data/data/sakkarose.androidgame.AiLaTrieuPhu/databases/Question.sqlite");

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0)
        {
            myoutput.write(buffer,0,length);
        }

        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    public void opendatabase() throws SQLException
    {
        String mypath = DB_PATH + DB_NAME;
        appDatabase = SQLiteDatabase.openDatabase(mypath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public ArrayList<Question> getData()
    {
        opendatabase();
        ArrayList<Question> arrQuestions=new ArrayList<>();

        for (int i=1;i<16;i++)
        {
            String Table = TABLE_NAME + i+"";
            String Query = "SELECT * FROM "+Table+" ORDER BY random() limit 1";

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor= db.rawQuery(Query,null);

            int indexId= cursor.getColumnIndex(TABLE_ID);
            int indexQuestion= cursor.getColumnIndex(TABLE_QUESTION);
            int indexCaseA=cursor.getColumnIndex(TABLE_CASE_A);
            int indexCaseB=cursor.getColumnIndex(TABLE_CASE_B);
            int indexCaseC=cursor.getColumnIndex(TABLE_CASE_C);
            int indexCaseD=cursor.getColumnIndex(TABLE_CASE_D);
            int indexTrueCase=cursor.getColumnIndex(TABLE_TRUE_CASE);

            cursor.moveToFirst();

            int ID = cursor.getInt(indexId);
            String Question = cursor.getString(indexQuestion);
            String CaseA = cursor.getString(indexCaseA);
            String CaseB = cursor.getString(indexCaseB);
            String CaseC = cursor.getString(indexCaseC);
            String CaseD = cursor.getString(indexCaseD);
            int TrueCase = cursor.getInt(indexTrueCase);

            Question Question1 = new Question(Question,CaseA,CaseB,CaseC,CaseD,TrueCase,ID);
            arrQuestions.add(Question1);
        }
        close();
        return arrQuestions;
    }

    public synchronized void close()
    {
        if(appDatabase != null)
        {
            appDatabase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(newVersion>oldVersion)
        {
            try
            {
                copydatabase();
            } catch (IOException e)
            {
            }
        }
    }

}
