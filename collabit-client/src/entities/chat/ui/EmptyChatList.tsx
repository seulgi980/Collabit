const EmptyChatList = () => {
  return (
    <div className="flex h-full flex-col items-center justify-center gap-2 text-gray-500">
      <svg
        className="h-12 w-12"
        viewBox="0 0 24 24"
        fill="none"
        stroke="currentColor"
      >
        <path
          strokeLinecap="round"
          strokeLinejoin="round"
          d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
        />
      </svg>
      <p>아직 진행 중인 채팅이 없습니다.</p>
    </div>
  );
};

export default EmptyChatList;
