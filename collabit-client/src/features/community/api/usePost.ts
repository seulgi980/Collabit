import { useState, useEffect } from "react";

const usePost = () => {
  const [images, setImages] = useState<File[]>([]);
  const [preview, setPreview] = useState<string[]>([]);
  const [content, setContent] = useState<string>("");

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = e.target.files;
    if (files) {
      const imageArray = Array.from(files);
      if (imageArray.length + images.length > 4) {
        alert("이미지는 최대 4개까지 업로드 가능합니다.");
        return;
      }
      setImages((prev) => [...prev, ...imageArray]);
      const newPreview = imageArray.map((file) => URL.createObjectURL(file));
      setPreview((prev) => [...prev, ...newPreview]);
      console.log(preview);
    }
  };

  const handleContentChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContent(e.target.value);
  };

  useEffect(() => {
    return () => {
      preview.forEach((url) => URL.revokeObjectURL(url));
    };
  }, [preview]);

  const handleDeleteImage = (index: number) => {
    setPreview((prev) => prev.filter((_, i) => i !== index));
    setImages((prev) => prev.filter((_, i) => i !== index));
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(content, images);
  };
  return {
    images,
    preview,
    content,
    handleImageChange,
    handleContentChange,
    handleDeleteImage,
    handleSubmit,
  };
};

export default usePost;
