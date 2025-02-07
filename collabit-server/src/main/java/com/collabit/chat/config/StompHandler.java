package com.collabit.chat.config;

import com.collabit.user.repository.UserRepository;
import com.collabit.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // ✅ CONNECT 명령어인 경우 nickname 기반 인증 수행
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.debug("🔵 WebSocket CONNECT 요청 수신");

            try {
                // ✅ STOMP 헤더에서 `nickname` 가져오기
                List<String> nicknameHeaders = accessor.getNativeHeader("nickname");
                String nickname = (nicknameHeaders != null && !nicknameHeaders.isEmpty()) ? nicknameHeaders.get(0) : null;

                if (nickname == null || nickname.isBlank()) {
                    log.warn("🔴 WebSocket 요청에서 nickname이 제공되지 않음.");
                    throw new RuntimeException("nickname이 필요합니다.");
                }

                // ✅ nickname을 이용해 userCode 조회
                Optional<User> userOptional = userRepository.findByNickname(nickname);
                if (userOptional.isEmpty()) {
                    log.warn("🔴 WebSocket 요청에서 유효하지 않은 nickname: {}", nickname);
                    throw new RuntimeException("사용자를 찾을 수 없습니다.");
                }

                User user = userOptional.get();
                String userCode = user.getCode(); // userCode 가져오기

                // ✅ SecurityContextHolder에 인증 정보 설정
                Authentication authentication = new UsernamePasswordAuthenticationToken(userCode, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                accessor.setUser(authentication);

                log.debug("🟢 WebSocket 인증 성공: userCode={}", userCode);
            } catch (Exception e) {
                log.error("🔴 WebSocket 인증 실패: {}", e.getMessage());
                throw new RuntimeException("WebSocket 인증 실패");
            }
        }

        return message;
    }
}
