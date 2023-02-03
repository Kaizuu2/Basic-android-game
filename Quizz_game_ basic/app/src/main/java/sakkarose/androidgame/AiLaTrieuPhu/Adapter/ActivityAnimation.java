package sakkarose.androidgame.AiLaTrieuPhu.Adapter;

import android.app.Activity;

import sakkarose.androidgame.AiLaTrieuPhu.R;

public class ActivityAnimation {
        public void fadeAnimation(Activity a)
        {
            a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        public void unzoomAnimation(Activity a)
        {
            a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
        }
}
