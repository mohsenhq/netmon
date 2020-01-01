package org.redapps.netmon.model;

import java.util.Arrays;

public enum  RoleName {
    ROLE_ADMIN("admin"), // System administration
    ROLE_CUSTOMER ("customer"), // Customer
    ROLE_OFFICE("office"), // Staff user
    ROLE_TECHNICAL("technical"),// Network and system users
    ROLE_MANAGER("manager"); // Manager of organization

    private String value;

    RoleName(String value) {
        this.value = value;
    }

    public static RoleName fromValue(String value) {
        for (RoleName role : values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(values()));
    }
}
