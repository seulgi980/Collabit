import ChattingRoom from "@/features/chat/ui/ChattingRoom";

const ChatRoomPage = async ({
  params,
}: {
  params: Promise<{ projectId: number }>;
}) => {
  const { projectId } = await params;
  return <ChattingRoom id={projectId} />;
};

export default ChatRoomPage;
