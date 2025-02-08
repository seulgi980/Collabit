import { Client, StompSubscription } from "@stomp/stompjs";
import { useEffect, useRef, useState, useCallback } from "react";
import SockJS from "sockjs-client";
import { useAuth } from "@/features/auth/api/useAuth";
import { useChatStore } from "@/shared/lib/stores/chatStore";
import { ChatRoomSwitchRequest } from "@/shared/types/request/chat";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { useQueryClient } from "@tanstack/react-query";
import {
  ChatRoomListResponse,
  ChatMessageResponse,
} from "@/shared/types/response/chat";
import { PageResponse } from "@/shared/types/response/page";

export const useSocket = () => {
  const clientRef = useRef<Client | null>(null);
  const subscriptionRefs = useRef<Record<string, StompSubscription>>({});
  const prevRoomId = useRef<number | null>(null);
  const { userInfo } = useAuth();
  const { chatId, updateUserStatus } = useChatStore();
  const [connectionStatus, setConnectionStatus] = useState<string | null>(null);
  const queryClient = useQueryClient();

  // WebSocket 연결 및 구독 설정
  useEffect(() => {
    if (!userInfo?.nickname || clientRef.current) return;

    const socket = new SockJS(`http://localhost:8080/ws/chat`);
    clientRef.current = new Client({
      webSocketFactory: () => socket as unknown as WebSocket,
      reconnectDelay: 5000,
      debug: (msg) => console.log(msg),
      connectHeaders: { nickname: userInfo.nickname },
      heartbeatIncoming: 0,
      heartbeatOutgoing: 0,
      onConnect: () => {
        console.log("✅ WebSocket 연결 성공");
        if (chatId) {
          // 연결 후 채팅방 연결 요청
          clientRef.current?.publish({
            destination: `/app/chat.connect/${chatId}`,
            body: JSON.stringify({ roomCode: chatId }),
          });
          subscribeToRoom(chatId);
        }
        setConnectionStatus("connected");
      },
      onDisconnect: () => {
        console.log("🔴 WebSocket 연결 해제");
        if (chatId) {
          clientRef.current?.publish({
            destination: `/app/chat.disconnect/${chatId}`,
            body: JSON.stringify({ roomCode: chatId }),
          });
        }
        setConnectionStatus("disconnected");
      },
    });

    clientRef.current.activate();

    return () => {
      // 모든 구독 해제
      Object.values(subscriptionRefs.current).forEach((subscription) => {
        subscription?.unsubscribe();
      });
      subscriptionRefs.current = {};

      if (clientRef.current?.connected) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [userInfo?.nickname]);

  // 채팅방 구독 함수
  const subscribeToRoom = (roomCode: number) => {
    if (!clientRef.current?.connected) {
      console.error("❌ Cannot subscribe: WebSocket not connected");
      return;
    }

    if (subscriptionRefs.current[roomCode.toString()]) {
      subscriptionRefs.current[roomCode.toString()].unsubscribe();
    }

    console.log(`✅ 채팅방 ${roomCode} 구독 시작`);
    clientRef.current.subscribe(`/topic/chat/${roomCode}`, (message) => {
      const receivedMessage = JSON.parse(message.body);

      if (receivedMessage.type === "USER_ENTER") {
        updateUserStatus(receivedMessage.userCode, true);
      } else if (receivedMessage.type === "USER_EXIT") {
        updateUserStatus(receivedMessage.userCode, false);
      } else if (receivedMessage.type === "USER_SWITCH") {
        updateUserStatus(receivedMessage.userCode, true);
      } else {
        // 채팅 메시지 목록 즉시 업데이트
        queryClient.setQueryData(
          ["chatMessages", roomCode],
          (old: PageResponse<ChatMessageResponse>[]) => {
            const messageResponse: ChatMessageResponse = {
              nickname: receivedMessage.nickname,
              message: receivedMessage.message,
              timestamp: receivedMessage.timestamp,
            };

            const updatedPages = [...old];
            updatedPages[0] = {
              ...updatedPages[0],
              content: [messageResponse, ...updatedPages[0].content],
            };

            return {
              ...old,
              pages: updatedPages,
            };
          },
        );
      }
    });
  };

  // 채팅방 이동 감지 및 switchRoom 실행
  useEffect(() => {
    if (!clientRef.current?.connected || !chatId) {
      return;
    }

    const switchRequest: ChatRoomSwitchRequest = {
      newRoomCode: chatId,
    };

    console.log(`🔄 방 이동: → ${switchRequest.newRoomCode}`);

    // 이전 방 구독 해제
    if (prevRoomId.current) {
      clientRef.current.unsubscribe(`/topic/chat/${prevRoomId.current}`);
      clientRef.current.unsubscribe(`/topic/status/${prevRoomId.current}`);
    }

    // 새로운 방 구독 및 전환 메시지 전송
    subscribeToRoom(chatId);
    clientRef.current.publish({
      destination: "/app/chat.switchRoom",
      body: JSON.stringify(switchRequest),
    });

    prevRoomId.current = chatId;
  }, [chatId]);

  // WebSocket 메시지 전송 함수
  const sendMessage = (message: WebSocketMessage) => {
    if (!clientRef.current?.connected) {
      console.error("❌ WebSocket이 연결되지 않음.");
      return;
    }
    if (!chatId) {
      console.error("❌ chatId가 설정되지 않음.");
      return;
    }

    console.log("📩 메시지 전송:", message);
    clientRef.current.publish({
      destination: `/app/chat.message/${chatId}`,
      body: JSON.stringify(message),
    });

    // 채팅방 목록 데이터 직접 업데이트
    queryClient.setQueryData(["chatList", userInfo?.nickname], (old: any) => {
      if (!old?.pages) return old;

      const updatedPages = old.pages.map((page: any) => ({
        ...page,
        content: page.content.map((room: ChatRoomListResponse) => {
          if (room.roomCode === chatId) {
            return {
              ...room,
              lastMessage: message.message,
              lastMessageTime: message.timestamp,
            };
          }
          return room;
        }),
      }));

      return {
        ...old,
        pages: updatedPages,
      };
    });
  };

  const subscribe = useCallback(
    (destination: string, callback: (message: WebSocketMessage) => void) => {
      if (!clientRef.current?.connected) return;

      // 기존 구독이 있다면 제거
      if (subscriptionRefs.current[destination]) {
        subscriptionRefs.current[destination].unsubscribe();
      }

      // 새로운 구독 추가
      subscriptionRefs.current[destination] = clientRef.current.subscribe(
        destination,
        (message) => {
          callback(JSON.parse(message.body));
        },
      );
    },
    [],
  );

  return { clientRef, sendMessage, connectionStatus, subscribe };
};
