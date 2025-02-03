import { ProjectListResponse } from "../types/response/project";

const mockProjectList: ProjectListResponse = [
  {
    organization: "네이버",
    organizationImage: "https://www.naver.com/favicon.ico",
    projects: [
      {
        title: "네이버 페이 리뉴얼",
        code: 2025,
        total: 8,
        participant: 6,
        isDone: false,
        createdAt: "2024-03-15",
        contributors: [
          {
            githubId: "github1",
            profileImage: `https://www.gravatar.com/avatar/github1?d=identicon&f=y`,
          },
          {
            githubId: "github2",
            profileImage: `https://www.gravatar.com/avatar/github2?d=identicon&f=y`,
          },
          {
            githubId: "johndoe",
            profileImage: `https://www.gravatar.com/avatar/johndoe?d=identicon&f=y`,
          },
          {
            githubId: "janesmith",
            profileImage: `https://www.gravatar.com/avatar/janesmith?d=identicon&f=y`,
          },
          {
            githubId: "developer123",
            profileImage: `https://www.gravatar.com/avatar/developer123?d=identicon&f=y`,
          },
          {
            githubId: "coder456",
            profileImage: `https://www.gravatar.com/avatar/coder456?d=identicon&f=y`,
          },
          {
            githubId: "techie789",
            profileImage: `https://www.gravatar.com/avatar/techie789?d=identicon&f=y`,
          },
          {
            githubId: "webdev101",
            profileImage: `https://www.gravatar.com/avatar/webdev101?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "네이버 지도 UI 개선",
        code: 2024,
        total: 6,
        participant: 4,
        isDone: true,
        createdAt: "2024-02-20",
        contributors: [
          {
            githubId: "github3",
            profileImage: `https://www.gravatar.com/avatar/github3?d=identicon&f=y`,
          },
          {
            githubId: "uimaster",
            profileImage: `https://www.gravatar.com/avatar/uimaster?d=identicon&f=y`,
          },
          {
            githubId: "designer99",
            profileImage: `https://www.gravatar.com/avatar/designer99?d=identicon&f=y`,
          },
          {
            githubId: "frontdev",
            profileImage: `https://www.gravatar.com/avatar/frontdev?d=identicon&f=y`,
          },
          {
            githubId: "cssartist",
            profileImage: `https://www.gravatar.com/avatar/cssartist?d=identicon&f=y`,
          },
          {
            githubId: "pixelpro",
            profileImage: `https://www.gravatar.com/avatar/pixelpro?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "네이버 검색 알고리즘 개선",
        code: 2029,
        total: 2,
        participant: 2,
        isDone: true,
        createdAt: "2024-01-05",
        contributors: [
          {
            githubId: "searchmaster",
            profileImage: `https://www.gravatar.com/avatar/searchmaster?d=identicon&f=y`,
          },
          {
            githubId: "algopro",
            profileImage: `https://www.gravatar.com/avatar/algopro?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "네이버 클라우드 보안 강화",
        code: 2030,
        total: 4,
        participant: 3,
        isDone: false,
        createdAt: "2024-03-25",
        contributors: [
          {
            githubId: "cloudsec",
            profileImage: `https://www.gravatar.com/avatar/cloudsec?d=identicon&f=y`,
          },
          {
            githubId: "seceng",
            profileImage: `https://www.gravatar.com/avatar/seceng?d=identicon&f=y`,
          },
          {
            githubId: "netpro",
            profileImage: `https://www.gravatar.com/avatar/netpro?d=identicon&f=y`,
          },
        ],
      },
    ],
  },
  {
    organization: "카카오",
    organizationImage: "https://www.kakaocorp.com/page/favicon.ico",
    projects: [
      {
        title: "카카오톡 채팅 기능 개선",
        code: 2026,
        total: 7,
        participant: 5,
        isDone: false,
        createdAt: "2024-03-10",
        contributors: [
          {
            githubId: "github4",
            profileImage: `https://www.gravatar.com/avatar/github4?d=identicon&f=y`,
          },
          {
            githubId: "chatdev",
            profileImage: `https://www.gravatar.com/avatar/chatdev?d=identicon&f=y`,
          },
          {
            githubId: "messenger",
            profileImage: `https://www.gravatar.com/avatar/messenger?d=identicon&f=y`,
          },
          {
            githubId: "realtime",
            profileImage: `https://www.gravatar.com/avatar/realtime?d=identicon&f=y`,
          },
          {
            githubId: "socketpro",
            profileImage: `https://www.gravatar.com/avatar/socketpro?d=identicon&f=y`,
          },
          {
            githubId: "backend123",
            profileImage: `https://www.gravatar.com/avatar/backend123?d=identicon&f=y`,
          },
          {
            githubId: "fullstack",
            profileImage: `https://www.gravatar.com/avatar/fullstack?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "카카오페이 보안 감사",
        code: 2031,
        total: 2,
        participant: 2,
        isDone: true,
        createdAt: "2024-02-01",
        contributors: [
          {
            githubId: "fintech1",
            profileImage: `https://www.gravatar.com/avatar/fintech1?d=identicon&f=y`,
          },
          {
            githubId: "secaudit",
            profileImage: `https://www.gravatar.com/avatar/secaudit?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "카카오 T 신규 기능 개발",
        code: 2032,
        total: 5,
        participant: 3,
        isDone: false,
        createdAt: "2024-03-18",
        contributors: [
          {
            githubId: "mobilepro",
            profileImage: `https://www.gravatar.com/avatar/mobilepro?d=identicon&f=y`,
          },
          {
            githubId: "mapdev",
            profileImage: `https://www.gravatar.com/avatar/mapdev?d=identicon&f=y`,
          },
          {
            githubId: "taxiapi",
            profileImage: `https://www.gravatar.com/avatar/taxiapi?d=identicon&f=y`,
          },
        ],
      },
    ],
  },
  {
    organization: "구글",
    organizationImage: "https://www.google.com/favicon.ico",
    projects: [
      {
        title: "구글 클라우드 플랫폼 개선",
        code: 2027,
        total: 10,
        participant: 8,
        isDone: false,
        createdAt: "2024-03-20",
        contributors: [
          {
            githubId: "cloudmaster",
            profileImage: `https://www.gravatar.com/avatar/cloudmaster?d=identicon&f=y`,
          },
          {
            githubId: "googledev1",
            profileImage: `https://www.gravatar.com/avatar/googledev1?d=identicon&f=y`,
          },
          {
            githubId: "kubepro",
            profileImage: `https://www.gravatar.com/avatar/kubepro?d=identicon&f=y`,
          },
          {
            githubId: "devopseng",
            profileImage: `https://www.gravatar.com/avatar/devopseng?d=identicon&f=y`,
          },
          {
            githubId: "cloudarchitect",
            profileImage: `https://www.gravatar.com/avatar/cloudarchitect?d=identicon&f=y`,
          },
          {
            githubId: "infradev",
            profileImage: `https://www.gravatar.com/avatar/infradev?d=identicon&f=y`,
          },
          {
            githubId: "srepro",
            profileImage: `https://www.gravatar.com/avatar/srepro?d=identicon&f=y`,
          },
          {
            githubId: "cloudops",
            profileImage: `https://www.gravatar.com/avatar/cloudops?d=identicon&f=y`,
          },
          {
            githubId: "k8smaster",
            profileImage: `https://www.gravatar.com/avatar/k8smaster?d=identicon&f=y`,
          },
          {
            githubId: "gcpexpert",
            profileImage: `https://www.gravatar.com/avatar/gcpexpert?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "안드로이드 13 보안 업데이트",
        code: 2028,
        total: 6,
        participant: 5,
        isDone: true,
        createdAt: "2024-02-15",
        contributors: [
          {
            githubId: "androidsec",
            profileImage: `https://www.gravatar.com/avatar/androidsec?d=identicon&f=y`,
          },
          {
            githubId: "securitypro",
            profileImage: `https://www.gravatar.com/avatar/securitypro?d=identicon&f=y`,
          },
          {
            githubId: "mobilesec",
            profileImage: `https://www.gravatar.com/avatar/mobilesec?d=identicon&f=y`,
          },
          {
            githubId: "cryptodev",
            profileImage: `https://www.gravatar.com/avatar/cryptodev?d=identicon&f=y`,
          },
          {
            githubId: "androiddev",
            profileImage: `https://www.gravatar.com/avatar/androiddev?d=identicon&f=y`,
          },
          {
            githubId: "osdev",
            profileImage: `https://www.gravatar.com/avatar/osdev?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "Chrome 확장프로그램 보안점검",
        code: 2033,
        total: 2,
        participant: 2,
        isDone: true,
        createdAt: "2024-01-15",
        contributors: [
          {
            githubId: "chromesec",
            profileImage: `https://www.gravatar.com/avatar/chromesec?d=identicon&f=y`,
          },
          {
            githubId: "extsecurity",
            profileImage: `https://www.gravatar.com/avatar/extsecurity?d=identicon&f=y`,
          },
        ],
      },
      {
        title: "Google Workspace AI 통합",
        code: 2034,
        total: 7,
        participant: 4,
        isDone: false,
        createdAt: "2024-03-22",
        contributors: [
          {
            githubId: "aidev",
            profileImage: `https://www.gravatar.com/avatar/aidev?d=identicon&f=y`,
          },
          {
            githubId: "mleng",
            profileImage: `https://www.gravatar.com/avatar/mleng?d=identicon&f=y`,
          },
          {
            githubId: "workspacedev",
            profileImage: `https://www.gravatar.com/avatar/workspacedev?d=identicon&f=y`,
          },
          {
            githubId: "productivitypro",
            profileImage: `https://www.gravatar.com/avatar/productivitypro?d=identicon&f=y`,
          },
        ],
      },
    ],
  },
];

export const getMockProjectListAPI = async ({
  keyword,
  sort,
}: {
  keyword?: string;
  sort?: string;
}): Promise<ProjectListResponse> => {
  // 검색어가 있는 경우 필터링
  let filteredProjects = mockProjectList.map((org) => ({
    ...org,
    projects: [...org.projects].sort((a, b) => {
      // isDone이 false인 항목이 앞으로 오도록 정렬
      if (a.isDone === b.isDone) {
        // isDone이 같은 경우 생성일 기준 내림차순
        return (
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
      }
      return a.isDone ? 1 : -1;
    }),
  }));

  if (keyword) {
    filteredProjects = filteredProjects
      .map((org) => ({
        organization: org.organization,
        organizationImage: org.organizationImage,
        projects: org.projects.filter(
          (project) =>
            project.title.toLowerCase().includes(keyword.toLowerCase()) ||
            project.code.toString().includes(keyword),
        ),
      }))
      .filter((org) => org.projects.length > 0);
  }

  // 정렬 처리
  if (sort) {
    filteredProjects = filteredProjects.map((org) => ({
      organization: org.organization,
      organizationImage: org.organizationImage,
      projects: [...org.projects].sort((a, b) => {
        switch (sort) {
          case "latest":
            return (
              new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
            );
          case "oldest":
            return (
              new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
            );
          case "participant":
            return b.participant - a.participant;
          default:
            return 0;
        }
      }),
    }));
  }

  // API 호출 시뮬레이션
  await new Promise((resolve) => setTimeout(resolve, 100));

  return filteredProjects;
};
