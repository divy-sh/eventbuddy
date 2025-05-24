package com.eventbuddy.eventbuddy.Utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.eventbuddy.eventbuddy.model.User;

public class Utils {
    public static User getAuthenticatedUser() throws BuddyError {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        } else {
            throw new BuddyError("Current principal is not a User");
        }
    }
}
