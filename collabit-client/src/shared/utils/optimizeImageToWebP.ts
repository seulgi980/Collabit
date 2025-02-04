const optimizeImageToWebP = async (
  file: File,
  maxWidth = 1920,
  quality = 0.8,
): Promise<Blob> => {
  return new Promise((resolve, reject) => {
    // WebP 지원 확인
    const canUseWebP = () => {
      const elem = document.createElement("canvas");
      if (elem.getContext && elem.getContext("2d")) {
        return elem.toDataURL("image/webp").indexOf("data:image/webp") === 0;
      }
      return false;
    };

    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = (event) => {
      const img = new Image();
      img.src = event.target?.result as string;
      img.onload = () => {
        const canvas = document.createElement("canvas");
        let width = img.width;
        let height = img.height;

        // 이미지 리사이징
        if (width > maxWidth) {
          height = Math.round((height * maxWidth) / width);
          width = maxWidth;
        }

        canvas.width = width;
        canvas.height = height;

        const ctx = canvas.getContext("2d");
        ctx?.drawImage(img, 0, 0, width, height);

        // WebP 지원 여부에 따라 포맷 결정
        const imageFormat = canUseWebP() ? "image/webp" : "image/jpeg";

        canvas.toBlob(
          (blob) => {
            if (blob) {
              resolve(blob);
            } else {
              reject(new Error("이미지 최적화 실패"));
            }
          },
          imageFormat,
          quality,
        );
      };
      img.onerror = reject;
    };
    reader.onerror = reject;
  });
};

export default optimizeImageToWebP;
