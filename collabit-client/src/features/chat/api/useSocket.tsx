import { PageResponse } from "@/shared/types/response/page";
import { ChatMessageResponse } from "@/shared/types/response/chat";
import { Client, IMessage } from "@stomp/stompjs";
import { useQueryClient } from "@tanstack/react-query";
import { useEffect, useRef } from "react";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { ChatRoomSwitchRequest } from "@/shared/types/request/chat";
import SockJS from "sockjs-client";
import { useAuth } from "@/features/auth/api/useAuth";

export const useSocket = (roomCode?: number) => {
  const clientRef = useRef<Client | null>(null);
  const queryClient = useQueryClient();
  const { userInfo } = useAuth();

  useEffect(() => {
    if (!userInfo?.nickname) return;
    if (clientRef.current) return;

    const socket = new SockJS(`http://localhost:8080/ws/chat`, null, {
      transports: ["websocket", "xhr-streaming", "xhr-polling"],
    });

    clientRef.current = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      debug: (msg) => console.log(msg),
      connectHeaders: { nickname: userInfo?.nickname },
      heartbeatIncoming: 0, // ✅ Heartbeat 비활성화
      heartbeatOutgoing: 0, // ✅ Heartbeat 비활성화
      onConnect: () => {
        console.log(`✅ WebSocket 연결 성공 - Room: ${roomCode}`);

        clientRef.current?.subscribe(
          `/topic/chat/${roomCode}`,
          (message: IMessage) => {
            try {
              const newMessage: ChatMessageResponse = JSON.parse(message.body);

              queryClient.setQueryData(
                ["chatMessages", roomCode],
                (oldData: { pages: PageResponse<ChatMessageResponse>[] }) => {
                  if (!oldData || !oldData.pages?.length) {
                    return {
                      pages: [
                        {
                          content: [newMessage],
                          pageNumber: 1,
                          totalPages: 1,
                        },
                      ],
                    };
                  }
                  return {
                    ...oldData,
                    pages: oldData.pages.map((page, index) =>
                      index === 0
                        ? {
                            ...page,
                            content: [newMessage, ...page.content],
                          }
                        : page,
                    ),
                  };
                },
              );
            } catch (error) {
              console.error("❌ 메시지 처리 중 오류 발생:", error);
            }
          },
        );
      },
      onDisconnect: () => {
        console.log("🔴 WebSocket 연결 해제");
      },
    });

    clientRef.current.activate();

    // ✅ 페이지가 닫힐 때 WebSocket 연결 해제
    const handleBeforeUnload = () => {
      if (clientRef.current?.connected) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
      if (clientRef.current?.connected) {
        clientRef.current.deactivate();
        clientRef.current = null;
      }
    };
  }, [userInfo?.nickname]);

  return {
    sendMessage: (message: WebSocketMessage) => {
      if (!clientRef.current?.connected) {
        console.error("❌ WebSocket이 연결되지 않음.");
        return;
      }
      clientRef.current.publish({
        destination: `/app/chat/sendMessage/${roomCode}`,
        body: JSON.stringify(message),
      });
    },
    switchRoom: (switchDTO: ChatRoomSwitchRequest) => {
      if (!clientRef.current?.connected) {
        console.error("❌ WebSocket이 연결되지 않음.");
        return;
      }
      clientRef.current.publish({
        destination: `/app/chat/switchRoom`,
        body: JSON.stringify(switchDTO),
      });
    },
  };
};
