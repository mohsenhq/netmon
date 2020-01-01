package org.redapps.netmon.payload;

import org.redapps.netmon.util.AppConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ResetPasswordRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String newPassword;

    @NotBlank
    @Size(max = AppConstants.ACTIVATION_CODE_LEN)
    private String forgottenCode;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getForgottenCode() {
        return forgottenCode;
    }

    public void setForgottenCode(String forgottenCode) {
        this.forgottenCode = forgottenCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
