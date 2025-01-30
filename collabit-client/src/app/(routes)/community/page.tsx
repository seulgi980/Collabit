"use client";
import usePost from "@/features/community/api/usePost";
import Post from "@/features/community/ui/Post";

export default function Page() {
  const {
    images,
    preview,
    content,
    handleImageChange,
    handleContentChange,
    handleDeleteImage,
    handleSubmit,
  } = usePost();

  return (
    <div className="mx-auto max-w-5xl py-5 md:py-10">
      <h2 className="sr-only">커뮤니티</h2>
      <Post
        images={images}
        preview={preview}
        content={content}
        handleImageChange={handleImageChange}
        handleContentChange={handleContentChange}
        handleDeleteImage={handleDeleteImage}
        handleSubmit={handleSubmit}
      />
      {/* <ul
          className={cn(
            preview.length > 0 &&
              "grid h-[260px] w-[400px] overflow-hidden rounded-lg",
            preview.length === 1 && "grid-cols-1",
            preview.length === 2 && "grid-cols-2",
            preview.length === 3 && "grid-cols-2",
            preview.length === 4 && "grid-cols-2 grid-rows-2",
          )}
        >
          {preview.map((preview, index) => (
            <li key={preview} className="relative h-full w-full">
              <Image
                className="object-cover"
                src={preview}
                alt={`미리보기 ${index + 1}`}
                fill
                sizes="(max-width: 400px) 100vw"
              />
            </li>
          ))}
        </ul> */}
    </div>
  );
}
