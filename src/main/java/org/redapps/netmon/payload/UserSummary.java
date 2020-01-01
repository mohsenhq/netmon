package org.redapps.netmon.payload;

public class UserSummary {
    private Long id;
    private String username;
    private String name;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private boolean isActive;

    public UserSummary(Long id, String username, String name , boolean isActive) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.isActive =isActive;
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
}
