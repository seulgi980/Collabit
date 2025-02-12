import { ChartResponse } from "@/shared/types/response/report";
import { Metadata } from "next";

interface SharePageProps {
  params: Promise<{ user: string }>;
}

export async function generateMetadata({
  params,
}: {
  params: { user: string };
}): Promise<Metadata> {
  const { user } = params;
  const data = await getSharedUserReportAPI(user);

  return {
    title: `${data.info?.nickname}님의 협업 리포트 - Collabit`,
    description: `${data.aiSummary?.strength}`,
    openGraph: {
      title: `${data.info?.nickname}님의 협업 리포트 - Collabit`,
      description: `${data.aiSummary?.strength}`,
    },
  };
}

// ${배포주소}/share/user/{hashUser(nickname)}
const SharePage = async ({ params }: SharePageProps) => {
  const { user } = await params;
  const data = await getSharedUserReportAPI(user);

  return (
    <div>
      <h1>Share Page for user: {user}</h1>
    </div>
  );
};

export default SharePage;

const api = process.env.NEXT_PUBLIC_API_URL;
const getSharedUserReportAPI = async (user: string): Promise<ChartResponse> => {
  const res = await fetch(`${api}/portfolio/share/${user}`, {
    cache: "force-cache",
  });

  const data = await res.json();
  return data;
};
