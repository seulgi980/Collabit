import TabNavButton from "@/entities/common/ui/TabNavButton";

const ChatNav = () => {
  return (
    <div className="flex w-full justify-evenly border-b border-b-border pb-[14px] pt-3">
      <TabNavButton href="/chat">일반</TabNavButton>
      <TabNavButton href="/survey">프로젝트</TabNavButton>
    </div>
  );
};

export default ChatNav;
