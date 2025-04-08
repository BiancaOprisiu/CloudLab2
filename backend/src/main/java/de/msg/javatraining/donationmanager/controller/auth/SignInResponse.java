package de.msg.javatraining.donationmanager.controller.auth;

import java.util.List;

public class SignInResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private int loginCount;

    private boolean passwordChangeRequired;

    public int getLoginCount() {
        return loginCount;
    }

    public SignInResponse(String accessToken, Long id, String username, String email, List<String> roles, int loginCount, boolean passwordChangeRequired) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.loginCount = loginCount;
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public SignInResponse(Long id, String username, String email, List<String> roles, int loginCount, boolean passwordChangeRequired) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.loginCount = loginCount;
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }
}
