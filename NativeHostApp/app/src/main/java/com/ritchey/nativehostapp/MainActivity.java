package com.ritchey.nativehostapp; // 替换为你的包名

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnShowUnityReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowUnityReward = findViewById(R.id.btnShowUnityReward);
        btnShowUnityReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnityReward();
            }
        });
    }

    private void showUnityReward() {
        Intent intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
        String rewardJson = "{\"score\":75000,\"coins\":250,\"unlockItem\":\"new_chick_hat\"}";
        intent.putExtra("REWARD_DETAILS_JSON", rewardJson);
        startActivity(intent);
    }
}