import { headers } from "next/headers";

interface ServerUser {
  id: string;
  role: string;
}

export async function getServerUserFromToken(): Promise<ServerUser | null> {
  try {
    const headersList = await headers();
    const userData = headersList.get("x-user-data");

    if (!userData) {
      return null;
    }

    const parsedUser = JSON.parse(userData) as ServerUser;

    return parsedUser;
  } catch (error) {
    console.error("Error parsing user data:", error);
    return null;
  }
}

export default getServerUserFromToken;
