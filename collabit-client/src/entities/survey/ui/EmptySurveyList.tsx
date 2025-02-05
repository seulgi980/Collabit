const EmptySurveyList = () => {
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
          d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z"
        />
      </svg>
      <p>아직 진행 중인 설문이 없습니다.</p>
    </div>
  );
};

export default EmptySurveyList;
