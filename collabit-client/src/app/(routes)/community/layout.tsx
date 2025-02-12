const CommunityLayout = async ({ children }: { children: React.ReactNode }) => {
  return (
    <>
      <div className="mx-auto flex w-full max-w-5xl flex-col items-center py-5 md:py-10">
        {children}
      </div>
    </>
  );
};

export default CommunityLayout;
