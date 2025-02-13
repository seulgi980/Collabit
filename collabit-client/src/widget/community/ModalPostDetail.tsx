"use client";

import Modal from "@/shared/ui/Modal";

const ModalPostDetail = ({ postId }: { postId: string }) => {
  return (
    <Modal>
      <div className="h-[500px] w-full overflow-y-auto">
        <h1>게시글이다 {postId}</h1>
        {/* 실제 게시글 내용 */}
      </div>
    </Modal>
  );
};

export default ModalPostDetail;
