package com.cookandroid.caffeservice; // 실제 패키지명으로 변경해주세요

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// 이 액티비티는 activity_login.xml 레이아웃을 사용합니다.
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XML 레이아웃 파일(activity_login.xml)을 화면에 연결
        setContentView(R.layout.activity_login);

        // 1. UI 요소 초기화 및 연결
        idEditText = findViewById(R.id.edit_text_id);
        passwordEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);
        signUpText = findViewById(R.id.text_signup);

        // 2. 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 3. 회원가입 텍스트 클릭 리스너 설정
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 회원가입 액티비티로 이동하는 인텐트를 구현합니다.
                Toast.makeText(LoginActivity.this, "회원가입 페이지로 이동", Toast.LENGTH_SHORT).show();
                // 예: startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        // TODO: 비밀번호 찾기, ID 찾기, 소셜 로그인 버튼에 대한 리스너도 추가해야 합니다.
    }

    /**
     * 사용자의 입력 값을 검증하고 백엔드 로그인 API를 호출하는 메서드입니다.
     */
    private void attemptLogin() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 1. 기본적인 입력 유효성 검사
        if (id.isEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 로그인 로직 (백엔드 통신 필요)
        Log.d(TAG, "Login Attempt with ID: " + id);

        // TODO: 여기에 실제 백엔드 API (Retrofit 또는 Volley 등) 호출 코드를 추가합니다.
        // 예: LoginService.login(id, password, new Callback() { ... });

        // 임시 성공 처리 (백엔드 코드가 추가되기 전까지)
        if (id.equals("testuser") && password.equals("1234")) {
            Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show();
            // TODO: 메인 화면으로 이동
            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            // finish();
        } else {
            // Toast.makeText(this, "아이디 또는 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
            // 임시로 성공 메시지를 표시하고 실제 백엔드 연결 후 주석 처리
            Toast.makeText(this, "로그인 시도 중...", Toast.LENGTH_SHORT).show();
        }
    }
}