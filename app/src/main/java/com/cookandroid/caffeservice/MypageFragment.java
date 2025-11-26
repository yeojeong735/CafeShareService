package com.cookandroid.caffeservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo; // 버전 정보 획득을 위해 추가
import android.content.pm.PackageManager; // 버전 정보 획득을 위해 추가
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog; // AlertDialog를 사용하기 위해 추가
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;

// Note: 이 Fragment는 MainActivity에 포함되어 있다고 가정합니다.
public class MypageFragment extends Fragment {

    // SharedPreferences 키 (LoginActivity와 동일하게 유지)
    private static final String PREF_NAME = "MyAppPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "currentUserId";

    private LinearLayout logoutButtonContainer;
    private TextView nicknameTextView;
    private ImageView settingsIcon;
    private RecyclerView rvTimelineFeed;
    private TimelineAdapter timelineAdapter;
    private ArrayList<TimelineData> timelineList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_mypage.xml 레이아웃을 사용
        View rootView = inflater.inflate(R.layout.activity_mypage, container, false);

        // 1. 닉네임 TextView 연결 및 업데이트
        nicknameTextView = rootView.findViewById(R.id.nickname_text);
        updateNicknameDisplay();

        // 2. 설정 아이콘 연결 및 팝업 표시 리스너 설정
        settingsIcon = rootView.findViewById(R.id.settings_icon);
        if (settingsIcon != null) {
            settingsIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSettingsDialog(); // 설정 버튼 클릭 시 다이얼로그 호출
                }
            });
        }

        // 3. 로그아웃 버튼 컨테이너 연결
        logoutButtonContainer = rootView.findViewById(R.id.logout_button_container);
        if (logoutButtonContainer != null) {
            logoutButtonContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performLogout();
                }
            });
        }

        return rootView;
    }

    /**
     * SharedPreferences에서 사용자 ID를 불러와 TextView에 표시하는 함수입니다.
     */
    private void updateNicknameDisplay() {
        Context context = getContext();
        if (context != null && nicknameTextView != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            String userId = sharedPreferences.getString(KEY_USER_ID, "로그인 사용자");
            // 닉네임 형식에 맞춰 TextView 업데이트
            nicknameTextView.setText(userId + " 님");
        }
    }

    /**
     * 설정 버튼 클릭 시 나타나는 팝업 다이얼로그를 생성하고 표시합니다.
     */
    private void showSettingsDialog() {
        if (getContext() == null) return;

        // 팝업에 표시될 항목 리스트
        final String[] items = {"내 정보 수정", "탈퇴하기", "버전 정보"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("설정 메뉴");

        // 리스트 항목 클릭 이벤트 처리
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0: // 내 정보 수정
                    Toast.makeText(getContext(), "내 정보 수정 페이지로 이동 예정", Toast.LENGTH_SHORT).show();
                    // TODO: ProfileEditActivity.class 등으로 Intent 이동 구현
                    break;
                case 1: // 탈퇴하기
                    showWithdrawalConfirmationDialog(); // 탈퇴 확인 다이얼로그 호출
                    break;
                case 2: // 버전 정보
                    showAppVersionDialog(); // 버전 정보 다이얼로그 호출
                    break;
            }
        });

        builder.show();
    }

    /**
     * 앱 버전 정보를 보여주는 다이얼로그입니다.
     */
    private void showAppVersionDialog() {
        if (getContext() == null) return;

        String appVersion;
        try {
            // AndroidManifest.xml에서 현재 앱 버전을 가져옴
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            appVersion = "버전 정보를 찾을 수 없습니다.";
        }

        new AlertDialog.Builder(getContext())
                .setTitle("버전 정보")
                .setMessage("현재 버전: v" + appVersion + "\n\n(c) 2025 CafeService Team")
                .setPositiveButton("확인", null)
                .show();
    }

    /**
     * 탈퇴 확인을 위한 2차 컨펌 다이얼로그입니다.
     */
    private void showWithdrawalConfirmationDialog() {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("경고: 정말 탈퇴하시겠습니까?")
                .setMessage("탈퇴 시 모든 사용자 정보 및 리뷰 데이터가 영구적으로 삭제됩니다. 계속하시겠습니까?")
                .setPositiveButton("예 (탈퇴)", (dialog, which) -> {
                    // TODO: 여기에 실제 탈퇴 API 호출 및 로직을 구현해야 합니다.
                    Toast.makeText(getContext(), "탈퇴 처리 진행 중...", Toast.LENGTH_LONG).show();
                    // 예: performWithdrawalAPI();
                })
                .setNegativeButton("아니오", null)
                .show();
    }


    /**
     * 로그아웃 처리를 수행하는 함수입니다.
     */
    private void performLogout() {
        Context context = getContext();
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            // 로그인 상태 및 사용자 ID 모두 삭제
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.remove(KEY_USER_ID);
            editor.apply();

            Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

            // 로그인 화면으로 이동
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        }
    }


    private void initTimelineRecyclerView() {
        ArrayList<TimelineData> timelineList = new ArrayList<>();

        // 사용자 활동 기록 (클릭한 카페 목록처럼) 더미 데이터
        // 데이터 형식: (카페 이름, 주소, 기록 날짜)
        timelineList.add(new TimelineData("카페 피드", "서울시 마포구 동교동", "2025-11-25"));
        timelineList.add(new TimelineData("더존매터", "서울시 마포구 성미산로", "2025-11-23"));
        timelineList.add(new TimelineData("카페드레브", "서울시 강남구 테헤란로", "2025-11-20"));
        timelineList.add(new TimelineData("쿠이케", "서울시 종로구 삼청동", "2025-11-18"));

        TimelineAdapter timelineAdapter = new TimelineAdapter(timelineList);
        rvTimelineFeed.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimelineFeed.setAdapter(timelineAdapter);
    }

}