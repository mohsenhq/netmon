package org.redapps.netmon.payload;

import org.redapps.netmon.model.Company;
import org.redapps.netmon.model.Role;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserResponse {

    private Long id;
    private String name;
    private String username;
    private long nationalId;
    private String email;
    private String mobile;
    private String telNumber;
    private boolean active;
    private Instant createdAt;
    private LocalDateTime lastAccess;
    private Set<Role> roles = new HashSet<>();

    private Company company;

    public UserResponse() {
    }

    public UserResponse(Long id, String name, String username, long nationalId, String email, String mobile,
                        String telNumber, boolean active, Instant createdAt, LocalDateTime lastAccess,
                        Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.nationalId = nationalId;
        this.email = email;
        this.mobile = mobile;
        this.telNumber = telNumber;
        this.active = active;
        this.createdAt = createdAt;
        this.lastAccess = lastAccess;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getNationalId() {
        return nationalId;
    }

    public void setNationalId(long nationalId) {
        this.nationalId = nationalId;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
