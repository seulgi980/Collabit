"use client";
import { useAuth } from "@/features/auth/api/useAuth";
import usePost from "@/features/community/api/usePost";
import Post from "@/features/community/ui/Post";

const PostPage = () => {
  const {
    images,
    preview,
    content,
    handleImageChange,
    handleContentChange,
    handleDeleteImage,
    handleSubmit,
  } = usePost();
  const { userInfo } = useAuth();
  return (
    <Post
      userInfo={userInfo}
      images={images}
      preview={preview}
      content={content}
      handleImageChange={handleImageChange}
      handleContentChange={handleContentChange}
      handleDeleteImage={handleDeleteImage}
      handleSubmit={handleSubmit}
    />
  );
};

export default PostPage;
