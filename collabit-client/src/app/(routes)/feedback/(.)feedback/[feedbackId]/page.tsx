import ChattingRoom from "@/features/chat/ui/ChattingRoom";

const ChatRoomPage = async ({
  params,
}: {
  params: Promise<{ feedbackId: number }>;
}) => {
  const { feedbackId } = await params;
  return <ChattingRoom id={feedbackId} />;
};

export default ChatRoomPage;
