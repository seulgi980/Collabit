import ChattingRoom from "@/features/chat/ui/ChatRoom";

const ChatRoomPage = async ({
  params,
}: {
  params: Promise<{ projectId: number }>;
}) => {
  const { projectId } = await params;
  return <ChattingRoom id={projectId} />;
};

export default ChatRoomPage;
