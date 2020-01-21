package org.redapps.netmon.model;

import org.redapps.netmon.model.audit.DateAudit;
import org.redapps.netmon.util.AppConstants;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        })
})

public class User extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NaturalId // unique, not null
    @NotBlank
    @Size(max = 30)
    private String username;

    @Nullable
    private long nationalID;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(max = 20)
    private String mobile;

    @NotBlank
    @Size(max = 20)
    private String telNumber;

    @Size(max = AppConstants.ACTIVATION_CODE_LEN)
    private String activationCode;

    @Size(max = AppConstants.FORGOTTEN_CODE_LEN)
    private String forgottenCode;

    @Type(type = "yes_no")
    private boolean active = false;

    @NotBlank
    @Size(max = 100)
    private String password;

    private LocalDateTime lastAccess;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {

    }

    public User(String name, String username, long nationalID, String email, String mobile,
                String telNumber, String password, String activationCode) {
        this.name = name;
        this.username = username;
        this.nationalID = nationalID;
        this.email = email;
        this.mobile = mobile;
        this.telNumber = telNumber;
        this.password = password;
        this.activationCode = activationCode;
        this.active = false;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getForgottenCode() {
        return forgottenCode;
    }

    public void setForgottenCode(String forgottenCode) {
        this.forgottenCode = forgottenCode;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isCustomer() {

        for (Role role : roles) {
            if (role.getName() == (RoleName.ROLE_CUSTOMER))
                return true;
        }
        return false;
    }

    public long getNationalID() {
        return nationalID;
    }

    public void setNationalID(long nationalID) {
        this.nationalID = nationalID;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }
}