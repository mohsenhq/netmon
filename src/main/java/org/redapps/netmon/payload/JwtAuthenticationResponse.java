package org.redapps.netmon.payload;

import org.redapps.netmon.model.Role;

import java.util.HashSet;
import java.util.Set;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    private Set<Role> roles = new HashSet<>();

    public JwtAuthenticationResponse(String accessToken, Set<Role> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Set<Role> getRole() {
        return roles;
    }

    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }
}
