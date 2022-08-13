package tech.nilanjan.cloud.google.spring.SpannerWithSpringApplication.ui.model.response;

public class LoginRest {
    private final String accessToken;

    public LoginRest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
