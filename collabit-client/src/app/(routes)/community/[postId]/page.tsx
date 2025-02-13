import ModalPostDetail from "@/widget/community/ModalPostDetail";

const CommunityDetailPage = async ({
  params,
}: {
  params: Promise<{ postId: string }>;
}) => {
  const { postId } = await params;

  return <ModalPostDetail postId={postId} />;
};

export default CommunityDetailPage;
