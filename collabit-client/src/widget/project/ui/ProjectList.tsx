"use client";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/shared/ui/accordion";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import SurveySharingModal from "@/widget/project/ui/SurveySharingModal";
import { useSuspenseQuery } from "@tanstack/react-query";
import { ProjectListResponse } from "@/shared/types/response/project";
import SurveyResultModal from "@/widget/project/ui/SurveyResultModal";
import { getProjectListAPI } from "@/shared/api/project";

const ProjectList = ({ keyword, sort }: { keyword: string; sort: string }) => {
  const { data } = useSuspenseQuery<ProjectListResponse>({
    queryKey: ["projectList", keyword, sort],
    queryFn: () => getProjectListAPI({ keyword, sort }),
  });

  const projectList: ProjectListResponse = Array.isArray(data) ? data : [];

  if (projectList.length === 0) {
    return <div className="m-10 text-center">프로젝트를 등록해주세요.</div>;
  }

  return (
    <Accordion
      type="multiple"
      defaultValue={projectList.map((org) => org.organization)}
    >
      {projectList.map((org) => (
        <AccordionItem key={org.organization} value={org.organization}>
          <AccordionTrigger className="text-lg font-semibold">
            <div className="flex items-center gap-2">
              <Avatar>
                <AvatarImage src={org.organizationImage} />
                <AvatarFallback>{org.organization.slice(0, 2)}</AvatarFallback>
              </Avatar>
              {org.organization}
            </div>
          </AccordionTrigger>
          <AccordionContent>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
              {org.projects.map((project) =>
                project.done ? (
                  <SurveyResultModal
                    key={`${org.organization}-${project.code}`}
                    project={project}
                    organization={org.organization}
                  />
                ) : (
                  <SurveySharingModal
                    key={`${org.organization}-${project.code}`}
                    project={project}
                    organization={org.organization}
                  />
                ),
              )}
            </div>
          </AccordionContent>
        </AccordionItem>
      ))}
    </Accordion>
  );
};

export default ProjectList;
