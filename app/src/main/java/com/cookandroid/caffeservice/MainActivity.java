package com.cookandroid.caffeservice;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView ratingDisplayTextView;
    private CardView searchBarCard;
    private ImageButton tabMap, tabFavorite, tabSearch, tabMypage;
    private FragmentManager fragmentManager;
    private int currentSelectedTab = R.id.tab_map; // 현재 선택된 탭 ID

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ===========================================
        // 1. 기존 UI 컴포넌트 및 프래그먼트 로직
        // ===========================================
        searchBarCard = findViewById(R.id.search_bar_card);
        tabMap = findViewById(R.id.tab_map);
        tabFavorite = findViewById(R.id.tab_favorite);
        tabSearch = findViewById(R.id.tab_search);
        tabMypage = findViewById(R.id.tab_mypage);

        fragmentManager = getSupportFragmentManager();

        // 초기 화면은 Map Fragment (지도)로 설정합니다.
        if (savedInstanceState == null) {
            loadFragment(new MapFragment(), R.id.tab_map);
        }

        // 검색창 클릭 리스너
        searchBarCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 검색 화면(SearchActivity)으로 이동하는 Intent를 실행합니다.
                android.util.Log.d("MainActivity", "검색창 클릭 - 검색 화면으로 이동 예정");
            }
        });

        // 하단 네비게이션 탭 리스너 설정
        tabMap.setOnClickListener(v -> loadFragment(new MapFragment(), R.id.tab_map));
        tabFavorite.setOnClickListener(v -> loadFragment(new FavoriteFragment(), R.id.tab_favorite));
        tabSearch.setOnClickListener(v -> loadFragment(new SearchFragment(), R.id.tab_search));
        tabMypage.setOnClickListener(v -> loadFragment(new MyPageFragment(), R.id.tab_mypage));


        // ===========================================
        // 2. 별점 시스템 로직 통합
        // ===========================================

        // ID를 이용해 XML 뷰 요소들을 연결
        ratingBar = findViewById(R.id.starRatingBar);
        ratingDisplayTextView = findViewById(R.id.ratingDisplayTextView);

        // 초기 점수 텍스트 업데이트
        updateRatingText(ratingBar.getRating());

        // ⭐ RatingBar의 값이 변경될 때마다 호출되는 리스너 설정
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // 1. 점수 텍스트 뷰 업데이트
                updateRatingText(rating);

                // 2. 사용자가 직접 변경했을 때만 추가 로직 실행
                if (fromUser) {
                    handleFinalRating((int) rating);

                    // 사용자에게 피드백을 보여주는 Toast 메시지
                    Toast.makeText(MainActivity.this,
                            "선택된 별점: " + (int) rating + " 점",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    } // onCreate 함수 종료


    // ===========================================
    // 3. 기존 헬퍼 함수들 및 프래그먼트 클래스
    // ===========================================

    private void loadFragment(Fragment fragment, int newTabId) {
        if (newTabId == currentSelectedTab) {
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.social_login_container, fragment);
        transaction.commit();

        updateTabIcons(newTabId);
        currentSelectedTab = newTabId;
    }

    private void updateTabIcons(int selectedId) {
        tabMap.setImageResource(selectedId == R.id.tab_map ? R.drawable.ic_tab_map_selected : R.drawable.ic_tab_map);
        tabFavorite.setImageResource(selectedId == R.id.tab_favorite ? R.drawable.ic_tab_favorite_selected : R.drawable.ic_tab_favorite);
        tabSearch.setImageResource(selectedId == R.id.tab_search ? R.drawable.ic_tab_search_selected : R.drawable.ic_tab_search);
        tabMypage.setImageResource(selectedId == R.id.tab_mypage ? R.drawable.ic_tab_mypage_selected : R.drawable.ic_tab_mypage);
    }

    // 임시 프래그먼트 클래스
    private class MapFragment extends Fragment {
    }

    private class FavoriteFragment extends Fragment {
    }

    private class SearchFragment extends Fragment {
    }

    private class MyPageFragment extends Fragment {
    }


    // ===========================================
    // 4. 별점 시스템 헬퍼 함수들
    // ===========================================

    /**
     * Rating TextView의 텍스트를 포맷에 맞게 업데이트하는 함수입니다.
     */
    private void updateRatingText(float rating) {
        String formattedRating = String.format("%.1f 점", rating);
        ratingDisplayTextView.setText(formattedRating);
    }

    /**
     * 최종적으로 확정된 별점 점수를 처리하는 함수입니다.
     */
    private void handleFinalRating(int score) {
        // TODO: 여기에 실제 서버 전송 또는 로컬 데이터베이스 저장 로직을 작성하세요.
        System.out.println("DEBUG: 서버로 최종 별점 " + score + "점 전송 준비 완료.");
    }
}