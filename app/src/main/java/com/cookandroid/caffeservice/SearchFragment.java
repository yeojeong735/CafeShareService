package com.cookandroid.caffeservice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // activity_search.xml 레이아웃 사용
        View view = inflater.inflate(R.layout.activity_search, container, false);

        // ID 연결
        TextView tvSortFilter = view.findViewById(R.id.tv_sort_filter);
        LinearLayout layoutCafeItem = view.findViewById(R.id.layout_cafe_item);

        // 1. 정렬 버튼 기능
        if (tvSortFilter != null) {
            tvSortFilter.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.getMenu().add("거리순");
                popup.getMenu().add("평점순");

                popup.setOnMenuItemClickListener(item -> {
                    tvSortFilter.setText(item.getTitle() + " ▼");
                    return true;
                });
                popup.show();
            });
        }

        // 2. 상세 이동 기능
        if (layoutCafeItem != null) {
            layoutCafeItem.setOnClickListener(v -> {
                // [나중에 사용] 상세 페이지(Activity)가 준비되면 아래 주석을 해제하세요.
                /*
                Intent intent = new Intent(getActivity(), CafeDetailActivity.class); // 또는 ReviewActivity.class
                intent.putExtra("cafeName", "쿠이케");
                startActivity(intent);
                */
            });
        }

        return view;
    }
}