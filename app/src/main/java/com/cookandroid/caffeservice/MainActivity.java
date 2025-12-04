package com.cookandroid.caffeservice;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    // 1. 변수 선언
    private ImageView btnHome, btnFavorite, btnSearch, btnMyPage;
    private int currentSelectedTab = R.id.btnHome;

    // colors.xml에서 가져올 색상 변수
    private int colorPink;
    private int colorGray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 2. 초기화
        try {
            colorPink = ContextCompat.getColor(this, R.color.icon_color);
            colorGray = ContextCompat.getColor(this, R.color.gray);
        } catch (Exception e) {
            colorPink = 0xFFDD7793;
            colorGray = 0xFFCACACA;
        }

        btnHome = findViewById(R.id.btnHome);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnSearch = findViewById(R.id.btnSearch);
        btnMyPage = findViewById(R.id.btnMyPage);

        // 3. 앱 실행 시 초기 화면 설정
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            updateTabIcons(R.id.btnHome);
        }

        // 4. 탭 버튼 리스너 설정
        View.OnClickListener tabListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedId = v.getId();

                // 탭 아이콘 색상 변경
                updateTabIcons(clickedId);

                // 즐겨찾기: 투명 배경이므로 겹치기(add)
                if (clickedId == R.id.btnFavorite) {
                    showOverlayFragment(new FavoriteFragment());
                    return;
                }

                // 검색: 투명 배경이므로 겹치기(add)
                if (clickedId == R.id.btnSearch) {
                    showOverlayFragment(new SearchFragment());
                    return;
                }

                // 나머지는 일반적인 화면 교체
                Fragment selectedFragment = null;

                if (clickedId == R.id.btnHome) {
                    selectedFragment = new HomeFragment();
                } else if (clickedId == R.id.btnMyPage) {
                    selectedFragment = new MypageFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                }
            }
        };

        btnHome.setOnClickListener(tabListener);
        btnFavorite.setOnClickListener(tabListener);
        btnSearch.setOnClickListener(tabListener);
        btnMyPage.setOnClickListener(tabListener);
    }

    /**
     * 탭 아이콘 색상 변경 메서드
     */
    private void updateTabIcons(int selectedId) {
        if(btnHome != null) btnHome.setColorFilter(selectedId == R.id.btnHome ? colorPink : colorGray);
        if(btnFavorite != null) btnFavorite.setColorFilter(selectedId == R.id.btnFavorite ? colorPink : colorGray);
        if(btnSearch != null) btnSearch.setColorFilter(selectedId == R.id.btnSearch ? colorPink : colorGray);
        if(btnMyPage != null) btnMyPage.setColorFilter(selectedId == R.id.btnMyPage ? colorPink : colorGray);

        currentSelectedTab = selectedId;
    }

    /**
     * [일반 모드] 프래그먼트 교체 (기존 화면 지움)
     * 홈, 마이페이지용
     */
    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * [오버레이 모드] 프래그먼트 겹치기 (지도 위에 띄움)
     * 즐겨찾기, 검색용
     */
    private void showOverlayFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 1. 바닥에 지도를 깝니다 (항상 HomeFragment)
        transaction.replace(R.id.fragment_container, new HomeFragment());
        // 2. 그 위에 투명한 화면을 겹칩니다 (add)
        transaction.add(R.id.fragment_container, fragment);
        transaction.commit();
    }

    /**
     * [화면 전환 기능]
     * 다른 프래그먼트에서 검색 화면으로 이동할 때 호출
     */
    public void switchToSearchFragment() {
        updateTabIcons(R.id.btnSearch); // 탭 색상 변경
        showOverlayFragment(new SearchFragment());
    }
}