package com.cookandroid.caffeservice; // 실제 패키지명으로 변경해주세요

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    // UI 컴포넌트 변수 선언
    private RatingBar ratingBar;
    private EditText reviewEditText;
    private CardView addPhotoCard;
    private Button registerButton;
    // TODO: 해시태그 입력 관리를 위한 컴포넌트도 추가 필요

    // 현재 리뷰를 작성할 카페 ID (백엔드 통신에 사용)
    private String cafeId = "cafe_find_101"; // 예시 ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // XML 레이아웃 파일(activity_review.xml)을 화면에 연결
        setContentView(R.layout.activity_review);

        // 1. UI 컴포넌트 연결
        ratingBar = findViewById(R.id.rating_bar);
        reviewEditText = findViewById(R.id.edit_text_review);
        addPhotoCard = findViewById(R.id.card_add_photo);
        registerButton = findViewById(R.id.button_register);

        // 2. RatingBar 스타일 정의 (선택 사항: 별 색상 변경을 위해 style.xml에 추가할 수 있습니다)
        // XML에서 theme="@style/PinkRatingBar"를 사용하여 핑크색 별을 적용할 수 있습니다.

        // 3. 사진 추가 버튼 클릭 리스너
        addPhotoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 갤러리 또는 카메라를 열어 사진을 선택하는 로직을 구현합니다.
                Toast.makeText(ReviewActivity.this, "사진 추가 기능 구현 예정", Toast.LENGTH_SHORT).show();
            }
        });

        // 4. 등록 버튼 클릭 리스너 (핵심 로직)
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    /**
     * 입력된 리뷰 데이터를 수집하고 백엔드 API를 호출하여 등록하는 메서드입니다.
     */
    private void submitReview() {
        float rating = ratingBar.getRating();
        String reviewText = reviewEditText.getText().toString().trim();

        // TODO: 해시태그 데이터 수집 (예: List<String> hashtags = getHashtags();)
        // TODO: 업로드할 사진 파일 경로 수집 (예: List<String> photoPaths = getPhotoPaths();)

        // 1. 필수 입력 항목 검사
        if (rating == 0.0f) {
            Toast.makeText(this, "별점을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (reviewText.isEmpty()) {
            Toast.makeText(this, "리뷰 내용을 작성해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. 데이터 로그 출력 (디버깅용)
        Log.d(TAG, "Review submitted for Cafe ID: " + cafeId);
        Log.d(TAG, "Rating: " + rating);
        Log.d(TAG, "Review Text: " + reviewText);
        // Log.d(TAG, "Hashtags: " + hashtags.toString()); // 해시태그 추가 후 사용

        // 3. 백엔드 통신 로직 (Retrofit 또는 Volley 등을 사용하여 서버에 데이터 전송)
        // TODO: 여기에 실제 API 호출 코드를 추가합니다.
        /*
        ReviewApi.submitReview(cafeId, rating, reviewText, new Callback<ReviewResponse>() {
            @Override
            public void onResponse(ReviewResponse response) {
                if (response.isSuccess()) {
                    Toast.makeText(ReviewActivity.this, "리뷰가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 리뷰 작성 후 현재 화면 닫기
                } else {
                    Toast.makeText(ReviewActivity.this, "등록 실패: " + response.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(ReviewActivity.this, "서버 연결 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        });
        */

        // 임시 성공 처리 (백엔드 코드가 추가되기 전까지)
        Toast.makeText(this, "리뷰 등록 시도 중...", Toast.LENGTH_SHORT).show();
        // finish();
    }
}