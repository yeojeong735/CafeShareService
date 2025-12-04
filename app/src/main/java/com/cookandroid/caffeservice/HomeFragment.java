package com.cookandroid.caffeservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView; // ImageView 추가

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText etSearchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // fragment_home.xml 연결
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 1. 지도 불러오기
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 2. 검색바 설정
        etSearchBar = view.findViewById(R.id.etSearchBar);

        // 3. 검색 버튼(돋보기) 설정
        ImageView btnSearchHome = view.findViewById(R.id.btnSearchHome);

        // 돋보기 버튼을 눌렀을 때만 검색 화면으로 이동
        btnSearchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1) 키보드 숨기기
                hideKeyboard();

                // 2) 검색 화면으로 전환 (MainActivity 기능 호출)
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).switchToSearchFragment();
                }
            }
        });

        return view;
    }

    /**
     * 화면이 다시 보일 때마다 호출됨 (홈으로 돌아왔을 때)
     * 검색어를 지우고 초기 상태로 되돌립니다.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (etSearchBar != null) {
            etSearchBar.setText(""); // 입력된 글자 지우기
            etSearchBar.clearFocus(); // 포커스 해제
        }
    }

    // 키보드 숨기는 헬퍼 메서드
    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 동양미래대학교 좌표
        LatLng dongyangUniv = new LatLng(37.499990, 126.867580);

        // 마커 추가
        mMap.addMarker(new MarkerOptions()
                .position(dongyangUniv)
                .title("동양미래대학교"));

        // 카메라 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dongyangUniv, 17f));

        // 내 위치 활성화 (권한 필요)
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }
}