package com.ritchey.nativehostapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.unity3d.player.UnityPlayer;

public class UnityPlayerActivity extends AppCompatActivity {

    private UnityPlayer mUnityPlayer;
    private FrameLayout frameLayoutForUnity;
    private String rewardDetailsJson; 

    private static final String TAG = "UnityPlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("REWARD_DETAILS_JSON")) {
            rewardDetailsJson = intent.getStringExtra("REWARD_DETAILS_JSON");
        } else {

            rewardDetailsJson = "{\"message\":\"Default Reward Trigger from Android\"}";
        }

        frameLayoutForUnity = new FrameLayout(this);
        setContentView(frameLayoutForUnity);

        mUnityPlayer = new UnityPlayer(this);
        frameLayoutForUnity.addView(mUnityPlayer.getView(),
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        mUnityPlayer.requestFocus();
        Log.d(TAG, "UnityPlayer created and view added.");
    }


    public void onUnityModuleReady() {
        Log.d(TAG, "Unity reported it is ready! Triggering reward sequence.");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mUnityPlayer != null && rewardDetailsJson != null) {
                    UnityPlayer.UnitySendMessage("RewardSystem", "TriggerRewardSequence", rewardDetailsJson);
                    Log.i(TAG, "Called TriggerRewardSequence on RewardSystem with JSON: " + rewardDetailsJson);
                } else {
                    Log.e(TAG, "UnityPlayer or rewardDetailsJson is null. Cannot send message.");
                }
            }
        });
    }


    public void onUnityRewardAnimationCompleted() {
        Log.d(TAG, "Unity reward animation completed!");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UnityPlayerActivity.this, "Unity Animation Finished!", Toast.LENGTH_LONG).show();
                // 示例：2秒后关闭此Activity
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mUnityPlayer != null) mUnityPlayer.newIntent(intent);
    }

    @Override
    protected void onDestroy() {
        if (mUnityPlayer != null) mUnityPlayer.quit();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mUnityPlayer != null) mUnityPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUnityPlayer != null) mUnityPlayer.resume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mUnityPlayer != null) mUnityPlayer.lowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            if (mUnityPlayer != null) mUnityPlayer.lowMemory();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mUnityPlayer != null) mUnityPlayer.windowFocusChanged(hasFocus);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mUnityPlayer != null) {
            if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
                return mUnityPlayer.injectEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mUnityPlayer != null) return mUnityPlayer.injectEvent(event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mUnityPlayer != null) return mUnityPlayer.injectEvent(event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mUnityPlayer != null) mUnityPlayer.configurationChanged(newConfig);
    }
}
}