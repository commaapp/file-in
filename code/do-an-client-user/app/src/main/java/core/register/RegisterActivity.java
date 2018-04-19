package core.register;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doan.R;
import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import myutil.MyLog;

/**
 * Created by D on 4/17/2018.
 */

public class RegisterActivity extends Activity {


    @BindView(R.id.simpleRatingBar)
    ScaleRatingBar simpleRatingBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);
        ButterKnife.bind(this);
        simpleRatingBar.setStepSize(0.1f);
        simpleRatingBar.setNumStars(5);
        simpleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar baseRatingBar, float v) {
                MyLog.e(getClass(), v + "");
            }
        });
//        LayerDrawable stars = (LayerDrawable) demoRate.getProgressDrawable();
//        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
    }
}
