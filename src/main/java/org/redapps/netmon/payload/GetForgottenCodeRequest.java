package org.redapps.netmon.payload;

import javax.validation.constraints.NotBlank;

public class GetForgottenCodeRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
