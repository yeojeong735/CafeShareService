package com.cookandroid.caffeservice;

import java.io.Serializable;

public class TimelineData implements Serializable {
    private String cafeName;    // 타임라인에 표시될 카페 이름
    private String cafeAddress; // 타임라인에 표시될 카페 주소
    private String date;        // (선택적) 기록된 날짜 (예: 즐겨찾기 한 날짜)

    // 생성자: 이름과 주소, 날짜를
    // 필수로 받습니다.
    public TimelineData(String cafeName, String cafeAddress, String date) {
        this.cafeName = cafeName;
        this.cafeAddress = cafeAddress;
        this.date = date;
    }

    // Getter 메서드
    public String getCafeName() { return cafeName; }
    public String getCafeAddress() { return cafeAddress; }
    public String getDate() { return date; }
}