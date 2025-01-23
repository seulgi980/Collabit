import { Progress } from "@/shared/ui/progress";

export default function Page() {
  return (
    <div className="flex w-full flex-col items-center justify-center gap-10 py-20">
      <h2 className="text-4xl font-bold">회원가입</h2>
      <Progress className="bg-gray-200" value={20} />
    </div>
  );
}
