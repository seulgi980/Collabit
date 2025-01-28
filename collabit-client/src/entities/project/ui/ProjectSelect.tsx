import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";

interface ProjectSelectProps {
  nickname: string;
  organizations: Array<string>;
  selectType: string;
  setSelectType: (selectType: string) => void;
}

const ProjectSelect = ({
  nickname,
  organizations,
  selectType,
  setSelectType,
}: ProjectSelectProps) => {
  return (
    <div>
      <Select onValueChange={setSelectType}>
        <SelectTrigger className="w-[120px] py-4">
          <SelectValue placeholder="select" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value={nickname}>{nickname}</SelectItem>
          {organizations.map((organization, index) => (
            <SelectItem key={index} value={organization}>
              {organization}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>
    </div>
  );
};

export default ProjectSelect;
