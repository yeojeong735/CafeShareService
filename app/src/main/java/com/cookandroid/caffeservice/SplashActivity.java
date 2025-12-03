package com.cookandroid.caffeservice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 앱 시작 시 일정 시간 동안 표시되는 스플래시 화면 액티비티
 */
public class SplashActivity extends AppCompatActivity {

    // 스플래시 화면 유지 시간 (3000ms = 3초)
    private static final int SPLASH_TIME_OUT = 3000;

    // 이동할 화면 LoginActivity에서 MainActivity로 변경
    private Class<?> nextActivity = MainActivity.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 스플래시 화면을 전체 화면으로 표시 (상태바 숨기기)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // XML 레이아웃 파일 연결
        setContentView(R.layout.activity_splash);

        // 지정된 시간이 지난 후 다음 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // SignUpActivity로 이동
                Intent intent = new Intent(SplashActivity.this, nextActivity);
                startActivity(intent);

                // 현재 스플래시 액티비티를 종료 (뒤로가기 버튼으로 돌아오지 않도록)
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}