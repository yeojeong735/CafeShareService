package com.cookandroid.caffeservice;

import android.content.Intent;
import android.content.SharedPreferences; // 내부 저장소 사용
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// Google 로그인 관련 Import
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

// Retrofit 및 데이터 모델 Import
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.cookandroid.caffeservice.api.RetrofitClient;
import com.cookandroid.caffeservice.LoginData.LoginRequest;
import com.cookandroid.caffeservice.LoginData.LoginResponse;
import com.cookandroid.caffeservice.LoginData.GoogleLoginRequest;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;

    private EditText idEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpText;
    private ImageButton googleLoginButton;
    private ImageButton phoneLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. UI 요소 초기화 및 연결
        idEditText = findViewById(R.id.edit_text_id);
        passwordEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);
        signUpText = findViewById(R.id.text_signup);
        googleLoginButton = findViewById(R.id.button_google_login);

        phoneLoginButton = findViewById(R.id.button_phone_login);

        // 2. GoogleSignInOptions 설정 및 클라이언트 객체 생성
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 3. Google 로그인 버튼 클릭 리스너 설정
        googleLoginButton.setOnClickListener(v -> signInWithGoogle());

        // 4. 핸드폰 버튼 클릭 시 -> 로컬 DB 초기화 (개발용)
        if (phoneLoginButton != null) {
            phoneLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetLocalDatabase(); // 초기화 함수 호출
                }
            });
        } else {
            // 혹시 버튼 ID가 달라서 null일 경우 로그 출력
            Log.e(TAG, "phoneLoginButton is null. Check XML ID.");
        }

        // 5. 일반 로그인 버튼 클릭 리스너 설정
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // 6. 회원가입 텍스트 클릭 리스너 설정
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // -----------------------------------------------------
    // ⭐️ [개발용] 로컬 데이터 초기화 메서드 ⭐️
    // -----------------------------------------------------
    private void resetLocalDatabase() {
        SharedPreferences prefs = getSharedPreferences("UserDB", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // 모든 데이터 삭제
        editor.apply();
        Toast.makeText(this, "⚠️ 로컬 회원 정보가 초기화되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // -----------------------------------------------------
    // ⭐️ 화면 전환 유틸리티 메서드 ⭐️
    // -----------------------------------------------------

    private void navigateToMain(String token) {
        // [추가] 앱 설정(MyAppPrefs)에 로그인 상태 저장
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("currentUserId", idEditText.getText().toString());
        editor.apply();

        Toast.makeText(this, "로그인 성공! 메인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // 로그인 화면을 스택에서 제거하여 뒤로가기 버튼으로 돌아오지 않게 합니다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


    // -----------------------------------------------------
    // ⭐️ Google 로그인 로직 ⭐️
    // -----------------------------------------------------

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            sendTokenToServer(idToken);

        } catch (ApiException e) {
            Log.w(TAG, "Google sign in failed: " + e.getStatusCode(), e);
            Toast.makeText(this, "Google 로그인 실패: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendTokenToServer(String token) {
        RetrofitClient.getAuthService().googleLogin(new GoogleLoginRequest(token))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.isSuccess()) {
                                String accessToken = loginResponse.getAccessToken();
                                navigateToMain(accessToken);
                            } else {
                                Toast.makeText(LoginActivity.this, "Google 로그인 실패: 서버 처리 오류", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Google 로그인 실패: 서버 처리 오류", Toast.LENGTH_LONG).show();
                            Log.e(TAG, "Server error code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "API Call Failed: " + t.getMessage(), t);
                    }
                });
    }

    // -----------------------------------------------------
    // ⭐️ 일반 로그인 로직 ⭐️
    // -----------------------------------------------------

    private void attemptLogin() {
        String id = idEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // ================================================================
        // 1. 내부 저장소(로컬 DB) 확인
        // ================================================================
        SharedPreferences prefs = getSharedPreferences("UserDB", MODE_PRIVATE);

        // 아이디가 저장소에 있는지 확인
        if (prefs.contains(id)) {
            String savedPassword = prefs.getString(id, null);

            if (savedPassword != null && savedPassword.equals(password)) {
                // 비밀번호 일치 -> 로그인 성공
                Log.d(TAG, "Local Login Success: " + id);
                navigateToMain("local_dummy_token"); // 가짜 토큰으로 이동
                return; // 성공했으니 함수 종료 (서버 통신 안 함)
            } else {
                // 아이디는 있는데 비밀번호가 틀림 -> 실패 처리 (서버 통신 안 함)
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // 로컬에 아이디가 아예 없으면 -> 서버 통신 코드로 넘어감 (혹시 서버에 있을 수 있으니)
        // ================================================================


        // 2. 로컬에 없으면 기존 서버 통신 시도
        Log.d(TAG, "Login Attempt with ID: " + id);

        LoginRequest requestBody = new LoginRequest(id, password);

        RetrofitClient.getAuthService().login(requestBody).enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse != null && loginResponse.isSuccess()) {
                        String accessToken = loginResponse.getAccessToken();
                        navigateToMain(accessToken);

                    } else {
                        String errorMessage = (loginResponse != null && loginResponse.getMessage() != null) ?
                                loginResponse.getMessage() : "로그인에 실패했습니다. (서버 응답 오류)";
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패: 서버 응답 오류 (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "네트워크 연결 실패: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("LOGIN_API", "API Call Failed: " + t.getMessage(), t);
            }
        });
    }
}