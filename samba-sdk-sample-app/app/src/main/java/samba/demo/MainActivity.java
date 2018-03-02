package samba.demo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.samba.Samba;
import com.samba.SambaConfig;
import com.samba.SambaListener;
import com.samba.SambaSetup;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    //CHANGE THIS TO THE PUBLISHER ID YOU RECEIVE FROM SAMBA
    private static final String PUBLISHER_ID = "27";

    //CHANGE THIS TO THE SECRET KEY YOU RECEIVE FROM SAMBA
    private static final String SECRET_KEY = "TextMeSecretKey";

    private Button mBtnLoad;
    private Button mBtnShow;
    private ProgressBar mProgressBar;
    private Switch mOptimizeDownloadOnMobileNetworkSwitch;
    private Switch mDisableSoundAtVideoStartSwitch;

    private Samba mSamba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        TextView secretTextView = findViewById(R.id.secret_key);
        secretTextView.setText(getString(R.string.text_publisher_id, PUBLISHER_ID));

        TextView publisherIdTextView = findViewById(R.id.publisher_id);
        publisherIdTextView.setText(getString(R.string.text_secret_key, SECRET_KEY));

        Button initBtn = findViewById(R.id.init_btn);
        initBtn.setOnClickListener(onClickListener);

        mBtnLoad = findViewById(R.id.load_btn);
        mBtnLoad.setOnClickListener(onClickListener);

        mBtnShow = findViewById(R.id.show_btn);
        mBtnShow.setOnClickListener(onClickListener);

        mProgressBar = findViewById(R.id.progress_bar);

        mOptimizeDownloadOnMobileNetworkSwitch = findViewById(R.id.optimize_download_switch);
        mOptimizeDownloadOnMobileNetworkSwitch.setChecked(true);
        mOptimizeDownloadOnMobileNetworkSwitch.setOnCheckedChangeListener(onCheckedChangedListener);

        mDisableSoundAtVideoStartSwitch = findViewById(R.id.disable_sound_switch);
        mDisableSoundAtVideoStartSwitch.setOnCheckedChangeListener(onCheckedChangedListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.init_btn: {
                    init();
                    break;
                }
                case R.id.load_btn: {
                    load();
                    break;
                }
                case R.id.show_btn: {
                    show();
                    break;
                }
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener onCheckedChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            switch (compoundButton.getId()) {
                case R.id.optimize_download_switch:
                    SambaConfig.optimizeDownloadOnMobileNetwork(checked);
                    break;
                case R.id.disable_sound_switch:
                    //when switch is checked set video sound disabled when starting video
                    SambaConfig.setSoundDisabled(checked);
                    break;
            }
        }
    };

    private void init() {
        //Optimize mobile download on mobile when starting activity
        SambaConfig.optimizeDownloadOnMobileNetwork(true);

        //FOR TESTING purposes we generate on every init a new user Id.
        //Change this value to the end provided by Samba.
        String userId = UUID.randomUUID().toString();

        //Setup
        SambaSetup sambaSetup = new SambaSetup(PUBLISHER_ID, SECRET_KEY, userId);
        Samba.init(sambaSetup, this);
        mBtnLoad.setEnabled(true);
    }

    private void load() {
        //Get samba instance
        mSamba = Samba.getInstance(this);

        //Set a samba listener in order to register for sdk events
        mSamba.setSambaListener(sambaListener);

        //Start loading samba ads
        mSamba.loadAd();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private SambaListener sambaListener = new SambaListener() {
        @Override
        public void onAdLoadSuccess() {
            //Run the ui related code on UI thread as callback may not return on main thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBtnShow.setEnabled(true);
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onAdLoadFail(@NonNull String s) {

        }

        @Override
        public void onAdDidReachEnd(boolean wasSuccessfulView) {
            super.onAdDidReachEnd(wasSuccessfulView);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBtnShow.setEnabled(false);
                }
            });
        }
    };

    private void show() {
        //Once samba ad is loaded, show the ad
        if (mSamba.isAdLoaded()) {
            mSamba.showAd();
        }
    }

}
