package sakkarose.androidgame.AiLaTrieuPhu.Model;

public class Question
{
    public String Question;
    public String CaseA;
    public String CaseB;
    public String CaseC;
    public String CaseD;
    public int TrueCase;
    public int ID;

    public Question()
    {}

    public Question(String Question, String CaseA, String CaseB, String CaseC, String CaseD, int TrueCase, int ID)
    {
        this.Question = Question;
        this.CaseA = CaseA;
        this.CaseB = CaseB;
        this.CaseC = CaseC;
        this.CaseD = CaseD;
        this.TrueCase = TrueCase;
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getQuestion() { return Question; }

    public String getCaseA() { return CaseA; }

    public String getCaseB() { return CaseB; }

    public String getCaseC() { return CaseC; }

    public String getCaseD() { return CaseD; }

    public int getTrueCase() { return TrueCase; }


}
