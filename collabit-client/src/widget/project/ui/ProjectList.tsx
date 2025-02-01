"use client";
import { getMockProjectListAPI } from "@/shared/api/mockProject";
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/shared/ui/accordion";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import SurveyResultModal from "@/widget/project/ui/SurveyResultModal";
import SurveySharingModal from "@/widget/project/ui/SurveySharingModal";
import { useSuspenseQuery } from "@tanstack/react-query";
const ProjectList = ({ keyword, sort }: { keyword: string; sort: string }) => {
  const { data } = useSuspenseQuery({
    queryKey: ["projectList", keyword, sort],
    queryFn: () => getMockProjectListAPI({ keyword, sort }),
    //queryFn: () => getProjectListAPI({ keyword, sort }),
  });

  return (
    <Accordion
      type="multiple"
      defaultValue={data?.map((org) => org.organization)}
    >
      {data.map((org) => {
        return (
          <AccordionItem key={org.organization} value={org.organization}>
            <AccordionTrigger className="text-lg font-semibold">
              <div className="flex items-center gap-2">
                <Avatar>
                  <AvatarImage src={org.organizationImage} />
                  <AvatarFallback>
                    {org.organization.slice(0, 2)}
                  </AvatarFallback>
                </Avatar>
                {org.organization}
              </div>
            </AccordionTrigger>
            <AccordionContent>
              <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
                {org.projects.map((project) =>
                  project.isDone ? (
                    <div key={`${org.organization}-${project.code}`}>
                      <SurveyResultModal project={project} />
                    </div>
                  ) : (
                    <div key={`${org.organization}-${project.code}`}>
                      <SurveySharingModal project={project} />
                    </div>
                  ),
                )}
              </div>
            </AccordionContent>
          </AccordionItem>
        );
      })}
    </Accordion>
  );
};

export default ProjectList;
