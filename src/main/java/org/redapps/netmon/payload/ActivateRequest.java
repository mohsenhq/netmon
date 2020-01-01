package org.redapps.netmon.payload;

import org.redapps.netmon.util.AppConstants;

import javax.validation.constraints.*;

public class ActivateRequest {

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    @NotBlank
    @Size(max = AppConstants.ACTIVATION_CODE_LEN)
    private String activationCode;
}


