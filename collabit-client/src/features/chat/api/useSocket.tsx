import { Client, IMessage, StompSubscription } from "@stomp/stompjs";
import { useEffect, useRef, useState } from "react";
import SockJS from "sockjs-client";
import { useAuth } from "@/features/auth/api/useAuth";
import { useChatStore } from "@/shared/lib/stores/chatStore";
import { ChatRoomSwitchRequest } from "@/shared/types/request/chat";
import { ChatMessageResponse } from "@/shared/types/response/chat";
import { WebSocketMessage } from "@/shared/types/model/Chat";

export const useSocket = () => {
  const clientRef = useRef<Client | null>(null);
  const subscriptionRefs = useRef<{ chatRoom?: StompSubscription }>({});
  const prevRoomId = useRef<number | null>(null);
  const { userInfo } = useAuth();
  const { chatId, updateChatMessages } = useChatStore();
  const [connectionStatus, setConnectionStatus] = useState<string | null>(null);

  // ✅ WebSocket 연결 및 구독 설정

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
        if (chatId) subscribeToRoom(chatId);
        setConnectionStatus("connected");
      },
      onDisconnect: () => {
        console.log("🔴 WebSocket 연결 해제");
        setConnectionStatus("disconnected");
      },
    });

    clientRef.current.activate();

    return () => {
      if (subscriptionRefs.current.chatRoom) {
        subscriptionRefs.current.chatRoom.unsubscribe();
        subscriptionRefs.current.chatRoom = undefined;
      }

      if (clientRef.current?.connected) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [userInfo?.nickname]);

  // ✅ 채팅방 구독 함수
  const subscribeToRoom = (roomId: number) => {
    if (!clientRef.current?.connected) {
      console.error("❌ Cannot subscribe: WebSocket not connected");
      return;
    }

    if (subscriptionRefs.current.chatRoom) {
      subscriptionRefs.current.chatRoom.unsubscribe();
    }

    console.log(`✅ 채팅방 ${roomId} 구독 시작`);
    subscriptionRefs.current.chatRoom = clientRef.current.subscribe(
      `/topic/chat/${roomId}`,
      (message: IMessage) => {
        try {
          const newMessage: ChatMessageResponse = JSON.parse(message.body);
          updateChatMessages((prevMessages) => [...prevMessages, newMessage]);
        } catch (error) {
          console.error("❌ 메시지 처리 오류:", error);
        }
      },
    );
  };

  // ✅ 채팅방 이동 감지 및 switchRoom 실행
  useEffect(() => {
    if (
      !clientRef.current?.connected ||
      !chatId ||
      prevRoomId.current === chatId
    ) {
      return;
    }

    const switchRequest: ChatRoomSwitchRequest = {
      oldRoomCode: prevRoomId.current ?? 0,
      newRoomCode: chatId,
    };

    console.log(
      `🔄 방 이동: ${switchRequest.oldRoomCode} → ${switchRequest.newRoomCode}`,
    );

    clientRef.current.publish({
      destination: "/app/chat.switchRoom",
      body: JSON.stringify(switchRequest),
    });

    subscribeToRoom(chatId);
    prevRoomId.current = chatId;
  }, [chatId]);

  // ✅ WebSocket 메시지 전송 함수
  const sendMessage = (message: WebSocketMessage) => {
    if (!clientRef.current?.connected) {
      console.error("❌ WebSocket이 연결되지 않음.");
      return;
    }
    const { chatId } = useChatStore.getState();
    if (!chatId) {
      console.error("❌ chatId가 설정되지 않음.");
      return;
    }
    console.log("📩 메시지 전송:", message);
    clientRef.current.publish({
      destination: `/app/chat.message/${chatId}`,
      body: JSON.stringify(message),
    });
  };

  return { clientRef, sendMessage, connectionStatus };
};
