import { ProjectListResponse } from "../types/response/project";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const getProjectListAPI = async ({
  keyword,
  sort,
}: {
  keyword?: string;
  sort?: string;
}): Promise<ProjectListResponse> => {
  const queryParams = new URLSearchParams();

  if (keyword) queryParams.append("keyword", keyword);
  if (sort) queryParams.append("sort", sort);

  const queryString = queryParams.toString();
  const url = queryString
    ? `${apiUrl}/project?${queryString}`
    : `${apiUrl}/project`;

  try {
    const res = await fetch(url, {
      method: "GET",
    });
    const data = await res.json();
    console.log(data);
    return data;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
