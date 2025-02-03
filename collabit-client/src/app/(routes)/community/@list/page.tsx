import FloatingButton from "@/entities/common/ui/FloatingButton";
import CommunityCard from "@/features/community/ui/CommunityCard";

const ListPage = () => {
  return (
    <div className="relative w-full">
      <h2 className="sr-only">커뮤니티</h2>
      {posts.map((post) => (
        <CommunityCard key={post.id} post={post} />
      ))}
      <FloatingButton href={"/community/post"} />
    </div>
  );
};

export default ListPage;

const posts = [
  {
    id: 1,
    user: {
      nickname: "clapsheep",
      profileImage: "https://github.com/shadcn.png",
    },
    content:
      "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Ab, cumque nobis suscipit neque voluptate numquam officia blanditiis, quisquam fugiat eaque est ducimus, ipsum quod culpa! Qui non voluptas quasi debitis?",
    images: [
      "https://github.com/shadcn.png",
      "https://github.com/shadcn.png",
      "https://github.com/shadcn.png",
      "https://github.com/shadcn.png",
    ],
    likeCount: 100,
    commentCount: 100,
    isLiked: true,
    createdAt: "2025-01-31",
  },
  {
    id: 2,
    user: {
      nickname: "어피치",
      profileImage: "https://github.com/shadcn.png",
    },
    content:
      "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Ab, cumque nobis suscipit neque voluptate numquam officia blanditiis, quisquam fugiat eaque est ducimus, ipsum quod culpa! Qui non voluptas quasi debitis?",
    images: [
      "https://github.com/shadcn.png",
      "https://github.com/shadcn.png",
      "https://github.com/shadcn.png",
    ],
    likeCount: 100,
    commentCount: 100,
    isLiked: false,
    createdAt: "2025-01-29",
  },
  {
    id: 3,
    user: {
      nickname: "춘식이",
      profileImage: "https://github.com/shadcn.png",
    },
    content:
      "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Ab, cumque nobis suscipit neque voluptate numquam officia blanditiis, quisquam fugiat eaque est ducimus, ipsum quod culpa! Qui non voluptas quasi debitis?",
    images: ["https://github.com/shadcn.png", "https://github.com/shadcn.png"],
    likeCount: 100,
    commentCount: 100,
    isLiked: false,
    createdAt: "2025-01-04",
  },
  {
    id: 4,
    user: {
      nickname: "프로도",
      profileImage: "https://github.com/shadcn.png",
    },
    content:
      "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Ab, cumque nobis suscipit neque voluptate numquam officia blanditiis, quisquam fugiat eaque est ducimus, ipsum quod culpa! Qui non voluptas quasi debitis?",
    images: ["https://github.com/shadcn.png"],
    likeCount: 100,
    commentCount: 100,
    isLiked: false,
    createdAt: "2024-12-25",
  },
  {
    id: 5,
    user: {
      nickname: "무지",
      profileImage: "https://github.com/shadcn.png",
    },
    content:
      "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Ab, cumque nobis suscipit neque voluptate numquam officia blanditiis, quisquam fugiat eaque est ducimus, ipsum quod culpa! Qui non voluptas quasi debitis?",
    images: [],
    likeCount: 100,
    commentCount: 100,
    isLiked: false,
    createdAt: "2024-12-12",
  },
];
