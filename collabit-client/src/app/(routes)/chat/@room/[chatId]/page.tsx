import ChattingRoom from "@/features/chat/ui/ChatRoom";

const ChatRoomPage = async ({
  params,
}: {
  params: Promise<{ chatId: number }>;
}) => {
  const { chatId } = await params;
  return <ChattingRoom id={chatId} />;
};

export default ChatRoomPage;
