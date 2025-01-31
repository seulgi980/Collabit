import { Button } from "@/shared/ui/button";
import { Input } from "@/shared/ui/input";
import { SearchIcon } from "lucide-react";

interface ProjectInputProps {
  keyword: string;
  setKeyword: (keyword: string) => void;
  handleSearchKeyword: (e: React.FormEvent<HTMLFormElement>) => void;
}

const ProjectInput = ({
  keyword,
  setKeyword,
  handleSearchKeyword,
}: ProjectInputProps) => {
  return (
    <form className="flex w-full items-center" onSubmit={handleSearchKeyword}>
      <span className="z-10 mr-[-40px] flex h-8 w-8 items-center justify-center">
        <SearchIcon className="h-5 w-5 text-gray-400" />
      </span>
      <Input
        placeholder="레포지토리명을 입력하세요."
        className="ml-2 w-full bg-white py-4 pl-[30px] text-xs"
        value={keyword}
        onChange={(e) => setKeyword(e.target.value)}
      />
    </form>
  );
};

export default ProjectInput;
