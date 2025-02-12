const CommunityDetailPage = async ({
  params,
}: {
  params: Promise<{ postId: string }>;
}) => {
  const { postId } = await params;
  console.log(postId);

  return <div>CommunityDetailPage</div>;
};

export default CommunityDetailPage;
