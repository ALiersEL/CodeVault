package com.aliersel.codevaultbackend.constant.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

public enum AppUserRole implements GrantedAuthority {
    ROLE_ADMIN(0),
    ROLE_USER(1),
    ROLE_GUEST(2);

    @Getter
    private Integer level;

    AppUserRole(Integer level) {
        this.level = level;
    }

    public static AppUserRole getRoleByLevel(Integer level) {
        for (AppUserRole role : AppUserRole.values()) {
            if (role.getLevel().equals(level)) {
                return role;
            }
        }
        return null;
    }

    public String getAuthority() {
        return name();
    }

}
