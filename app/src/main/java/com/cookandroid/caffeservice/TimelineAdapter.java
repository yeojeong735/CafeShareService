package com.cookandroid.caffeservice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private ArrayList<TimelineData> mList; // ⭐ 변수 이름 mList로 통일 (오류 수정) ⭐

    // 생성자
    public TimelineAdapter(ArrayList<TimelineData> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_timeline_feed.xml 연결
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timeline_feed, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimelineData item = mList.get(position); // ⭐ mList 사용 ⭐

        // 1. 카페 이름 바인딩 (tvTimelineCafeName)
        holder.tvCafeName.setText(item.getCafeName());

        // 2. 카페 주소 바인딩 (tvTimelineAddress)
        holder.tvCafeAddress.setText(item.getCafeAddress());

        // TODO: 클릭 리스너 추가 로직 (예: holder.itemView.setOnClickListener)
    }

    @Override
    public int getItemCount() {
        return mList.size(); // ⭐ mList 사용 ⭐
    }

    // 뷰홀더 클래스: item_timeline_feed.xml의 ID와 일치해야 합니다.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 이름과 주소 TextView를 연결합니다.
        TextView tvCafeName, tvCafeAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_timeline_feed.xml에 있는 ID들과 연결
            tvCafeName = itemView.findViewById(R.id.tvTimelineCafeName);
            tvCafeAddress = itemView.findViewById(R.id.tvTimelineAddress);

            // TODO: ImageView(ivStarIndicator, ivMoreOptions) 등도 필요하면 여기에 연결합니다.
        }
    }
}
