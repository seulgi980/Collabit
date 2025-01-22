package com.collabit.chat.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int code;

    @Column(nullable = false, unique = true)
    private String uniqueCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCode1", referencedColumnName = "code", nullable = false)
    private User user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userCode2", referencedColumnName = "code", nullable = false)
    private User user2;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // 중복 방지 코드 생성 로직
    public static String generateChatRoomCode(String userCode1, String userCode2) {
        if (userCode1.compareTo(userCode2) < 0) {
            return userCode1 + "-" + userCode2;
        } else {
            return userCode2 + "-" + userCode1;
        }
    }
}
