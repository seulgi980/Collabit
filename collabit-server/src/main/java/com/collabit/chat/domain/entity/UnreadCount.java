package com.collabit.chat.domain.entity;

import java.util.HashMap;
import java.util.Map;

public class UnreadCount {
    private int roomCode;
    private Map<String, Integer> userUnreadCounts; // Key: UserCode, Value: Unread count

    public UnreadCount(int roomCode) {
        this.roomCode = roomCode;
        this.userUnreadCounts = new HashMap<>();
    }

    public void incrementUnreadCount(String userCode) {
        userUnreadCounts.put(userCode, userUnreadCounts.getOrDefault(userCode, 0) + 1);
    }

    public void resetUnreadCount(String userCode) {
        userUnreadCounts.put(userCode, 0);
    }

    public int getUnreadCount(String userCode) {
        return userUnreadCounts.getOrDefault(userCode, 0);
    }
}