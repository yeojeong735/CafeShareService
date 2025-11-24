package com.cookandroid.caffeservice.LoginData;

// GoogleLoginRequest.java

public class GoogleLoginRequest {
    private String token; // ID 토큰을 담는 필드

    public GoogleLoginRequest(String token) {
        this.token = token;
    }
    // Getter/Setter는 Retrofit에서 필수는 아니지만, 필요하면 추가하세요.
}