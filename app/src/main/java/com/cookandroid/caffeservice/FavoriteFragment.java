package com.cookandroid.caffeservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favorite, container, false);

        // 1. '카페 핀드' 버튼 연결
        LinearLayout btnCafePinned = view.findViewById(R.id.btn_cafe_pinned);

        // 2. 상세(리뷰 작성) 이동 기능
        if (btnCafePinned != null) {
            btnCafePinned.setOnClickListener(v -> {
                // 클릭 시 리뷰 작성 화면(ReviewActivity)으로 이동

                // Intent를 사용하여 화면 전환
                Intent intent = new Intent(getActivity(), ReviewActivity.class);

                // 다음 화면에 "어떤 카페"인지 알려주기 위해 데이터 전달
                intent.putExtra("CAFE_NAME", "카페 핀드");
                intent.putExtra("CAFE_ID", "cafe_find_001"); // 임시 ID

                startActivity(intent);
            });
        }

        return view;
    }
}