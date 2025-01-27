const ChatLayout = ({
  children,
  chatList,
  chatRoom,
}: {
  children: React.ReactNode;
  chatList: React.ReactNode;
  chatRoom: React.ReactNode;
}) => {
  return (
    <>
      {children}
      <div className="flex h-full w-full">
        <div className="w-1/4 min-w-[200px] bg-slate-400">{chatList}</div>
        <div className="w-3/4 bg-gray-100">{chatRoom}</div>
      </div>
    </>
  );
};

export default ChatLayout;
