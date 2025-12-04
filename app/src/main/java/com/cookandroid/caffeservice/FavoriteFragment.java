package com.cookandroid.caffeservice;

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
        // activity_favorite.xml 레이아웃 사용
        View view = inflater.inflate(R.layout.activity_favorite, container, false);

        // 1. '카페 핀드' 버튼 연결
        LinearLayout btnCafePinned = view.findViewById(R.id.btn_cafe_pinned);

        // 2. 상세 이동 기능
        if (btnCafePinned != null) {
            btnCafePinned.setOnClickListener(v -> {
                // 클릭 시 상세 페이지 이동 로직 (현재는 아무 동작 안 함)

                // [나중에 사용] 상세 페이지(Activity)가 준비되면 아래 주석을 해제하세요.
                /*
                Intent intent = new Intent(getActivity(), CafeDetailActivity.class);
                intent.putExtra("cafeName", "카페 핀드");
                startActivity(intent);
                */
            });
        }

        return view;
    }
}