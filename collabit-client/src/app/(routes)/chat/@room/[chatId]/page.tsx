import ChattingRoom from "@/features/chat/ui/ChattingRoom";

const ChatRoomPage = async ({
  params,
}: {
  params: Promise<{ chatId: number }>;
}) => {
  const { chatId } = await params;
  console.log(chatId, "server");

  return <ChattingRoom chatId={chatId} />;
};

export default ChatRoomPage;
