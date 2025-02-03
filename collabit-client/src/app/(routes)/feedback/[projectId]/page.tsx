import ChattingRoom from "@/features/chat/ui/ChattingRoom";

const FeedbackPage = async ({
  params,
}: {
  params: Promise<{ projectId: number }>;
}) => {
  const { projectId } = await params;
  return <ChattingRoom id={projectId} />;
};

export default FeedbackPage;
