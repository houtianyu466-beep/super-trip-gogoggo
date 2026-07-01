package com.example.tripagent.common.auth;

public class UserContext {

    private static final ThreadLocal<LoginUser> LOCAL = new ThreadLocal<>();

    private UserContext() {
    }

    public static void set(LoginUser loginUser) {
        LOCAL.set(loginUser);
    }

    public static LoginUser get() {
        return LOCAL.get();
    }

    public static Long getEmployeeId() {
        LoginUser loginUser = get();
        return loginUser == null ? null : loginUser.getEmployeeId();
    }

    public static String getRole() {
        LoginUser loginUser = get();
        return loginUser == null ? null : loginUser.getRole();
    }

    public static void clear() {
        LOCAL.remove();
    }
}